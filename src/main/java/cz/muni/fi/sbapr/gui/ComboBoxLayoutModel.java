/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.utils.RGHelper;
import java.io.IOException;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author Adam
 */
public class ComboBoxLayoutModel extends AbstractListModel implements ComboBoxModel{
    
    private final String[] layoutNames;
    private XSLFSlideLayout selectedLayout = null;

    public ComboBoxLayoutModel(){
        layoutNames = RGHelper.INSTANCE.getLayoutNames();
        selectedLayout = RGHelper.INSTANCE.getLayout(layoutNames[0]);
    }
    
    @Override
    public int getSize() {
        return layoutNames.length;
    }

    @Override
    public Object getElementAt(int index) {
        return RGHelper.INSTANCE.getLayout(layoutNames[index]);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedLayout = (XSLFSlideLayout) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedLayout;
    }
    
    private class XSLFSlideLayoutString extends XSLFSlideLayout{

        public XSLFSlideLayoutString(PackagePart part) throws IOException, XmlException {
            super(part);
        }
        
        @Override
        public String toString(){
            return getName();
        }
        
    }
    
}
