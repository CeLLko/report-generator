/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.Slide;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class SlideGUI extends Slide implements Cloneable {
  
    public SlideGUI(){
        super();
    }
    
    public SlideGUI(Element slideElement) {
        super(slideElement);
    }

    public String getDescription(){
        return (slideElement).getAttribute("description");
    }
    
    @Override
    public SlideGUI clone() throws CloneNotSupportedException {
        return (SlideGUI) super.clone();
    }
}
