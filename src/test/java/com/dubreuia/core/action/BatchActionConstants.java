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

package com.dubreuia.core.action;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;

import java.util.function.Consumer;

public interface BatchActionConstants {

    Consumer<CodeInsightTestFixture> SAVE_ACTION_BATCH_MANAGER = (fixture) ->
            WriteCommandAction.writeCommandAction(fixture.getProject()).run(() -> runFixure(fixture));

    static void runFixure(CodeInsightTestFixture fixture) {
        // set modification timestamp ++
        fixture.getFile().clearCaches();

        // call plugin on document
        new BatchAction().analyze(fixture.getProject(), new AnalysisScope(fixture.getProject()));
    }
}
