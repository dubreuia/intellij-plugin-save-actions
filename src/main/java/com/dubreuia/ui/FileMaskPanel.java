package com.dubreuia.ui;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

abstract class FileMaskPanel extends JPanel {

    private final SortedListModel patternModels = new SortedListModel();

    private final JBList<String> patternList;

    private final JPanel patternPanel;

    private final String textAddMessage;

    private final String textAddTitle;

    private final String textEditMessage;

    private final String textEditTitle;

    FileMaskPanel(Set<String> patterns, String textEmpty, String textTitle, String textAddMessage,
                  String textAddTitle, String textEditMessage, String textEditTitle) {
        this.textAddMessage = textAddMessage;
        this.textAddTitle = textAddTitle;
        this.textEditMessage = textEditMessage;
        this.textEditTitle = textEditTitle;
        patternList = new JBList<>(patternModels);
        patternList.setEmptyText(textEmpty);
        patternPanel = ToolbarDecorator.createDecorator(patternList)
                .setAddAction(getAddActionButtonRunnable(patterns))
                .setRemoveAction(getRemoveActionButtonRunnable(patterns))
                .setEditAction(getEditActionButtonRunnable(patterns))
                .disableUpDownActions()
                .createPanel();
        patternPanel.setBorder(IdeBorderFactory.createTitledBorder(textTitle));
    }

    private AnActionButtonRunnable getEditActionButtonRunnable(Set<String> patterns) {
        return actionButton -> {
            String oldValue = patternList.getSelectedValue();
            String pattern = Messages.showInputDialog(
                    textEditMessage, textEditTitle, null, oldValue, getRegexInputValidator());
            if (pattern != null && !pattern.equals(oldValue)) {
                patterns.remove(oldValue);
                patternModels.removeElement(oldValue);
                if (patterns.add(pattern)) {
                    patternModels.addElementSorted(pattern);
                }
            }
        };
    }

    JPanel getPanel() {
        return patternPanel;
    }

    void update(Set<String> patterns) {
        patternModels.clear();
        patternModels.addAllSorted(patterns);
    }

    @NotNull
    private AnActionButtonRunnable getRemoveActionButtonRunnable(Set<String> patterns) {
        return actionButton -> {
            for (String selectedValue : patternList.getSelectedValuesList()) {
                patterns.remove(selectedValue);
                patternModels.removeElement(selectedValue);
            }
        };
    }

    @NotNull
    private AnActionButtonRunnable getAddActionButtonRunnable(Set<String> patterns) {
        return actionButton -> {
            String pattern = Messages.showInputDialog(
                    textAddMessage, textAddTitle, null, null, getRegexInputValidator());
            if (pattern != null) {
                if (patterns.add(pattern)) {
                    patternModels.addElementSorted(pattern);
                }
            }
        };
    }

    @NotNull
    private InputValidator getRegexInputValidator() {
        return new InputValidator() {
            @Override
            public boolean checkInput(String string) {
                if (string == null || string.trim().isEmpty()) {
                    // do not allow null or blank entries
                    return false;
                }
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

    private static class SortedListModel extends DefaultListModel<String> {

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
