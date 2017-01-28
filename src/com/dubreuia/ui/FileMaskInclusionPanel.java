package com.dubreuia.ui;

import java.util.Set;

public class FileMaskInclusionPanel extends AbstractFileMaskPanel {
    private static final String TEXT_TITLE = "File path inclusions";

    private static final String TEXT_ADD_OR_EDIT_MESSAGE = "" +
            "<html><body>" +
            "<p>Use case sensitive Java regular expression that matches the end of the full file path.</p>" +
            "<p>Examples:</p>" +
            "<ul>" +
            "<li><strong>.*\\.java</strong>                  (include all '.java' in all folders)</li>" +
            "<li><strong>Include\\.java</strong>             (include file 'Include.java' in all folders)</li>" +
            "<li><strong>src/Include\\.java</strong>         (include file 'Include.java' in 'src' folders)</li>" +
            "<li><strong>src/.*</strong>                     (include folder 'src' recursively)</li>" +
            "<li><strong>myProject/Include.md</strong>       (include file 'Include.md' in project 'myProject')</li>" +
            "</ul>" +
            "</body></html>";

    private static final String TEXT_ADD_TITLE = "Add file path inclusion regex";
    private static final String TEXT_EDIT_TITLE = "Edit file path inclusion regex";

    private static final String TEXT_EMPTY = "No restrictions";

    FileMaskInclusionPanel(Set<String> inclusions) {
        super(inclusions, TEXT_EMPTY, TEXT_TITLE, TEXT_ADD_OR_EDIT_MESSAGE, TEXT_ADD_TITLE, TEXT_ADD_OR_EDIT_MESSAGE, TEXT_EDIT_TITLE);
    }
}
