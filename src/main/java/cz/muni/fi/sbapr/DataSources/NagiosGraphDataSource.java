/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import cz.muni.fi.sbapr.Presentation;
import cz.muni.fi.sbapr.utils.IterableNodeList;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class NagiosGraphDataSource extends DataSource<byte[]> {

    private String url;
    private String user, pass;
    private final HttpClientBuilder httpClientBuilder;

    /**
     *
     * @param element
     */
    public NagiosGraphDataSource(Element element) {
        super(element);
        List<String> ignoredNodes = Arrays.asList(new String[]{"url", "width", "height"});
        String url = getAttribute("url");
        String width = getAttribute("width");
        String height = getAttribute("height");
        this.url = url + "?imageformat=png";
        if(width != null && height != null){
            this.url = this.url.concat("&geom=" + width + "x" + height);
        }
        IterableNodeList nodes = new IterableNodeList(element.getChildNodes());
        nodes.forEach(node -> {
            if (!ignoredNodes.contains(node.getNodeName())) {
                this.url = this.url.concat("&" + node.getNodeName() + "=" + getAttribute(node.getNodeName()));
            }
        });
        this.user = getAttribute("user");
        this.pass = getAttribute("pass");

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(this.user, this.pass));
        httpClientBuilder = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider);
    }

    /**
     *
     * @return
     */
    @Override
    public byte[] getData() {
        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            HttpGet httpget = new HttpGet(this.url);
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                System.out.println(response.getStatusLine());
                return EntityUtils.toByteArray(response.getEntity());
            }
        } catch (IOException ex) {
            Logger.getLogger(NagiosGraphDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param shape
     * @return
     */
    @Override
    public XSLFShape updateShape(XSLFShape shape) {
        try {
            XSLFPictureShape picture = null;
            XSLFPictureData idx = Presentation.INSTANCE.getPPTX().addPicture(getData(), XSLFPictureData.PictureType.PNG);
            picture = shape.getSheet().createPicture(idx);

            Rectangle2D anchor = shape.getAnchor();
            shape.getSheet().removeShape(shape);
            picture.setAnchor(anchor);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
            System.err.println("Problem occured while updating shape "+shape.getShapeName()+" with "+getClass().getName());
        }
        return shape;
    }

}
