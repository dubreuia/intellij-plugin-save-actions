package com.dubreuia.integration;

public enum ActionTestFile {

    Reformat_KO_Import_KO,
    Reformat_KO_Import_OK,
    Reformat_OK_Import_KO,
    Reformat_OK_Import_OK,

    Reformat_KO_Rearrange_KO,
    Reformat_KO_Rearrange_OK,
    Reformat_OK_Rearrange_OK,

    FieldCanBeFinal_KO,
    FieldCanBeFinal_OK,

    LocalCanBeFinal_KO,
    LocalCanBeFinal_OK,

    MethodMayBeStatic_KO,
    MethodMayBeStatic_OK,

    UnqualifiedFieldAccess_KO,
    UnqualifiedFieldAccess_OK,

    UnqualifiedMethodAccess_KO,
    UnqualifiedMethodAccess_OK,

    UnqualifiedStaticMemberAccess_KO,
    UnqualifiedStaticMemberAccess_OK,

    CustomUnqualifiedStaticMemberAccess_KO,
    CustomUnqualifiedStaticMemberAccess_OK,

    MissingOverrideAnnotation_KO,
    MissingOverrideAnnotation_OK,

    UseBlocks_KO,
    UseBlocks_OK,

    GenerateSerialVersionUID_KO,
    GenerateSerialVersionUID_OK,

    UnnecessaryThis_KO,
    UnnecessaryThis_OK,

    FinalPrivateMethod_KO,
    FinalPrivateMethod_OK,

    UnnecessaryFinalOnLocalVariableOrParameter_KO,
    UnnecessaryFinalOnLocalVariableOrParameter_OK,

    ExplicitTypeCanBeDiamond_KO,
    ExplicitTypeCanBeDiamond_OK,

    SuppressAnnotation_KO,
    SuppressAnnotation_OK,

    UnnecessarySemicolon_KO,
    UnnecessarySemicolon_OK,

    AccessCanBeTightened_KO,
    AccessCanBeTightened_OK,

    InspectionsAll_KO,
    InspectionsAll_OK,

    //
    ;

    public String getFilename() {
        return name() + ".java";
    }

}
