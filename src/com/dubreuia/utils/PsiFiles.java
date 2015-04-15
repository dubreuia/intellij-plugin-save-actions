package com.dubreuia.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PsiFiles {

    public static boolean isPsiFilePhysicallyInProject(Project project, PsiFile psiFile) {
        return isPsiFilePhysicallyInProject(project, psiFile.getParent());
    }

    public static boolean isPsiFilePhysicallyInProject(Project project, PsiDirectory psiDirectory) {
        if (psiDirectory != null) {
            if (project.getBaseDir().equals(psiDirectory.getVirtualFile())) {
                return true;
            }
            return isPsiFilePhysicallyInProject(project, psiDirectory.getParent());
        }
        return false;
    }

    public static boolean isPsiFileExcluded(Project project, PsiFile psiFile, Set<String> exclusions) {
        String fullPsiFileUrl = psiFile.getVirtualFile().getPresentableUrl();
        String fullProjectUrl = project.getPresentableUrl();
        String usableUrl = getUsableUrl(fullProjectUrl, fullPsiFileUrl);
        return null != usableUrl && isUrlExcluded(usableUrl, exclusions);
    }

    static String getUsableUrl(String projectUrl, String psiFileUrl) {
        if (null != projectUrl && psiFileUrl.contains(projectUrl)) {
            return psiFileUrl.substring(projectUrl.length() + 1);
        }
        return null;
    }

    static boolean isUrlExcluded(String url, Set<String> exclusions) {
        for (String exclusion : exclusions) {
            Pattern pattern;
            try {
                pattern = Pattern.compile(exclusion);
            } catch (PatternSyntaxException e) {
                return false;
            }
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

}
