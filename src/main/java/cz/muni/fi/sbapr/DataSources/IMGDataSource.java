/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import cz.muni.fi.sbapr.Presentation;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.w3c.dom.Element;

public class IMGDataSource extends DataSource<BufferedImage> {

    private URL url;
    private Optional<Integer> width;
    private Optional<Integer> height;

    public IMGDataSource(Element element) {
        super(element);
        try {
            url = new URL(getAttribute("url"));
            width = Optional.of(Integer.parseInt(getAttribute("width")));
            height = Optional.of(Integer.parseInt(getAttribute("height")));
        } catch (MalformedURLException ex) {
            Logger.getLogger(IMGDataSource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {
            width = Optional.empty();
            height = Optional.empty();
        }
    }

    @Override
    public BufferedImage getData() {
        try {
            BufferedImage img = ImageIO.read(url);
            if (width.isPresent() && height.isPresent()) {
                return img;
            } else {
                Image tmp = img.getScaledInstance(width.get(), height.get(), Image.SCALE_SMOOTH);
                BufferedImage dimg = new BufferedImage(width.get(), height.get(), BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = dimg.createGraphics();
                g2d.drawImage(tmp, 0, 0, null);
                g2d.dispose();

                return dimg;
            }
        } catch (IOException ex) {
            Logger.getLogger(IMGDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XSLFShape updateShape(XSLFSlide slide, XSLFShape shape) {
        XSLFPictureShape picture = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(this.getData(), "png", baos);
            byte[] pictureData = baos.toByteArray();
            
            XSLFPictureData idx = Presentation.INSTANCE.getPPTX().addPicture(pictureData, XSLFPictureData.PictureType.PNG);
            picture = slide.createPicture(idx);

            Rectangle2D anchor = shape.getAnchor();
            slide.removeShape(shape);
            picture.setAnchor(anchor);
        } catch (IOException ex) {
            Logger.getLogger(IMGDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shape;
    }
}
