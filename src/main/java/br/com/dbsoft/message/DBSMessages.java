package br.com.dbsoft.message;

import java.util.Collection;
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

	/* (non-Javadoc)
	 * @see br.com.dbsoft.message.IDBSMessages#getMessages()
	 */
	@Override
	public Collection<MessageClass> getMessages(){
		return wMessages.values();
	}
	
//	@Override
//	public Iterator<Entry<String, MessageClass>> iterator(){
//		return wMessages.entrySet().iterator();
//	}
	
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
			xM.copy(pMessage);
			wMessages.put(pMessage.getMessageKey(), xM);
			pvFindNextMessage();
			return xM;
		} catch (InstantiationException | IllegalAccessException e) {
			wLogger.error(e);
		}
		return xM;
	}

	/**
	 * Adiciona todas as mensagems a fila
	 * @param pMessages
	 */
	@Override
	public void addAll(IDBSMessages<MessageClass> pMessages){
		wMessages.values().addAll(pMessages.getMessages());
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
//		if (wMessages.size() > 0){
		if (getCurrentMessageKey() != null){
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
	public Boolean isMessageValidated(String pMessageKey){
		if (wMessages.containsKey(pMessageKey)){
			return wMessages.get(pMessageKey).isMessageValidated();
		}
		return false;
	}
	


	/**
	 * Valida ou invalida a mensagem
	 * Se não for mensagem de warning, automaticamente retira mensagem da fila 
	 * @param pButtonPressed
	 */
	@Override
	public void setMessageValidated(Boolean pIsValidated){
		if (wCurrentMessageKey !=null){
			String xCurrentMessageKey = wCurrentMessageKey;
			//Se não for mensagem de warning, automaticamente retira mensagem da fila
			if (wMessages.get(wCurrentMessageKey).getMessageType() != MESSAGE_TYPE.WARNING ){
				wMessages.remove(wCurrentMessageKey);
			}else {
				//Seta se foi validada e mantém para ser verificada posteriormente pelo usuário
				wMessages.get(wCurrentMessageKey).setMessageValidated(pIsValidated);
			}
			onMessageValidate(xCurrentMessageKey, pIsValidated);
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
	public void onMessageValidate(String pMessageKey, Boolean pIsValidated){}
	
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
	private boolean pvFindMessageType(MESSAGE_TYPE pMessageType){
		for (Entry<String, MessageClass> xM : wMessages.entrySet()) {
			if (xM.getValue().getMessageType().equals(pMessageType)){
				return true;
			}
		}	
		return false;
	}

	@Override
	public MessageClass get(String pMessageKey) {
		if (DBSObject.isEmpty(pMessageKey)){return null;}
		return wMessages.get(pMessageKey);
	}


}
