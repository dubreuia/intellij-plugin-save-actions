package com.dubreuia.integration;

public class Class {

    private int field;

    private void method() {
        if (true) {
            System.out.println("sout");
        }
    }

    public void method1() {
        method();
        field = 0;
    }

}