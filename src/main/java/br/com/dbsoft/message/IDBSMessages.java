package br.com.dbsoft.message;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface IDBSMessages extends Serializable, IDBSMessageListener{

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
	public List<IDBSMessageBase> getListMessageBase();
	
	public void add(IDBSMessageBase pMessageBase);
	
	/** 
	 * Inclui uma mensagem na fila.</br>
	 * O <b>TargetsIds</b> pode ser utilizado indicar a quem se destina a mensagem, 
	 * como no caso de um validação que retorna mensagem de erro onde é importante saber a qual campo se refere.<br/>
	 * <b>Caso queira que a mensagem afete componentes em tela, o targetsIds deverá conter o clientId componente. Se for mais de um, devem estar separador por espaço.</b><br/> 
	 * Caso seja adicionada um mensagem que já exista, porém para outros targetsIds, não será adicionada nova mensagem, mas os <b>targetsIds</b> serão adicionados a mensagem já existente.<br/>
	 * Caso exista mais de um TargetId, eles deverão estar separador por espaço.<br/>
	 * @param pMessage
	 * @param pTargetsIds Ids separados por espaços.
	 */
	public void add(IDBSMessageBase pMessageBase, String pTargetsIds);

	/** 
	 * Inclui uma mensagem na fila.</br>
	 * Ignora inclusão se existir uma mensagem com a mesma chave.
	 * @param pMessage
	 */
	public void add(IDBSMessage pMessage);
	
	/** 
	 * Inclui uma mensagem na fila.</br>
	 * O <b>TargetsIds</b> pode ser utilizado indicar a quem se destina a mensagem, 
	 * como no caso de um validação que retorna mensagem de erro onde é importante saber a qual campo se refere.<br/>
	 * <b>Caso queira que a mensagem afete componentes em tela, o targetsIds deverá conter o clientId componente. Se for mais de um, devem estar separador por espaço.</b><br/> 
	 * Caso seja adicionada um mensagem que já exista, porém para outros targetsIds, não será adicionada nova mensagem, mas os <b>targetsIds</b> serão adicionados a mensagem já existente.<br/>
	 * Caso exista mais de um TargetId, eles deverão estar separador por espaço.<br/>
	 * @param pMessage
	 * @param pTargetsIds Ids separados por espaços.
	 */
	public void add(IDBSMessage pMessage, String pTargetsIds);

	/**
	 * Adiciona todas as mensagems a fila.
	 * Ignora inclusão se existir uma mensagem com a mesma chave.</br>
	 * @param pMessages
	 */
	public void addAll(IDBSMessages pMessages);

	/**
	 * Adiciona todas as mensagems da lista a fila.
	 * Ignora inclusão se existir uma mensagem com a mesma chave.</br>
	 * @param pMessages
	 */
	public void addAll(List<DBSMessage> pMessages);

	/**
	 * Adiciona todas as mensagems a fila e remove da lista original.
	 * Ignora inclusão se existir uma mensagem com a mesma chave.</br>
	 * @param pMessages
	 */
	public void moveAll(IDBSMessages pMessages);

	/**
	 * Adiciona todas as mensagems da lista a fila e remove da lista original.
	 * Ignora inclusão se existir uma mensagem com a mesma chave.</br>
	 * @param pMessages
	 */
	public void moveAll(List<DBSMessage> pMessages);

	public void addAllMessageBase(List<IDBSMessageBase> pMessages);
	
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
	 * <li>false= Não existente ou não validada ou validada como negativa.</li>
	 * </ul>
	 */
	public boolean isMessageValidatedTrue(String pMessageKey);

	/**
	 * Retorna se mensagem existe e foi validada como true.<br/>
	 * Utilizada a <b>messageKey</b> da mensagem enviada para pesquisar a mensagem desejada.
	 * @param pMessage
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Não existente ou não validada ou validada como negativa.</li>
	 * </ul>
	 */
	public boolean isMessageValidatedTrue(IDBSMessage pMessage);

	/**
	 * Retorna se mensagem existe e foi validada como true.<br/>
	 * Utilizada a <b>messageKey</b> da mensagem enviada para pesquisar a mensagem desejada.
	 * @param pMessage
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Não existente ou não validada ou validada como negativa.</li>
	 * </ul>
	 */
	public boolean isAllMessagesValidatedTrue();

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
	 * Retorna uma mensagem vinculada ao <b>TargetId</b> informado.<br/>
	 * Desta forma é possível identificar se uma mensagem tem um destino específico, como por exemplo com input.
	 * @param pMessageKey
	 */
	public IDBSMessage getMessageForTargetId(String pTargetId);

	/**
	 * Retorna list com as mensagens vinculada ao  <b>TargetId</b> informado.
	 * Desta forma é possível identificar se uma mensagem tem um destino específico, como por exemplo com input.
	 * @param pMessageKey
	 */
	public List<IDBSMessage> getMessagesForTargetId(String pTargetId);
	
	/**
	 * @return A mensagem corrente(a primeira ainda não validada) se houver ou <i>null</i> se não houver.
	 */
	public IDBSMessage getCurrentMessage();
	
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
	 * Quantidade total de mensagens validadas e não validadas.
	 * @return
	 */
	public Integer size();

	/**
	 * Quantidade total de mensagens não validadas.
	 * @return
	 */
	public Integer notValidatedSize();

	/**
	 * Retorna se existe mensagem de erro(FacesMessage.SEVERITY_ERROR) não validada.
	 * @return
	 */
	public Boolean hasErrorsMessages();

	/**
	 * Retorna se existe alguma mensagem de alerta(FacesMessage.SEVERITY_WARN) não validada.<br/>
	 * Mesagens de alerta necessitam de confirmação.
	 * @return
	 */
	public Boolean hasWarningsMessages();
	
	/**
	 * Retorna se existe mensagem de informação(FacesMessage.SEVERITY_INFO) não validada.
	 * @return
	 */
	public Boolean hasInformationsMessages();
	
	/**
	 * Retorna se existe mensagem fatais(FacesMessage.SEVERITY_FATAL) não validada.
	 * @return
	 */
	public Boolean hasFatalsMessages();

	/**
	 * Retorna a mensagem mais severa, conforme a seguinte ordem: FATAL,ERROR,WARNING,INFO.
	 * @return
	 */
	public IDBSMessage getMostSevereMessage();

	
	/**
	 * Retorna se existe alguma mensagem não validada.
	 * @return
	 */
	public Boolean hasMessages();
	
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
}
