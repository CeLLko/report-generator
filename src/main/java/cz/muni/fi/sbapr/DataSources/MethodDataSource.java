/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class MethodDataSource extends DataSource<String>{

    private Method method;

    public MethodDataSource(Element element) {
        super(element);
        /*try {
            String className = ((String)args[0]).substring(0,((String)args[0]).lastIndexOf(".")-1);
            String methodName = ((String)args[0]).substring(((String)args[0]).lastIndexOf(".")+1);
            method = Class.forName(className).getDeclaredMethod(methodName, new Class[] {});
            this.args=args;
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
            Logger.getLogger(TextDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    @Override
    public String getData() {
        try {
            Object[] args = null;
            return method.invoke(null, args).toString();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(TextDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XSLFShape updateShape( XSLFShape shape) {
        ((XSLFTextShape) shape).clearText();
        ((XSLFTextShape) shape).appendText(this.getData(), false);
        return shape;
    }
    
}
