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

import com.dubreuia.core.component.SaveActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.dubreuia.core.ExecutionMode.shortcut;
import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.activateOnShortcut;
import static com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE;
import static java.util.Collections.singletonList;

/**
 * This action runs the plugin on shortcut, only if property {@link com.dubreuia.model.Action#activateOnShortcut} is
 * enabled. It delegates to {@link SaveActionManager}.
 *
 * @see SaveActionManager
 */
public class ShortcutAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        LOGGER.info("[+] Start ShortcutAction#actionPerformed with event " + event);
        PsiFile psiFile = event.getData(PSI_FILE);
        Project project = event.getProject();
        Set<PsiFile> psiFiles = new HashSet<>(singletonList(psiFile));
        SaveActionManager.INSTANCE.guardedProcessPsiFiles(project, psiFiles, activateOnShortcut, shortcut);
        LOGGER.info("End ShortcutAction#actionPerformed processed " + psiFiles.size() + " files");
    }

}
