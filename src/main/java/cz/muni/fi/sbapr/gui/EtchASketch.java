/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import cz.muni.fi.sbapr.SlideElement;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 *
 * @author Adam
 */
public class EtchASketch extends JPanel {

    private final Map<Integer, LayoutButton> shapes;
    private final Dimension layoutDimension;
    private final JDialog parent;

    public EtchASketch(Dimension dimension, SlideEditDialog parent) {
        this.parent = parent;
        this.layoutDimension = dimension;
        this.shapes = new HashMap<>();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });
    }

    public void addPlaceholderButton(XSLFTextShape shape) {
        LayoutButton button = new LayoutButton(shape);
        shapes.put(shape.getShapeId(), button);
        button.setBorderTitle(((SlideEditDialog) parent).getSlide().getSlideElement(shape).getDescription());
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                layoutButtonMouseReleased(evt, button);
            }
        });
        this.add(button);
        button.resize();
    }

    private void layoutButtonMouseReleased(java.awt.event.MouseEvent evt, LayoutButton source) {
        boolean isNewElement = ((SlideElement)((SlideEditDialog) parent).getSlide().getSlideElements().get(source.getShape())).getElement().getAttribute("dataSource").isEmpty();
        JDialog slideElementWizard = new SlideElementEditDialog(this.parent, ((SlideEditDialog) parent).getSlide().getSlideElement(source.getShape()), isNewElement);
        slideElementWizard.pack();
        slideElementWizard.setVisible(true);
    }

    public double getScaleWidthFactor() {
        return (this.getWidth() / (double) this.layoutDimension.width);
    }

    public double getScaleHeightFactor() {
        return (this.getHeight() / (double) this.layoutDimension.height);
    }

    public void shake() {
        shapes.clear();
        this.removeAll();
    }

    public void resize() {
        Iterator it = shapes.entrySet().iterator();
        while (it.hasNext()) {
                LayoutButton item = (LayoutButton) ((Entry) it.next()).getValue();
                item.resize();
        }
        //shapes.values().forEach(button -> button.resize());
    }
    
    public Collection<LayoutButton> getLayoutButtons(){
        return this.shapes.values();
    }
}
