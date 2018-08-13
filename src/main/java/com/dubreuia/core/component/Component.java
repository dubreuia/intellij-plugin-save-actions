package com.dubreuia.core.component;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import static com.intellij.AppTopics.FILE_DOCUMENT_SYNC;

/**
 * The plugin entry class. This is not a singleton, for java based ide the corresponding component will also get
 * instanciated (check {@link com.dubreuia.core.component.java.Component}).
 */
public class Component implements ApplicationComponent {

    public static final String COMPONENT_NAME = "Save Actions";

    @Override
    public void initComponent() {
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();
        connection.subscribe(FILE_DOCUMENT_SYNC, getSaveActionManager());
    }

    @NotNull
    protected SaveActionManager getSaveActionManager() {
        return new SaveActionManager();
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

}
