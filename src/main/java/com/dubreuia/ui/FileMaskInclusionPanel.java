package com.dubreuia.ui;

import java.util.Set;

class FileMaskInclusionPanel extends FileMaskPanel {

    private static final String TEXT_TITLE = "File path inclusions (if empty all included)";

    private static final String TEXT_ADD_OR_EDIT_MESSAGE = "" +
            "<html><body>" +
            "<p>When this list is empty, all files are included. When you add inclusion expressions, only the files " +
            "that match will be impacted by the save actions.</p>" +
            "<p>(use case sensitive Java regular expression that matches the end of the full file path)</p>" +
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

    private static final String TEXT_EMPTY = "Everything included";

    FileMaskInclusionPanel(Set<String> inclusions) {
        super(inclusions, TEXT_EMPTY, TEXT_TITLE, TEXT_ADD_OR_EDIT_MESSAGE, TEXT_ADD_TITLE, TEXT_ADD_OR_EDIT_MESSAGE,
                TEXT_EDIT_TITLE);
    }

}
