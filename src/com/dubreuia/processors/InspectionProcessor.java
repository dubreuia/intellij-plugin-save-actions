package com.dubreuia.processors;

import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.intellij.codeInspection.*;
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

    private final Storage storage;

    private final Action action;

    private final LocalInspectionTool inspectionTool;

    InspectionProcessor(Project project, PsiFile psiFile, Storage storage, Action action, LocalInspectionTool inspectionTool) {
        this.project = project;
        this.psiFile = psiFile;
        this.storage = storage;
        this.action = action;
        this.inspectionTool = inspectionTool;
    }

    @Override
    public void writeToFile() {
        if (storage.isEnabled(action)) {
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    new InspectionWriteQuickFixesAction(project).execute();
                }
            });
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(inspectionTool.getID(), storage.isEnabled(action));
    }

    private class InspectionWriteQuickFixesAction extends WriteCommandAction.Simple {

        protected InspectionWriteQuickFixesAction(Project project, PsiFile... files) {
            super(project, files);
        }

        @Override
        protected void run() {
            InspectionManager inspectionManager = InspectionManager.getInstance(project);
            GlobalInspectionContext context = inspectionManager.createNewGlobalContext(false);
            InspectionToolWrapper toolWrapper = new LocalInspectionToolWrapper(inspectionTool);
            List<ProblemDescriptor> problemDescriptors = InspectionEngine.runInspectionOnFile(psiFile, toolWrapper, context);
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
