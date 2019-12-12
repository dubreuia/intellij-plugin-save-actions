package com.dubreuia.core.component;

import com.dubreuia.processors.BuildProcessor;
import com.dubreuia.processors.GlobalProcessor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.StorageFactory.DEFAULT;
import static com.intellij.AppTopics.FILE_DOCUMENT_SYNC;

/**
 * The plugin entry class that instanciates (or reuse) and delegates to {@link SaveActionManager}. This is not a
 * singleton, for java based ide the corresponding component will also get instanciated (check {@link JavaComponent}).
 *
 * @see SaveActionManager
 */
public class Component implements ApplicationComponent {

    public static final String COMPONENT_NAME = "Save Actions";

    @Override
    public void initComponent() {
        LOGGER.info("Starting component: " + COMPONENT_NAME);

        var manager = SaveActionManager.getInstance();
        manager.setStorageFactory(DEFAULT);
        manager.addProcessors(BuildProcessor.stream());
        manager.addProcessors(GlobalProcessor.stream());

        var bus = ApplicationManager.getApplication().getMessageBus();
        var connection = bus.connect();
        connection.subscribe(FILE_DOCUMENT_SYNC, manager);
    }

    @Override
    public void disposeComponent() {
        LOGGER.info("Stopping component: " + COMPONENT_NAME);
    }

    @NotNull
    @Override
    public String getComponentName() {
        return COMPONENT_NAME;
    }

}
