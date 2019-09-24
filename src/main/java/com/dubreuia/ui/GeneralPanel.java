package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.dubreuia.model.ActionType;
import com.intellij.ui.IdeBorderFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

class GeneralPanel {

    private static final String TEXT_TITLE_ACTIONS = "General";

    private final Map<Action, JCheckBox> checkboxes;

    GeneralPanel(Map<Action, JCheckBox> checkboxes) {
        this.checkboxes = checkboxes;
    }

    JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_ACTIONS));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        Action.stream(ActionType.activation)
                .map(checkboxes::get)
                .filter(Objects::nonNull)
                .forEach(panel::add);
        panel.add(Box.createHorizontalGlue());
        panel.setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
        return panel;
    }

}
