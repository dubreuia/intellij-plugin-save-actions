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
