package br.com.dbsoft.event;

import br.com.dbsoft.message.DBSMessages;
import br.com.dbsoft.message.IDBSMessages;


/**
 * Class da qual todos os eventos deverão ser extendidos
 * @param <SourceObjectClass> Class do object que originará o evento
 */
public abstract class DBSEvent<SourceObjectClass> implements IDBSEvent<SourceObjectClass>{
	
	private boolean 					wOk = true;
	private IDBSMessages				wMessages = new DBSMessages();
	private SourceObjectClass 			wSourceObject;

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
	@Override
	public final boolean isOk() {
		return wOk;
	}

	/**
	 * Se processamento foi Ok
	 * @param pOk
	 */
	@Override
	public final void setOk(boolean pOk) {
		wOk = pOk;
	}

	/**
	 * Objeto origem que disparou o evento
	 * @return
	 */
	@Override
	public final SourceObjectClass getSource() {
		return wSourceObject;
	}

	@Override
	public IDBSMessages getMessages() {
		return wMessages;
	}
	
	

}
