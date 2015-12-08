package br.com.dbsoft.message;


import org.joda.time.DateTime;

import br.com.dbsoft.util.DBSNumber;

/**
 * @author ricardo.villar
 *
 */
public interface IDBSMessage extends Cloneable{

	public static enum MESSAGE_TYPE{
	   	SUCESS		(1, "-sucess"),
	   	INFORMATION	(10, "-information"),
	    WARNING		(20, "-warning"),
	    IMPORTANT	(30, "-important"),
	    ERROR		(40, "-error");

	    Integer wCode;
	    String wName;
		
	    public static MESSAGE_TYPE get(Object pCode) {
			Integer xI = DBSNumber.toInteger(pCode);
			if (xI != null){
				return get(xI);
			}else{
				return null;
			}
		}
	    
		public static MESSAGE_TYPE get(Integer pCode) {
			switch (pCode) {
			case 1:
				return MESSAGE_TYPE.SUCESS;
			case 10:
				return MESSAGE_TYPE.INFORMATION;
			case 20:
				return MESSAGE_TYPE.WARNING;
			case 30:
				return MESSAGE_TYPE.IMPORTANT;
			case 40:
				return MESSAGE_TYPE.ERROR;
			}
			return null;
		}
	    
	    MESSAGE_TYPE (Integer pCode, String pName){
	    	wCode = pCode;
	    	wName = pName;
	    }
	    
		public String getName() {
			return wName;
		}
	
		public int getCode() {
			return wCode;
		}
	}
	
	public String getMessageKey();
	public void setMessageKey(String pMessageKey);
	
	public String getMessageText();
	
	public void setMessageText(String pMessageText);

	public MESSAGE_TYPE getMessageType();
	public void setMessageType(MESSAGE_TYPE pMessageType);
	
	public Integer getMessageCode();
	public void setMessageCode(Integer pMessageCode);
	
	public Boolean isValidated();
	
	public void setValidated(Boolean validated);
	public Exception getException();
	
	public void setException(Exception pException);
	public String getMessageTooltip();
	
	public void setMessageTooltip(String pMessageTooltip);
	
	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	public void setMessageTextParameters(Object... pParameters);

	/**
	 * Horário que a mensagem foi criada
	 * @return
	 */
	public DateTime getMessageTime();

	/**
	 * Horário que a mensagem foi criada
	 */
	public void setMessageTime(DateTime pTime);


}
