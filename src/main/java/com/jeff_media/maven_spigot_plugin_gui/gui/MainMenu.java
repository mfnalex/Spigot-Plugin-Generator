package com.jeff_media.maven_spigot_plugin_gui.gui;


import com.jeff_media.maven_spigot_plugin_gui.SpigotPluginGenerator;
import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.data.WrappedComponent;
import com.jeff_media.maven_spigot_plugin_gui.utils.Constraints;
import com.jeff_media.maven_spigot_plugin_gui.utils.MapCombiner;
import com.jeff_media.maven_spigot_plugin_gui.utils.MavenArchetypeGenerateInvoker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MainMenu extends JFrame {

    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(300, 200);
    private static final int MARGIN = 2;
    private static final int BORDER_MARGIN = 5;
    private static final Border BORDER = BorderFactory.createEmptyBorder(BORDER_MARGIN, BORDER_MARGIN, BORDER_MARGIN, BORDER_MARGIN);
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
    @Getter

    private final Container mainTab;
    private final Container dependencyTab;
    private final Container generateTab;
    private final Container aboutTab;
    private final JButton downloadMavenButton = new JButton("Update Maven");
    private final JButton downloadArchetypeButton = new JButton("Update Archetype");
    private final JProgressBar progressBar = new JProgressBar();
    private final JButton generateButton = new JButton("Generate");
    private final SpigotPluginGenerator main;
    @Getter
    private final Map<RequiredProperty, WrappedComponent> fields = new LinkedHashMap<>();
    private final JTabbedPane tabbedPane;
    private final List<RequiredProperty> allProperties;
    private final JLabel fileOutputLabel = new JLabel("<choose a directory>\nasdasdasd<br>adsasdasdasdad");
    @Getter
    private DependencyTable dependencyTable;

    public MainMenu(SpigotPluginGenerator spigotPluginGenerator, List<RequiredProperty> allProperties) {
        super("Spigot Plugin Creator");

        this.main = spigotPluginGenerator;
        this.allProperties = allProperties;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(MINIMUM_WINDOW_SIZE);

        // Setup TabbedPane
        this.tabbedPane = new JTabbedPane();
        //this.tabbedPane.setBorder(BORDER);
        JPanel borderLayout = new JPanel(new BorderLayout());
        //this.setContentPane(tabbedPane);
        this.setContentPane(borderLayout);
        borderLayout.add(tabbedPane, BorderLayout.CENTER);

        // First tab: Properties
        mainTab = createMainTab();
        tabbedPane.addTab("Properties", mainTab);
        dependencyTab = createDependencyTab();
        tabbedPane.addTab("Dependencies", dependencyTab);
        generateTab = new GenerateTab(main, this);
        tabbedPane.addTab("Generate", generateTab);
        aboutTab = wrapIntoScrollPane(new AboutTab(main));
        tabbedPane.addTab("About", aboutTab);

        registerAboutButtons();

        generateTab.setPreferredSize(mainTab.getPreferredSize());

        //borderLayout.add(progressBar, BorderLayout.NORTH);
        progressBar.setIndeterminate(true);
        progressBar.setString("Downloading Maven...");
        progressBar.setStringPainted(true);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Container createMainTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        fillPane(panel, allProperties.stream().filter(RequiredProperty::isGeneralProperty).collect(Collectors.toList()), newConstraints());
        return wrapIntoScrollPane(panel);
    }

    private Container createDependencyTab() {
        dependencyTable = new DependencyTable(this, allProperties.stream().filter(RequiredProperty::isDependency).collect(Collectors.toList()));
        dependencyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dependencyTable.setBorder(EMPTY_BORDER);
        TableColumnAdjuster tca = new TableColumnAdjuster(dependencyTable);
        tca.adjustColumns();
        JScrollPane scrollPane = new JScrollPane(dependencyTable);
        scrollPane.setBorder(EMPTY_BORDER);
        return new JScrollPane(dependencyTable);
    }

    private void registerAboutButtons() {
        downloadMavenButton.addActionListener(e -> {
            SpigotPluginGenerator.removeMaven();
            SpigotPluginGenerator.downloadMaven();
        });
    }

    public void fillPane(Container pane, List<RequiredProperty> properties, GridBagConstraints constraints) {
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        for (RequiredProperty property : properties) {
            WrappedComponent component = property.add(pane, constraints);
            fields.put(property, component);
        }
    }

    public GridBagConstraints newConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
        c.gridx = 0;
        c.gridy = 0;
        return c;
    }

    private JScrollPane wrapIntoScrollPane(Component panel) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.add(panel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(outerPanel);
        scrollPane.setBorder(BORDER);
        return scrollPane;
    }


}
