/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.utils.RGHelper;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import cz.muni.fi.sbapr.DataSources.DataSource;
import cz.muni.fi.sbapr.utils.IterableNodeList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Callable slide, holds references to
 * {@link org.apache.poi.xslf.usermodel.XSLFSlide slide} as well as all its
 * elements
 *
 * @author adamg
 */
public class Slide implements Callable<XSLFSlide>, Cloneable {

    private XSLFSlide slide;
    private List<SlideElement> elements = new ArrayList<>();
    protected Element slideElement;

    /**
     * Creates a new slide by parsing an XML {@link org.w3c.dom.Node node}
     *
     * @param slideNode the node to parse
     */
    public Slide() {
        this.slideElement = RGHelper.INSTANCE.getDoc().createElement("slide");
    }

    public Slide(Element slideElement) {
        this.slideElement = slideElement;
    }

    public void parse() {

        XSLFSlideLayout layout = RGHelper.INSTANCE.getLayout(slideElement.getAttribute("layout"));
        slide = Presentation.INSTANCE.getPPTX().createSlide(layout);
        new IterableNodeList(slideElement.getChildNodes())
                .stream()
                .filter((node) -> (node.getNodeType() == Node.ELEMENT_NODE))
                .map(node -> (Element) node)
                .forEachOrdered((element) -> {
                    List<Object> args = new ArrayList<>();
                    //new IterableNodeList(((Element) node).getChildNodes())
                    //    .stream()
                    //    .filter(attribute -> attribute instanceof DeferredElementImpl)
                    //    .forEach(attr -> args.add((Object) attr.getTextContent()));

                    DataSource ds = RGHelper.INSTANCE.getNewDataSourceInstance(element.getAttribute("dataSource"), element);

                    int shapeId = Integer.parseInt(element.getAttribute("placeholderID"));
                    //slide.getShapes().stream().forEach(action -> System.out.println(action.getShapeName()+" " +action.getShapeId()));
                    XSLFTextShape placeholder = (XSLFTextShape) slide.getShapes().stream().filter(shape -> shape.getShapeId() == shapeId).findAny().get();
                    elements.add(new SlideElement(slide, placeholder, ds));
                });
    }

    @Override
    public XSLFSlide call() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(elements.size());
        pool.invokeAll(elements);
        return slide;
    }
    
    @Override
    public Slide clone() throws CloneNotSupportedException {
        return (Slide) super.clone();
    }
}
