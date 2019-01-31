package com.dubreuia.core;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;

public enum ExecutionMode {

    /**
     * When the plugin is called normaly (the IDE calls the plugin component on frame deactivation or "save all"). The
     * {@link #saveSingle} is also called on every documents.
     *
     * @see FileDocumentManager#saveAllDocuments()
     */
    saveAll,

    /**
     * When the plugin is called only with a single save (some other plugins like ideavim do that).
     *
     * @see FileDocumentManager#saveDocument(Document)
     */
    saveSingle,

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
