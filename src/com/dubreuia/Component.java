package com.dubreuia;

import com.dubreuia.processors.ProcessorFactory;
import com.dubreuia.utils.PsiFiles;
import com.intellij.AppTopics;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// for more info/help, see: https://confluence.jetbrains.com/display/IDEADEV/Plugin+Development+FAQ
// more useful: https://code.google.com/p/ide-examples/wiki/IntelliJIdeaPsiCookbook
public
class Component implements ApplicationComponent {
    private static final String COMPONENT_NAME = "Save Actions";

    private final Settings settings = ServiceManager.getService(Settings.class);

    public
    void initComponent() {
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();
        connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC, getFileDocumentManagerAdapter());
    }

    public
    void disposeComponent() {
    }

    private
    FileDocumentManagerAdapter getFileDocumentManagerAdapter() {
        return new FileDocumentManagerAdapter() {

            @Override
            public
            void beforeDocumentSaving(@NotNull Document document) {
                if (!SaveAllAction.TRIGGERED) {
                    return;
                }

                // only process the project that is currently active
                final DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResult();
                if (dataContext != null) {
                    final Project project = DataKeys.PROJECT.getData(dataContext);
                    final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);

                    if (isPsiFileEligible(project, psiFile)) {
                        processPsiFile(project, psiFile);
                    }
                }
            }

            private
            void processPsiFile(final Project project, final PsiFile psiFile) {
                List<AbstractLayoutCodeProcessor> processors = ProcessorFactory.INSTANCE.getSaveActionsProcessors(project, psiFile,
                                                                                                                  settings);
                for (AbstractLayoutCodeProcessor processor : processors) {
                    if (processor != null) {
                        processor.run();
                    }
                }
            }

            /**
             * The psi files seems to be shared between projects, so we need to check if the file is physically
             * in that project before reformatting, or else the file is formatted twice and intellij will ask to
             * confirm unlocking of non-project file in the other project.
             */
            private
            boolean isPsiFileEligible(Project project, PsiFile psiFile) {
                return psiFile != null && !PsiFiles.isPsiFileExcluded(project, psiFile, settings.getExclusions());
            }
        };
    }

    @NotNull
    public
    String getComponentName() {
        return COMPONENT_NAME;
    }

}
