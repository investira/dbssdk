package br.com.dbsoft.message;

import java.util.Collection;
import java.util.List;

import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;


/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
public interface IDBSMessages<MessageClass extends IDBSMessage>  {

	/**
	 * Retorna lista contendo chave e respectiva mensagem
	 * @return
	 */
	public Collection<MessageClass>getMessages();


//	/**
//	 * Retorna iterator da chave e respectiva mensagem
//	 * @return
//	 */
//	public Iterator<Entry<String, MessageClass>> iterator();
//	

	/** Inclui uma mensagem na fila para ser exibida.
	 * @param pMessage
	 */
	public MessageClass add(MessageClass pMessage);
	
	/** Inclui uma mensagem na fila para ser exibida.
	 * @param pClientId
	 * @param pMessage
	 * @return
	 */
	public MessageClass add(String pClientId, MessageClass pMessage);

	/**
	 * Adiciona todas as mensagems a fila
	 * @param pMessages
	 */
	public void addAll(IDBSMessages<MessageClass> pMessages);

	/**
	 * Remove uma mensagem da fila e reposiciona da próxima
	 * @param pMessageKey
	 */
	public void remove(MessageClass pMessage);
	
	/**
	 * Remove uma mensagem da fila e reposiciona da próxima
	 * @param pMessageKey
	 */
	public void remove(String pMessageKey);
	
	/**
	 * Retorna uma mensagem a partir da chave informada
	 * @param pMessageKey
	 */
	public MessageClass get(String pMessageKey);

	/**
	 * Retorna uma mensagem vinculada ao clientId informado.
	 * @param pMessageKey
	 */
	public MessageClass getMessageForClientId(String pClientId);

	/**
	 * Retorna list com as mensagens vinculada ao clientId informado.
	 * @param pMessageKey
	 */
	public List<MessageClass> getMessagesForClientId(String pClientId);
	
	/**
	 * Apaga todas as mensagem da fila 
	 */
	public void clear();

	
	/**
	 * Retorna a chave da mensagem corrente
	 * @return
	 */
	public String getCurrentMessageKey();
	
	/**
	 * Retorna o texto mensagem corrente
	 * @return
	 */
	public String getCurrentMessageText();


	/**
	 * Retorna o tipo mensagem corrente
	 * @return
	 */
	public MESSAGE_TYPE getCurrentMessageType();
	
	/**
	 * Retorna o tooltip da mensagem corrente
	 * @return
	 */
	public String getCurrentMessageTooltip();
	

	/**
	 * Retorna a mensagem corrente
	 * @return
	 */
	public MessageClass getCurrentMessage();
	

	/**
	 * Retorna se existe mensagem de erro
	 * @return
	 */
	public boolean hasErrors();
	/**
	 * Retorna se existe alguma mensagem de alerta
	 * @return
	 */
	public boolean hasWarnings();
	/**
	 * Retorna se existe mensagem de informação
	 * @return
	 */
	public boolean hasInformations();
	
	/**
	 * Retorna se existe alguma mensagem
	 * @return
	 */
	public boolean hasMessages();

	/**
	 * Retorna se mensagem foi validada.<br/>
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa</li>
	 * <li>null= Ainda não validada</li>
	 * </ul>
	 * 
	 */
	public Boolean isMessageValidated(String pMessageKey);
	

	/**
	 * Valida ou invalida a mensagem
	 * Se não for mensagem de warning, automaticamente retira mensagem da fila 
	 * @param pButtonPressed
	 */
	public void setMessageValidated(Boolean pIsValidated);
	
	/**
	 * Método após a validação de qualquer mensagem
	 * @param pMessageKey
	 * @param pIsValidated
	 */
	public void onMessageValidate(String pMessageKey, Boolean pIsValidated);
	
	/**
	 * Usuário que criou as mensagens
	 * @return
	 */
	public String getFromUserId();

	/**
	 * Usuário que criou as mensagens
	 * @param pFromUserId
	 */
	public void setFromUserId(String pFromUserId);
	

}
