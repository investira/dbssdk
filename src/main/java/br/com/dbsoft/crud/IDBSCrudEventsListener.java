package br.com.dbsoft.crud;

import br.com.dbsoft.error.DBSIOException;

public interface IDBSCrudEventsListener<DataModelClass> {

	/**
	 * Disparado antes de inicar uma edição, seja, insert, update ou delete.
	 * @param pEvent
	 * @return
	 * @throws DBSIOException
	 */
	public void beforeEdit(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Neste evento deve-se implementar a leitura integralno banco de dados do registro a partir das informações passadas no <b>DataModelClass</b>.
	 * Deve-se criar <b>novo</b> objeto do tipo <b>DataModelClass</b>, setar seus valores e retorna-lo.
	 * Deve-se retornar <b>null</b> caso o registro não seja encontrado.
	 * @param pEvent
	 * @return Dados lidos
	 * @throws DBSIOException
	 */
	public void beforeRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Neste evento deve-se implementar a exclusão no banco de dados das informações passadas no <b>DataModelClass</b>
	 * Deve-se retornar a quantidade de registros afetados.
	 * @param pEvent
	 * @return Quantidade de registros efetados.
	 */
	public void beforeDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Neste evento deve-se implementar o merge no banco de dados das informações passadas no <b>DataModelClass</b>
	 * Para efetuar <b>insert</b> a <b>PK</b> deve estar nula, caso contrário será efetuado um <b>update</b>.
	 * Deve-se retornar a quantidade de registros afetados.
	 * @param pEvent
	 * @return Quantidade de registros efetados.
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
	 * Neste evento deve-se implementar a leitura integralno banco de dados do registro a partir das informações passadas no <b>DataModelClass</b>.
	 * Deve-se criar <b>novo</b> objeto do tipo <b>DataModelClass</b>, setar seus valores e retorna-lo.
	 * Deve-se retornar <b>null</b> caso o registro não seja encontrado.
	 * @param pEvent
	 * @return Dados lidos
	 * @throws DBSIOException
	 */
	public DataModelClass onRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Neste evento deve-se implementar a exclusão no banco de dados das informações passadas no <b>DataModelClass</b>
	 * Deve-se retornar a quantidade de registros afetados.
	 * @param pEvent
	 * @return Quantidade de registros efetados.
	 */
	public Integer onDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Neste evento deve-se implementar o merge no banco de dados das informações passadas no <b>DataModelClass</b>
	 * Para efetuar <b>insert</b> a <b>PK</b> deve estar nula, caso contrário será efetuado um <b>update</b>.
	 * Deve-se retornar a quantidade de registros afetados.
	 * @param pEvent
	 * @return Quantidade de registros efetados.
	 */
	public Integer onMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Neste evento deve-se implementar o merge no banco de dados das informações passadas no <b>DataModelClass</b>
	 * Para efetuar <b>insert</b> a <b>PK</b> deve estar nula, caso contrário será efetuado um <b>update</b>.
	 * Deve-se retornar a quantidade de registros afetados.
	 * @param pEvent
	 * @return Quantidade de registros efetados.
	 */
	public void onError(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Evento para validação dos dados.<br/>
	 * Para indicar problemas na validação deve-se setar <b>pEvent.setOk(false)</b>.<br/>
	 * @param pEvent
	 * @throws DBSIOException
	 */
//	@SuppressWarnings("rawtypes")
	public void onValidate(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;
}
