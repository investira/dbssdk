package br.com.dbsoft.rest;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.dbsoft.rest.dados.DadosRestError;
import br.com.dbsoft.rest.interfaces.IRestError;

/**
 * Classe para as respostas dos serviços rest
 * 
 * @param <C>
 */
public class DBSRestReturn<C> {

	@JsonProperty("data")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private C wData;
	
	@JsonProperty("error")
	@JsonDeserialize(contentAs=DadosRestError.class)
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private IRestError wError;

	@JsonProperty("pages")
	@JsonInclude(value=Include.NON_EMPTY, content=Include.NON_NULL)
	private Map<String, String> wPages;
	
	@JsonProperty("metaData")
	@JsonInclude(value=Include.NON_EMPTY, content=Include.NON_NULL)
	private Map<String, Object> wMetaData;
	
	public C getData() {
		return wData;
	}

	public void setData(C pData) {
		wData = pData;
	}

	public IRestError getError() {
		return wError;
	}

	public void setError(IRestError pError) {
		wError = pError;
	}

	public Map<String, String> getPages() {
		return wPages;
	}

	public void setPages(Map<String, String> pPages) {
		wPages = pPages;
	}

	public Map<String, Object> getMetaData() {
		return wMetaData;
	}

	public void setMetaData(Map<String, Object> pMetaData) {
		wMetaData = pMetaData;
	}

	//CONSTRUTORES ====================================================
	public DBSRestReturn() {
	}
	
	public DBSRestReturn(C pData) {
		wData = pData;
	}

	public DBSRestReturn(C pData, IRestError pRestError) {
		wData = pData;
		wError = pRestError;
	}
	
	public DBSRestReturn(C pData, IRestError pRestError, Map<String, String> pPages) {
		wData = pData;
		wError = pRestError;
		wPages = pPages;
	}
	
	public DBSRestReturn(C pData, IRestError pRestError, Map<String, String> pPages, Map<String, Object> pMetaData) {
		wData = pData;
		wError = pRestError;
		wPages = pPages;
		wMetaData = pMetaData;
	}

}
