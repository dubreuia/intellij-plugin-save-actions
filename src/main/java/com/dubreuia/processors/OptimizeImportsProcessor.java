package com.dubreuia.processors;

import com.dubreuia.model.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.organizeImports;

class OptimizeImportsProcessor extends com.intellij.codeInsight.actions.OptimizeImportsProcessor implements Processor {

    private static final String NAME = "OptimizeImports";

    private final Storage storage;

    OptimizeImportsProcessor(Project project, PsiFile psiFile, Storage storage) {
        super(project, psiFile);
        this.storage = storage;
    }

    @Override
    public void run() {
        if (storage.isEnabled(organizeImports)) {
            try {
                super.run();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String toString() {
        return toString(NAME, storage.isEnabled(organizeImports));
    }

}
