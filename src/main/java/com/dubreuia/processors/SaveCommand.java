package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Set;
import java.util.function.BiFunction;

/**
 * Abstracts a save command with a {@link BiFunction} from pair ({@link Project}, {@link PsiFile}[])
 * to {@link Runnable}. The entry point is {@link #execute()}.
 */
public abstract class SaveCommand {

    private final Project project;
    private final Set<PsiFile> psiFiles;
    private final Set<ExecutionMode> modes;
    private final Action action;
    private final BiFunction<Project, PsiFile[], Runnable> command;

    protected SaveCommand(Project project, Set<PsiFile> psiFiles, Set<ExecutionMode> modes, Action action,
                          BiFunction<Project, PsiFile[], Runnable> command) {
        this.project = project;
        this.psiFiles = psiFiles;
        this.modes = modes;
        this.action = action;
        this.command = command;
    }

    public Project getProject() {
        return project;
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

    public BiFunction<Project, PsiFile[], Runnable> getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return action.toString();
    }

    public abstract Result<ResultCode> execute();

}
