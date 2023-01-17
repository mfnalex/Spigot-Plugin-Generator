package com.jeff_media.maven_spigot_plugin_gui;

import com.jeff_media.maven_spigot_plugin_gui.gui.Dialog;
import com.jeff_media.maven_spigot_plugin_gui.gui.ProgressDialog;
import com.jeff_media.maven_spigot_plugin_gui.utils.FileDownloader;
import lombok.Getter;
import net.lingala.zip4j.ZipFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class SpigotPluginGenerator {

    private static final String BINARY_LINK = "https://dlcdn.apache.org/maven/maven-3/%1$s/binaries/apache-maven-%1$s-bin.zip";
    private static final File DATA_FOLDER = new File(System.getProperty("user.home"), ".maven-spigot-plugin-gui");
    private static final File MAVEN_ZIP_FILE = new File(DATA_FOLDER, "mvn.zip");
    private static final File MAVEN_FOLDER = new File(DATA_FOLDER, "mvn");
    private static final String MAVEN_VERSION = "3.8.7";

    @Getter
    private static final Logger logger = LogManager.getLogger(SpigotPluginGenerator.class);

    public SpigotPluginGenerator() throws ExecutionException, InterruptedException {
        logger.info("Starting SpigotPluginGenerator");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            logger.warn("Could not set look and feel", e);
        }

        createDataFolder();

        javax.swing.SwingUtilities.invokeLater(() -> new Dialog());

        ProgressDialog progressDialog = new ProgressDialog("Downloading Maven");
        new FileDownloader(String.format(BINARY_LINK, MAVEN_VERSION), MAVEN_ZIP_FILE).startDownload().thenAccept(file -> {

            try (ZipFile zipFile = new ZipFile(file)) {
                zipFile.extractAll(DATA_FOLDER.getAbsolutePath());
            } catch (IOException e) {
                throw new CompletionException("Could not extract Maven binary zip", e);
            }

            if (!file.delete()) {
                throw new CompletionException(new IOException("Could not delete " + file.getAbsolutePath()));
            }

            if (!new File(DATA_FOLDER, "apache-maven-" + MAVEN_VERSION).renameTo(MAVEN_FOLDER)) {
                throw new CompletionException(new IOException("Could not rename " + new File(DATA_FOLDER, "apache-maven-" + MAVEN_VERSION).getAbsolutePath() + " to " + MAVEN_FOLDER.getAbsolutePath()));
            }

            progressDialog.dispose();
            logger.info("Done");
        }).exceptionally(throwable -> {
            logger.error("Could not download Maven", throwable);
            progressDialog.setText("Error");
            return null;
        });
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
