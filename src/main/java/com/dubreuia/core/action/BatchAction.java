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
package com.dubreuia.core.action;

import com.dubreuia.core.service.SaveActionsService;
import com.dubreuia.core.service.SaveActionsServiceManager;
import com.intellij.analysis.AnalysisScope;
import com.intellij.analysis.BaseAnalysisAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.dubreuia.core.ExecutionMode.batch;
import static com.dubreuia.model.Action.activateOnBatch;
import static java.util.Collections.synchronizedSet;

/**
 * This action runs the save actions on the given scope of files, only if property
 * {@link com.dubreuia.model.Action#activateOnShortcut} is enabled. The user is asked for the scope using a standard
 * IDEA dialog. It delegates to {@link SaveActionsService}. Originally based on
 * {@link com.intellij.codeInspection.inferNullity.InferNullityAnnotationsAction}.
 *
 * @author markiewb
 * @see SaveActionsServiceManager
 */
public class BatchAction extends BaseAnalysisAction {

    private static final Logger LOGGER = Logger.getInstance(SaveActionsService.class);
    private static final String COMPONENT_NAME = "Save Actions";

    public BatchAction() {
        super(COMPONENT_NAME, COMPONENT_NAME);
    }

    @Override
    protected void analyze(@NotNull Project project, @NotNull AnalysisScope scope) {
        LOGGER.info("[+] Start BatchAction#analyze with project " + project + " and scope " + scope);
        Set<PsiFile> psiFiles = synchronizedSet(new HashSet<>());
        scope.accept(new PsiElementVisitor() {
            @Override
            public void visitFile(PsiFile psiFile) {
                super.visitFile(psiFile);
                psiFiles.add(psiFile);
            }
        });
        SaveActionsServiceManager.getService().guardedProcessPsiFiles(project, psiFiles, activateOnBatch, batch);
        LOGGER.info("End BatchAction#analyze processed " + psiFiles.size() + " files");
    }

}
