package com.dubreuia;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public class SaveAllAction extends AnAction implements DumbAware {

    public static volatile boolean TRIGGERED = false;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            TRIGGERED = true;
            ApplicationManager.getApplication().saveAll();
        } finally {
            TRIGGERED = false;
        }
    }

}
