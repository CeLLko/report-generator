/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.exceptions.GeneratorException;
import cz.muni.fi.sbapr.utils.RGHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

public enum Presentation {

    INSTANCE;

    private boolean initialized = false;
    private final XMLSlideShow PPTX;
    private List<Slide> slides = new ArrayList();
    private File file = null;

    private Presentation() {
        PPTX = new XMLSlideShow();
    }

    /**
     * Initiates a Presentation object
     *
     * @param file
     */
    public void init() {
        if (RGHelper.INSTANCE.isInitialized()) {
            slides = RGHelper.INSTANCE.parseSlides();
        }
    }

    /**
     * Builds the presentation file
     */
    public void build(File file) throws GeneratorException {
        ExecutorService pool = Executors.newFixedThreadPool(slides.size());
        try {
            pool.invokeAll(slides);
        } catch (InterruptedException ex) {
            throw new GeneratorException(ex.getMessage());
        } finally {
            pool.shutdown();
        }
        System.out.println("Presentation created successfully");

        FileOutputStream out = null;
        if(file == null){
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("PowerPoint files", "pptx"));
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("pptx")) {
                    // filename is OK as-is
                } else {
                    file = new File(file.toString() + ".pptx");
                    file = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".pptx");
                }
            } else {
                return;
            }
        }
       
        try {
            out = new FileOutputStream(file);
            PPTX.write(out);
            out.close();
        } catch (IOException ex) {
            throw new GeneratorException(ex.getMessage());
        }
        System.out.println("Presentation saved successfully");
    }

    public XMLSlideShow getPPTX() {
        return PPTX;
    }

    public List<Slide> getSlides() {
        return slides;
    }

    /**
     * Checks if the Presentation instance has been initialized
     *
     * @return initialization status
     */
    public boolean isInitialized() {
        return initialized;
    }
}
