package com.dubreuia.model;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;

import java.util.HashSet;
import java.util.Set;

@State(name = "SaveActionSettings",
        storages = {@com.intellij.openapi.components.Storage(file = "./saveactions_settings.xml")})
public class Storage implements PersistentStateComponent<Storage> {

    private Set<Action> actions = new HashSet<Action>();

    private Set<String> exclusions = new HashSet<String>();
    private Set<String> inclusions = new HashSet<String>();

    private boolean firstLaunch = true;

    private String configurationPath;

    public String getConfigurationPath() {
        return configurationPath;
    }

    public void setConfigurationPath(String configurationPath) {
        this.configurationPath = configurationPath;
    }

    public Storage getState() {
        return this;
    }

    public void loadState(Storage state) {
        firstLaunch = false;
        XmlSerializerUtil.copyBean(state, this);
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }

    public Set<String> getExclusions() {
        return exclusions;
    }

    public void setExclusions(Set<String> exclusions) {
        this.exclusions = exclusions;
    }

    public boolean isEnabled(Action action) {
        return actions.contains(action);
    }

    public void setEnabled(Action action, boolean enable) {
        if (enable) {
            actions.add(action);
        } else {
            actions.remove(action);
        }
    }

    public Set<String> getInclusions() {
        return inclusions;
    }

    public void setInclusions(Set<String> inclusions) {
        this.inclusions = inclusions;
    }

    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    public void stopFirstLaunch() {
        firstLaunch = false;
    }

}
