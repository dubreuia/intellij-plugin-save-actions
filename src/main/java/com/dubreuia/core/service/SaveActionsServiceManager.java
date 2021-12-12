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

package com.dubreuia.core.service;

import com.dubreuia.core.service.impl.SaveActionsDefaultService;
import com.dubreuia.core.service.impl.SaveActionsJavaService;
import com.intellij.openapi.application.ApplicationManager;

/**
 * SaveActionsServiceManager is providing the concrete service implementation.
 * All actions are handled by the {@link SaveActionsService} implementation.
 *
 * @see SaveActionsDefaultService
 * @see SaveActionsJavaService
 */
public class SaveActionsServiceManager {


    private SaveActionsServiceManager() {
    }

    public static SaveActionsService getService() {
        return ServiceHandler.INSTANCE.getService();
    }

    private enum ServiceHandler {

        INSTANCE;

        private static SaveActionsService service;

        public SaveActionsService getService() {
            if (service == null) {
                newService();
            }
            return service;
        }

        private void newService() {
            service = ApplicationManager.getApplication().getService(SaveActionsJavaService.class);
            if (service == null) {
                service = ApplicationManager.getApplication().getService(SaveActionsDefaultService.class);
            }
        }
    }
}
