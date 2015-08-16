package com.dubreuia.processors;

import com.dubreuia.Settings;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.List;

public enum ProcessorFactory {

    INSTANCE;

    public List<AbstractLayoutCodeProcessor> getSaveActionsProcessors(Project project, PsiFile psiFile, Settings settings) {
        final ArrayList<AbstractLayoutCodeProcessor> processors = new ArrayList<AbstractLayoutCodeProcessor>();
        if (settings.isActivate()) {
            processors.add(getOptimizeImportsProcessor(project, psiFile, settings));
            processors.add(getRearrangeCodeProcessor(project, psiFile, settings));
            processors.add(getReformatCodeProcessor(project, psiFile, settings));
        }
        return processors;
    }

    private AbstractLayoutCodeProcessor getOptimizeImportsProcessor(Project project, PsiFile psiFile, Settings settings) {
        if (settings.isImports()) {
            return new OptimizeImportsProcessor(project, psiFile);
        }
        return null;
    }

    private AbstractLayoutCodeProcessor getRearrangeCodeProcessor(Project project, PsiFile psiFile, Settings settings) {
        if (settings.isRearrange()) {
            return new RearrangeCodeProcessor(project, psiFile);
        }
        return null;
    }

    private AbstractLayoutCodeProcessor getReformatCodeProcessor(Project project, PsiFile psiFile, Settings settings) {
        if (settings.isReformat()) {
            if (null == ChangeListManager.getInstance(project).getChange(psiFile.getVirtualFile())) {
                // That means no VCS is configured, ignore changed code configuration
                return new ReformatCodeProcessor(project, psiFile, false);
            } else {
                return new ReformatCodeProcessor(project, psiFile, settings.isChangedCode());
            }
        }
        return null;
    }

}
