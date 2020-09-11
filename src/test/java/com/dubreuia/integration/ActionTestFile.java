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

    LocalCanBeFinalExceptImplicit_KO,
    LocalCanBeFinalExceptImplicit_OK,

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

    SingleStatementInBlock_KO,
    SingleStatementInBlock_OK,

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
