package com.dubreuia.core;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiFile;
import org.apache.log4j.Level;

public class SaveActionShortcutManager extends AnAction {

    public static final Logger LOGGER = Logger.getInstance(SaveActionShortcutManager.class);

    static {
        LOGGER.setLevel(Level.DEBUG);
    }


    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        LOGGER.debug("Running SaveActionShortcutManager");

        PsiFile psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);

        SaveActionManager saveActionManager = new SaveActionManager();
        saveActionManager.checkAndProcessPsiFile(anActionEvent.getProject(), psiFile);

    }
}
