package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.intellij.lang.Language;
import com.intellij.ui.IdeBorderFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static com.dubreuia.model.Action.explicitTypeCanBeDiamond;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.finalPrivateMethod;
import static com.dubreuia.model.Action.localCanBeFinal;
import static com.dubreuia.model.Action.missingOverrideAnnotation;
import static com.dubreuia.model.Action.suppressAnnotation;
import static com.dubreuia.model.Action.unnecessarySemicolon;
import static com.dubreuia.model.Action.unqualifiedFieldAccess;

class InspectionPanel {

    private static final String TEXT_TITLE_INSPECTIONS = "Java inspection and quick fix";

    private final Map<Action, JCheckBox> checkboxes;

    InspectionPanel(Map<Action, JCheckBox> checkboxes) {
        this.checkboxes = checkboxes;
    }

    JPanel getPanel() {
        boolean javaEnabled = Language.findLanguageByID("JAVA") != null;
        JPanel panel = new JPanel();
        if (javaEnabled) {
            panel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_INSPECTIONS));
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            panel.add(checkboxes.get(localCanBeFinal));
            panel.add(checkboxes.get(fieldCanBeFinal));
            panel.add(checkboxes.get(explicitTypeCanBeDiamond));
            panel.add(checkboxes.get(unqualifiedFieldAccess));
            panel.add(checkboxes.get(suppressAnnotation));
            panel.add(checkboxes.get(finalPrivateMethod));
            panel.add(checkboxes.get(unnecessarySemicolon));
            panel.add(checkboxes.get(missingOverrideAnnotation));
            panel.add(Box.createHorizontalGlue());
            panel.setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
        }
        return panel;
    }

}
