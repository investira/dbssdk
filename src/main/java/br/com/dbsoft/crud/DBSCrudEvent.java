package br.com.dbsoft.crud;

import br.com.dbsoft.event.DBSEvent;

public class DBSCrudEvent<DataModelClass> extends DBSEvent<IDBSCrud<DataModelClass>> implements IDBSCrudEvent<DataModelClass> {

	private DataModelClass wDataModelClass;
	
	public DBSCrudEvent(IDBSCrud<DataModelClass> pSourceObject, DataModelClass pDataModelClass) {
		super(pSourceObject);
		wDataModelClass = pDataModelClass;
	}

	@Override
	public DataModelClass getDataModel() {
		return wDataModelClass;
	}


}
