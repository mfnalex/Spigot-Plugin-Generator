package com.jeff_media.maven_spigot_plugin_gui.gui;

//public class StatusWindow extends JFrame {
//
//    public StatusWindow(String text) {
//        super("Status");
//        setSize(400, 200);
//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        this.setAlwaysOnTop(true);
//        this.setLocationRelativeTo(null);
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        add(new JLabel(text));
//        JProgressBar bar = new JProgressBar();
//        bar.setIndeterminate(true);
//        add(bar);
//        setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        new StatusWindow("Downloading Maven...");
//    }
//}

import com.jeff_media.maven_spigot_plugin_gui.Logger;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.Random;

public class StatusWindow extends JPanel {

    private static final Logger LOGGER = new Logger(StatusWindow.class);

    private JProgressBar progressBar;
    private static StatusWindow me = null;
    private static JFrame frame = null;


    public StatusWindow(String text) {
        super(new BorderLayout());

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setString(text);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(400, progressBar.getPreferredSize().height));

        JPanel panel = new JPanel();
        panel.add(progressBar);

        add(panel, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    }



    public static void disappear() {
        LOGGER.debug("Disappearing");
        if(frame!=null) {
            frame.setVisible(false);
        }
    }

    public static void createAndShow(String text) {
        if(frame != null) {
            me.progressBar.setString(text);
            frame.setVisible(true);
            return;
        }
        final StatusWindow[] window = new StatusWindow[1];
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = StatusWindow.frame = new JFrame("ProgressBarDemo");
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setAlwaysOnTop(true);
                frame.setLocationRelativeTo(null);

                //Create and set up the content pane.
                window[0] = new StatusWindow(text);
                me = window[0];
                window[0].setOpaque(true); //content panes must be opaque
                frame.setContentPane(window[0]);

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}