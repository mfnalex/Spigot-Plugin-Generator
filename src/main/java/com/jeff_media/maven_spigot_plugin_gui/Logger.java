//package com.jeff_media.maven_spigot_plugin_gui;
//
//import com.jeff_media.maven_spigot_plugin_gui.gui.MainMenu;
//
//public class Logger {
//
//    private final String className;
//    private static final String ERROR = "[ERROR] ";
//    private static final String WARNING = "[WARN] ";
//    private static final String INFO = "[INFO] ";
//    private static final String DEBUG = "[DEBUG] ";
//
//    public Logger(Class<?> clazz) {
//        this.className = "[" + clazz.getName() + "] ";
//    }
//
//    public void info(String message) {
//        System.out.println(className + INFO + message);
//        MainMenu.getLogTextArea().append(INFO + message + "\n");
//    }
//
//    public void info(String message, Throwable t) {
//        info(message);
//        t.printStackTrace();
//    }
//
//    public void error(String message) {
//        System.err.println(className + ERROR + message);
//        MainMenu.getLogTextArea().append(ERROR + message + "\n");
//    }
//
//    public void error(String message, Throwable t) {
//        error(message);
//        t.printStackTrace();
//    }
//
//    public void warn(String message) {
//        System.err.println(className + WARNING + message);
//        MainMenu.getLogTextArea().append(WARNING + message + "\n");
//    }
//
//    public void warn(String message, Throwable t) {
//        warn(message);
//        t.printStackTrace();
//    }
//
//    public void debug(String message) {
//        System.out.println(className + DEBUG + message);
//    }
//
//    public void debug(String message, Throwable t) {
//        debug(message);
//        t.printStackTrace();
//    }
//
//}
