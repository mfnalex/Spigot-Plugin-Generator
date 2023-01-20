package com.jeff_media.maven_spigot_plugin_gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.jeff_media.maven_spigot_plugin_gui.data.Archetype;
import com.jeff_media.maven_spigot_plugin_gui.data.Archetypes;
import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.gui.MainMenu;
import com.jeff_media.maven_spigot_plugin_gui.utils.ArchetypeMetadataParser;
import com.jeff_media.maven_spigot_plugin_gui.utils.FileDownloader;
import com.jeff_media.maven_spigot_plugin_gui.utils.MapCombiner;
import com.jeff_media.maven_spigot_plugin_gui.utils.MavenArchetypeGenerateInvoker;
import com.jeff_media.maven_spigot_plugin_gui.utils.OsUtils;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Slf4j
public class SpigotPluginGenerator {

    private static final String BINARY_LINK = "https://dlcdn.apache.org/maven/maven-3/%1$s/binaries/apache-maven-%1$s-bin.zip";
    private static final File DATA_FOLDER = new File(System.getProperty("user.home"), ".maven-spigot-plugin-gui");
    private static final File MAVEN_ZIP_FILE = new File(DATA_FOLDER, "mvn.zip");
    private static final File MAVEN_FOLDER = new File(DATA_FOLDER, "mvn");
    private static final File MAVEN_EXECUTABLE_FOLDER = new File(MAVEN_FOLDER, "bin");
    public static File getMavenExecutable() {
        if(OsUtils.isWindows()) {
            return new File(MAVEN_EXECUTABLE_FOLDER, "mvn.cmd");
        } else {
            return new File(MAVEN_EXECUTABLE_FOLDER, "mvn");
        }
    }
    private static final String MAVEN_VERSION = "3.8.7";

    private static final String ARCHETYPE_LINK = "https://github.com/JEFF-Media-GbR/spigot-plugin-archetype/archive/refs/heads/master.zip";
    private static final File ARCHETYPE_FOLDER = new File(DATA_FOLDER, "archetype");
    private static final File ARCHETYPE_METADATA = new File(ARCHETYPE_FOLDER + "/src/main/resources/META-INF/maven/archetype-metadata.xml");

    private MainMenu mainMenu;

    public SpigotPluginGenerator() throws ExecutionException, InterruptedException {
        FlatLightLaf.setup();
        log.info("Starting SpigotPluginGenerator");

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            log.warn("Could not set look and feel", e);
        }

        createDataFolder();

        if (!isMavenInstalled()) {
            downloadMaven();
        } else {
            log.info("Maven is already installed");
        }


        if (!isArchetypeInstalled()) {
            log.info("Downloading archetype...");
            downloadAndExtract(ARCHETYPE_LINK, new File(DATA_FOLDER, "archetype.zip"), "spigot-plugin-archetype-master", ARCHETYPE_FOLDER);
            log.info("Download complete.");
        } else {
            log.info("Archetype is already installed");
        }

        ArchetypeMetadataParser parser;
        try {
            log.info("Parsing archetype metadata...");
            parser = new ArchetypeMetadataParser(ARCHETYPE_METADATA);
            log.info("Parsing complete.");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        List<RequiredProperty> requiredProperties = parser.getRequiredProperties();

        for (RequiredProperty requiredProperty : requiredProperties) {
            log.debug("Found property: " + requiredProperty);
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            mainMenu = new MainMenu(this, requiredProperties);
        });

    }

    public static void removeMaven() {
        log.info("Removing Maven");
        try {
            FileUtils.deleteDirectory(MAVEN_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Maven removed");
    }

    public static void downloadMaven() {
        log.info("Downloading Maven...");
        try {
            downloadAndExtract(String.format(BINARY_LINK, MAVEN_VERSION), MAVEN_ZIP_FILE, "apache-maven-" + MAVEN_VERSION, MAVEN_FOLDER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Download complete.");
    }

    private void createDataFolder() {
        ;
        if (!DATA_FOLDER.exists()) {
            log.debug("Creating data folder at " + DATA_FOLDER.getAbsolutePath());
            if (!DATA_FOLDER.mkdirs()) {
                throw new RuntimeException("Could not create data directory at " + DATA_FOLDER.getAbsolutePath());
            }
        } else {
            log.debug("Data folder already exists");
        }
    }

    private boolean isMavenInstalled() {
        return MAVEN_EXECUTABLE_FOLDER.exists();
    }

    private static void downloadAndExtract(String zipUrl, File whereToSave, String extractedName, File renameTo) throws InterruptedException, ExecutionException {
        new FileDownloader(zipUrl, whereToSave).startDownload().thenAccept(file -> {

            try (ZipFile zipFile = new ZipFile(file)) {
                zipFile.extractAll(DATA_FOLDER.getAbsolutePath());
            } catch (IOException e) {
                throw new CompletionException("Could not extract zip file", e);
            }

            if (!file.delete()) {
                throw new CompletionException(new IOException("Could not delete " + file.getAbsolutePath()));
            }

            if (!new File(DATA_FOLDER, extractedName).renameTo(renameTo)) {
                throw new CompletionException(new IOException("Could not rename " + new File(DATA_FOLDER, extractedName).getAbsolutePath() + " to " + renameTo.getAbsolutePath()));
            }

            log.info("Done");
        }).exceptionally(throwable -> {
            log.error("Could not download or extract file: " + zipUrl, throwable);
            return null;
        }).get();
    }

    private boolean isArchetypeInstalled() {
        return ARCHETYPE_METADATA.exists();
    }

    public MavenArchetypeGenerateInvoker createMavenArchetypeGenerateInvoker(File workingDir) {
        Archetype archetype = Archetypes.SPIGOT_PLUGIN;
        return new MavenArchetypeGenerateInvoker(archetype, MapCombiner.combine(mainMenu.getFields(), mainMenu.getDependencyTable().getDependencies()), MainMenu.getLogTextArea(), getMavenExecutable(), workingDir);
    }

}
