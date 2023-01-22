package com.jeff_media.maven_spigot_plugin_gui.utils;

import com.jeff_media.maven_spigot_plugin_gui.data.Archetype;
import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.data.WrappedComponent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MavenArchetypeGenerateInvoker /*implements Runnable */ {
    private final Archetype archetype;
    //private final Map<RequiredProperty, WrappedComponent> properties;
    private final JTextArea logTextArea;
    private final File mavenExecutable;
    private final File workingDir;
    private final File mavenFolder;

    public MavenArchetypeGenerateInvoker(Archetype archetype, /*Map<RequiredProperty, WrappedComponent> properties, */JTextArea logTextArea, File mavenExecutable, File workingDir, File mavenFolder) {
        this.archetype = archetype;
        //this.properties = properties;
        this.logTextArea = logTextArea;
        this.mavenExecutable = mavenExecutable;
        this.workingDir = workingDir;
        this.mavenFolder = mavenFolder;

    }

    public void downloadArchetype(Archetype archetype) {
        runMavenCommand(null, Arrays.asList("dependency:get", "-U", "-DgroupId=" + archetype.getGroupId(), "-DartifactId=" + archetype.getArtifactId(), "-Dversion=" + archetype.getVersion(), "-DremoteRepositories=" + archetype.getRepository()));
    }

    private static String formatForOsSpecificConsole(String command) {
        if(command.contains("\"")) {
            command = command.replace("\"", "\\\"");
        }
        if(command.contains("'")) {
            command = command.replace("'", "\\'");
        }
        if(command.contains(" ")) {
            if (OsUtils.isWindows()) {
                return "\"" + command + "\"";
            } else {
                return command.replace(" ", "\\ ");
            }
        }
        return command;
    }

    public int runMavenCommand(@Nullable List<String> properties, List<String> additionalCommands) {
        String javaHome = System.getProperty("java.home");
        File javaPath = new File(new File(javaHome, "bin"), OsUtils.isWindows() ? "java.exe" : "java");
        String classPathParameter = new File(new File(mavenFolder, "boot"), "plexus-classworlds-2.6.0.jar").getAbsolutePath();
        String mainClass = "org.codehaus.classworlds.Launcher";
        String multiModulePath = formatForOsSpecificConsole("-Dmaven.multiModuleProjectDirectory=" + workingDir.getAbsolutePath());
        String classWorlds = "-Dclassworlds.conf=" + new File(mavenExecutable.getParentFile(), "m2.conf").getAbsolutePath();
        String mavenHome = "-Dmaven.home=" + mavenExecutable.getParentFile().getParentFile().getAbsolutePath();
        String escapedJavaPath = formatForOsSpecificConsole(javaPath.getAbsolutePath());
        List<String> commands = new ArrayList<>(Arrays.asList(escapedJavaPath, multiModulePath, classWorlds, mavenHome, "-classpath", classPathParameter, mainClass));
        if (properties != null) {
            commands.addAll(properties.stream().map(MavenArchetypeGenerateInvoker::formatForOsSpecificConsole).collect(Collectors.toList()));
        }
        commands.addAll(Arrays.asList("-Dfile.encoding=UTF-8", "-DinteractiveMode=false"));
        commands.addAll(additionalCommands);


        int result = 255;

        //System.out.println("Windows command:\n\n" + String.join(" ", commands));
        log.info("Command: " + String.join(" ", commands));
        //System.out.println("Linux command: " + linux);
        ProcessBuilder pb = new ProcessBuilder().directory(workingDir).command(commands);
        for(String param : pb.command()) {
            log.info("Param: " + param);
        }
        //builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        //Process proc = builder.start();

        try {
            logTextArea.append(String.join(" ", pb.command())+ "\n\n");
            Process process = pb.start();
            InputStream is = process.getInputStream();
            InputStream eis = process.getErrorStream();
            Reader reader = new InputStreamReader(is);
            Reader ereader = new InputStreamReader(eis);
            BufferedReader br = new BufferedReader(reader);
            BufferedReader ebr = new BufferedReader(ereader);
            String line;
            while ((line = br.readLine()) != null) {
                log.info(line);
                logTextArea.append(line + "\n");
            }
            while((line = ebr.readLine()) != null) {
                log.error(line);
                logTextArea.append(line + "\n");
            }
//            log.info("Sleeping 5 seconds");
//            Thread.sleep(5000);
//            log.info("Sleep done");
            result = process.waitFor();
            logTextArea.append("\nProcess exited with code " + result + "\n");
            //throw new IllegalArgumentException("Test asd");
        } catch (Throwable ex) {


            StringWriter stackTraceWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(stackTraceWriter));
            logTextArea.append(stackTraceWriter+"\n");


        }

        return result;

    }

    public List<String> getArchetypeProperties(Map<RequiredProperty, WrappedComponent> properties) {
        List<String> parameters = new ArrayList<>(Arrays.asList(String.format("-DarchetypeGroupId=%s", archetype.getGroupId()), String.format("-DarchetypeArtifactId=%s", archetype.getArtifactId()), String.format("-DarchetypeVersion=%s", archetype.getVersion()), String.format("-DarchetypeRepository=%s", archetype.getRepository())));
        for (Map.Entry<RequiredProperty, WrappedComponent> entry : properties.entrySet()) {
            RequiredProperty property = entry.getKey();
            String value = entry.getValue().getValue();
            parameters.add(String.format("-D%s=%s", property.getKey(), value));
        }
        //parameters.add(String.format("-DoutputDirectory=%s", workingDir.getAbsolutePath()));
        //parameters.add("org.apache.maven.plugins:maven-archetype-plugin:RELEASE:generate");
        return parameters;
    }

}
