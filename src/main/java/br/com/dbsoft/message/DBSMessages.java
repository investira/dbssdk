package br.com.dbsoft.message;

import java.util.Iterator;
import java.util.LinkedHashMap;
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
//	private Class<MessageClass>						wMessageClass;
	private String									wFromUserId;
	
//	public DBSMessages(Class<MessageClass> pMessageClass){
//		//Salva class para ser utilizada na criação de uma nova instancia
//		wMessageClass = pMessageClass;
//	}

	@Override
	public LinkedHashMap<String, MessageClass> getMessages(){
		return wMessages;
	}
	
	@Override
	public Iterator<Entry<String, MessageClass>> iterator(){
		return wMessages.entrySet().iterator();
	}
	
	/** Inclui uma mensagem na fila para ser exibida.
	 * @param pMessage
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MessageClass add(MessageClass pMessage){
		MessageClass xM = null;
		try {
			if (pMessage == null){return null;}
			if (wMessages.containsKey(pMessage.getMessageKey())){
				//Exclui ela da fila
				wMessages.remove(pMessage.getMessageKey());
			}
			//Cria nova mensagem do tipo informado
			xM = (MessageClass) pMessage.getClass().newInstance();
			xM.setMessageKey(pMessage.getMessageKey());
			xM.setMessageCode(DBSObject.getNotNull(pMessage.getMessageCode(),0));
			xM.setMessageType(pMessage.getMessageType());
			xM.setMessageText(pMessage.getMessageText());
			xM.setMessageTooltip(DBSObject.getNotNull(pMessage.getMessageTooltip(), ""));
			xM.setMessageTime(pMessage.getMessageTime());
			wMessages.put(pMessage.getMessageKey(), xM);
			pvFindNextMessage();
			return xM;
		} catch (InstantiationException | IllegalAccessException e) {
			wLogger.error(e);
		}
		return xM;
//		return pvCreateMessage(pMessage.getMessageKey(), pMessage.getMessageCode(), pMessage.getMessageType(), pMessage.getMessageText(), pMessage.getMessageTooltip(), pMessage.getMessageTime());
	}
	

//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(DBSIOException pMessage) {
//		return pvCreateMessage(pMessage.getLocalizedMessage(), 0, MESSAGE_TYPE.ERROR, pMessage.getLocalizedMessage(), pMessage.getOriginalException().getLocalizedMessage(), null);
//	}
//
//
//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(MESSAGE_TYPE pMessageType, String pMessageText){
//		return pvCreateMessage(pMessageText, 0, pMessageType, pMessageText, null,  null);
//	}
//	
//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(MESSAGE_TYPE pMessageType, Integer pMessageCode, String pMessageText){
//		return pvCreateMessage(pMessageText, pMessageCode, pMessageType, pMessageText, null,  null);
//	}
//
//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
//		return pvCreateMessage(pMessageText,0, pMessageType, pMessageText, pMessageTooltip,  null);
//	}
//
//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(MESSAGE_TYPE pMessageType, String pMessageText, DateTime pMessageTime){
//		return pvCreateMessage(pMessageText,0, pMessageType, pMessageText, null,  pMessageTime);
//	}
//
//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
//		return pvCreateMessage(pMessageText,0, pMessageType, pMessageText, pMessageTooltip,  pMessageTime);
//	}
//
//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText){
//		return pvCreateMessage(pMessageKey,0, pMessageType, pMessageText, null,  null);
//	}
//	
//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
//		return pvCreateMessage(pMessageKey,0, pMessageType, pMessageText, pMessageTooltip,  null);
//	}
//	
//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, DateTime pMessageTime){
//		return pvCreateMessage(pMessageKey,0, pMessageType, pMessageText, null,  pMessageTime);
//	}
//
//	/** Inclui uma mensagem na fila para ser exibida.
//	 * @param pMessage
//	 */
//	@Override
//	public MessageClass add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pMessageTime){
//		return pvCreateMessage(pMessageKey,0, pMessageType, pMessageText, pMessageTooltip,  pMessageTime);
//	}


	/**
	 * Adiciona todas as mensagems a fila
	 * @param pMessages
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void addAll(IDBSMessages<MessageClass> pMessages) {
		for (Object xMessage : pMessages.getMessages().values()) {
			add((MessageClass) xMessage);
		}
	}



	/**
	 * Remove uma mensagem da fila e reposiciona da próxima
	 * @param pMessageKey
	 */
	@Override
	public void remove(String pMessageKey){
		if (wMessages.containsKey(pMessageKey)){
			wMessages.remove(pMessageKey);
		}
		pvFindNextMessage();
	}
	
	@Override
	public void remove(MessageClass pMessage) {
		remove(pMessage.getMessageKey());
	}

	
	/**
	 * Apaga todas as mensagem da fila 
	 */
	@Override
	public void clear(){
		wMessages.clear();
		pvFindNextMessage();
	}

	
	/**
	 * Retorna a chave da mensagem corrente
	 * @return
	 */
	@Override
	public String getCurrentMessageKey(){
		return wCurrentMessageKey;
	}
	
	/**
	 * Retorna o texto mensagem corrente
	 * @return
	 */
	@Override
	public String getCurrentMessageText(){
		if (wCurrentMessageKey== null){
			return "mensagem não definida";
		}else{
			return wMessages.get(wCurrentMessageKey).getMessageText();
		}
	}


	/**
	 * Retorna o tipo mensagem corrente
	 * @return
	 */
	@Override
	public MESSAGE_TYPE getCurrentMessageType(){
		if (wCurrentMessageKey== null){
			return null;
		}else{
			return wMessages.get(wCurrentMessageKey).getMessageType();
		}
	}
	
	/**
	 * Retorna o tooltip da mensagem corrente
	 * @return
	 */
	@Override
	public String getCurrentMessageTooltip(){
		if (wCurrentMessageKey== null){
			return null;
		}else{
			return wMessages.get(wCurrentMessageKey).getMessageTooltip();
		}
	}
	
	/**
	 * Retorna a mensagem corrente
	 * @return
	 */
	@Override
	public MessageClass getCurrentMessage(){
		if (wCurrentMessageKey== null){
			return null;
		}else{
			return wMessages.get(wCurrentMessageKey);
		}
	}
	

	/**
	 * Retorna se existe mensagem de erro
	 * @return
	 */
	@Override
	public boolean hasErrors(){
		return pvFindMessageType(MESSAGE_TYPE.ERROR);
	}

	/**
	 * Retorna se existe alguma mensagem de alerta
	 * @return
	 */
	@Override
	public boolean hasWarnings(){
		return pvFindMessageType(MESSAGE_TYPE.WARNING);
	}

	/**
	 * Retorna se existe mensagem de informação
	 * @return
	 */
	@Override
	public boolean hasInformations(){
		return pvFindMessageType(MESSAGE_TYPE.INFORMATION);
	}
	
	/**
	 * Retorna se existe alguma mensagem
	 * @return
	 */
	@Override
	public boolean hasMessages(){
		if (wMessages.size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * Retorna se mensagem foi validada.<br/>
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa</li>
	 * <li>null= Ainda não validada</li>
	 * </ul>
	 * 
	 */
	@Override
	public Boolean isValidated(String pMessageKey){
		if (wMessages.containsKey(pMessageKey)){
			return wMessages.get(pMessageKey).isValidated();
		}
		return false;
	}
	


	/**
	 * Valida ou invalida a mensagem
	 * Se não for mensagem de warning, automaticamente retira mensagem da fila 
	 * @param pButtonPressed
	 */
	@Override
	public void setValidated(Boolean pIsValidated){
		if (wCurrentMessageKey !=null){
			String xCurrentMessageKey = wCurrentMessageKey;
			//Se não for mensagem de warning, automaticamente retira mensagem da fila
			if (wMessages.get(wCurrentMessageKey).getMessageType() != MESSAGE_TYPE.WARNING ){
				wMessages.remove(wCurrentMessageKey);
			}else {
				//Seta se foi validada e mantém para ser verificada posteriormente pelo usuário
				wMessages.get(wCurrentMessageKey).setValidated(pIsValidated);
			}
			validated(xCurrentMessageKey, pIsValidated);
			//Procura a próxima mensagem que ainda não foi setada a validação
			pvFindNextMessage();
		}
	}

	
	/**
	 * Método após a validação de qualquer mensagem
	 * @param pMessageKey
	 * @param pIsValidated
	 */
	@Override
	public void validated(String pMessageKey, Boolean pIsValidated){}
	
	/**
	 * Usuário que criou as mensagens
	 * @return
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
	
	//PRIVATE =======================================================================================
	/**
	 * Busca a próxima mensagem que não foi setada a validação
	 */
	private void pvFindNextMessage(){
		wCurrentMessageKey = null;
		for (Entry<String, MessageClass> xM : wMessages.entrySet()) {
			if (xM.getValue().isValidated() == null){
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
	private boolean pvFindMessageType(MESSAGE_TYPE pMessageType){
		for (Entry<String, MessageClass> xM : wMessages.entrySet()) {
			if (xM.getValue().getMessageType().equals(pMessageType)){
				return true;
			}
		}	
		return false;
	}

//	protected MessageClass pvCreateMessage(String pMessageKey, 
//							  Integer pMessageCode, 
//							  MESSAGE_TYPE pMessageType, 
//							  String pMessageText, 
//							  String pMessageTooltip, 
//							  DateTime pMessageTime){
//		if (pMessageKey == null){return null;}
//		MessageClass xM = null;
//		if (wMessages.containsKey(pMessageKey)){
//			//Exclui ela da fila
//			wMessages.remove(pMessageKey);
//		}
//		try {
//			//Cria nova mensagem do tipo informado
//			xM = wMessageClass.newInstance();
//			xM.setMessageKey(pMessageKey);
//			xM.setMessageCode(DBSObject.getNotNull(pMessageCode,0));
//			xM.setMessageType(pMessageType);
//			xM.setMessageText(pMessageText);
//			xM.setMessageTooltip(DBSObject.getNotNull(pMessageTooltip, ""));
//			xM.setMessageTime(pMessageTime);
//			wMessages.put(pMessageKey, xM);
//			pvFindNextMessage();
//		} catch (InstantiationException | IllegalAccessException e) {
//			wLogger.error(e);
//		}
//		return xM;
//	}

}
