package br.com.dbsoft.rest;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.dbsoft.message.DBSMessageBase;
import br.com.dbsoft.message.IDBSMessageBase;
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
	
	@JsonProperty("include")
	@JsonInclude(value=Include.NON_EMPTY, content=Include.NON_NULL)
	private Map<String, Object> wInclude;
	
	@JsonProperty("messages")
	@JsonDeserialize(contentAs=DBSMessageBase.class)
	@JsonInclude(value=Include.NON_EMPTY, content=Include.NON_NULL)
	private List<IDBSMessageBase> wMessages;
	
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
	
	public Map<String, Object> getInclude() {
		return wInclude;
	}
	public void setInclude(Map<String, Object> pInclude) {
		wInclude = pInclude;
	}
	
	public List<IDBSMessageBase> getMessages() {
		return wMessages;
	}
	public void setMessages(List<IDBSMessageBase> pMessages) {
		wMessages = pMessages;
	}

}
