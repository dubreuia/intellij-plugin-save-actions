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
        GlobalInspectionContext context = inspectionManager.createNewGlobalContext(false);
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
