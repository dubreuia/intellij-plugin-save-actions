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

package com.dubreuia.core.component;

import com.dubreuia.processors.BuildProcessor;
import com.dubreuia.processors.GlobalProcessor;
import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.StorageFactory.DEFAULT;
import static com.intellij.AppTopics.FILE_DOCUMENT_SYNC;

/**
 * The plugin entry class that is instantiated (or reused) and delegated to {@link SaveActionManager}. This is not a
 * Singleton, for Java based ide the corresponding component is also instantiated (see {@link JavaComponent}).
 * <p>
 * To be able to dynamically reload or update the Plugin we have to implement {@link DynamicPluginListener}
 * which is used on reload the Plugin of an already opened project
 * and {@link ProjectManagerListener} to instantiate the Plugin while opening projects.
 *
 * @see SaveActionManager
 */
public class Component implements DynamicPluginListener, ProjectManagerListener {

    public static final String COMPONENT_NAME = "Save Actions";

    protected void init() {
        LOGGER.info("Starting component: " + COMPONENT_NAME);

        SaveActionManager manager = SaveActionManager.INSTANCE
                .setStorageFactory(DEFAULT)
                .addProcessors(BuildProcessor.stream())
                .addProcessors(GlobalProcessor.stream());

        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();
        connection.subscribe(FILE_DOCUMENT_SYNC, manager);
    }

    protected void disposeComponent() {
        LOGGER.info("Stopping component: " + COMPONENT_NAME);
    }

    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        init();
    }

    @Override
    public void pluginUnloaded(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        disposeComponent();
    }

    @Override
    public void projectOpened(@NotNull Project project) {
        init();
    }

    @Override
    public void projectClosed(@NotNull Project project) {
        disposeComponent();
    }
}
