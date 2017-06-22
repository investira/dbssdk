package br.com.dbsoft.message;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import org.apache.log4j.Logger;

import br.com.dbsoft.util.DBSObject;


/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
/**
 * @author ricardo.villar
 *
 */
public class DBSMessages implements IDBSMessages{

	private static final long serialVersionUID = -8346514168333934241L;

	protected Logger			wLogger = Logger.getLogger(this.getClass());
	
	private LinkedHashMap<String, IDBSMessage> 	wMessages = new LinkedHashMap<String, IDBSMessage>();
	private LinkedHashMap<String, IDBSMessage> 	wMessagesValidated =  new LinkedHashMap<String, IDBSMessage>();
	private Set<IDBSMessagesListener> 			wMessagesListeners = new HashSet<IDBSMessagesListener>();
	private String								wFromUserId;
	private Boolean								wIsController = false;
	private String								wCurrentMessageKey = null;
	
//	public String wChave;
	
	public DBSMessages() {}
	
	/**
	 * @param pController Indica quando irá manter a lista principal com somente as mensagens não validadas. 
	 */
	public DBSMessages(Boolean pIsController) {
		wIsController = pIsController;
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getMessages()
	 */
	@Override
	public List<IDBSMessage> getListMessage() {
		return new ArrayList<IDBSMessage>(wMessages.values());
	}
	
	@Override
	public List<DBSMessage> getListMessage2() {
		List<DBSMessage> xList = new ArrayList<DBSMessage>();
		for (IDBSMessage xMsg : wMessages.values()) {
			xList.add((DBSMessage)xMsg);
		}
		return xList;
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
		if (pMessages == null){return;}
		Iterator<IDBSMessage> xMessages = pMessages.iterator();
		while(xMessages.hasNext()){
			add(xMessages.next());
		}
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#addAll(java.util.List)
	 */
	@Override
	public void addAll(List<DBSMessage> pMessages) {
		if (pMessages == null){return;}
		for (IDBSMessage xMessage: pMessages){
			add(xMessage);
		}
	}
	
	@Override
	public void addAllMessageBase(List<IDBSMessageBase> pMessages) {
		if (pMessages == null){return;}
		for (IDBSMessageBase xMessageBase: pMessages){
			add(xMessageBase);
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

		//Recupera mensagem validada se já existir
		IDBSMessage xM = wMessagesValidated.get(pMessage.getMessageKey());
		//Se mensagem já existir e estiver validada, exclui da lista para ser reincluida após eventual nova validação.
		if (xM != null){
			pMessage.reset();
			wMessagesValidated.remove(pMessage.getMessageKey());
			xM = null;
		//Recupera mensagem se já existir
		}else{
			xM = wMessages.get(pMessage.getMessageKey());
		}
		//Se mensagem não existir na fila
		if (xM == null){ 
			//Se mensagem for estárica, cria copia(clone) da mensagem enviada para não afetar a original. 
		    if (Modifier.isStatic(pMessage.getClass().getModifiers())) {
		    	wLogger.warn(pMessage.getMessageKey() + " é uma mensagem static, o que não permite o controle de validação(validate). Envie uma copia ou clone."); 
		    	return;
		    }else{
		    	xM = pMessage;
		    }
		    if (wIsController){
			    xM.addMessageListener(this);
		    }
			wMessages.put(xM.getMessageKey(), xM);
			pvFindNextMessage();
			pvFireEventAfterAddMessage(xM);
		}
		if (pSourceId != null){
			//Adicionla sourceId a lista
			xM.getMessageSourceIds().add(pSourceId);
		}
	}
	
	@Override
	public void add(IDBSMessageBase pMessageBase) {
		add(pMessageBase, null);
	}
	
	@Override
	public void add(IDBSMessageBase pMessageBase, String pSourceId) {
		add(new DBSMessage(pMessageBase), pSourceId);
	}


	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#remove(java.lang.String)
	 */
	@Override
	public void remove(String pMessageKey){
		if (wMessages.containsKey(pMessageKey)){
			wMessages.remove(pMessageKey);
			pvFindNextMessage();
			pvFireEventAfterRemoveMessage(pMessageKey);
		}else if(wMessagesValidated.containsKey(pMessageKey)){
			wMessagesValidated.remove(pMessageKey);
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
		wMessagesValidated.clear();
		pvFindNextMessage();
		pvFireEventAfterClearMessages();
	}

	@Override
	public Integer size() {
		return wMessages.size() + wMessagesValidated.size();
	}

	@Override
	public void reset() {
		//Transfere mensagens validadas para as mensagens não validadas.
		for (Entry<String, IDBSMessage> xM : wMessagesValidated.entrySet()) {
			add(xM.getValue());
		}
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasMessages()
	 */
	@Override
	public Boolean hasMessages(){
		if (wMessages != null 
		 && wMessages.size() > 0){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasErrors()
	 */
	@Override
	public Boolean hasErrorsMessages(){
		return pvFindMessageType(FacesMessage.SEVERITY_ERROR);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasWarnings()
	 */
	@Override
	public Boolean hasWarningsMessages(){
		return pvFindMessageType(FacesMessage.SEVERITY_WARN);
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#hasInformations()
	 */
	@Override
	public Boolean hasInformationsMessages(){
		return pvFindMessageType(FacesMessage.SEVERITY_INFO);
	}

	@Override
	public Boolean hasFatalsMessages() {
		return pvFindMessageType(FacesMessage.SEVERITY_FATAL);
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
		return pvGetMessage(pMessageKey);
	}
	
	@Override
	public IDBSMessage getMessage(IDBSMessage pMessage) {
		if (pMessage == null){return null;}
		return getMessage(pMessage.getMessageKey());
	}

	@Override
	public IDBSMessage getCurrentMessage() {
		if (wCurrentMessageKey==null){
			return null;
		}
		return wMessages.get(wCurrentMessageKey);
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
	public boolean isMessageValidatedTrue(String pMessageKey) {
		if (pMessageKey == null){return false;}
		IDBSMessage xMessage = wMessagesValidated.get(pMessageKey);
		return (xMessage != null && xMessage.isMessageValidatedTrue());
	}
	
	@Override
	public boolean isMessageValidatedTrue(IDBSMessage pMessage) {
		if (pMessage == null){return false;}
		return isMessageValidatedTrue(pMessage.getMessageKey());
	}

	

	@Override
	public boolean isAllMessagesValidatedTrue() {
		if (wMessages.size() > 0){return false;}
		for (Entry<String, IDBSMessage> xM : wMessagesValidated.entrySet()) {
			if (!xM.getValue().isMessageValidatedTrue()){ //Não validada como true.
				return false;
			}
		}	
		return true;
	}

	/**
	 * Disparado após mensagem ser validada.
	 * @param pMessage
	 */
	@Override
	public void afterMessageValidated(IDBSMessage pMessage) {
		if (pMessage.isMessageValidated() != null){
			//Transfere mensagem para a lista de mensagens validadas.
			wMessages.remove(pMessage.getMessageKey());
			wMessagesValidated.put(pMessage.getMessageKey(), pMessage);
		}
		pvFireEventAfterMessageValidated(pMessage);
	}

	//PRIVATE =======================================================================================


	/**
	 * Retorna se existe alguma mensagem do tipo informado
	 * @param pMessageType
	 * @return
	 */
	private boolean pvFindMessageType(Severity pSeverity){
		for (Entry<String, IDBSMessage> xM : wMessages.entrySet()) {
			if (xM.getValue().getMessageType().getSeverity().equals(pSeverity)
			 && xM.getValue().isMessageValidated() == null){ //Ainda não validade. Não é true nem false.
				return true;
			}
		}	
		return false;
	}

	private void pvFireEventAfterAddMessage(IDBSMessage pMessage){
		Iterator<IDBSMessagesListener> xI = getMessagesListeners().iterator(); 
		while(xI.hasNext()){
			IDBSMessagesListener xListener = xI.next();
			xListener.afterAddMessage(this, pMessage);
		}
	}

	private void pvFireEventAfterRemoveMessage(String pMessageKey){
		Iterator<IDBSMessagesListener> xI = getMessagesListeners().iterator(); 
		while(xI.hasNext()){
			IDBSMessagesListener xListener = xI.next();
			xListener.afterRemoveMessage(this, pMessageKey);
		}
	}
	
	private void pvFireEventAfterClearMessages(){
		Iterator<IDBSMessagesListener> xI = getMessagesListeners().iterator(); 
		while(xI.hasNext()){
			IDBSMessagesListener xListener = xI.next();
			xListener.afterClearMessages(this);
		}
	}
	
	private void pvFireEventAfterMessageValidated(IDBSMessage pMessage){
		Iterator<IDBSMessagesListener> xI = getMessagesListeners().iterator();
		while(xI.hasNext()){
			IDBSMessagesListener xListener = xI.next();
			xListener.afterMessageValidated(this, pMessage);
//			System.out.println("(DBSMessages)pvFireEventAfterMessageValidated\t" + xListener.toString());
		}
	}
	/**
	 * @param pMessageKey
	 * @return Mensagem independentemente se é uma mensagem validada(true ou false) ou não(null).
	 */
	private IDBSMessage pvGetMessage(String pMessageKey){
		IDBSMessage xMsg = wMessagesValidated.get(pMessageKey);
		if (xMsg == null){
			xMsg = wMessages.get(pMessageKey);
		}
		return xMsg;
	}
	
	/**
	 * Busca a próxima mensagem que não foi setada a validação.
	 */
	private void pvFindNextMessage(){
		wCurrentMessageKey = null;
		for (Entry<String, IDBSMessage> xM : wMessages.entrySet()) {
			if (xM.getValue().isMessageValidated() == null){ //Ainda não validade. Não é true nem false.
				wCurrentMessageKey = xM.getValue().getMessageKey();
			}
		}	
	}


}
