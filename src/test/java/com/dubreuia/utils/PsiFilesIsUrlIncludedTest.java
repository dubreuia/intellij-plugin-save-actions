package com.dubreuia.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static com.dubreuia.utils.PsiFiles.atLeastOneMatch;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

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
        List<Object[]> parameters = new ArrayList<>();

        // Default cases and invalid regex
        parameters.add(getParameter(false, null, "*"));
        parameters.add(getParameter(false, "/home/alex/projects/project1/ignore.java", "*"));

        // Base cases
        parameters.add(getParameter(true, "/project/Ignore.java", "Ignore.java"));
        parameters.add(getParameter(false, "/project/Ignore.java", "ignore.java"));
        parameters.add(getParameter(true, "/project/Ignore.java", ".*\\.java"));

        // With different project strings
        parameters.add(getParameter(false, "/home/alex/projects/project1/ignore.java", ".*\\.properties"));
        parameters.add(getParameter(true, "/home/alex/projects/project1/ignore.properties", ".*\\.properties"));
        parameters.add(getParameter(true, "c://projects/project/ignore.properties", ".*\\.properties"));

        // With specific paths
        parameters.add(getParameter(false, "/home/alex/projects/project1/ignore.properties", "src/.*\\.properties"));
        parameters.add(getParameter(true, "/home/alex/projects/project1/src/ignore.properties", "src/.*\\.properties"));

        // With specific folders recursive
        parameters.add(getParameter(true, "/project1/src/ignore/Ignore.java", "ignore/.*"));
        parameters.add(getParameter(true, "/project1/src/ignore/sub/Ignore.java", "ignore/.*"));
        parameters.add(getParameter(true, "/project1/src/ignore/sub/Ignore.java", "ignore/.*\\.java"));
        parameters.add(getParameter(true, "/project1/src/ignore/sub/Ignore.java", "ignore/.*/.*\\.java"));

        return parameters;
    }

    private static Object[] getParameter(boolean expected, String psiFileUrl, String exclusion) {
        return new Object[]{expected, psiFileUrl, exclusion};
    }

    @Test
    public void test() {
        assertEquals(expected, atLeastOneMatch(url, singleton(exclusion)));
    }

}