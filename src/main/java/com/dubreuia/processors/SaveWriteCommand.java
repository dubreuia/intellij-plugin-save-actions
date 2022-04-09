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
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.Set;
import java.util.function.BiFunction;

import static com.dubreuia.processors.ResultCode.FAILED;
import static com.dubreuia.processors.ResultCode.OK;

/**
 * Implements a write action that encapsulates {@link com.intellij.openapi.command.WriteCommandAction} that returns
 * a {@link Result}.
 */
public class SaveWriteCommand extends SaveCommand {

    public SaveWriteCommand(Project project, Set<PsiFile> psiFiles, Set<ExecutionMode> modes, Action action,
                            BiFunction<Project, PsiFile[], Runnable> command) {
        super(project, psiFiles, modes, action, command);
    }

    @Override
    public synchronized Result<ResultCode> execute() {
        try {
            WriteCommandAction.writeCommandAction(getProject(), getPsiFilesAsArray())
                    .run(() -> getCommand().apply(getProject(), getPsiFilesAsArray()).run());
            return new Result<>(OK);
        } catch (Exception e) {
            return new Result<>(FAILED);
        }
    }
}
