package com.jeff_media.maven_spigot_plugin_gui.utils;

import com.jeff_media.maven_spigot_plugin_gui.SpigotPluginGenerator;
import com.jeff_media.maven_spigot_plugin_gui.data.Archetype;
import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.data.WrappedComponent;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MavenArchetypeGenerateInvoker implements Runnable {
    private final Archetype archetype;
    private final Map<RequiredProperty, WrappedComponent> properties;
    private final JTextPane logTextArea;
    private final File mavenExecutable;
    private final File workingDir;

    private final PipedInputStream stdOutPin = new PipedInputStream();
    private final PipedInputStream stdErrPin = new PipedInputStream();
    private Thread stdOutReader;
    private Thread stdErrReader;

    private StyledDocument doc;
    private Style style;

    private boolean stopThreads;

    public MavenArchetypeGenerateInvoker(Archetype archetype, Map<RequiredProperty, WrappedComponent> properties, JTextPane logTextArea, File mavenExecutable, File workingDir) {
        this.archetype = archetype;
        this.properties = properties;
        this.logTextArea = logTextArea;
        this.mavenExecutable = mavenExecutable;
        this.workingDir = workingDir;

        doc = (StyledDocument) logTextArea.getDocument();
        style = doc.addStyle("ConsoleStyle", null);
    }

    public List<String> getMavenCommand() {
        List<String> parameters = new ArrayList<>(Arrays.asList(
                mavenExecutable.getAbsolutePath(),
                "-DinteractiveMode=false",
                "-Dmaven.home=" + SpigotPluginGenerator.getMavenExecutable().getParentFile().getParentFile().getAbsolutePath(),
                "-Dmaven.multiModuleProjectDirectory=" + workingDir.getAbsolutePath(),
                String.format("-DarchetypeGroupId=%s", archetype.getGroupId()),
                String.format("-DarchetypeArtifactId=%s", archetype.getArtifactId()),
                String.format("-DarchetypeVersion=%s", archetype.getVersion()),
                String.format("-DarchetypeRepository=%s", archetype.getRepository())));
        for (Map.Entry<RequiredProperty, WrappedComponent> entry : properties.entrySet()) {
            RequiredProperty property = entry.getKey();
            String value = entry.getValue().getValue();
            parameters.add(String.format("-D%s=%s", property.getKey(), value));
        }
        //parameters.add(String.format("-DoutputDirectory=%s", workingDir.getAbsolutePath()));
        //parameters.add("org.apache.maven.plugins:maven-archetype-plugin:RELEASE:generate");
        return parameters;
    }

    public void runMaven() {
        List<String> params = getMavenCommand();
        String windows = params.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(" "));
        //String linux = String.join(" ", params);
        windows = windows + " org.apache.maven.plugins:maven-archetype-plugin:RELEASE:generate";
        System.out.println("Windows command: " + windows);
        //System.out.println("Linux command: " + linux);
        ProcessBuilder builder = new ProcessBuilder()
                .directory(mavenExecutable.getParentFile())
                .command(windows)
                .inheritIO();
        //builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        try {
            Process proc = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setupLogStream() {
//        try {
//            PipedOutputStream stdOutPout = new PipedOutputStream(stdOutPin);
//            PipedOutputStream stdErrPout = new PipedOutputStream(stdErrPin);
//            System.setOut(new PrintStream(stdOutPout, true));
//            System.setErr(new PrintStream(stdErrPout, true));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            PipedOutputStream stdOutPos = new PipedOutputStream(this.stdOutPin);
            System.setOut(new PrintStream(stdOutPos, true));
        } catch (IOException | SecurityException io) {
            logTextArea.setText("Couldn't redirect STDOUT to this console\n" + io.getMessage());
        }

        try {
            PipedOutputStream stdErrPos = new PipedOutputStream(this.stdErrPin);
            System.setErr(new PrintStream(stdErrPos, true));
        } catch (IOException | SecurityException io) {
            logTextArea.setText("Couldn't redirect STDERR to this console\n" + io.getMessage());
        }
        
        

        stopThreads = false; // Will be set to true at closing time. This will stop the threads

        // Starting two threads to read the PipedInputStreams
        stdOutReader = new Thread(this);
        stdOutReader.setDaemon(true);
        stdOutReader.start();

        stdErrReader = new Thread(this);
        stdErrReader.setDaemon(true);
        stdErrReader.start();
    }

    @Override
    public synchronized void run() {
        try {
            while (Thread.currentThread() == stdOutReader) {
                try {
                    this.wait(100);
                } catch (InterruptedException ie) {
                }
                if (stdOutPin.available() != 0) {
                    String input = this.readLine(stdOutPin);
                    StyleConstants.setForeground(style, Color.black);
                    doc.insertString(doc.getLength(), input, style);
                    // Make sure the last line is always visible
                    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
                }
                if (stopThreads) {
                    return;
                }
            }

            while (Thread.currentThread() == stdErrReader) {
                try {
                    this.wait(100);
                } catch (InterruptedException ie) {
                }
                if (stdErrPin.available() != 0) {
                    String input = this.readLine(stdErrPin);
                    StyleConstants.setForeground(style, Color.red);
                    doc.insertString(doc.getLength(), input, style);
                    // Make sure the last line is always visible
                    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
                }
                if (stopThreads) {
                    return;
                }
            }
        } catch (Exception e) {
            logTextArea.setText("\nConsole reports an Internal error.");
            logTextArea.setText("The error is: " + e);
        }
    }

    private synchronized String readLine(PipedInputStream in) throws IOException {
        String input = "";
        do {
            int available = in.available();
            if (available == 0) {
                break;
            }
            byte b[] = new byte[available];
            in.read(b);
            input += new String(b, 0, b.length);
        } while (!input.endsWith("\n") && !input.endsWith("\r\n") && !stopThreads);
        return input;
    }
}
