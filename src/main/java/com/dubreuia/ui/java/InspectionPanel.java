package com.dubreuia.ui.java;

import static com.dubreuia.core.SaveActionFactory.JAVA_AVAILABLE;
import static com.dubreuia.model.ActionType.java;

import java.awt.*;
import java.util.Map;
import javax.swing.*;

import com.dubreuia.model.Action;
import com.intellij.ui.IdeBorderFactory;

public class InspectionPanel {

    private static final String TEXT_TITLE_INSPECTIONS = "Java inspection and quick fix";

    private final Map<Action, JCheckBox> checkboxes;

    public InspectionPanel(Map<Action, JCheckBox> checkboxes) {
        this.checkboxes = checkboxes;
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel();
        if (!JAVA_AVAILABLE) {
            return panel;
        }
        panel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_INSPECTIONS));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        Action.stream(java).map(checkboxes::get).forEach(panel::add);
        panel.add(Box.createHorizontalGlue());
        panel.setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
        return panel;
    }

}
