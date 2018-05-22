/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.utils.RGHelper;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Adam
 */
public class ComboBoxDataSourceModel extends AbstractListModel implements ComboBoxModel {

    private final String[] dataSourceNames;
    private String selectedDataSource = null;

    /**
     *
     * @param defaultDataSource
     */
    public ComboBoxDataSourceModel(String defaultDataSource) {
        dataSourceNames = RGHelper.INSTANCE.getDataSourceNames();
        this.setSelectedItem(defaultDataSource);
    }

    @Override
    public int getSize() {
        return dataSourceNames.length;
    }

    @Override
    public Object getElementAt(int index) {
        return dataSourceNames[index];
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedDataSource = (String) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedDataSource;
    }

}
