package br.com.dbsoft.http;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.httpclient.methods.DeleteMethod;

import br.com.dbsoft.error.exception.AuthException;
import br.com.dbsoft.error.exception.BadCredentialsException;

public class DBSHttpMethodDelete extends DBSHttpMethod {
	
	//CONSTRUTORES =============================================================================
	public DBSHttpMethodDelete() {
		super();
	}
	public DBSHttpMethodDelete(String pToken) {
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
	public final String doDelete(String pURL) throws AuthException, IOException {
		try {
			DeleteMethod xMethod = pvCreateMethod(pURL);
			return getResponseAsString(xMethod);
		}catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				DeleteMethod xMethod = pvCreateMethod(pURL);
				return getResponseAsString(xMethod);
			}
			throw e;
		}
	}
	
	public final <T,P> T doDelete(String pURL, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			DeleteMethod xMethod = pvCreateMethod(pURL);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				DeleteMethod xMethod = pvCreateMethod(pURL);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	public final <T,P> T doDelete(String pURL, Map<String, String> pExtraHeaders, Class<T> pResponseType, Class<P> pParameterType) throws AuthException, IOException {
		try {
			DeleteMethod xMethod = pvCreateMethod(pURL, pExtraHeaders);
			return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
		} 
		catch(BadCredentialsException e) {
			if (forceRenewToken()) {
				DeleteMethod xMethod = pvCreateMethod(pURL, pExtraHeaders);
				return pvGetResponseAsJson(xMethod, pResponseType, pParameterType);
			}
			throw e;
		}
	}
	
	//METODOS PRIVADOS =============================================================================
	private DeleteMethod pvCreateMethod(String pURL) throws AuthException, IOException {
		return pvCreateMethod(pURL, Collections.<String,String>emptyMap());
	}
	
	private DeleteMethod pvCreateMethod(String pURL, Map<String, String> pExtraHeaders) throws AuthException, IOException{
		return pvCreateBasicGetMethod(pURL, pExtraHeaders);
	}
	
	private DeleteMethod pvCreateBasicGetMethod(String pURL, Map<String, String> pExtraHeaders) {
		DeleteMethod xGetMethod = new DeleteMethod(pURL);
		pvConfigHttpMethod(xGetMethod, pExtraHeaders);
		return xGetMethod;
	}

}
