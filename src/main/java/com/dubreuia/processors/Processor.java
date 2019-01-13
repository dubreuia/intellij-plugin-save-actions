package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.intellij.codeInsight.actions.OptimizeImportsProcessor;
import com.intellij.codeInsight.actions.RearrangeCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.dubreuia.core.ExecutionMode.normal;
import static com.dubreuia.core.ExecutionMode.shortcut;
import static com.dubreuia.utils.Helper.toVirtualFiles;
import static com.intellij.codeInsight.actions.RearrangeCodeProcessor.COMMAND_NAME;

public enum Processor {

    compile(Action.compile,
            EnumSet.of(normal, shortcut),
            (project, psiFiles) -> () -> CompilerManager.getInstance(project).compile(toVirtualFiles(psiFiles), null)),

    organizeImports(Action.organizeImports,
            (project, psiFiles) -> new OptimizeImportsProcessor(project, psiFiles, null)::run),

    reformat(Action.reformat,
            (project, psiFiles) -> new ReformatCodeProcessor(project, psiFiles, null, false)::run),

    reformatChangedCode(Action.reformatChangedCode,
            (project, psiFiles) -> new ReformatCodeProcessor(project, psiFiles, null, true)::run),

    rearrange(Action.rearrange,
            (project, psiFiles) -> new RearrangeCodeProcessor(project, psiFiles, COMMAND_NAME, null)::run);

    private final Action action;
    private final BiFunction<Project, PsiFile[], Runnable> command;
    private final Set<ExecutionMode> modes;

    Processor(Action action, BiFunction<Project, PsiFile[], Runnable> command) {
        this(action, EnumSet.allOf(ExecutionMode.class), command);
    }

    Processor(Action action, Set<ExecutionMode> modes, BiFunction<Project, PsiFile[], Runnable> command) {
        this.action = action;
        this.command = command;
        this.modes = modes;
    }

    public WriteCommandAction getWriteCommandAction(Project project, PsiFile[] psiFiles) {
        return new WriteCommandAction(project, action, modes, psiFiles) {
            @Override
            protected void run(@NotNull Result result) {
                // TODO result
                // TODO move to class
                command.apply(project, psiFiles).run();
            }
        };
    }

    public static Stream<Processor> stream() {
        return Arrays.stream(values());
    }

}
