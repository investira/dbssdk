package br.com.dbsoft.io;

import br.com.dbsoft.event.DBSEvent;

public class DBSDAOEvent extends DBSEvent<DBSDAOBase<?>> {

	public DBSDAOEvent(DBSDAOBase<?> pObject) {
		super(pObject);
	}

}
