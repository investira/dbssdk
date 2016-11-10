package br.com.dbsoft.message;

/**
 * @author ricardo.villar
 *
 */
public interface IDBSMessagesListener{
	
	/**
	 * Disparado após a mensagem ser adicionada.
	 * @param pMessage Mensagem que será incluida.
	 * @return Retorna mensagem que foi incluída.</br>
	 * Quando a mensagem enviada for <i>static</i>, será criado automaticamente um <i>clone</i> e este é que será incluido e retornado.
	 */
	public void afterAddMessage(IDBSMessage pMessage);

	/**
	 * Disparado após a mensagem ser removida.
	 * @param pMessageKey Chave da mensagem removida.
	 */
	public void afterRemoveMessage(String pMessageKey);

	/**
	 * Disparado após a mensagem ser removida.
	 * @param pMessageKey Chave da mensagem removida.
	 */
	public void afterClearMessages();

	/**
	 * Disparado após a mensagem ser validada.
	 * @param pMessage Copia da mensagem que foi validada.<br/>
	 * @param pMessage
	 */
//	public <MessageClass extends IDBSMessage> void afterMessageValidated(MessageClass pMessage);

}
