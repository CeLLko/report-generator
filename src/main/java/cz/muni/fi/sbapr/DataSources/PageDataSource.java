/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import cz.muni.fi.sbapr.Presentation;
import cz.muni.fi.sbapr.Slide;
import cz.muni.fi.sbapr.SlideElement;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class PageDataSource extends DataSource<String> {

    /**
     *
     * @param element
     */
    public PageDataSource(Element element) {
        super(element);
    }

    /**
     *
     * @return
     */
    @Override
    public String getData() {
        String pageNumber = null;
        List<Slide> slides = Presentation.INSTANCE.getSlides();
        List<SlideElement> slideElements = new ArrayList<>();
        slides.stream().forEach(slide -> slide.getSlideElements()
                .values().stream().forEach(e -> slideElements.add((SlideElement) e))
        );
        Slide thisSlide = slides.stream()
                .filter(slide -> slide.getSlideElements().containsValue(slideElements
                        .stream()
                        .filter(s -> s.getElement().equals(element))
                        .findAny()
                        .get()))
                .findAny()
                .get();
        return Presentation.INSTANCE.getSlides().indexOf(thisSlide)+1+"";
    }

    /**
     *
     * @param shape
     * @return
     */
    @Override
    public XSLFShape updateShape(XSLFShape shape) {
        try {
            ((XSLFTextShape) shape).setText(getData());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.err.println("Problem occured while updating shape " + shape.getShapeName() + " with " + getClass().getName());
        }
        return shape;
    }

}
