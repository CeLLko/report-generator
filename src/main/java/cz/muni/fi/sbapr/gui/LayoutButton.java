package cz.muni.fi.sbapr.gui;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 *
 * @author Adam
 */
public class LayoutButton extends JPanel {

    private final XSLFTextShape shape;
    private final TitledBorder border;

    /**
     *
     * @param shape
     */
    public LayoutButton(XSLFTextShape shape) {
        this.shape = shape;
        border = javax.swing.BorderFactory.createTitledBorder("");
        super.setBorder(border);
        super.setBackground(new java.awt.Color(250, 250, 250));
    }

    /**
     *
     */
    public void resize() {
        double x = shape.getAnchor().getX() * ((EtchASketch) getParent()).getScaleWidthFactor();
        double y = shape.getAnchor().getY() * ((EtchASketch) getParent()).getScaleHeightFactor();
        double width = shape.getAnchor().getWidth() * ((EtchASketch) getParent()).getScaleWidthFactor();
        double height = shape.getAnchor().getHeight() * ((EtchASketch) getParent()).getScaleHeightFactor();
        super.setBounds((int) x, (int) y, (int) width, (int) height);
    }

    /**
     *
     * @param title
     */
    public void setBorderTitle(String title){
        this.border.setTitle(title);
    }
    
    /**
     *
     * @return
     */
    public XSLFTextShape getShape(){
        return this.shape;
    }
}