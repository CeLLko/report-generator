/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.utils.RGHelper;
import cz.muni.fi.sbapr.utils.WindowsRegistry;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.xpath.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

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
            if (zipFile.isEncrypted()) {
                zipFile.setPassword("hodor");
            }
        } catch (ZipException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        RGHelper.INSTANCE.parse(zipFile);
        Presentation.INSTANCE.init("name.pptx");
        Presentation.INSTANCE.build();
        String value = WindowsRegistry.readRegistry("HKLM\\SOFTWARE\\Microsoft\\Office\\16.0\\PowerPoint\\InstallRoot", "Path");
        Process p = Runtime.getRuntime().exec(value + "\\POWERPNT.EXE E:\\Dokumenty\\Skola\\SBAPR\\SBAPR\\name.pptx");
    }
}
