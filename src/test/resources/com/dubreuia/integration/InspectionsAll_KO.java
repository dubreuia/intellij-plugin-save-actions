package com.dubreuia.integration;

public class Class {

    public int field;

    public void method() {
        if (true)
            System.out.println("sout");
    }

    public void method1() {
        method();
        field = 0;
        ;
    }

}