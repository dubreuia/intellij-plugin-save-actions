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

package com.dubreuia.ui.java;

import com.dubreuia.core.service.SaveActionsServiceManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileTextField;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import static com.dubreuia.ui.Configuration.BOX_LAYOUT_MAX_HEIGHT;
import static com.dubreuia.ui.Configuration.BOX_LAYOUT_MAX_WIDTH;
import static com.intellij.openapi.ui.TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.awt.BorderLayout.WEST;

/**
 * @author markiewb
 */
public class IdeSupportPanel {

    private static final String BUTTON = "Reset";
    private static final String EXTENSION = "epf";
    private static final String LABEL = "Use external Eclipse configuration file (.epf)";
    private static final String TITLE = "Eclipse Support";

    private TextFieldWithBrowseButton path;

    public JPanel getPanel(String configurationPath) {
        JPanel panel = new JPanel();
        if (!SaveActionsServiceManager.getService().isJavaAvailable()) {
            return panel;
        }

        panel.setBorder(IdeBorderFactory.createTitledBorder(TITLE));
        panel.setLayout(new BorderLayout());

        JBLabel label = getLabel();
        path = getPath(configurationPath);
        JButton reset = getResetButton(path);

        panel.add(label, WEST);
        panel.add(path, CENTER);
        panel.add(reset, EAST);

        panel.setMaximumSize(new Dimension(BOX_LAYOUT_MAX_WIDTH, BOX_LAYOUT_MAX_HEIGHT));

        return panel;
    }

    @NotNull
    private JBLabel getLabel() {
        JBLabel label = new JBLabel();
        label.setText(LABEL);
        label.setLabelFor(path);
        return label;
    }

    @NotNull
    private TextFieldWithBrowseButton getPath(String configurationPath) {
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor(EXTENSION);
        FileTextField field = FileChooserFactory.getInstance().createFileTextField(descriptor, null);
        field.getField().setEnabled(false);
        field.getField().setText(configurationPath);
        TextFieldWithBrowseButton resultPath = new TextFieldWithBrowseButton(field.getField());
        resultPath.addBrowseFolderListener(null, null, null, descriptor, TEXT_FIELD_WHOLE_TEXT);
        return resultPath;
    }

    @NotNull
    private JButton getResetButton(TextFieldWithBrowseButton path) {
        JButton reset = new JButton(BUTTON);
        reset.addActionListener(e -> path.setText(""));
        return reset;
    }

    public String getPath() {
        return path == null ? null : path.getText();
    }

    public void setPath(String configurationPath) {
        if (path != null) {
            path.setText(configurationPath);
        }
    }

}
