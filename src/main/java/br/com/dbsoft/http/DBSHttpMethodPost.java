package br.com.dbsoft.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import br.com.dbsoft.error.exception.AuthException;
import br.com.dbsoft.error.exception.BadCredentialsException;
import br.com.dbsoft.json.DBSJson;

public class DBSHttpMethodPost extends DBSHttpMethod {

	//CONSTRUTORES =============================================================================
	public DBSHttpMethodPost() {
		super();
	}
	public DBSHttpMethodPost(String pToken) {
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
	 * Executa chamada POST ao metódo definido na URL e retorna o resultado como String
	 * @param pURL
	 * @param pData
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final String doPost(String pURL, Map<String, String> pData) throws AuthException, IOException {
		try {
			PostMethod xMethod = pvCreatePostMethod(pURL, pData);
			return getResponseAsString(xMethod);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PostMethod xMethod = pvCreatePostMethod(pURL, pData);
				return getResponseAsString(xMethod);
			}
			throw e;
		}
	}

	/**
	 * Executa chamada POST ao metódo definido na URL e retorna o resultado como XML
	 * @param pURL
	 * @param pData
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final String doPostXML(String pURL, Map<String, String> pData) throws AuthException, IOException {
		try {
			PostMethod xMethod = pvCreatePostMethod(pURL, pData, wExtraHeaderInfo);
			return getResponseAsString(xMethod);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PostMethod xMethod = pvCreatePostMethod(pURL, pData, wExtraHeaderInfo);
				return getResponseAsString(xMethod);
			}
			throw e;
		}
	}

	/**
	 * Executa chamada POST ao metódo definido na URL e retorna o resultado como Class
	 * @param pURL
	 * @param pData
	 * @param pResponseType
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final <T> T doPost(String pURL, Map<String, String> pData, Class<T> pResponseType) throws AuthException, IOException {
		try {
			PostMethod xMethod = pvCreatePostMethod(pURL, pData);
			return getResponseAsJson(xMethod, pResponseType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PostMethod xMethod = pvCreatePostMethod(pURL, pData);
				return getResponseAsJson(xMethod, pResponseType);
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
	public final <T, P> T doPost(String pURL, Map<String, String> pData, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			PostMethod xMethod = pvCreatePostMethod(pURL, pData);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PostMethod xMethod = pvCreatePostMethod(pURL, pData);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	/**
	 * Executa chamada POST ao metódo definido na URL e retorna o resultado como XML
	 * @param pURL
	 * @param pObject
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final String doPostXML(String pURL, Object pObject) throws AuthException, IOException	{
		try {
			PostMethod xMethod = pvCreatePostMethod(pURL, pObject, wExtraHeaderInfo);
			return getResponseAsString(xMethod);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PostMethod xMethod = pvCreatePostMethod(pURL, pObject, wExtraHeaderInfo);
				return getResponseAsString(xMethod);
			}
			throw e;
		}
	}

	/**
	 * Executa chamada POST ao metódo definido na URL e retorna o resultado como String
	 * @param pURL
	 * @param pObject
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final String doPost(String pURL, Object pObject) throws AuthException, IOException	{
		try {
			PostMethod xMethod = pvCreatePostMethod(pURL, pObject);
			return getResponseAsString(xMethod);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PostMethod xMethod = pvCreatePostMethod(pURL, pObject);
				return getResponseAsString(xMethod);
			}
			throw e;
		}
	}

	/**
	 * Executa chamada POST ao metódo definido na URL e retorna o resultado como Class
	 * @param pURL
	 * @param pObject
	 * @param pResponseType
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final <T> T doPost(String pURL, Object pObject, Class<T> pResponseType) throws AuthException, IOException {
		try {
			PostMethod xMethod = pvCreatePostMethod(pURL, pObject);
			return getResponseAsJson(xMethod, pResponseType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PostMethod xMethod = pvCreatePostMethod(pURL, pObject);
				return getResponseAsJson(xMethod, pResponseType);
			}
			throw e;
		}
	}
	
	/**
	 * Executa chamada POST ao metódo definido na URL e retorna o resultado como String
	 * @param pURL
	 * @param pObject
	 * @param pResponseType
	 * @param pParameterType
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final <T, P> T doPost(String pURL, Object pObject, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			PostMethod xMethod = pvCreatePostMethod(pURL, pObject);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				PostMethod xMethod = pvCreatePostMethod(pURL, pObject);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	//METODOS PRIVADOS =============================================================================	
	private PostMethod pvCreatePostMethod(String pURL, Map<String, String> pData) throws AuthException, IOException {
		return pvCreatePostMethod(pURL, pData, Collections.<String,String>emptyMap());
	}
	
	private PostMethod pvCreatePostMethod(String pURL, Map<String, String> pData, Map<String, String> pExtraHeaders) throws AuthException, IOException {
		PostMethod xPostMethod = pvCreateBasicPostMethod(pURL, pExtraHeaders);
		
		List<NameValuePair> xPostData = new ArrayList<>(pData.size());
		for (Map.Entry<String, String> e : pData.entrySet()) {
			xPostData.add(new NameValuePair(e.getKey(), e.getValue()));
		}
		xPostMethod.setRequestBody(xPostData.toArray(new NameValuePair[pData.size()]));
		
		return xPostMethod;
	}
	
	private PostMethod pvCreatePostMethod(String pURL, Object pObject) throws AuthException, IOException {
		return pvCreatePostMethod(pURL, pObject, Collections.<String, String>emptyMap());
	}
	
	private PostMethod pvCreatePostMethod(String pURL, Object pObject, Map<String, String> pExtraHeaders) throws AuthException, IOException {
		PostMethod xPostMethod = pvCreateBasicPostMethod(pURL, pExtraHeaders);
		
		String xJSon = DBSJson.toJson(pObject);
		StringRequestEntity xJSonPostData = new StringRequestEntity(xJSon, "application/json", StandardCharsets.UTF_8.name());  
		xPostMethod.setRequestEntity(xJSonPostData);
		
		return xPostMethod;
	}
	
	private PostMethod pvCreateBasicPostMethod(String pURL, Map<String, String> pExtraHeaders) {
		PostMethod xPostMethod = new PostMethod(pURL);
		pvConfigHttpMethod(xPostMethod, pExtraHeaders);
		return xPostMethod;
	};
}
