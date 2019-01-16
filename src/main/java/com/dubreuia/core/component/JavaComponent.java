package com.dubreuia.core.component;

import com.dubreuia.processors.java.JavaProcessor;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.StorageFactory.JAVA;

/**
 * The plugin entry class for java based ide. This is not a singleton, the parent is also instanciated.
 */
public class JavaComponent implements ApplicationComponent {

    private static final String COMPONENT_NAME = "Save Actions Java";

    @Override
    public void initComponent() {
        // TODO log
        LOGGER.info("STARTING " + COMPONENT_NAME);

        SaveActionManager manager = SaveActionManager.getInstance();
        manager.setJavaAvailable(true);
        manager.addProcessors(JavaProcessor.stream());
        manager.setStorageFactory(JAVA);
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
