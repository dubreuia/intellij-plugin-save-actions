package com.dubreuia;

import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.dubreuia.processors.ProcessorFactory;
import com.intellij.ide.IdeEventQueue;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.InvocationEvent;
import java.util.List;

import static com.dubreuia.utils.PsiFiles.isPsiFileExcluded;
import static com.dubreuia.utils.PsiFiles.isPsiFileFocused;

public class SaveActionManager extends FileDocumentManagerAdapter {

    public static final Logger LOGGER = Logger.getInstance(SaveActionManager.class);

    static {
        LOGGER.setLevel(Level.DEBUG);
    }

    private final Storage storage = ServiceManager.getService(Storage.class);

    @Override
    public void beforeDocumentSaving(@NotNull Document document) {
        if (skipEvent(document)) return;

        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            if (isPsiFileEligible(project, psiFile)) {
                processPsiFile(project, psiFile);
            }
        }
    }

    private boolean skipEvent(@NotNull Document document) {
        AWTEvent trueCurrentEvent = IdeEventQueue.getInstance().getTrueCurrentEvent();
        if (trueCurrentEvent instanceof InvocationEvent) {
            if (String.valueOf(trueCurrentEvent).startsWith("java.awt.event.InvocationEvent[INVOCATION_DEFAULT,runnable=LaterInvocator.FlushQueue lastInfo=[runnable: com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor")) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Skipping event: " + document + "  " + ReflectionToStringBuilder.toString(trueCurrentEvent));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * The psi files seems to be shared between projects, so we need to check if the file is physically
     * in that project before reformating, or else the file is formatted twice and intellij will ask to
     * confirm unlocking of non-project file in the other project.
     */
    private boolean isPsiFileEligible(Project project, PsiFile psiFile) {
        return psiFile != null &&
                project.isInitialized() &&
                !project.isDisposed() &&
                ProjectRootManager.getInstance(project).getFileIndex().isInContent(psiFile.getVirtualFile()) &&
                isPsiFileFocused(psiFile) &&
                !isPsiFileExcluded(project, psiFile, storage.getExclusions());
    }

    private void processPsiFile(Project project, PsiFile psiFile) {
        List<Processor> processors = ProcessorFactory.INSTANCE.getSaveActionsProcessors(project, psiFile, storage);
        LOGGER.debug("Running processors " + processors + ", file " + psiFile + ", project " + project);
        for (Processor processor : processors) {
            processor.writeToFile();
        }
    }

}