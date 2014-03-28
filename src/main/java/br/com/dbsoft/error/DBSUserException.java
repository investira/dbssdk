package br.com.dbsoft.error;

public class DBSUserException extends DBSException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 665336724879263281L;

	public DBSUserException(String pMessage, Exception e){
		super(pMessage, e);
	}
	
	public DBSUserException(String pMessage){
		super(pMessage);
	}
	
	public DBSUserException(Exception e){
		super(e);
	}
	
	public DBSUserException(DBSUserException e){
		super(e);
	}

}
