package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;

public class ActivationToggleAction extends ToggleAction implements DumbAware {
    
    @Override
    public boolean isSelected(AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            Storage storage = ServiceManager.getService(project, Storage.class);
            return storage.isEnabled(Action.activate);
        }
        return false;
    }

    @Override
    public void setSelected(AnActionEvent e, boolean state) {
        Project project = e.getProject();
        if (project != null) {
            Storage storage = ServiceManager.getService(project, Storage.class);
            storage.setEnabled(Action.activate, state);
        }
    }
}
