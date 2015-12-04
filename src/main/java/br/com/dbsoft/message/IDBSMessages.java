package br.com.dbsoft.message;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;


/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
public interface IDBSMessages<MessageClass extends IDBSMessage>  {


	public LinkedHashMap<String, MessageClass> getMessages();
	
	public Iterator<Entry<String, MessageClass>> iterator();
	
	public void add(DBSIOException e);
	
	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A exibição se derá na mesma ondem da inclusão
	 * @param pMessageKey Chave da mensagem que será utilizada para verificar a resposta do usuário
	 * @param pMessageType pTipo de mensaagem
	 * @param pMessageText Texto da mensagem
	 * @param pMessageTooltip Texto adicional a mensagem para se utilizado como diga
	 */
	public void add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pTime);
	
	/** Inclui uma mensagem na fila para ser exibida.
	 * @param pMessage
	 */
	public void add(MessageClass pMessage);

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A exibição se derá na mesma ondem da inclusão
	 * @param pMessageKey Chave da mensagem que será utilizada para verificar a resposta do usuário
	 * @param pMessageType pTipo de mensaagem
	 * @param pMessageText Texto da mensagem
	 */
	public void add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip);

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A exibição se derá na mesma ondem da inclusão
	 * @param pMessageKey Chave da mensagem que será utilizada para verificar a resposta do usuário
	 * @param pMessageType pTipo de mensaagem
	 * @param pMessageText Texto da mensagem
	 */
	public void add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText);
	
	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A exibição se derá na mesma ondem da inclusão
	 * @param pMessageKey Chave da mensagem que será utilizada para verificar a resposta do usuário
	 * @param pMessageType pTipo de mensaagem
	 * @param pMessageText Texto da mensagem
	 */
	public void add(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, DateTime pTime);
	
	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A chave da mensagem é o próprio texto
	 * @param pMessageType
	 * @param pMessageText
	 */
	public void add(MESSAGE_TYPE pMessageType, String pMessageText);

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A chave da mensagem é o próprio texto
	 * @param pMessageType
	 * @param pMessageText
	 */
	public void add(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip);

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A chave da mensagem é o próprio texto
	 * @param pMessageType
	 * @param pMessageText
	 */
	public void add(MESSAGE_TYPE pMessageType, String pMessageText, DateTime pTime);

	/**
	 * Inclui uma mensagem na fila para ser exibida.
	 * A chave da mensagem é o próprio texto
	 * @param pMessageType
	 * @param pMessageText
	 */
	public void add(MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip, DateTime pTime);
	

	/**
	 * Adiciona todas as mensagems a fila
	 * @param pMessages
	 */
	public void addAll(IDBSMessages<IDBSMessage> pMessages);

	/**
	 * Remove uma mensagem da fila e reposiciona da próxima
	 * @param pMessageKey
	 */
	public void remove(String pMessageKey);
	
	
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
	public Boolean isValidated(String pMessageKey);
	

	/**
	 * Valida ou invalida a mensagem
	 * Se não for mensagem de warning, automaticamente retira mensagem da fila 
	 * @param pButtonPressed
	 */
	public void setValidated(Boolean pIsValidated);
	
	/**
	 * Método após a validação de qualquer mensagem
	 * @param pMessageKey
	 * @param pIsValidated
	 */
	public void validated(String pMessageKey, Boolean pIsValidated);
	
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
