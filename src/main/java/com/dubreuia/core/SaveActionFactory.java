package com.dubreuia.core;

import com.dubreuia.core.component.SaveActionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SaveActionFactory {

    private SaveActionFactory() {
        // static only
    }

    public static boolean JAVA_AVAILABLE = false;

    public static boolean COMPILING_AVAILABLE = initCompilingAvailable();

    public static List<SaveActionManager> getManagers() {
        List<SaveActionManager> managers = new ArrayList<>();
        managers.add(new SaveActionManager());
        if (JAVA_AVAILABLE) {
            managers.add(new com.dubreuia.core.component.java.SaveActionManager());
        }
        return managers;
    }

    public static Stream<SaveActionManager> streamManagers() {
        return getManagers().stream();
    }

    private static boolean initCompilingAvailable() {
        try {
            return Class.forName("com.intellij.openapi.compiler.CompilerManager") != null;
        } catch (Exception e) {
            return false;
        }
    }

}
