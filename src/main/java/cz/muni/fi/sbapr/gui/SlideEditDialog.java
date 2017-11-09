/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.Slide;
import cz.muni.fi.sbapr.utils.RGHelper;
import java.awt.Component;
import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.xml.transform.TransformerException;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Attr;

/**
 *
 * @author adamg
 */
public class SlideEditDialog extends javax.swing.JDialog {

    private Slide slide = null;
    private Slide changedSlide;
    private String originalLayoutName = null;
    private boolean isNew = true, changed = false;

    /**
     * Creates new form SlideEditDialog
     */
    public SlideEditDialog(Frame parent, Slide slide, boolean isNew) {
        super(parent, false);
        this.isNew = isNew;
        this.slide = slide;
        initComponents();
        if (isNew) {
            changedSlide = slide;
            Attr layoutAttr = RGHelper.INSTANCE.getDoc().createAttribute("layout");
            layoutAttr.setValue(((XSLFSlideLayout) comboBoxTemplate.getSelectedItem()).getName());
            changedSlide.getElement().setAttributeNode(layoutAttr);
            Attr descriptionAttr = RGHelper.INSTANCE.getDoc().createAttribute("description");
            descriptionAttr.setValue(fieldDescription.getText());
            changedSlide.getElement().setAttributeNode(descriptionAttr);
        } else {
            try {
                changedSlide = slide.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(SlideEditDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            originalLayoutName = changedSlide.getElement().getAttribute("layout");
            ((ComboBoxLayoutModel) comboBoxTemplate.getModel()).setSelectedItem(originalLayoutName);
            fieldDescription.setText(changedSlide.getElement().getAttribute("description"));
        }
        Stream<XSLFShape> list = ((XSLFSlideLayout) comboBoxTemplate.getSelectedItem()).getShapes().stream().filter(shape -> shape instanceof XSLFTextShape);
        list.forEach(shape -> ((EtchASketch) layoutPanel).addPlaceholderButton((XSLFTextShape) shape));
        ((EtchASketch) layoutPanel).repaint();

    }

    public Slide getSlide() {
        return slide;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonCancel = new javax.swing.JButton();
        comboBoxTemplate = new javax.swing.JComboBox<>();
        labelTemplate = new javax.swing.JLabel();
        layoutPanel = new EtchASketch(RGHelper.INSTANCE.getTemplate().getPageSize(), this);
        buttonOK = new javax.swing.JButton();
        fieldDescription = new javax.swing.JTextField();
        labelDescription = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        buttonCancel.setText("Cancel");
        buttonCancel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        buttonCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonCancel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        comboBoxTemplate.setModel(new ComboBoxLayoutModel(RGHelper.INSTANCE.getLayouts().values().stream().findFirst().get())
        );
        comboBoxTemplate.setSelectedIndex(0);
        comboBoxTemplate.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(final JList list, Object value, final int index, final boolean isSelected,
                final boolean cellHasFocus) {
                if(value instanceof XSLFSlideLayout)
                value = ((XSLFSlideLayout)value).getName().replace("_","")
                .replaceAll(String.format("%s|%s|%s",
                    "(?<=[A-Z])(?=[A-Z][a-z])",
                    "(?<=[^A-Z])(?=[A-Z])",
                    "(?<=[A-Za-z])(?=[^A-Za-z])"
                )," ");;

                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        comboBoxTemplate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxTemplateItemStateChanged(evt);
            }
        });
        comboBoxTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxTemplateActionPerformed(evt);
            }
        });
        comboBoxTemplate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                comboBoxTemplatePropertyChange(evt);
            }
        });

        labelTemplate.setText("Template:");

        layoutPanel.setBackground(new java.awt.Color(255, 255, 255));
        layoutPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        layoutPanel.setFocusable(false);
        layoutPanel.setPreferredSize(new java.awt.Dimension(780, 540));
        layoutPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                layoutPanelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout layoutPanelLayout = new javax.swing.GroupLayout(layoutPanel);
        layoutPanel.setLayout(layoutPanelLayout);
        layoutPanelLayout.setHorizontalGroup(
            layoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        layoutPanelLayout.setVerticalGroup(
            layoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );

        buttonOK.setText("OK");
        buttonOK.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        buttonOK.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKActionPerformed(evt);
            }
        });

        fieldDescription.setToolTipText("Slide description");
        fieldDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldDescriptionActionPerformed(evt);
            }
        });

        labelDescription.setText("Description:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(layoutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 612, Short.MAX_VALUE)
                        .addComponent(buttonOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonCancel))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelTemplate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelDescription)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(fieldDescription))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTemplate)
                    .addComponent(labelDescription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(layoutPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCancel)
                    .addComponent(buttonOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKActionPerformed
        //this.slide.setDescription(this.textFieldDescription.getText());
        changedSlide.getElement().setAttribute("layout", ((ComboBoxLayoutModel) comboBoxTemplate.getModel()).getSelectedItemName());
        changedSlide.getElement().setAttribute("description", fieldDescription.getText());
        
        PresentationGUI.INSTANCE.getSlides().set(PresentationGUI.INSTANCE.getSlides().lastIndexOf(slide), changedSlide);
        hide();
    }//GEN-LAST:event_buttonOKActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        if (isNew && !changed) {
        } else if (changed) {
            //slide.getElement().getParentNode().replaceChild(changedNode, slide.getElement());
        }
        hide();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void comboBoxTemplatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_comboBoxTemplatePropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxTemplatePropertyChange

    private void comboBoxTemplateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxTemplateItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxTemplateItemStateChanged

    private void comboBoxTemplateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxTemplateActionPerformed
        ((EtchASketch) layoutPanel).shake();
        Stream<XSLFShape> list = ((XSLFSlideLayout) comboBoxTemplate.getSelectedItem()).getShapes().stream().filter(shape -> shape instanceof XSLFTextShape);
        list.forEach(shape -> ((EtchASketch) layoutPanel).addPlaceholderButton((XSLFTextShape) shape));
        ((EtchASketch) layoutPanel).repaint();
        String selectedLayoutName = ((ComboBoxLayoutModel) comboBoxTemplate.getModel()).getSelectedItemName();
        if (selectedLayoutName == originalLayoutName && !isNew) {
            try {
                changedSlide = slide.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(SlideEditDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            changed = true;
            changedSlide.getSlideElements().clear();
        }
        try {
            RGHelper.INSTANCE.printXML();
        } catch (TransformerException ex) {
            Logger.getLogger(SlideEditDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_comboBoxTemplateActionPerformed

    private void layoutPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_layoutPanelComponentResized

    }//GEN-LAST:event_layoutPanelComponentResized

    private void fieldDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldDescriptionActionPerformed
        changed = true;
    }//GEN-LAST:event_fieldDescriptionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonOK;
    private javax.swing.JComboBox<String> comboBoxTemplate;
    private javax.swing.JTextField fieldDescription;
    private javax.swing.JLabel labelDescription;
    private javax.swing.JLabel labelTemplate;
    private javax.swing.JPanel layoutPanel;
    // End of variables declaration//GEN-END:variables
}
