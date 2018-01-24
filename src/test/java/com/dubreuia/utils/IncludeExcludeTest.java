package com.dubreuia.utils;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.dubreuia.utils.PsiFiles.isIncludedAndNotExcluded;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class IncludeExcludeTest {
    private boolean expected;

    private String url;

    private String exclusion;
    private String inclusion;

    public IncludeExcludeTest(boolean expected, String url, String inclusion, String exclusion) {
        this.expected = expected;
        this.url = url;
        this.exclusion = exclusion;
        this.inclusion = inclusion;
    }

    @Parameterized.Parameters(name = "{index} - {0} - {1} - {2} - {3}")
    public static List<Object[]> parameters() {
        List<Object[]> parameters = new ArrayList<Object[]>();

        //only excludes - taken from PsiFilesIsUrlIncludedTest
        {
            // Default cases and invalid regex
            parameters.add(getParameter(true, null, "", "*"));
            parameters.add(getParameter(true, "/home/alex/projects/project1/ignore.java", "", "*"));

            // Base cases
            parameters.add(getParameter(false, "/project/Ignore.java", "", "Ignore.java"));
            parameters.add(getParameter(true, "/project/Ignore.java", "", "ignore.java"));
            parameters.add(getParameter(false, "/project/Ignore.java", "", ".*\\.java"));

            // With different project strings
            parameters.add(getParameter(true, "/home/alex/projects/project1/ignore.java", "", ".*\\.properties"));
            parameters.add(getParameter(false, "/home/alex/projects/project1/ignore.properties", "", ".*\\.properties"));
            parameters.add(getParameter(false, "c://projects/project/ignore.properties", "", ".*\\.properties"));

            // With specific paths
            parameters.add(getParameter(true, "/home/alex/projects/project1/ignore.properties", "", "src/.*\\.properties"));
            parameters.add(getParameter(false, "/home/alex/projects/project1/src/ignore.properties", "", "src/.*\\.properties"));

            // With specific folders recursive
            parameters.add(getParameter(false, "/project1/src/ignore/Ignore.java", "", "ignore/.*"));
            parameters.add(getParameter(false, "/project1/src/ignore/sub/Ignore.java", "", "ignore/.*"));
            parameters.add(getParameter(false, "/project1/src/ignore/sub/Ignore.java", "", "ignore/.*\\.java"));
            parameters.add(getParameter(false, "/project1/src/ignore/sub/Ignore.java", "", "ignore/.*/.*\\.java"));
        }
        //only includes
        {
            parameters.add(getParameter(true, "/project/Include.java", "Include.java", ""));
            parameters.add(getParameter(false, "/project/Include.java", "include.java", ""));
            parameters.add(getParameter(true, "/project/Include.java", ".*\\.java", ""));
        }

        //includes and excludes
        {
            parameters.add(getParameter(false, "/project/Include.java", ".*\\.java", ".*\\.java"));
            parameters.add(getParameter(true, "/project/Include.java", ".*\\.java", ".*\\.xml"));
            parameters.add(getParameter(false, "/project/Include.xml", ".*\\.java", ".*\\.xml"));
        }

        return parameters;
    }

    public static Object[] getParameter(boolean expected, String psiFileUrl, String inclusion, String exclusion) {
        return new Object[]{expected, psiFileUrl, inclusion, exclusion};
    }

    @Test
    public void test() {
        assertEquals(expected, isIncludedAndNotExcluded(url, createSingleton(inclusion), createSingleton(exclusion)));
    }

    private Set<String> createSingleton(String inclusion) {
        if (null == inclusion || inclusion.isEmpty()) {
            return emptySet();
        }
        return singleton(inclusion);
    }

}
