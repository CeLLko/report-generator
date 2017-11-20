/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.gui.PresentationGUI;
import cz.muni.fi.sbapr.utils.RGHelper;
import cz.muni.fi.sbapr.utils.WindowsRegistry;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.xpath.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.exception.ZipExceptionConstants;
import net.lingala.zip4j.model.FileHeader;

/**
 *
 * @author adamg
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws XPathExpressionException, IOException {
        ZipFile zipFile = null;
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Report Generator files", "rg"));
        try {
            if (args.length == 0) {
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    zipFile = new ZipFile(fc.getSelectedFile());
                } else {
                    return;
                }
            } else {
                zipFile = new ZipFile(args[0]);
            }
            if ( zipFile.isValidZipFile()) {
                if (zipFile.isEncrypted()) {
                    JPanel panel = new JPanel();
                    JLabel label = new JLabel("Enter a password:");
                    JPasswordField pass = new JPasswordField(50);
                    panel.add(label);
                    panel.add(pass);
                    String[] options = new String[]{"OK", "Cancel"};
                    JOptionPane passPane = new JOptionPane(panel, JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, options, options[0]);
                    JDialog dialog = passPane.createDialog(null, "");
                    boolean correctPass = false;
                    do {
                        dialog.show();
                        if (options[0] == passPane.getValue()) {
                            char[] password = pass.getPassword();
                            zipFile.setPassword(password);
                            try {
                                zipFile.getInputStream((FileHeader) zipFile.getFileHeaders().get(0));
                                correctPass = true;
                            } catch (ZipException ex) {
                                if (ex.getCode() == ZipExceptionConstants.WRONG_PASSWORD) {
                                    pass.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 3));
                                    correctPass = false;
                                }
                            }
                            RGHelper.INSTANCE.getZipParams().setPassword(password);
                        } else {
                            return;
                        }
                    } while (!correctPass);
                }
                RGHelper.INSTANCE.parse(zipFile);
            }
        } catch (IOException | ZipException ex) {
            Logger.getLogger(PresentationGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        RGHelper.INSTANCE.parse(zipFile);
        Presentation.INSTANCE.init("name.pptx");
        Presentation.INSTANCE.build();
        String value = WindowsRegistry.readRegistry("HKLM\\SOFTWARE\\Microsoft\\Office\\16.0\\PowerPoint\\InstallRoot", "Path");
        Process p = Runtime.getRuntime().exec(value + "\\POWERPNT.EXE E:\\Dokumenty\\Skola\\SBAPR\\SBAPR\\name.pptx");
    }
}
