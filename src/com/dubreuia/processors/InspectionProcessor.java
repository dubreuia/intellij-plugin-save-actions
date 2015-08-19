package com.dubreuia.processors;

import com.dubreuia.model.Action;
import com.dubreuia.model.StorageRO;
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
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.List;

import static com.dubreuia.processors.ProcessorMessage.toStringBuilder;

class InspectionProcessor implements Processor {

    private final Project project;

    private final PsiFile psiFile;

    private final StorageRO storage;

    private final Action action;

    private final LocalInspectionTool inspectionTool;

    InspectionProcessor(final Project project, final PsiFile psiFile, final StorageRO storage, final Action action,
                        final LocalInspectionTool inspectionTool) {
        this.project = project;
        this.psiFile = psiFile;
        this.storage = storage;
        this.action = action;
        this.inspectionTool = inspectionTool;
    }

    @Override
    public void writeToFile() {
        if (storage.isEnabled(action)) {
            ApplicationManager.getApplication().invokeLater(new InspectionRunnable());
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(inspectionTool.getID(), storage.isEnabled(action));
    }

    private class InspectionRunnable implements Runnable {

        @Override
        public void run() {
            new InspectionWriteQuickFixesAction(project).execute();
        }

    }

    private class InspectionWriteQuickFixesAction extends WriteCommandAction.Simple {

        protected InspectionWriteQuickFixesAction(Project project, PsiFile... files) {
            super(project, files);
        }

        @Override
        protected void run() {
            final InspectionManager inspectionManager = InspectionManager.getInstance(project);
            final GlobalInspectionContext context = inspectionManager.createNewGlobalContext(false);
            final InspectionToolWrapper toolWrapper = new LocalInspectionToolWrapper(inspectionTool);
            final List<ProblemDescriptor> problemDescriptors = InspectionEngine.runInspectionOnFile(psiFile, toolWrapper, context);
            for (ProblemDescriptor problemDescriptor : problemDescriptors) {
                QuickFix[] fixes = problemDescriptor.getFixes();
                if (fixes != null) {
                    writeQuickFixes(problemDescriptor, fixes);
                }
            }
        }

        private void writeQuickFixes(ProblemDescriptor problemDescriptor, QuickFix[] fixes) {
            for (QuickFix fix : fixes) {
                if (fix != null) {
                    fix.applyFix(project, problemDescriptor);
                }
            }
        }
    }

}
