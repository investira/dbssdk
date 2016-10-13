package br.com.dbsoft.message;

import java.util.Set;

import org.joda.time.DateTime;

/**
 * @author ricardo.villar
 *
 */
public interface IDBSMessage extends Cloneable{
   	
//	SUCESS		(1, "-sucess"),
//   	INFORMATION	(10, "-information"),
//    WARNING		(20, "-warning"),
//    IMPORTANT	(30, "-important"),
//    ERROR		(40, "-error");
	
//  
	public static final String ATTRIBUTE_NAME = "DBSMESSAGES";

	public static enum MESSAGE_TYPE
	{
	    ABOUT 		("a", "Sobre", "-i_information -green", false, 1),
	    SUCCESS		("s", "Sucesso", "-i_sucess -green", false, 1),
	    INFORMATION	("i", "Informação", "-i_information", false, 10),
	    IMPORTANT	("t", "Importante", "-i_important", false, 10),
	    WARNING 	("w", "Atenção", "-i_warning -yellow", true, 20),
	    CONFIRM		("c", "Confirmar", "-i_question_confirm", true, 20),
	    IGNORE 		("g", "Ignorar", "-i_question_ignore -yellow", true, 20),
	    PROHIBID 	("p", "Proibido", "-i_forbidden -red", false, 30),
	    ERROR 		("e", "Erro", "-i_error -red", false, 40);

	    String 	wCode;
	    String 	wName;
	    String 	wIconClass;
	    Boolean wRequireConfirmation;
	    Integer wSeverity; 
	    
	    MESSAGE_TYPE (String pCode, String pName, String pIconClass, Boolean pRequireConfirmation, Integer pSeverity){
	    	wCode = pCode;
	    	wName = pName;
	    	wIconClass = pIconClass;
	    	wRequireConfirmation = pRequireConfirmation;
	    	wSeverity = pSeverity; 
	    }
	
	    public String getCode(){
	    	return wCode;
	    }
	    
	    public String getName(){
	    	return wName;
	    }
	    
	    public String getIconClass(){
	    	return wIconClass;
	    }

	    public Boolean getRequireConfirmation(){
	    	return wRequireConfirmation;
	    }

	    public Integer getSeverity(){
	    	return wSeverity;
	    }

	    public static MESSAGE_TYPE get(String pType){
			if (pType == null){return null;}
			pType = pType.trim().toLowerCase();
	    	for (MESSAGE_TYPE xCT:MESSAGE_TYPE.values()) {
	    		if (xCT.getCode().equals(pType)){
	    			return xCT;
	    		}
	    	}
	    	return null;
		}
	}
	
	/**
	 * Retorna o código da mensagem
	 * @return
	 */
	public String getMessageKey();
	public void setMessageKey(String pMessageKey);
	
	public String getMessageText();
	public void setMessageText(String pMessageText);

	public MESSAGE_TYPE getMessageType();
	public void setMessageType(MESSAGE_TYPE pMessageType);
	
	public Integer getMessageCode();
	public void setMessageCode(Integer pMessageCode);

	public Boolean isMessageValidated();
	public void setMessageValidated(Boolean validated);

	public Exception getException();
	public void setException(Exception pException);

	public String getMessageTooltip();
	public void setMessageTooltip(String pMessageTooltip);
	
	public DateTime getMessageTime();
	public void setMessageTime(DateTime pTime);
	
	/**
	 * Id's vinculados esta mensagem.<br/>
	 * Esta propriedade pode ser utilizada para devolver a mensagem com informações adicionais sobre a origem.</br>
	 * Como no caso de um validade que retorna mensagens de erro onde é importante saber quais campos foram afetados.<br/>
	 * O valor do <b>id</b> é a critério do usuário.
	 * @return
	 */
	public Set<String> getMessageClientIds();
	
	/**
	 * Copia dados de uma mensagem para esta
	 * @return
	 */
	public void copy(IDBSMessage pSourceMessage);

	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	public void setMessageTextParameters(Object... pParameters);



}
