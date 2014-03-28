package br.com.dbsoft.error;

import org.apache.log4j.Logger;

public class DBSException extends Exception {

	private Integer wErrorCode = DBSError.CODES.GENERIC;

	private static final long serialVersionUID = -8315170852933944554L;
	
	protected static Logger			wLogger = Logger.getLogger(DBSException.class);

	public DBSException(){
		super();
//		wLogger.error(this);
	}

	public DBSException(String pMessage, Exception e, Integer pErrorCode){
		super(pMessage, e);
		this.addSuppressed(e);
		if (pErrorCode==-1){
			wLogger.error(this);
		}
		setErrorCode(pErrorCode);
	}

	public DBSException(Exception e, Integer pErrorCode){
		super(e);
		this.addSuppressed(e);
		if (pErrorCode==-1){
			wLogger.error(this);
		}
		setErrorCode(pErrorCode);
	}
	
	public DBSException(String pMessage, Exception e){
		super(pMessage, e);
		this.addSuppressed(e);
	}

	public DBSException(Exception e){
		super(e);
		this.addSuppressed(e);
//		wLogger.error(e);
	}

	public DBSException(String pMessage){
		super(pMessage);
	}
	
	public void setErrorCode(Integer pErrorCode){
		wErrorCode = pErrorCode;
	}
	
	public Integer getErrorCode(){
		return wErrorCode;
	}

	@Override
	public String getLocalizedMessage(){
		if (wErrorCode != null){
			String xMsg = DBSError.getErrorMessage(wErrorCode.toString());
			if (xMsg !=null && 
				!xMsg.equals("")){
				return xMsg;
			}
		}
		return super.getLocalizedMessage();
	}
	
//	@Override
//	public void printStackTrace(){
//		wLogger.error(this);
//	}
//	
}
