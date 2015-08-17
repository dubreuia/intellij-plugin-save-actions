package com.dubreuia.processors;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

class OptimizeImportsProcessor extends com.intellij.codeInsight.actions.OptimizeImportsProcessor {

    private static final String NAME = "optimize organizeImports processor";

    public OptimizeImportsProcessor(Project project, PsiFile file) {
        super(project, file);
    }

    @Override
    public String toString() {
        return NAME;
    }

}
