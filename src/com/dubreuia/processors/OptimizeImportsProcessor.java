package com.dubreuia.processors;

import com.dubreuia.model.StorageRO;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import static com.dubreuia.model.Action.organizeImports;
import static com.dubreuia.processors.ProcessorMessage.toStringBuilder;

class OptimizeImportsProcessor extends com.intellij.codeInsight.actions.OptimizeImportsProcessor implements Processor {

    private static final String ID = "OptimizeImports";

    private final StorageRO storage;

    OptimizeImportsProcessor(Project project, PsiFile file, StorageRO storage) {
        super(project, file);
        this.storage = storage;
    }

    @Override
    public void writeToFile() {
        if (storage.isEnabled(organizeImports)) {
            super.run();
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(ID, storage.isEnabled(organizeImports));
    }

}
