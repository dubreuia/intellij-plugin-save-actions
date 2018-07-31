package com.dubreuia.integration;

public class Class {

    private String canRemoveThis = "";
    private String cannotRemoveThis = "";

    public void method() {
        canRemoveThis = "";

        String cannotRemoveThis = "";
        this.cannotRemoveThis = "";

        canRemoveThis = "";
    }

    public void otherMethod() {
        method();
    }

}
