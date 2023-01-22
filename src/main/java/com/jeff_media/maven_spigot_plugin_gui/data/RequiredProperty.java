package com.jeff_media.maven_spigot_plugin_gui.data;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

@RequiredArgsConstructor
@Data
@Slf4j
public class RequiredProperty {

    public static Comparator<RequiredProperty> COMPARATOR_BY_KEY = Comparator.comparing(RequiredProperty::getKey);

    //private static final Logger log = new Logger(RequiredProperty.class);

    @NonNull
    private final String key;
    @Nullable
    private final String name;
    @NonNull
    private final InputType type;
    private final boolean separator;
    @Nullable
    private final String defaultValue;
    @Nullable
    private final String descriptionValue;
    @Nullable
    private final String urlValue;
    @Nullable
    private final Scope scopeValue;

    @Nullable
    public static RequiredProperty fromNode(Node node) {
        Node keyNode = node.getAttributes().getNamedItem("key");
        Node nameNode = node.getAttributes().getNamedItem("name");
        Node typeNode = node.getAttributes().getNamedItem("type");
        Node separatorNode = node.getAttributes().getNamedItem("separator");
        Node descriptionNode = node.getAttributes().getNamedItem("description");
        Node urlNode = node.getAttributes().getNamedItem("url");
        Node scopeNode = node.getAttributes().getNamedItem("scope");

        String defaultValue = null;

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals("defaultValue")) {
                defaultValue = child.getTextContent();
            }
        }

        String keyValue, nameValue;
        InputType typeValue;
        boolean separatorValue;
        Scope scopeValue;

        if (keyNode == null) {
            log.error("Found <requiredProperty> without attribute \"key\": " + node.getTextContent());
            return null;
        }

        keyValue = keyNode.getTextContent();

        if (nameNode == null) {
            nameValue = keyValue;
            log.warn("Found <requiredProperty> without attribute \"name\", using key as name: " + keyValue);
        } else {
            nameValue = nameNode.getTextContent();
        }

        if (typeNode == null) {
            log.warn("Found <requiredProperty> without attribute \"type\", using \"text\" as type: " + keyValue);
            typeValue = InputType.TEXT;
        } else {
            typeValue = InputType.fromString(typeNode.getTextContent());
        }

        if (scopeNode == null) {
            scopeValue = null;
        } else {
            scopeValue = Scope.fromString(scopeNode.getTextContent());
        }

        String descriptionValue = descriptionNode == null ? null : descriptionNode.getTextContent();
        String urlValue = urlNode == null ? null : urlNode.getTextContent();

        separatorValue = separatorNode != null && Boolean.parseBoolean(separatorNode.getTextContent());
        return new RequiredProperty(keyValue, nameValue, typeValue, separatorValue, defaultValue, descriptionValue, urlValue, scopeValue);
    }

    public WrappedComponent add(Container pane, GridBagConstraints constraints) {
        //System.out.println("Adding " + this + " with to gridy=" + constraints.gridy);
        constraints.weightx = 0;
        constraints.gridwidth = 1;
        Component toReturn;
        switch (type) {
            case TEXT: {
                JLabel label = new JLabel(name);
                JTextField field = new JTextField(defaultValue);
                pane.add(label, constraints);
                constraints.gridx++;
                constraints.weightx = 1;
                pane.add(field, constraints);
                toReturn = field;
                break;
            }
            case CHECKBOX: {
                JCheckBox box = new JCheckBox(name, Boolean.parseBoolean(defaultValue));
                constraints.gridwidth = 2;
                constraints.weightx = 1;
                pane.add(box, constraints);
                toReturn = box;
                break;
            }
            case DEPENDENCY: {
                JCheckBox box = new JCheckBox(name, Boolean.parseBoolean(defaultValue));
                pane.add(box, constraints);
                constraints.gridx++;
                constraints.weightx = 1;
                pane.add(new JLabel(descriptionValue), constraints);
                toReturn = box;
                break;
            }
            default:
                throw new IllegalArgumentException(type + " is not a valid type.");
        }
        constraints.gridy++;
        constraints.gridx = 0;
        if (separator) {
            constraints.gridwidth = 2;
            pane.add(new JSeparator(), constraints);
            constraints.gridy++;
        }
        return new WrappedComponent(toReturn);
    }

    public Object[] asTableRow() {
        return new Object[]{Boolean.parseBoolean(defaultValue), name, urlValue == null ? "" : "Link", descriptionValue, scopeValue};
    }

    public boolean isGeneralProperty() {
        return type == InputType.CHECKBOX || type == InputType.TEXT;
    }

    public boolean isDependency() {
        return type == InputType.DEPENDENCY;
    }
}
