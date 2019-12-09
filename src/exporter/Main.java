package exporter;

import exporter.exceptions.WrongInputException;
import exporter.exportClass.XMLExporter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import static exporter.constants.ProgramConstants.*;
import static exporter.validator.XML_Validator.checkSchemeXML;

public class Main {
    private static final String dbUrl="jdbc:mysql://localhost:8081/test" + "?verifyServerCertificate=false"+
            "&useSSL=false"+
            "&requireSSL=false"+
            "&useLegacyDatetimeCode=false"+
            "&amp"+
            "&serverTimezone=UTC";;
    private static final String uName = "root";
    private static final String pass = "admin";
    private static final String fullPath = "E:/JavaLabs_Epam/src/telephoneStation/resources/stationExp.xml";
    private static final String xsdPath = "./src/exporter/resources/stationXSD.xsd";
    private static Scanner in = new Scanner(System.in);
    private static XMLExporter xmlEx;
    private static boolean isNotEnd = true;
    private static boolean finalMessIsNeeded = false;
    private static final Logger logger = Logger.getLogger(Main.class);
    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch(ClassNotFoundException ex) {
            logger.fatal("Driver not found: " + ex.getMessage());
        }
    };

    public static void main(String[] args) {
        PropertyConfigurator.configure("./src/log4j.properties");
        try {
            xmlEx = new XMLExporter(dbUrl, uName, pass);
            System.out.println("Connection success");
        }catch (SQLException sqlEx){
            isNotEnd = false;
            logger.error(sqlEx.getMessage());
            finalMessIsNeeded = true;
        }

        while (isNotEnd) {
            printMenu();
            try {
                int choice = Controller.chooseFromMenu(MENU_MIN, MAIN_MENU_MAX, in);
                switch (choice) {
                    case 1:
                        boolean checkFile = checkSchemeXML(fullPath, xsdPath);
                        if(checkFile)
                            xmlEx.exportXML(fullPath);
                        else
                            System.out.println("Your XML is not valid");
                        break;
                    case 2:
                        isNotEnd = false;
                        break;
                    default:
                        System.out.println(MAIN_MENU_DEFAULT_MESS);
                }
            } catch (SQLException | ParserConfigurationException | IOException | SAXException ex) {
                logger.error(ex.getMessage());
            } catch (WrongInputException e) {
                logger.info(e.getMessage());
                in.next();
            }

        }

        if (finalMessIsNeeded)
            System.out.println("Press any button to leave");
    }

    private static void printMenu() {
        System.out.println(MENU);
    }

}
