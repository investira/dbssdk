package br.com.dbsoft.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import br.com.dbsoft.error.exception.AuthException;
import br.com.dbsoft.error.exception.BadCredentialsException;
import br.com.dbsoft.json.DBSJson;

public class DBSHttpMethodPut extends DBSHttpMethod {
	
	//CONSTRUTORES =============================================================================
	public DBSHttpMethodPut() {
		super();
	}
	public DBSHttpMethodPut(String pToken) {
		super(pToken);
	}
	
	//METODOS OVERRIDE =============================================================================
	@Override
	protected boolean forceRenewToken() {
		setUsername(null);
		wToken = null;
		return true;
	};
	
	//METODOS PUBLICOS =============================================================================
	/**
	 * Executa chamada GET ao metódo definido na URL e retorna o resultado como String
	 * @param pURL
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final String doPut(String pURL) throws AuthException, IOException {
		try {
			PutMethod xMethod = pvCreateMethod(pURL);
			return getResponseAsString(xMethod);
		}catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PutMethod xMethod = pvCreateMethod(pURL);
				return getResponseAsString(xMethod);
			}
			throw e;
		}
	}
	
	/**
	 * Executa chamada GET ao metódo definido na URL e retorna o resultado como Class
	 * @param pURL
	 * @param pResponseType
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final <T> T doPut(String pURL, Class<T> pResponseType) throws AuthException, IOException {
		try {
			PutMethod xMethod = pvCreateMethod(pURL);
			return getResponseAsJson(xMethod, pResponseType);
		} catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PutMethod xMethod = pvCreateMethod(pURL);
				return getResponseAsJson(xMethod, pResponseType);
			}
			throw e;
		}
	}
	
	/**
	 * Executa chamada GET ao metódo definido na URL e retorna o resultado como Class
	 * @param pURL
	 * @param pResponseType
	 * @param pParameterType
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final <T,P> T doPut(String pURL, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			PutMethod xMethod = pvCreateMethod(pURL);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PutMethod xMethod = pvCreateMethod(pURL);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	/**
	 * Executa chamada POST ao metódo definido na URL e retorna o resultado como Class
	 * @param pURL
	 * @param pData
	 * @param pResponseType
	 * @param pParameterType
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final <T, P> T doPut(String pURL, Object pData, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			PutMethod xMethod = pvCreateMethod(pURL, pData);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PutMethod xMethod = pvCreateMethod(pURL, pData);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	public final <T, P> T doPost(String pURL, Map<String, String> pExtraHeaders, Object pData, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			PutMethod xMethod = pvCreateMethod(pURL, pData, pExtraHeaders);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PutMethod xMethod = pvCreateMethod(pURL, pData, pExtraHeaders);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	//METODOS PRIVADOS =============================================================================
	private PutMethod pvCreateMethod(String pURL) throws AuthException, IOException {
		return pvCreateMethod(pURL, Collections.<String,String>emptyMap());
	}
	
	private PutMethod pvCreateMethod(String pURL, Map<String, String> pExtraHeaders) throws AuthException, IOException{
		return pvCreateBasicMethod(pURL, pExtraHeaders);
	}
	
	private PutMethod pvCreateBasicMethod(String pURL, Map<String, String> pExtraHeaders) {
		PutMethod xMethod = new PutMethod(pURL);
		pvConfigHttpMethod(xMethod, pExtraHeaders);
		return xMethod;
	}
	private PutMethod pvCreateMethod(String pURL, Object pData) throws AuthException, IOException {
		return pvCreateMethod(pURL, pData, new HashMap<String, String>());
	}
	private PutMethod pvCreateMethod(String pURL, Object pData, Map<String, String> pExtraHeaders) throws AuthException, IOException {
		PutMethod xMethod = pvCreateBasicMethod(pURL, pExtraHeaders);
		String xJSon = DBSJson.toJson(pData);
		StringRequestEntity xJSonPostData = new StringRequestEntity(xJSon, "application/json", StandardCharsets.UTF_8.name());  
		xMethod.setRequestEntity(xJSonPostData);
		return xMethod;
	}

}
