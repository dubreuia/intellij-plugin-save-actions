package com.dubreuia.core.java;

import com.dubreuia.model.EPFStorage;
import com.dubreuia.model.Storage;
import com.dubreuia.processors.Processor;
import com.dubreuia.processors.Processor.ProcessorComparator;
import com.dubreuia.processors.java.ProcessorFactory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.awt.RelativePoint;
import org.apache.log4j.Level;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
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

        Storage originalStorage = getStorage(project);

        Storage storage = getStorageFromEPFOrDefault(project, originalStorage);
        if (storage != null) {

            List<Processor> processors = ProcessorFactory.INSTANCE
                    .getSaveActionsProcessors(project, psiFile, storage);
            sort(processors, new ProcessorComparator());
            return processors;
        }
        return Collections.emptyList();
    }

    /**
     * @return <ul>
     * <li>if the path to EPF configuration file is set and valid, then the configuration based on EPF is returned</li>
     * <li>if the path to EPF configuration file is not set, then the default configuration is returned</li>
     * <li>otherwise null</li>
     * </ul>
     */
    @Nullable
    private Storage getStorageFromEPFOrDefault(Project project, Storage originalStorage) {
        try {
            Storage storage;
            Storage storageFromEPF = new EPFStorage().getStorage(originalStorage.getConfigurationPath(), originalStorage);
            if (storageFromEPF != null) {
                LOGGER.debug("Loading configuration from " + originalStorage.getConfigurationPath());
                storage = storageFromEPF;
            } else {
                storage = originalStorage;
            }
            return storage;
        } catch (IOException e) {

            LOGGER.debug("Could not load configuration from " + originalStorage.getConfigurationPath(), e);

            //FIXME replace with notification API
            StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
            JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder("Save Actions:<br>Could not load " + originalStorage.getConfigurationPath() + "<br>Please check the configuration.", MessageType.WARNING, null)
                    .createBalloon()
                    .show(RelativePoint.getCenterOf(statusBar.getComponent()),
                            Balloon.Position.atRight);

            return null;

        }
    }

}