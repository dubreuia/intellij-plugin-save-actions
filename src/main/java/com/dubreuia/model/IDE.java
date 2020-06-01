package com.dubreuia.model;

import java.util.Arrays;

public enum IDE {

    Rider("Rider"),

    Other("Other");

    private final String name;

    IDE() {
        this(null);
    }

    IDE(String name) {
        this.name = name;
    }

    public static IDE valueOfOrDefault(String name) {
        return Arrays.stream(values())
                .filter(ide -> ide.name.equals(name))
                .findFirst()
                .orElse(IDE.Other);
    }

}
