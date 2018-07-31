package com.dubreuia.integration;

import com.dubreuia.core.SaveActionFactory;
import com.dubreuia.core.SaveActionManager;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.dubreuia.integration.ActionFile.CustomUnqualifiedStaticMemberAccess_KO;
import static com.dubreuia.integration.ActionFile.CustomUnqualifiedStaticMemberAccess_OK;
import static com.dubreuia.integration.ActionFile.ExplicitTypeCanBeDiamond_KO;
import static com.dubreuia.integration.ActionFile.ExplicitTypeCanBeDiamond_OK;
import static com.dubreuia.integration.ActionFile.FieldCanBeFinal_KO;
import static com.dubreuia.integration.ActionFile.FieldCanBeFinal_OK;
import static com.dubreuia.integration.ActionFile.FinalPrivateMethod_KO;
import static com.dubreuia.integration.ActionFile.FinalPrivateMethod_OK;
import static com.dubreuia.integration.ActionFile.LocalCanBeFinal_KO;
import static com.dubreuia.integration.ActionFile.LocalCanBeFinal_OK;
import static com.dubreuia.integration.ActionFile.MissingOverrideAnnotation_KO;
import static com.dubreuia.integration.ActionFile.MissingOverrideAnnotation_OK;
import static com.dubreuia.integration.ActionFile.SuppressAnnotation_KO;
import static com.dubreuia.integration.ActionFile.SuppressAnnotation_OK;
import static com.dubreuia.integration.ActionFile.UnnecessaryFinalOnLocalVariableOrParameter_KO;
import static com.dubreuia.integration.ActionFile.UnnecessaryFinalOnLocalVariableOrParameter_OK;
import static com.dubreuia.integration.ActionFile.UnnecessarySemicolon_KO;
import static com.dubreuia.integration.ActionFile.UnnecessarySemicolon_OK;
import static com.dubreuia.integration.ActionFile.UnnecessaryThis_KO;
import static com.dubreuia.integration.ActionFile.UnnecessaryThis_OK;
import static com.dubreuia.integration.ActionFile.UnqualifiedFieldAccess_KO;
import static com.dubreuia.integration.ActionFile.UnqualifiedFieldAccess_OK;
import static com.dubreuia.integration.ActionFile.UnqualifiedMethodAccess_KO;
import static com.dubreuia.integration.ActionFile.UnqualifiedMethodAccess_OK;
import static com.dubreuia.integration.ActionFile.UnqualifiedStaticMemberAccess_KO;
import static com.dubreuia.integration.ActionFile.UnqualifiedStaticMemberAccess_OK;
import static com.dubreuia.integration.ActionFile.UseBlocks_KO;
import static com.dubreuia.integration.ActionFile.UseBlocks_OK;
import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.activateOnShortcut;
import static com.dubreuia.model.Action.customUnqualifiedStaticMemberAccess;
import static com.dubreuia.model.Action.explicitTypeCanBeDiamond;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.finalPrivateMethod;
import static com.dubreuia.model.Action.localCanBeFinal;
import static com.dubreuia.model.Action.missingOverrideAnnotation;
import static com.dubreuia.model.Action.suppressAnnotation;
import static com.dubreuia.model.Action.unnecessaryFinalOnLocalVariableOrParameter;
import static com.dubreuia.model.Action.unnecessarySemicolon;
import static com.dubreuia.model.Action.unnecessaryThis;
import static com.dubreuia.model.Action.unqualifiedFieldAccess;
import static com.dubreuia.model.Action.unqualifiedMethodAccess;
import static com.dubreuia.model.Action.unqualifiedStaticMemberAccess;
import static com.dubreuia.model.Action.useBlocks;

public class JavaIntegrationTest extends IntegrationTest {

    @Override
    SaveActionManager getSaveActionManager() {
        return new com.dubreuia.core.java.SaveActionManager();
    }

    @BeforeClass
    public void beforeClass() {
        SaveActionFactory.JAVA_AVAILABLE = true;
    }

    @Test
    public void should_fieldCanBeFinal_add_final_to_field() {
        storage.setEnabled(activate, true);
        storage.setEnabled(fieldCanBeFinal, true);
        assertFormat(FieldCanBeFinal_KO, FieldCanBeFinal_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_fieldCanBeFinal_add_final_to_field_on_shortcut() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(fieldCanBeFinal, true);
        assertFormat(FieldCanBeFinal_KO, FieldCanBeFinal_OK, SAVE_ACTION_SHORTCUT_MANAGER);
    }

    @Test
    public void should_localCanBeFinal_add_final_to_local_variable_and_parameters() {
        storage.setEnabled(activate, true);
        storage.setEnabled(localCanBeFinal, true);
        assertFormat(LocalCanBeFinal_KO, LocalCanBeFinal_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_unqualifiedFieldAccess_add_this_to_field_access() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unqualifiedFieldAccess, true);
        assertFormat(UnqualifiedFieldAccess_KO, UnqualifiedFieldAccess_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_unqualifiedMethodAccess_add_this_to_method_access() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unqualifiedMethodAccess, true);
        assertFormat(UnqualifiedMethodAccess_KO, UnqualifiedMethodAccess_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    @Disabled("I don't understand what it should do")
    public void should_unqualifiedStaticMemberAccess_add_this_to_method_access() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unqualifiedStaticMemberAccess, true);
        assertFormat(UnqualifiedStaticMemberAccess_KO, UnqualifiedStaticMemberAccess_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    @Disabled("I don't understand what it should do")
    public void should_customUnqualifiedStaticMemberAccess_add_this_to_method_access() {
        storage.setEnabled(activate, true);
        storage.setEnabled(customUnqualifiedStaticMemberAccess, true);
        assertFormat(CustomUnqualifiedStaticMemberAccess_KO, CustomUnqualifiedStaticMemberAccess_OK,
                SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_missingOverrideAnnotation_add_override_annotation() {
        storage.setEnabled(activate, true);
        storage.setEnabled(missingOverrideAnnotation, true);
        assertFormat(MissingOverrideAnnotation_KO, MissingOverrideAnnotation_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_useBlocks_add_blocks_to_if_else_while_for() {
        storage.setEnabled(activate, true);
        storage.setEnabled(useBlocks, true);
        assertFormat(UseBlocks_KO, UseBlocks_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_unnecessaryThis_removes_this_on_method_and_field() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unnecessaryThis, true);
        assertFormat(UnnecessaryThis_KO, UnnecessaryThis_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_finalPrivateMethod_removes_final_on_private_methods() {
        storage.setEnabled(activate, true);
        storage.setEnabled(finalPrivateMethod, true);
        assertFormat(FinalPrivateMethod_KO, FinalPrivateMethod_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_unnecessaryFinalOnLocalVariableOrParameter_removes_final_on_local_varible_and_parameters() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unnecessaryFinalOnLocalVariableOrParameter, true);
        assertFormat(UnnecessaryFinalOnLocalVariableOrParameter_KO, UnnecessaryFinalOnLocalVariableOrParameter_OK,
                SAVE_ACTION_MANAGER);
    }

    @Test
    @Disabled("Doesn't work")
    public void should_explicitTypeCanBeDiamond_removes_explicit_diamond() {
        storage.setEnabled(activate, true);
        storage.setEnabled(explicitTypeCanBeDiamond, true);
        assertFormat(ExplicitTypeCanBeDiamond_KO, ExplicitTypeCanBeDiamond_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_suppressAnnotation_remove_unnecessary_suppress_warning_annotation() {
        storage.setEnabled(activate, true);
        storage.setEnabled(suppressAnnotation, true);
        assertFormat(SuppressAnnotation_KO, SuppressAnnotation_OK, SAVE_ACTION_MANAGER);
    }

    @Test
    public void should_unnecessarySemicolon_remove_unnecessary_semicolon() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unnecessarySemicolon, true);
        assertFormat(UnnecessarySemicolon_KO, UnnecessarySemicolon_OK, SAVE_ACTION_MANAGER);
    }

}
