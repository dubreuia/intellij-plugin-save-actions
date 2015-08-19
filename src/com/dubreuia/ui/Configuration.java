package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;

public class Configuration implements Configurable {

    private static final String TEXT_DISPLAY_NAME = "Save Actions";

    private final Storage storage = ServiceManager.getService(Storage.class);

    private final Set<String> exclusions = new HashSet<String>();

    private final Map<Action, JCheckBox> checkboxes = new HashMap<Action, JCheckBox>();

    private final ActionListener checkboxActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateEnabled();
        }
    };

    private FormattingPanel formattingPanel;

    private InspectionPanel inspectionPanel;

    private FileMaskPanel fileMasksPanel;

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = initComponent();
        initFirstLaunch();
        initActionListeners();
        return panel;
    }

    private void initFirstLaunch() {
        if (storage.isFirstLaunch()) {
            for (Action action : Action.values()) {
                if (action.isDefaultValue()) {
                    storage.setEnabled(action, true);
                }
            }
        }
    }

    private void initActionListeners() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            checkbox.getValue().addActionListener(checkboxActionListener);
        }
    }

    @Override
    public boolean isModified() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            if (storage.isEnabled(checkbox.getKey()) != checkbox.getValue().isSelected()) {
                return true;
            }
        }
        return !storage.getExclusions().equals(exclusions);
    }


    @Override
    public void apply() throws ConfigurationException {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            storage.setEnabled(checkbox.getKey(), checkbox.getValue().isSelected());
        }
        storage.setExclusions(new HashSet<String>(exclusions));
    }

    @Override
    public void reset() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            checkbox.getValue().setSelected(storage.isEnabled(checkbox.getKey()));
        }
        updateEnabled();
        updateExclusions();
    }

    @Override
    public void disposeUIResources() {
        checkboxes.clear();
        exclusions.clear();
        formattingPanel = null;
        inspectionPanel = null;
        fileMasksPanel = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return TEXT_DISPLAY_NAME;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    private JPanel initComponent() {
        for (Action action : Action.values()) {
            checkboxes.put(action, new JCheckBox(action.getText()));
        }
        formattingPanel = new FormattingPanel(checkboxes);
        inspectionPanel = new InspectionPanel(checkboxes);
        fileMasksPanel = new FileMaskPanel(exclusions);
        return initPanel(formattingPanel.getPanel(), inspectionPanel.getPanel(), fileMasksPanel.getPanel());
    }

    private JPanel initPanel(JPanel actions, JPanel inspections, JPanel fileMasks) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(checkboxes.get(activate));
        panel.add(actions);
        panel.add(inspections);
        panel.add(fileMasks);
        return panel;
    }

    private void updateEnabled() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            if (!activate.equals(checkbox.getKey())) {
                checkbox.getValue().setEnabled(checkboxes.get(activate).isSelected());
            }
        }
        checkboxes.get(reformatChangedCode).setEnabled(
                checkboxes.get(activate).isSelected() && checkboxes.get(reformat).isSelected());
    }

    private void updateExclusions() {
        exclusions.clear();
        exclusions.addAll(storage.getExclusions());
        fileMasksPanel.update(exclusions);
    }

}
