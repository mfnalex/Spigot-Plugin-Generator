package com.jeff_media.maven_spigot_plugin_gui.data;

import lombok.Data;

import java.awt.*;

@Data
public class WrappedComponent {
    private final Component component;

    public String getValue() {
        if(component instanceof TextField) {
            return ((TextField) component).getText();
        } else if(component instanceof Checkbox) {
            return String.valueOf(((Checkbox) component).getState());
        } else {
            return null;
        }
    }
}
