package com.jeff_media.maven_spigot_plugin_gui.utils;

public final class OsUtils {
    private static String OS = null;

    public static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

    public static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

}
