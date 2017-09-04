package br.com.dbsoft.message;

import java.util.Set;

/**
 * @author ricardo.villar
 *
 */
public interface IDBSMessage extends IDBSMessageBase {
   	
	/**
	 * Retorna o código da mensagem
	 * @return
	 */
	public String getMessageKey();
	public void setMessageKey(String pMessageKey);
	
	/**
	 * Retorna se mensagem foi validada como true.<br/>
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa ou não validada</li>
	 * </ul>
	 */
	public boolean isMessageValidatedTrue();
	
	/**
	 * Retorna se mensagem foi validada.<br/>
	 * Mensagens de erro e fatais que não são pergutas(Questions), são sempre validadas como false.
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa</li>
	 * <li>null= Ainda não validada</li>
	 * </ul>
	 */
	public Boolean isMessageValidated();

	
	/**
	 * Seta se mensagem foi validada.<br/>
	 * Dispara os <b>IDBSMessageListener</b> que eventualmente existam atrelados a esta mensagem.
	 * @param <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa</li>
	 * <li>null= Ainda não validada</li>
	 * </ul>
	 */
	public void setMessageValidated(Boolean validated);
	
	/**
	 * Reseta mensagem como não validada(null) sem disparar os <b>IDBSMessageListener</b> 
	 * que eventualmente existam atrelados a ela. 
	 */
	public void reset();

	public String getMessageTooltip();
	public void setMessageTooltip(String pMessageTooltip);
	
	/**
	 * Id's vinculados esta mensagem.<br/>
	 * O <b>TargetsIds</b> pode ser utilizado indicar a quem se destina a mensagem, 
	 * como no caso de um validação que retorna mensagem de erro onde é importante saber a qual campo se refere.<br/>
	 * <b>Caso queira que a mensagem afete componentes em tela, o targetsIds deverá conter o clientId componente. Se for mais de um, devem estar separador por espaço.</b><br/> 
	 * Caso seja adicionada um mensagem que já exista, porém para outros targetsIds, não será adicionada nova mensagem, mas os <b>targetsIds</b> serão adicionados a mensagem já existente.<br/>
	 * Caso exista mais de um TargetId, eles deverão estar separador por espaço.<br/>
	 * @return
	 */
	public Set<String> getMessageTargetsIds();
	
	/**
	 * Retorna lista com os listeners
	 * @return
	 */
	public Set<IDBSMessageListener> getMessageListeners();
	/**
	 * Adiciona um listener que receberá os eventos disparados pela mensagem.</br>
	 * Retorna a própria mensagem já com o listener incluído.
	 * @param pMessageListener
	 * @return
	 */
	public IDBSMessage addMessageListener(IDBSMessageListener pMessageListener);
	/**
	 * Remove o listener.</br>
	 * Retorna a própria mensagem já com o listener removido.
	 * @param pMessageListener
	 * @return
	 */
	public IDBSMessage removeMessageListener(IDBSMessageListener pMessageListener);
	
	/**
	 * Copia dados de uma mensagem para esta
	 * @return
	 */
	public void copyFrom(IDBSMessage pSourceMessage);

	/**
	 * Verifica se mensagem é iqual a partir da chave da mensagem.
	 * @param pSourceMessage
	 * @return
	 */
	public boolean equals(IDBSMessage pSourceMessage);

	/**
	 * Verifica se mensagem é iqual a partir da chave.
	 * @param pMessageKey
	 * @return
	 */
	public boolean equals(String pMessageKey);

	public IDBSMessage clone();

	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	public void setMessageTextParameters(Object... pParameters);

	public Exception getException();
	public void setException(Exception pException);
	
}
