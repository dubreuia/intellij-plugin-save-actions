/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Alexandre DuBreuil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.dubreuia.processors.java;

import com.intellij.codeInspection.GlobalInspectionContext;
import com.intellij.codeInspection.InspectionEngine;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.QuickFix;
import com.intellij.codeInspection.ex.InspectionToolWrapper;
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;

/**
 * Implements a runnable for inspections commands.
 */
class InspectionRunnable implements Runnable {

    private final Project project;
    private final Set<PsiFile> psiFiles;
    private final LocalInspectionTool inspectionTool;

    InspectionRunnable(Project project, Set<PsiFile> psiFiles, LocalInspectionTool inspectionTool) {
        this.project = project;
        this.psiFiles = psiFiles;
        this.inspectionTool = inspectionTool;
    }

    @Override
    public void run() {
        InspectionManager inspectionManager = InspectionManager.getInstance(project);
        GlobalInspectionContext context = inspectionManager.createNewGlobalContext();
        InspectionToolWrapper toolWrapper = new LocalInspectionToolWrapper(inspectionTool);
        for (PsiFile psiFile : psiFiles) {
            List<ProblemDescriptor> problemDescriptors = getProblemDescriptors(context, toolWrapper, psiFile);
            for (ProblemDescriptor problemDescriptor : problemDescriptors) {
                QuickFix[] fixes = problemDescriptor.getFixes();
                if (fixes != null) {
                    writeQuickFixes(problemDescriptor, fixes);
                }
            }
        }
    }

    private List<ProblemDescriptor> getProblemDescriptors(GlobalInspectionContext context,
                                                          InspectionToolWrapper toolWrapper,
                                                          PsiFile psiFile) {
        List<ProblemDescriptor> problemDescriptors;
        try {
            problemDescriptors = InspectionEngine.runInspectionOnFile(psiFile, toolWrapper, context);
        } catch (IndexNotReadyException exception) {
            LOGGER.info("Cannot inspect files: index not ready (" + exception.getMessage() + ")");
            return Collections.emptyList();
        }
        return problemDescriptors;
    }

    private void writeQuickFixes(ProblemDescriptor problemDescriptor, QuickFix[] fixes) {
        for (QuickFix fix : fixes) {
            @SuppressWarnings("unchecked")
            QuickFix<ProblemDescriptor> typedFix = (QuickFix<ProblemDescriptor>) fix;
            if (fix != null) {
                try {
                    typedFix.applyFix(project, problemDescriptor);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

}
