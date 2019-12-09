package exporter.exportClass;

import com.mysql.cj.result.SqlDateValueFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class XMLExporter {
    private Connection con;
    public XMLExporter(String dbUrl, String uName, String pass) throws SQLException {
        this.con = DriverManager.getConnection(dbUrl, uName, pass);
    }

    public void exportXML(String fullPath) throws SQLException, ParserConfigurationException, IOException, SAXException {
        try{
            con.createStatement()
                    .execute("DROP TABLE station");
        }catch (SQLException ex) {;}
        con.createStatement()
                .execute("CREATE TABLE station(\n" +
                        " id integer primary key auto_increment,\n" +
                        " firstName varchar(25) not null unique,\n" +
                        " patronymic varchar(25) not null,\n" +
                        " lastName varchar(25) not null,\n" +
                        " mobileOperator varchar(25) not null,\n" +
                        " countryCode varchar(10) not null,\n" +
                        " number varchar(25) not null,\n" +
                        " fullPhoneNumber varchar(25) not null,\n" +
                        " country varchar(25) not null,\n" +
                        " city varchar(25) not null,\n" +
                        " homeNumber varchar(10) not null,\n" +
                        " street varchar(25) not null,\n" +
                        " flatNumber varchar(10) not null,\n" +
                        " isFlat varchar(10) not null\n" +
                        ")");


        File file = new File(fullPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDoc = builder.parse(file);

        PreparedStatement stmt = con
                .prepareStatement("INSERT INTO station(\n" +
                        " firstName, patronymic, lastName, mobileOperator, countryCode, number,\n" +
                        " fullPhoneNumber, country, city, homeNumber, street, flatNumber, isFlat)\n" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        Node root = xmlDoc.getFirstChild();
        NodeList nlist = root.getChildNodes();
        for (int i = 0 ; i < nlist.getLength() ; i++) {
            Node child = nlist.item(i);//sub
            ArrayList<String> columns = new ArrayList<String>();
            NodeList nlist2 = child.getChildNodes();
            for(int j = 0; j < nlist2.getLength(); j++)
            {
                Node child2 = nlist2.item(j);
                if(child2.getNodeName().equals("phoneNumber") || child2.getNodeName().equals("address")) {
                    NodeList nList3 = child2.getChildNodes();
                    for (int m = 0; m < nList3.getLength(); m++){
                        Node child3 = nList3.item(m);
                        columns.add(child3.getTextContent());
                    }
                }
                else
                    columns.add(child2.getTextContent());
            }
            for (int n = 0 ; n < columns.size() ; n++) {
                stmt.setString(n+1, columns.get(n));
            }
            stmt.execute();
        }
    }
}