package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
import com.dubreuia.model.java.EpfStorage;
import com.dubreuia.ui.java.IdeSupportPanel;
import com.dubreuia.ui.java.InspectionPanel;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.activateOnBatch;
import static com.dubreuia.model.Action.activateOnShortcut;
import static com.dubreuia.model.Action.customUnqualifiedStaticMemberAccess;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;
import static com.dubreuia.model.Action.unqualifiedStaticMemberAccess;

public class Configuration implements Configurable {

    private static final String TEXT_DISPLAY_NAME = "Save Actions";

    private final Storage storage;

    private final Set<String> exclusions = new HashSet<>();
    private final Set<String> inclusions = new HashSet<>();
    private final List<String> quickLists = new ArrayList<>();
    private final Map<Action, JCheckBox> checkboxes = new HashMap<>();
    private final ActionListener checkboxActionListener = this::updateCheckboxEnabled;

    private GeneralPanel generalPanel;
    private FormattingPanel formattingPanel;
    private BuildPanel buildPanel;
    private InspectionPanel inspectionPanel;
    private FileMaskPanel fileMasksExclusionPanel;
    private FileMaskPanel fileMasksInclusionPanel;
    private IdeSupportPanel ideSupport;

    public Configuration(Project project) {
        storage = ServiceManager.getService(project, Storage.class);
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
                || !storage.getInclusions().equals(inclusions)
                || !storage.getQuickLists().equals(quickLists);
    }

    @Override
    public void apply() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            storage.setEnabled(checkbox.getKey(), checkbox.getValue().isSelected());
        }
        storage.setExclusions(new HashSet<>(exclusions));
        storage.setInclusions(new HashSet<>(inclusions));
        storage.setQuickLists(new ArrayList<>(quickLists));
        storage.setConfigurationPath(ideSupport.getPath());
        Storage efpStorage = EpfStorage.INSTANCE.getStorageOrDefault(ideSupport.getPath(), storage);
        updateSelectedStateOfCheckboxes(efpStorage.getActions());
        updateCheckboxEnabled(null);
    }

    @Override
    public void reset() {
        updateSelectedStateOfCheckboxes(storage.getActions());
        updateCheckboxEnabled(null);
        updateExclusions();
        updateInclusions();
        updateQuickLists();
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
        quickLists.clear();
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
        buildPanel = new BuildPanel(checkboxes, quickLists);
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
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;

        c.gridy = 0;
        panel.add(general, c);
        c.gridy = 1;
        panel.add(actions, c);
        c.gridy = 2;
        panel.add(build, c);
        c.gridy = 3;
        panel.add(inspections, c);

        JPanel fileMaskPanel = new JPanel();
        fileMaskPanel.setLayout(new BoxLayout(fileMaskPanel, BoxLayout.LINE_AXIS));
        fileMaskPanel.add(fileMasksInclusions);
        fileMaskPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        fileMaskPanel.add(fileMasksExclusions);
        c.gridy = 4;
        panel.add(fileMaskPanel, c);

        c.gridy = 5;
        panel.add(ideSupport, c);

        c.gridy = 6;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        panel.add(filler, c);

        return panel;
    }

    private void updateInclusions() {
        inclusions.clear();
        inclusions.addAll(storage.getInclusions());
        fileMasksInclusionPanel.update(inclusions);
    }

    private void updateExclusions() {
        exclusions.clear();
        exclusions.addAll(storage.getExclusions());
        fileMasksExclusionPanel.update(exclusions);
    }

    private void updateQuickLists() {
        quickLists.clear();
        quickLists.addAll(storage.getQuickLists());
        buildPanel.update(quickLists);
    }

    private void updateCheckboxEnabled(ActionEvent event) {
        updateCheckboxEnabledIfActiveSelected();
        updateCheckboxEnabledGroupReformat();
        updateCheckboxEnabledGroupStaticExclusive(event);
    }

    private void updateCheckboxEnabledIfActiveSelected() {
        for (Map.Entry<Action, JCheckBox> checkbox : checkboxes.entrySet()) {
            Action currentCheckBoxKey = checkbox.getKey();
            if (!activate.equals(currentCheckBoxKey)
                    && !activateOnShortcut.equals(currentCheckBoxKey)
                    && !activateOnBatch.equals(currentCheckBoxKey)) {
                checkbox.getValue().setEnabled(isActiveSelected());
            }
        }
    }

    private void updateCheckboxEnabledGroupReformat() {
        boolean reformatSelected = checkboxes.get(reformat).isSelected();
        checkboxes.get(reformatChangedCode).setEnabled(isActiveSelected() && reformatSelected);
    }

    private void updateCheckboxEnabledGroupStaticExclusive(ActionEvent event) {
        if (event == null || !(event.getSource() instanceof JCheckBox)) {
            return;
        }
        JCheckBox thisCheckbox = (JCheckBox) event.getSource();
        if (thisCheckbox.isSelected()) {
            if (thisCheckbox == checkboxes.get(unqualifiedStaticMemberAccess)) {
                checkboxes.get(customUnqualifiedStaticMemberAccess).setSelected(false);
            } else if (thisCheckbox == checkboxes.get(customUnqualifiedStaticMemberAccess)) {
                checkboxes.get(unqualifiedStaticMemberAccess).setSelected(false);
            }
        }
    }

    private boolean isActiveSelected() {
        boolean activateIsSelected = checkboxes.get(activate).isSelected();
        boolean activateShortcutIsSelected = checkboxes.get(activateOnShortcut).isSelected();
        boolean activateBatchIsSelected = checkboxes.get(activateOnBatch).isSelected();
        return activateIsSelected || activateShortcutIsSelected || activateBatchIsSelected;
    }

}
