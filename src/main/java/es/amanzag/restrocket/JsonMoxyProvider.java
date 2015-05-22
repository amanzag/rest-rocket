package es.amanzag.restrocket;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.moxy.json.MoxyJsonConfig;

@Provider
public class JsonMoxyProvider implements ContextResolver<MoxyJsonConfig>{
	private final MoxyJsonConfig config;
	
	public JsonMoxyProvider() {
		config = new MoxyJsonConfig();
	}

	@Override
	public MoxyJsonConfig getContext(Class<?> arg0) {
		return config;
	}
}
