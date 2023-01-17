package com.jeff_media.maven_spigot_plugin_gui.gui;

import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.data.WrappedComponent;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;


import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.Border;

public class Dialog {

    private static final double W_MIN = 0;
    private static final double W_MAX = 1;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    private static final int MARGIN = 2;

    private final Map<RequiredProperty, WrappedComponent> fields = new LinkedHashMap<>();


    private final GridBagConstraints constraints = new GridBagConstraints();
    private final Container generalPropertiesPane = new JPanel();

    private final Container dependenciesPane = new JPanel();

    //private final Container dependenciesPane;

    public void fillPane(Container pane, List<RequiredProperty> properties) {
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);


        for(RequiredProperty property : properties) {
            WrappedComponent component = property.add(pane, constraints);
            fields.put(property, component);
        }
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


    public Dialog(List<RequiredProperty> properties) {
        JFrame window = new JFrame("spigot-plugin-archetype");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(300,200));
        JPanel innerWindow = new JPanel();
        innerWindow.setLayout(new GridBagLayout());
        Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        innerWindow.setBorder(padding);
        window.setContentPane(innerWindow);


        JTabbedPane tabbedPane = new JTabbedPane();

        // General Properties
        tabbedPane.addTab("General Properties", generalPropertiesPane);
        generalPropertiesPane.setLayout(new GridBagLayout());
        fillPane(generalPropertiesPane, properties.stream().filter(RequiredProperty::isGeneralProperty).collect(Collectors.toList()));
        generalPropertiesPane.setMaximumSize(generalPropertiesPane.getPreferredSize());

        // Dependencies
        JTable table = new DependencyTable(properties);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnAdjuster tca = new TableColumnAdjuster(table);
        tca.adjustColumns();
        JScrollPane tableScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        tabbedPane.addTab("Dependencies", tableScrollPane);
        //tabbedPane.addTab("Dependencies", dependenciesPane);
        //dependenciesPane.setLayout(new GridBagLayout());
        //dependenciesPane.setBackground(Color.WHITE);
        //fillPane(dependenciesPane, properties.stream().filter(RequiredProperty::isDependency).collect(Collectors.toList()));

        innerWindow.add(tabbedPane, getConstraints(0, 0, 1, 1));

        innerWindow.add(new JButton("Create"), getConstraints(0, 1, 1, 0));

        window.setLocationRelativeTo(null);

        window.pack();
        window.setVisible(true);

    }


}
