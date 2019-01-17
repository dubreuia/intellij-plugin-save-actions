package com.dubreuia.ui;

import com.dubreuia.core.component.SaveActionManager;
import com.dubreuia.model.Action;
import com.intellij.openapi.actionSystem.ex.QuickList;
import com.intellij.openapi.actionSystem.ex.QuickListsManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.IdeBorderFactory;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.dubreuia.model.Action.compile;
import static com.dubreuia.model.Action.executeAction;
import static com.dubreuia.model.Action.reload;
import static java.text.MessageFormat.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

class BuildPanel {

    private static final String TEXT_TITLE_ACTIONS = "Build actions";
    private static final int QUICK_LIST_MAX_DESCRIPTION_LENGTH = 100;

    private final List<String> quickLists;
    private final List<QuickListWrapper> quickListElements;
    private final ListComboBoxModel<QuickListWrapper> quickListModel;
    private final JPanel panel;

    BuildPanel(Map<Action, JCheckBox> checkboxes, List<String> quickLists) {
        this.quickLists = quickLists;
        quickListElements = new ArrayList<>();
        quickListModel = new ListComboBoxModel<>(quickListElements);
        quickListModel.addListDataListener(getListDataListener(quickLists));
        panel = new JPanel();
        panel.setBorder(IdeBorderFactory.createTitledBorder(TEXT_TITLE_ACTIONS));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        if (SaveActionManager.getInstance().isCompilingAvailable()) {
            panel.add(wrap(checkboxes.get(compile), null));
            panel.add(wrap(checkboxes.get(reload), null));
        }
        @SuppressWarnings("unchecked")
        JComboBox<QuickListWrapper> comboBox = new ComboBox<QuickListWrapper>(quickListModel);
        panel.add(wrap(checkboxes.get(executeAction), comboBox));
        panel.add(Box.createHorizontalGlue());
        panel.setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
    }

    JPanel getPanel() {
        return panel;
    }

    void update() {
        List<QuickListWrapper> quickListWrappers = Arrays
                .stream(QuickListsManager.getInstance().getAllQuickLists())
                .map(QuickListWrapper::new)
                .sorted(comparing(QuickListWrapper::toString))
                .collect(toList());
        quickListElements.clear();
        quickListElements.addAll(quickListWrappers);
        if (!quickLists.isEmpty()) {
            String selectedItem = quickLists.get(0);
            quickListElements.stream()
                    .filter(wrapper -> wrapper.hasId(selectedItem))
                    .findFirst()
                    .ifPresent(quickListModel::setSelectedItem);
        }
    }

    private JComponent wrap(JCheckBox checkBox, @SuppressWarnings("rawtypes") JComboBox comboBox) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.add(checkBox, BorderLayout.WEST);
        if (comboBox != null) {
            checkBox.addChangeListener(e -> {
                comboBox.setEnabled(checkBox.isSelected());
                update();
            });
            comboBox.setEnabled(checkBox.isSelected());
            wrapper.add(comboBox, BorderLayout.SOUTH);
        }
        wrapper.setMaximumSize(new Dimension(3000, 100));
        return wrapper;
    }

    private static class QuickListWrapper {

        private final QuickList quickList;

        private QuickListWrapper(QuickList quickList) {
            this.quickList = quickList;
        }

        private boolean hasId(String id) {
            return getId().equals(id);
        }

        private String getId() {
            return quickList.hashCode() + "";
        }

        @Override
        public String toString() {
            String name = quickList.getName();
            String description = quickList.getDescription();
            if (description == null) {
                return name;
            }
            if (description.length() > QUICK_LIST_MAX_DESCRIPTION_LENGTH) {
                description = description.substring(0, QUICK_LIST_MAX_DESCRIPTION_LENGTH);
                description = description + "...";
            }
            return format("{0} ({1})", name, description);
        }

    }

    private ListDataListener getListDataListener(List<String> quickLists) {
        return new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                @SuppressWarnings("rawtypes")
                ListComboBoxModel source = (ListComboBoxModel) e.getSource();
                QuickListWrapper selectedItem = (QuickListWrapper) source.getSelectedItem();
                if (selectedItem == null) {
                    return;
                }
                quickLists.clear();
                quickLists.add(selectedItem.getId());
            }

        };
    }

}
