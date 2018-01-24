package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.dubreuia.model.EpfStorage;
import com.dubreuia.model.Storage;
import com.dubreuia.ui.java.IdeSupportPanel;
import com.dubreuia.ui.java.InspectionPanel;
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
import static com.dubreuia.model.Action.activateOnShortcut;
import static com.dubreuia.model.Action.noActionIfCompileErrors;
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

    private GeneralPanel generalPanel;

    private FormattingPanel formattingPanel;

    private BuildPanel buildPanel;

    private InspectionPanel inspectionPanel;

    private FileMaskPanel fileMasksExclusionPanel;

    private FileMaskPanel fileMasksInclusionPanel;

    private IdeSupportPanel ideSupport;

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
    }

    @Override
    public boolean isModified() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            if (storage.isEnabled(checkbox.getKey()) != checkbox.getValue().isSelected()) {
                return true;
            }
        }
        if (storage.getConfigurationPath() != null
                && !storage.getConfigurationPath().equals(ideSupport.getPath())) {
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
        storage.setConfigurationPath(ideSupport.getPath());
        Storage efpStorage = EpfStorage.INSTANCE.getStorageOrDefault(ideSupport.getPath(), storage);
        updateSelectedStateOfCheckboxes(efpStorage.getActions());
        updateEnabled();
    }

    @Override
    public void reset() {
        updateSelectedStateOfCheckboxes(storage.getActions());
        updateEnabled();
        updateExclusions();
        updateInclusions();
        ideSupport.setPath(storage.getConfigurationPath());
    }

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
        ideSupport = null;
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
        fileMasksInclusionPanel = new FileMaskInclusionPanel(inclusions);
        fileMasksExclusionPanel = new FileMaskExclusionPanel(exclusions);
        ideSupport = new IdeSupportPanel();
        return initRootPanel(
                generalPanel.getPanel(),
                formattingPanel.getPanel(),
                buildPanel.getPanel(),
                inspectionPanel.getPanel(),
                fileMasksInclusionPanel.getPanel(),
                fileMasksExclusionPanel.getPanel(),
                ideSupport.getPanel(storage.getConfigurationPath())
        );
    }

    private JPanel initRootPanel(JPanel general, JPanel actions, JPanel build, JPanel inspections,
                                 JPanel fileMasksInclusions, JPanel fileMasksExclusions,
                                 JPanel ideSupport) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(general);
        panel.add(actions);
        panel.add(build);
        panel.add(inspections);

        JPanel fileMaskPanel = new JPanel();
        fileMaskPanel.setLayout(new BoxLayout(fileMaskPanel, BoxLayout.LINE_AXIS));
        fileMaskPanel.add(fileMasksInclusions);
        fileMaskPanel.add(fileMasksExclusions);
        panel.add(fileMaskPanel);

        panel.add(ideSupport);

        return panel;
    }

    private void updateEnabled() {
        boolean activateIsSelected = checkboxes.get(activate).isSelected();
        boolean activateShortcutIsSelected = checkboxes.get(activateOnShortcut).isSelected();

        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            Action currentCheckBoxKey = checkbox.getKey();

            if (!activate.equals(currentCheckBoxKey) && !activateOnShortcut.equals(currentCheckBoxKey)) {
                checkbox.getValue().setEnabled(activateIsSelected || activateShortcutIsSelected);
            }
        }
        boolean activateSelected = checkboxes.get(activate).isSelected() || checkboxes.get(activateOnShortcut).isSelected();
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
