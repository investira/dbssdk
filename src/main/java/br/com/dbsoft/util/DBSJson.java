package br.com.dbsoft.util;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
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
	public static JsonElement toJsonTree(Object pObject){ 
		if (pObject == null) {return null;}
		Gson xJSON = new Gson();
		JsonElement xJE = xJSON.toJsonTree(pObject);
		if (xJE.isJsonArray()){return null;}
		return xJE;
	}
	
	/**
	 * Retorna String JSon a partir de objeto informado
	 * @param pObject
	 * @return
	 * @throws DBSIOException
	 */
	public static <T> JsonElement toJsonTree(Object pObject, Class<T> pClass){ 
		if (pObject == null) {return null;}
		Gson xJSON = new Gson();
		JsonElement xJE = xJSON.toJsonTree(pObject, pClass);
		return xJE;
	}

	/**
	 * Retorna String JSon a partir de objeto informado
	 * @param pObject
	 * @return
	 * @throws DBSIOException
	 */
	public static <T> String toJson(T pObject){
		if (pObject == null) {return null;}
//		Gson xJSON = new Gson();
		Gson xJSON = pvCreateGson();
		return xJSON.toJson(pObject);
	}
	
	/**
	 * Retorna String JSon a partir de objeto informado segundo uma política de nomes para campos
	 * @param pObject
	 * @param pFieldNamingPolicy
	 * @return
	 */
	public static <T> String toJson(T pObject, FieldNamingPolicy pFieldNamingPolicy){
		if (pObject == null) {return null;}
		Gson xJSON = new GsonBuilder()
				.setFieldNamingPolicy(pFieldNamingPolicy)
				.create();
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
		T 		xFromJson = null;
		Gson   	xJSON = pvCreateGson();
		String 	xS = pObject.toString();
		try {
			xFromJson = xJSON.fromJson(xS, pClass);
		} catch (Exception xE) {
			xE.printStackTrace();
		}
		return xFromJson;
	}
	
	/**
	 * Retorna classe do tipo informado a partir de String JSon segundo uma política de nomes para campos
	 * @param pObject
	 * @param pClass
	 * @param pFieldNamingPolicy
	 * @return
	 */
	public static <T> T fromJson(Object pObject, Class<T> pClass, FieldNamingPolicy pFieldNamingPolicy){
		if (pObject == null) {return null;}
		Gson xJSON = new GsonBuilder()
				.setFieldNamingPolicy(pFieldNamingPolicy)
				.create();
		
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
	
	/**
	 * Cria uma instância de GSON (Google) e registra dois adapters: java.sql.Date e java.sql.Timestamp
	 * @return
	 */
	private static Gson pvCreateGson() {
		//Cria um objeto construtor json para gerencia as informações recebidas
		GsonBuilder xBuilder = new GsonBuilder(); 

		//Registra os adaptadores para fazer a DESERIALIZAÇÃO de valor LONG para o tipo informado
		//SQL.Date
		//Deserialização
		xBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() { 
			@Override
			public Date deserialize(JsonElement pJson, Type pTypeOfT, JsonDeserializationContext pContext) throws JsonParseException {
				return new Date(pJson.getAsJsonPrimitive().getAsLong()); 
			}
		});
		//Serialização
		xBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
			@Override
			public JsonElement serialize(Date pSrc, Type pTypeOfSrc, JsonSerializationContext pContext) {
				return new JsonPrimitive(DBSFormat.getFormattedDateTimes(pSrc));
			}
		});
		
		//SQL.Timestamp 
		xBuilder.registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() { 
			@Override
			public Timestamp deserialize(JsonElement pJson, Type pTypeOfT, JsonDeserializationContext pContext) throws JsonParseException {
				return new Timestamp(pJson.getAsJsonPrimitive().getAsLong()); 
			} 
		});
		
		//Registra o formato das datas;
		xBuilder.setDateFormat("dd/MM/yyyy HH:mm:ss");
		//Cria o Gson
		return xBuilder.create();
	}
}
