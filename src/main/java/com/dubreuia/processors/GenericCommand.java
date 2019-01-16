package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BiFunction;

import static com.dubreuia.processors.ResultCode.OK;

/**
 * Implements a {@link SaveCommand} for generic commands.
 */
public class GenericCommand extends SaveCommand {

    private final BiFunction<Project, PsiFile[], Runnable> command;

    GenericCommand(Project project, Set<PsiFile> psiFiles, Set<ExecutionMode> modes, Action action,
                   BiFunction<Project, PsiFile[], Runnable> command) {
        super(project, psiFiles, modes, action);
        this.command = command;
    }

    @Override
    protected void run(@NotNull Result<ResultCode> result) {
        command.apply(getProject(), getPsiFilesAsArray()).run();
        result.setResult(OK);
    }

}

