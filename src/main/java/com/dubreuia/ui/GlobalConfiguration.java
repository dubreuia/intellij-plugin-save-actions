package com.dubreuia.ui;

import com.dubreuia.model.GlobalStorage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;

public class GlobalConfiguration extends Configuration implements Configurable {

    private static final String TEXT_DISPLAY_NAME = "Save Actions Global Settings";

    public GlobalConfiguration() {
        super(ConfigurationType.GLOBAL, () -> ServiceManager.getService(GlobalStorage.class));
    }

    @Nls
    @Override
    public String getDisplayName() {
        return TEXT_DISPLAY_NAME;
    }

}
