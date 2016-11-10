package br.com.dbsoft.message;

import java.io.Serializable;

/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
public interface IDBSMessagesController extends Serializable, IDBSMessageListener, IDBSMessagesListener{

	/**
	 * Retorna a mensagem corrente
	 * @return
	 */
	public IDBSMessage getCurrentMessage();
	
	public IDBSMessages getMessages();
	
}
