package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Set;

/**
 * Implements a {@link com.intellij.openapi.command.WriteCommandAction} for save actions.
 */
public abstract class SaveCommand extends WriteCommandAction<ResultCode> {

    private final Set<PsiFile> psiFiles;
    private final Set<ExecutionMode> modes;
    private final Action action;

    protected SaveCommand(Project project, Set<PsiFile> psiFiles, Set<ExecutionMode> modes, Action action) {
        super(project, action.name(), psiFiles.toArray(new PsiFile[0]));
        this.psiFiles = psiFiles;
        this.modes = modes;
        this.action = action;
    }

    public Set<PsiFile> getPsiFiles() {
        return psiFiles;
    }

    public PsiFile[] getPsiFilesAsArray() {
        return psiFiles.toArray(new PsiFile[0]);
    }

    public Set<ExecutionMode> getModes() {
        return modes;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public String toString() {
        return action.toString();
    }

}
