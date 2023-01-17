package com.jeff_media.maven_spigot_plugin_gui.gui;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog {

    JLabel label = new JLabel();
    JProgressBar bar = new JProgressBar();
    JFrame frame = new JFrame("spigot-plugin-archetype");

    public ProgressDialog(String text) {

        setText(text);

        Container pane = frame.getContentPane();
        GridBagConstraints c = new GridBagConstraints();

        pane.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setMinimumSize(new Dimension(300,200));

        c.gridx = 0;
        c.gridy = 0;
        pane.add(label, c);

        c.gridy = 1;
        bar.setIndeterminate(true);
        pane.add(bar);

        frame.pack();

    }

    public void show() {
        frame.setVisible(true);
    }

    public void dispose() {
        frame.dispose();
    }

    public void setText(String text) {
        label.setText(text);
    }

}
