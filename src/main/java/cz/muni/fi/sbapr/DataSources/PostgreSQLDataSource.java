/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import org.apache.poi.xslf.usermodel.XSLFShape;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class PostgreSQLDataSource extends DataSource<Object> {

    private final String db;
    private final String user;
    private final String pass;
    private final String statement;
    
    public PostgreSQLDataSource(Element element) {
        super(element);
        this.db = "jdbc:postgresql://obiwan.gdovin.eu:5432/test1";
        this.user = "postgres";
        this.pass = "123456";
        this.statement = "SELECT * FROM \"public\".\"MOCK_DATA\" WHERE id<6";
        
        //this.db = "jdbc:postgresql://"+getAttribute("host")+":"+getAttribute("port")+"/"+getAttribute("db");
        //this.user = getAttribute("user");
        //this.pass = getAttribute("pass");
        //this.statement = getAttribute("statement");
    }

    @Override
    public Object getData() {
        List<List<String>> result = new ArrayList<>();
        List<String> currentList;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(db, user, pass);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(statement);
            ResultSetMetaData metadata = rs.getMetaData();
            int numberOfColumns = metadata.getColumnCount();
            while (rs.next()) {
                currentList = new ArrayList<>();
                result.add(currentList);
                for(int i = 1; i<=numberOfColumns; i++){
                    currentList.add(String.valueOf(rs.getObject(currentList.size()+1)));
                }
            }
            rs.close();
            stmt.close();
            c.close();
            
            if(numberOfColumns == 1 && result.get(0).size()==1){
                return result.get(0).get(0);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return result;
    }

    @Override
    public XSLFShape updateShape(XSLFShape shape) {
        Object data =  getData();
        if(data instanceof String){
            ((XSLFTextShape) shape).clearText();
            ((XSLFTextShape) shape).appendText((String) data, false);
            return shape;
        }else{
            List<List<String>> tableData = (List<List<String>>) data;
            int numberOfRows = tableData.size();
            int numberOfColumns = tableData.get(0).size();
            Rectangle2D anchor = shape.getAnchor();
            double maxColumnWidths[] = new double[numberOfColumns];
            for(int i=0; i<numberOfColumns; i++){
                maxColumnWidths[i] = 0;
            }
            //tbl.setAnchor(new Rectangle(50, 50, 450, 300));
            XSLFTable tbl = (XSLFTable) shape.getSheet().createTable(numberOfRows, numberOfColumns);
           //XSLFTableRow headerRow = tbl.addRow();
            //headerRow.setHeight(50);

            for(int i = 0; i< numberOfRows; i++){
                XSLFTableRow tr = tbl.addRow();
                tr.setHeight(20);
                for(int j = 0; j < numberOfColumns; j++){
                    XSLFTableCell cell = tr.addCell();
                    XSLFTextRun r = cell.addNewTextParagraph().addNewTextRun();                 
                    String text = tableData.get(i).get(j);
                    r.setText(text);
                    r.setFontSize(10d);
                    
                    maxColumnWidths[j] = text.length() > maxColumnWidths[j] ? text.length() : maxColumnWidths[j];
                    
                    cell.setBorderColor(TableCell.BorderEdge.bottom, Color.gray);
                    cell.setBorderColor(TableCell.BorderEdge.left, Color.gray);
                    cell.setBorderColor(TableCell.BorderEdge.right, Color.gray);
                    cell.setBorderColor(TableCell.BorderEdge.top, Color.gray);
                    if (i % 2 == 0){
                        cell.setFillColor(new Color(221, 235, 247));
                    }
                    else{
                        cell.setFillColor(new Color(255, 255, 255));
                    }

                }
            }
            double sum = DoubleStream.of(maxColumnWidths).sum();
            for(int i = 0; i<maxColumnWidths.length; i++){
                maxColumnWidths[i]/=sum;
            }
            double width = anchor.getMaxX()-anchor.getMinX();
            for(int i = 0; i< numberOfColumns; i++){
                tbl.setColumnWidth(i, maxColumnWidths[i]*width);
            }
            shape.getSheet().removeShape(shape);
            tbl.setAnchor(anchor);
            
            return tbl;
        }
    }
}
