package br.com.dbsoft.message;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.dbsoft.json.resolver.DBSDateTimeDeserializer;
import br.com.dbsoft.json.resolver.DBSDateTimeSerializer;

//@XmlRootElement
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(value = { "messageText", "messageCode", "statusCode", "messageTime", "messageType" }, ignoreUnknown = true)
public class DBSMessageBase implements IDBSMessageBase {

	private static final long serialVersionUID = -2180786400261608470L;
	
	@JsonProperty("text")
	private String						wMessageText;
	@JsonProperty("code")
	private Integer						wMessageCode = 0;
	@JsonProperty("messageType")
	private MESSAGE_TYPE				wMessageType;
	@JsonSerialize(using = DBSDateTimeSerializer.class)
	@JsonDeserialize(using = DBSDateTimeDeserializer.class)
	@JsonProperty("messageTime")
	private DateTime					wMessageTime;
	@JsonProperty("statusCode")
	private Integer						wStatusCode;
	
	//Construtores============================
	@Override
	public String getMessageText() {return wMessageText;}
	
	@Override
	public void setMessageText(String pMessageText) {
		wMessageText = pMessageText;
	}

	@Override
	public MESSAGE_TYPE getMessageType() {return wMessageType;}
	
	/**
	 * Retorna o tipo de mensagem 
	 * @param pMessageType 
	 */
	@Override
	public void setMessageType(MESSAGE_TYPE pMessageType) {wMessageType = pMessageType;}
	
	/**
	 * Código da mensagem.
	 * @return
	 */
	@Override
	public Integer getMessageCode() {return wMessageCode;}

	/**
	 * Retorna o código da mensagem, 
	 * @param pMessageCode
	 */
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageCode(java.lang.Integer)
	 */
	@Override
	public void setMessageCode(Integer pMessageCode) {wMessageCode = pMessageCode;}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getMessageTime()
	 */
	@Override
	public DateTime getMessageTime() {return wMessageTime;}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageTime(org.joda.time.DateTime)
	 */
	@Override
	public void setMessageTime(DateTime pTime) {wMessageTime = pTime;}

	@Override
	public Integer getStatusCode() {
		return wStatusCode;
	}
	@Override
	public void setStatusCode(Integer pStatusCode) {
		wStatusCode = pStatusCode;
	}

	@Override
	public String toString() {
		return getMessageText();
	}
}
