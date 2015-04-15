package com.dubreuia.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class PsiFilesIsUrlIncludedTest {

    private boolean expected;

    private String url;

    private String exclusion;

    public PsiFilesIsUrlIncludedTest(boolean expected, String url, String exclusion) {
        this.expected = expected;
        this.url = url;
        this.exclusion = exclusion;
    }

    @Parameterized.Parameters(name = "{index} - {0} - {1} - {2}")
    public static List<Object[]> parameters() {
        List<Object[]> parameters = new ArrayList<Object[]>();
        parameters.add(getParameter(false, null, "*"));
        parameters.add(getParameter(false, "test.java", "*"));
        parameters.add(getParameter(false, "test.java", "conf/.*\\.java"));
        parameters.add(getParameter(false, "src/main/java/com/test/Foo.java", "src/main/java/.*/.*Test\\.java"));
        parameters.add(getParameter(true, "test.java", ".*\\.java"));
        parameters.add(getParameter(true, "src/test.java", "src/.*\\.java"));
        parameters.add(getParameter(true, "src/main/java/FooTest.java", ".*Test\\.java"));
        parameters.add(getParameter(true, "src/main/java/com/test/FooTest.java", "src/main/java/.*/.*Test\\.java"));
        return parameters;
    }

    public static Object[] getParameter(boolean expected, String url, String exclusion) {
        return new Object[]{expected, url, exclusion};
    }

    @Test
    public void test() {
        Assert.assertEquals(expected, PsiFiles.isUrlExcluded(url, Collections.singleton(exclusion)));
    }

}