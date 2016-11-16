package br.com.dbsoft.message;

import java.io.Serializable;

/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
public interface IDBSMessagesController extends Serializable, IDBSMessageListener, IDBSMessagesListener{

	/**
	 * @return A mensagem corrente se houver ou <i>null</i> se não houver.
	 */
	public IDBSMessage getCurrentMessage();
	
	/**
	 * @return As mensagens não validadas(validated = null).
	 */
	public IDBSMessages getMessages();
	
}
