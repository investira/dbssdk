package br.com.dbsoft.error;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import br.com.dbsoft.error.DBSError.CODES;
import br.com.dbsoft.message.IDBSMessageBase;

public class DBSIOException extends DBSException {

	private static final long serialVersionUID = 5415621980418874270L;

	public DBSIOException(Exception e){ 
		super(e);
	}
	
	public DBSIOException(IOException e){ 
		super(e);
	}
	
	public DBSIOException(SQLException e){ 
		super("[" + e.getErrorCode() + "] " + e.getLocalizedMessage(), e);
	}

	public DBSIOException(DBSIOException e){
		super("[" + e.getErrorCode() + "] " + e.getLocalizedMessage(), e);
	}

	public DBSIOException(String pMessage){
		super(pMessage);
	}
	
	public DBSIOException(IDBSMessageBase pMessage){
		super(pMessage);
	}

	public DBSIOException(SQLException e, Connection pConnection){
		super("[" + e.getErrorCode() + "] " + e.getLocalizedMessage(), e, DBSError.toCodes(e, pConnection));
	}

	public DBSIOException(String pMessage, SQLException e, Connection pConnection){
		super(pMessage, e, DBSError.toCodes(e, pConnection));
	}

	
	public DBSIOException(String pMessage, DBSIOException e){
		super(pMessage, e);
	}

	public boolean isIntegrityConstraint(){
		return (this.getErrorCode().equals(CODES.INTEGRITY_CONSTRAINT));
	}

	public boolean isNoConnection(){
		return (this.getErrorCode().equals(CODES.NO_CONNECTION));
	}


}
