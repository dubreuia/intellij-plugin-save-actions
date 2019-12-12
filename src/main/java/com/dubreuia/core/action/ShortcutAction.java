package com.dubreuia.core.action;

import com.dubreuia.core.component.SaveActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

import static com.dubreuia.core.ExecutionMode.shortcut;
import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.activateOnShortcut;
import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE;
import static java.util.Collections.singletonList;

/**
 * This action runs the plugin on shortcut, only if property {@link com.dubreuia.model.Action#activateOnShortcut} is
 * enabled. It delegates to {@link SaveActionManager}.
 *
 * @see SaveActionManager
 */
public class ShortcutAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        LOGGER.info("[+] Start ShortcutAction#actionPerformed with event " + event);
        var psiFile = event.getData(PSI_FILE);
        var project = event.getProject();
        var psiFiles = new HashSet<>(singletonList(psiFile));
        SaveActionManager.getInstance().guardedProcessPsiFiles(project, psiFiles, activateOnShortcut, shortcut);
        LOGGER.info("End ShortcutAction#actionPerformed processed " + psiFiles.size() + " files");
    }

}
