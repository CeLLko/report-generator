/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.gui;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 *
 * @author Adam
 */
public class EtchASketch extends JPanel {

    private Map<Integer, LayoutButton> buttons = new HashMap<>();
    ;
    private final Dimension layoutDimension;

    public EtchASketch(Dimension dimension) {
        this.layoutDimension = dimension;
    }

    private class LayoutButton extends JButton {

        private int placeholderId;

        public LayoutButton(int placeholderId, int width, int height, double x, double y) {
            this.placeholderId = placeholderId;
        }
    }

    public void addPlaceholderButton(XSLFTextShape shape) {
        int scaledWidth = (int) (shape.getAnchor().getWidth() / (this.layoutDimension.width / this.getWidth()));
        int scaledHeight = (int) (shape.getAnchor().getHeight() / (this.layoutDimension.height / this.getHeight()));
        double scaledX = shape.getAnchor().getX() / (this.layoutDimension.width / this.getWidth());
        double scaledY = shape.getAnchor().getY() / (this.layoutDimension.height / this.getHeight());
        LayoutButton button = new LayoutButton(shape.getShapeId(), scaledWidth, scaledHeight, scaledX, scaledY);
        buttons.put(shape.getShapeId(), button);
        this.add(button);
        this.repaint();
    }

    public void shake() {

    }

}
