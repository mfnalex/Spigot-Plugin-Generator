package com.jeff_media.maven_spigot_plugin_gui.gui;

import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
import com.jeff_media.maven_spigot_plugin_gui.data.WrappedComponent;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.TableView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DependencyTable extends JTable {

    @Getter private final List<RequiredProperty> properties;

    public DependencyTable(MainMenu menu, List<RequiredProperty> properties) {
        super(properties.stream().sorted(RequiredProperty.COMPARATOR_BY_KEY).map(RequiredProperty::asTableRow).toArray(Object[][]::new), new String[]{"Enabled", "Name", "Description"});
        this.properties = properties.stream().sorted(RequiredProperty.COMPARATOR_BY_KEY).collect(Collectors.toList());
        this.setTableHeader(new JTableHeader(this.columnModel));
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

    public Map<RequiredProperty, WrappedComponent> getDependencies() {
        Map map = new HashMap();
        for(RequiredProperty property : properties) {
            map.put(property, new WrappedComponent((boolean) getModel().getValueAt(properties.indexOf(property), 0)));
        }
        return map;
    }
}
