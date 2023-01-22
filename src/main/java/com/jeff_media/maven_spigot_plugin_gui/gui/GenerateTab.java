package com.jeff_media.maven_spigot_plugin_gui.gui;

import com.jeff_media.maven_spigot_plugin_gui.SpigotPluginGenerator;
import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.data.WrappedComponent;
import com.jeff_media.maven_spigot_plugin_gui.utils.Constraints;
import com.jeff_media.maven_spigot_plugin_gui.utils.MapCombiner;
import com.jeff_media.maven_spigot_plugin_gui.utils.MavenArchetypeGenerateInvoker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class GenerateTab extends JPanel {

    @Getter private static final JTextArea logTextArea = new JTextArea();
    private final JButton generateButton = new JButton("Generate");
    private final MainMenu menu;

    public GenerateTab(SpigotPluginGenerator main, MainMenu menu) {
        super(new BorderLayout());
        this.menu = menu;
        JPanel panel = this;
        JPanel upperPanel = new JPanel(new GridBagLayout());
        upperPanel.add(generateButton, Constraints.get(0, 0, 1, 1));
        panel.add(upperPanel, BorderLayout.NORTH);

        generateButton.addActionListener(e -> {

            DirectoryChooser chooser = new DirectoryChooser();
            int result = chooser.showOpenDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) return;
            File outputDirectory = chooser.getSelectedFile();
            log.info("Output directory: " + outputDirectory.getAbsolutePath());

            MavenArchetypeGenerateInvoker invoker = main.createMavenArchetypeGenerateInvoker(outputDirectory, logTextArea);
            List<String> properties = invoker.getArchetypeProperties(new MapCombiner<RequiredProperty, WrappedComponent>().combine(menu.getFields(), menu.getDependencyTable().getDependencies()));
            log.info("Properties: " + String.join(" ",properties));
            new Thread(() -> {
                int returnCode = invoker.runMavenCommand(invoker.getArchetypeProperties(new MapCombiner<RequiredProperty, WrappedComponent>().combine(menu.getFields(), menu.getDependencyTable().getDependencies())), Collections.singletonList("org.apache.maven.plugins:maven-archetype-plugin:RELEASE:generate"));
                if(returnCode == 0) {
                    try {
                        Desktop.getDesktop().browse(new File(outputDirectory, menu.getFields().entrySet().stream().filter(entry -> entry.getKey().getKey().equals("artifactId")).findFirst().get().getValue().getValue()).toURI());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }).start();
        });


        JScrollPane logScrollPane = new JScrollPane();
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(false);
        logTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logScrollPane.setViewportView(logTextArea);

        logTextArea.setAutoscrolls(true);
        DefaultCaret caret = (DefaultCaret) logTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        panel.add(logScrollPane, BorderLayout.CENTER);
    }
}
