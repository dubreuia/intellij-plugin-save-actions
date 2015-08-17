package com.dubreuia.ui;

import com.dubreuia.model.Action;
import com.dubreuia.model.Storage;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;

public class Configuration implements Configurable {

    private static final String TEXT_TITLE_EXCLUSIONS = "File path exclusions";

    private static final String TEXT_DISPLAY_NAME = "Save Actions";

    private static final String TEXT_EXCLUSION_ADD_MESSAGE = "" +
            "<html><body>" +
            "Exclude file paths with Java regular expression. Examples: " +
            "<ul>" +
            "<strong>Main\\.java</strong>    (exclude 'Main.java' only in root folder)<br/>" +
            "<strong>src/Foo\\.java</strong> (exclude file 'Foo.java' only in folder 'src')<br/>" +
            "<strong>.*/.*\\.xml</strong>    (exclude all xml files in any folder)<br/>" +
            "</ul>" +
            "</body></html>";

    private static final String TEXT_EXCLUSION_ADD_TITLE = "Exclude";

    private static final String TEXT_EXCLUSION_EMPTY = "No file path exclusions";

    private final Storage storage = ServiceManager.getService(Storage.class);

    private final SortedListModel exclusionModels = new SortedListModel();

    private final Set<String> exclusions = new HashSet<String>();

    private final Map<Action, JCheckBox> checkboxes = new HashMap<Action, JCheckBox>();

    private final ActionListener checkboxActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateEnabled();
        }
    };

    private JBList exclusionList;

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
        updateExclusions();
        updateEnabled();
    }

    @Override
    public void disposeUIResources() {
        checkboxes.clear();
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

    private JPanel initComponent() {
        for (Action action : Action.values()) {
            checkboxes.put(action, new JCheckBox(action.getText()));
        }
        JPanel formattingActions = new FormattingPanel(checkboxes);
        JPanel inspectionActions = new InspectionPanel(checkboxes);
        JPanel fileMasks = initFileMasks();
        return initPanel(formattingActions, inspectionActions, fileMasks);
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
