/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

import cz.muni.fi.sbapr.utils.RGHelper;
import cz.muni.fi.sbapr.utils.IterableNodeList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.w3c.dom.Attr;
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
    private Element element;
    private Map<Integer, SlideElement> slideElements = new HashMap() {

        @Override
        public Object get(Object key) {
            if (containsKey(((XSLFShape) key).getShapeId())) {
                return super.get(((XSLFShape) key).getShapeId());
            } else {
                return create(key);
            }
        }

        private Object create(Object shape) {
            Element newElement = RGHelper.INSTANCE.getDoc().createElement("element");
            element.appendChild(newElement);
            Attr attr = RGHelper.INSTANCE.getDoc().createAttribute("placeholderID");
            attr.setValue("" + ((XSLFShape) shape).getShapeId());
            newElement.setAttributeNode(attr);
            SlideElement newSlideElement = new SlideElement((XSLFShape) shape, newElement);
            slideElements.put(((XSLFShape) shape).getShapeId(), newSlideElement);
            return newSlideElement;
        }

        @Override
        public Object remove(Object element) {
            Object res = super.remove(element);
            if (res != null) {
                ((SlideElement) res).getElement().getParentNode().removeChild(((SlideElement) res).getElement());
            }
            return res;
        }
        
        @Override
        public void clear(){
            keySet().forEach(key -> remove(key));
        }
    };

    public Slide() {
        this.element = RGHelper.INSTANCE.getDoc().createElement("slide");
        IterableNodeList list = (RGHelper.INSTANCE.getNodeListByName("slides"));
        (list.stream().findAny().get()).appendChild(element);
    }

    public Slide(Element element) {
        this.element = element;
        XSLFSlideLayout layout = RGHelper.INSTANCE.getLayout(element.getAttribute("layout"));
        slide = Presentation.INSTANCE.getPPTX().createSlide(layout);
        new IterableNodeList(element.getChildNodes())
                .stream()
                .filter((node) -> (node.getNodeType() == Node.ELEMENT_NODE))
                .map(node -> (Element) node)
                .forEachOrdered((slideElement) -> {
                    int shapeId = Integer.parseInt(slideElement.getAttribute("placeholderID"));
                    XSLFShape shape = slide.getShapes().stream().filter(s -> s.getShapeId() == shapeId).findAny().get();
                    slideElements.put(shape.getShapeId(), new SlideElement(shape, slideElement));
                });
    }

    @Override
    public XSLFSlide call() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(slideElements.size());
        pool.invokeAll(slideElements.values());
        return slide;
    }

    public Element getElement() {
        return element;
    }

    public SlideElement getSlideElement(XSLFShape shape) {
        return slideElements.get(shape);
    }
    
    public Map getSlideElements() {
        return slideElements;
    }

    public String getDescription() {
        return element.getAttribute("description");
    }
    
    @Override
    public Slide clone() throws CloneNotSupportedException{
        Slide clone = (Slide) super.clone();
        clone.element = (Element) element.cloneNode(true);
        return clone;
    }
}
