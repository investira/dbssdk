package br.com.dbsoft.json;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParametersSerializer {

	private static final ObjectMapper MAPPER;
	static {
		MAPPER = new ObjectMapper();
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		MAPPER.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
		MAPPER.setDateFormat(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss"));
	}
	
	public static ObjectMapper get() {
		return MAPPER;
	}
	
	private ParametersSerializer() {}
}
