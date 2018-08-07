package com.dubreuia.integration;

// https://github.com/dubreuia/intellij-plugin-save-actions/issues/155

class Hello {
    static final String STR = "Hello";

    void sayIt() {
        println(Hello.STR); // should qualify
    }

    class Other {
        String method(){
            String s = Hello.STR; // should qualify
            return s.replace("l", "y");
        }
    }
}

class World extends Hello {
    void sayIt() {
        println(Hello.STR + " World!"); // should qualify
    }
}