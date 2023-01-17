package com.jeff_media.maven_spigot_plugin_gui.utils;

import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArchetypeMetadataParser {

    @Getter List<RequiredProperty> requiredProperties = new ArrayList<>();

    public ArchetypeMetadataParser(File archetypeMetadataFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(archetypeMetadataFile);

        NodeList propertiesNodeList = document.getElementsByTagName("requiredProperty");

        for(int i = 0; i < propertiesNodeList.getLength(); i++) {
            RequiredProperty property = RequiredProperty.fromNode(propertiesNodeList.item(i));
            if(property != null) {
                requiredProperties.add(property);
            }
        }
    }
}
