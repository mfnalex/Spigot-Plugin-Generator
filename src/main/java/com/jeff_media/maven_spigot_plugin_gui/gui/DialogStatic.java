package com.jeff_media.maven_spigot_plugin_gui.gui;

import javax.swing.*;
import java.awt.*;

public class DialogStatic {

    private static double W_MIN = 0;
    private static double W_MAX = 1;
    private static int LEFT = 0;
    private static int RIGHT = 1;

    private final JTextField pluginNameField = new JTextField("MyPlugin");
    private final JTextField groupIdField = new JTextField("org.example");
    private final JTextField artifactIdField = new JTextField("my-plugin");
    private final JTextField versionField = new JTextField("1.0-SNAPSHOT");
    private final JTextField packageField = new JTextField("org.example.myplugin");
    private final JTextField mainClassField = new JTextField("MyPlugin");
    private final JTextField descriptionField = new JTextField("My awesome plugin");
    private final JTextField authorField = new JTextField("Joe Mama");
    private final JTextField spigotVersionField = new JTextField("1.19.3");
    private final JTextField javaVersionField = new JTextField("8");
    private final JTextField pathToTestServerField = new JTextField("C:\\mctest\\plugins");

    private final JCheckBox useNMSCheckBox = new JCheckBox("Include NMS / CraftBukkit dependency");
    private final JCheckBox addStaticGetterCheckBox = new JCheckBox("Add static getter for main instance");


    private final Container pane;
    private final GridBagConstraints constraints = new GridBagConstraints();

    public void init() {
        JLabel label;

        pane.setLayout(new GridBagLayout());
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        addComp(row, LEFT, 1, W_MIN,
                new JLabel("Plugin Name"));

        addComp(row, RIGHT, 1, W_MAX,
                pluginNameField);


        // Limiter
        addComp(++row,LEFT, 2, W_MAX,
                new JSeparator());

        // Group ID
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("Group ID"));


        addComp(row, RIGHT, 1, W_MAX,
                groupIdField);

        // Artifact ID
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("Artifact ID"));

        addComp(row, RIGHT, 1, W_MAX,
                artifactIdField);

        // Version
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("Version"));

        addComp(row, RIGHT, 1, W_MAX,
                versionField);

        // Limiter
        addComp(++row,LEFT, 2, W_MAX,
                new JSeparator());

        // Package
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("Package"));

        addComp(row, RIGHT, 1, W_MIN,
                packageField);

        // SpigotPluginGenerator Class
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("SpigotPluginGenerator Class"));

        addComp(row, RIGHT, 1, W_MAX,
                mainClassField);

        // Description
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("Description"));

        addComp(row, RIGHT, 1, W_MAX,
                descriptionField);

        // Author
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("Author"));

        addComp(row, RIGHT, 1, W_MAX,
                authorField);

        // Spigot Version
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("Spigot Version"));

        addComp(row, RIGHT, 1, W_MAX,
                spigotVersionField);

        // Use NMS / CraftBukkit
        addComp(++row, LEFT, 2, W_MIN,
                useNMSCheckBox);

        // Java Version
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("Java Version"));

        addComp(row, RIGHT, 1, W_MAX,
                javaVersionField);

        // Path to test server
        addComp(++row, LEFT, 1, W_MIN,
                new JLabel("Path to test server"));

        addComp(row, RIGHT, 1, W_MAX,
                pathToTestServerField);

        // Add static getter
        addComp(++row, LEFT, 2, W_MIN,
                addStaticGetterCheckBox);


    }

    public void addComp(int row, int col, int colspan, double weightX, Component component) {
        constraints.gridx = col;
        constraints.gridy = row;
        constraints.weightx = weightX;
        constraints.gridwidth = colspan;
        pane.add(component, constraints);
    }


    public DialogStatic() {
        JFrame frame = new JFrame("spigot-plugin-archetype");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setMinimumSize(new Dimension(300,200));

        pane = frame.getContentPane();
        init();

        frame.pack();
        frame.setVisible(true);

    }
}
