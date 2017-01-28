package com.dubreuia.ui;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

abstract class AbstractFileMaskPanel extends JPanel {

    private final SortedListModel patternModels = new SortedListModel();

    private final JBList patternList;

    private final JPanel patternPanel;
    private final String TEXT_ADD_MESSAGE;
    private final String TEXT_ADD_TITLE;
    private final String TEXT_EDIT_MESSAGE;
    private final String TEXT_EDIT_TITLE;

    AbstractFileMaskPanel(Set<String> patterns, String TEXT_EMPTY, String TEXT_TITLE, String TEXT_ADD_MESSAGE, String TEXT_ADD_TITLE, String TEXT_EDIT_MESSAGE, String TEXT_EDIT_TITLE) {
        this.TEXT_ADD_MESSAGE = TEXT_ADD_MESSAGE;
        this.TEXT_ADD_TITLE = TEXT_ADD_TITLE;
        this.TEXT_EDIT_MESSAGE = TEXT_EDIT_MESSAGE;
        this.TEXT_EDIT_TITLE = TEXT_EDIT_TITLE;
        this.patternList = new JBList(patternModels);
        this.patternList.setEmptyText(TEXT_EMPTY);
        this.patternPanel = ToolbarDecorator.createDecorator(patternList)
                .setAddAction(getAddActionButtonRunnable(patterns))
                .setRemoveAction(getRemoveActionButtonRunnable(patterns))
                .setEditAction(getEditActionButtonRunnable(patterns))
                .disableUpDownActions()
                .createPanel();
        this.patternPanel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE));
    }

    private AnActionButtonRunnable getEditActionButtonRunnable(final Set<String> patterns) {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                final String oldValue = (String) patternList.getSelectedValue();

                String pattern = Messages.showInputDialog(
                        TEXT_EDIT_MESSAGE, TEXT_EDIT_TITLE, null, oldValue, getRegexInputValidator());
                if (pattern != null && !pattern.equals(oldValue)) {
                    patterns.remove(oldValue);
                    patternModels.removeElement(oldValue);

                    if (patterns.add(pattern)) {
                        patternModels.addElementSorted(pattern);
                    }
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
    private AnActionButtonRunnable getRemoveActionButtonRunnable(final Set<String> patterns) {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                for (Object object : patternList.getSelectedValues()) {
                    String selectedValue = (String) object;
                    patterns.remove(selectedValue);
                    patternModels.removeElement(selectedValue);
                }
            }
        };
    }


    @NotNull
    private AnActionButtonRunnable getAddActionButtonRunnable(final Set<String> patterns) {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                String pattern = Messages.showInputDialog(
                        TEXT_ADD_MESSAGE, TEXT_ADD_TITLE, null, null, getRegexInputValidator());
                if (pattern != null) {
                    if (patterns.add(pattern)) {
                        patternModels.addElementSorted(pattern);
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
                    if (string == null || string.trim().isEmpty()) {
                        //do not allow null or blank entries
                        return false;
                    }
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
