package br.com.dbsoft.file;

import br.com.dbsoft.event.DBSEvent;

/**
 * @author ricardo.villar
 *
 */
public class DBSFileTransferEvent extends DBSEvent<DBSFileTransfer> implements IDBSFileTransferEvent{

	public DBSFileTransferEvent(DBSFileTransfer pSourceObject) {
		super(pSourceObject);
	}


}
