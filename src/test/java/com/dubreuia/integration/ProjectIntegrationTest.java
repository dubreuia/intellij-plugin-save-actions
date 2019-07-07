package com.dubreuia.integration;

import org.junit.jupiter.api.BeforeEach;
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
import static com.dubreuia.model.Action.useGlobalConfiguration;

class ProjectIntegrationTest extends IntegrationTest {

    @BeforeEach
    void useProjectConfiguration() {
        projectStorage.setEnabled(useGlobalConfiguration, false);
    }

    @Test
    void should_reformat_without_activation_produces_same_file() {
        projectStorage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_reformat_with_activation_produces_indented_file() {
        projectStorage.setEnabled(activate, true);
        projectStorage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_OK_Import_KO);
    }

    @Test
    void should_reformat_with_shortcut_produces_same_file() {
        projectStorage.setEnabled(activateOnShortcut, true);
        projectStorage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_reformat_with_shortcut_produces_indented_file_on_shortcut() {
        projectStorage.setEnabled(activateOnShortcut, true);
        projectStorage.setEnabled(reformat, true);
        assertSaveActionShortcut(Reformat_KO_Import_KO, Reformat_OK_Import_KO);
    }

    @Test
    void should_reformat_as_batch_produces_indented_file() {
        projectStorage.setEnabled(activateOnBatch, true);
        projectStorage.setEnabled(reformat, true);
        assertSaveActionBatch(Reformat_KO_Import_KO, Reformat_OK_Import_KO);
    }

    @Test
    void should_reformat_as_batch_on_shortcut_produces_same_file() {
        projectStorage.setEnabled(activateOnShortcut, true);
        projectStorage.setEnabled(reformat, true);
        assertSaveActionBatch(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_import_without_activation_produces_same_file() {
        projectStorage.setEnabled(organizeImports, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_import_with_activation_produces_cleaned_import_file() {
        projectStorage.setEnabled(activate, true);
        projectStorage.setEnabled(organizeImports, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_OK);
    }

    @Test
    void should_import_and_format_with_activation_produces_cleaned_import_and_formated_file() {
        projectStorage.setEnabled(activate, true);
        projectStorage.setEnabled(organizeImports, true);
        projectStorage.setEnabled(reformat, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_OK_Import_OK);
    }

    @Test
    void should_rearrange_without_activation_produces_same_file() {
        projectStorage.setEnabled(rearrange, true);
        assertSaveAction(Reformat_KO_Import_KO, Reformat_KO_Import_KO);
    }

    @Test
    void should_rearrange_with_activation_produces_ordered_file() {
        projectStorage.setEnabled(activate, true);
        projectStorage.setEnabled(rearrange, true);
        assertSaveAction(Reformat_KO_Rearrange_KO, Reformat_KO_Rearrange_OK);
    }

    @Test
    void should_rearrange_and_format_with_activation_produces_ordered_file_and_formated_file() {
        projectStorage.setEnabled(activate, true);
        projectStorage.setEnabled(reformat, true);
        projectStorage.setEnabled(rearrange, true);
        assertSaveAction(Reformat_KO_Rearrange_KO, Reformat_OK_Rearrange_OK);
    }

}
