//package com.jeff_media.maven_spigot_plugin_gui.gui;
//
//import com.jeff_media.maven_spigot_plugin_gui.data.RequiredProperty;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.util.List;
//
//public class TableCheckBox extends JTable {
//
//    private static final long serialVersionUID = 1L;
//    private JTable table;
//    private final List<RequiredProperty> properties;
//
//    public TableCheckBox(List<RequiredProperty> properties) {
//        this.properties = properties;
//        Object[] columnNames = ;
//        Object[][] tableData = ;
//        DefaultTableModel model = new DefaultTableModel(tableData, columnNames);
//        table = new JTable(model) {
//
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public Class<?> getColumnClass(int column) {
//                switch (column) {
//                    case 0:
//                        return Boolean.class;
//                    case 1:
//                        return String.class;
//                    case 2:
//                        return String.class;
//                    default: throw new IllegalArgumentException();
//                }
//            }
//        };
//        table.setMinimumSize(new Dimension(400, 300));
//        table.setPreferredScrollableViewportSize(new Dimension(400, 300));
//        JScrollPane scrollPane = new JScrollPane(table);
//        add(scrollPane);
//    }
//}
