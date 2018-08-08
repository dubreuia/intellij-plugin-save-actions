package com.dubreuia.processors;

import com.dubreuia.model.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import static com.dubreuia.core.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;
import static com.dubreuia.processors.ProcessorMessage.toStringBuilder;

class ReformatCodeProcessor extends com.intellij.codeInsight.actions.ReformatCodeProcessor implements Processor {

    private static final String NAME_CHANGED_TEXT = "ReformatChangedText";

    private static final String NAME_ALL_TEXT = "ReformatAllText";

    private final Storage storage;

    ReformatCodeProcessor(Project project, PsiFile file, Storage storage) {
        super(project, file, null, storage.isEnabled(reformatChangedCode));
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
        return toStringBuilder(storage.isEnabled(reformatChangedCode) ? NAME_CHANGED_TEXT : NAME_ALL_TEXT,
                storage.isEnabled(reformat));
    }

}