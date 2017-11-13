/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import org.apache.poi.xslf.usermodel.XSLFShape;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class PostgreSQLDataSource extends DataSource<String> {

    private final String db;
    private final String user;
    private final String pass;
    private final String statement;
    
    public PostgreSQLDataSource(Element element) {
        super(element);
        /*this.db = "jdbc:postgresql://obiwan.gdovin.eu:5432/test1";
        this.user = "postgres";
        this.pass = "123456";*/
        this.statement = "SELECT currency FROM \"public\".\"MOCK_DATA\" WHERE id=1";
        
        this.db = "jdbc:postgresql://"+getAttribute("host")+":"+getAttribute("port")+"/"+getAttribute("db");
        this.user = getAttribute("user");
        this.pass = getAttribute("pass");
        //this.statement = getAttribute("statement");
    }

    @Override
    public String getData() {
        String result = null;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(db, user, pass);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(statement);
            while (rs.next()) {
                result = rs.getString(1);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return result;
    }

    @Override
    public XSLFShape updateShape(XSLFShape shape) {
        ((XSLFTextShape) shape).clearText();
        ((XSLFTextShape) shape).appendText(getData(), false);
        return shape;
    }

}
