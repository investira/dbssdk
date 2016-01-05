package br.com.dbsoft.crud;

import br.com.dbsoft.error.DBSIOException;

public interface IDBSCrudEventsListener<DataModelClass> {

	/**
	 * Disparado antes de inicar uma edição, seja, insert, update ou delete.<br/>
	 * Read dos dados anteriores não foi efetuado, desta forma <b>getDataModelRead<b/> estará nulo.
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void beforeEdit(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Disparado antes do <b>onRead</b> sejá na execução do <b>merge</b> ou do próprio <b>read</b>.<br/>
	 * Read dos dados anteriores não foi efetuado, desta forma <b>getDataModelRead<b/> estará nulo.
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void beforeRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Disparado antes do <b>onDelete</b>.<br/>
	 * Read dos dados anteriores não foi efetuado, desta forma <b>getDataModelRead<b/> estará nulo.
	 * @param pEvent
	 */
	public void beforeDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Disparado antes do <b>onMerge</b>.<br/>
	 * Read dos dados anteriores não foi efetuado, desta forma <b>getDataModelRead<b/> estará nulo.
	 * @param pEvent
	 */
	public void beforeMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Disparado após uma edição independentemente se houve ou não erro, seja, insert, update ou delete.<br/>
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void afterEdit(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;
	
	/**
	 * Evento disparado ao final do read executado com sucesso.<br/>
	 * Os dados lidos podem ser consultados em <b>pEvent.getDataModelRead()</b>.<br/>
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void afterRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Evento disparado ao final do delete executado com sucesso.<br/>
	 * Os dados lidos podem ser consultados em <b>pEvent.getDataModel()</b> e <b>pEvent.getDataModelRead()</b>.<br/>
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void afterDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Evento disparado ao final do merge executado com sucesso.<br/>
	 * Os dados lidos podem ser consultados em <b>pEvent.getDataModelRead()</b>.<br/>
	 * @param pEvent
	 * @throws DBSIOException
	 */
	public void afterMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	
	/**
	 * Evento disparado em caso de erro.<br/>
	 * Os dados lidos podem ser consultados em <b>pEvent.getDataModelRead()</b>.<br/>
	 * @param pEvent
	 * @return Quantidade de registros efetados.
	 */
	public void afterError(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

}
