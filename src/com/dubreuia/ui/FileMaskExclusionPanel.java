package com.dubreuia.ui;

import java.util.Set;

public class FileMaskExclusionPanel extends AbstractFileMaskPanel {
    private static final String TEXT_TITLE = "File path exclusions";

    private static final String TEXT_ADD_OR_EDIT_MESSAGE = "" +
            "<html><body>" +
            "<p>Use case sensitive Java regular expression that matches the end of the full file path.</p>" +
            "<p>Examples:</p>" +
            "<ul>" +
            "<li><strong>Ignore\\.java</strong>              (exclude file 'Ignore.java' in all folders)</li>" +
            "<li><strong>.*\\.properties</strong>            (exclude all '.properties' in all folders)</li>" +
            "<li><strong>src/Ignore\\.java</strong>          (exclude file 'Ignore.java' in 'src' folders)</li>" +
            "<li><strong>ignore/.*</strong>                  (exclude folder 'ignore' recursively)</li>" +
            "<li><strong>myProject/Ignore.md</strong>        (exclude file 'Ignore.md' in project 'myProject')</li>" +
            "</ul>" +
            "</body></html>";

    private static final String TEXT_ADD_TITLE = "Add file path exclusion regex";
    private static final String TEXT_EDIT_TITLE = "Edit file path exclusion regex";

    private static final String TEXT_EMPTY = "No file path exclusions";

    FileMaskExclusionPanel(Set<String> exclusions) {
        super(exclusions, TEXT_EMPTY, TEXT_TITLE, TEXT_ADD_OR_EDIT_MESSAGE, TEXT_ADD_TITLE, TEXT_ADD_OR_EDIT_MESSAGE, TEXT_EDIT_TITLE);
    }
}
