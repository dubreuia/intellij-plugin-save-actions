/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Alexandre DuBreuil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.dubreuia.processors;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.core.component.SaveActionManager;
import com.dubreuia.model.Action;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.debugger.ui.HotSwapUI;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.ex.QuickList;
import com.intellij.openapi.actionSystem.ex.QuickListsManager;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static com.dubreuia.utils.Helper.toVirtualFiles;
import static com.intellij.openapi.actionSystem.ActionPlaces.UNKNOWN;
import static com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR;
import static com.intellij.openapi.actionSystem.CommonDataKeys.PROJECT;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Available processors for build.
 */
public enum BuildProcessor implements Processor {

    compile(Action.compile,
            (project, psiFiles) -> () -> {
                if (!SaveActionManager.INSTANCE.isCompilingAvailable()) {
                    return;
                }
                CompilerManager.getInstance(project).compile(toVirtualFiles(psiFiles), null);
            }),

    reload(Action.reload,
            (project, psiFiles) -> () -> {
                if (!SaveActionManager.INSTANCE.isCompilingAvailable()) {
                    return;
                }
                DebuggerManagerEx debuggerManager = DebuggerManagerEx.getInstanceEx(project);
                DebuggerSession session = debuggerManager.getContext().getDebuggerSession();
                if (session != null && session.isAttached()) {
                    HotSwapUI.getInstance(project).reloadChangedClasses(session, true);
                }
            }),

    executeAction(Action.executeAction,
            (project, psiFiles) -> () -> {
                Map<Integer, QuickList> quickListsIds = Arrays
                        .stream(QuickListsManager.getInstance().getAllQuickLists())
                        .collect(toMap(QuickList::hashCode, identity()));
                List<QuickList> quickLists = SaveActionManager.INSTANCE.getStorage(project)
                        .getQuickLists().stream()
                        .map(Integer::valueOf)
                        .map(quickListsIds::get)
                        .filter(Objects::nonNull)
                        .collect(toList());
                for (QuickList quickList : quickLists) {
                    String[] actionIds = quickList.getActionIds();
                    for (String actionId : actionIds) {
                        AnAction action = ActionManager.getInstance().getAction(actionId);
                        if (action == null) {
                            continue;
                        }
                        DataContext dataContext = SimpleDataContext.builder()
                                .add(PROJECT, project)
                                .add(EDITOR, FileEditorManager.getInstance(project).getSelectedTextEditor())
                                .setParent(null)
                                .build();
                        AnActionEvent event = AnActionEvent.createFromAnAction(action, null, UNKNOWN, dataContext);
                        action.actionPerformed(event);
                    }
                }
            }) {
        @Override
        public SaveCommand getSaveCommand(Project project, Set<PsiFile> psiFiles) {
            return new SaveReadCommand(project, psiFiles, getModes(), getAction(), getCommand());
        }
    },

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
