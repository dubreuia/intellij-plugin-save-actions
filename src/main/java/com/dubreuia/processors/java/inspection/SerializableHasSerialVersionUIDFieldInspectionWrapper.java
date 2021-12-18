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

package com.dubreuia.processors.java.inspection;

import com.dubreuia.core.service.SaveActionsService;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.diagnostic.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class changes package between intellij versions.
 *
 * @see com.intellij.codeInspection.SerializableHasSerialVersionUidFieldInspection
 */
public class SerializableHasSerialVersionUIDFieldInspectionWrapper {

    private static final Logger LOGGER = Logger.getInstance(SaveActionsService.class);

    private SerializableHasSerialVersionUIDFieldInspectionWrapper() {
    }

    public static LocalInspectionTool get() {
        return Arrays.stream(SerializableClass.values())
                .map(SerializableClass::getInspectionInstance)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot find inspection tool SerializableHasSerialVersionUIDFieldInspection"));
    }

    private enum SerializableClass {
        CLASS_NAME_INTELLIJ_2021_3("com.intellij.codeInspection.SerializableHasSerialVersionUidFieldInspection",
                "com.dubreuia.processors.java.inspection.CustomSerializableHasSerialVersionUidFieldInspection"),
        CLASS_NAME_INTELLIJ_2018_3("com.siyeh.ig.serialization.SerializableHasSerialVersionUIDFieldInspection",
                "com.siyeh.ig.serialization.SerializableHasSerialVersionUIDFieldInspection"),
        CLASS_NAME_INTELLIJ_2016("com.siyeh.ig.serialization.SerializableHasSerialVersionUIDFieldInspectionBase",
                "com.siyeh.ig.serialization.SerializableHasSerialVersionUIDFieldInspectionBase");

        /**
         * Field className: Inspection class provided by IDE
         */
        private final String className;

        /**
         * Field targetClass: Inspection class to run. Needed to apply wrapper class for Idea 2021.3 and up.
         *
         * @see CustomSerializableHasSerialVersionUidFieldInspection
         */
        private final String targetClass;

        SerializableClass(String className, String targetClass) {
            this.className = className;
            this.targetClass = targetClass;
        }

        public LocalInspectionTool getInspectionInstance() {
            try {
                Class.forName(className).asSubclass(LocalInspectionTool.class);
                Class<? extends LocalInspectionTool> targetInspectionClass =
                        Class.forName(targetClass).asSubclass(LocalInspectionTool.class);
                LOGGER.info(String.format("Found serial version uid class %s", targetInspectionClass.getName()));
                return targetInspectionClass.cast(targetInspectionClass.getDeclaredConstructor().newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                return null;
            }
        }
    }
}
