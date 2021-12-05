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

import com.intellij.codeInspection.LocalInspectionTool;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.stream.Stream;

import static com.dubreuia.core.component.SaveActionManager.LOGGER;

/**
 * This class changes package between intellij versions.
 *
 * @see com.intellij.codeInspection.SerializableHasSerialVersionUidFieldInspection
 */
public class SerializableHasSerialVersionUIDFieldInspectionWrapper {

    private static final String CLASS_NAME_INTELLIJ_2016 =
            "com.siyeh.ig.serialization.SerializableHasSerialVersionUIDFieldInspectionBase";
    private static final String CLASS_NAME_INTELLIJ_2018_3 =
            "com.siyeh.ig.serialization.SerializableHasSerialVersionUIDFieldInspection";
    private static final String CLASS_NAME_INTELLIJ_2021_3 =
            "com.intellij.codeInspection.SerializableHasSerialVersionUidFieldInspection";

    private SerializableHasSerialVersionUIDFieldInspectionWrapper() {
    }

    public static LocalInspectionTool get() {

        return Stream.of(CLASS_NAME_INTELLIJ_2016, CLASS_NAME_INTELLIJ_2018_3,
                        CLASS_NAME_INTELLIJ_2021_3)
                .map(SerializableHasSerialVersionUIDFieldInspectionWrapper::getInspectionInstance)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(inspectionTool -> LOGGER.info(
                        "Found serial version uid class " + inspectionTool.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot find inspection tool SerializableHasSerialVersionUIDFieldInspection"));
    }

    private static Optional<LocalInspectionTool> getInspectionInstance(String className) {

        try {
            Class<? extends LocalInspectionTool> inspectionClass = Class
                    .forName(className).asSubclass(LocalInspectionTool.class);
            LocalInspectionTool localInspectionTool =
                    inspectionClass.cast(inspectionClass.getDeclaredConstructor().newInstance());
            return Optional.of(localInspectionTool);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            return Optional.empty();
        }
    }

}
