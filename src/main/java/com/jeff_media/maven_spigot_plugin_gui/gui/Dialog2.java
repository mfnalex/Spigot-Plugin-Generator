package com.jeff_media.maven_spigot_plugin_gui.gui;

import javax.swing.*;
import java.awt.*;

public class Dialog2 extends JFrame {

    public Dialog2() {
        super("Dialog");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(300, 200));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Dialog2();
    }
}
