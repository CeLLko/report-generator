/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import cz.muni.fi.sbapr.utils.IterableNodeList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class TextDataSource extends DataSource<String>{

    private final List<String> lines = new ArrayList<>();

    public TextDataSource(Element element) {
        super(element);
        //this.lines = Arrays.asList(Arrays.copyOf(args, args.length, String[].class));
        new IterableNodeList((element).getChildNodes())
            .stream()
            .filter(attribute -> attribute instanceof DeferredElementImpl)
            .forEach(attr -> lines.add(attr.getTextContent()));
    }
    
    
    @Override
    public String getData() {
        return lines.stream().collect(Collectors.joining("/n"));
    }

    @Override
    public XSLFShape updateShape(XSLFShape shape) {
        ((XSLFTextShape) shape).clearText();
        lines.stream().forEach(line -> ((XSLFTextShape) shape).appendText(line, true));
        return shape;
    }
    
}
