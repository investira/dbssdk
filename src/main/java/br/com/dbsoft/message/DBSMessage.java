package br.com.dbsoft.message;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSIO;
import br.com.dbsoft.util.DBSObject;

/**
 * @author ricardo.villar
 *
 */
@XmlRootElement
public class DBSMessage extends DBSMessageBase implements IDBSMessage{

	private static final long serialVersionUID = 2176781176871000385L;

	private String						messageTextOriginal;
	private Boolean						validated = null; 
	private Exception					exception;
	private String						messageTooltip = "";
	private String						messageKey = null;
	private Set<String>					messageSourceIds = new HashSet<String>();
	private Set<IDBSMessageListener> 	messageListeners = new HashSet<IDBSMessageListener>();
	
	
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
	public void setMessageText(String pMessageText) {
		//Seta a chave como o próprio texto caso não tenha seja nula.
		if (messageKey == null){
			setMessageKey(pMessageText);
		}
		super.setMessageText(pMessageText);
	}
	
	@Override
	public String getMessageKey(){return messageKey; }

	@Override
	public void setMessageKey(String pMessageKey){
		if (pMessageKey != null){
			messageKey = pMessageKey.trim();
		}else{
			messageKey = pMessageKey;
		}
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#isMessageValidated()
	 */
	@Override
	public Boolean isMessageValidated() {return validated;}
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageValidated(java.lang.Boolean)
	 */
	@Override
	public void setMessageValidated(Boolean pValidated) {
		validated = pValidated;
		pvFireEventAfterMessageValidated();
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#isMessageValidatedTrue()
	 */
	@Override
	public boolean isMessageValidatedTrue() {
		return DBSObject.getNotNull(isMessageValidated(), false);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getMessageTooltip()
	 */
	@Override
	public String getMessageTooltip() {return messageTooltip;}
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setMessageTooltip(java.lang.String)
	 */
	@Override
	public void setMessageTooltip(String pMessageTooltip) {this.messageTooltip = pMessageTooltip;}
	
	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	@Override
	public void setMessageTextParameters(Object... pParameters){
		if (messageTextOriginal != null){
			this.setMessageText(String.format(messageTextOriginal, pParameters));
		}else{
			this.setMessageText(String.format(getMessageText(), pParameters));
		}
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getIds()
	 */
	@Override
	public Set<String> getMessageSourceIds() {
		return messageSourceIds;
	}
	


	@Override
	public IDBSMessage addMessageListener(IDBSMessageListener pMessageListener) {
		if (pMessageListener == null){return this;}
		messageListeners.add(pMessageListener);
		return this;
	}

	@Override
	public IDBSMessage removeMessageListener(IDBSMessageListener pMessageListener) {
		if (pMessageListener == null){return this;}
		messageListeners.remove(pMessageListener);
		return this;
	}

	@Override
	public Set<IDBSMessageListener> getMessageListeners() {
		return messageListeners;
	}

	@Override
	public void copyFrom(IDBSMessage pMessage){
		if (pMessage == null 
         || pMessage.equals(this)){return;}
		DBSIO.copyDataModelFieldsValue(pMessage, this);
		messageSourceIds = new HashSet<String>();
		messageSourceIds.addAll(pMessage.getMessageSourceIds());
		messageListeners = new HashSet<IDBSMessageListener>();
		messageListeners.addAll(pMessage.getMessageListeners());
	}

	@Override
	public boolean equals(IDBSMessage pSourceMessage) {
		return equals(pSourceMessage.getMessageKey());
	}

	@Override
	public boolean equals(String pMessageKey) {
		return DBSObject.isEqual(this.getMessageKey(), pMessageKey);
	}
	
	@Override
	public IDBSMessage clone(){
		try {
			IDBSMessage xM = this.getClass().newInstance(); 
			xM.copyFrom(this);
			return xM;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void reset() {
		validated = null;
	}


	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#getException()
	 */
	@Override
	public Exception getException() {return exception;}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessage#setException(java.lang.Exception)
	 */
	@Override
	public void setException(Exception pException) {this.exception = pException;}

	//PROTECTED =========================
	protected void pvSetMessage(String pMessageKey, Integer pMessageCode, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
		setMessageKey(pMessageKey);
		setMessageCode(pMessageCode);
		setMessageType(pMessageType);
		setMessageText(pMessageText);
		setMessageTooltip(pMessageTooltip);
		setMessageTime(pMessageTime);
		messageTextOriginal = pMessageText;
	}

	//PRIVATE =========================
	/**
	 * Dispara evento informando que mensagem foi validada.
	 */
	private void pvFireEventAfterMessageValidated(){
		Iterator<IDBSMessageListener> xI = getMessageListeners().iterator(); 
		while(xI.hasNext()){
			IDBSMessageListener xListener = xI.next();
			xListener.afterMessageValidated(this);
//			System.out.println("pvFireEventAfterMessageValidated\t" + xListener.toString());
		}
		messageListeners.clear();
	}



}
