package com.jeff_media.maven_spigot_plugin_gui.gui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

public class DirectoryChooser extends JFileChooser {

    private static final File HOME = new File(System.getProperty("user.home"));
    private static final File IDEA_PROJECTS = new File(HOME, "IdeaProjects");

    public DirectoryChooser() {
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File defaultFolder = getDefaultFolder();
        if (defaultFolder != null) {
            setCurrentDirectory(defaultFolder);
        }
    }

    @Nullable
    private static File getDefaultFolder() {
        if (IDEA_PROJECTS.exists()) {
            return IDEA_PROJECTS;
        }
        if (HOME.exists()) {
            return HOME;
        } else {
            return null;
        }
    }
}
