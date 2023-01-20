package com.jeff_media.maven_spigot_plugin_gui.data;

import javax.swing.*;
import java.awt.*;

public class WrappedComponent {
    private final Component component;
    private final Boolean booleanValue;

    public WrappedComponent(Component component) {
        this.component = component;
        this.booleanValue = null;
    }

    public WrappedComponent(boolean booleanValue) {
        this.booleanValue = booleanValue;
        this.component = null;
    }

    public String getValue() {
        if(booleanValue != null) {
            return String.valueOf(booleanValue);
        }
        if(component instanceof JTextField) {
            return ((JTextField) component).getText();
        } else if(component instanceof JCheckBox) {
            return String.valueOf(((JCheckBox) component).isSelected());
        } else {
            throw new IllegalArgumentException(component + " is not a readable component");
        }
    }
}
