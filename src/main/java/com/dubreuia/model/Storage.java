/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Alexandre DuBreuil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.dubreuia.model;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.serviceContainer.NonInjectable;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@State(name = "SaveActionSettings", storages = {@com.intellij.openapi.components.Storage("saveactions_settings.xml")})
@Service(Service.Level.PROJECT)
public final class Storage implements PersistentStateComponent<Storage> {

    private boolean firstLaunch;
    private Set<Action> actions;
    private Set<String> exclusions;
    private Set<String> inclusions;
    private String configurationPath;
    private List<String> quickLists;

    @NonInjectable
    public Storage() {
        firstLaunch = true;
        actions = new HashSet<>();
        exclusions = new HashSet<>();
        inclusions = new HashSet<>();
        configurationPath = null;
        quickLists = new ArrayList<>();
    }
    
    @NonInjectable
    public Storage(Storage storage) {
        firstLaunch = storage.firstLaunch;
        actions = new HashSet<>(storage.actions);
        exclusions = new HashSet<>(storage.exclusions);
        inclusions = new HashSet<>(storage.inclusions);
        configurationPath = storage.configurationPath;
        quickLists = new ArrayList<>(storage.quickLists);
    }

    @Override
    public Storage getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Storage state) {
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

    public String getConfigurationPath() {
        return configurationPath;
    }

    public void setConfigurationPath(String configurationPath) {
        this.configurationPath = configurationPath;
    }

    public List<String> getQuickLists() {
        return quickLists;
    }

    public void setQuickLists(List<String> quickLists) {
        this.quickLists = quickLists;
    }

    public void clear() {
        firstLaunch = true;
        actions.clear();
        exclusions.clear();
        inclusions.clear();
        configurationPath = null;
        quickLists.clear();
    }

}
