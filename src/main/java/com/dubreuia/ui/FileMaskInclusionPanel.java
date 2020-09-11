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

class FileMaskInclusionPanel extends FileMaskPanel {

    private static final String TEXT_TITLE = "File Path Inclusions (if empty all included)";

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
