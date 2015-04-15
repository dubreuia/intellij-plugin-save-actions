package com.dubreuia.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

@RunWith(Parameterized.class)
public class PsiFilesGetUsableUrlTest {

    private String expected;

    private String projectUrl;

    private String psiFileUrl;

    public PsiFilesGetUsableUrlTest(String expected, String projectUrl, String psiFileUrl) {
        this.expected = expected;
        this.projectUrl = projectUrl;
        this.psiFileUrl = psiFileUrl;
    }

    @Parameterized.Parameters(name = "{index} - {0} - {1} - {2}")
    public static List<Object[]> parameters() {
        List<Object[]> parameters = new ArrayList<Object[]>();
        parameters.add(getParameter(null, null, null));
        parameters.add(getParameter(null,
                "/home/alexandre/Documents/project/save-actions2",
                "/home/alexandre/Documents/project/save-actions/src/com/dubreuia/Settings.java"));
        parameters.add(getParameter("src/com/dubreuia/Settings.java",
                "/home/alexandre/Documents/project/save-actions",
                "/home/alexandre/Documents/project/save-actions/src/com/dubreuia/Settings.java"));
        return parameters;
    }

    public static Object[] getParameter(String expected, String projectUrl, String psiFileUrl) {
        return new Object[]{expected, projectUrl, psiFileUrl};
    }

    @Test
    public void test() {
        Assert.assertEquals(expected, PsiFiles.getUsableUrl(projectUrl, psiFileUrl));
    }

}