package com.dubreuia.processors;

import com.dubreuia.Settings;
import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor;
import com.intellij.codeInsight.actions.OptimizeImportsProcessor;
import com.intellij.codeInsight.actions.RearrangeCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public enum ProcessorFactory {

    INSTANCE;

    private Settings settings = ServiceManager.getService(Settings.class);

    public AbstractLayoutCodeProcessor getSaveActionsProcessor(Project project, PsiFile psiFile) {
        AbstractLayoutCodeProcessor processor;
        processor = getReformatCodeProcessor(project, psiFile);
        processor = getRearrangeCodeProcessor(processor);
        processor = getOptimizeImportsProcessor(processor, project, psiFile);
        return processor;
    }

    private AbstractLayoutCodeProcessor getOptimizeImportsProcessor(AbstractLayoutCodeProcessor processor,
                                                                    Project project, PsiFile psiFile) {
        if (null != processor && settings.isImports()) {
            return new OptimizeImportsProcessor(processor);
        } else if (settings.isImports()) {
            return new OptimizeImportsProcessor(project, psiFile);
        }
        return processor;
    }

    private AbstractLayoutCodeProcessor getRearrangeCodeProcessor(AbstractLayoutCodeProcessor processor) {
        if (null != processor && settings.isRearrange()) {
            return new RearrangeCodeProcessor(processor, null);
        }
        return processor;
    }

    private AbstractLayoutCodeProcessor getReformatCodeProcessor(Project project, PsiFile psiFile) {
        if (settings.isReformat()) {
            return new ReformatCodeProcessor(project, psiFile, null, settings.isReformatChangedCode());
        }
        return null;
    }

}
