package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.dubreuia.model.ActionType;
import com.intellij.ui.IdeBorderFactory;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.Map;
import java.util.Objects;

class ConfigurationPanel {

    private static final String TEXT_TITLE_ACTIONS = "Configuration";

    private final Map<Action, JCheckBox> checkboxes;

    ConfigurationPanel(Map<Action, JCheckBox> checkboxes) {
        this.checkboxes = checkboxes;
    }

    JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_ACTIONS));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        Action.stream(ActionType.configuration)
                .map(checkboxes::get)
                .filter(Objects::nonNull)
                .forEach(panel::add);
        panel.add(Box.createHorizontalGlue());
        panel.setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
        return panel;
    }

}
