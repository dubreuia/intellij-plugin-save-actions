package com.dubreuia.model;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@State(name = "SaveActionGlobalSettings",
        storages = {@com.intellij.openapi.components.Storage("saveactions_global_settings.xml")})
public class GlobalStorage extends Storage implements PersistentStateComponent<GlobalStorage> {

    public GlobalStorage() {
        super();
    }

    public GlobalStorage(Storage storage) {
        super(storage);
    }

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
