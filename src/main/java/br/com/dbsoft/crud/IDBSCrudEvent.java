package br.com.dbsoft.crud;

import br.com.dbsoft.event.IDBSEvent;

/**
 * @author ricardo.villar
 *
 */
public interface IDBSCrudEvent<DataModelClass> extends IDBSEvent<IDBSCrud<DataModelClass>> {

	/**
	 * Dados
	 * @return
	 */
	public DataModelClass getDataModel();
	
}
