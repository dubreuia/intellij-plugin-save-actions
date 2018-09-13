package com.dubreuia.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.PsiErrorElementUtil;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;

public class PsiFiles {

    private static final String REGEX_STARTS_WITH_ANY_STRING = ".*?";

    private PsiFiles() {
        // static class
    }

    public static boolean isPsiFileEligible(Project project,
                                            PsiFile psiFile,
                                            Set<String> inclusions,
                                            Set<String> exclusions,
                                            boolean noActionIfCompileErrors) {
        return psiFile != null
                && isProjectValid(project)
                && isPsiFileInProject(project, psiFile)
                && isPsiFileHasErrors(project, psiFile, noActionIfCompileErrors)
                && isPsiFileIncluded(psiFile, inclusions, exclusions)
                && isPsiFileFresh(psiFile)
                && isPsiFileValid(psiFile);
    }

    private static boolean isProjectValid(Project project) {
        return project.isInitialized()
                && !project.isDisposed();
    }

    private static boolean isPsiFileInProject(Project project, PsiFile file) {
        boolean inProject = ProjectRootManager.getInstance(project).getFileIndex().isInContent(file.getVirtualFile());
        if (!inProject) {
            LOGGER.info("File " + file.getVirtualFile().getCanonicalPath() + " not in current project " + project);
        }
        return inProject;
    }

    private static boolean isPsiFileHasErrors(Project project, PsiFile psiFile, boolean noActionIfCompileErrors) {
        if (noActionIfCompileErrors) {
            return !PsiErrorElementUtil.hasErrors(project, psiFile.getVirtualFile());
        }
        return true;
    }

    private static boolean isPsiFileIncluded(PsiFile psiFile, Set<String> inclusions, Set<String> exclusions) {
        String canonicalPath = psiFile.getVirtualFile().getCanonicalPath();
        return isIncludedAndNotExcluded(canonicalPath, inclusions, exclusions);
    }

    private static boolean isPsiFileFresh(PsiFile psiFile) {
        return psiFile.getModificationStamp() != 0;
    }

    private static boolean isPsiFileValid(PsiFile psiFile) {
        return psiFile.isValid();
    }

    static boolean isIncludedAndNotExcluded(String path, Set<String> inclusions, Set<String> exclusions) {
        return isIncluded(inclusions, path) && !isExcluded(exclusions, path);
    }

    private static boolean isExcluded(Set<String> exclusions, String path) {
        boolean psiFileExcluded = atLeastOneMatch(path, exclusions);
        if (psiFileExcluded) {
            LOGGER.info("File " + path + " excluded in " + exclusions);
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
            LOGGER.info("File " + path + " included in " + inclusions);
        }
        return psiFileIncluded;
    }

    private static boolean atLeastOneMatch(String psiFileUrl, Set<String> patterns) {
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
