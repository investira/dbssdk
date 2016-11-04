package br.com.dbsoft.message;

import java.util.List;

import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;


/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
public interface IDBSMessages<MessageClass extends IDBSMessage>{

	
	/**
	 * Retorna copia da lista de mensagens.<br/>
	 * Qualquer alteração no conteúdo desta lista, em nada afetará o controle de mensagens original.
	 * @return
	 */
	public List<MessageClass> getMessages();


	/** 
	 * Inclui uma mensagem na fila.
	 * @param pMessage
	 */
	public MessageClass add(MessageClass pMessage);
	
	/** 
	 * Inclui uma mensagem na fila e vincula um <b>sourceId</b> a ela.</br>
	 * O <b>sourceId</b> pode ser utilizado indicar a origem da mensagem, 
	 * como no caso de um validade que retorna mensagens de erro onde é importante saber quais campos foram afetados.<br/>
	 * O valor do <b>sourceId</b> é a critério do usuário.
	 * @param pClientId
	 * @param pMessage
	 * @return
	 */
	public MessageClass add(MessageClass pMessage, String pSourceId);

	/**
	 * Adiciona todas as mensagems a fila.
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
	 * Retorna uma mensagem vinculada ao <b>sourceId</b> informado.
	 * @param pMessageKey
	 */
	public MessageClass getMessageForSourceId(String pClientId);

	/**
	 * Retorna list com as mensagens vinculada ao  <b>sourceId</b> informado.
	 * @param pMessageKey
	 */
	public List<MessageClass> getMessagesForSourceId(String pClientId);
	
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
	 * Retorna se mensagem foi validada como true.<br/>
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa ou não validada</li>
	 * </ul>
	 */
	public Boolean isMessageValidatedTrue(String pMessageKey);

	
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
	 * Valida ou invalida a mensagem corrente da fila.
	 * Se não for mensagem de warning, automaticamente retira mensagem da fila 
	 * @param pIsValidated
	 * @return Caminho da view a ser exibida
	 */
	public String setMessageValidated(Boolean pIsValidated);
	
	/**
	 * Valida ou invalida a mensagem informada pela <b>messageKey</b>.
	 * Se não for mensagem de warning, automaticamente retira mensagem da fila 
	 * @param pMessageKey
	 * @param pIsValidated
	 * @return Caminho da view a ser exibida
	 */
	public String setMessageValidated(String pMessageKey, Boolean pIsValidated);

	/**
	 * Valida ou invalida a mensagem informada.
	 * Se não for mensagem de warning, automaticamente retira mensagem da fila 
	 * @param pMessageKey
	 * @param pIsValidated
	 * @return Caminho da view a ser exibida
	 */
	public String setMessageValidated(MessageClass pMessage);

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
