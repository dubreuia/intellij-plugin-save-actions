package com.dubreuia.model;

import com.google.common.annotations.VisibleForTesting;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.serviceContainer.NonInjectable;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@State(name = "SaveActionSettings",
        storages = {@com.intellij.openapi.components.Storage("./saveactions_settings.xml")})
public class ProjectStorage extends Storage implements PersistentStateComponent<ProjectStorage> {

    @VisibleForTesting
    public ProjectStorage() {
        super();
    }

    @NonInjectable
    public ProjectStorage(Storage storage) {
        super(storage);
    }

    @Override
    public ProjectStorage getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProjectStorage state) {
        stopFirstLaunch();
        XmlSerializerUtil.copyBean(state, this);
    }

}
