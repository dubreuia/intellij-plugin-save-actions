package com.dubreuia.integration;

public class Class {

    public void methodToOverride() {

    }

    private static class Children extends Class {

        @java.lang.Override
        public void methodToOverride() {

        }

        // This is a known limitation of the plugin: override is no added to methods from external jars and jdk
        public String toString() {
            return super.toString();
        }

    }

}
