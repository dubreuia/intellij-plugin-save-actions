package com.dubreuia.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

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

}
