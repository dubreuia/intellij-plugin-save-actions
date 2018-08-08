package com.dubreuia.integration;

public class Class {

    public int field;

    public void method() {

    }

    public void method1() {
        method();
        field = 0;
    }

}