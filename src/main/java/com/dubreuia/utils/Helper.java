package com.dubreuia.utils;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.util.Arrays;

public class Helper {

    private Helper() {
        // static
    }

    public static VirtualFile[] toVirtualFiles(PsiFile[] psiFiles) {
        return Arrays.stream(psiFiles).map(PsiFile::getVirtualFile).toArray(VirtualFile[]::new);
    }

}
