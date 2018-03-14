/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.DataSources.DataSource;
import cz.muni.fi.sbapr.utils.IterableNodeList;
import cz.muni.fi.sbapr.utils.RGHelper;
import java.util.concurrent.Callable;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.w3c.dom.Element;

/**
 * Callable slide element, holds references to
 * {@link org.apache.poi.xslf.usermodel.XSLFSlide slide} and its null {@link {@link org.apache.poi.xslf.usermodel.XSLFShape placeholder} 
 * as well as the {@link cz.muni.fi.sbapr.DataSources.DataSource datasource}.
 * @author Adam
 */
public class SlideElement implements Callable<XSLFShape>{

    private DataSource dataSource;
    private Element element;
    private XSLFShape shape;
    private String description = "Description";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SlideElement(XSLFShape shape, Element element) {
        this.shape = shape;
        this.element = element;
    }

    /**
     * Updates the desired
     * {@link org.apache.poi.xslf.usermodel.XSLFShape placeholder(shape)} with
     * new data
     *
     * @return updated {@link org.apache.poi.xslf.usermodel.XSLFShape, shape}
     * @throws Exception
     */
    @Override
    public XSLFShape call() throws Exception {
        this.dataSource = RGHelper.INSTANCE.getNewDataSourceInstance(element.getAttribute("dataSource"), element);
        XSLFShape res = dataSource.updateShape(shape);
        return res;
    }

    public Element getElement() {
        return element;
    }
    
    public void setElement(Element newElement){
        new IterableNodeList(element.getChildNodes()).forEach(node -> element.removeChild(node));
        new IterableNodeList(newElement.getChildNodes()).forEach(node -> element.appendChild(node));
        
    }
    
    

}
