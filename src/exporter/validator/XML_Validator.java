package exporter.validator;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class XML_Validator {
    public static boolean checkSchemeXML(String xml, String xsdPath){
        try {
            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(new File(xsdPath))
                    .newValidator()
                    .validate(new StreamSource(new File(xml)));

        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
