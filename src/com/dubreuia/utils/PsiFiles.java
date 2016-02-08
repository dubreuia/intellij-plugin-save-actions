package com.dubreuia.utils;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
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

    public static boolean isPsiFileInFocus(PsiFile psiFile) {
        DataContext result = DataManager.getInstance().getDataContextFromFocus().getResult();
        boolean psiFileInFocus = false;
        if (result != null) {
            PsiFile focus = DataKeys.PSI_FILE.getData(result);
            if (null != psiFile && null != focus) {
                psiFileInFocus = focus.equals(psiFile);
                if (!psiFileInFocus) {
                    LOGGER.debug("File " + psiFile.getVirtualFile().getCanonicalPath() + " not in focus of " +
                            focus.getVirtualFile().getCanonicalPath());
                }
            }
        }
        return psiFileInFocus;
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
