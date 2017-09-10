package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.dubreuia.model.EPFStorage;
import com.dubreuia.model.Storage;
import com.dubreuia.ui.java.InspectionPanel;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.rearrange;
import static com.dubreuia.model.Action.rearrangeChangedCode;
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
    private ImportConfigurationPanel importConfiguration;
    private GeneralPanel generalPanel;

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
            updateSelectedStateOfCheckboxes(Action.getDefaults());
            storage.stopFirstLaunch();
        }
    }

    private void initActionListeners() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            checkbox.getValue().addActionListener(checkboxActionListener);
        }

        // when the EPF configuration is altered, then check and uncheck the options
        importConfiguration.setChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                String path = (String) e.getSource();
                File file = new File(path);
                if ("".equals(file.getPath())) {
                    // no configuration file
                    enableAllCheckboxes(true);
                    updateSelectedStateOfCheckboxes(Action.getDefaults());
                    updateEnabled();
                } else if (!file.exists()) {
                    // configuration file does not exist
                    enableAllCheckboxes(false);
                    updateSelectedStateOfCheckboxes(Collections.<Action>emptySet());
                    updateEnabled();
                } else if (file.canRead()) {
                    //configuration file found: disable checkboxes and check the checkboxes based on the configuration.
                    enableAllCheckboxes(false);
                    Storage storageFromEPF = null;
                    try {
                        storageFromEPF = new EPFStorage().getStorage(file.getAbsolutePath(), storage);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    updateSelectedStateOfCheckboxes(storageFromEPF.getActions());
                }

            }
        });
    }


    @Override
    public boolean isModified() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            if (storage.isEnabled(checkbox.getKey()) != checkbox.getValue().isSelected()) {
                return true;
            }
        }
        if (!storage.getConfigurationPath().equals(importConfiguration.getPath())) {
            return true;
        }
        return !storage.getExclusions().equals(exclusions)
                || !storage.getInclusions().equals(inclusions);
    }

    @Override
    public void apply() throws ConfigurationException {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            storage.setEnabled(checkbox.getKey(), checkbox.getValue().isSelected());
        }
        storage.setExclusions(new HashSet<String>(exclusions));
        storage.setInclusions(new HashSet<String>(inclusions));
        storage.setConfigurationPath(importConfiguration.getPath());
    }

    @Override
    public void reset() {
        updateSelectedStateOfCheckboxes(storage.getActions());
        updateEnabled();
        updateExclusions();
        updateInclusions();
        importConfiguration.setPath(storage.getConfigurationPath());
    }


    private void enableAllCheckboxes(boolean enabled) {

        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            checkbox.getValue().setEnabled(enabled);
        }

    }

    /**
     * Check the checkboxes for the given actions. If an action for a checkbox is not provided, then the checkbox will be unchecked.
     *
     * @param selectedActions
     */
    private void updateSelectedStateOfCheckboxes(Set<Action> selectedActions) {

        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            boolean isSelected = selectedActions.contains(checkbox.getKey());
            checkbox.getValue().setSelected(isSelected);
        }
    }

    @Override
    public void disposeUIResources() {
        checkboxes.clear();
        exclusions.clear();
        inclusions.clear();
        generalPanel = null;
        formattingPanel = null;
        buildPanel = null;
        inspectionPanel = null;
        fileMasksInclusionPanel = null;
        fileMasksExclusionPanel = null;
        importConfiguration = null;
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
        generalPanel = new GeneralPanel(checkboxes);
        formattingPanel = new FormattingPanel(checkboxes);
        buildPanel = new BuildPanel(checkboxes);
        inspectionPanel = new InspectionPanel(checkboxes);
        importConfiguration = new ImportConfigurationPanel();
        fileMasksInclusionPanel = new FileMaskInclusionPanel(inclusions);
        fileMasksExclusionPanel = new FileMaskExclusionPanel(exclusions);
        return initRootPanel(
                generalPanel.getPanel(),
                formattingPanel.getPanel(),
                buildPanel.getPanel(),
                inspectionPanel.getPanel(),
                fileMasksInclusionPanel.getPanel(),
                fileMasksExclusionPanel.getPanel(),
                importConfiguration.getPanel()
        );
    }

    private JPanel initRootPanel(JPanel general, JPanel actions, JPanel build, JPanel inspections, JPanel fileMasksInclusions,
                                 JPanel fileMasksExclusions, JPanel importConfigurationPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(importConfigurationPanel);
        panel.add(general);
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
        boolean rearangeSelected = checkboxes.get(rearrange).isSelected();
        checkboxes.get(reformatChangedCode).setEnabled(activateSelected && reformatSelected);
        checkboxes.get(rearrangeChangedCode).setEnabled(activateSelected && rearangeSelected);
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
