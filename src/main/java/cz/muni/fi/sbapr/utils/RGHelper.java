/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.utils;

import cz.muni.fi.sbapr.DataSources.DataSource;
import cz.muni.fi.sbapr.Slide;
import cz.muni.fi.sbapr.gui.DataSourcePanels.DataSourcePanel;
import cz.muni.fi.sbapr.gui.DataSourcePanels.DefaultDataSourcePanel;
import java.awt.Dialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
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
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

    private Map<String, Class> dataSources = new HashMap<>();
    private Map<String, Class> dataSourcePanels = new HashMap<>();
    private Map<String, XSLFSlideLayout> layouts;
    private XMLSlideShow template;
    private File pptxEntry;
    private File xmlEntry;

    public void parse(ZipFile zipFile) throws IOException {
        try {
            pptxEntry = File.createTempFile("pptx", "tmp");
            xmlEntry = File.createTempFile("xml", "tmp");
            List fileHeaders = zipFile.getFileHeaders();
            for (int i = 0; i < fileHeaders.size(); i++) {
                FileHeader fileHeader = (FileHeader) fileHeaders.get(i);
                if (fileHeader.isEncrypted()) {
                    fileHeader.setPassword("hodor".toCharArray());
                }
                if (fileHeader.getFileName().toLowerCase().endsWith(".xml")) {
                    FileUtils.copyInputStreamToFile(zipFile.getInputStream(fileHeader), xmlEntry);
                    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlEntry);
                } else if (fileHeader.getFileName().toLowerCase().endsWith(".pptx") || fileHeader.getFileName().toLowerCase().endsWith(".ppt")) {
                    FileUtils.copyInputStreamToFile(zipFile.getInputStream(fileHeader), pptxEntry);
                    template = new XMLSlideShow(new FileInputStream(pptxEntry));
                }
            }
            xPath = XPathFactory.newInstance().newXPath();
            doc.getDocumentElement().normalize();
            parseLayouts(template);
            parseDataSources();
            initialized = true;
        } catch (ZipException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(RGHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void parse(File pptxFile) throws IOException {
        try {
            pptxEntry = File.createTempFile("pptx", "tmp");
            xmlEntry = File.createTempFile("xml", "tmp");
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = doc.createElement("report");
            doc.appendChild(rootElement);
            Element slides = doc.createElement("slides");
            rootElement.appendChild(slides);

            template = new XMLSlideShow(new FileInputStream(pptxFile));
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
        IterableNodeList DSList = RGHelper.INSTANCE.getNodeListByName("dataSource");
        DSList.stream().filter(node -> node.getNodeType() == Node.ELEMENT_NODE).forEach(node -> {
            Element element = (Element) node;
            String id = element.getAttribute("id");
            String fullClassName = element.getAttribute("type");
            String fullPanelClassName = element.getAttribute("panel");
            try {
                this.dataSources.put(id, Class.forName(fullClassName));
                this.dataSourcePanels.put(id, Class.forName(fullPanelClassName));

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(RGHelper.class
                        .getName()).log(Level.SEVERE, null, ex);
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

    public File getXMLFile() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(this.xmlEntry);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(RGHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(RGHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return xmlEntry;
    }
    
    public File getPPTXFile() {
        return pptxEntry;
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
            Logger.getLogger(RGHelper.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return new IterableNodeList(nodeList);
    }

    public DataSource getNewDataSourceInstance(String className, Element element) {
        try {
            return (DataSource) (dataSources.get(className).getConstructor(Element.class
            ).newInstance(element));

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RGHelper.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public DataSourcePanel getNewDataSourcePanelInstance(String className, Dialog cont) {
        try {
            return (DataSourcePanel) (dataSourcePanels.get(className).getConstructor(Dialog.class).newInstance(cont));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RGHelper.class.getName()).log(Level.SEVERE, null, ex);
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

    private static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

}
