/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

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
import org.apache.poi.xslf.usermodel.XMLSlideShow;

/**
 *
 * @author adamg
 */
public enum Presentation {

    /**
     *
     */
    INSTANCE;

    private boolean initialized = false;

    private final XMLSlideShow PPTX;
    private List<Slide> slides = new ArrayList<>();
    private File file = null;

    private Presentation() {
        PPTX = new XMLSlideShow();
    }

    /**
     * Initiates a Presentation object
     *
     * @param name output file name 
     */
    public void init(String name) {
        name = (name.endsWith(".ppt") || name.endsWith(".pptx")) ? name : name + ".pptx";
        file = new File(name);

        if (RGHelper.INSTANCE.isInitialized()) {
            slides = RGHelper.INSTANCE.parseSlides();
            slides.forEach(slide -> slide.parse());
        }
    }

    /**
     * Builds the presentation file
     */
    public void build() {
        ExecutorService pool = Executors.newFixedThreadPool(slides.size());
        try {
            pool.invokeAll(slides);
        } catch (InterruptedException ex) {
            Logger.getLogger(Presentation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pool.shutdown();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            PPTX.write(out);
            System.out.println("Presentation created successfully");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Presentation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Presentation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Presentation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * @return the working {@link org.apache.poi.xslf.usermodel.XMLSlideShow XMLLSlideShow} reference
     */
    public XMLSlideShow getPPTX() {
        return PPTX;
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
