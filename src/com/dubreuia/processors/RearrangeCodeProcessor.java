package com.dubreuia.processors;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

class RearrangeCodeProcessor extends com.intellij.codeInsight.actions.RearrangeCodeProcessor {

    private static final String NAME = "rearrange code processor";

    public RearrangeCodeProcessor(Project project, PsiFile file) {
        super(project, new PsiFile[]{file}, COMMAND_NAME, null);
    }

    @Override
    public String toString() {
        return NAME;
    }

}