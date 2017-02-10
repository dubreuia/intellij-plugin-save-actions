package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
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

    private final Storage storage;

    private final Set<String> exclusions = new HashSet<String>();
    private final Set<String> inclusions = new HashSet<String>();

    private final Map<Action, JCheckBox> checkboxes = new HashMap<Action, JCheckBox>();

    private final ActionListener checkboxActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateEnabled();
        }
    };

    private FormattingPanel formattingPanel;

    private BuildPanel buildPanel;

    private InspectionPanel inspectionPanel;

    private FileMaskPanel fileMasksExclusionPanel;
    private FileMaskPanel fileMasksInclusionPanel;

    public Configuration(Project project) {
        this.storage = ServiceManager.getService(project, Storage.class);
    }

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
            storage.stopFirstLaunch();
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
        if (!storage.getExclusions().equals(exclusions)) {
            return true;
        }
        if (!storage.getInclusions().equals(inclusions)) {
            return true;
        }
        return false;
    }


    @Override
    public void apply() throws ConfigurationException {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            storage.setEnabled(checkbox.getKey(), checkbox.getValue().isSelected());
        }
        storage.setExclusions(new HashSet<String>(exclusions));
        storage.setInclusions(new HashSet<String>(inclusions));
    }

    @Override
    public void reset() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            checkbox.getValue().setSelected(storage.isEnabled(checkbox.getKey()));
        }
        updateEnabled();
        updateExclusions();
        updateInclusions();
    }

    @Override
    public void disposeUIResources() {
        checkboxes.clear();
        exclusions.clear();
        inclusions.clear();
        formattingPanel = null;
        buildPanel = null;
        inspectionPanel = null;
        fileMasksInclusionPanel = null;
        fileMasksExclusionPanel = null;
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
        buildPanel = new BuildPanel(checkboxes);
        inspectionPanel = new InspectionPanel(checkboxes);
        fileMasksInclusionPanel = new FileMaskInclusionPanel(inclusions);
        fileMasksExclusionPanel = new FileMaskExclusionPanel(exclusions);
        return initRootPanel(
                formattingPanel.getPanel(),
                buildPanel.getPanel(),
                inspectionPanel.getPanel(),
                fileMasksInclusionPanel.getPanel(),
                fileMasksExclusionPanel.getPanel()
        );
    }

    private JPanel initRootPanel(JPanel actions, JPanel build, JPanel inspections, JPanel fileMasksInclusions,
                                 JPanel fileMasksExclusions) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(checkboxes.get(activate));
        panel.add(actions);
        panel.add(build);
        panel.add(inspections);

        JPanel fileMaskPanel = new JPanel();
        fileMaskPanel.setLayout(new BoxLayout(fileMaskPanel, BoxLayout.LINE_AXIS));
        fileMaskPanel.add(fileMasksInclusions);
        fileMaskPanel.add(fileMasksExclusions);
        panel.add(fileMaskPanel);
        return panel;
    }

    private void updateEnabled() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            if (!activate.equals(checkbox.getKey())) {
                checkbox.getValue().setEnabled(checkboxes.get(activate).isSelected());
            }
        }
        boolean activateSelected = checkboxes.get(activate).isSelected();
        boolean reformatSelected = checkboxes.get(reformat).isSelected();
        checkboxes.get(reformatChangedCode).setEnabled(activateSelected && reformatSelected);
    }

    private void updateExclusions() {
        exclusions.clear();
        exclusions.addAll(storage.getExclusions());
        fileMasksExclusionPanel.update(exclusions);
    }

    private void updateInclusions() {
        inclusions.clear();
        inclusions.addAll(storage.getInclusions());
        fileMasksInclusionPanel.update(inclusions);
    }

}
