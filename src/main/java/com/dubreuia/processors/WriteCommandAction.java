package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Set;

public abstract class WriteCommandAction<T> extends com.intellij.openapi.command.WriteCommandAction<T> {

    private final Action action;
    private final Set<ExecutionMode> modes;

    protected WriteCommandAction(Project project, Action action, Set<ExecutionMode> modes, PsiFile... files) {
        super(project, action.name(), files);
        this.action = action;
        this.modes = modes;
    }

    public Action getAction() {
        return action;
    }

    public Set<ExecutionMode> getModes() {
        return modes;
    }

    @Override
    public String toString() {
        return action.toString();
    }

}
