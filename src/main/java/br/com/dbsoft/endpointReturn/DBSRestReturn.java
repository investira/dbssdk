package br.com.dbsoft.endpointReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.dbsoft.message.DBSMessageBase;
import br.com.dbsoft.message.IDBSMessageBase;

/**
 * Wrapper para as respostas dos endpoints do IFeed
 * 
 * @param <C>
 */
@XmlRootElement(name = "collection")
public class DBSRestReturn<C> {

	@JsonProperty("values")
	private List<C> wValues;
	
	@JsonProperty("searchLinks")
	@JsonInclude(value=Include.NON_EMPTY, content=Include.NON_NULL)
	private Map<String, String> wSearchLinks;
	
	@JsonProperty("messages")
	@JsonDeserialize(contentAs=DBSMessageBase.class)
	private List<IDBSMessageBase> wMessages;

	@XmlAnyElement(lax = true)
	public List<C> getValues() {
		return wValues;
	}

	public void setValues(List<C> pValues) {
		wValues = pValues;
	}

	public Map<String, String> getSearchLinks() {
		return wSearchLinks;
	}

	public void setSearchLinks(Map<String, String> pSearchLinks) {
		wSearchLinks = pSearchLinks;
	}

	@XmlAnyElement(lax = true)
	public List<IDBSMessageBase> getMessages() {
		return wMessages;
	}

	public DBSRestReturn() {
		this.wValues = new ArrayList<C>();
		this.wMessages = new ArrayList<IDBSMessageBase>();
	}

	public DBSRestReturn(List<C> pValues) {
		this.wValues = pValues;
	}

	public DBSRestReturn(List<C> pValues, Map<String, String> pSearchLinks, List<IDBSMessageBase> pMessages) {
		this.wValues = pValues;
		this.wSearchLinks = pSearchLinks;
		this.wMessages = pMessages;
	}
	
	public DBSRestReturn(List<C> pValues, List<IDBSMessageBase> pMessages) {
		this.wValues = pValues;
		this.wMessages = pMessages;
	}

	public static <I, C extends I> DBSRestReturn<C> getEndpointReturn(List<I> pValuesInterface, Class<C> pConcretClass, List<IDBSMessageBase> pMessages) {
		List<C> xClasList = new ArrayList<>();

		for (I xInterface : pValuesInterface) {
			xClasList.add(pConcretClass.cast(xInterface));
		}

		return new DBSRestReturn<C>(xClasList, pMessages);
	}
}
