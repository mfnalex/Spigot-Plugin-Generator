package com.jeff_media.maven_spigot_plugin_gui;

public class Logger {

    private final String prefix;

    public Logger(Class<?> clazz) {
        this.prefix = "[" + clazz.getName() + "] ";
    }

    public void info(String message) {
        System.out.println(prefix + message);
    }

    public void error(String message) {
        System.err.println(prefix + message);
    }

}
