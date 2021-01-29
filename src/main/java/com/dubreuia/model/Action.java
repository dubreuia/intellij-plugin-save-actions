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

package com.dubreuia.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static com.dubreuia.model.ActionType.activation;
import static com.dubreuia.model.ActionType.build;
import static com.dubreuia.model.ActionType.global;
import static com.dubreuia.model.ActionType.java;
import static com.dubreuia.model.ActionType.kotlin;
import static java.util.stream.Collectors.toSet;

public enum Action {

    // Activation

    activate("Activate save actions on save (before saving each file, performs the configured actions below)",
            activation, true),

    activateOnShortcut("Activate save actions on shortcut (default \"CTRL + SHIFT + S\")",
            activation, false),

    activateOnBatch("Activate save actions on batch (\"Code > Save Actions > Execute on multiple files\")",
            activation, false),

    noActionIfCompileErrors("No action if compile errors (applied per file)",
            activation, false),

    // Global

    organizeImports("Optimize imports",
            global, true),

    reformat("Reformat file",
            global, true),

    reformatChangedCode("Reformat only changed code (only if VCS configured)",
            global, false),

    rearrange("Rearrange fields and methods " +
            "(configured in \"File > Settings > Editor > Code Style > (...) > Arrangement\")",
            global, false),

    // Build

    compile("[experimental] Compile files (using \"Build > Build Project\")",
            build, false),

    reload("[experimental] Reload files in running debugger (using \"Run > Reload Changed Classes\")",
            build, false),

    executeAction("[experimental] Execute an action (using quick lists at " +
            "\"File > Settings > Appearance & Behavior > Quick Lists\")",
            build, false),

    // Java fixes

    fieldCanBeFinal("Add final modifier to field",
            java, false),

    localCanBeFinal("Add final modifier to local variable or parameter",
            java, false),

    localCanBeFinalExceptImplicit("Add final modifier to local variable or parameter except if it is implicit",
            java, false),

    methodMayBeStatic("Add static modifier to methods",
            java, false),

    unqualifiedFieldAccess("Add this to field access",
            java, false),

    unqualifiedMethodAccess("Add this to method access",
            java, false),

    unqualifiedStaticMemberAccess("Add class qualifier to static member access",
            java, false),

    customUnqualifiedStaticMemberAccess("Add class qualifier to static member access outside declaring class",
            java, false),

    missingOverrideAnnotation("Add missing @Override annotations",
            java, false),

    useBlocks("Add blocks to if/while/for statements",
            java, false),

    generateSerialVersionUID("Add a serialVersionUID field for Serializable classes",
            java, false),

    unnecessaryThis("Remove unnecessary this to field and method",
            java, false),

    finalPrivateMethod("Remove final from private method",
            java, false),

    unnecessaryFinalOnLocalVariableOrParameter("Remove unnecessary final to local variable or parameter",
            java, false),

    explicitTypeCanBeDiamond("Remove explicit generic type for diamond",
            java, false),

    suppressAnnotation("Remove unused suppress warning annotation",
            java, false),

    unnecessarySemicolon("Remove unnecessary semicolon",
            java, false),

    singleStatementInBlock("Remove blocks from if/while/for statements",
            java, false),

    accessCanBeTightened("Change visibility of field or method to lower access",
            java, false),

    memberVisibilityCanBePrivate("Class member can have 'private' visibility",
            kotlin, false);

    private final String text;
    private final ActionType type;
    private final boolean defaultValue;

    Action(String text, ActionType type, boolean defaultValue) {
        this.text = text;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getText() {
        return text;
    }

    public ActionType getType() {
        return type;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public static Set<Action> getDefaults() {
        return Arrays.stream(Action.values())
                .filter(Action::isDefaultValue)
                .collect(toSet());
    }

    public static Stream<Action> stream() {
        return Arrays.stream(values());
    }

    public static Stream<Action> stream(ActionType type) {
        return Arrays.stream(values()).filter(action -> action.type.equals(type));
    }

}
