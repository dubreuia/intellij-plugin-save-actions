package com.dubreuia.core;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SaveActionFactory {

    private SaveActionFactory() {
        // static only
    }

    public static boolean JAVA_ENABLED = false;

    @NotNull
    public static List<SaveActionManager> getSaveActionManagers() {
        List<SaveActionManager> managers = new ArrayList<SaveActionManager>();
        managers.add(new SaveActionManager());
        if (JAVA_ENABLED) {
            managers.add(new com.dubreuia.core.java.SaveActionManager());
        }
        return managers;
    }

}
