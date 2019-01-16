package com.dubreuia.core.component;

import com.dubreuia.processors.BuildProcessor;
import com.dubreuia.processors.GlobalProcessor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.intellij.AppTopics.FILE_DOCUMENT_SYNC;

/**
 * The plugin entry class. This is not a singleton, for java based ide the corresponding component will also get
 * instanciated (check {@link JavaComponent}).
 */
public class Component implements ApplicationComponent {

    public static final String COMPONENT_NAME = "Save Actions";

    @Override
    public void initComponent() {
        // TODO log
        LOGGER.info("STARTING " + COMPONENT_NAME);

        SaveActionManager manager = SaveActionManager.getInstance();
        manager.addProcessors(BuildProcessor.stream());
        manager.addProcessors(GlobalProcessor.stream());

        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();
        connection.subscribe(FILE_DOCUMENT_SYNC, manager);
    }

    @Override
    public void disposeComponent() {
        // TODO log
        LOGGER.info("STOPPING " + COMPONENT_NAME);
    }

    @NotNull
    @Override
    public String getComponentName() {
        return COMPONENT_NAME;
    }

}
