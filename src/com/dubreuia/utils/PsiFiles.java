package com.dubreuia.utils;

import static com.dubreuia.SaveActionManager.LOGGER;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PsiFiles {

    private static final String REGEX_STARTS_WITH_ANY_STRING = ".*?";

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

    public static boolean isPsiFileExcluded(PsiFile psiFile, Set<String> exclusions) {
        boolean psiFileExcluded = isUrlExcluded(psiFile.getVirtualFile().getCanonicalPath(), exclusions);
        if (psiFileExcluded) {
            LOGGER.debug("File " + psiFile.getVirtualFile().getCanonicalPath() + " excluded in " + exclusions);
        }
        return psiFileExcluded;
    }

    static boolean isUrlExcluded(String psiFileUrl, Set<String> exclusions) {
        for (String exclusion : exclusions) {
            try {
                Pattern pattern = Pattern.compile(REGEX_STARTS_WITH_ANY_STRING + exclusion);
                Matcher matcher = pattern.matcher(psiFileUrl);
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
