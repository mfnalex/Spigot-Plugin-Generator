package com.jeff_media.maven_spigot_plugin_gui.gui;

import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;

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

    private final Map<RequiredProperty, Component> fields = new LinkedHashMap<>();


    private final GridBagConstraints generalPropertiesConstraints = new GridBagConstraints();
    private final Container generalPropertiesPane = new JPanel();

    private final GridBagConstraints dependenciesConstraints = new GridBagConstraints();
    private final Container dependenciesPane = new JPanel();

    //private final Container dependenciesPane;

    public void initGeneralProperties(Container pane, List<RequiredProperty> properties) {
        pane.setLayout(new GridBagLayout());
        generalPropertiesConstraints.fill = GridBagConstraints.HORIZONTAL;
        generalPropertiesConstraints.gridx = 0;
        generalPropertiesConstraints.gridy = 0;
        generalPropertiesConstraints.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);


        for(RequiredProperty property : properties) {
            property.add(pane, generalPropertiesConstraints);
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

        JLabel headerGeneral = new JLabel("General Properties");
        innerWindow.add(headerGeneral, getConstraints(0, 0, 0, 0));
        initGeneralProperties(generalPropertiesPane, properties.stream().filter(RequiredProperty::isGeneralProperty).collect(Collectors.toList()));
        innerWindow.add(generalPropertiesPane, getConstraints(0, 1, 1, 1));

        innerWindow.add(new JLabel("Dependencies"), getConstraints(1, 0, 0, 0));
        initDependencies(dependenciesPane, properties.stream().filter(RequiredProperty::isDependency).collect(Collectors.toList()));
        innerWindow.add(dependenciesPane, getConstraints(1,1,1,1));

        window.pack();
        window.setVisible(true);

    }

    private void initDependencies(Container pane, List<RequiredProperty> collect) {
        pane.add(new JLabel("Test123"));
    }
}
