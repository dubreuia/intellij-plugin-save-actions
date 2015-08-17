package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.intellij.ui.IdeBorderFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static com.dubreuia.model.Action.explicitTypeCanBeDiamond;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.finalPrivateMethod;
import static com.dubreuia.model.Action.localCanBeFinal;
import static com.dubreuia.model.Action.suppressAnnotation;
import static com.dubreuia.model.Action.unnecessarySemicolon;
import static com.dubreuia.model.Action.unqualifiedFieldAccess;

public class InspectionPanel extends JPanel {

    private static final String TEXT_TITLE_INSPECTIONS = "Inspections quick fix to perform on save";

    public InspectionPanel(final Map<Action, JCheckBox> checkboxes) {
        setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_INSPECTIONS));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(checkboxes.get(localCanBeFinal));
        add(checkboxes.get(fieldCanBeFinal));
        add(checkboxes.get(explicitTypeCanBeDiamond));
        add(checkboxes.get(unqualifiedFieldAccess));
        add(checkboxes.get(suppressAnnotation));
        add(checkboxes.get(finalPrivateMethod));
        add(checkboxes.get(unnecessarySemicolon));
        add(Box.createHorizontalGlue());
        setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
    }

}