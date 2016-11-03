package br.com.dbsoft.message;


public interface IDBSMessageListener {
	
	/**
	 * Disparado após a mensagem ser validada.
	 * @param pMessage Copia da mensagem que foi validada.
	 * @return Retorna mensagem local que foi enviada. </br>
	 * Esta informação é importante para atualizar automaticamente os dados da mensagem local com após a validação. 
	 */
	public <MessageClass extends IDBSMessage> IDBSMessage afterMessageValidated(MessageClass pMessage);
	
}
