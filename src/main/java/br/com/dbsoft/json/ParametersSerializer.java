package br.com.dbsoft.json;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import br.com.dbsoft.util.DBSString;

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

	public static class DBSPropertyNamingStrategy extends PropertyNamingStrategy {

		private static final long serialVersionUID = 2803688738889527441L;

	    @Override
	    public String nameForGetterMethod(MapperConfig<?> pConfig, AnnotatedMethod pMethod, String pDefaultName) {
	        return convert(pMethod, pMethod.getName().toString());
	    }

	    @Override
	    public String nameForSetterMethod(MapperConfig<?> pConfig, AnnotatedMethod pMethod, String pDefaultName) {
	        return convert(pMethod, pMethod.getName().toString());
	    }

	    private String convert(AnnotatedMethod pMethod, String pDefaultName) {
	        Class<?> xClazz = pMethod.getDeclaringClass();
	        Field[] xFields = xClazz.getDeclaredFields();
	        for (Field xFld : xFields) {
	        	String xMethodName = pDefaultName;
	        	if (xMethodName.startsWith("get") || xMethodName.startsWith("set")) {
	        		xMethodName = DBSString.getSubString(xMethodName, 4, xMethodName.length());
	        	} else if (xMethodName.startsWith("is")) {
	        		xMethodName = DBSString.getSubString(xMethodName, 3, xMethodName.length());
	        	}
	            if (xFld.getName().equalsIgnoreCase(xMethodName)) {
	                return xFld.getName();
	            }
	        }
	        return pDefaultName;
	    }
	}
}
