package com.dubreuia.integration;

public class Class {

    private String theFieldToAccess = "";

    public void method() {
        String noThisToAdd = this.theFieldToAccess + "";
        String thisToAdd = this.theFieldToAccess + "";
    }

}
