/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dubreuia.core.action;

import com.dubreuia.core.SaveActionFactory;
import com.dubreuia.core.component.SaveActionManager;
import com.intellij.analysis.AnalysisScope;
import com.intellij.analysis.BaseAnalysisAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

import static com.dubreuia.core.ExecutionMode.batch;
import static com.dubreuia.core.component.Component.COMPONENT_NAME;
import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.activate;

/**
 * This action runs the save actions on the given scope of files. The user is asked for the scope using a
 * standard IDEA dialog. It delegates to {@link SaveActionManager}. Originally based on
 * {@link com.intellij.codeInspection.inferNullity.InferNullityAnnotationsAction}.
 *
 * @author markiewb
 * @see SaveActionManager
 */
public class BatchAction extends BaseAnalysisAction {

    public BatchAction() {
        super(COMPONENT_NAME, COMPONENT_NAME);
    }

    @Override
    protected void analyze(@NotNull Project project, @NotNull AnalysisScope scope) {
        LOGGER.info("Running SaveActionBatchAction on " + project + " with scope " + scope);
        AtomicLong fileCount = new AtomicLong();
        scope.accept(new PsiElementVisitor() {
            @Override
            public void visitFile(PsiFile psiFile) {
                super.visitFile(psiFile);
                fileCount.incrementAndGet();
                SaveActionFactory.streamManagers()
                        .filter(manager -> manager.getStorage(project).isEnabled(activate))
                        .forEach(manager -> manager.processPsiFileIfNecessary(project, psiFile, batch));
            }
        });
        LOGGER.info("Executed SaveActionBatchAction on " + fileCount.get() + " files ");
    }

}