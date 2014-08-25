package br.com.dbsoft.message;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessage.MESSAGE_TYPE;
import br.com.dbsoft.util.DBSObject;


/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
public class DBSMessages<MessageClass extends DBSMessage>  {

	protected Logger			wLogger = Logger.getLogger(this.getClass());
	
	protected LinkedHashMap<String, MessageClass> 	wMessages =  new LinkedHashMap<String, MessageClass>(); 
	protected String 								wCurrentMessageKey;
	private Class<MessageClass>						wMessageClass;
	private String									wFromUserId;
	
	public DBSMessages(Class<MessageClass> pMessageClass){
		//Salva class para ser utilizada na criação de uma nova instancia
		wMessageClass = pMessageClass;
	}

	public LinkedHashMap<String, MessageClass> getMessages(){
		return wMessages;
	}
	
	public Iterator<Entry<String, MessageClass>> iterator(){
		return wMessages.entrySet().iterator();
	}
	
	public void add(DBSIOException e){
		add(MESSAGE_TYPE.ERROR, e.getLocalizedMessage(), e.getOriginalException().getLocalizedMessage());
	}
	
	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A exibição se derá na mesma ondem da inclusão
	 * @param pMessageKey Chave da mensagem que será utilizada para verificar a resposta do usuário
	 * @param pMessageType pTipo de mensaagem
	 * @param pMessageText Texto da mensagem
	 * @param pMessageTooltip Texto adicional a mensagem para se utilizado como diga
	 */
	public synchronized void add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pTime){
		//Caso a mensagem já exista, o que indica que já pode ter sido exibida(validada)...
		if (wMessages.containsKey(pMessageKey)){
			//Exclui ela da fila
			wMessages.remove(pMessageKey);
		}
		try {
			//Cria nova mensagem do tipo informado
			MessageClass xM = wMessageClass.newInstance();
			xM.setMessageText(pMessageText);
			xM.setMessageType(pMessageType);
			xM.setMessageTooltip(DBSObject.getNotNull(pMessageTooltip, ""));
			xM.setTime(pTime);
			wMessages.put(pMessageKey, xM);
			pvFindNextMessage();
		} catch (InstantiationException | IllegalAccessException e) {
			wLogger.error(e);
		}
	}
	
	/** Inclui uma mensagem na fila para ser exibida.
	 * @param pMessage
	 */
	public void add(MessageClass pMessage){
		this.add(pMessage.getMessageText(), pMessage.getMessageType(), pMessage.getMessageText(), pMessage.getMessageTooltip(), pMessage.getTime());
	}

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A exibição se derá na mesma ondem da inclusão
	 * @param pMessageKey Chave da mensagem que será utilizada para verificar a resposta do usuário
	 * @param pMessageType pTipo de mensaagem
	 * @param pMessageText Texto da mensagem
	 */
	public void add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
//		add(pMessageKey, pMessageType, pMessageText, pMessageTooltip, null);
		add(pMessageKey, pMessageType, pMessageText, pMessageTooltip, null);
	}

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A exibição se derá na mesma ondem da inclusão
	 * @param pMessageKey Chave da mensagem que será utilizada para verificar a resposta do usuário
	 * @param pMessageType pTipo de mensaagem
	 * @param pMessageText Texto da mensagem
	 */
	public void add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText){
		add(pMessageKey, pMessageType, pMessageText, "", null);
	}
	
	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A exibição se derá na mesma ondem da inclusão
	 * @param pMessageKey Chave da mensagem que será utilizada para verificar a resposta do usuário
	 * @param pMessageType pTipo de mensaagem
	 * @param pMessageText Texto da mensagem
	 */
	public void add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, DateTime pTime){
		add(pMessageKey, pMessageType, pMessageText, "", pTime);
	}
	
	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A chave da mensagem é o próprio texto
	 * @param pMessageType
	 * @param pMessageText
	 */
	public void add(MESSAGE_TYPE pMessageType, String pMessageText){
		add(pMessageType, pMessageText, null, null);
	}

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A chave da mensagem é o próprio texto
	 * @param pMessageType
	 * @param pMessageText
	 */
	public void add(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
		add(pMessageType, pMessageText, pMessageTooltip, null);
	}

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A chave da mensagem é o próprio texto
	 * @param pMessageType
	 * @param pMessageText
	 */
	public void add(MESSAGE_TYPE pMessageType, String pMessageText, DateTime pTime){
		add(pMessageType, pMessageText, null, pTime);
	}

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A chave da mensagem é o próprio texto
	 * @param pMessageType
	 * @param pMessageText
	 */
	public void add(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pTime){
		add(pMessageText, pMessageType, pMessageText, pMessageTooltip, pTime);
	}
	

	/**
	 * Adiciona todas as mensagems a fila
	 * @param pMessages
	 */
	public synchronized <M extends DBSMessages<?>> void addAll(M pMessages){
		for (Object xM : pMessages.getMessages().values()) {
			DBSMessage xMsg = (DBSMessage) xM;
			add(xMsg.getMessageText(), xMsg.getMessageType(), xMsg.getMessageText(), xMsg.getMessageTooltip(), xMsg.getTime());
		}
	}

	/**
	 * Remove uma mensagem da fila e reposiciona da próxima
	 * @param pMessageKey
	 */
	public void remove(String pMessageKey){
		if (wMessages.containsKey(pMessageKey)){
			wMessages.remove(pMessageKey);
		}
		pvFindNextMessage();
	}
	
	
	/**
	 * Apaga todas as mensagem da fila 
	 */
	public void clear(){
		wMessages.clear();
		pvFindNextMessage();
	}

	
	/**
	 * Retorna a chave da mensagem corrente
	 * @return
	 */
	public String getCurrentMessageKey(){
		return wCurrentMessageKey;
	}
	
	/**
	 * Retorna o texto mensagem corrente
	 * @return
	 */
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
	public boolean hasErrors(){
		return pvFindMessageType(MESSAGE_TYPE.ERROR);
	}

	/**
	 * Retorna se existe alguma mensagem de alerta
	 * @return
	 */
	public boolean hasWarnings(){
		return pvFindMessageType(MESSAGE_TYPE.WARNING);
	}

	/**
	 * Retorna se existe mensagem de informação
	 * @return
	 */
	public boolean hasInformations(){
		return pvFindMessageType(MESSAGE_TYPE.INFORMATION);
	}
	
	/**
	 * Retorna se existe alguma mensagem
	 * @return
	 */
	public boolean hasMessages(){
		if (wMessages.size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * Retorna se mensagem foi validada
	 * @param pMessageKey
	 * @return
	 */
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
	public void validated(String pMessageKey, Boolean pIsValidated){}
	
	/**
	 * Usuário que criou as mensagens
	 * @return
	 */
	public String getFromUserId() {
		return wFromUserId;
	}

	/**
	 * Usuário que criou as mensagens
	 * @param pFromUserId
	 */
	public void setFromUserId(String pFromUserId) {
		wFromUserId = pFromUserId.trim().toUpperCase();
	}

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

}
