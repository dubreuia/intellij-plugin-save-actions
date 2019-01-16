package com.dubreuia.core.component;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

import java.util.function.BiConsumer;

import static com.dubreuia.core.ExecutionMode.normal;
import static com.dubreuia.model.Action.activate;

public interface SaveActionManagerConstants {

    BiConsumer<CodeInsightTestFixture, SaveActionManager> SAVE_ACTION_MANAGER = (fixture, saveActionManager) ->
            new WriteCommandAction.Simple(fixture.getProject()) {
                @Override
                protected void run() {
                    // set modification timestamp ++
                    ((PsiFileImpl) fixture.getFile()).clearCaches();

                    // call plugin on document
                    PsiFile[] psiFiles = new PsiFile[]{fixture.getFile()};
                    saveActionManager.processPsiFileIfNecessary(fixture.getProject(), psiFiles, activate, normal);
                }
            }.execute();

}
