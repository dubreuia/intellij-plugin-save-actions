package com.dubreuia;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.IdeBorderFactory;

public class Configuration implements Configurable {

    private static final String TEXT_TITLE_OPTIONS = "Formatting options";
    private static final String TEXT_TITLE_ACTIONS = "Actions to perform on save";

    private static final String TEXT_ACTIVATE = "Activate save actions";
    private static final String TEXT_IMPORTS = "Organize imports";
    private static final String TEXT_REFORMAT = "Reformat code";
    private static final String TEXT_REFORMAT_CHANGED_CODE = "Format and rearrange only changed code";
    private static final String TEXT_REARRANGE = "Rearrange code";

    private static final String TEXT_DISPLAY_NAME = "Save Actions";

    private Settings settings = ServiceManager.getService(Settings.class);

    private JCheckBox activate;
    private JCheckBox imports;
    private JCheckBox reformat;
    private JCheckBox reformatChangedCode;
    private JCheckBox rearrange;

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = initComponent();
        initActionListeners();
        return panel;
    }

    private void initActionListeners() {
        activate.addActionListener(getActionListener());
        imports.addActionListener(getActionListener());
        reformat.addActionListener(getActionListener());
        reformatChangedCode.addActionListener(getActionListener());
        rearrange.addActionListener(getActionListener());
    }

    @Override
    public boolean isModified() {
        boolean modified = settings.isActivate() != activate.isSelected();
        modified = modified || settings.isImports() != imports.isSelected();
        modified = modified || settings.isReformat() != reformat.isSelected();
        modified = modified || settings.isReformatChangedCode() != reformatChangedCode.isSelected();
        modified = modified || settings.isRearrange() != rearrange.isSelected();
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.setActivate(activate.isSelected());
        settings.setImports(imports.isSelected());
        settings.setReformat(reformat.isSelected());
        settings.setReformatChangedCode(reformatChangedCode.isSelected());
        settings.setRearrange(rearrange.isSelected());
    }

    @Override
    public void reset() {
        activate.setSelected(settings.isActivate());
        imports.setSelected(settings.isImports());
        reformat.setSelected(settings.isReformat());
        reformatChangedCode.setSelected(settings.isReformatChangedCode());
        rearrange.setSelected(settings.isRearrange());
        updateDependencies();
    }

    @Override
    public void disposeUIResources() {

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

    private ActionListener getActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDependencies();
            }
        };
    }

    private JPanel initComponent() {
        JPanel actions = initActions();
        JPanel options = initOptions();
        return initPanel(actions, options);
    }

    private JPanel initActions() {
        JPanel actions = new JPanel();
        actions.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_ACTIONS));
        actions.setLayout(new BoxLayout(actions, BoxLayout.PAGE_AXIS));
        actions.add(imports = new JCheckBox(TEXT_IMPORTS));
        actions.add(reformat = new JCheckBox(TEXT_REFORMAT));
        actions.add(rearrange = new JCheckBox(TEXT_REARRANGE));
        actions.add(Box.createHorizontalGlue());
        return actions;
    }

    private JPanel initOptions() {
        JPanel options = new JPanel();
        options.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_OPTIONS));
        options.setLayout(new BoxLayout(options, BoxLayout.PAGE_AXIS));
        options.add(reformatChangedCode = new JCheckBox(TEXT_REFORMAT_CHANGED_CODE));
        options.add(Box.createHorizontalGlue());
        return options;
    }

    private JPanel initPanel(JPanel actions, JPanel options) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(activate = new JCheckBox(TEXT_ACTIVATE));
        panel.add(actions);
        panel.add(options);
        panel.add(Box.createHorizontalGlue());
        return panel;
    }

    private void updateDependencies() {
        updateEnabled();
        updateSelected();
    }

    private void updateEnabled() {
        imports.setEnabled(activate.isSelected());
        reformat.setEnabled(activate.isSelected());
        reformatChangedCode.setEnabled(activate.isSelected() && reformat.isSelected());
        rearrange.setEnabled(activate.isSelected() && reformat.isSelected());
    }

    private void updateSelected() {
        if (!activate.isSelected()) {
            imports.setSelected(false);
            reformat.setSelected(false);
            reformatChangedCode.setSelected(false);
            rearrange.setSelected(false);
        }
        if (!reformat.isSelected()) {
            reformatChangedCode.setSelected(false);
            rearrange.setSelected(false);
        }
    }

}