package br.com.dbsoft.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParametersSerializer {

	private static final ObjectMapper MAPPER;
	static {
		MAPPER = new ObjectMapper();
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
	}
	
	public static ObjectMapper get() {
		return MAPPER;
	}
	
	private ParametersSerializer() {}
}
