package com.dubreuia.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.dubreuia.SaveActionManager.LOGGER;

public class PsiFiles {

    private PsiFiles() {
        // static class
    }

    public static boolean isPsiFileInProject(Project project, PsiFile file) {
        boolean inProject = ProjectRootManager.getInstance(project).getFileIndex().isInContent(file.getVirtualFile());
        if (!inProject) {
            LOGGER.debug("File " + file.getVirtualFile().getCanonicalPath() + " not in current project");
        }
        return inProject;
    }

    public static boolean isPsiFileExcluded(Project project, PsiFile psiFile, Set<String> exclusions) {
        String fullPsiFileUrl = psiFile.getVirtualFile().getPresentableUrl();
        String fullProjectUrl = project.getPresentableUrl();
        String usableUrl = getUsableUrl(fullProjectUrl, fullPsiFileUrl);
        boolean psiFileExcluded = null != usableUrl && isUrlExcluded(usableUrl, exclusions);
        if (psiFileExcluded) {
            LOGGER.debug("File " + psiFile.getVirtualFile().getCanonicalPath() + " excluded in " + exclusions);
        }
        return psiFileExcluded;
    }

    static String getUsableUrl(String projectUrl, String psiFileUrl) {
        if (null != projectUrl && psiFileUrl.contains(projectUrl)) {
            return psiFileUrl.substring(projectUrl.length() + 1);
        }
        return null;
    }

    static boolean isUrlExcluded(String url, Set<String> exclusions) {
        for (String exclusion : exclusions) {
            try {
                Pattern pattern = Pattern.compile(exclusion);
                Matcher matcher = pattern.matcher(url);
                if (matcher.matches()) {
                    return true;
                }
            } catch (PatternSyntaxException e) {
                return false;
            }
        }
        return false;
    }

}
