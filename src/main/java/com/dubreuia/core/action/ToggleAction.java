package com.dubreuia.core.action;

import com.dubreuia.model.Storage;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;

import static com.dubreuia.model.Action.activate;

/**
 * This action toggles on and off the plugin, by modifying the underlying storage.
 */
public class ToggleAction extends com.intellij.openapi.actionSystem.ToggleAction implements DumbAware {

    @Override
    public boolean isSelected(AnActionEvent event) {
        var project = event.getProject();
        if (project != null) {
            var storage = ServiceManager.getService(project, Storage.class);
            return storage.isEnabled(activate);
        }
        return false;
    }

    @Override
    public void setSelected(AnActionEvent event, boolean state) {
        var project = event.getProject();
        if (project != null) {
            var storage = ServiceManager.getService(project, Storage.class);
            storage.setEnabled(activate, state);
        }
    }

}
