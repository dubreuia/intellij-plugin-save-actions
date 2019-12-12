package com.dubreuia.integration;

public class Class {

    private void method() {
        String canBeFinal = "";
        String cannotBeFinal = "";
        cannotBeFinal = "";
        try (Resource r = new Resource()) {

        }
    }

    class Resource implements AutoCloseable {

        @Override
        public void close() {

        }

    }

    public String lombok() {
        val example = new ArrayList<String>();
        example.add("Hello, World!");
        val foo = example.get(0);
        return foo.toLowerCase();
    }

    public void lombok2() {
        val map = new HashMap<Integer, String>();
        map.put(0, "zero");
        map.put(5, "five");
        for (val entry : map.entrySet()) {
            System.out.printf("%d: %s\n", entry.getKey(), entry.getValue());
        }
    }

}
