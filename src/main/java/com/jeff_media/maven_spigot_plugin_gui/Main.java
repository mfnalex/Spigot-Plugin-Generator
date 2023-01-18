package com.jeff_media.maven_spigot_plugin_gui;

import com.jeff_media.maven_spigot_plugin_gui.utils.ArchetypeMetadataParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException, ParserConfigurationException, IOException, SAXException {
        new SpigotPluginGenerator();
    }
}
