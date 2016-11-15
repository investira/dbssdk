package br.com.dbsoft.message;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;
import br.com.dbsoft.util.DBSObject;


/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
public class DBSMessages implements IDBSMessages {

	private static final long serialVersionUID = -8346514168333934241L;

	protected Logger			wLogger = Logger.getLogger(this.getClass());
	
	private LinkedHashMap<String, IDBSMessage> 	wMessages =  new LinkedHashMap<String, IDBSMessage>(); 
	private Set<IDBSMessagesListener> 			wMessagesListeners = new HashSet<IDBSMessagesListener>();
	private String								wFromUserId;
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getMessages()
	 */
	@Override
	public List<IDBSMessage> getListMessage() {
		return new ArrayList<IDBSMessage>(wMessages.values());
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getMessages()
	 */
	@Override
	public Iterator<IDBSMessage> iterator(){
		return wMessages.values().iterator();
	}

	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#addAll(br.com.dbsoft.message.IDBSMessages)
	 */
	@Override
	public void addAll(IDBSMessages pMessages){
		Iterator<IDBSMessage> xMessages = pMessages.iterator();
		while(xMessages.hasNext()){
			add(xMessages.next());
		}
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#add(br.com.dbsoft.message.IDBSMessage)
	 */
	@Override
	public void add(IDBSMessage pMessage){
		add(pMessage, null);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#add(java.lang.String, br.com.dbsoft.message.IDBSMessage)
	 */
	@Override
	public void add(IDBSMessage pMessage, String pSourceId) {
		if (pMessage == null){return;}
//		try {
			IDBSMessage xM = wMessages.get(pMessage.getMessageKey());
			//Se mensagem já existir e estiver validada, exclui da lista para ser reincluida posteriormente abaixo.
			if (xM != null && xM.isMessageValidatedTrue()){
				wMessages.remove(pMessage.getMessageKey());
				xM = null;
			}
			//Se mensagem não existir na fila
			if (xM == null){ 
				//Se mensagem for estárica, cria copia(clone) da mensagem enviada para não afetar a original. 
			    if (Modifier.isStatic(pMessage.getClass().getModifiers())) {
			    	wLogger.warn(pMessage.getMessageKey() + " é uma mensagem static, o que não permite o controle de validação(validate). Envie uma copia ou clone."); 
			    	return;
//					Cria nova mensagem do tipo informado
//					xM = pMessage.getClass().newInstance();
//					Copia dados da mensagem origem
//					xM.copyFrom(pMessage);
			    }else{
			    	xM = pMessage;
			    }
				wMessages.put(xM.getMessageKey(), xM);
				pvFireEventAfterAddMessage(xM);
			}
			if (pSourceId != null){
				//Adicionla sourceId a lista
				xM.getMessageSourceIds().add(pSourceId);
			}
//		} catch (InstantiationException | IllegalAccessException e) {
//			wLogger.error(e);
//		}
	}


	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#remove(java.lang.String)
	 */
	@Override
	public void remove(String pMessageKey){
		if (wMessages.containsKey(pMessageKey)){
			wMessages.remove(pMessageKey);
			pvFireEventAfterRemoveMessage(pMessageKey);
		}
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#remove(br.com.dbsoft.message.IDBSMessage)
	 */
	@Override
	public void remove(IDBSMessage pMessage) {
		remove(pMessage.getMessageKey());
	}

	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#clear()
	 */
	@Override
	public void clear(){
		wMessages.clear();
		pvFireEventAfterClearMessages();
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasErrors()
	 */
	@Override
	public boolean hasErrorsMessages(){
		return pvFindMessageType(MESSAGE_TYPE.ERROR);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasWarnings()
	 */
	@Override
	public boolean hasWarningsMessages(){
		return pvFindMessageType(MESSAGE_TYPE.WARNING);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasInformations()
	 */
	@Override
	public boolean hasInformationsMessages(){
		return pvFindMessageType(MESSAGE_TYPE.INFORMATION);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasMessages()
	 */
	@Override
	public boolean hasMessages(){
		if (wMessages.size() > 0){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getFromUserId()
	 */
	@Override
	public String getMessagesFromUserId() {
		return wFromUserId;
	}

	/**
	 * Usuário que criou as mensagens
	 * @param pFromUserId
	 */
	@Override
	public void setMessagesFromUserId(String pFromUserId) {
		wFromUserId = pFromUserId.trim().toUpperCase();
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#get(java.lang.String)
	 */
	@Override
	public IDBSMessage getMessage(String pMessageKey) {
		if (DBSObject.isEmpty(pMessageKey)){return null;}
		return wMessages.get(pMessageKey);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getMessageForSourceId(java.lang.String)
	 */
	@Override
	public IDBSMessage getMessageForSourceId(String pSourceId) {
		for (Entry<String, IDBSMessage> xM : wMessages.entrySet()) {
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
	public List<IDBSMessage> getMessagesForSourceId(String pSourceId) {
		List<IDBSMessage> xMsgs = new ArrayList<IDBSMessage>();
		for (Entry<String, IDBSMessage> xM : wMessages.entrySet()){
			for (String xSourceId:xM.getValue().getMessageSourceIds()){
				if (DBSObject.isEqual(xSourceId, pSourceId)){
					xMsgs.add(xM.getValue());
					break; //Proxima mensagem
				}
			}
		}
		return xMsgs;
	}

	@Override
	public IDBSMessages addMessagesListener(IDBSMessagesListener pMessagesListener) {
		if (pMessagesListener == null){return this;}
		wMessagesListeners.add(pMessagesListener);
		return this;
	}

	@Override
	public IDBSMessages removeMessagesListener(IDBSMessagesListener pMessagesListener) {
		if (pMessagesListener == null){return this;}
		wMessagesListeners.remove(pMessagesListener);
		return this;
	}

	@Override
	public Set<IDBSMessagesListener> getMessagesListeners() {
		return wMessagesListeners;
	}

	@Override
	public Integer size() {
		return wMessages.size();
	}

	@Override
	public boolean isMessageValidatedTrue(String pMessageKey) {
		if (pMessageKey == null){return false;}
		IDBSMessage xMessage = getMessage(pMessageKey);
		return (xMessage != null && xMessage.isMessageValidatedTrue());
	}
	
	@Override
	public boolean isMessageValidatedTrue(IDBSMessage pMessage) {
		if (pMessage == null){return false;}
		return isMessageValidatedTrue(pMessage.getMessageKey());
	}

	@Override
	public void reset() {
		for (Entry<String, IDBSMessage> xM : wMessages.entrySet()) {
			xM.getValue().reset();
		}	
	}

	//PRIVATE =======================================================================================

	/**
	 * Retorna se existe alguma mensagem do tipo informado
	 * @param pMessageType
	 * @return
	 */
	private boolean pvFindMessageType(MESSAGE_TYPE pMessageType){
		for (Entry<String, IDBSMessage> xM : wMessages.entrySet()) {
			if (xM.getValue().getMessageType().equals(pMessageType)){
				return true;
			}
		}	
		return false;
	}

	private void pvFireEventAfterAddMessage(IDBSMessage pMessage){
		Iterator<IDBSMessagesListener> xI = getMessagesListeners().iterator(); 
		while(xI.hasNext()){
			IDBSMessagesListener xListener = xI.next();
			xListener.afterAddMessage(pMessage);
		}
	}

	private void pvFireEventAfterRemoveMessage(String pMessageKey){
		Iterator<IDBSMessagesListener> xI = getMessagesListeners().iterator(); 
		while(xI.hasNext()){
			IDBSMessagesListener xListener = xI.next();
			xListener.afterRemoveMessage(pMessageKey);
		}
	}
	
	private void pvFireEventAfterClearMessages(){
		Iterator<IDBSMessagesListener> xI = getMessagesListeners().iterator(); 
		while(xI.hasNext()){
			IDBSMessagesListener xListener = xI.next();
			xListener.afterClearMessages();
		}
	}

}
