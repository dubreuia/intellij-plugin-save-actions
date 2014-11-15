package com.dubreuia;

import com.intellij.AppTopics;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.codeInsight.actions.OptimizeImportsProcessor;
import com.intellij.codeInsight.actions.RearrangeCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class Component implements ApplicationComponent {

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
                for (Project project : ProjectManager.getInstance().getOpenProjects()) {
                    PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
                    if (null != psiFile) {
                        AbstractLayoutCodeProcessor processor;
                        processor = getReformatCodeProcessor(project, psiFile);
                        processor = getRearrangeCodeProcessor(processor);
                        processor = getOptimizeImportsProcessor(processor, project, psiFile);
                        if (null != processor) {
                            processor.run();
                        }
                    }
                }
            }
        };
    }

    private AbstractLayoutCodeProcessor getOptimizeImportsProcessor(AbstractLayoutCodeProcessor processor, Project project, PsiFile psiFile) {
        if (null != processor && settings.isImports()) {
            return new OptimizeImportsProcessor(processor);
        } else if (settings.isImports()) {
            return new OptimizeImportsProcessor(project, psiFile);
        }
        return processor;
    }

    private AbstractLayoutCodeProcessor getRearrangeCodeProcessor(AbstractLayoutCodeProcessor processor) {
        if (null != processor && settings.isRearrange()) {
            return new RearrangeCodeProcessor(processor, null);
        }
        return processor;
    }

    private AbstractLayoutCodeProcessor getReformatCodeProcessor(Project project, PsiFile psiFile) {
        if (settings.isReformat()) {
            return new ReformatCodeProcessor(project, psiFile, null, settings.isReformatChangedCode());
        }
        return null;
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "Save Action";
    }

}