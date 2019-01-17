package br.com.dbsoft.endpointReturn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

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
public class DBSEndpointReturnValues<C> {

	@JsonProperty("values")
	private List<C> wValues;
	
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

	@XmlAnyElement(lax = true)
	public List<IDBSMessageBase> getMessages() {
		return wMessages;
	}

	public DBSEndpointReturnValues() {
		this.wValues = new ArrayList<C>();
		this.wMessages = new ArrayList<IDBSMessageBase>();
	}

	public DBSEndpointReturnValues(List<C> pValues) {
		this.wValues = pValues;
	}

	public DBSEndpointReturnValues(List<C> pValues, List<IDBSMessageBase> pMessages) {
		this.wValues = pValues;
		this.wMessages = pMessages;
	}

	public static <I, C extends I> DBSEndpointReturnValues<C> getEndpointReturn(List<I> pValuesInterface, Class<C> pConcretClass, List<IDBSMessageBase> pMessages) {
		List<C> xClasList = new ArrayList<>();

		for (I xInterface : pValuesInterface) {
			xClasList.add(pConcretClass.cast(xInterface));
		}

		return new DBSEndpointReturnValues<C>(xClasList, pMessages);
	}
}
