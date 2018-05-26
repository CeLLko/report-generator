/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.utils;

import cz.muni.fi.sbapr.DataSources.DataSource;
import cz.muni.fi.sbapr.Slide;
import cz.muni.fi.sbapr.exceptions.TemplateParserException;
import cz.muni.fi.sbapr.gui.DataSourcePanels.DataSourcePanel;
import cz.muni.fi.sbapr.gui.DataSourcePanels.DefaultDataSourcePanel;
import java.awt.Dialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public enum RGHelper {

    INSTANCE;

    private boolean initialized = false;

    private XPath xPath;
    private Document doc;

    private Map<String, Class> dataSources = new HashMap<>();
    private Map<String, Class> dataSourcePanels = new HashMap<>();
    private Map<String, XSLFSlideLayout> layouts;
    private XMLSlideShow template;
    private File pptxEntry;
    private File xmlEntry;
    private final ZipParameters zipParams = new ZipParameters() {
        {
            setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
            setEncryptFiles(true);
            setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        }
    };

    public void parse(ZipFile zipFile) throws TemplateParserException {
        try {
            pptxEntry = File.createTempFile("template", "pptx");
            pptxEntry.deleteOnExit();
            xmlEntry = File.createTempFile("report", "xml");
            xmlEntry.deleteOnExit();
            List fileHeaders = zipFile.getFileHeaders();
            for (int i = 0; i < fileHeaders.size(); i++) {
                FileHeader fileHeader = (FileHeader) fileHeaders.get(i);
                if (fileHeader.getFileName().toLowerCase().endsWith("xml")) {
                    try (FileOutputStream out = new FileOutputStream(xmlEntry)) {
                        IOUtils.copy(zipFile.getInputStream(fileHeader), out);
                    }
                    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlEntry);
                } else if (fileHeader.getFileName().toLowerCase().endsWith("pptx") || fileHeader.getFileName().toLowerCase().endsWith("ppt")) {
                    try (FileOutputStream out = new FileOutputStream(pptxEntry)) {
                        IOUtils.copy(zipFile.getInputStream(fileHeader), out);
                    }
                    template = new XMLSlideShow(new FileInputStream(pptxEntry));
                }
            }
            xPath = XPathFactory.newInstance().newXPath();
            doc.getDocumentElement().normalize();
            parseLayouts(template);
            parseDataSources();
            initialized = true;
        } catch (NullPointerException | ZipException | ParserConfigurationException | IOException | SAXException ex) {
            throw new TemplateParserException(ex.getMessage());
        }
    }

    public void parse(File pptxFile) throws TemplateParserException {
        try {
            pptxEntry = File.createTempFile("template", "pptx");
            pptxEntry.deleteOnExit();
            xmlEntry = File.createTempFile("report", "xml");
            xmlEntry.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(pptxEntry)) {
                IOUtils.copy(new FileInputStream(pptxFile), out);
            }
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(getClass().getClassLoader().getResource("report.xml").getFile()));
            xPath = XPathFactory.newInstance().newXPath();
            doc.getDocumentElement().normalize();
            parseDataSources();

            template = new XMLSlideShow(new FileInputStream(pptxFile));
            parseLayouts(template);
            initialized = true;
        } catch (NotOfficeXmlFileException | ParserConfigurationException | IOException | SAXException ex) {
            throw new TemplateParserException(ex.getMessage());
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

    private void parseDataSources() throws TemplateParserException {
        IterableNodeList DSList = RGHelper.INSTANCE.getNodeListByName("dataSource");
        DSList.stream().filter(node -> node.getNodeType() == Node.ELEMENT_NODE).forEach(node -> {
            Element element = (Element) node;
            String id = element.getAttribute("id");
            String fullClassName = element.getAttribute("type");
            String fullPanelClassName = element.getAttribute("panel");
            try {
                this.dataSources.put(id, Class.forName(fullClassName));
            } catch (ClassNotFoundException ex) {
                this.dataSources.remove(id);
                return;
            }
            try {
                this.dataSourcePanels.put(id, Class.forName(fullPanelClassName));
            } catch (ClassNotFoundException ex) {
                this.dataSourcePanels.put(id, null);
            }

        });
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

    public String[] getDataSourceNames() {
        Set<String> keys = dataSources.keySet();
        return keys.toArray(new String[keys.size()]);
    }

    public File getXMLFile() throws TemplateParserException {
        try (FileOutputStream out = new FileOutputStream(xmlEntry)) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            String xmlString = sw.toString();
            out.write(xmlString.getBytes(), 0, xmlString.getBytes().length);

        } catch (IOException | TransformerException ex) {
            throw new TemplateParserException(ex.getMessage());
        }
        return xmlEntry;
    }

    public File getPPTXFile() {
        return pptxEntry;
    }

    public ZipParameters getZipParams() {
        return zipParams;
    }

    public Document getDoc() {
        return doc;
    }

    public Map<String, XSLFSlideLayout> getLayouts() {
        return layouts;
    }

    public XSLFSlideLayout getLayout(String name) {
        return layouts.get(name);
    }

    public XMLSlideShow getTemplate() {
        return template;
    }

    public IterableNodeList getNodeListByName(String name) {
        NodeList nodeList = null;
        nodeList = (NodeList) doc.getElementsByTagName(name);
        return new IterableNodeList(nodeList);
    }

    public IterableNodeList getNodeList(String expression) {
        NodeList nodeList = null;
        try {
            nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

        } catch (XPathExpressionException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return new IterableNodeList(nodeList);
    }

    public DataSource getNewDataSourceInstance(String className, Element element) {
        try {
            return (DataSource) (dataSources.get(className).getConstructor(Element.class)
                    .newInstance(element));

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RGHelper.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public DataSourcePanel getNewDataSourcePanelInstance(String className, Dialog cont) {
        try {
            return (DataSourcePanel) (dataSourcePanels.get(className).getConstructor(Dialog.class).newInstance(cont));
        } catch (Exception ex) {
            return new DefaultDataSourcePanel(cont);
        }
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
