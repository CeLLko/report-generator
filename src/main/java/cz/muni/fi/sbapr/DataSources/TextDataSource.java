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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class TextDataSource extends DataSource<String> {

    private final List<String> lines = new ArrayList<>();

    /**
     *
     * @param element
     */
    public TextDataSource(Element element) {
        super(element);
        //this.lines = Arrays.asList(Arrays.copyOf(args, args.length, String[].class));
        new IterableNodeList((element).getChildNodes())
                .stream()
                .filter(attribute -> attribute instanceof DeferredElementImpl)
                .forEach(attr -> lines.add(attr.getTextContent()));
    }

    /**
     *
     * @return
     */
    @Override
    public String getData() {
        String text = lines.stream().collect(Collectors.joining("/n"));
        Pattern p = Pattern.compile("%%%(.*?)%%%");
        Matcher m = p.matcher(text);
        int i = 0;
        while (m.find()) {
            String dt = m.group();
            String[] split = dt.substring(3, m.group().length() - 3).split("\\|");
            String replacement = DateTimeDataSource.getData(split[0], split[1], split[2]);
            text = text.replace(dt, replacement);
        }
        return text;
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
            System.err.println("Problem occured while updating shape " + shape.getShapeName() + " with " + getClass().getName());
        }
        return shape;
    }

}
