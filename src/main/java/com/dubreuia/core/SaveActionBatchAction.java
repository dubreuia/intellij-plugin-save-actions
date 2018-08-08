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
package com.dubreuia.core;

import com.dubreuia.processors.ProcessorACCESSOR;
import com.intellij.analysis.AnalysisScope;
import com.intellij.analysis.BaseAnalysisAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Runs the save actions on the given scope of files. The user is asked for the scope using a standard IDEA dialog.
 * <p>
 * Originally based on https://github.com/JetBrains/intellij-community/blob/master/java/java-impl/src/com/intellij/codeInspection/inferNullity/InferNullityAnnotationsAction.java
 *
 * @author markiewb
 */
public class SaveActionBatchAction extends BaseAnalysisAction {

    public static final Logger LOGGER = Logger.getInstance(SaveActionBatchAction.class);

    public SaveActionBatchAction() {
        super("Save Actions", "Save Actions");
    }

    @Override
    protected void analyze(@NotNull Project project, @NotNull AnalysisScope scope) {

        List<SaveActionManager> saveActionManagers = SaveActionFactory.getSaveActionManagers();
        LOGGER.debug("Running Save Actions on multiple files " + scope);
        AtomicLong fileCount = new AtomicLong();
        scope.accept(new PsiElementVisitor() {
            @Override
            public void visitFile(PsiFile psiFile) {
                super.visitFile(psiFile);
                fileCount.incrementAndGet();

                for (SaveActionManager saveActionManager : saveActionManagers) {
                    saveActionManager.checkAndProcessPsiFile(project, psiFile,
                            //don't compile in a batch action
                            x -> !ProcessorACCESSOR.getCompileProcessorClass().isInstance(x));
                }
            }
        });
        LOGGER.debug("Ran Save Actions on " + fileCount.get() + " files ");

    }


}