package Utils;

import Entity.GroupEntity;
import org.xml.sax.SAXException;

import javax.ejb.Stateless;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.util.List;

@Stateless
public class MarshallingBean {
    public void saveToXML(List<GroupEntity> groups) {
        try {
            ListWrapper listWrapper = new ListWrapper(groups);
            JAXBContext context = JAXBContext.newInstance(ListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File file = new File("C:\\file.xml");
            marshaller.marshal(listWrapper, file);
        } catch (JAXBException exception) {
            exception.printStackTrace();
        }
    }

    public ListWrapper readFromXML() throws JAXBException, IOException, SAXException {
        ListWrapper listWrapper;
        JAXBContext context = JAXBContext.newInstance(ListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        File file = new File("C:\\file.xml");
        File xsd = new File("C:\\xsd.xsd");

        isCorrect(file, xsd);
        listWrapper = (ListWrapper) unmarshaller.unmarshal(file);
        return listWrapper;
    }

    public static void isCorrect(File xml, File xsd) throws SAXException, IOException {
        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(xsd)
                .newValidator()
                .validate(new StreamSource(xml));
    }
}
