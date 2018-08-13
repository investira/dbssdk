package br.com.dbsoft.http;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;

import br.com.dbsoft.error.exception.BadCredentialsException;
import br.com.dbsoft.error.exception.CommunicationException;
import br.com.dbsoft.error.exception.ForbiddenException;
import br.com.dbsoft.json.DBSJson;
import br.com.dbsoft.json.ParametersSerializer;
import br.com.dbsoft.util.DBSHttp;
import br.com.dbsoft.util.DBSObject;

public abstract class DBSHttpMethod {
	
	protected static Logger wLogger = Logger.getLogger(DBSHttpMethod.class);
	
	protected Map<String, String> wExtraHeaderInfo = Collections.singletonMap("Accept", "application/xml;charset=UTF-8");
	
	protected String wToken;

	private String wUsername;
	
	/*
	 * fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException:
	 * sun.security.validator.ValidatorException: PKIX path building failed:
	 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find
	 * valid certification path to requested target
	 */
	private static TrustManager[] wTrustAllCerts = new TrustManager[] { 
		new X509TrustManager() {

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}
	};
	
	public void setUsername(String pUsername) {
		wUsername = pUsername;
	}
	
	//CONSTRUTORES =====================================================================================
	public DBSHttpMethod() {}
	
	public DBSHttpMethod(String pToken) {
		wToken = pToken;
	}
	
	//MÉTODOS PUBLICOS ================================================================================
	/**
	 * Cria um HttpClient para conexões HTTP.
	 * Configura o HTTPS para aceitar qualquer certificado
	 * @return HttpClient
	 */
	public static final HttpClient getHttpClient() {
		//Aceita como válido qualquer certificado da conexão SSL
		SSLContext xSSLContext;
		try {
			xSSLContext = SSLContext.getInstance("SSL");
			xSSLContext.init(null, wTrustAllCerts, new java.security.SecureRandom());
			SSLContext.setDefault(xSSLContext);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			wLogger.error(e);
		}
		
		return new HttpClient();
	}
	
	public static final String getResponseAsString(HttpMethod pMethod){
		return getResponseAsString(pMethod, null);
	}
	
	public static final String getResponseAsString(HttpMethod pMethod, HttpClient pClient){
		HttpClient xClient = pClient;
		if (xClient == null) {
			xClient = getHttpClient();
		}
		
		String xResponse = "";
		try {
			xClient.executeMethod(pMethod);
			xResponse = DBSHttp.getStringFromInputStream(pMethod.getResponseBodyAsStream());
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new CommunicationException("Server is offline", e);
		}
		
		int xStatusCode = pMethod.getStatusCode();
		if (xStatusCode == 401) {
			throw new BadCredentialsException("Token inválido: " + xResponse);
		}

		if (xStatusCode == 403) {
			throw new ForbiddenException("Acesso negado: " + xResponse);
		}
		
		if (xStatusCode == 404) {
			throw new CommunicationException("Serviço indisponível: " + xResponse);
		}
		
		if (xStatusCode == 505) {
			throw new CommunicationException("Serviço funciona apenas em HTTPS");
		}

		if (xStatusCode != 200) {
			throw new BadCredentialsException("Erro ao obter dados de autenticação: " + xResponse);
		}
		return xResponse;
	}
	
	public static final <T> T getResponseAsJson(HttpMethod pMethod, Class<T> pResponseType){
		String xResponse = getResponseAsString(pMethod);
		try {
			return DBSJson.fromJson(xResponse, pResponseType);
		}
		catch (JsonSyntaxException e) {
			throw new IllegalStateException("Error converting response to " + pResponseType.getSimpleName() + ":\n---\n" + xResponse + "\n---", e);
		}	
	}
	
	//MÉTODOS PROTECTED ================================================================================
	protected abstract boolean forceRenewToken();
	
	protected void pvConfigHttpMethod(HttpMethod pMethod, Map<String, String> pExtraHeaders) {
		for (Map.Entry<String, String> header : pExtraHeaders.entrySet()) {
			pMethod.setRequestHeader(new Header(header.getKey(), header.getValue()));
		}
		
		if (!DBSObject.isEmpty(wToken)) {
			Header xOAuthHeader = new Header("Authorization", "Bearer " + wToken);
			pMethod.setRequestHeader(xOAuthHeader);
		}
		
		if (!DBSObject.isEmpty(wUsername)) {
			Header xUserHeader = new Header("username", wUsername);
			pMethod.setRequestHeader(xUserHeader);
		}
	}
	
	protected final <T,P> T pvGetResponseAsJson(HttpMethod pMethod, Class<T> pResponseType, Class<P> pParameterType) throws IOException{
		String xResponse = getResponseAsString(pMethod);
		ObjectMapper xMapper = ParametersSerializer.get();
		xMapper.setPropertyNamingStrategy(new ParametersSerializer.DBSPropertyNamingStrategy());
		JavaType xType = xMapper.getTypeFactory().constructParametricType(pResponseType, pParameterType);
		return xMapper.readValue(xResponse, xType);
	}
}
