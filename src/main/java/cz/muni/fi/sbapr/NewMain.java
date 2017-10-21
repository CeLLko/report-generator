/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.utils.RGHelper;
import cz.muni.fi.sbapr.utils.WindowsRegistry;
import java.io.IOException;
import java.util.zip.ZipFile;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.xpath.*;

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
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            zipFile = new ZipFile(fc.getSelectedFile());
        }
        
        RGHelper.INSTANCE.parse(zipFile);
        Presentation.INSTANCE.init("name.pptx");
        Presentation.INSTANCE.build();
        String value = WindowsRegistry.readRegistry("HKLM\\SOFTWARE\\Microsoft\\Office\\16.0\\PowerPoint\\InstallRoot", "Path");
        Process p = Runtime.getRuntime().exec(value+"\\POWERPNT.EXE E:\\Dokumenty\\Skola\\SBAPR\\SBAPR\\name.pptx");
    }
}
