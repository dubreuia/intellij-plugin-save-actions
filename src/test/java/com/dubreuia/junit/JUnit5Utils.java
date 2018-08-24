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
