package com.dubreuia.ui;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ex.Settings;
import org.jdesktop.swingx.JXHyperlink;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionListener;

public class ParentConfiguration implements Configurable {

    static final String GLOBAL_SETTINGS = "Global settings";

    static final String PROJECT_SETTINGS = "Project settings";

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Save Actions";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = new JPanel();
        JButton globalSettingsButton = new JXHyperlink();
        globalSettingsButton.setText(GLOBAL_SETTINGS);
        JButton projectSettingsButton = new JXHyperlink();
        projectSettingsButton.setText(PROJECT_SETTINGS);

        ActionListener browseSettingsAction = e -> {
            DataContext context = DataManager.getInstance().getDataContext(panel);
            Settings settings = Settings.KEY.getData(context);
            if (settings != null) {
                Class<? extends Configurable> configurable = globalSettingsButton.equals(e.getSource())
                        ? GlobalConfiguration.class
                        : ProjectConfiguration.class;
                settings.select(settings.find(configurable));
            }
        };
        globalSettingsButton.addActionListener(browseSettingsAction);
        projectSettingsButton.addActionListener(browseSettingsAction);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(globalSettingsButton);
        panel.add(projectSettingsButton);
        panel.add(Box.createHorizontalGlue());
        panel.setMinimumSize(new Dimension(Short.MAX_VALUE, 0));
        return panel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() {
    }

}
