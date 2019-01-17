package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.intellij.codeInsight.actions.OptimizeImportsProcessor;
import com.intellij.codeInsight.actions.RearrangeCodeProcessor;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.intellij.codeInsight.actions.RearrangeCodeProcessor.COMMAND_NAME;

/**
 * Available processors for global.
 */
public enum GlobalProcessor implements Processor {

    organizeImports(Action.organizeImports,
            (project, psiFiles) -> new OptimizeImportsProcessor(project, psiFiles, null)::run),

    reformat(Action.reformat,
            (project, psiFiles) -> new ReformatCodeProcessor(project, psiFiles, null, false)::run),

    reformatChangedCode(Action.reformatChangedCode,
            (project, psiFiles) -> new ReformatCodeProcessor(project, psiFiles, null, true)::run),

    rearrange(Action.rearrange,
            (project, psiFiles) -> new RearrangeCodeProcessor(project, psiFiles, COMMAND_NAME, null)::run),

    ;

    private final Action action;
    private final BiFunction<Project, PsiFile[], Runnable> command;

    GlobalProcessor(Action action, BiFunction<Project, PsiFile[], Runnable> command) {
        this.action = action;
        this.command = command;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public Set<ExecutionMode> getModes() {
        return EnumSet.allOf(ExecutionMode.class);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public SaveWriteCommand getSaveCommand(Project project, Set<PsiFile> psiFiles) {
        return new SaveWriteCommand(project, psiFiles, getModes(), getAction(), getCommand());
    }

    public BiFunction<Project, PsiFile[], Runnable> getCommand() {
        return command;
    }

    public static Optional<Processor> getProcessorForAction(Action action) {
        return stream().filter(processor -> processor.getAction().equals(action)).findFirst();
    }

    public static Stream<Processor> stream() {
        return Arrays.stream(values());
    }

}
