package com.dubreuia.processors;

import com.dubreuia.model.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.psi.PsiFile;

import static com.dubreuia.core.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;
import static com.dubreuia.processors.ProcessorMessage.toStringBuilder;

class ReformatCodeProcessor extends com.intellij.codeInsight.actions.ReformatCodeProcessor implements Processor {

    private static final String ID_CHANGED_TEXT = "ReformatChangedText";

    private static final String ID_ALL_TEXT = "ReformatAllText";

    private final Storage storage;

    ReformatCodeProcessor(Project project, PsiFile file, Storage storage) {
        super(project, file, null, processChangedTextOnly(project, file, storage));
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
    public int order() {
        return 2;
    }

    @Override
    public String toString() {
        return toStringBuilder(storage.isEnabled(reformatChangedCode) ? ID_CHANGED_TEXT : ID_ALL_TEXT,
                storage.isEnabled(reformat));
    }

    private static boolean processChangedTextOnly(Project project, PsiFile psiFile, Storage storage) {
        if (null == ChangeListManager.getInstance(project).getChange(psiFile.getVirtualFile())) {
            // That means no VCS is configured, ignore changed code configuration
            return false;
        } else {
            return storage.isEnabled(reformatChangedCode);
        }
    }

}