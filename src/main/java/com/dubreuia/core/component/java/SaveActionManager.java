package com.dubreuia.core.component.java;

import com.dubreuia.model.Storage;
import com.dubreuia.model.epf.EpfStorage;
import com.dubreuia.processors.Processor;
import com.dubreuia.processors.Processor.ProcessorComparator;
import com.dubreuia.processors.java.ProcessorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.List;

/**
 * Event handler class, instanciated by {@link com.dubreuia.core.component.java.Component}. The
 * {@link #getSaveActionsProcessors(Project, PsiFile)} returns the java specific processors.
 */
public class SaveActionManager extends com.dubreuia.core.component.SaveActionManager {

    @Override
    public Storage getStorage(Project project) {
        Storage defaultStorage = super.getStorage(project);
        return EpfStorage.INSTANCE.getStorageOrDefault(defaultStorage.getConfigurationPath(), defaultStorage);
    }

    @Override
    protected List<Processor> getSaveActionsProcessors(Project project, PsiFile psiFile) {
        Storage storage = getStorage(project);
        List<Processor> processors = ProcessorFactory.INSTANCE.getSaveActionsProcessors(project, psiFile, storage);
        processors.sort(new ProcessorComparator());
        return processors;
    }

}