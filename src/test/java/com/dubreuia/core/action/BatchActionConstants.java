package com.dubreuia.core.action;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

import java.util.function.Consumer;

public interface BatchActionConstants {

    Consumer<CodeInsightTestFixture> SAVE_ACTION_BATCH_MANAGER = (fixture) ->
            new WriteCommandAction.Simple(fixture.getProject()) {
                @Override
                protected void run() {
                    // set modification timestamp ++
                    ((PsiFileImpl) fixture.getFile()).clearCaches();

                    // call plugin on document
                    new BatchAction().analyze(fixture.getProject(), new AnalysisScope(fixture.getProject()));
                }
            }.execute();

}
