package com.dubreuia.processors;

import com.dubreuia.model.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;

class ReformatCodeProcessor extends com.intellij.codeInsight.actions.ReformatCodeProcessor implements Processor {

    private static final String NAME_CHANGED_TEXT = "ReformatChangedText";
    private static final String NAME_ALL_TEXT = "ReformatAllText";

    private final Storage storage;

    ReformatCodeProcessor(Project project, PsiFile psiFile, Storage storage) {
        super(project, psiFile, null, storage.isEnabled(reformatChangedCode));
        this.storage = storage;
    }

    @Override
    public void run() {
        if (storage.isEnabled(reformat)) {
            try {
                super.run();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String toString() {
        return toString(storage.isEnabled(reformatChangedCode) ? NAME_CHANGED_TEXT : NAME_ALL_TEXT,
                storage.isEnabled(reformat));
    }

}