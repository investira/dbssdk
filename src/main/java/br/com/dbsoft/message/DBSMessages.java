package br.com.dbsoft.message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;
import br.com.dbsoft.util.DBSObject;


/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
public class DBSMessages<MessageClass extends IDBSMessage> implements IDBSMessages<MessageClass> {

	protected Logger			wLogger = Logger.getLogger(this.getClass());
	
	protected LinkedHashMap<String, MessageClass> 	wMessages =  new LinkedHashMap<String, MessageClass>(); 
	protected String 								wCurrentMessageKey;
	private String									wFromUserId;
	

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getMessages()
	 */
	@Override
	public List<MessageClass> getMessages(){
		return new ArrayList<MessageClass>(wMessages.values());
	}

	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#add(br.com.dbsoft.message.IDBSMessage)
	 */
	@Override
	public MessageClass add(MessageClass pMessage){
		return add(pMessage, null);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#addAll(br.com.dbsoft.message.IDBSMessages)
	 */
	@Override
	public void addAll(IDBSMessages<MessageClass> pMessages){
		wMessages.values().addAll(pMessages.getMessages());
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#add(java.lang.String, br.com.dbsoft.message.IDBSMessage)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MessageClass add(MessageClass pMessage, String pSourceId) {
		if (pMessage == null){return null;}
		try {
			MessageClass xM = wMessages.get(pMessage.getMessageKey());
			//Se mensagem não existir na fila
			if (xM == null){
				//Cria nova mensagem do tipo informado
				xM = (MessageClass) pMessage.getClass().newInstance();
				//Copia dados da mensagem origem
				xM.copy(pMessage);
				wMessages.put(pMessage.getMessageKey(), xM);
			}
			if (pSourceId != null){
				//Inclui o sourceId na lista
				xM.getMessageSourceIds().add(pSourceId);
			}
			pvFindNextMessage();
			return xM;
		} catch (InstantiationException | IllegalAccessException e) {
			wLogger.error(e);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#remove(java.lang.String)
	 */
	@Override
	public void remove(String pMessageKey){
		if (wMessages.containsKey(pMessageKey)){
			wMessages.remove(pMessageKey);
		}
		pvFindNextMessage();
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#remove(br.com.dbsoft.message.IDBSMessage)
	 */
	@Override
	public void remove(MessageClass pMessage) {
		remove(pMessage.getMessageKey());
	}

	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#clear()
	 */
	@Override
	public void clear(){
		wMessages.clear();
		pvFindNextMessage();
	}

	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getCurrentMessageKey()
	 */
	@Override
	public String getCurrentMessageKey(){
		return wCurrentMessageKey;
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getCurrentMessageText()
	 */
	@Override
	public String getCurrentMessageText(){
		if (wCurrentMessageKey== null){
			return "mensagem não definida";
		}else{
			return wMessages.get(wCurrentMessageKey).getMessageText();
		}
	}


	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getCurrentMessageType()
	 */
	@Override
	public MESSAGE_TYPE getCurrentMessageType(){
		if (wCurrentMessageKey== null){
			return null;
		}else{
			return wMessages.get(wCurrentMessageKey).getMessageType();
		}
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getCurrentMessageTooltip()
	 */
	@Override
	public String getCurrentMessageTooltip(){
		if (wCurrentMessageKey== null){
			return null;
		}else{
			return wMessages.get(wCurrentMessageKey).getMessageTooltip();
		}
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getCurrentMessage()
	 */
	@Override
	public MessageClass getCurrentMessage(){
		if (wCurrentMessageKey== null){
			return null;
		}else{
			return wMessages.get(wCurrentMessageKey);
		}
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasErrors()
	 */
	@Override
	public boolean hasErrors(){
		return pvFindMessageType(MESSAGE_TYPE.ERROR);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasWarnings()
	 */
	@Override
	public boolean hasWarnings(){
		return pvFindMessageType(MESSAGE_TYPE.WARNING);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasInformations()
	 */
	@Override
	public boolean hasInformations(){
		return pvFindMessageType(MESSAGE_TYPE.INFORMATION);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasMessages()
	 */
	@Override
	public boolean hasMessages(){
//		if (wMessages.size() > 0){
		if (getCurrentMessageKey() != null){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#isMessageValidated(java.lang.String)
	 */
	@Override
	public Boolean isMessageValidatedTrue(String pMessageKey){
		if (wMessages.containsKey(pMessageKey)){
			return wMessages.get(pMessageKey).isMessageValidatedTrue();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#isMessageValidated(java.lang.String)
	 */
	@Override
	public Boolean isMessageValidated(String pMessageKey){
		if (wMessages.containsKey(pMessageKey)){
			return wMessages.get(pMessageKey).isMessageValidated();
		}
		return false;
	}
	

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#setMessageValidated(java.lang.Boolean)
	 */
	@Override
	public String setMessageValidated(Boolean pIsValidated){
		return setMessageValidated(wCurrentMessageKey, pIsValidated);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#setMessageValidated(br.com.dbsoft.message.IDBSMessage)
	 */
	@Override
	public String setMessageValidated(MessageClass pMessage) {
		if (pMessage != null){
			return setMessageValidated(pMessage.getMessageKey(), pMessage.isMessageValidated());
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#setMessageValidated(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public String setMessageValidated(String pMessageKey, Boolean pIsValidated) {
		String pReturnView = null;
		if (pMessageKey !=null){
			MessageClass xMsg = get(pMessageKey);
			if (xMsg != null){
				xMsg.setMessageValidated(pIsValidated);
				//Se não for mensagem de warning, automaticamente retira mensagem da fila
				if (xMsg.getMessageType() != MESSAGE_TYPE.WARNING ){
					wMessages.remove(pMessageKey);
				}
				//Procura a próxima mensagem que ainda não foi setada a validação
				pvFindNextMessage();
			}
		}
		return pReturnView;
	}


	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getFromUserId()
	 */
	@Override
	public String getFromUserId() {
		return wFromUserId;
	}

	/**
	 * Usuário que criou as mensagens
	 * @param pFromUserId
	 */
	@Override
	public void setFromUserId(String pFromUserId) {
		wFromUserId = pFromUserId.trim().toUpperCase();
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#get(java.lang.String)
	 */
	@Override
	public MessageClass get(String pMessageKey) {
		if (DBSObject.isEmpty(pMessageKey)){return null;}
		return wMessages.get(pMessageKey);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getMessageForSourceId(java.lang.String)
	 */
	@Override
	public MessageClass getMessageForSourceId(String pSourceId) {
		for (Entry<String, MessageClass> xM : wMessages.entrySet()) {
			for (String xSourceId:xM.getValue().getMessageSourceIds()){
				if (DBSObject.isEqual(xSourceId, pSourceId)){
					return xM.getValue();
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getMessagesForSourceId(java.lang.String)
	 */
	@Override
	public List<MessageClass> getMessagesForSourceId(String pSourceId) {
		List<MessageClass> xMsgs = new ArrayList<MessageClass>();
		for (Entry<String, MessageClass> xM : wMessages.entrySet()){
			for (String xSourceId:xM.getValue().getMessageSourceIds()){
				if (DBSObject.isEqual(xSourceId, pSourceId)){
					xMsgs.add(xM.getValue());
					break; //Proxima mensagem
				}
			}
		}
		return xMsgs;
	}


	//PRIVATE =======================================================================================
	/**
	 * Busca a próxima mensagem que não foi setada a validação
	 */
	private void pvFindNextMessage(){
		wCurrentMessageKey = null;
		for (Entry<String, MessageClass> xM : wMessages.entrySet()) {
			//Mensagem ainda não validade com true ou false
			if (xM.getValue().isMessageValidated() == null){
				wCurrentMessageKey =  xM.getKey();
				break;
			}
		}
	}


	/**
	 * Retorna se existe alguma mensagem do tipo informado
	 * @param pMessageType
	 * @return
	 */
	/**
	 * @param pMessageType
	 * @return
	 */
	private boolean pvFindMessageType(MESSAGE_TYPE pMessageType){
		for (Entry<String, MessageClass> xM : wMessages.entrySet()) {
			if (xM.getValue().getMessageType().equals(pMessageType)){
				return true;
			}
		}	
		return false;
	}





}
