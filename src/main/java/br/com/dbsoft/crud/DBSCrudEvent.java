package br.com.dbsoft.crud;

import br.com.dbsoft.event.DBSEvent;

public class DBSCrudEvent<DataModelClass> extends DBSEvent<IDBSCrud<DataModelClass>> implements IDBSCrudEvent<DataModelClass> {

	private DataModelClass wDataModel;
	
	private DataModelClass wDataModelRead;
	
	public DBSCrudEvent(IDBSCrud<DataModelClass> pSourceObject, DataModelClass pDataModel) {
		super(pSourceObject);
		wDataModel = pDataModel;
		wDataModelRead = null;
	}

	public DBSCrudEvent(IDBSCrud<DataModelClass> pSourceObject, DataModelClass pDataModel, DataModelClass pDataModelRead) {
		super(pSourceObject);
		wDataModel = pDataModel;
		wDataModelRead = pDataModelRead;
	}

	@Override
	public DataModelClass getDataModel() {
		return wDataModel;
	}

	@Override
	public DataModelClass getDataModelRead() {
		return wDataModelRead;
	}

}
