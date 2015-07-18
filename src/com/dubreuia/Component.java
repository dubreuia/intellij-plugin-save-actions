package com.dubreuia;

import com.intellij.AppTopics;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class Component implements ApplicationComponent {

    private static final String COMPONENT_NAME = "Save Actions";

    public void initComponent() {
        final MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        final MessageBusConnection connection = bus.connect();
        connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC, getFileDocumentManagerAdapter());
    }

    public void disposeComponent() {
    }

    private FileDocumentManagerAdapter getFileDocumentManagerAdapter() {
        return new SaveActionFileDocumentManager();
    }

    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

}
