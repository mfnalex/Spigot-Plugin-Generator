package com.jeff_media.maven_spigot_plugin_gui;


import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FlatLightLaf.setup();
        log.info("Starting SpigotPluginGenerator");

        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception e) {
            log.warn("Could not set look and feel", e);
        }

        new SpigotPluginGenerator();
    }

}
