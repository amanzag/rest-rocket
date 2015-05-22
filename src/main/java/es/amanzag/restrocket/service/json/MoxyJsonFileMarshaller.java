package es.amanzag.restrocket.service.json;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;

import es.amanzag.restrocket.dto.Direction;
import es.amanzag.restrocket.dto.Position;
import es.amanzag.restrocket.dto.Target;
import es.amanzag.restrocket.dto.TargetList;

public class MoxyJsonFileMarshaller {
    private final JAXBContext jc;

    private final String jsonFilePath;
    
    public MoxyJsonFileMarshaller(final String jsonFilePath) throws JAXBException {
        this.jsonFilePath = jsonFilePath;
        jc =
                JAXBContextFactory.createContext(
                        new Class[] { TargetList.class, Direction.class, Position.class, Target.class }, null);
    }
    
    public TargetList unmarshallFromFile() throws JAXBException {
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
        StreamSource json = new StreamSource(jsonFilePath);

        return unmarshaller.unmarshal(json, TargetList.class).getValue();
    }

    public void marshallToFile(TargetList targetList) throws JAXBException {
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

        marshaller.marshal(targetList, new File(jsonFilePath));
    }
}
