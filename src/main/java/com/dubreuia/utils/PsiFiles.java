package com.dubreuia.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.dubreuia.core.SaveActionManager.LOGGER;

public class PsiFiles {

    private static final String REGEX_STARTS_WITH_ANY_STRING = ".*?";

    private PsiFiles() {
        // static class
    }

    public static boolean isPsiFileInProject(Project project, PsiFile file) {
        boolean inProject = ProjectRootManager.getInstance(project).getFileIndex().isInContent(file.getVirtualFile());
        if (!inProject) {
            LOGGER.debug("File " + file.getVirtualFile().getCanonicalPath() + " not in current project " + project);
        }
        return inProject;
    }

    public static boolean isIncludedAndNotExcluded(String path, Set<String> inclusions, Set<String> exclusions) {
        return isIncluded(inclusions, path) && !isExcluded(exclusions, path);
    }

    private static boolean isExcluded(Set<String> exclusions, String path) {
        boolean psiFileExcluded = atLeastOneMatch(path, exclusions);
        if (psiFileExcluded) {
            LOGGER.debug("File " + path + " excluded in " + exclusions);
        }
        return psiFileExcluded;
    }

    private static boolean isIncluded(Set<String> inclusions, String path) {
        if (inclusions.isEmpty()) {
            // If no inclusion are defined, all files are allowed
            return true;
        }
        boolean psiFileIncluded = atLeastOneMatch(path, inclusions);
        if (psiFileIncluded) {
            LOGGER.debug("File " + path + " included in " + inclusions);
        }
        return psiFileIncluded;
    }

    static boolean atLeastOneMatch(String psiFileUrl, Set<String> patterns) {
        for (String pattern : patterns) {
            try {
                Matcher matcher = Pattern.compile(REGEX_STARTS_WITH_ANY_STRING + pattern).matcher(psiFileUrl);
                if (matcher.matches()) {
                    return true;
                }
            } catch (PatternSyntaxException e) {
                // invalid patterns are ignored
                return false;
            }
        }
        return false;
    }

}
