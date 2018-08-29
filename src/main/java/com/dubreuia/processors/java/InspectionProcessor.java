package com.dubreuia.processors.java;

import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.intellij.codeInspection.GlobalInspectionContext;
import com.intellij.codeInspection.InspectionEngine;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.QuickFix;
import com.intellij.codeInspection.ex.InspectionToolWrapper;
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.List;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;

class InspectionProcessor implements Processor {

    private final Project project;
    private final PsiFile psiFile;
    private final Storage storage;
    private final Action action;
    private final LocalInspectionTool inspectionTool;

    InspectionProcessor(
            Project project,
            PsiFile psiFile,
            Storage storage,
            Action action,
            LocalInspectionTool inspectionTool) {
        this.project = project;
        this.psiFile = psiFile;
        this.storage = storage;
        this.action = action;
        this.inspectionTool = inspectionTool;
    }

    @Override
    public void run() {
        if (storage.isEnabled(action)) {
            ApplicationManager.getApplication()
                    .invokeLater(() -> new InspectionWriteQuickFixesAction(project, psiFile).execute());
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String toString() {
        return toString(inspectionTool.getID(), storage.isEnabled(action));
    }

    private class InspectionWriteQuickFixesAction extends WriteCommandAction.Simple {

        private InspectionWriteQuickFixesAction(Project project, PsiFile... files) {
            super(project, files);
        }

        @Override
        protected void run() {
            InspectionManager inspectionManager = InspectionManager.getInstance(project);
            GlobalInspectionContext context = inspectionManager.createNewGlobalContext(false);
            InspectionToolWrapper toolWrapper = new LocalInspectionToolWrapper(inspectionTool);
            List<ProblemDescriptor> problemDescriptors;
            try {
                problemDescriptors = InspectionEngine.runInspectionOnFile(psiFile, toolWrapper, context);
            } catch (IndexNotReadyException exception) {
                return;
            }
            for (ProblemDescriptor problemDescriptor : problemDescriptors) {
                QuickFix[] fixes = problemDescriptor.getFixes();
                if (fixes != null) {
                    writeQuickFixes(problemDescriptor, fixes);
                }
            }
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

}
