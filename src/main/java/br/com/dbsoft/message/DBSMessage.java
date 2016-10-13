package br.com.dbsoft.message;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSObject;

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
	private Set<String>	wMessageClientIds = new HashSet<String>();
	
	//Construtores============================
	public DBSMessage(){}

	public DBSMessage(DBSIOException e){
		pvSetMessage(e.getLocalizedMessage(), 0, MESSAGE_TYPE.ERROR, e.getLocalizedMessage(), null, null);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageText, 0, pMessageType, pMessageText, null,  null);
	}
	
	public DBSMessage(MESSAGE_TYPE pMessageType, Integer pMessageCode, String pMessageText){
		pvSetMessage(pMessageText, pMessageCode, pMessageType, pMessageText, null, null);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
		pvSetMessage(pMessageText,0, pMessageType, pMessageText, pMessageTooltip, null);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText, DateTime pMessageTime){
		pvSetMessage(pMessageText,0, pMessageType, pMessageText, null, pMessageTime);
	}

	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
		pvSetMessage(pMessageText,0, pMessageType, pMessageText, pMessageTooltip, pMessageTime);
	}

	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, null, null);
	}
	
	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, pMessageTooltip, null);
	}
	
	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, DateTime pMessageTime){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, null, pMessageTime);
	}

	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText, pMessageTooltip, pMessageTime);
	}

	//=========================================
	
	@Override
	public String getMessageKey(){return wMessageKey; }

	@Override
	public void setMessageKey(String pMessageKey){
		if (pMessageKey != null){
			wMessageKey = pMessageKey.trim();
		}else{
			wMessageKey = pMessageKey;
		}
	}
	
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
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageCode(java.lang.Integer)
	 */
	@Override
	public void setMessageCode(Integer pMessageCode) {wMessageCode = pMessageCode;}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#isMessageValidated()
	 */
	@Override
	public Boolean isMessageValidated() {return wValidated;}
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageValidated(java.lang.Boolean)
	 */
	@Override
	public void setMessageValidated(Boolean validated) {wValidated = validated;}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getException()
	 */
	@Override
	public Exception getException() {return wException;}
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setException(java.lang.Exception)
	 */
	@Override
	public void setException(Exception pException) {this.wException = pException;}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getMessageTooltip()
	 */
	@Override
	public String getMessageTooltip() {return wMessageTooltip;}
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageTooltip(java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getMessageTime()
	 */
	@Override
	public DateTime getMessageTime() {return wTime;}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageTime(org.joda.time.DateTime)
	 */
	@Override
	public void setMessageTime(DateTime pTime) {wTime = pTime;}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getIds()
	 */
	@Override
	public Set<String> getMessageClientIds() {
		return wMessageClientIds;
	}
	


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

	@Override
	public void copy(IDBSMessage pMessage){
		if (pMessage == null){return;}
		setMessageCode(DBSObject.getNotNull(pMessage.getMessageCode(),0));
		setMessageKey(pMessage.getMessageKey());
		setMessageText(pMessage.getMessageText());
		setMessageTime(pMessage.getMessageTime());
		setMessageTooltip(DBSObject.getNotNull(pMessage.getMessageTooltip(),""));
		setMessageType(pMessage.getMessageType());
		setMessageValidated(pMessage.isMessageValidated());
		getMessageClientIds().clear();
		getMessageClientIds().addAll(pMessage.getMessageClientIds());
	}


}
