/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.w3c.dom.Element;

/**
 * The datasource superclass
 * @author Adam
 */
public abstract class DataSource<T>{
    
    private Element element;
    
    protected DataSource(Element element){
        this.element = element;
    }
            
    public abstract T getData();
    
    public abstract XSLFShape updateShape(XSLFSlide slide, XSLFShape shape);
    
    
    protected String getAttribute(String name){
        return element.getElementsByTagName(name).item(0).getTextContent();
    }
}
