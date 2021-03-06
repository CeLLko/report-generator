/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui.DataSourcePanels;

import cz.muni.fi.sbapr.gui.SlideElementEditDialog;
import cz.muni.fi.sbapr.utils.IterableNodeList;
import java.awt.Dialog;
import java.awt.Dimension;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class EmptyDataSourcePanel extends DataSourcePanel {

    /**
     * Creates new form DefaultDataSourcePanel
     *
     * @param parent
     */
    public EmptyDataSourcePanel(Dialog parent) {
        super(parent);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMinimumSize(new java.awt.Dimension(280, 56));
        setPreferredSize(new java.awt.Dimension(280, 56));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     *
     */
    public void resize() {
        setPreferredSize(new Dimension(getSize().width, getMinimumSize().height));
        ((SlideElementEditDialog) parent).setMinimumSize(new Dimension(320, getMinimumSize().height + 130));
        ((SlideElementEditDialog) parent).setPreferredSize(new Dimension(getSize().width+40, getMinimumSize().height));
        ((SlideElementEditDialog) parent).pack();
        revalidate();
    }

    /**
     *
     * @param element
     * @return
     */
    @Override
    public boolean updateElement(Element element) {
        new IterableNodeList(element.getChildNodes()).forEach(child -> element.removeChild(child));
        return true;
    }

    /**
     *
     * @param element
     */
    @Override
    public void loadElement(Element element) {
        
    }
}
