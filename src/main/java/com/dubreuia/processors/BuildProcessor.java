package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.core.component.SaveActionManager;
import com.dubreuia.model.Action;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.debugger.settings.DebuggerSettings;
import com.intellij.debugger.ui.HotSwapUI;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.QuickList;
import com.intellij.openapi.actionSystem.ex.QuickListsManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.dubreuia.utils.Helper.toVirtualFiles;
import static com.intellij.openapi.actionSystem.ActionPlaces.UNKNOWN;
import static com.intellij.openapi.actionSystem.CommonDataKeys.PROJECT;
import static com.intellij.openapi.actionSystem.impl.SimpleDataContext.getSimpleContext;

/**
 * Available processors for build.
 */
public enum BuildProcessor implements Processor {

    compile(Action.compile,
            (project, psiFiles) -> () -> {
                if (SaveActionManager.getInstance().isCompilingAvailable()) {
                    CompilerManager.getInstance(project).compile(toVirtualFiles(psiFiles), null);
                }
            }),

    reload(Action.reload,
            (project, psiFiles) -> () -> {
                if (SaveActionManager.getInstance().isCompilingAvailable()) {
                    DebuggerManagerEx debuggerManager = DebuggerManagerEx.getInstanceEx(project);
                    DebuggerSession session = debuggerManager.getContext().getDebuggerSession();
                    if (session != null && session.isAttached()) {
                        boolean compileEnabled = SaveActionManager.getInstance()
                                .getStorage(project).isEnabled(Action.compile);
                        boolean compileHotswapSetting = DebuggerSettings.getInstance().COMPILE_BEFORE_HOTSWAP;
                        boolean compileBeforeHotswap = compileEnabled ? false : compileHotswapSetting;
                        HotSwapUI.getInstance(project).reloadChangedClasses(session, compileBeforeHotswap);
                    }
                }
            }),

    ;

    private final Action action;
    private final BiFunction<Project, PsiFile[], Runnable> command;

    BuildProcessor(Action action, BiFunction<Project, PsiFile[], Runnable> command) {
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
        return 2;
    }

    @Override
    public SaveCommand getSaveCommand(Project project, Set<PsiFile> psiFiles) {
        return new GenericCommand(project, psiFiles, getModes(), getAction(), command);
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
