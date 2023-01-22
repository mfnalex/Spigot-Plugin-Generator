package com.jeff_media.maven_spigot_plugin_gui.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class TableColumnAdjuster {

    private static final int SPACING = 10;

    private final JTable table;

    public TableColumnAdjuster(JTable table) {
        this.table = table;
    }

    public void adjustColumns() {
        TableColumnModel tcm = table.getColumnModel();
        for (int i = 0; i < tcm.getColumnCount(); i++)
            adjustColumn(i);
    }

    private void adjustColumn(final int column) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);

        if (!tableColumn.getResizable()) return;

        int columnHeaderWidth = getColumnHeaderWidth(column);
        int columnDataWidth = getColumnDataWidth(column);
        int preferredWidth = Math.max(columnHeaderWidth, columnDataWidth);

        updateTableColumn(column, preferredWidth);
    }

    private int getColumnHeaderWidth(int column) {

        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        Object value = tableColumn.getHeaderValue();
        TableCellRenderer renderer = tableColumn.getHeaderRenderer();

        if (renderer == null) renderer = table.getTableHeader().getDefaultRenderer();

        Component c = renderer.getTableCellRendererComponent(table, value, false, false, -1, column);
        return c.getPreferredSize().width;
    }

    private int getColumnDataWidth(int column) {
        int preferredWidth = 0;

        for (int row = 0; row < table.getRowCount(); row++)
            preferredWidth = Math.max(preferredWidth, getCellDataWidth(row, column));

        return preferredWidth;
    }

    private void updateTableColumn(int column, int width) {
        final TableColumn tableColumn = table.getColumnModel().getColumn(column);

        if (!tableColumn.getResizable()) return;

        table.getTableHeader().setResizingColumn(tableColumn);
        tableColumn.setWidth(width + SPACING);
    }

    private int getCellDataWidth(int row, int column) {
        TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
        Object value = table.getValueAt(row, column);
        Component c = cellRenderer.getTableCellRendererComponent(table, value, false, false, row, column);

        return c.getPreferredSize().width + table.getIntercellSpacing().width;
    }
}
