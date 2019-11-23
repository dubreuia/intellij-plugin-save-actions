package com.dubreuia.model;

import com.dubreuia.model.java.EpfStorage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.function.Function;

public enum StorageFactory {

    DEFAULT(project -> {
        Storage globalStorage = ServiceManager.getService(project, GlobalStorage.class);
        if (globalStorage.isEnabled(Action.activateProjectConfiguration)) {
            return ServiceManager.getService(project, ProjectStorage.class);
        }
        return globalStorage;
    }),

    JAVA(project -> {
        Storage defaultStorage = DEFAULT.getStorage(project);
        return EpfStorage.INSTANCE.getStorageOrDefault(defaultStorage.getConfigurationPath(), defaultStorage);
    });

    private final Function<Project, Storage> provider;

    StorageFactory(Function<Project, Storage> provider) {
        this.provider = provider;
    }

    public Storage getStorage(Project project) {
        return provider.apply(project);
    }

}
