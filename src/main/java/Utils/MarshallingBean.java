package Utils;

import Entity.GroupEntity;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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

    public ListWrapper readFromXML() {
        ListWrapper listWrapper;
        try {
            JAXBContext context = JAXBContext.newInstance(ListWrapper.class);
            File file = new File("C:\\file.xml");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            listWrapper = (ListWrapper) unmarshaller.unmarshal(file);
        } catch (Exception e) {
            return null;
        }
        return listWrapper;
    }
}
