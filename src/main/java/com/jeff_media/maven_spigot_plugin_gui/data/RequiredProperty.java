package com.jeff_media.maven_spigot_plugin_gui.data;

import com.jeff_media.maven_spigot_plugin_gui.SpigotPluginGenerator;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;

@RequiredArgsConstructor
@Data
public class RequiredProperty {

    private final String key;
    private final String name;
    private final InputType type;
    private final boolean separator;

    @Nullable
    public static RequiredProperty fromNode(Node node) {
        Node key = node.getAttributes().getNamedItem("key");
        Node name = node.getAttributes().getNamedItem("name");
        Node type = node.getAttributes().getNamedItem("type");
        Node separator = node.getAttributes().getNamedItem("separator");

        String keyValue, nameValue;
        InputType typeValue;
        boolean separatorValue;

        if(key == null) {
            SpigotPluginGenerator.getLogger().error("Found <requiredProperty> without attribute \"key\": " + node.getTextContent());
            return null;
        }

        keyValue = key.getTextContent();

        if(name == null) {
            SpigotPluginGenerator.getLogger().debug("Found <requiredProperty> without attribute \"name\": " + node.getTextContent());
            return null;
        }

        nameValue = name.getTextContent();

        if(type == null) {
            SpigotPluginGenerator.getLogger().debug("Found <requiredProperty> without attribute \"type\": " + node.getTextContent());
            return null;
        }

        typeValue = InputType.fromString(type.getTextContent());
        separatorValue = separator != null && Boolean.parseBoolean(separator.getTextContent());
        return new RequiredProperty(keyValue, nameValue, typeValue, separatorValue);
    }
}
