package com.dubreuia;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Configuration implements Configurable {

    private static final String TEXT_TITLE_OPTIONS = "Formatting options";

    private static final String TEXT_TITLE_ACTIONS = "Actions to perform on save";

    private static final String TEXT_TITLE_EXCLUSIONS = "File path exclusions";

    private static final String TEXT_ACTIVATE = "Activate save actions";

    private static final String TEXT_IMPORTS = "Organize imports";

    private static final String TEXT_REFORMAT = "Reformat code";

    private static final String TEXT_REFORMAT_CHANGED_CODE = "Reformat only changed code (only if VCS configured)";

    private static final String TEXT_REARRANGE = "Rearrange code";

    private static final String TEXT_DISPLAY_NAME = "Save Actions";

    private static final String TEXT_EXCLUSION_ADD_MESSAGE = "" +
            "<html><body>" +
            "Exclude file paths with Java regular expression. Examples: " +
            "<ul>" +
            "<strong>Main\\.java</strong>    (exclude 'Main.java' only in root folder)<br/>" +
            "<strong>src/Foo\\.java</strong> (exclude file 'Foo.java' only in folder 'src')<br/>" +
            "<strong>.*/.*\\.xml</strong>       (exclude all xml files in any folder)<br/>" +
            "</ul>" +
            "</body></html>";

    private static final String TEXT_EXCLUSION_ADD_TITLE = "Exclude";

    private static final String TEXT_EXCLUSION_EMPTY = "No file path exclusions";

    private SortedListModel exclusionModels = new SortedListModel();

    private Set<String> exclusions = new HashSet<String>();

    private Settings settings = ServiceManager.getService(Settings.class);

    private JBList exclusionList;

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
        modified = modified || settings.isChangedCode() != reformatChangedCode.isSelected();
        modified = modified || settings.isRearrange() != rearrange.isSelected();
        modified = modified || !settings.getExclusions().equals(exclusions);
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.setActivate(activate.isSelected());
        settings.setImports(imports.isSelected());
        settings.setReformat(reformat.isSelected());
        settings.setChangedCode(reformatChangedCode.isSelected());
        settings.setRearrange(rearrange.isSelected());
        settings.setExclusions(new HashSet<String>(exclusions));
    }

    @Override
    public void reset() {
        activate.setSelected(settings.isActivate());
        imports.setSelected(settings.isImports());
        reformat.setSelected(settings.isReformat());
        reformatChangedCode.setSelected(settings.isChangedCode());
        rearrange.setSelected(settings.isRearrange());
        updateExclusions();
        updateEnabled();
    }

    @Override
    public void disposeUIResources() {
        activate = null;
        imports = null;
        reformat = null;
        reformatChangedCode = null;
        rearrange = null;
        exclusionList = null;
        exclusions.clear();
        exclusionModels.clear();
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

    @NotNull
    private AnActionButtonRunnable getAddActionButtonRunnable() {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                String exclusion = Messages.showInputDialog(
                        TEXT_EXCLUSION_ADD_MESSAGE, TEXT_EXCLUSION_ADD_TITLE, null, null, getRegexInputValidator());
                if (exclusion != null) {
                    if (exclusions.add(exclusion)) {
                        exclusionModels.addElementSorted(exclusion);
                    }
                }
            }
        };
    }

    @NotNull
    private InputValidator getRegexInputValidator() {
        return new InputValidator() {
            @Override
            public boolean checkInput(String string) {
                try {
                    Pattern.compile(string);
                    return true;
                } catch (PatternSyntaxException e) {
                    return false;
                }
            }

            @Override
            public boolean canClose(String s) {
                return true;
            }
        };
    }

    @NotNull
    private AnActionButtonRunnable getRemoveActionButtonRunnable() {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                for (Object object : exclusionList.getSelectedValues()) {
                    String selectedValue = (String) object;
                    exclusions.remove(selectedValue);
                    exclusionModels.removeElement(selectedValue);
                }
            }
        };
    }

    @NotNull
    private ActionListener getActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEnabled();
            }
        };
    }

    private JPanel initComponent() {
        JPanel actions = initActions();
        JPanel options = initOptions();
        JPanel fileMasks = initFileMasks();
        return initPanel(actions, options, fileMasks);
    }

    private JPanel initActions() {
        JPanel actions = new JPanel();
        actions.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_ACTIONS));
        actions.setLayout(new BoxLayout(actions, BoxLayout.PAGE_AXIS));
        actions.add(imports = new JCheckBox(TEXT_IMPORTS));
        actions.add(reformat = new JCheckBox(TEXT_REFORMAT));
        actions.add(rearrange = new JCheckBox(TEXT_REARRANGE));
        actions.add(Box.createHorizontalGlue());
        actions.setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
        return actions;
    }

    private JPanel initFileMasks() {
        exclusionList = new JBList(exclusionModels);
        exclusionList.setEmptyText(TEXT_EXCLUSION_EMPTY);
        JPanel exclusionPanel = ToolbarDecorator.createDecorator(exclusionList)
                .setAddAction(getAddActionButtonRunnable())
                .setRemoveAction(getRemoveActionButtonRunnable())
                .setMoveDownAction(null)
                .setMoveUpAction(null)
                .createPanel();
        exclusionPanel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_EXCLUSIONS));
        return exclusionPanel;
    }

    private JPanel initOptions() {
        JPanel options = new JPanel();
        options.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_OPTIONS));
        options.setLayout(new BoxLayout(options, BoxLayout.PAGE_AXIS));
        options.add(reformatChangedCode = new JCheckBox(TEXT_REFORMAT_CHANGED_CODE));
        options.add(Box.createHorizontalGlue());
        return options;
    }

    private JPanel initPanel(JPanel actions, JPanel options, JPanel fileMasks) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(activate = new JCheckBox(TEXT_ACTIVATE));
        panel.add(actions);
        panel.add(options);
        panel.add(fileMasks);
        return panel;
    }

    private void updateEnabled() {
        imports.setEnabled(activate.isSelected());
        reformat.setEnabled(activate.isSelected());
        reformatChangedCode.setEnabled(activate.isSelected() && reformat.isSelected());
        rearrange.setEnabled(activate.isSelected() && reformat.isSelected());
    }

    private void updateExclusions() {
        exclusions.clear();
        exclusions.addAll(settings.getExclusions());
        exclusionModels.clear();
        exclusionModels.addAllSorted(exclusions);
    }

    private static class SortedListModel extends DefaultListModel {

        private void addElementSorted(String element) {
            Enumeration<?> modelElements = elements();
            int index = 0;
            while (modelElements.hasMoreElements()) {
                String modelElement = (String) modelElements.nextElement();
                if (0 < modelElement.compareTo(element)) {
                    add(index, element);
                    return;
                }
                index++;
            }
            addElement(element);
        }

        private void addAllSorted(Collection<String> elements) {
            for (String element : elements) {
                addElementSorted(element);
            }
        }

    }

}
