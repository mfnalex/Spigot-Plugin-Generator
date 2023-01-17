package com.jeff_media.maven_spigot_plugin_gui.gui;

import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;

import javax.swing.*;
import javax.swing.text.TableView;
import java.util.ArrayList;
import java.util.List;

class DependencyTable extends JTable {

    private final List<RequiredProperty> properties;

    public DependencyTable(List<RequiredProperty> properties) {
        super(properties.stream().filter(RequiredProperty::isDependency).map(RequiredProperty::asTableRow).toArray(Object[][]::new), new String[]{"Enabled", "Name", "Description"});
        this.properties = properties;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0) return Boolean.class;
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 0;
    }

    public List<RequiredProperty> getSelectedDependencies() {
        List<RequiredProperty> selectedDependencies = new ArrayList<>();
        for(int i = 0; i < properties.size(); i++) {
            boolean selected = (boolean) getModel().getValueAt(i, 0);
            if(selected) {
                selectedDependencies.add(properties.get(i));
            }
        }
        return selectedDependencies;
    }
}
