package com.dubreuia.integration;

import org.junit.jupiter.api.Test;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.activateOnShortcut;
import static com.dubreuia.model.Action.reformat;

public class ActivationIntegrationTest extends IntegrationTest {

    @Test
    public void should_reformat_without_activation_produces_same_file() {
        storage.setEnabled(reformat, true);
        assertFormat(NOT_INDENTED, NOT_INDENTED, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_reformat_with_activation_produces_indented_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(reformat, true);
        assertFormat(NOT_INDENTED, INDENTED, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_reformat_with_shortcut_produces_same_file() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertFormat(NOT_INDENTED, NOT_INDENTED, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_reformat_with_shortcut_produces_indented_file_on_shortcut() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(reformat, true);
        assertFormat(NOT_INDENTED, INDENTED, SAVE_ACTION_SHORTCUT_MANAGER);
    }

}
