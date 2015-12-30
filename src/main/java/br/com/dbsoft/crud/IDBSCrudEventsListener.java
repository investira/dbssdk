package br.com.dbsoft.crud;

import br.com.dbsoft.error.DBSIOException;

public interface IDBSCrudEventsListener<DataModelClass> {

	/**
	 * Disparado antes de inicar uma edição, seja, insert, update ou delete.
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void beforeEdit(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Disparado antes do <b>onRead</b> sejá na execução do <b>merge</b> ou do próprio <b>read</b>.
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void beforeRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Disparado antes do <b>onDelete</b>.
	 * @param pEvent
	 */
	public void beforeDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Disparado antes do <b>onMerge</b>.
	 * @param pEvent
	 */
	public void beforeMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Disparado após uma edição independentemente se houve ou não erro, seja, insert, update ou delete.
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void afterEdit(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;
	
	/**
	 * Evento disparado ao final do read executado com sucesso.<br/>
	 * Os dados lidos podem ser consultados em <b>pEvent.getDataModel()</b>.
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void afterRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Evento disparado ao final do delete executado com sucesso.
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void afterDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Evento disparado ao final do merge executado com sucesso.
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void afterMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	
	/**
	 * Evento disparado em caso de erro.<br/>
	 * @param pEvent
	 * @return Quantidade de registros efetados.
	 */
	public void afterError(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

}
