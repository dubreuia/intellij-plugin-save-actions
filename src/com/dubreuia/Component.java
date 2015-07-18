package com.dubreuia;

import com.dubreuia.processors.ProcessorFactory;
import com.dubreuia.utils.PsiFiles;
import com.intellij.AppTopics;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.impl.IdeFrameImpl;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Component implements ApplicationComponent {

    private static final String COMPONENT_NAME = "Save Actions";

    private Settings settings = ServiceManager.getService(Settings.class);

    public void initComponent() {
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();
        connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC, getFileDocumentManagerAdapter());
    }

    private FileDocumentManagerAdapter getFileDocumentManagerAdapter() {
        return new FileDocumentManagerAdapter() {

            @Override
            public void beforeDocumentSaving(@NotNull Document document) {
                if (!isManuallyTriggered() && isActive(document)) {
                    return;
                }

                for (Project project : ProjectManager.getInstance().getOpenProjects()) {
                    PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
                    if (isPsiFileEligible(project, psiFile)) {
                        List<AbstractLayoutCodeProcessor> processors =
                                ProcessorFactory.INSTANCE.getSaveActionsProcessors(project, psiFile, settings);
                        for (AbstractLayoutCodeProcessor processor : processors) {
                            if (null != processor) {
                                processor.run();
                            }
                        }
                    }
                }
            }

            private boolean isManuallyTriggered() {
                return SaveAllAction.TRIGGERED;
            }

            private boolean isActive(@NotNull Document document) {
                IdeFrame activeFrame = (IdeFrame) IdeFrameImpl.getActiveFrame();
                return activeFrame != null && activeFrame.getProject() != null &&
                        isActive(activeFrame.getProject(), document);
            }

            private boolean isActive(@NotNull Project project, @NotNull Document document) {
                Editor selectedTextEditor = FileEditorManagerEx.getInstance(project).getSelectedTextEditor();
                return selectedTextEditor != null && selectedTextEditor.getDocument() == document;
            }
        };
    }

    /**
     * The psi files seems to be shared between projects, so we need to check if the file is physically
     * in that project before reformating, or else the file is formatted twice and intellij will ask to
     * confirm unlocking of non-project file in the other project.
     */
    private boolean isPsiFileEligible(Project project, PsiFile psiFile) {
        return null != psiFile &&
                PsiFiles.isPsiFilePhysicallyInProject(project, psiFile) &&
                !PsiFiles.isPsiFileExcluded(project, psiFile, settings.getExclusions());
    }

    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    public void disposeComponent() {
    }

}
