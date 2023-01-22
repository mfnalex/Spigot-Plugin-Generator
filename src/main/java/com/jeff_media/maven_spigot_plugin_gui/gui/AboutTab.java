package com.jeff_media.maven_spigot_plugin_gui.gui;

import com.jeff_media.maven_spigot_plugin_gui.SpigotPluginGenerator;
import com.jeff_media.maven_spigot_plugin_gui.data.Archetypes;
import com.jeff_media.maven_spigot_plugin_gui.utils.Constraints;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutTab extends JPanel {

    private final JLabel textPane = new JLabel();
    private static final String GITHUB_URL = "https://github.com/mfnalex/maven-spigot-plugin-gui/";
    String html = "<html><body style='width: 200px'>";;
    String br = "<br>";


    public AboutTab(SpigotPluginGenerator main) {
        super(new GridBagLayout());

        textPane.setText(html + "Spigot Plugin Generator " + main.getVersion());

        JLabel github = new JLabel(html + "<a href=" + GITHUB_URL + ">GitHub</a>");
        github.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(GITHUB_URL));
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        int row = 0;
        add(textPane, Constraints.get(0, row++, 1, 1));
        add(new JLabel("Copyright 2023 JEFF Media GbR / mfnalex"), Constraints.get(0, row++, 1, 1));
        add(getEmptyLabel(), Constraints.get(0, row++, 1, 1));
        add(github, Constraints.get(0, row++, 1, 1));
        add(getEmptyLabel(), Constraints.get(0, row++, 1, 1));
        add(new JLabel("Maven version: " + SpigotPluginGenerator.MAVEN_VERSION), Constraints.get(0, row++, 1, 1));
        add(new JLabel("Archetype version: " + Archetypes.SPIGOT_PLUGIN.getVersion()), Constraints.get(0, row++, 1, 1));
    }

    private JLabel getEmptyLabel() {
        return new JLabel(" ");
    }
}
