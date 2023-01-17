package com.jeff_media.maven_spigot_plugin_gui.data;

import com.jeff_media.maven_spigot_plugin_gui.Logger;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;

import java.awt.*;

@RequiredArgsConstructor
@Data
public class RequiredProperty {
    
    private static final Logger logger = new Logger(RequiredProperty.class);

    private final String key;
    private final String name;
    private final InputType type;
    private final boolean separator;
    private final String defaultValue;

    @Nullable
    public static RequiredProperty fromNode(Node node) {
        Node key = node.getAttributes().getNamedItem("key");
        Node name = node.getAttributes().getNamedItem("name");
        Node type = node.getAttributes().getNamedItem("type");
        Node separator = node.getAttributes().getNamedItem("separator");
        String defaultValue = null;

        NodeList children = node.getChildNodes();
        for(int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if(child.getNodeName().equals("defaultValue")) {
                defaultValue = child.getTextContent();
            }
        }

        String keyValue, nameValue;
        InputType typeValue;
        boolean separatorValue;

        if(key == null) {
            logger.error("Found <requiredProperty> without attribute \"key\": " + node.getTextContent());
            return null;
        }

        keyValue = key.getTextContent();

        if(name == null) {
            nameValue = keyValue;
        } else {
            nameValue = name.getTextContent();
        }

        if(type == null) {
            logger.debug("Found <requiredProperty> without attribute \"type\": " + node.getTextContent());
            return null;
        }

        typeValue = InputType.fromString(type.getTextContent());
        separatorValue = separator != null && Boolean.parseBoolean(separator.getTextContent());
        return new RequiredProperty(keyValue, nameValue, typeValue, separatorValue, defaultValue);
    }

    public Component add(Container pane, GridBagConstraints constraints) {
        //System.out.println("Adding " + this + " with to gridy=" + constraints.gridy);
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        Component toReturn = null;
        switch (type) {
            case TEXT:
                JLabel label = new JLabel(name);
                JTextField field = new JTextField(defaultValue);
                pane.add(label, constraints);
                constraints.gridx++;
                constraints.weightx = 1;
                pane.add(field, constraints);
                toReturn = field;
                break;
            case CHECKBOX:
                JCheckBox box = new JCheckBox(name, Boolean.parseBoolean(defaultValue));
                constraints.gridwidth = 2;
                constraints.weightx = 1;
                pane.add(box, constraints);
                toReturn = box;
                break;
            default:
                throw new IllegalArgumentException(type + " is not a valid type.");
        }
        constraints.gridy++;
        constraints.gridx = 0;
        if(separator) {
            constraints.gridwidth = 2;
            pane.add(new JSeparator(), constraints);
            constraints.gridy++;
        }
        return toReturn;
    }

    public boolean isGeneralProperty() {
        return type == InputType.CHECKBOX || type == InputType.TEXT;
    }

    public boolean isDependency() {
        return type == InputType.DEPENDENCY;
    }

}
