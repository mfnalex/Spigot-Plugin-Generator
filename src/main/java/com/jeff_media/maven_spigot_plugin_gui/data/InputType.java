package com.jeff_media.maven_spigot_plugin_gui.data;


import java.util.Locale;

public enum InputType {
    TEXT, CHECKBOX, DEPENDENCY;

    public static InputType fromString(String type) {
        return valueOf(type.toUpperCase(Locale.ROOT));
    }

}