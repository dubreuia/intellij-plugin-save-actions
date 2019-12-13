package com.dubreuia.core.component;

import com.dubreuia.processors.java.JavaProcessor;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.StorageFactory.JAVA;

/**
 * The plugin entry class for java based ide. This is not a singleton, the main component {@link Component} is also
 * instanciated before, this one is instanciated after.
 *
 * @see SaveActionManager
 */
public class JavaComponent implements ApplicationComponent {

    private static final String COMPONENT_NAME = "Save Actions Java";

    @Override
    public void initComponent() {
        LOGGER.info("Starting component: " + COMPONENT_NAME);

        SaveActionManager manager = SaveActionManager.getInstance();
        manager.setStorageFactory(JAVA);
        manager.enableJava();
        manager.addProcessors(JavaProcessor.stream());
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
