package br.com.dbsoft.file;

import br.com.dbsoft.event.DBSEvent;

/**
 * @author ricardo.villar
 *
 */
public class DBSFileTransferEvent extends DBSEvent<DBSFileTransfer> {

	public DBSFileTransferEvent(DBSFileTransfer pSourceObject) {
		super(pSourceObject);
	}


}
