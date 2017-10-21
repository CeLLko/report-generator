/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import cz.muni.fi.sbapr.Presentation;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
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
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class NagiosGraphDataSource extends DataSource<byte[]> {

    private String url;
    private final HttpClientBuilder httpClientBuilder;

    public NagiosGraphDataSource(Element element) {
        super(element);
        String url = getAttribute("url");
        String host = getAttribute("host");
        String service = getAttribute("service");
        String width = getAttribute("width");
        String height = getAttribute("height");
        this.url = url + "?host=" + host + "&service=" + service + "&geom=" + width + "x" + height + "&imageformat=png";

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials("nagiosadmin", "123456"));
        httpClientBuilder = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider);
    }

    @Override
    public byte[] getData() {
        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            HttpGet httpget = new HttpGet(this.url);
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                return EntityUtils.toByteArray(response.getEntity());
            }
        } catch (IOException ex) {
            Logger.getLogger(NagiosGraphDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XSLFShape updateShape(XSLFSlide slide, XSLFShape shape) {
        XSLFPictureShape picture = null;
        XSLFPictureData idx = Presentation.INSTANCE.getPPTX().addPicture(getData(), XSLFPictureData.PictureType.PNG);
        picture = slide.createPicture(idx);

        Rectangle2D anchor = shape.getAnchor();
        slide.removeShape(shape);
        picture.setAnchor(anchor);
        return shape;
    }

}
