package br.com.dbsoft.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.TypeFactory;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSObject;

public class DBSJson {

//	private static Logger wLogger = Logger.getLogger(DBSJSON.class);

	/**
	 * Retorna String JSon a partir de objeto informado
	 * @param pObject
	 * @return
	 * @throws JsonProcessingException 
	 * @throws DBSIOException
	 */
	public static <T> String toJson(T pObject){
		if (DBSObject.isNull(pObject)) {return null;}
		ObjectMapper xJSON = pvCreateJson();
		xJSON.setPropertyNamingStrategy(null);
		try {
			return xJSON.writeValueAsString(pObject);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Retorna classe do tipo informado a partir de String JSon  
	 * @param pObject
	 * @param pClass
	 * @return
	 * @throws DBSIOException
	 */
	public static <T> T fromJson(Object pObject, Class<T> pClass){
		return fromJson(pObject, pClass, null);
	}
	
	/**
	 * Retorna classe do tipo informado a partir de String JSon segundo uma política de nomes para campos
	 * @param pObject
	 * @param pClass
	 * @param pPropertyNamingStrategy
	 * @return
	 */
	public static <T> T fromJson(Object pObject, Class<T> pClass, PropertyNamingStrategy pPropertyNamingStrategy){
		if (DBSObject.isEmpty(pObject)) {return null;}
		T 				xFromJson = null;
		ObjectMapper 	xJSON = pvCreateJson();
		String 			xS = pObject.toString();
		
		if (!DBSObject.isNull(pPropertyNamingStrategy)) {
			xJSON.setPropertyNamingStrategy(pPropertyNamingStrategy);
		}
		try {
			xFromJson = xJSON.readValue(xS, pClass);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xFromJson;
	}
	
	public static <T,P> T fromJsonList(String pJSON, Class<T> pResponseType, Class<P> pParameterType) {
		if (DBSObject.isEmpty(pJSON)) {return null;}
		T 				xFromJson = null;
		ObjectMapper 	xMapper = ParametersSerializer.get();
		
		xMapper.setPropertyNamingStrategy(new ParametersSerializer.DBSPropertyNamingStrategy());
		JavaType xType = xMapper.getTypeFactory().constructParametricType(pResponseType, pParameterType);
		try {
			xFromJson = xMapper.readValue(pJSON, xType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xFromJson;
	}
	
	/**
	 * Retorna classe do tipo informado a partir de String JSon  
	 * @param pObject
	 * @param pClass
	 * @return
	 * @throws DBSIOException
	 */
	public static <T> List<T> fromJsonList(Object pObject, Class<T> pClass){
		if (DBSObject.isEmpty(pObject)) {return null;}
		ObjectMapper 		xJSON = pvCreateJson();
		String 				xS = pObject.toString();
		List<T> 			xList = new ArrayList<T>();
		final TypeFactory 	xFactory = xJSON.getTypeFactory();
		final JavaType 		xListOfT = xFactory.constructCollectionType(List.class, pClass);	
		
		try {
			xList = xJSON.readValue(xS, xListOfT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xList;
	}
	
	private static ObjectMapper pvCreateJson() {
		return ParametersSerializer.get();
	}
	
	/**
	 * Parser de JSON para ENUM
	 * @param pEnumClass
	 * @param pValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T getEnumFromJSONString(Class<T> pEnumClass, String pValue) {
		if (pEnumClass == null) {
			throw new IllegalArgumentException("EnumClass não pode ser null.");
		}

		for (Enum<?> xEnumValue : pEnumClass.getEnumConstants()) {
			if (xEnumValue.toString().equalsIgnoreCase(pValue)) {
				return (T) xEnumValue;
			}
		}

		// Construct an error message that indicates all possible values for the enum.
		StringBuilder xErrorMessage = new StringBuilder();
		boolean xFirstTime = true;
		for (Enum<?> xEnumValue : pEnumClass.getEnumConstants()) {
			xErrorMessage.append(xFirstTime ? "" : ", ").append(xEnumValue);
			xFirstTime = false;
		}
		throw new IllegalArgumentException(pValue + " é um valor inválido. Os valores suportados são " + xErrorMessage);
	}

}
