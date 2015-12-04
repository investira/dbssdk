package br.com.dbsoft.message;


import org.joda.time.DateTime;

import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;


/**
 * @author ricardo.villar
 *
 */
public class DBSMessage {

	private String			wMessageTextOriginal;
	private String			wMessageText;
	private Boolean			wValidated = null; 
	private MESSAGE_TYPE	wMessageType;
	private Exception		wException;
	private String			wMessageTooltip = "";
	private DateTime		wTime;
	private String			wMessageKey = null;
	private Integer			wMessageCode = 0;
	
	public DBSMessage(){}
	
	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageText, 0, pMessageType, pMessageText);
	}
	
	public DBSMessage(MESSAGE_TYPE pMessageType, Integer pMessageCode, String pMessageText){
		pvSetMessage(pMessageText, pMessageCode, pMessageType, pMessageText);
	}

	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText);
	}
	
	public void pvSetMessage(String pMessageKey, Integer pMessageCode, MESSAGE_TYPE pMessageType, String pMessageText){
		setMessageKey(pMessageKey);
		setMessageCode(pMessageCode);
		setMessageText(pMessageText);
		setMessageType(pMessageType);
		wMessageTextOriginal = pMessageText;
	}

	public String getMessageKey(){
		return wMessageKey; 
	}

	public void setMessageKey(String pMessageKey){
		wMessageKey = pMessageKey; 
	}
	
	public String getMessageText() {
		return wMessageText;
	}
	public void setMessageText(String pMessageText) {
		//Seta a chave como o próprio texto caso não tenha seja nula.
		if (wMessageKey == null){
			setMessageKey(pMessageText);
		}
		wMessageText = pMessageText;
	}

	public MESSAGE_TYPE getMessageType() {
		return wMessageType;
	}
	/**
	 * Retorna o tipo de mensagem 
	 * @param pMessageType 
	 */
	public void setMessageType(MESSAGE_TYPE pMessageType) {
		wMessageType = pMessageType;
	}
	
	/**
	 * Código da mensagem.
	 * @return
	 */
	public Integer getMessageCode() {
		return wMessageCode;
	}

	/**
	 * Retorna o código da mensagem, 
	 * @param pMessageCode
	 */
	public void setMessageCode(Integer pMessageCode) {
		wMessageCode = pMessageCode;
	}
	
	/**
	 * @return Null = ainda não validada.<br/> True = Validada para verdadeiro.<br/> False = Validada para falso 
	 */
	public Boolean isValidated() {
		return wValidated;
	}
	
	public void setValidated(Boolean validated) {
		wValidated = validated;
	}
	public Exception getException() {
		return wException;
	}
	/**
	 * Configura a exception vinculada a mensagem caso exista
	 * @param pException
	 */
	public void setException(Exception pException) {
		this.wException = pException;
	}
	public String getMessageTooltip() {
		return wMessageTooltip;
	}
	public void setMessageTooltip(String pMessageTooltip) {
		this.wMessageTooltip = pMessageTooltip;
	}
	
	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	public void setMessageTextParameters(Object... pParameters){
		if (wMessageTextOriginal != null){
			this.setMessageText(String.format(wMessageTextOriginal, pParameters));
		}else{
			this.setMessageText(String.format(this.getMessageText(), pParameters));
		}
	}


	/**
	 * Horário que a mensagem foi criada
	 * @return
	 */
	public DateTime getTime() {
		return wTime;
	}

	/**
	 * Horário que a mensagem foi criada
	 */
	public void setTime(DateTime pTime) {
		wTime = pTime;
	}


}
