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
	public void afterAddMessage(IDBSMessages pMessages, IDBSMessage pMessage);

	/**
	 * Disparado após a mensagem ser removida.
	 * @param pMessageKey Chave da mensagem removida.
	 */
	public void afterRemoveMessage(IDBSMessages pMessages, String pMessageKey);

	/**
	 * Disparado após a mensagem ser removida.
	 * @param pMessageKey Chave da mensagem removida.
	 */
	public void afterClearMessages(IDBSMessages pMessages);
	
	/**
	 * Disparado após a mensagem ser validada independentemente do valor da validação(true ou false).
	 * @param pMessage Mensagem que foi validada.
	 */
	public void afterMessageValidated(IDBSMessages pMessages, IDBSMessage pMessage);


}
