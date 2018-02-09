package br.com.dbsoft.service;

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
@XmlRootElement(name="object")
public class DBSEndpointReturn<C> {

	private C object;
	
	@JsonDeserialize(contentAs=DBSMessageBase.class)
	private List<IDBSMessageBase> messages;
	
    @XmlAnyElement(lax=true)
	public C getObject() {
		return object;
	}

	public void setObject(C pObject) {
		this.object = pObject;
	}
	
	@XmlAnyElement(lax = true)
	public List<IDBSMessageBase> getMessages() {
		return messages;
	}

	//CONSTRUTORES
	public DBSEndpointReturn(){}
	
	public DBSEndpointReturn(C pObject) {
		this.object = pObject;
	}
	
	public DBSEndpointReturn(C pObject, List<IDBSMessageBase> pMessage) {
		this.object = pObject;
		this.messages = pMessage;
	}

}
