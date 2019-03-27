package br.com.dbsoft.http;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.httpclient.methods.EntityEnclosingMethod;

import br.com.dbsoft.error.exception.AuthException;
import br.com.dbsoft.error.exception.BadCredentialsException;

public class DBSHttpMethodPatch extends DBSHttpMethod {
	
	//CONSTRUTORES =============================================================================
	public DBSHttpMethodPatch() {
		super();
	}
	public DBSHttpMethodPatch(String pToken) {
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
	public final String doPatch(String pURL) throws AuthException, IOException {
		try {
			EntityEnclosingMethod xMethod = pvCreateMethod(pURL);
			return getResponseAsString(xMethod);
		}catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				EntityEnclosingMethod xMethod = pvCreateMethod(pURL);
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
	public final <T> T doPatch(String pURL, Class<T> pResponseType) throws AuthException, IOException {
		try {
			EntityEnclosingMethod xMethod = pvCreateMethod(pURL);
			return getResponseAsJson(xMethod, pResponseType);
		} catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				EntityEnclosingMethod xMethod = pvCreateMethod(pURL);
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
	public final <T,P> T doPatch(String pURL, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			EntityEnclosingMethod xMethod = pvCreateMethod(pURL);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				EntityEnclosingMethod xMethod = pvCreateMethod(pURL);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	public final <T,P> T doPatch(String pURL, Map<String, String> pExtraHeaders, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			EntityEnclosingMethod xMethod = pvCreateMethod(pURL, pExtraHeaders);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				EntityEnclosingMethod xMethod = pvCreateMethod(pURL, pExtraHeaders);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	//METODOS PRIVADOS =============================================================================
	private EntityEnclosingMethod pvCreateMethod(String pURL) throws AuthException, IOException {
		return pvCreateMethod(pURL, Collections.<String,String>emptyMap());
	}
	
	private EntityEnclosingMethod pvCreateMethod(String pURL, Map<String, String> pExtraHeaders) throws AuthException, IOException{
		return pvCreateBasicEntityEnclosingMethod(pURL, pExtraHeaders);
	}
	
	private EntityEnclosingMethod pvCreateBasicEntityEnclosingMethod(String pURL, Map<String, String> pExtraHeaders) {
		EntityEnclosingMethod xEntityEnclosingMethod = new EntityEnclosingMethod(pURL) {
			@Override
			public String getName() {
				return "PATCH";
			}
		};
		pvConfigHttpMethod(xEntityEnclosingMethod, pExtraHeaders);
		return xEntityEnclosingMethod;
	}

}
