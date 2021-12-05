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

package com.dubreuia.core.component;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.dubreuia.core.ExecutionMode.saveAll;
import static com.dubreuia.model.Action.activate;
import static java.util.Collections.singleton;

public interface SaveActionManagerConstants {

    BiConsumer<CodeInsightTestFixture, SaveActionManager> SAVE_ACTION_MANAGER = (fixture, saveActionManager) ->
            WriteCommandAction.writeCommandAction(fixture.getProject()).run(() -> runFixture(fixture, saveActionManager));

    static void runFixture(CodeInsightTestFixture fixture, SaveActionManager saveActionManager) {
        // set modification timestamp ++
        fixture.getFile().clearCaches();

        // call plugin on document
        Set<PsiFile> psiFiles = new HashSet<>(singleton(fixture.getFile()));
        saveActionManager.guardedProcessPsiFiles(fixture.getProject(), psiFiles, activate, saveAll);
    }
}
