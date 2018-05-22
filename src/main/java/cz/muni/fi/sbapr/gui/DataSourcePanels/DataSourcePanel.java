/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui.DataSourcePanels;

import java.awt.BorderLayout;
import java.awt.Dialog;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Adam
 */
public abstract class DataSourcePanel extends JPanel{

    /**
     *
     */
    protected static final Border ERROR_BORDER = BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 3);

    /**
     *
     */
    protected static final Border DEFAULT_BORDER = UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border");

    /**
     *
     */
    protected final Dialog parent;
    
    /**
     *
     * @param parent
     */
    protected DataSourcePanel(Dialog parent){
       super(new BorderLayout());
       this.parent = parent;
    }
    
    /**
     *
     * @param element
     * @param name
     * @return
     */
    protected static String getAttribute(Element element, String name){
        Node node = element.getElementsByTagName(name).item(0);
        return node == null ? null : node.getTextContent();
    }
    
    /**
     *
     * @param element
     * @return
     */
    public abstract boolean updateElement(Element element);
    
    /**
     *
     * @param element
     */
    public abstract void loadElement(Element element);
}
