package com.dubreuia.ui.java;

import com.dubreuia.core.component.SaveActionManager;
import com.dubreuia.model.Action;
import com.intellij.ui.IdeBorderFactory;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.Map;

import static com.dubreuia.model.ActionType.java;

public class InspectionPanel {

    private static final String TEXT_TITLE_INSPECTIONS = "Java Inspection and Quick Fix";

    private final Map<Action, JCheckBox> checkboxes;

    public InspectionPanel(Map<Action, JCheckBox> checkboxes) {
        this.checkboxes = checkboxes;
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel();
        if (!SaveActionManager.getInstance().isJavaAvailable()) {
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
