package br.com.dbsoft.crud;

import br.com.dbsoft.event.IDBSEvent;

/**
 * @author ricardo.villar
 *
 */
public interface IDBSCrudEvent<DataModelClass> extends IDBSEvent<IDBSCrud<DataModelClass>> {

	/**
	 * Dados que serão utilizados para efetuar a ação.<br/>
	 * Para consultar os dados existentes antes de efetuar a ação, 
	 * deve-se utilizar o <b>getDataModelRead()</b> que estará disponível após o evento <b>afterRead</b>.<br/>
	 * Nos eventos anteriores ao <b>afterRead</b> e <b>caso não exista registro anterior</b> o <b>getDataModelRead()</b> será <b>null</b>.<br/>
	 * Para consultar o ordem dos eventos disparados para cada ação consulte o help da respectiva ação(merge, delete, read).  
	 * @return Dados que serão utilizados para efetuar a ação.
	 */
	public DataModelClass getDataModel();
	
	/**
	 * Dados lidos antes de efetuar a ação.<br/>
	 * Será efetuada uma tentativa de leitura do registro anterior a partir dos dados informados em <b>getDataModel</b>.
	 * Nos eventos anteriores ao <b>afterRead</b> e <b>caso não exista registro anterior</b> o <b>getDataModelRead()</b> será <b>null</b>.<br/>
	 * Para consultar o ordem dos eventos disparados para cada ação consulte o help da respectiva ação(merge, delete, read).  
	 * @return Dados lidos antes de efetuar a ação.
	 */
	public DataModelClass getDataModelRead();
	
}
