/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.DataSources.DataSource;
import java.util.concurrent.Callable;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFShape;

/**
 * Callable slide element, holds references to 
 * {@link org.apache.poi.xslf.usermodel.XSLFSlide slide} and its 
 * {@link {@link org.apache.poi.xslf.usermodel.XSLFShape placeholder} 
 * as well as the {@link cz.muni.fi.sbapr.DataSources.DataSource datasource}.
 * @author Adam
 */
public class SlideElement implements Callable<XSLFShape> {
    private final XSLFSlide slide;
    private final XSLFShape placeholder;
    private final DataSource dataSource;
       
    public SlideElement(XSLFSlide slide ,XSLFShape placeholder, DataSource dataSource) {
        this.slide = slide;
        this.placeholder = placeholder;
        this.dataSource = dataSource;
    }
    
    /**
     * Updates the desired {@link org.apache.poi.xslf.usermodel.XSLFShape placeholder(shape)} with new data
     * @return updated {@link org.apache.poi.xslf.usermodel.XSLFShape, shape}
     * @throws Exception 
     */
    @Override
    public XSLFShape call() throws Exception {
        return dataSource.updateShape(slide, placeholder);
    }
    
}
