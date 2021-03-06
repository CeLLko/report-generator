/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.SlideElement;
import cz.muni.fi.sbapr.gui.DataSourcePanels.DataSourcePanel;
import cz.muni.fi.sbapr.utils.RGHelper;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class SlideElementEditDialog extends javax.swing.JDialog {

    private SlideEditDialog parent = null;
    private SlideElement slideElement = null;
    private String originalDataSourceName = null;
    private boolean isNew;
    private boolean changed = false;

    /**
     * Creates new form DataSourceEditDialog
     * @param parent
     * @param slideElement
     * @param isNew
     */
    public SlideElementEditDialog(java.awt.Dialog parent, SlideElement slideElement, boolean isNew) {
        super(parent, true);
        this.parent = (SlideEditDialog) parent;
        this.isNew = isNew;
        this.slideElement = slideElement;
        if (isNew) {
            initComponents();
            Attr dataSourceAttr = RGHelper.INSTANCE.getDoc().createAttribute("dataSource");
            dataSourceAttr.setValue("");
            slideElement.getElement().setAttributeNode(dataSourceAttr);
            Attr descriptionAttr = RGHelper.INSTANCE.getDoc().createAttribute("description");
            descriptionAttr.setValue(fieldDescription.getText());
            slideElement.getElement().setAttributeNode(descriptionAttr);
        } else{
            originalDataSourceName = slideElement.getElement().getAttribute("dataSource");
            initComponents();
            changeDataSourcePanel();
        }
        fieldDescription.setText(slideElement.getDescription());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonOK = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        frameDS = new javax.swing.JInternalFrame();
        comboBoxDataSource = new javax.swing.JComboBox<>();
        fieldDescription = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(320, 180));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        buttonOK.setText("OK");
        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKActionPerformed(evt);
            }
        });

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        frameDS.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        BasicInternalFrameUI bi = (BasicInternalFrameUI)frameDS.getUI();
        bi.setNorthPane(null);
        frameDS.setFocusable(false);
        frameDS.setMinimumSize(new java.awt.Dimension(300, 53));
        frameDS.setPreferredSize(new java.awt.Dimension(300, 53));
        frameDS.setVisible(true);

        javax.swing.GroupLayout frameDSLayout = new javax.swing.GroupLayout(frameDS.getContentPane());
        frameDS.getContentPane().setLayout(frameDSLayout);
        frameDSLayout.setHorizontalGroup(
            frameDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        frameDSLayout.setVerticalGroup(
            frameDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 93, Short.MAX_VALUE)
        );

        comboBoxDataSource.setModel(new ComboBoxDataSourceModel(originalDataSourceName));
        comboBoxDataSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxDataSourceActionPerformed(evt);
            }
        });

        fieldDescription.setText("Description");
        fieldDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDescriptionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fieldDescription)
                    .addComponent(comboBoxDataSource, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 160, Short.MAX_VALUE)
                        .addComponent(buttonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel))
                    .addComponent(frameDS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonCancel, buttonOK});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboBoxDataSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(frameDS, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCancel)
                    .addComponent(buttonOK))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(buttonOK);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKActionPerformed
        slideElement.getElement().setAttribute("dataSource", (String) ((ComboBoxDataSourceModel) comboBoxDataSource.getModel()).getSelectedItem());
        Element newElement = (Element) slideElement.getElement().cloneNode(true);
        slideElement.getElement().setAttribute("description", fieldDescription.getText());
            this.parent.reloadLayoutButtons();
        if (frameDS.getContentPane() instanceof DataSourcePanel && ((DataSourcePanel) frameDS.getContentPane()).updateElement(newElement)) {
            slideElement.setElement(newElement);
            hide();
        } else {
            show();
        }
    }//GEN-LAST:event_buttonOKActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        hide();
    }//GEN-LAST:event_buttonCancelActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
    }//GEN-LAST:event_closeDialog

    private void comboBoxDataSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxDataSourceActionPerformed
        changeDataSourcePanel();
    }//GEN-LAST:event_comboBoxDataSourceActionPerformed

    private void fieldDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDescriptionActionPerformed
        changed = true;
    }//GEN-LAST:event_fieldDescriptionActionPerformed

    private void changeDataSourcePanel() {
        String selectedItem = (String) comboBoxDataSource.getSelectedItem();
        JPanel newPanel = RGHelper.INSTANCE.getNewDataSourcePanelInstance(selectedItem, this);
        frameDS.setContentPane(newPanel);
        changed = true;
        if ((selectedItem == null ? originalDataSourceName == null : selectedItem.equals(originalDataSourceName)) && !isNew) {
            ((DataSourcePanel) newPanel).loadElement(slideElement.getElement());
            changed = false;
        }
        resize();
    }

    /**
     *
     */
    public void resize() {
        setMinimumSize(new Dimension(340, frameDS.getContentPane().getMinimumSize().height + 160));
        setPreferredSize(getMinimumSize());
        pack();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOK;
    private javax.swing.JComboBox<String> comboBoxDataSource;
    private javax.swing.JTextField fieldDescription;
    private javax.swing.JInternalFrame frameDS;
    // End of variables declaration//GEN-END:variables

}
