package com.dubreuia.integration;

import java.util.List;
import java.util.ArrayList;

public class Class {

    @SuppressWarnings("unchecked")
    private void method() {
        List<String> list = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public List<String> issue87(final List stringList) {
        return (List<String>)stringList;
    }
}
