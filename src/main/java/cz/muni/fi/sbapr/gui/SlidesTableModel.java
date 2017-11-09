/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.Slide;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author adamg
 */
public class SlidesTableModel extends AbstractTableModel {

    @Override
    public int getRowCount() {
        return PresentationGUI.INSTANCE.getSlides().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Slide slide = PresentationGUI.INSTANCE.getSlides().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex;
            case 1:
                return slide.getDescription();
            default:
                throw new IllegalArgumentException("column index out of bounds");
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return Object.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
