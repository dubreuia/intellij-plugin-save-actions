package com.dubreuia.integration;

import com.dubreuia.model.Storage;

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

class CommonsIntegrationTest extends IntegrationTest {

    @StoragesTest
    void should_reformat_without_activation_produces_same_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @StoragesTest
    void should_reformat_with_activation_produces_indented_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(activate, true);
        storage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_OK_Import_KO);
    }

    @StoragesTest
    void should_reformat_with_shortcut_produces_same_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @StoragesTest
    void should_reformat_with_shortcut_produces_indented_file_on_shortcut(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertSaveActionShortcut(Reformat_KO_Import_KO, Reformat_OK_Import_KO);
    }

    @StoragesTest
    void should_reformat_as_batch_produces_indented_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(activateOnBatch, true);
        storage.setEnabled(reformat, true);
        assertSaveActionBatch(Reformat_KO_Import_KO, Reformat_OK_Import_KO);
    }

    @StoragesTest
    void should_reformat_as_batch_on_shortcut_produces_same_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertSaveActionBatch(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @StoragesTest
    void should_import_without_activation_produces_same_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(organizeImports, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @StoragesTest
    void should_import_with_activation_produces_cleaned_import_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(activate, true);
        storage.setEnabled(organizeImports, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_OK);
    }

    @StoragesTest
    void should_import_and_format_with_activation_produces_cleaned_import_and_formated_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(activate, true);
        storage.setEnabled(organizeImports, true);
        storage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_OK_Import_OK);
    }

    @StoragesTest
    void should_rearrange_without_activation_produces_same_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(rearrange, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @StoragesTest
    void should_rearrange_with_activation_produces_ordered_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(activate, true);
        storage.setEnabled(rearrange, true);
        assertSaveAction(Reformat_KO_Rearrange_KO, Reformat_KO_Rearrange_OK);
    }

    @StoragesTest
    void should_rearrange_and_format_with_activation_produces_ordered_file_and_formated_file(StorageToTest storageToTest) {
        Storage storage = usingStorage(storageToTest);
        storage.setEnabled(activate, true);
        storage.setEnabled(reformat, true);
        storage.setEnabled(rearrange, true);
        assertSaveAction(Reformat_KO_Rearrange_KO, Reformat_OK_Rearrange_OK);
    }

}
