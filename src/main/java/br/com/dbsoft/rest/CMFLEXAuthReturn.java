package br.com.dbsoft.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe para as respostas dos serviços rest
 * 
 * @param <C>
 */
public class CMFLEXAuthReturn<C> {

	@JsonProperty("access_token")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wAccessToken;
	
	@JsonProperty("token_type")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wTokenType;
	
	@JsonProperty("expires_in")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private Integer wExpiresIn;
	
	@JsonProperty("as:client_id")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wAsClientId;
	
	@JsonProperty("userName")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wUserName;
	
	@JsonProperty("IdUsuarioLogado")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wIdUsuarioLogado;
	
	@JsonProperty("NomeDoUsuarioLogado")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wNomeDoUsuarioLogado;
	
	@JsonProperty("IdEmpresaLogada")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wIdEmpresaLogada;
	
	@JsonProperty("AliasEmpresaLogada")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wAliasEmpresaLogada;
	
	@JsonProperty("ListaEmpresas")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wListaEmpresas;
	
	@JsonProperty("UsuarioMaster")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wUsuarioMaster;
	
	@JsonProperty(".issued")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wIssued;
	
	@JsonProperty(".expires")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wExpires;
	
	public String getAccessToken() {
	    return wAccessToken;
	}

	public void setAccessToken(String pAccessToken) {
	    wAccessToken = pAccessToken;
	}

	public String getTokenType() {
	    return wTokenType;
	}

	public void setTokenType(String pTokenType) {
	    wTokenType = pTokenType;
	}

	public Integer getExpiresIn() {
	    return wExpiresIn;
	}

	public void setExpiresIn(Integer pExpiresIn) {
	    wExpiresIn = pExpiresIn;
	}

	public String getAsClientId() {
	    return wAsClientId;
	}

	public void setAsClientId(String pAsClientId) {
	    wAsClientId = pAsClientId;
	}

	public String getUserName() {
	    return wUserName;
	}

	public void setUserName(String pUserName) {
	    wUserName = pUserName;
	}

	public String getIdUsuarioLogado() {
	    return wIdUsuarioLogado;
	}

	public void setIdUsuarioLogado(String pIdUsuarioLogado) {
	    wIdUsuarioLogado = pIdUsuarioLogado;
	}

	public String getNomeDoUsuarioLogado() {
	    return wNomeDoUsuarioLogado;
	}

	public void setNomeDoUsuarioLogado(String pNomeDoUsuarioLogado) {
	    wNomeDoUsuarioLogado = pNomeDoUsuarioLogado;
	}

	public String getIdEmpresaLogada() {
	    return wIdEmpresaLogada;
	}

	public void setIdEmpresaLogada(String pIdEmpresaLogada) {
	    wIdEmpresaLogada = pIdEmpresaLogada;
	}

	public String getAliasEmpresaLogada() {
	    return wAliasEmpresaLogada;
	}

	public void setAliasEmpresaLogada(String pAliasEmpresaLogada) {
	    wAliasEmpresaLogada = pAliasEmpresaLogada;
	}

	public String getListaEmpresas() {
	    return wListaEmpresas;
	}

	public void setListaEmpresas(String pListaEmpresas) {
	    wListaEmpresas = pListaEmpresas;
	}

	public String getUsuarioMaster() {
	    return wUsuarioMaster;
	}

	public void setUsuarioMaster(String pUsuarioMaster) {
	    wUsuarioMaster = pUsuarioMaster;
	}

	public String getIssued() {
	    return wIssued;
	}

	public void setIssued(String pIssued) {
	    wIssued = pIssued;
	}

	public String getExpires() {
	    return wExpires;
	}

	public void setExpires(String pExpires) {
	    wExpires = pExpires;
	}
}
