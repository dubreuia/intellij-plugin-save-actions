package com.dubreuia.processors;

import com.dubreuia.model.StorageRO;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import static com.dubreuia.model.Action.rearrange;
import static com.dubreuia.processors.ProcessorMessage.toStringBuilder;

class RearrangeCodeProcessor extends com.intellij.codeInsight.actions.RearrangeCodeProcessor implements Processor {

    private static final String ID = "RearrangeCode";

    private final StorageRO storage;

    RearrangeCodeProcessor(Project project, PsiFile file, StorageRO storage) {
        super(project, new PsiFile[]{file}, COMMAND_NAME, null);
        this.storage = storage;
    }

    @Override
    public void writeToFile() {
        if (storage.isEnabled(rearrange)) {
            super.run();
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(ID, storage.isEnabled(rearrange));
    }

}