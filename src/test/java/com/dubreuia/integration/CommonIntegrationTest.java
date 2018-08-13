package com.dubreuia.integration;

import com.dubreuia.core.component.SaveActionManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.dubreuia.integration.ActionTestFile.FieldCanBeFinal_KO;
import static com.dubreuia.integration.ActionTestFile.Import_KO_Reformat_KO;
import static com.dubreuia.integration.ActionTestFile.Import_KO_Reformat_OK;
import static com.dubreuia.integration.ActionTestFile.Import_OK_Reformat_KO;
import static com.dubreuia.integration.ActionTestFile.Import_OK_Reformat_OK;
import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.activateOnShortcut;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.organizeImports;
import static com.dubreuia.model.Action.reformat;

public class CommonIntegrationTest extends IntegrationTest {

    @Override
    SaveActionManager getSaveActionManager() {
        return new SaveActionManager();
    }

    @Test
    public void should_reformat_without_activation_produces_same_file() {
        storage.setEnabled(reformat, true);
        assertSaveAction(Import_KO_Reformat_KO, Import_KO_Reformat_KO);
    }

    @Test
    public void should_reformat_with_activation_produces_indented_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(reformat, true);
        assertSaveAction(Import_KO_Reformat_KO, Import_KO_Reformat_OK);
    }

    @Test
    public void should_reformat_with_shortcut_produces_same_file() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertSaveAction(Import_KO_Reformat_KO, Import_KO_Reformat_KO);
    }

    @Test
    public void should_reformat_with_shortcut_produces_indented_file_on_shortcut() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertSaveActionShortcut(Import_KO_Reformat_KO, Import_KO_Reformat_OK);
    }

    @Test
    @Disabled("only works for ui")
    public void should_reformat_as_batch_produces_indented_file() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertSaveActionBatch(Import_KO_Reformat_KO, Import_KO_Reformat_OK);
    }

    @Test
    public void should_import_without_activation_produces_same_file() {
        storage.setEnabled(organizeImports, true);
        assertSaveAction(Import_KO_Reformat_KO, Import_KO_Reformat_KO);
    }

    @Test
    public void should_import_with_activation_produces_cleaned_import_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(organizeImports, true);
        assertSaveAction(Import_KO_Reformat_KO, Import_OK_Reformat_KO);
    }

    @Test
    public void should_import_and_format_with_activation_produces_cleaned_import_and_formated_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(organizeImports, true);
        storage.setEnabled(reformat, true);
        assertSaveAction(Import_KO_Reformat_KO, Import_OK_Reformat_OK);
    }

    @Test
    public void should_java_quick_fix_do_not_work_with_common_save_action_manager() {
        storage.setEnabled(activate, true);
        storage.setEnabled(fieldCanBeFinal, true);
        assertSaveAction(FieldCanBeFinal_KO, FieldCanBeFinal_KO);
    }

}
