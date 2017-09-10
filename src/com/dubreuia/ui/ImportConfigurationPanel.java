package com.dubreuia.ui;

import com.intellij.lang.Language;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileTextField;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.Arrays;

public class ImportConfigurationPanel {

    private static final String TEXT_TITLE = "Import configuration";
    private TextFieldWithBrowseButton myPathField;
    private String DEFAULT_TEXT = " to a Workspace-Mechanic configuration file (*.epf) (Java only)";
    private ChangeListener changeListener;

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @NotNull
    private static Color findColorByKey(String... colorKeys) {

        Color c = null;
        for (String key : colorKeys) {
            c = UIManager.getColor(key);
            if (c != null) {
                break;
            }
        }

        assert c != null : "Can't find color for keys " + Arrays.toString(colorKeys);
        return c;

    }

    JPanel getPanel() {
        JPanel panel = new JPanel();

        boolean javaEnabled = Language.findLanguageByID("JAVA") != null;
        if (!javaEnabled) {
            //panel is only available if JAVA is activated
            return panel;
        }


        panel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE));
        panel.setLayout(new BorderLayout());


        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("epf");
        FileTextField field = FileChooserFactory.getInstance().createFileTextField(descriptor, null);


        myPathField = new TextFieldWithBrowseButton(field.getField());
        myPathField.addBrowseFolderListener(
                "",
                "Path to *.epf file",
                null,
                descriptor,
                TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

        myPathField.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent e) {
                boolean isDefault = StringUtil.equals(myPathField.getText(), DEFAULT_TEXT) || "".equals(myPathField.getText());
                boolean isValidFile = true;

                if (!isDefault) {

                    VirtualFile vf = LocalFileSystem.getInstance().findFileByIoFile(new File(myPathField.getText()));
                    isValidFile = descriptor.isFileSelectable(vf);
                    if (!isValidFile) {
                        myPathField.getTextField().setForeground(getFileNotFoundValueColor());
                    } else {
                        myPathField.getTextField().setForeground(getChangedValueColor());
                    }
                } else {
                    myPathField.getTextField().setForeground(getDefaultValueColor());
                }
                if (changeListener != null) {
                    //inform the listener in Configuration that the file has been changed
                    if (isDefault) {
                        changeListener.stateChanged(new ChangeEvent(""));
                    } else {
                        changeListener.stateChanged(new ChangeEvent(myPathField.getText()));
                    }

                }


            }
        });
        myPathField.getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                removeDefaultText();
            }

            @Override
            public void focusLost(FocusEvent e) {
                setDefaultText();
            }
        });
        myPathField.setText(DEFAULT_TEXT);


        JBLabel label = new JBLabel();
        label.setText("File path");
        label.setLabelFor(myPathField);


        panel.add(label, BorderLayout.WEST);
        panel.add(myPathField, BorderLayout.CENTER);


        return panel;
    }

    private void removeDefaultText() {
        if (DEFAULT_TEXT.equals(myPathField.getText())) {
            myPathField.setText("");
        }
    }

    private void setDefaultText() {
        if ("".equals(myPathField.getText())) {
            myPathField.setText(DEFAULT_TEXT);
        }
    }

    private Color getFileNotFoundValueColor() {
        return JBColor.RED;
    }

    private Color getDefaultValueColor() {
        return findColorByKey("TextField.inactiveForeground", "nimbusDisabledText");
    }

    private Color getChangedValueColor() {
        return findColorByKey("TextField.foreground");
    }

    public String getPath() {
        if (myPathField != null) {

            if (DEFAULT_TEXT.equals(myPathField.getText())) {
                return "";
            }
            return myPathField.getText();
        }
        return "";
    }

    public void setPath(String configurationPath) {
        if (myPathField != null) {
            myPathField.setText(configurationPath);
            setDefaultText();
        }
    }
}
