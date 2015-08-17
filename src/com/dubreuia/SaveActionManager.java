package com.dubreuia;

import com.dubreuia.model.Storage;
import com.dubreuia.processors.ProcessorFactory;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.codeInspection.ExplicitTypeCanBeDiamondInspection;
import com.intellij.codeInspection.GlobalInspectionContext;
import com.intellij.codeInspection.InspectionEngine;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.QuickFix;
import com.intellij.codeInspection.ex.InspectionToolWrapper;
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper;
import com.intellij.codeInspection.localCanBeFinal.LocalCanBeFinal;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.siyeh.ig.classlayout.FinalPrivateMethodInspection;
import com.siyeh.ig.maturity.SuppressionAnnotationInspection;
import com.siyeh.ig.style.FieldMayBeFinalInspection;
import com.siyeh.ig.style.UnnecessarySemicolonInspection;
import com.siyeh.ig.style.UnqualifiedFieldAccessInspection;
import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dubreuia.utils.Documents.isDocumentActive;
import static com.dubreuia.utils.PsiFiles.isPsiFileExcluded;
import static com.dubreuia.utils.PsiFiles.isPsiFilePhysicallyInProject;

public class SaveActionManager extends FileDocumentManagerAdapter {

    public static final Logger LOGGER = Logger.getInstance(SaveActionManager.class);

    static {
        LOGGER.setLevel(Level.DEBUG);
    }

    private final Storage storage = ServiceManager.getService(Storage.class);

    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        if (!SaveAllAction.TRIGGERED && isDocumentActive(document)) {
            LOGGER.debug("Document " + document + " is still active, do not execute");
            return;
        }
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (isPsiFileEligible(project, psiFile)) {
                processPsiFile(project, psiFile);
                processPsiFile2(project, psiFile);
            }
        }
    }


    /**
     * The psi files seems to be shared between projects, so we need to check if the file is physically
     * in that project before reformating, or else the file is formatted twice and intellij will ask to
     * confirm unlocking of non-project file in the other project.
     */
    private boolean isPsiFileEligible(Project project, PsiFile psiFile) {
        return psiFile != null &&
                isPsiFilePhysicallyInProject(project, psiFile) &&
                !isPsiFileExcluded(project, psiFile, storage.getExclusions());
    }

    private void processPsiFile(final Project project, final PsiFile psiFile) {
        final List<AbstractLayoutCodeProcessor> processors =
                ProcessorFactory.INSTANCE.getSaveActionsProcessors(project, psiFile, storage);
        LOGGER.debug("Running processors " + processors + ", file " + psiFile + ", project " + project);
        for (AbstractLayoutCodeProcessor processor : processors) {
            if (processor != null) {
                processor.run();
            }
        }
    }

    private void processPsiFile2(final Project project, final PsiFile psiFile) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                new WriteCommandAction.Simple(project) {
                    @Override
                    protected void run() throws Throwable {
                        InspectionManager inspectionManager = InspectionManager.getInstance(project);
                        GlobalInspectionContext context = inspectionManager.createNewGlobalContext(false);

                        LocalInspectionTool tool1 = new LocalCanBeFinal();
                        ex(context, tool1, psiFile, project);

                        LocalInspectionTool tool3 = new ExplicitTypeCanBeDiamondInspection();
                        ex(context, tool3, psiFile, project);

                        LocalInspectionTool tool5 = new UnqualifiedFieldAccessInspection();
                        ex(context, tool5, psiFile, project);

                        LocalInspectionTool tool6 = new SuppressionAnnotationInspection();
                        ex(context, tool6, psiFile, project);

                        LocalInspectionTool tool7 = new FinalPrivateMethodInspection();
                        ex(context, tool7, psiFile, project);

                        LocalInspectionTool tool8 = new UnnecessarySemicolonInspection();
                        ex(context, tool8, psiFile, project);

                        LocalInspectionTool tool9 = new FieldMayBeFinalInspection();
                        ex(context, tool9, psiFile, project);
                    }
                }.execute();
            }
        });
    }

    private void ex(GlobalInspectionContext context, LocalInspectionTool tool, PsiFile psiFile, Project project) {
//        new GlobalInspectionToolWrapper()
        InspectionToolWrapper toolWrapper = new LocalInspectionToolWrapper(tool);
        List<ProblemDescriptor> problemDescriptors = InspectionEngine.runInspectionOnFile(psiFile, toolWrapper, context);
        for (ProblemDescriptor problemDescriptor : problemDescriptors) {
            QuickFix[] fixes = problemDescriptor.getFixes();
            if (fixes != null) {
                for (QuickFix fix : fixes) {
                    if (fix != null) {
                        fix.applyFix(project, problemDescriptor);
                    }
                }
            }
        }
    }

}