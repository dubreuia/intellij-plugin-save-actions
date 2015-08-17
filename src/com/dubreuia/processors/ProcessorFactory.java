package com.dubreuia.processors;

import com.dubreuia.model.Storage;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.List;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.organizeImports;
import static com.dubreuia.model.Action.rearrange;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;

public enum ProcessorFactory {

    INSTANCE;

    public List<AbstractLayoutCodeProcessor> getSaveActionsProcessors(Project project, PsiFile psiFile, Storage storage) {
        final ArrayList<AbstractLayoutCodeProcessor> processors = new ArrayList<AbstractLayoutCodeProcessor>();
        if (storage.isEnabled(activate)) {
            processors.add(getOptimizeImportsProcessor(project, psiFile, storage));
            processors.add(getRearrangeCodeProcessor(project, psiFile, storage));
            processors.add(getReformatCodeProcessor(project, psiFile, storage));
        }
        return processors;
    }

    private AbstractLayoutCodeProcessor getOptimizeImportsProcessor(Project project, PsiFile psiFile, Storage storage) {
        if (storage.isEnabled(organizeImports)) {
            return new OptimizeImportsProcessor(project, psiFile);
        }
        return null;
    }

    private AbstractLayoutCodeProcessor getRearrangeCodeProcessor(Project project, PsiFile psiFile, Storage storage) {
        if (storage.isEnabled(rearrange)) {
            return new RearrangeCodeProcessor(project, psiFile);
        }
        return null;
    }

    private AbstractLayoutCodeProcessor getReformatCodeProcessor(Project project, PsiFile psiFile, Storage storage) {
        if (storage.isEnabled(reformat)) {
            if (null == ChangeListManager.getInstance(project).getChange(psiFile.getVirtualFile())) {
                // That means no VCS is configured, ignore changed code configuration
                return new ReformatCodeProcessor(project, psiFile, false);
            } else {
                return new ReformatCodeProcessor(project, psiFile, storage.isEnabled(reformatChangedCode));
            }
        }
        return null;
    }

}
