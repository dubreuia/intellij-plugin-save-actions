package com.dubreuia.integration;

public enum ActionFile {

    FieldCanBeFinal_KO,
    FieldCanBeFinal_OK,

    LocalCanBeFinal_KO,
    LocalCanBeFinal_OK,

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

    Import_KO_Reformat_KO,
    Import_KO_Reformat_OK,
    Import_OK_Reformat_KO,
    Import_OK_Reformat_OK,

    //
    ;

    public String getFilename() {
        return name() + ".java";
    }

}
