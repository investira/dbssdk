package br.com.dbsoft.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.com.dbsoft.message.DBSMessageBase;
import br.com.dbsoft.message.IDBSMessageBase;

/**
 * Wrapper para as respostas dos endpoints do IFeed
 * 
 * @author Renato
 *
 * @param <C>
 */
@XmlRootElement(name = "collection")
public class DBSEndpointReturnList<C> {

	private List<C> itens;
	
	@JsonDeserialize(contentAs=DBSMessageBase.class)
	private List<IDBSMessageBase> messages;

	@XmlAnyElement(lax = true)
	public List<C> getItens() {
		return itens;
	}

	public void setItens(List<C> pItens) {
		itens = pItens;
	}

	@XmlAnyElement(lax = true)
	public List<IDBSMessageBase> getMessages() {
		return messages;
	}

	public DBSEndpointReturnList() {
	}

	public DBSEndpointReturnList(List<C> pLista) {
		this.itens = pLista;
	}

	public DBSEndpointReturnList(List<C> pLista, List<IDBSMessageBase> pMessages) {
		this.itens = pLista;
		this.messages = pMessages;
	}

	public static <I, C extends I> DBSEndpointReturnList<C> getEndpointReturn(List<I> pListInterface, Class<C> pConcretClass, List<IDBSMessageBase> pMessages) {
		List<C> xClasList = new ArrayList<>();

		for (I xInterface : pListInterface) {
			xClasList.add(pConcretClass.cast(xInterface));
		}

		return new DBSEndpointReturnList<C>(xClasList, pMessages);
	}
}
