package com.jeff_media.maven_spigot_plugin_gui.utils;

import com.jeff_media.maven_spigot_plugin_gui.gui.MainMenu;

import java.awt.*;

public class Constraints {
    private static final int MARGIN = 2;
    public static GridBagConstraints get(int x, int y, double weightx, double weighty) {
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
