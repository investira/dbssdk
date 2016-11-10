package br.com.dbsoft.message;


public interface IDBSMessageListener {
	
	/**
	 * Disparado após a mensagem ser validada.
	 * @param pMessage Mensagem que foi validada.
	 */
	public void afterMessageValidated(IDBSMessage pMessage);
	
}
