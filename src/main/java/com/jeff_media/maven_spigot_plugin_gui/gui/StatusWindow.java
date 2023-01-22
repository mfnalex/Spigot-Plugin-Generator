package com.jeff_media.maven_spigot_plugin_gui.gui;

import com.jeff_media.maven_spigot_plugin_gui.SpigotPluginGenerator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import java.awt.*;

@Slf4j
public class StatusWindow extends JFrame {

    JProgressBar bar;
    @Getter JTextArea logArea = new JTextArea();

    public StatusWindow(String text) {
        super("Spigot Plugin Generator");
        setSize(800, 300);
        JPanel panel = new JPanel(new BorderLayout());
        bar = new JProgressBar();
        panel.add(bar, BorderLayout.NORTH);
        setContentPane(panel);
        bar.setIndeterminate(true);
        bar.setStringPainted(true);


        logArea.setEditable(false);
        logArea.setLineWrap(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        logArea.setAutoscrolls(true);
        DefaultCaret caret = (DefaultCaret) logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);



        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(logArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        setText(text);
    }

    public void setText(String text) {
        bar.setString(text);
        logArea.append(text+"\n");
        log.info(text);
    }
}
