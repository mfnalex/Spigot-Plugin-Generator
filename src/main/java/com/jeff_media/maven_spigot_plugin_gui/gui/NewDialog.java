package com.jeff_media.maven_spigot_plugin_gui.gui;


import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.data.WrappedComponent;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public class NewDialog extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(NewDialog.class);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(300, 200);
    private static final int MARGIN = 2;
    private static final int BORDER_MARGIN = 5;
    private static final Border BORDER = BorderFactory.createEmptyBorder(BORDER_MARGIN, BORDER_MARGIN, BORDER_MARGIN, BORDER_MARGIN);
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();


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
        tabbedPane.addTab("Properties", getMainTab());
        tabbedPane.addTab("Dependencies", getDependenciesTab());

        pack();
        setVisible(true);
    }

    private Component getDependenciesTab() {
        JTable table = new DependencyTable(allProperties.stream().filter(RequiredProperty::isDependency).collect(Collectors.toList()));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setBorder(EMPTY_BORDER);
        TableColumnAdjuster tca = new TableColumnAdjuster(table);
        tca.adjustColumns();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(EMPTY_BORDER);
        return new JScrollPane(table);
    }

    private Component getMainTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        fillPane(panel, allProperties.stream().filter(RequiredProperty::isGeneralProperty).collect(Collectors.toList()), newConstraints());
        return wrapIntoScrollPane(panel);
    }

    private Component wrapIntoScrollPane(Component panel) {
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
