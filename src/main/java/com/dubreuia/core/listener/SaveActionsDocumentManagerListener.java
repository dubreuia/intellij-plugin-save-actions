/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Alexandre DuBreuil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.dubreuia.core.listener;

import com.dubreuia.core.service.SaveActionsService;
import com.dubreuia.core.service.SaveActionsServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dubreuia.core.ExecutionMode.saveAll;
import static com.dubreuia.model.Action.activate;

/**
 * FileDocumentManagerListener to catch save events. This listener is registered as ExtensionPoint.
 */
public final class SaveActionsDocumentManagerListener implements FileDocumentManagerListener {

    private static final Logger LOGGER = Logger.getInstance(SaveActionsService.class);

    private final Project project;
    private PsiDocumentManager psiDocumentManager = null;

    public SaveActionsDocumentManagerListener(Project project) {
        this.project = project;
    }

    @Override
    public void beforeAllDocumentsSaving() {
        LOGGER.debug("[+] Start SaveActionsDocumentManagerListener#beforeAllDocumentsSaving, " + project.getName());
        List<Document> unsavedDocuments = Arrays.asList(FileDocumentManager.getInstance().getUnsavedDocuments());
        if (!unsavedDocuments.isEmpty()) {
            LOGGER.debug(String.format("Locating psi files for %d documents: %s", unsavedDocuments.size(), unsavedDocuments));
            beforeDocumentsSaving(unsavedDocuments);
        }
        LOGGER.debug("End SaveActionsDocumentManagerListener#beforeAllDocumentsSaving");
    }

    public void beforeDocumentsSaving(List<Document> documents) {
        if (project.isDisposed()) {
            return;
        }
        initPsiDocManager();
        Set<PsiFile> psiFiles = documents.stream()
                .map(psiDocumentManager::getPsiFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        SaveActionsServiceManager.getService().guardedProcessPsiFiles(project, psiFiles, activate, saveAll);
    }

    private synchronized void initPsiDocManager() {
        if (psiDocumentManager == null) {
            psiDocumentManager = PsiDocumentManager.getInstance(project);
        }
    }

}
