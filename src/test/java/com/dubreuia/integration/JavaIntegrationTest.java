package com.dubreuia.integration;

import com.dubreuia.core.SaveActionFactory;
import com.dubreuia.core.component.SaveActionManager;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.dubreuia.integration.ActionTestFile.AccessCanBeTightened_KO;
import static com.dubreuia.integration.ActionTestFile.AccessCanBeTightened_OK;
import static com.dubreuia.integration.ActionTestFile.CustomUnqualifiedStaticMemberAccess_KO;
import static com.dubreuia.integration.ActionTestFile.CustomUnqualifiedStaticMemberAccess_OK;
import static com.dubreuia.integration.ActionTestFile.ExplicitTypeCanBeDiamond_KO;
import static com.dubreuia.integration.ActionTestFile.ExplicitTypeCanBeDiamond_OK;
import static com.dubreuia.integration.ActionTestFile.FieldCanBeFinal_KO;
import static com.dubreuia.integration.ActionTestFile.FieldCanBeFinal_OK;
import static com.dubreuia.integration.ActionTestFile.FinalPrivateMethod_KO;
import static com.dubreuia.integration.ActionTestFile.FinalPrivateMethod_OK;
import static com.dubreuia.integration.ActionTestFile.GenerateSerialVersionUID_KO;
import static com.dubreuia.integration.ActionTestFile.GenerateSerialVersionUID_OK;
import static com.dubreuia.integration.ActionTestFile.InspectionsAll_KO;
import static com.dubreuia.integration.ActionTestFile.InspectionsAll_OK;
import static com.dubreuia.integration.ActionTestFile.LocalCanBeFinal_KO;
import static com.dubreuia.integration.ActionTestFile.LocalCanBeFinal_OK;
import static com.dubreuia.integration.ActionTestFile.MethodMayBeStatic_KO;
import static com.dubreuia.integration.ActionTestFile.MethodMayBeStatic_OK;
import static com.dubreuia.integration.ActionTestFile.MissingOverrideAnnotation_KO;
import static com.dubreuia.integration.ActionTestFile.MissingOverrideAnnotation_OK;
import static com.dubreuia.integration.ActionTestFile.SuppressAnnotation_KO;
import static com.dubreuia.integration.ActionTestFile.SuppressAnnotation_OK;
import static com.dubreuia.integration.ActionTestFile.UnnecessaryFinalOnLocalVariableOrParameter_KO;
import static com.dubreuia.integration.ActionTestFile.UnnecessaryFinalOnLocalVariableOrParameter_OK;
import static com.dubreuia.integration.ActionTestFile.UnnecessarySemicolon_KO;
import static com.dubreuia.integration.ActionTestFile.UnnecessarySemicolon_OK;
import static com.dubreuia.integration.ActionTestFile.UnnecessaryThis_KO;
import static com.dubreuia.integration.ActionTestFile.UnnecessaryThis_OK;
import static com.dubreuia.integration.ActionTestFile.UnqualifiedFieldAccess_KO;
import static com.dubreuia.integration.ActionTestFile.UnqualifiedFieldAccess_OK;
import static com.dubreuia.integration.ActionTestFile.UnqualifiedMethodAccess_KO;
import static com.dubreuia.integration.ActionTestFile.UnqualifiedMethodAccess_OK;
import static com.dubreuia.integration.ActionTestFile.UnqualifiedStaticMemberAccess_KO;
import static com.dubreuia.integration.ActionTestFile.UnqualifiedStaticMemberAccess_OK;
import static com.dubreuia.integration.ActionTestFile.UseBlocks_KO;
import static com.dubreuia.integration.ActionTestFile.UseBlocks_OK;
import static com.dubreuia.model.Action.accessCanBeTightened;
import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.activateOnShortcut;
import static com.dubreuia.model.Action.customUnqualifiedStaticMemberAccess;
import static com.dubreuia.model.Action.explicitTypeCanBeDiamond;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.finalPrivateMethod;
import static com.dubreuia.model.Action.generateSerialVersionUID;
import static com.dubreuia.model.Action.localCanBeFinal;
import static com.dubreuia.model.Action.methodMayBeStatic;
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
        return new com.dubreuia.core.component.java.SaveActionManager();
    }

    @BeforeClass
    public void beforeClass() {
        SaveActionFactory.JAVA_AVAILABLE = true;
    }

    @Test
    public void should_fieldCanBeFinal_add_final_to_field() {
        storage.setEnabled(activate, true);
        storage.setEnabled(fieldCanBeFinal, true);
        assertSaveAction(FieldCanBeFinal_KO, FieldCanBeFinal_OK);
    }

    @Test
    public void should_fieldCanBeFinal_add_final_to_field_on_shortcut() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(fieldCanBeFinal, true);
        assertSaveActionShortcut(FieldCanBeFinal_KO, FieldCanBeFinal_OK);
    }

    @Test
    public void should_fieldCanBeFinal_add_final_to_field_on_batch() {
        storage.setEnabled(activate, true);
        storage.setEnabled(fieldCanBeFinal, true);
        assertSaveActionBatch(FieldCanBeFinal_KO, FieldCanBeFinal_OK);
    }

    @Test
    public void should_localCanBeFinal_add_final_to_local_variable_and_parameters() {
        storage.setEnabled(activate, true);
        storage.setEnabled(localCanBeFinal, true);
        assertSaveAction(LocalCanBeFinal_KO, LocalCanBeFinal_OK);
    }

    @Test
    @Disabled("do not work")
    public void should_methodMayBeStatic_add_static_keyword_to_method() {
        storage.setEnabled(activateOnShortcut, true);
        storage.setEnabled(methodMayBeStatic, true);
        assertSaveActionShortcut(MethodMayBeStatic_KO, MethodMayBeStatic_OK);
    }

    @Test
    public void should_unqualifiedFieldAccess_add_this_to_field_access() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unqualifiedFieldAccess, true);
        assertSaveAction(UnqualifiedFieldAccess_KO, UnqualifiedFieldAccess_OK);
    }

    @Test
    public void should_unqualifiedMethodAccess_add_this_to_method_access() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unqualifiedMethodAccess, true);
        assertSaveAction(UnqualifiedMethodAccess_KO, UnqualifiedMethodAccess_OK);
    }

    @Test
    public void should_unqualifiedStaticMemberAccess_add_this_to_method_access() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unqualifiedStaticMemberAccess, true);
        assertSaveAction(UnqualifiedStaticMemberAccess_KO, UnqualifiedStaticMemberAccess_OK);
    }

    @Test
    public void should_customUnqualifiedStaticMemberAccess_add_this_to_method_access() {
        storage.setEnabled(activate, true);
        storage.setEnabled(customUnqualifiedStaticMemberAccess, true);
        assertSaveAction(CustomUnqualifiedStaticMemberAccess_KO, CustomUnqualifiedStaticMemberAccess_OK);
    }

    @Test
    public void should_missingOverrideAnnotation_add_override_annotation() {
        storage.setEnabled(activate, true);
        storage.setEnabled(missingOverrideAnnotation, true);
        assertSaveAction(MissingOverrideAnnotation_KO, MissingOverrideAnnotation_OK);
    }

    @Test
    public void should_useBlocks_add_blocks_to_if_else_while_for() {
        storage.setEnabled(activate, true);
        storage.setEnabled(useBlocks, true);
        assertSaveAction(UseBlocks_KO, UseBlocks_OK);
    }

    @Test
    @Disabled("do not work")
    public void should_generateSerialVersionUID_generates_serial_version_uid_for_serializable_class() {
        storage.setEnabled(activate, true);
        storage.setEnabled(generateSerialVersionUID, true);
        assertSaveAction(GenerateSerialVersionUID_KO, GenerateSerialVersionUID_OK);
    }

    @Test
    public void should_unnecessaryThis_removes_this_on_method_and_field() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unnecessaryThis, true);
        assertSaveAction(UnnecessaryThis_KO, UnnecessaryThis_OK);
    }

    @Test
    public void should_finalPrivateMethod_removes_final_on_private_methods() {
        storage.setEnabled(activate, true);
        storage.setEnabled(finalPrivateMethod, true);
        assertSaveAction(FinalPrivateMethod_KO, FinalPrivateMethod_OK);
    }

    @Test
    public void should_unnecessaryFinalOnLocalVariableOrParameter_removes_final_on_local_varible_and_parameters() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unnecessaryFinalOnLocalVariableOrParameter, true);
        assertSaveAction(UnnecessaryFinalOnLocalVariableOrParameter_KO, UnnecessaryFinalOnLocalVariableOrParameter_OK);
    }

    @Test
    @Disabled("do not work")
    public void should_explicitTypeCanBeDiamond_removes_explicit_diamond() {
        storage.setEnabled(activate, true);
        storage.setEnabled(explicitTypeCanBeDiamond, true);
        assertSaveAction(ExplicitTypeCanBeDiamond_KO, ExplicitTypeCanBeDiamond_OK);
    }

    @Test
    public void should_suppressAnnotation_remove_unnecessary_suppress_warning_annotation() {
        storage.setEnabled(activate, true);
        storage.setEnabled(suppressAnnotation, true);
        assertSaveAction(SuppressAnnotation_KO, SuppressAnnotation_OK);
    }

    @Test
    public void should_unnecessarySemicolon_remove_unnecessary_semicolon() {
        storage.setEnabled(activate, true);
        storage.setEnabled(unnecessarySemicolon, true);
        assertSaveAction(UnnecessarySemicolon_KO, UnnecessarySemicolon_OK);
    }

    @Test
    public void should_accessCanBeTightened_remove_unnecessary_semicolon() {
        storage.setEnabled(activate, true);
        storage.setEnabled(accessCanBeTightened, true);
        assertSaveAction(AccessCanBeTightened_KO, AccessCanBeTightened_OK);
    }

    @Test
    @Disabled("do not work with multiple elements (only first works)")
    public void should_inspectionsAll_boogaloo() {
        storage.setEnabled(activate, true);
        storage.setEnabled(useBlocks, true);
        storage.setEnabled(accessCanBeTightened, true);
        storage.setEnabled(unnecessarySemicolon, true);
        assertSaveAction(InspectionsAll_KO, InspectionsAll_OK);
    }

}
