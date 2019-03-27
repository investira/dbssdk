package br.com.dbsoft.http;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.httpclient.methods.GetMethod;

import br.com.dbsoft.error.exception.AuthException;
import br.com.dbsoft.error.exception.BadCredentialsException;

public class DBSHttpMethodGet extends DBSHttpMethod {
	
	//CONSTRUTORES =============================================================================
	public DBSHttpMethodGet() {
		super();
	}
	public DBSHttpMethodGet(String pToken) {
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
	public final String doGet(String pURL) throws AuthException, IOException {
		try {
			GetMethod xMethod = pvCreateMethod(pURL);
			return getResponseAsString(xMethod);
		}catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				GetMethod xMethod = pvCreateMethod(pURL);
				return getResponseAsString(xMethod);
			}
			throw e;
		}
	}
	
	/**
	 * Executa chamada GET ao metódo definido na URL e retorna o resultado como XML
	 * @param pURL
	 * @return
	 * @throws AuthException
	 * @throws IOException
	 */
	public final String doGetXML(String pURL) throws AuthException, IOException {
		try {
			GetMethod xMethod = pvCreateMethod(pURL, wExtraHeaderInfo);
			return getResponseAsString(xMethod);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				GetMethod xMethod = pvCreateMethod(pURL, wExtraHeaderInfo);
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
	public final <T> T doGet(String pURL, Class<T> pResponseType) throws AuthException, IOException {
		try {
			GetMethod xMethod = pvCreateMethod(pURL);
			return getResponseAsJson(xMethod, pResponseType);
		} catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				GetMethod xMethod = pvCreateMethod(pURL);
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
	public final <T,P> T doGet(String pURL, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			GetMethod xMethod = pvCreateMethod(pURL);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				GetMethod xMethod = pvCreateMethod(pURL);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	public final <T,P> T doGet(String pURL, Map<String, String> pExtraHeaders, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			GetMethod xMethod = pvCreateMethod(pURL, pExtraHeaders);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				GetMethod xMethod = pvCreateMethod(pURL, pExtraHeaders);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	public final <T,P> T doGetList(String pURL, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			GetMethod xMethod = pvCreateMethod(pURL);
			return pvGetResponseListAsJson(xMethod, pResponseType, pParameterType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				GetMethod xMethod = pvCreateMethod(pURL);
				return pvGetResponseListAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	//METODOS PRIVADOS =============================================================================
	private GetMethod pvCreateMethod(String pURL) throws AuthException, IOException {
		return pvCreateMethod(pURL, Collections.<String,String>emptyMap());
	}
	
	private GetMethod pvCreateMethod(String pURL, Map<String, String> pExtraHeaders) throws AuthException, IOException{
		return pvCreateBasicGetMethod(pURL, pExtraHeaders);
	}
	
	private GetMethod pvCreateBasicGetMethod(String pURL, Map<String, String> pExtraHeaders) {
		GetMethod xGetMethod = new GetMethod(pURL);
		pvConfigHttpMethod(xGetMethod, pExtraHeaders);
		return xGetMethod;
	}

}
