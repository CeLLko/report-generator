/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.utils;

import cz.muni.fi.sbapr.DataSources.DataSource;
import cz.muni.fi.sbapr.Slide;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Adam
 */
public enum RGHelper {
    INSTANCE;

    private boolean initialized = false;

    private XPath xPath;
    private Document doc;

    private Map<String, Class> dataSources;
    private Map<String, XSLFSlideLayout> layouts;

    public void parse(ZipFile zipFile) throws IOException {
        ZipEntry xmlEntry = zipFile.getEntry("report-template.xml");
        if (xmlEntry == null) {
            System.err.print("report-template.xml not found, ");
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                xmlEntry = entries.nextElement();
                if (xmlEntry.getName().endsWith(".xml")) {
                    System.err.println("using " + xmlEntry.getName() + " instead.");
                    break;
                }
            }
            throw new IOException("no XML file found");
        }

        ZipEntry pptxEntry = zipFile.getEntry("infrastructure-report-template.pptx");
        if (pptxEntry != null) {
            String crunchifyValue = DigestUtils.md5Hex(IOUtils.toByteArray(zipFile.getInputStream(pptxEntry)));
            System.err.println(crunchifyValue);
        } else {
            System.err.print("infrastructure-report-template.pptx not found, ");
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                pptxEntry = entries.nextElement();
                if (pptxEntry.getName().endsWith(".pptx") || pptxEntry.getName().endsWith(".ppt")) {
                    System.err.println("using " + pptxEntry.getName() + " instead.");
                    break;
                }
            }
            throw new IOException("no PPTX file found");
        }
        try {
            xPath = XPathFactory.newInstance().newXPath();
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(zipFile.getInputStream(xmlEntry));
            doc.getDocumentElement().normalize();

            XMLSlideShow template = new XMLSlideShow(zipFile.getInputStream(pptxEntry));
            parseLayouts(template);
            parseDataSources();
            initialized = true;
        } catch (SAXException | ParserConfigurationException ex) {
            Logger.getLogger(RGHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void parse(File pptxFile) throws IOException {
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = doc.createElement("report");
            doc.appendChild(rootElement);

            XMLSlideShow template = new XMLSlideShow(new FileInputStream(pptxFile));
            parseLayouts(template);
            initialized = true;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(RGHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Slide> parseSlides() {
        List<Slide> slides = new ArrayList<>();
        IterableNodeList slideList = RGHelper.INSTANCE.getNodeList("/report/slides/slide");
        slideList.forEach(node -> {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                slides.add(new Slide((Element) node));
            }
        });
        return slides;
    }

    private void parseDataSources() {
        Map<String, Class> dataSources = new HashMap<>();
        IterableNodeList DSList = RGHelper.INSTANCE.getNodeList("/report/dataSources/dataSource");
        DSList.stream().filter(node -> node.getNodeType() == Node.ELEMENT_NODE).forEach(node -> {
            Element element = (Element) node;
            String id = element.getAttribute("id");
            String fullClassName = element.getAttribute("type");
            try {
                dataSources.put(id, Class.forName(fullClassName));

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RGHelper.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.dataSources = dataSources;
    }

    private void parseLayouts(XMLSlideShow template) {
        Map<String, XSLFSlideLayout> layouts = new HashMap<>();
        template.getSlideMasters().forEach((master) -> {
            for (XSLFSlideLayout layout : master.getSlideLayouts()) {
                layouts.put(layout.getName(), layout);
            }
        });
        this.layouts = layouts;
    }

    public String[] getLayoutNames() {
        Set<String> keys = layouts.keySet();
        return keys.toArray(new String[keys.size()]);
    }
    
    public Map<String, XSLFSlideLayout> getLayouts(){
        return layouts;
    }
    
    public Document getDoc() {
        return doc;
    }

    public XSLFSlideLayout getLayout(String name) {
        return layouts.get(name);
    }

    public IterableNodeList getNodeList(String expression) {
        NodeList nodeList = null;
        try {
            nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

        } catch (XPathExpressionException ex) {
            Logger.getLogger(RGHelper.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return new IterableNodeList(nodeList);
    }

    public DataSource
            getNewDataSourceInstance(String className, Element element) {
        try {
            return (DataSource) (dataSources.get(className).getConstructor(Element.class
            ).newInstance(element));

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RGHelper.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Checks if the XMLParser instance has been initialized
     *
     * @return initialization status
     */
    public boolean isInitialized() {
        return initialized;
    }
}
