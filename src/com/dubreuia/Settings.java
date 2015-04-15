package com.dubreuia;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;

import java.util.HashSet;
import java.util.Set;

@State(name = "SaveActionSettings",
        storages = {
                @Storage(file = StoragePathMacros.APP_CONFIG + "/saveactions_settings.xml")})
public class Settings implements PersistentStateComponent<Settings> {

    private boolean activate = true;

    private boolean imports = true;

    private boolean reformat = true;

    private boolean changedCode = true;

    private boolean rearrange = false;

    private Set<String> exclusions = new HashSet<String>();

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

    public boolean isChangedCode() {
        return changedCode;
    }

    public void setChangedCode(boolean changedCode) {
        this.changedCode = changedCode;
    }

    public boolean isRearrange() {
        return rearrange;
    }

    public void setRearrange(boolean rearrange) {
        this.rearrange = rearrange;
    }

    public Set<String> getExclusions() {
        return exclusions;
    }

    public void setExclusions(Set<String> exclusions) {
        this.exclusions = exclusions;
    }

}