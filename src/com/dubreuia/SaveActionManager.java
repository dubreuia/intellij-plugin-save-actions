package com.dubreuia;

import com.dubreuia.processors.ProcessorFactory;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
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

    private final Settings settings = ServiceManager.getService(Settings.class);

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
                !isPsiFileExcluded(project, psiFile, settings.getExclusions());
    }

    private void processPsiFile(final Project project, final PsiFile psiFile) {
        final List<AbstractLayoutCodeProcessor> processors =
                ProcessorFactory.INSTANCE.getSaveActionsProcessors(project, psiFile, settings);
        LOGGER.debug("Running processors " + processors + ", file " + psiFile + ", project " + project);
        for (AbstractLayoutCodeProcessor processor : processors) {
            if (processor != null) {
                processor.run();
            }
        }
    }

}
