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

import com.dubreuia.processors.java.JavaProcessor;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;
import static com.dubreuia.model.StorageFactory.JAVA;

/**
 * The plugin entry class for Java based ide. This is not a singleton, the main component {@link Component} will also be
 * instantiated before. This class is instantiated afterwards.
 *
 * @see SaveActionManager
 * @see Component
 */
public class JavaComponent extends Component {

    private static final String COMPONENT_NAME = "Save Actions Java";

    @Override
    protected void init() {
        LOGGER.info("Starting component: " + COMPONENT_NAME);

        SaveActionManager.INSTANCE
                .setStorageFactory(JAVA)
                .enableJava()
                .addProcessors(JavaProcessor.stream());
    }

    @Override
    protected void disposeComponent() {
        LOGGER.info("Stopping component: " + COMPONENT_NAME);
    }

}
