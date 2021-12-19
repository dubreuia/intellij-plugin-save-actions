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

import com.dubreuia.processors.BuildProcessor;
import com.dubreuia.processors.GlobalProcessor;
import com.dubreuia.processors.java.JavaProcessor;

import static com.dubreuia.model.StorageFactory.JAVA;

/**
 * This ApplicationService implementation is used for all JAVA based IDE flavors.
 * <p/>
 * It is assigned as ExtensionPoint from inside plugin-java.xml and overrides the default implementation
 * {@link SaveActionsDefaultService} which is not being loaded for Intellij IDEA, Android Studio a.s.o.
 * Instead this implementation will be assigned. Thus, all processors have to be configured by this class as well.
 * <p/>
 * Services must be final classes as per definition. That is the reason to use an abstract class here.
 * <p/>
 *
 * @see AbstractSaveActionsService
 * @since 2.4.0
 */
public final class SaveActionsJavaService extends AbstractSaveActionsService {

    public SaveActionsJavaService() {
        super(JAVA);
        addProcessors(BuildProcessor.stream());
        addProcessors(GlobalProcessor.stream());
        addProcessors(JavaProcessor.stream());
    }
}
