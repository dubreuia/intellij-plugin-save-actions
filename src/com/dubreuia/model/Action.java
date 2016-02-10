package com.dubreuia.model;

public enum Action {

    activate("Activate save actions", true),

    organizeImports("Organize imports", true),

    reformat("Reformat code", true),

    reformatChangedCode("Reformat only changed code (only if VCS configured)", false),

    rearrange("Rearrange code", false),

    localCanBeFinal("Add final to local variable", false),

    fieldCanBeFinal("Add final to field", false),

    explicitTypeCanBeDiamond("Remove explicit generic type for diamond", false),

    unqualifiedFieldAccess("Qualify field access with this", false),

    suppressAnnotation("Remove unused suppress warning annotation", false),

    finalPrivateMethod("Remove final from private method", false),

    unnecessarySemicolon("Remove unnecessary semicolon", false);

    private final String text;

    private final boolean defaultValue;

    Action(String text, boolean defaultValue) {
        this.text = text;
        this.defaultValue = defaultValue;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public String getText() {
        return text;
    }

}