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

package com.dubreuia.core.service.impl;

import com.dubreuia.core.ExecutionMode;
import com.dubreuia.core.component.Engine;
import com.dubreuia.core.service.SaveActionsService;
import com.dubreuia.model.Action;
import com.dubreuia.model.StorageFactory;
import com.dubreuia.processors.Processor;
import com.intellij.openapi.actionSystem.ex.QuickList;
import com.intellij.openapi.actionSystem.ex.QuickListsManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.dubreuia.model.StorageFactory.JAVA;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Super class for all ApplicationServices. All actions are routed here.
 * ApplicationServices are Singleton implementations by default.
 * <p>
 * The main method is {@link #guardedProcessPsiFiles(Project, Set, Action, ExecutionMode)} and will delegate to
 * {@link Engine#processPsiFilesIfNecessary()}. The method will check if the file needs to be processed and uses the
 * processors to apply the modifications.
 * <p>
 * The psi files are ide wide, that means they are shared between projects (and editor windows), so we need to check if
 * the file is physically in that project before reformatting, or else the file is formatted twice and intellij will ask
 * to confirm unlocking of non-project file in the other project, see {@link Engine} for more details.
 *
 * @since 2.3.0
 */
abstract class AbstractSaveActionsService implements SaveActionsService {

    protected static final Logger LOGGER = Logger.getInstance(SaveActionsService.class);

    private final List<Processor> processors;
    private final StorageFactory storageFactory;
    private final boolean javaAvailable;
    private final boolean compilingAvailable;

    protected AbstractSaveActionsService(StorageFactory storageFactory) {
        LOGGER.info("Save Actions Service \"" + getClass().getSimpleName() + "\" initialized.");
        this.storageFactory = storageFactory;
        processors = new ArrayList<>();
        javaAvailable = JAVA.equals(storageFactory);
        compilingAvailable = initCompilingAvailable();
    }

    @Override
    public synchronized void guardedProcessPsiFiles(Project project, Set<PsiFile> psiFiles, Action activation, ExecutionMode mode) {
        if (ApplicationManager.getApplication().isDisposed()) {
            LOGGER.info("Application is closing, stopping invocation");
            return;
        }
        Engine engine = new Engine(storageFactory.getStorage(project), processors, project, psiFiles, activation, mode);
        engine.processPsiFilesIfNecessary();
    }

    @Override
    public boolean isJavaAvailable() {
        return javaAvailable;
    }

    @Override
    public boolean isCompilingAvailable() {
        return compilingAvailable;
    }

    @Override
    public List<QuickList> getQuickLists(Project project) {
        Map<Integer, QuickList> quickListsIds =
                Arrays.stream(QuickListsManager.getInstance().getAllQuickLists())
                        .collect(toMap(QuickList::hashCode, identity()));

        return Optional.ofNullable(storageFactory.getStorage(project))
                .map(storage -> storage.getQuickLists().stream()
                        .map(Integer::valueOf)
                        .map(quickListsIds::get)
                        .filter(Objects::nonNull)
                        .collect(toList()))
                .orElse(new ArrayList<>());
    }

    protected SaveActionsService addProcessors(Stream<Processor> processors) {
        processors.forEach(this.processors::add);
        this.processors.sort(new Processor.OrderComparator());
        return this;
    }

    private boolean initCompilingAvailable() {
        try {
            Class.forName("com.intellij.openapi.compiler.CompilerManager");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
