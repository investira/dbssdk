package br.com.dbsoft.message;

import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@XmlRootElement
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class DBSMessageBase implements IDBSMessageBase {

	private static final long serialVersionUID = -2180786400261608470L;
	
	@JsonProperty("messageText")
	private String						messageText;
	@JsonProperty("messageType")
	private MESSAGE_TYPE				messageType;
	@JsonProperty("messageCode")
	private Integer						messageCode = 0;
	@JsonProperty("time")
	private DateTime					time;
	
	//Construtores============================
	@Override
	public String getMessageText() {return messageText;}
	
	@Override
	public void setMessageText(String pMessageText) {
		messageText = pMessageText;
	}

	@Override
	public MESSAGE_TYPE getMessageType() {return messageType;}
	
	/**
	 * Retorna o tipo de mensagem 
	 * @param pMessageType 
	 */
	@Override
	public void setMessageType(MESSAGE_TYPE pMessageType) {messageType = pMessageType;}
	
	/**
	 * Código da mensagem.
	 * @return
	 */
	@Override
	public Integer getMessageCode() {return messageCode;}

	/**
	 * Retorna o código da mensagem, 
	 * @param pMessageCode
	 */
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageCode(java.lang.Integer)
	 */
	@Override
	public void setMessageCode(Integer pMessageCode) {messageCode = pMessageCode;}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getMessageTime()
	 */
	@Override
	public DateTime getMessageTime() {return time;}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageTime(org.joda.time.DateTime)
	 */
	@Override
	public void setMessageTime(DateTime pTime) {time = pTime;}

	
	@Override
	public String toString() {
		return getMessageText();
	}
}
