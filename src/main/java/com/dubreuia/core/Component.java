package com.dubreuia.core;

import com.intellij.AppTopics;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

/**
 * The plugin entry class. This is not a singleton, for java based ide the corresponding component will also get
 * instanciated (check {@link com.dubreuia.core.java.Component}).
 */
public class Component implements ApplicationComponent {

    private static final String COMPONENT_NAME = "Save Actions";

    public void initComponent() {
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();
        connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC, getSaveActionManager());
    }

    @NotNull
    protected SaveActionManager getSaveActionManager() {
        return new SaveActionManager();
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

}
