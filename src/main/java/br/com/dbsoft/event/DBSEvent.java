package br.com.dbsoft.event;

import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.DBSMessages;

/**
 * Class da qual todos os eventos deverão ser extendidos
 * @param <SourceObjectClass> Class do object que originará o evento
 */
public abstract class DBSEvent<SourceObjectClass>{
	
	private boolean 				wOk = true;
	private DBSMessages<DBSMessage> wMessages = new DBSMessages<DBSMessage>(DBSMessage.class);
	private SourceObjectClass 		wSourceObject;

	/**
	 * Quando esta classe for extendida, deve-se alterar o tipo SourceObjectClass pelo objeto que será o padrão.
	 * @param pSourceObject
	 */
	public DBSEvent(SourceObjectClass pSourceObject){
		wSourceObject = pSourceObject;
	}
	
	/**
	 * Se processamento foi Ok
	 * @return
	 */
	public final boolean isOk() {
		return wOk;
	}

	/**
	 * Se processamento foi Ok
	 * @param pOk
	 */
	public final void setOk(boolean pOk) {
		wOk = pOk;
	}

	/**
	 * Objeto origem que disparou o evento
	 * @return
	 */
	public final SourceObjectClass getSource() {
		return wSourceObject;
	}

	public DBSMessages<DBSMessage> getMessages() {
		return wMessages;
	}
	
	

}
