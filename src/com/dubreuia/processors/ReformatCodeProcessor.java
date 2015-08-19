package com.dubreuia.processors;

import com.dubreuia.model.StorageRO;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.psi.PsiFile;

import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;
import static com.dubreuia.processors.ProcessorMessage.toStringBuilder;

class ReformatCodeProcessor extends com.intellij.codeInsight.actions.ReformatCodeProcessor implements Processor {

    private static final String ID_CHANGED_TEXT = "ReformatChangedText";

    private static final String ID_ALL_TEXT = "ReformatAllText";

    private final StorageRO storage;

    ReformatCodeProcessor(Project project, PsiFile psiFile, StorageRO storage) {
        super(project, psiFile, null, processChangedTextOnly(project, psiFile, storage));
        this.storage = storage;
    }

    @Override
    public void writeToFile() {
        if (storage.isEnabled(reformat)) {
            super.run();
        }
    }

    @Override
    public String toString() {
        return toStringBuilder(storage.isEnabled(reformatChangedCode) ? ID_CHANGED_TEXT : ID_ALL_TEXT,
                storage.isEnabled(reformat));
    }

    private static boolean processChangedTextOnly(Project project, PsiFile psiFile, StorageRO storage) {
        if (null == ChangeListManager.getInstance(project).getChange(psiFile.getVirtualFile())) {
            // That means no VCS is configured, ignore changed code configuration
            return false;
        } else {
            return storage.isEnabled(reformatChangedCode);
        }
    }

}