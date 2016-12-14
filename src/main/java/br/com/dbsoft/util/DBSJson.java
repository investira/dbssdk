package br.com.dbsoft.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import br.com.dbsoft.error.DBSIOException;

public class DBSJson {

//	private static Logger wLogger = Logger.getLogger(DBSJSON.class);

	/**
	 * Retorna String JSon a partir de objeto informado
	 * @param pObject
	 * @return
	 * @throws DBSIOException
	 */
	public static String toJsonTree(Object pObject){ 
		if (pObject == null) {return null;}
		Gson xJSON = new Gson();
		JsonElement xJE = xJSON.toJsonTree(pObject);
		if (xJE.isJsonArray()){return null;}
		return "";
	}
	
	/**
	 * Retorna String JSon a partir de objeto informado
	 * @param pObject
	 * @return
	 * @throws DBSIOException
	 */
	public static <T> String toJson(T pObject){
		if (pObject == null) {return null;}
		Gson xJSON = new Gson();
		return xJSON.toJson(pObject);
	}

	/**
	 * Retorna classe do tipo informado a partir de String JSon  
	 * @param pObject
	 * @param pClass
	 * @return
	 * @throws DBSIOException
	 */
	public static <T> T fromJson(Object pObject, Class<T> pClass){
		if (pObject == null) {return null;}
		Gson   xJSON = new Gson();
		String xS = pObject.toString();
		return xJSON.fromJson(xS, pClass);
	}
	
	/**
	 * Retorna classe do tipo informado a partir de String JSon  
	 * @param pObject
	 * @param pClass
	 * @return
	 * @throws DBSIOException
	 */
	public static <T> List<T> fromJsonList(Object pObject, Class<T> pClass){
		if (pObject == null) {return null;}
		Gson   xJSON = new Gson();
		String xS = pObject.toString();
		Type xType = new TypeToken<List<T>>(){}.getType();
		return xJSON.fromJson(xS, xType);
	}
}
