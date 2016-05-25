package com.dubreuia.ui;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

class FileMaskPanel extends JPanel {

    private static final String TEXT_TITLE_EXCLUSIONS = "File path exclusions";

    private static final String TEXT_ADD_MESSAGE = "" +
            "<html><body>" +
            "<p>Use case sensitive Java regular expression that matches the end of the full file path.</p>" +
            "<p>Examples:</p>" +
            "<ul>" +
            "<li><strong>Ignore\\.java</strong>              (exclude file 'Ignore.java' in all folders)</li>" +
            "<li><strong>.*\\.properties</strong>            (exclude all '.properties' in all folders)</li>" +
            "<li><strong>src/Ignore\\.java</strong>          (exclude file 'Ignore.java' in 'src' folders)</li>" +
            "<li><strong>ignore/.*</strong>                  (exclude folder 'ignore' recursively)</li>" +
            "<li><strong>myProject/Ignore.md</strong>        (exclude file 'Ignore.md' in project 'myProject')</li>" +
            "</ul>" +
            "</body></html>";

    private static final String TEXT_ADD_TITLE = "Add file path exclusion regex";

    private static final String TEXT_EMPTY = "No file path exclusions";

    private final SortedListModel exclusionModels = new SortedListModel();

    private final JBList exclusionList;

    private final JPanel exclusionPanel;

    FileMaskPanel(Set<String> exclusions) {
        this.exclusionList = new JBList(exclusionModels);
        this.exclusionList.setEmptyText(TEXT_EMPTY);
        this.exclusionPanel = ToolbarDecorator.createDecorator(exclusionList)
                .setAddAction(getAddActionButtonRunnable(exclusions))
                .setRemoveAction(getRemoveActionButtonRunnable(exclusions))
                .setMoveDownAction(null)
                .setMoveUpAction(null)
                .createPanel();
        this.exclusionPanel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_EXCLUSIONS));
    }

    JPanel getPanel() {
        return exclusionPanel;
    }

    void update(Set<String> exclusions) {
        exclusionModels.clear();
        exclusionModels.addAllSorted(exclusions);
    }

    @NotNull
    private AnActionButtonRunnable getRemoveActionButtonRunnable(final Set<String> exclusions) {
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
    private AnActionButtonRunnable getAddActionButtonRunnable(final Set<String> exclusions) {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                String exclusion = Messages.showInputDialog(
                        TEXT_ADD_MESSAGE, TEXT_ADD_TITLE, null, null, getRegexInputValidator());
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
