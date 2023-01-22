package com.jeff_media.maven_spigot_plugin_gui.utils;

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.io.OutputStream;

@RequiredArgsConstructor
public class CustomOutputStream extends OutputStream {
    private final JTextArea textArea;

    @Override
    public void write(int b) {
        textArea.append(String.valueOf((char) b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
