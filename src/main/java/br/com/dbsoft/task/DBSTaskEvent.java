package br.com.dbsoft.task;

import br.com.dbsoft.event.DBSEvent;

public class DBSTaskEvent extends DBSEvent<DBSTask<?>> implements IDBSTaskEvent{

	public DBSTaskEvent(DBSTask<?> pSourceObject) {
		super(pSourceObject);
	}

}
