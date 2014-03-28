package br.com.dbsoft.error;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import br.com.dbsoft.error.DBSError.CODES;

public class DBSIOException extends DBSException {

	private static final long serialVersionUID = 5415621980418874270L;

	public DBSIOException(Exception e){ 
		super(e);
	}
	
	public DBSIOException(IOException e){ 
		super(e);
	}
	
	public DBSIOException(SQLException e){ 
		super(e);
	}

	public DBSIOException(SQLException e, Connection pConnection){
		super(e, DBSError.toCodes(e, pConnection));
	}

	public DBSIOException(String pMessage){
		super(pMessage);
	}

	public DBSIOException(String pMessage, SQLException e, Connection pConnection){
		super(pMessage, e, DBSError.toCodes(e, pConnection));
	}

	public DBSIOException(DBSIOException e){
		super(e);
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

	
	
//	@Override
//	public String getLocalizedMessage(){
//		if (this.getClass().isInstance(SQLException.class)){
//			return DBSIO.getSQLExceptionMessage((SQLException) this);
//		}
//	}
}
