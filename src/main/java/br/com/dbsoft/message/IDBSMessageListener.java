package br.com.dbsoft.message;


public interface IDBSMessageListener {
	
	/**
	 * Disparado após a mensagem ser validada independentemente do valor da validação(true ou false).
	 * @param pMessage Mensagem que foi validada.
	 */
	public void afterMessageValidated(IDBSMessage pMessage);
	
}
