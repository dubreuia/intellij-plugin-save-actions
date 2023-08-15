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

import com.dubreuia.core.service.SaveActionsService;
import com.intellij.codeInspection.GlobalInspectionContext;
import com.intellij.codeInspection.InspectionEngine;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionEP;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.QuickFix;
import com.intellij.codeInspection.ex.InspectionToolWrapper;
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implements a runnable for inspections commands.
 */
class InspectionRunnable implements Runnable {

    private static final Logger LOGGER = Logger.getInstance(SaveActionsService.class);

    private final Project project;
    private final Set<PsiFile> psiFiles;
    private final InspectionToolWrapper<LocalInspectionTool, LocalInspectionEP> toolWrapper;

    InspectionRunnable(Project project, Set<PsiFile> psiFiles, LocalInspectionTool inspectionTool) {
        this.project = project;
        this.psiFiles = psiFiles;
        toolWrapper = new LocalInspectionToolWrapper(inspectionTool);
        LOGGER.info(String.format("Running inspection for %s", inspectionTool.getShortName()));
    }

    @Override
    public void run() {
        InspectionManager inspectionManager = InspectionManager.getInstance(project);
        GlobalInspectionContext context = inspectionManager.createNewGlobalContext();
        psiFiles.forEach(pf -> getProblemDescriptors(context, pf).forEach(this::writeQuickFixes));
    }

    private List<ProblemDescriptor> getProblemDescriptors(
            GlobalInspectionContext context,
            PsiFile psiFile) {
        try {
            return InspectionEngine.runInspectionOnFile(psiFile, toolWrapper, context);
        } catch (IndexNotReadyException exception) {
            LOGGER.info(String.format("Cannot inspect file %s: index not ready (%s)",
                    psiFile.getName(),
                    exception.getMessage()));
            return Collections.emptyList();
        }
    }

    @SuppressWarnings({"unchecked", "squid:S1905", "squid:S3740"})
    private void writeQuickFixes(ProblemDescriptor problemDescriptor) {
        QuickFix<?>[] fixes = problemDescriptor.getFixes();
        if (fixes == null) {
            return;
        }

        Set<QuickFix<ProblemDescriptor>> quickFixes = Arrays.stream(fixes)
                .filter(Objects::nonNull)
                .map(qf -> (QuickFix<ProblemDescriptor>) qf)
                .collect(Collectors.toSet());

        for (QuickFix<ProblemDescriptor> typedFix : quickFixes) {
            try {
                LOGGER.info(String.format("Applying fix \"%s\"", typedFix.getName()));
                typedFix.applyFix(project, problemDescriptor);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

}
