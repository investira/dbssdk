package br.com.dbsoft.error;

import org.apache.log4j.Logger;

import br.com.dbsoft.message.IDBSMessageBase;

/**
 * @author ricardo.villar
 *
 */
public class DBSException extends Exception {

	private Integer 		wErrorCode = DBSError.CODES.GENERIC;
	private String 			wMessage = "";
	private IDBSMessageBase wDBSMessage;

	private static final long serialVersionUID = -8315170852933944554L;
	
	protected Logger			wLogger = Logger.getLogger(this.getClass());

	public DBSException(){
		super();
		wLogger.error(this);
	}

	public DBSException(String pMessage, Exception e, Integer pErrorCode){
		super(pMessage, e);
		
		this.addSuppressed(e);
		//Caso erro não tenha sido encontrado na lista de erros tratados, registra no log.
		if (pErrorCode==-1){
			wLogger.error(pMessage, getOriginalException());
		}
		setErrorCode(pErrorCode);
	}

	public DBSException(Exception e, Integer pErrorCode){
		super(e);
		this.addSuppressed(e);
		//Caso erro não tenha sido encontrado na lista de erros tratados, registra no log.
		if (pErrorCode==-1){
			wLogger.error(getOriginalException()); 
		}
		setErrorCode(pErrorCode);
	}
	
	public DBSException(String pMessage, Exception e){
		super(pMessage, e);
		this.addSuppressed(e);
		wLogger.error(pMessage + "\n" + e.getStackTrace()[0].toString(), e);
	}

	public DBSException(Exception e){
		super(e);
		this.addSuppressed(e);
		wLogger.error(e.getMessage() + "\n" + e.getStackTrace()[0].toString());
	}

	public DBSException(String pMessage){
		super(pMessage);
		wLogger.error(pMessage, this);
	}
	
	public DBSException(IDBSMessageBase pMessage){
		super(pMessage.getMessageText());
		wDBSMessage = pMessage;
		wLogger.error(pMessage, this);
	}
	
	public void setErrorCode(Integer pErrorCode){
		wErrorCode = pErrorCode;
		wMessage = DBSError.getErrorMessageSQL(wErrorCode);
	}
	
	public Integer getErrorCode(){
		return wErrorCode;
	}

	@Override
	public String getLocalizedMessage(){
		if (!wMessage.equals("")){
			return wMessage;
		}
		return super.getLocalizedMessage();
	}
	
	/**
	 * Retorna exception original, a partir do qual este foi criado.
	 * @return
	 */
	public Throwable getOriginalException(){
		return this.getSuppressed()[0];
	}
	
//	@Override
//	public void printStackTrace(){
//		wLogger.error(this);
//	}
	
	public IDBSMessageBase getDBSMessage() {
		return wDBSMessage;
	}
	
}
