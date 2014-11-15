package com.dubreuia;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(name = "SaveActionSettings",
        storages = {@Storage(file = StoragePathMacros.APP_CONFIG + "/saveactions_settings.xml")})
public class Settings implements PersistentStateComponent<Settings> {

    private boolean activate;
    private boolean imports;
    private boolean reformat;
    private boolean reformatChangedCode;
    private boolean rearrange;

    public Settings getState() {
        return this;
    }

    public void loadState(Settings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public boolean isImports() {
        return imports;
    }

    public void setImports(boolean imports) {
        this.imports = imports;
    }

    public boolean isReformat() {
        return reformat;
    }

    public void setReformat(boolean reformat) {
        this.reformat = reformat;
    }

    public boolean isReformatChangedCode() {
        return reformatChangedCode;
    }

    public void setReformatChangedCode(boolean reformatChangedCode) {
        this.reformatChangedCode = reformatChangedCode;
    }

    public boolean isRearrange() {
        return rearrange;
    }

    public void setRearrange(boolean rearrange) {
        this.rearrange = rearrange;
    }

}

