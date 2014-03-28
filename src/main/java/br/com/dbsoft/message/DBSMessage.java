package br.com.dbsoft.message;

import java.util.List;

import org.joda.time.DateTime;

import br.com.dbsoft.util.DBSNumber;

/**
 * @author ricardo.villar
 *
 */
public class DBSMessage {

	public static enum MESSAGE_TYPE{
	   	INFORMATION	(10, "-information"),
	    WARNING		(20, "-warning"),
	    IMPORTANT	(30, "-important"),
	    ERROR		(40, "-error");

	    Integer wCode;
	    String wName;
		
	    public static MESSAGE_TYPE get(Object pCode) {
			Integer xI = DBSNumber.toInteger(pCode);
			if (xI != null){
				return get(xI);
			}else{
				return null;
			}
		}
	    
		public static MESSAGE_TYPE get(Integer pCode) {
			switch (pCode) {
			case 10:
				return MESSAGE_TYPE.INFORMATION;
			case 20:
				return MESSAGE_TYPE.WARNING;
			case 30:
				return MESSAGE_TYPE.IMPORTANT;
			case 40:
				return MESSAGE_TYPE.ERROR;
			}
			return null;
		}
	    
	    MESSAGE_TYPE (Integer pCode, String pName){
	    	wCode = pCode;
	    	wName = pName;
	    }
	    
		public String getName() {
			return wName;
		}
	
		public int getCode() {
			return wCode;
		}
	}
	
	private String			wMessageTextConstructor;
	private String			wMessageText;
	private Boolean			wValidated = null; 
	private MESSAGE_TYPE	wMessageType;
	private Exception		wException;
	private String			wMessageTooltip = "";
	private List<String>	wToUserIds;
	private DateTime		wTime;
	
	public DBSMessage(){}
	
	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText){
		setMessageText(pMessageText);
		setMessageType(pMessageType);
		wMessageTextConstructor = pMessageText;
	}
	
	/**
	 * Retorna chave a chave da mensagem é o próprio texto.
	 * Método existe somente para facilitar a compreensão da origem da chave.
	 * @return
	 */
	public String getMessageKey(){
		return getMessageText(); 
	}
	
	public String getMessageText() {
		return wMessageText;
	}
	public void setMessageText(String pMessageText) {
		wMessageText = pMessageText;
	}

	public MESSAGE_TYPE getMessageType() {
		return wMessageType;
	}
	/**
	 * Mensagens do tipo ERRO e INFORMAÇÃO 
	 * @param pMessageType 
	 */
	public void setMessageType(MESSAGE_TYPE pMessageType) {
		wMessageType = pMessageType;
	}
	
	/**
	 * @return True Null = ainda não validado. True = Validado para verdadeiro. False = Validado para falso 
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
		if (wMessageTextConstructor != null){
			this.setMessageText(String.format(wMessageTextConstructor, pParameters));
		}else{
			this.setMessageText(String.format(this.getMessageText(), pParameters));
		}
	}

	/**
	 * Lista dos usuários que irão receber a mensagem
	 * @return
	 */
	public List<String> getToUserIds() {
		return wToUserIds;
	}

	/**
	 * Lista dos usuários que irão receber a mensagem
	 * @param pToUserIds
	 */
	public void setToUserIds(List<String> pToUserIds) {
		wToUserIds = pToUserIds;
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
