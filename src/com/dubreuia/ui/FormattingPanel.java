package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.intellij.ui.IdeBorderFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static com.dubreuia.model.Action.organizeImports;
import static com.dubreuia.model.Action.rearrange;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;

public class FormattingPanel extends JPanel {

    private static final String TEXT_TITLE_ACTIONS = "Formatting actions to perform on save";

    public FormattingPanel(final Map<Action, JCheckBox> checkboxes) {
        setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_ACTIONS));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(checkboxes.get(organizeImports));
        add(checkboxes.get(reformat));
        add(checkboxes.get(reformatChangedCode));
        add(checkboxes.get(rearrange));
        add(Box.createHorizontalGlue());
        setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
    }

}