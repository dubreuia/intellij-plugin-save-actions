package com.dubreuia.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static com.dubreuia.model.ActionType.activation;
import static com.dubreuia.model.ActionType.build;
import static com.dubreuia.model.ActionType.global;
import static com.dubreuia.model.ActionType.java;
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

    compile("Compile files (using \"Build > Build Project\")",
            build, false),

    reload("Reload files in running debugger (using \"Run > Reload Changed Classes\")",
            build, false),

    executeAction("Execute an action (using quick lists at \"File > Settings > Appearance & Behavior > Quick Lists\"",
            build, false),

    // Java fixes

    fieldCanBeFinal("Add final modifier to field",
            java, false),

    localCanBeFinal("Add final modifier to local variable or parameter",
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

    accessCanBeTightened("Change visibility of field or method to lower access",
            java, false),

    ;

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
