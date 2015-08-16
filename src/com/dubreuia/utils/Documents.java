package com.dubreuia.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.impl.IdeFrameImpl;
import org.jetbrains.annotations.NotNull;

public class Documents {

    private Documents() {
        // static class
    }

    public static boolean isDocumentActive(@NotNull Document document) {
        final IdeFrame activeFrame = (IdeFrame) IdeFrameImpl.getActiveFrame();
        return activeFrame != null && activeFrame.getProject() != null && isDocumentActive(activeFrame.getProject(), document);
    }

    private static boolean isDocumentActive(@NotNull Project project, @NotNull Document document) {
        final Editor selectedTextEditor = FileEditorManagerEx.getInstance(project).getSelectedTextEditor();
        return selectedTextEditor != null && selectedTextEditor.getDocument() == document;
    }

}
