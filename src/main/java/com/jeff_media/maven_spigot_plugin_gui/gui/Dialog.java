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


    private final GridBagConstraints constraints = new GridBagConstraints();
    private final Container generalPropertiesPane;
    //private final Container dependenciesPane;

    public void initGeneralProperties(Container pane, List<RequiredProperty> properties) {
        pane.setLayout(new GridBagLayout());
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);


        for(RequiredProperty property : properties) {
            property.add(pane, constraints);
        }

    }


    public Dialog(List<RequiredProperty> properties) {
        JFrame frame = new JFrame("spigot-plugin-archetype");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300,200));
        JPanel wholeWindow = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        wholeWindow.setBorder(padding);
        frame.setContentPane(wholeWindow);

        generalPropertiesPane = frame.getContentPane();
        initGeneralProperties(generalPropertiesPane, properties.stream().filter(RequiredProperty::isGeneralProperty).collect(Collectors.toList()));

        frame.pack();
        frame.setVisible(true);

    }
}
