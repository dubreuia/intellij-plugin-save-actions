package com.dubreuia.processors;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

class ReformatCodeProcessor extends com.intellij.codeInsight.actions.ReformatCodeProcessor {

    private static final String NAME = "reformat code processor";

    private static final String CHANGED_TEXT_ONLY = "changed text only";

    private static final String ALL_TEXT = "all text";

    private final boolean processChangedTextOnly;

    public ReformatCodeProcessor(Project project, PsiFile file, boolean processChangedTextOnly) {
        super(project, file, null, processChangedTextOnly);
        this.processChangedTextOnly = processChangedTextOnly;
    }

    @Override
    public String toString() {
        return NAME + " - " + (processChangedTextOnly ? CHANGED_TEXT_ONLY : ALL_TEXT);
    }

}