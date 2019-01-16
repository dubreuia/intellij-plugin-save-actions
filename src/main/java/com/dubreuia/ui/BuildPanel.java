package com.dubreuia.ui;

import com.dubreuia.core.component.SaveActionManager;
import com.dubreuia.model.Action;
import com.intellij.ui.IdeBorderFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static com.dubreuia.model.Action.compile;
import static com.dubreuia.model.Action.reload;

class BuildPanel {

    private static final String TEXT_TITLE_ACTIONS = "Build actions";

    private final Map<Action, JCheckBox> checkboxes;

    BuildPanel(Map<Action, JCheckBox> checkboxes) {
        this.checkboxes = checkboxes;
    }

    JPanel getPanel() {
        JPanel panel = new JPanel();
        if (!SaveActionManager.getInstance().isCompilingAvailable()) {
            return panel;
        }
        panel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_ACTIONS));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(checkboxes.get(compile));
        panel.add(checkboxes.get(reload));
        panel.add(Box.createHorizontalGlue());
        panel.setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
        return panel;
    }

}