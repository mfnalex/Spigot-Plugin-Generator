package com.jeff_media.maven_spigot_plugin_gui.gui;


import com.jeff_media.maven_spigot_plugin_gui.SpigotPluginGenerator;
import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.data.WrappedComponent;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public class NewDialog extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewDialog.class);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(300, 200);
    private static final int MARGIN = 2;
    private static final int BORDER_MARGIN = 5;
    private static final Border BORDER = BorderFactory.createEmptyBorder(BORDER_MARGIN, BORDER_MARGIN, BORDER_MARGIN, BORDER_MARGIN);
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
    private final Container mainTab;
    private final Container dependencyTab;
    private final Container generateTab;
    private final Container aboutTab;
    private final JButton downloadMavenButton = new JButton("Update Maven");
    private final JButton downloadArchetypeButton = new JButton("Update Archetype");

    private JLabel fileOutputLabel = new JLabel("<choose a directory>\nasdasdasd<br>adsasdasdasdad");
    @Getter private static JTextArea logTextArea = new JTextArea();
    private JTable dependencyTable;

    private final Map<RequiredProperty, WrappedComponent> fields = new LinkedHashMap<>();

    private final JTabbedPane tabbedPane;

    private final List<RequiredProperty> allProperties;

    public NewDialog(List<RequiredProperty> allProperties) {
        super("Spigot Plugin Creator");
        this.allProperties = allProperties;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(MINIMUM_WINDOW_SIZE);

        // Setup TabbedPane
        this.tabbedPane = new JTabbedPane();
        //this.tabbedPane.setBorder(BORDER);
        this.setContentPane(tabbedPane);

        // First tab: Properties
        mainTab = createMainTab();
        tabbedPane.addTab("Properties", mainTab);
        dependencyTab = createDependencyTab();
        tabbedPane.addTab("Dependencies", dependencyTab);
        generateTab = createGenerateTab();
        tabbedPane.addTab("Generate", generateTab);
        aboutTab = createAboutTab();
        tabbedPane.addTab("About", aboutTab);

        registerAboutButtons();

        generateTab.setPreferredSize(mainTab.getPreferredSize());

        pack();
        setVisible(true);
    }

    private void registerAboutButtons() {
        downloadMavenButton.addActionListener(e -> {
            SpigotPluginGenerator.removeMaven();
            SpigotPluginGenerator.downloadMaven();
            StatusWindow.disappear();
        });
    }

    private Container createAboutTab() {
        JPanel panel = new JPanel(new GridBagLayout());

        panel.add(downloadMavenButton, getConstraints(0, 0, 0.5, 1));
        panel.add(downloadArchetypeButton, getConstraints(1, 0, 0.5, 1));

        return wrapIntoScrollPane(panel);
    }

    private Container createGenerateTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel upperPanel = new JPanel(new GridBagLayout());
        upperPanel.add(new JButton("Generate"), getConstraints(0, 0, 1, 1));
        panel.add(upperPanel, BorderLayout.NORTH);

        JScrollPane logScrollPane = new JScrollPane();
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(false);
        logTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logScrollPane.setViewportView(logTextArea);
        panel.add(logScrollPane, BorderLayout.CENTER);
        return panel;
    }

    private Container createDependencyTab() {
        dependencyTable = new DependencyTable(allProperties.stream().filter(RequiredProperty::isDependency).collect(Collectors.toList()));
        dependencyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dependencyTable.setBorder(EMPTY_BORDER);
        TableColumnAdjuster tca = new TableColumnAdjuster(dependencyTable);
        tca.adjustColumns();
        JScrollPane scrollPane = new JScrollPane(dependencyTable);
        scrollPane.setBorder(EMPTY_BORDER);
        return new JScrollPane(dependencyTable);
    }

    private Container createMainTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        fillPane(panel, allProperties.stream().filter(RequiredProperty::isGeneralProperty).collect(Collectors.toList()), newConstraints());
        return wrapIntoScrollPane(panel);
    }

    private JScrollPane wrapIntoScrollPane(Component panel) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(panel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(outerPanel);
        scrollPane.setBorder(BORDER);
        return scrollPane;
    }

    public void fillPane(Container pane, List<RequiredProperty> properties, GridBagConstraints constraints) {
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        for(RequiredProperty property : properties) {
            WrappedComponent component = property.add(pane, constraints);
            fields.put(property, component);
        }
    }

    public GridBagConstraints newConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
        c.gridx = 0;
        c.gridy = 0;
        return c;
    }

    private static GridBagConstraints getConstraints(int x, int y, double weightx, double weighty) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
        return constraints;
    }


}
