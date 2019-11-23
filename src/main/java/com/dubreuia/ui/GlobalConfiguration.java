package com.dubreuia.ui;

import com.dubreuia.model.ConfigurationType;
import com.dubreuia.model.GlobalStorage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;

public class GlobalConfiguration extends Configuration implements Configurable {

    public GlobalConfiguration() {
        super(ConfigurationType.GLOBAL, () -> ServiceManager.getService(GlobalStorage.class));
    }

    @Nls
    @Override
    public String getDisplayName() {
        return ParentConfiguration.GLOBAL_SETTINGS;
    }

}
