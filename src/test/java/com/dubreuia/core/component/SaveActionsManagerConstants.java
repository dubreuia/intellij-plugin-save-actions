package com.dubreuia.core.component;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

import java.util.function.BiConsumer;

public interface SaveActionsManagerConstants {

    BiConsumer<CodeInsightTestFixture, SaveActionManager> SAVE_ACTION_MANAGER = (fixture, saveActionManager) ->
            new WriteCommandAction.Simple(fixture.getProject()) {
                @Override
                protected void run() {
                    // set modification timestamp ++
                    ((PsiFileImpl) fixture.getFile()).clearCaches();

                    // call plugin on document
                    saveActionManager.beforeDocumentSaving(fixture.getDocument(fixture.getFile()));
                }
            }.execute();

}
