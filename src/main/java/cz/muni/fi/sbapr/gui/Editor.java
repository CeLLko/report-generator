package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.utils.RGHelper;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author adamg
 */
public class Editor extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    public Editor() {
        initComponents();
        Window thisWindow = this;
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    ExitMenu();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(thisWindow, "Couldn't save file");
                }
            }
        });
        PresentationGUI.INSTANCE.setSlideTable((SlidesTableModel) slidesTable.getModel());
        PresentationGUI.INSTANCE.setMainWindow(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        slidesScrollPanel = new javax.swing.JScrollPane();
        slidesTable = new javax.swing.JTable();
        slidesTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2) {
                    PresentationGUI.INSTANCE.updateSlide(slidesTable.getSelectedRow());
                }
            }
        });
        toolsPanel = new javax.swing.JPanel();
        toolsBar = new javax.swing.JToolBar();
        buttonAdd = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonDuplicate = new javax.swing.JButton();
        buttonMoveUp = new javax.swing.JButton();
        buttonMoveDown = new javax.swing.JButton();
        buttonEdit = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        fileMenuNew = new javax.swing.JMenuItem();
        fileMenuOpen = new javax.swing.JMenuItem();
        fileMenuSave = new javax.swing.JMenuItem();
        fileMenuSaveAs = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        fileMenuSetPassword = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        fileMenuExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(230, 200));

        slidesScrollPanel.setPreferredSize(new java.awt.Dimension(600, 400));

        slidesTable.setModel(new SlidesTableModel());
        slidesTable.setRowHeight(30);
        slidesTable.setShowVerticalLines(false);
        slidesTable.setTableHeader(null
        );
        slidesScrollPanel.setViewportView(slidesTable);

        getContentPane().add(slidesScrollPanel, java.awt.BorderLayout.CENTER);

        toolsPanel.setBackground(new java.awt.Color(255, 255, 255));
        toolsPanel.setPreferredSize(new java.awt.Dimension(400, 33));

        toolsBar.setBackground(new java.awt.Color(255, 255, 255));
        toolsBar.setRollover(true);
        toolsBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        toolsBar.setPreferredSize(new java.awt.Dimension(400, 33));

        buttonAdd.setBackground(new java.awt.Color(255, 255, 255));
        buttonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/new.png"))); // NOI18N
        buttonAdd.setBorderPainted(false);
        buttonAdd.setFocusable(false);
        buttonAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                buttonAddMouseReleased(evt);
            }
        });
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });
        toolsBar.add(buttonAdd);

        buttonDelete.setBackground(new java.awt.Color(255, 255, 255));
        buttonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/delete.png"))); // NOI18N
        buttonDelete.setBorderPainted(false);
        buttonDelete.setFocusable(false);
        buttonDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                buttonDeleteMouseReleased(evt);
            }
        });
        toolsBar.add(buttonDelete);

        buttonDuplicate.setBackground(new java.awt.Color(255, 255, 255));
        buttonDuplicate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/duplicate.png"))); // NOI18N
        buttonDuplicate.setBorderPainted(false);
        buttonDuplicate.setFocusable(false);
        buttonDuplicate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonDuplicate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonDuplicate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                buttonDuplicateMouseReleased(evt);
            }
        });
        toolsBar.add(buttonDuplicate);

        buttonMoveUp.setBackground(new java.awt.Color(255, 255, 255));
        buttonMoveUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/up.png"))); // NOI18N
        buttonMoveUp.setBorderPainted(false);
        buttonMoveUp.setFocusable(false);
        buttonMoveUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonMoveUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonMoveUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                buttonMoveUpMouseReleased(evt);
            }
        });
        toolsBar.add(buttonMoveUp);

        buttonMoveDown.setBackground(new java.awt.Color(255, 255, 255));
        buttonMoveDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/down.png"))); // NOI18N
        buttonMoveDown.setBorderPainted(false);
        buttonMoveDown.setFocusable(false);
        buttonMoveDown.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonMoveDown.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonMoveDown.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                buttonMoveDownMouseReleased(evt);
            }
        });
        toolsBar.add(buttonMoveDown);

        buttonEdit.setBackground(new java.awt.Color(255, 255, 255));
        buttonEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/edit.png"))); // NOI18N
        buttonEdit.setBorderPainted(false);
        buttonEdit.setFocusable(false);
        buttonEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                buttonEditMouseReleased(evt);
            }
        });
        toolsBar.add(buttonEdit);

        javax.swing.GroupLayout toolsPanelLayout = new javax.swing.GroupLayout(toolsPanel);
        toolsPanel.setLayout(toolsPanelLayout);
        toolsPanelLayout.setHorizontalGroup(
            toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolsBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        toolsPanelLayout.setVerticalGroup(
            toolsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolsPanelLayout.createSequentialGroup()
                .addComponent(toolsBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(toolsPanel, java.awt.BorderLayout.PAGE_END);

        fileMenu.setText("File");

        fileMenuNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        fileMenuNew.setText("New");
        fileMenuNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuNewActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuNew);

        fileMenuOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        fileMenuOpen.setText("Open");
        fileMenuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuOpenActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuOpen);

        fileMenuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        fileMenuSave.setText("Save");
        fileMenuSave.setToolTipText("");
        fileMenuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuSaveActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuSave);

        fileMenuSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        fileMenuSaveAs.setText("Save as");
        fileMenuSaveAs.setToolTipText("");
        fileMenuSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuSaveAsActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuSaveAs);
        fileMenu.add(jSeparator2);

        fileMenuSetPassword.setText("Set Password");
        fileMenuSetPassword.setToolTipText("");
        fileMenuSetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuSetPasswordActionPerformed(evt);
            }
        });
        fileMenu.add(fileMenuSetPassword);
        fileMenu.add(jSeparator1);

        fileMenuExit.setText("Exit");
        fileMenuExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fileMenuExitMouseReleased(evt);
            }
        });
        fileMenu.add(fileMenuExit);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAddMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonAddMouseReleased
        if (RGHelper.INSTANCE.isInitialized()) {
            PresentationGUI.INSTANCE.createSlide();
        } else {
            NewFileMenu();
            PresentationGUI.INSTANCE.createSlide();
        }
    }//GEN-LAST:event_buttonAddMouseReleased

    private void buttonDeleteMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonDeleteMouseReleased
        try {
            PresentationGUI.INSTANCE.deleteSlide(slidesTable.getSelectedRows());
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_buttonDeleteMouseReleased

    private void buttonDuplicateMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonDuplicateMouseReleased
        try {
            PresentationGUI.INSTANCE.duplicateSlide(slidesTable.getSelectedRow());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonDuplicateMouseReleased

    private void buttonMoveUpMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonMoveUpMouseReleased
        try {
            PresentationGUI.INSTANCE.moveSlide(slidesTable.getSelectedRow(), slidesTable.getSelectedRow() - 1);
            slidesTable.setRowSelectionInterval(slidesTable.getSelectedRow() - 1, slidesTable.getSelectedRow() - 1);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
        }
    }//GEN-LAST:event_buttonMoveUpMouseReleased

    private void buttonMoveDownMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonMoveDownMouseReleased
        try {
            PresentationGUI.INSTANCE.moveSlide(slidesTable.getSelectedRow(), slidesTable.getSelectedRow() + 1);
            slidesTable.setRowSelectionInterval(slidesTable.getSelectedRow() + 1, slidesTable.getSelectedRow() + 1);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
        }
    }//GEN-LAST:event_buttonMoveDownMouseReleased

    private void buttonEditMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonEditMouseReleased
        if (RGHelper.INSTANCE.isInitialized()) {
            PresentationGUI.INSTANCE.updateSlide(slidesTable.getSelectedRow());
        } else {
        }
    }//GEN-LAST:event_buttonEditMouseReleased

    private void fileMenuExitMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileMenuExitMouseReleased
        try {
            ExitMenu();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Couldn't save file");
        }
    }//GEN-LAST:event_fileMenuExitMouseReleased

    private void fileMenuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuOpenActionPerformed
        OpenFileMenu();
    }//GEN-LAST:event_fileMenuOpenActionPerformed

    private void ExitMenu() throws IOException {
        if (PresentationGUI.INSTANCE.isChanged() && PresentationGUI.INSTANCE.getSlides().size() > 0) {
            Object[] options = {"Save", "Don't save", "Cancel"};
            int res = JOptionPane.showOptionDialog(this, "Would you like to save changes?", "Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
            switch (res) {
                case JOptionPane.YES_OPTION:
                    PresentationGUI.INSTANCE.save();
                    System.exit(0);
                    break;
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
            }
        } else {
            System.exit(0);
        }
    }

    private void CloseMenu() {
        try {
            if (PresentationGUI.INSTANCE.isChanged() && PresentationGUI.INSTANCE.getSlides().size() > 0) {
                Object[] options = {"Save", "Don't save", "Cancel"};
                int res = JOptionPane.showOptionDialog(this, "Would you like to save changes?", "Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                switch (res) {
                    case JOptionPane.YES_OPTION:
                        PresentationGUI.INSTANCE.save();
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                }
            PresentationGUI.INSTANCE.getSlides().clear();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Couldn't save the file.");
        }
    }

    private void OpenFileMenu() {
        try {
            CloseMenu();
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Report Generator files", "rg"));
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                ZipFile zipFile = new ZipFile(fc.getSelectedFile());
                if (zipFile.isValidZipFile()) {
                    PresentationGUI.INSTANCE.open(zipFile);
                }
            }
        } catch (ZipException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Couldn't parse the file, make sure if's a correct file.");
        }
    }

    private void NewFileMenu() {
        CloseMenu();
        File pptxFile = null;
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("PowerPoint Template", "ppt", "pptx"));
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            pptxFile = fc.getSelectedFile();
            PresentationGUI.INSTANCE.createNew(pptxFile);
        }
    }
    private void fileMenuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuSaveActionPerformed
        try {
            PresentationGUI.INSTANCE.save();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Couldn't save the file.");
        }
    }//GEN-LAST:event_fileMenuSaveActionPerformed

    private void fileMenuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuNewActionPerformed
        NewFileMenu();
    }//GEN-LAST:event_fileMenuNewActionPerformed

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddActionPerformed

    }//GEN-LAST:event_buttonAddActionPerformed

    private void buttonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEditActionPerformed

    }//GEN-LAST:event_buttonEditActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1MouseClicked

    private void fileMenuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuSaveAsActionPerformed
        try {
            PresentationGUI.INSTANCE.saveAs();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Couldn't save the file.");
        }
    }//GEN-LAST:event_fileMenuSaveAsActionPerformed

    private void fileMenuSetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuSetPasswordActionPerformed
        PresentationGUI.INSTANCE.setPassword();
    }//GEN-LAST:event_fileMenuSetPasswordActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Editor.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Editor.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Editor.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Editor.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Editor.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                new Editor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdd;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonDuplicate;
    private javax.swing.JButton buttonEdit;
    private javax.swing.JButton buttonMoveDown;
    private javax.swing.JButton buttonMoveUp;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem fileMenuExit;
    private javax.swing.JMenuItem fileMenuNew;
    private javax.swing.JMenuItem fileMenuOpen;
    private javax.swing.JMenuItem fileMenuSave;
    private javax.swing.JMenuItem fileMenuSaveAs;
    private javax.swing.JMenuItem fileMenuSetPassword;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JScrollPane slidesScrollPanel;
    private javax.swing.JTable slidesTable;
    private javax.swing.JToolBar toolsBar;
    private javax.swing.JPanel toolsPanel;
    // End of variables declaration//GEN-END:variables
}
