package com.dubreuia.core;

public enum ExecutionMode {

    /**
     * When the plugin is called normaly (the IDE calls the plugin component on frame deactivation, or auto save).
     */
    normal,

    /**
     * When the plugin is called in batch mode (the IDE calls the plugin after a file selection popup).
     */
    batch,

    /**
     * When the plugin is called from a user input shortcut.
     */
    shortcut,

    ;

}
