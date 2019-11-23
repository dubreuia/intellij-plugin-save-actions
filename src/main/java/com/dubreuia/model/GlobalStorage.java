package com.dubreuia.model;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@State(name = "SaveActionGlobalSettings",
        storages = {@com.intellij.openapi.components.Storage("saveactions_global_settings.xml")})
public class GlobalStorage extends Storage implements PersistentStateComponent<GlobalStorage> {

    @Override
    public GlobalStorage getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GlobalStorage state) {
        stopFirstLaunch();
        XmlSerializerUtil.copyBean(state, this);
    }

}
