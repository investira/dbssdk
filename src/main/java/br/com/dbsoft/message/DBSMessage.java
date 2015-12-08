package br.com.dbsoft.message;


import org.joda.time.DateTime;

import br.com.dbsoft.error.DBSIOException;

/**
 * @author ricardo.villar
 *
 */
public class DBSMessage implements IDBSMessage{

	private String			wMessageTextOriginal;
	private String			wMessageText;
	private Boolean			wValidated = null; 
	private MESSAGE_TYPE	wMessageType;
	private Exception		wException;
	private String			wMessageTooltip = "";
	private DateTime		wTime;
	private String			wMessageKey = null;
	private Integer			wMessageCode = 0;
	
	//Construtores============================
	public DBSMessage(){}

	public DBSMessage(DBSIOException e){
		pvSetMessage(e.getLocalizedMessage(), 0, MESSAGE_TYPE.ERROR, e.getLocalizedMessage(), null,  null);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageText, 0, pMessageType, pMessageText, null,  null);
	}
	
	public DBSMessage(MESSAGE_TYPE pMessageType, Integer pMessageCode, String pMessageText){
		pvSetMessage(pMessageText, pMessageCode, pMessageType, pMessageText, null,  null);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
		pvSetMessage(pMessageText,0, pMessageType, pMessageText, pMessageTooltip,  null);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText, DateTime pMessageTime){
		pvSetMessage(pMessageText,0, pMessageType, pMessageText, null,  pMessageTime);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
		pvSetMessage(pMessageText,0, pMessageType, pMessageText, pMessageTooltip,  pMessageTime);
	}

	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, null,  null);
	}
	
	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, pMessageTooltip,  null);
	}
	
	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, DateTime pMessageTime){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, null,  pMessageTime);
	}

	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, pMessageTooltip,  pMessageTime);
	}

	//=========================================
	
	@Override
	public String getMessageKey(){return wMessageKey; }

	@Override
	public void setMessageKey(String pMessageKey){wMessageKey = pMessageKey; }
	
	@Override
	public String getMessageText() {return wMessageText;}
	
	@Override
	public void setMessageText(String pMessageText) {
		//Seta a chave como o próprio texto caso não tenha seja nula.
		if (wMessageKey == null){
			setMessageKey(pMessageText);
		}
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
	@Override
	public void setMessageCode(Integer pMessageCode) {wMessageCode = pMessageCode;}
	
	/**
	 * @return Null = ainda não validada.<br/> True = Validada para verdadeiro.<br/> False = Validada para falso 
	 */
	@Override
	public Boolean isValidated() {return wValidated;}
	@Override
	public void setValidated(Boolean validated) {wValidated = validated;}

	@Override
	public Exception getException() {return wException;}
	/**
	 * Configura a exception vinculada a mensagem caso exista
	 * @param pException
	 */
	@Override
	public void setException(Exception pException) {this.wException = pException;}
	
	@Override
	public String getMessageTooltip() {return wMessageTooltip;}
	@Override
	public void setMessageTooltip(String pMessageTooltip) {this.wMessageTooltip = pMessageTooltip;}
	
	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	@Override
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
	@Override
	public DateTime getMessageTime() {return wTime;}

	/**
	 * Horário que a mensagem foi criada
	 */
	@Override
	public void setMessageTime(DateTime pTime) {wTime = pTime;}


	//PRIVATE =========================
	protected void pvSetMessage(String pMessageKey, Integer pMessageCode, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
		setMessageKey(pMessageKey);
		setMessageCode(pMessageCode);
		setMessageType(pMessageType);
		setMessageText(pMessageText);
		setMessageTooltip(pMessageTooltip);
		setMessageTime(pMessageTime);
		wMessageTextOriginal = pMessageText;
	}


}
