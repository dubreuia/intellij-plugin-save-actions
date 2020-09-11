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

package com.dubreuia.integration;

import org.junit.jupiter.api.Test;

import static com.dubreuia.integration.ActionTestFile.Reformat_KO_Import_KO;
import static com.dubreuia.integration.ActionTestFile.Reformat_KO_Import_OK;
import static com.dubreuia.integration.ActionTestFile.Reformat_KO_Rearrange_KO;
import static com.dubreuia.integration.ActionTestFile.Reformat_KO_Rearrange_OK;
import static com.dubreuia.integration.ActionTestFile.Reformat_OK_Import_KO;
import static com.dubreuia.integration.ActionTestFile.Reformat_OK_Import_OK;
import static com.dubreuia.integration.ActionTestFile.Reformat_OK_Rearrange_OK;
import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.activateOnBatch;
import static com.dubreuia.model.Action.activateOnShortcut;
import static com.dubreuia.model.Action.organizeImports;
import static com.dubreuia.model.Action.rearrange;
import static com.dubreuia.model.Action.reformat;

class GlobalIntegrationTest extends IntegrationTest {

    @Test
    void should_reformat_without_activation_produces_same_file() {
        storage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_reformat_with_activation_produces_indented_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_OK_Import_KO);
    }

    @Test
    void should_reformat_with_shortcut_produces_same_file() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_reformat_with_shortcut_produces_indented_file_on_shortcut() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertSaveActionShortcut(Reformat_KO_Import_KO, Reformat_OK_Import_KO);
    }

    @Test
    void should_reformat_as_batch_produces_indented_file() {
        storage.setEnabled(activateOnBatch, true);
        storage.setEnabled(reformat, true);
        assertSaveActionBatch(Reformat_KO_Import_KO, Reformat_OK_Import_KO);
    }

    @Test
    void should_reformat_as_batch_on_shortcut_produces_same_file() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertSaveActionBatch(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_import_without_activation_produces_same_file() {
        storage.setEnabled(organizeImports, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_import_with_activation_produces_cleaned_import_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(organizeImports, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_OK);
    }

    @Test
    void should_import_and_format_with_activation_produces_cleaned_import_and_formated_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(organizeImports, true);
        storage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_OK_Import_OK);
    }

    @Test
    void should_rearrange_without_activation_produces_same_file() {
        storage.setEnabled(rearrange, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_rearrange_with_activation_produces_ordered_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(rearrange, true);
        assertSaveAction(Reformat_KO_Rearrange_KO, Reformat_KO_Rearrange_OK);
    }

    @Test
    void should_rearrange_and_format_with_activation_produces_ordered_file_and_formated_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(reformat, true);
        storage.setEnabled(rearrange, true);
        assertSaveAction(Reformat_KO_Rearrange_KO, Reformat_OK_Rearrange_OK);
    }

}
