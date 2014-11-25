package br.com.dbsoft.task;

import br.com.dbsoft.event.DBSEvent;

public class DBSTaskEvent extends DBSEvent<DBSTask<?>> {

	public DBSTaskEvent(DBSTask<?> pSourceObject) {
		super(pSourceObject);
	}

}
