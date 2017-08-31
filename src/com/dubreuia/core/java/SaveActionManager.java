package com.dubreuia.core.java;

import com.dubreuia.processors.Processor;
import com.dubreuia.processors.Processor.ProcessorComparator;
import com.dubreuia.processors.java.ProcessorFactory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.apache.log4j.Level;

import java.util.List;

import static java.util.Collections.sort;

/**
 * Event handler class, instanciated by {@link com.dubreuia.core.java.Component}. The
 * {@link #getSaveActionsProcessors(Project, PsiFile)} returns the java specific processors.
 */
class SaveActionManager extends com.dubreuia.core.SaveActionManager {

    private static final Logger LOGGER = Logger.getInstance(SaveActionManager.class);

    static {
        LOGGER.setLevel(Level.DEBUG);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected List<Processor> getSaveActionsProcessors(Project project, PsiFile psiFile) {
        List<Processor> processors = ProcessorFactory.INSTANCE
                .getSaveActionsProcessors(project, psiFile, getStorage(project));
        sort(processors, new ProcessorComparator());
        return processors;
    }

}