package com.dubreuia.ui;

import com.dubreuia.model.ConfigurationType;
import com.dubreuia.model.ProjectStorage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;

public class ProjectConfiguration extends Configuration implements Configurable {

    public ProjectConfiguration(Project project) {
        super(ConfigurationType.PROJECT, () -> ServiceManager.getService(project, ProjectStorage.class));
    }

    @Nls
    @Override
    public String getDisplayName() {
        return ParentConfiguration.PROJECT_SETTINGS;
    }

}
