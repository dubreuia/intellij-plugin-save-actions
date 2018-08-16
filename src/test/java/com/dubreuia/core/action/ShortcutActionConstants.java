package com.dubreuia.core.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.intellij.openapi.actionSystem.CommonDataKeys.PROJECT;
import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE;
import static com.intellij.openapi.actionSystem.impl.SimpleDataContext.getSimpleContext;

public interface ShortcutActionConstants {

    Consumer<CodeInsightTestFixture> SAVE_ACTION_SHORTCUT_MANAGER = (fixture) ->
            new WriteCommandAction.Simple(fixture.getProject()) {
                @Override
                protected void run() {
                    // set modification timestamp ++
                    ((PsiFileImpl) fixture.getFile()).clearCaches();

                    ActionManager actionManager = ActionManager.getInstance();
                    AnAction action = actionManager.getAction(ShortcutAction.class.getName());

                    Map<String, Object> data = new HashMap<>();
                    data.put(PROJECT.getName(), fixture.getProject());
                    data.put(PSI_FILE.getName(), fixture.getFile());
                    DataContext dataContext = getSimpleContext(data, null);

                    // call plugin on document
                    AnActionEvent event = AnActionEvent.createFromAnAction(action, null, "save-actions", dataContext);
                    new ShortcutAction().actionPerformed(event);
                }
            }.execute();

}
