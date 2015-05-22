package es.amanzag.restrocket.service.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.internal.oxm.record.json.JSONReader;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

import es.amanzag.restrocket.dto.Direction;
import es.amanzag.restrocket.dto.Position;
import es.amanzag.restrocket.dto.Target;
import es.amanzag.restrocket.dto.TargetList;
import es.amanzag.restrocket.service.RocketPersistenceService;
import es.amanzag.restrocket.service.exception.RocketException;

public class JsonRocketPersistenceService implements RocketPersistenceService {
    private static final String PROPERTIES_PATH = "persistence.properties";

    private String JSON_FILE_PATH_KEY = "persistence.file.path";

    private Properties properties;

    private MoxyJsonFileMarshaller marshaller;

    public JsonRocketPersistenceService() throws JAXBException, IOException {
        marshaller = new MoxyJsonFileMarshaller(getJsonFilePath());
    }
	
	@Override
    public Target getTarget(String targetId) throws RocketException {
	    List<Target> targets = getTargets().getTargets();
	    Optional<Target> value = targets
	            .stream()
	            .filter(target -> target.getTargetId().equals(targetId))
	            .findFirst();
	    
		return value.get();
	}
	
	@Override
	public synchronized void persistTarget(Target target) {
		
	}
	
	@Override
    public TargetList getTargets() throws RocketException {
        try {
            return marshaller.unmarshallFromFile();
        } catch (Exception e) {
            throw new RocketException(e);
        }
	}
	
	@Override
	public void deleteTarget(String targetId) {

	}
	
    private String getJsonFilePath() throws IOException {
        return getPersistenceConfiguration().getProperty(JSON_FILE_PATH_KEY);
    }

    private Properties getPersistenceConfiguration() throws IOException {
		if (properties == null) {
            properties = loadConfiguration();
		}
		return properties;
	}
	
	private synchronized Properties loadConfiguration() throws IOException {
		Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_PATH));

		return properties;
	}

    public static void main(String[] args) throws JAXBException, IOException, RocketException {
        JsonRocketPersistenceService s = new JsonRocketPersistenceService();
        s.getTarget("juana");
    }
}
