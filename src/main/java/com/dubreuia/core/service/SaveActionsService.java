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

package com.dubreuia.core.service;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.model.Action;
import com.intellij.openapi.actionSystem.ex.QuickList;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.List;
import java.util.Set;

/**
 * This interface is implemented by all SaveAction ApplicationServices. It is used to be able to override
 * a concrete implementation. Also, it has to be annotated with {@link Service}.
 */
@Service
public interface SaveActionsService {

    void guardedProcessPsiFiles(Project project, Set<PsiFile> psiFiles, Action activation, ExecutionMode mode);

    boolean isJavaAvailable();

    boolean isCompilingAvailable();

    List<QuickList> getQuickLists(Project project);

}
