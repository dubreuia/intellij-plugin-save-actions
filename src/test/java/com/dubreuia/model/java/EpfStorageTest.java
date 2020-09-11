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

package com.dubreuia.model.java;

import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.HashSet;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.explicitTypeCanBeDiamond;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.localCanBeFinal;
import static com.dubreuia.model.Action.missingOverrideAnnotation;
import static com.dubreuia.model.Action.organizeImports;
import static com.dubreuia.model.Action.rearrange;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;
import static com.dubreuia.model.Action.unnecessarySemicolon;
import static com.dubreuia.model.Action.unqualifiedFieldAccess;
import static com.dubreuia.model.Action.unqualifiedStaticMemberAccess;
import static com.dubreuia.model.Action.useBlocks;
import static com.dubreuia.model.java.EpfTestConstants.EXAMPLE_EPF_0;
import static com.dubreuia.model.java.EpfTestConstants.EXAMPLE_EPF_1;
import static com.dubreuia.model.java.EpfTestConstants.EXAMPLE_EPF_2;
import static org.assertj.core.api.Assertions.assertThat;

class EpfStorageTest {

    private Storage storage;

    @BeforeEach
    void before() {
        storage = new Storage();

        storage.setActions(new HashSet<>());
        storage.setInclusions(new HashSet<>());
        storage.setExclusions(new HashSet<>());

        storage.getActions().add(activate);
        storage.getActions().add(reformat);
        storage.getActions().add(reformatChangedCode);
        storage.getActions().add(fieldCanBeFinal);
        storage.getActions().add(missingOverrideAnnotation);
        storage.getActions().add(unnecessarySemicolon);

        storage.getInclusions().add("inclusion1");
        storage.getInclusions().add("inclusion2");

        storage.getExclusions().add("exclusion1");
        storage.getExclusions().add("exclusion2");
    }

    @Test
    void should_storage_with_bad_configuration_path_returns_default_storage() {
        Storage epfStorage;

        epfStorage = EpfStorage.INSTANCE.getStorageOrDefault(storage.getConfigurationPath(), storage);
        assertThat(storage).isSameAs(epfStorage);

        storage.setConfigurationPath("bad path");
        epfStorage = EpfStorage.INSTANCE.getStorageOrDefault(storage.getConfigurationPath(), storage);
        assertThat(storage).isSameAs(epfStorage);
    }

    @Test
    void should_default_storage_values_are_copied_to_epf_storage_if_empty_file() {
        Storage epfStorage = EpfStorage.INSTANCE.getStorageOrDefault(EXAMPLE_EPF_0.toString(), storage);
        assertThat(epfStorage).isNotSameAs(storage);

        assertThat(epfStorage.getActions()).isNotSameAs(storage.getActions());
        assertThat(epfStorage.getInclusions()).isNotSameAs(storage.getInclusions());
        assertThat(epfStorage.getExclusions()).isNotSameAs(storage.getExclusions());

        assertThat(epfStorage.getActions()).isEqualTo(storage.getActions());
        assertThat(epfStorage.getInclusions()).isEqualTo(storage.getInclusions());
        assertThat(epfStorage.getExclusions()).isEqualTo(storage.getExclusions());
    }

    @Test
    void should_storage_values_are_correct_for_file_format_1() {
        Storage epfStorage = EpfStorage.INSTANCE.getStorageOrDefault(EXAMPLE_EPF_1.toString(), storage);
        assertThat(epfStorage).isNotSameAs(storage);

        EnumSet<Action> expected = EnumSet.of(
                // from default store (not in java)
                activate,
                // in both store
                reformat,
                // added by java
                useBlocks,
                // added by java
                unqualifiedFieldAccess,
                // added by java
                localCanBeFinal,
                // added by java
                rearrange,
                // added by java
                organizeImports,
                // added by java
                explicitTypeCanBeDiamond,
                // added by java
                missingOverrideAnnotation,
                // from default store (not in java)
                unnecessarySemicolon
                // removed by java : fieldCanBeFinal
                // removed by java : reformatChangedCode
        );

        assertThat(epfStorage.getActions()).isEqualTo(expected);
        assertThat(epfStorage.getInclusions()).isEqualTo(storage.getInclusions());
        assertThat(epfStorage.getExclusions()).isEqualTo(storage.getExclusions());
    }

    @Test
    void should_storage_values_are_correct_for_file_format_2() {
        Storage epfStorage = EpfStorage.INSTANCE.getStorageOrDefault(EXAMPLE_EPF_2.toString(), storage);
        assertThat(epfStorage).isNotSameAs(storage);

        EnumSet<Action> expected = EnumSet.of(
                missingOverrideAnnotation,
                unnecessarySemicolon,
                rearrange,
                unqualifiedStaticMemberAccess,
                useBlocks,
                activate,
                organizeImports,
                unqualifiedFieldAccess,
                explicitTypeCanBeDiamond,
                localCanBeFinal,
                fieldCanBeFinal
        );

        assertThat(epfStorage.getActions()).isEqualTo(expected);
        assertThat(epfStorage.getInclusions()).isEqualTo(storage.getInclusions());
        assertThat(epfStorage.getExclusions()).isEqualTo(storage.getExclusions());
    }

}
