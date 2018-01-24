package com.dubreuia.core.java;

import com.dubreuia.model.EpfStorage;
import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.dubreuia.processors.Processor.ProcessorComparator;
import com.dubreuia.processors.java.ProcessorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.List;

import static java.util.Collections.sort;

/**
 * Event handler class, instanciated by {@link com.dubreuia.core.java.Component}. The
 * {@link #getSaveActionsProcessors(Project, PsiFile)} returns the java specific processors.
 */
public class SaveActionManager extends com.dubreuia.core.SaveActionManager {

    @Override
    protected List<Processor> getSaveActionsProcessors(Project project, PsiFile psiFile) {
        Storage storage = getStorage(project);
        List<Processor> processors = ProcessorFactory.INSTANCE.getSaveActionsProcessors(project, psiFile, storage);
        sort(processors, new ProcessorComparator());
        return processors;
    }

    @Override
    protected Storage getStorage(Project project) {
        Storage defaultStorage = super.getStorage(project);
        return EpfStorage.INSTANCE.getStorageOrDefault(defaultStorage.getConfigurationPath(), defaultStorage);
    }

}