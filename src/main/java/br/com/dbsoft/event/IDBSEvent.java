package br.com.dbsoft.event;

import br.com.dbsoft.message.IDBSMessages;

/**
 * Class da qual todos os eventos deverão ser extendidos
 * @param <SourceObjectClass> Class do object que originará o evento
 */
public interface IDBSEvent<SourceObjectClass>{
	
	/**
	 * Se processamento foi Ok
	 * @return
	 */
	public boolean isOk();

	/**
	 * Se processamento foi Ok
	 * @param pOk
	 */
	public void setOk(boolean pOk);

	/**
	 * Objeto origem que disparou o evento
	 * @return
	 */
	public SourceObjectClass getSource();

	/**
	 * Mensagens entre os eventos.
	 * Utilizar caso NÃO exista um outra forma de enviar mensagem para outro evento.
	 * @return
	 */
	public IDBSMessages getMessages();	

}
