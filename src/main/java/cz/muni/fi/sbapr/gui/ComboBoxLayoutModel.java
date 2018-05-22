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
public class ComboBoxLayoutModel extends AbstractListModel implements ComboBoxModel {

    private final String[] layoutNames;
    private XSLFSlideLayout selectedLayout = null;

    /**
     *
     * @param defaultLayout
     */
    public ComboBoxLayoutModel(XSLFSlideLayout defaultLayout) {
        layoutNames = RGHelper.INSTANCE.getLayoutNames();
        this.setSelectedItem(defaultLayout);
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

    /**
     *
     * @param name
     */
    public void setSelectedItem(String name) {
        setSelectedItem((XSLFSlideLayout) RGHelper.INSTANCE.getLayout(name));
    }

    @Override
    public Object getSelectedItem() {
        return selectedLayout;
    }

    /**
     *
     * @return
     */
    public String getSelectedItemName() {
        return RGHelper.INSTANCE.getLayouts().keySet().stream().filter(key -> RGHelper.INSTANCE.getLayout((String) key).equals(selectedLayout)).findFirst().get();
    }

    private class XSLFSlideLayoutString extends XSLFSlideLayout {

        public XSLFSlideLayoutString(PackagePart part) throws IOException, XmlException {
            super(part);
        }

        @Override
        public String toString() {
            return getName();
        }

    }

}
