package com.dubreuia.core;

import com.dubreuia.model.Storage;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;

import static com.dubreuia.model.Action.activate;

public class SaveActionToggleAction extends ToggleAction implements DumbAware {

    @Override
    public boolean isSelected(AnActionEvent event) {
        Project project = event.getProject();
        if (project != null) {
            Storage storage = ServiceManager.getService(project, Storage.class);
            return storage.isEnabled(activate);
        }
        return false;
    }

    @Override
    public void setSelected(AnActionEvent event, boolean state) {
        Project project = event.getProject();
        if (project != null) {
            Storage storage = ServiceManager.getService(project, Storage.class);
            storage.setEnabled(activate, state);
        }
    }

}
