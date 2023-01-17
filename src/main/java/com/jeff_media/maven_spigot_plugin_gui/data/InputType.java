package com.jeff_media.maven_spigot_plugin_gui.data;

public enum InputType {
    TEXT_FIELD, CHECKBOX;

    public static InputType fromString(String type) {
        switch (type) {
            case "text":
                return TEXT_FIELD;
            case "checkbox":
                return CHECKBOX;
            default:
                throw new IllegalArgumentException("Unknown input type " + type);
        }
    }
}