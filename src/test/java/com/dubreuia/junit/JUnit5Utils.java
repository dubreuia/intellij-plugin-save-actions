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

package com.dubreuia.junit;

import com.intellij.rt.execution.junit.FileComparisonFailure;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.InvocationTargetException;

public class JUnit5Utils {

    private JUnit5Utils() {
        // static
    }

    public static void rethrowAsJunit5Error(AssertionError error) {
        if (error.getCause() instanceof InvocationTargetException) {
            InvocationTargetException intellijInternal = (InvocationTargetException) error.getCause();
            if (intellijInternal.getCause() instanceof FileComparisonFailure) {
                FileComparisonFailure fileComparisonFailure = ((FileComparisonFailure) intellijInternal.getCause());
                String expected = fileComparisonFailure.getExpected();
                String actual = fileComparisonFailure.getActual();
                throw new AssertionFailedError("Expected file do not match actual file", expected, actual);
            }
        }
        throw error;
    }

    public static void rethrowAsJunit5Error(Runnable runnable) {
        try {
            runnable.run();
        } catch (AssertionError error) {
            rethrowAsJunit5Error(error);
        }
    }

}
