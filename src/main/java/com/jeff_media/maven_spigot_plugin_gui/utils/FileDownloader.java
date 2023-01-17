package com.jeff_media.maven_spigot_plugin_gui.utils;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class FileDownloader {

    private final String url;
    private final File file;
    protected final Logger logger;

    public FileDownloader(String url, File file, Logger logger) {
        this.url = url;
        this.file = file;
        this.logger = logger;
    }

    public CompletableFuture<File> startDownload() {
        logger.debug("FileDownloader: Starting download of " + url + " to " + file.getAbsolutePath());
        return CompletableFuture.supplyAsync(() -> {
            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream()); FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                return file;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

}
