package com.jeff_media.maven_spigot_plugin_gui;

import com.apple.eawt.Application;
import com.google.common.io.Resources;
import com.jeff_media.maven_spigot_plugin_gui.data.Archetype;
import com.jeff_media.maven_spigot_plugin_gui.data.Archetypes;
import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.data.WrappedComponent;
import com.jeff_media.maven_spigot_plugin_gui.gui.MainMenu;
import com.jeff_media.maven_spigot_plugin_gui.gui.StatusWindow;
import com.jeff_media.maven_spigot_plugin_gui.pets.BrutusBrutalos;
import com.jeff_media.maven_spigot_plugin_gui.pets.Pet;
import com.jeff_media.maven_spigot_plugin_gui.utils.ArchetypeMetadataParser;
import com.jeff_media.maven_spigot_plugin_gui.utils.FileDownloader;
import com.jeff_media.maven_spigot_plugin_gui.utils.MapCombiner;
import com.jeff_media.maven_spigot_plugin_gui.utils.MavenArchetypeGenerateInvoker;
import com.jeff_media.maven_spigot_plugin_gui.utils.OsUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Slf4j
public class SpigotPluginGenerator {

    public static final String MAVEN_VERSION = "3.8.7";
    @Getter
    private static final Pet pet = new BrutusBrutalos();
    private static final String BINARY_LINK = "https://archive.apache.org/dist/maven/maven-3/%1$s/binaries/apache-maven-%1$s-bin.zip";
    private static final File DATA_FOLDER = new File(System.getProperty("user.home"), ".maven-spigot-plugin-gui");
    private static final File MAVEN_ZIP_FILE = new File(DATA_FOLDER, "mvn.zip");
    private static final File MAVEN_FOLDER = new File(DATA_FOLDER, "mvn");
    private static final File MAVEN_EXECUTABLE_FOLDER = new File(MAVEN_FOLDER, "bin");
    private static final String ARCHETYPE_LINK = "https://github.com/JEFF-Media-GbR/spigot-plugin-archetype/archive/refs/heads/master.zip";
    private static final File ARCHETYPE_FOLDER = new File(DATA_FOLDER, "archetype");
    private static final File ARCHETYPE_METADATA = new File(ARCHETYPE_FOLDER + "/src/main/resources/META-INF/maven/archetype-metadata.xml");
    @Getter
    private final String version;
    private MainMenu mainMenu;

    public SpigotPluginGenerator() throws ExecutionException, InterruptedException {

        String version = "N/A";
        try {
            version = Resources.toString(Resources.getResource("version"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
        this.version = version;
        log.info("Spigot Plugin Generator v" + version);

        pet.makeSound();


        createDataFolder();

        StatusWindow window = new StatusWindow("Downloading Maven " + MAVEN_VERSION + " ...");

        if (!isMavenInstalled()) {
            downloadMaven();
        } else {
            log.info("Maven is already installed");
        }


        //if (!isArchetypeInstalled()) {
        removeArchetype();
        window.setText("Downloading Metadata for Archetype" + Archetypes.SPIGOT_PLUGIN.getFullIdentifier() + " ...");
        log.info("Downloading archetype...");
        downloadAndExtract(ARCHETYPE_LINK, new File(DATA_FOLDER, "archetype.zip"), "spigot-plugin-archetype-master", ARCHETYPE_FOLDER);
        log.info("Download complete.");
        /*} else {
            log.info("Archetype is already installed");
        }*/

        ArchetypeMetadataParser parser;
        try {
            window.setText("Parsing Archetype Metadata ...");
            log.info("Parsing archetype metadata ...");
            parser = new ArchetypeMetadataParser(ARCHETYPE_METADATA);
            log.info("Parsing complete.");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        window.setText("Downloading Archetype " + Archetypes.SPIGOT_PLUGIN.getFullIdentifier() + " from " + Archetypes.SPIGOT_PLUGIN.getRepository() + " ...");
        createMavenArchetypeGenerateInvoker(MAVEN_EXECUTABLE_FOLDER, window.getLogArea()).downloadArchetype(Archetypes.SPIGOT_PLUGIN);

        List<RequiredProperty> requiredProperties = parser.getRequiredProperties();

        for (RequiredProperty requiredProperty : requiredProperties) {
            log.debug("Found property: " + requiredProperty);
        }

        window.setText("Starting GUI ...");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.dispose();

        javax.swing.SwingUtilities.invokeLater(() -> mainMenu = new MainMenu(this, requiredProperties));

    }

    private void createDataFolder() {
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

    public static void downloadMaven() {
        log.info("Downloading Maven...");
        try {
            downloadAndExtract(String.format(BINARY_LINK, MAVEN_VERSION), MAVEN_ZIP_FILE, "apache-maven-" + MAVEN_VERSION, MAVEN_FOLDER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Download complete.");
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

    public static void removeMaven() {
        log.info("Removing Maven");
        try {
            FileUtils.deleteDirectory(MAVEN_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Maven removed");
    }

    public static void removeArchetype() {
        log.info("Removing Archetype");
        try {
            FileUtils.deleteDirectory(ARCHETYPE_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Archetype removed");
    }

    private boolean isArchetypeInstalled() {
        return ARCHETYPE_METADATA.exists();
    }

    public MavenArchetypeGenerateInvoker createMavenArchetypeGenerateInvoker(File workingDir, JTextArea logArea) {
        Archetype archetype = Archetypes.SPIGOT_PLUGIN;
        return new MavenArchetypeGenerateInvoker(archetype, logArea, getMavenExecutable(), workingDir, MAVEN_FOLDER);
    }

    public static File getMavenExecutable() {
        if (OsUtils.isWindows()) {
            return new File(MAVEN_EXECUTABLE_FOLDER, "mvn.cmd");
        } else {
            return new File(MAVEN_EXECUTABLE_FOLDER, "mvn");
        }
    }

}
