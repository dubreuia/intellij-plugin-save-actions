/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Alexandre DuBreuil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.dubreuia.ui;

import java.util.Set;

class FileMaskExclusionPanel extends FileMaskPanel {

    private static final String TEXT_TITLE = "File Path Exclusions (exclusions override inclusions)";

    private static final String TEXT_ADD_OR_EDIT_MESSAGE = "" +
            "<html><body>" +
            "<p>When you add exclusion expressions, only the files that do not match will be impacted by the save " +
            "actions.</p>" +
            "<p>(use case sensitive Java regular expression that matches the end of the full file path)</p>" +
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

    private static final String TEXT_EMPTY = "Nothing excluded";

    FileMaskExclusionPanel(Set<String> exclusions) {
        super(exclusions, TEXT_EMPTY, TEXT_TITLE, TEXT_ADD_OR_EDIT_MESSAGE, TEXT_ADD_TITLE, TEXT_ADD_OR_EDIT_MESSAGE,
                TEXT_EDIT_TITLE);
    }

}
