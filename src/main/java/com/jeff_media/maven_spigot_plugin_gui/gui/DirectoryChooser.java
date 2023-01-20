package com.jeff_media.maven_spigot_plugin_gui.gui;

import javax.swing.*;
import java.io.File;

public class DirectoryChooser extends JFileChooser {
    public DirectoryChooser() {
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setCurrentDirectory(new File(System.getProperty("user.home")));
    }

    public static void main(String[] args) {
        DirectoryChooser chooser = new DirectoryChooser();
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            System.out.println(chooser.getSelectedFile());
        }
    }
}
