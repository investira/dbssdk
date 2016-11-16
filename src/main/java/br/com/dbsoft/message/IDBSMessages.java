package br.com.dbsoft.message;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface IDBSMessages extends Serializable{

	/**
	 * Retorna iterator.<br/>
	 * @return
	 */
	public Iterator<IDBSMessage> iterator();

	/**
	 * Retorna copia da lista de mensagens.<br/>
	 * Qualquer inclusão ou exclusão em itens desta lista, em nada afetará o controle de mensagens original.
	 * @return
	 */
	public List<IDBSMessage> getListMessage();
	
	/** 
	 * Inclui uma mensagem na fila.</br>
	 * Se mensagem for <i>static</i>, será criada uma clone da mensagem enviada.<br/>.
	 * @param pMessage
	 */
	public void add(IDBSMessage pMessage);
	
	/** 
	 * Inclui uma mensagem na fila e vincula um <b>sourceId</b> a ela.</br>
	 * Se mensagem for <i>static</i>, será criada uma clone da mensagem enviada.<br/>.
	 * O <b>sourceId</b> pode ser utilizado indicar a origem da mensagem, 
	 * como no caso de um validade que retorna mensagens de erro onde é importante saber quais campos foram afetados.<br/>
	 * O valor do <b>sourceId</b> é a critério do usuário.
	 * @param pClientId
	 * @param pMessage
	 */
	public void add(IDBSMessage pMessage, String pSourceId);

	/**
	 * Adiciona todas as mensagems a fila.
	 * @param pMessages
	 */
	public void addAll(IDBSMessages pMessages);

	/**
	 * Remove uma mensagem da fila e reposiciona da próxima
	 * @param pMessageKey
	 */
	public void remove(IDBSMessage pMessage);
	
	/**
	 * Remove uma mensagem da fila e reposiciona da próxima
	 * @param pMessageKey
	 */
	public void remove(String pMessageKey);
	
	/**
	 * Retorna se mensagem existe e foi validada como true.<br/>
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa ou não validada ou não existente.</li>
	 * </ul>
	 */
	public boolean isMessageValidatedTrue(String pMessageKey);

	/**
	 * Retorna se mensagem existe e foi validada como true.<br/>
	 * Utilizada a <b>messageKey</b> da mensagem enviada para pesquisar a mensagem desejada.
	 * @param pMessage
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa ou não validada ou não existente.</li>
	 * </ul>
	 */
	public boolean isMessageValidatedTrue(IDBSMessage pMessage);

	/**
	 * Retorna uma mensagem a partir da chave informada
	 * @param pMessageKey
	 */
	public IDBSMessage getMessage(String pMessageKey);

	/**
	 * Retorna uma mensagem a partir da mensagem informada.
	 * Utiliza a chave da mensagem informada(MessageKey). 
	 * @param pMessageKey
	 */
	public IDBSMessage getMessage(IDBSMessage pMessage);


	/**
	 * Retorna uma mensagem vinculada ao <b>sourceId</b> informado.
	 * @param pMessageKey
	 */
	public IDBSMessage getMessageForSourceId(String pClientId);

	/**
	 * Retorna list com as mensagens vinculada ao  <b>sourceId</b> informado.
	 * @param pMessageKey
	 */
	public List<IDBSMessage> getMessagesForSourceId(String pClientId);
	
	/**
	 * Apaga todas as mensagem da fila 
	 */
	public void clear();

	/**
	 * Reseta todas as mensagens como não validadas(null) sem disparar os <b>IDBSMessageListener</b> 
	 * que eventualmente existam atrelados a elas. 
	 */
	public void reset();

	
	/**
	 * Retorna se existe mensagem de erro não validada.
	 * @return
	 */
	public boolean hasErrorsMessages();

	/**
	 * Retorna se existe alguma mensagem de alerta não validada.<br/>
	 * Mesagens de alerta necessitam de confirmação.
	 * @return
	 */
	public boolean hasWarningsMessages();
	
	/**
	 * Retorna se existe mensagem de informação não validada.
	 * @return
	 */
	public boolean hasInformationsMessages();
	
	/**
	 * Retorna se existe alguma mensagem.
	 * @return
	 */
	public boolean hasMessages();
	
	/**
	 * Usuário que criou as mensagens
	 * @return
	 */
	public String getMessagesFromUserId();

	/**
	 * Usuário que criou as mensagens
	 * @param pFromUserId
	 */
	public void setMessagesFromUserId(String pFromUserId);
	
	/**
	 * Retorna lista com os listeners
	 * @return
	 */
	public Set<IDBSMessagesListener> getMessagesListeners();
	/**
	 * Adiciona um listener que receberá os eventos disparados pela mensagem.</br>
	 * Retorna a próprio dbsmessages já com o listener incluído.
	 * @param pMessageListener
	 * @return
	 */
	public IDBSMessages addMessagesListener(IDBSMessagesListener pMessagesListener);
	/**
	 * Remove o listener.</br>
	 * Retorna a próprio dbsmessages já com o listener removido.
	 * @param pMessageListener
	 * @return
	 */
	public IDBSMessages removeMessagesListener(IDBSMessagesListener pMessagesListener);
	/**
	 * Quantidade de mensagens
	 * @return
	 */
	public Integer size();
}
