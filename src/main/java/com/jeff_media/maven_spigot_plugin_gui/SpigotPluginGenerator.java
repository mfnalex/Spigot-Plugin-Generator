package com.jeff_media.maven_spigot_plugin_gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.gui.Dialog;
import com.jeff_media.maven_spigot_plugin_gui.gui.ProgressDialog;
import com.jeff_media.maven_spigot_plugin_gui.utils.ArchetypeMetadataParser;
import com.jeff_media.maven_spigot_plugin_gui.utils.FileDownloader;
import lombok.Getter;
import net.lingala.zip4j.ZipFile;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class SpigotPluginGenerator {

    private static final String BINARY_LINK = "https://dlcdn.apache.org/maven/maven-3/%1$s/binaries/apache-maven-%1$s-bin.zip";
    private static final File DATA_FOLDER = new File(System.getProperty("user.home"), ".maven-spigot-plugin-gui");
    private static final File MAVEN_ZIP_FILE = new File(DATA_FOLDER, "mvn.zip");
    private static final File MAVEN_FOLDER = new File(DATA_FOLDER, "mvn");
    private static final File MAVEN_EXECUTALBE = new File(new File(MAVEN_FOLDER,"bin"), "mvn");
    private static final String MAVEN_VERSION = "3.8.7";

    private static final String ARCHETYPE_LINK = "https://github.com/JEFF-Media-GbR/spigot-plugin-archetype/archive/refs/heads/master.zip";
    private static final File ARCHETYPE_FOLDER = new File(DATA_FOLDER, "archetype");
    private static final File ARCHETYPE_METADATA = new File(ARCHETYPE_FOLDER + "/src/main/resources/META-INF/maven/archetype-metadata.xml");
    private static final Logger logger = new Logger(SpigotPluginGenerator.class);

    public SpigotPluginGenerator() throws ExecutionException, InterruptedException {
        FlatLightLaf.setup();
        logger.info("Starting SpigotPluginGenerator");

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            logger.warn("Could not set look and feel", e);
        }

        createDataFolder();

        ProgressDialog progressDialog = new ProgressDialog("Downloading Maven...");
        if(!isMavenInstalled()) {
            progressDialog.show();
            downloadAndExtract(progressDialog, String.format(BINARY_LINK, MAVEN_VERSION),MAVEN_ZIP_FILE, "apache-maven-" + MAVEN_VERSION, MAVEN_FOLDER);
        }

        if(!isMavenInstalled()){
            logger.debug("Maven is not installed. Aborting.");
            return;
        }

        if(!isArchetypeInstalled()){
            progressDialog.show();
            progressDialog.setText("Downloading archetype...");
            downloadAndExtract(progressDialog, ARCHETYPE_LINK, new File(DATA_FOLDER, "archetype.zip"), "spigot-plugin-archetype-master", ARCHETYPE_FOLDER);
        }

        if(!isArchetypeInstalled()) {
            logger.debug("Archetype is not installed. Aborting.");
            return;
        }

        ArchetypeMetadataParser parser;
        try {
             parser = new ArchetypeMetadataParser(ARCHETYPE_METADATA);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        List<RequiredProperty> requiredProperties = parser.getRequiredProperties();

        for(RequiredProperty requiredProperty : requiredProperties) {
            logger.error("Required property: " + requiredProperty);
        }

        logger.debug("Maven is installed, showing Dialog");
        javax.swing.SwingUtilities.invokeLater(() -> new Dialog(requiredProperties));

    }

    private static void downloadAndExtract(ProgressDialog progressDialog, String zipUrl, File whereToSave, String extractedName, File renameTo) throws InterruptedException, ExecutionException {
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

            logger.info("Done");
        }).exceptionally(throwable -> {
            logger.error("Could not download or extract file: " + zipUrl, throwable);
            progressDialog.setText("Error");
            return null;
        }).get();
    }

    private boolean isArchetypeInstalled() {
        return ARCHETYPE_METADATA.exists();
    }

    private boolean isMavenInstalled() {
        return MAVEN_EXECUTALBE.exists();
    }

    private void createDataFolder() {
        ;
        if (!DATA_FOLDER.exists()) {
            logger.debug("Creating data folder at " + DATA_FOLDER.getAbsolutePath());
            if (!DATA_FOLDER.mkdirs()) {
                throw new RuntimeException("Could not create data directory at " + DATA_FOLDER.getAbsolutePath());
            }
        } else {
            logger.debug("Data folder already exists");
        }
    }

}
