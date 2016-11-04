package br.com.dbsoft.message;

import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import org.joda.time.DateTime;

/**
 * @author ricardo.villar
 *
 */
/**
 * @author ricardo.villar
 *
 */
/**
 * @author ricardo.villar
 *
 */
/**
 * @author ricardo.villar
 *
 */
public interface IDBSMessage extends Cloneable{
   	
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
	    Integer wSeverityLevel; 
	    Severity wSeverity;
	    
	    MESSAGE_TYPE (String pCode, String pName, String pIconClass, Boolean pRequireConfirmation, Integer pSeverityLevel){
	    	wCode = pCode;
	    	wName = pName;
	    	wIconClass = pIconClass;
	    	wRequireConfirmation = pRequireConfirmation;
	    	wSeverityLevel = pSeverityLevel; 
			if (wSeverityLevel < 19){
				wSeverity = FacesMessage.SEVERITY_INFO;
			}else if (wSeverityLevel < 29){
				wSeverity = FacesMessage.SEVERITY_WARN;
			}else if (wSeverityLevel < 49){
				wSeverity = FacesMessage.SEVERITY_ERROR;
			}
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

	    public Severity getSeverity(){
	    	return wSeverity;
	    }
	    
	    /**
	     * 1 - Informação simples</br>
	     * 10 - Informação importante</br>
	     * 20 - Confirmação necessárias</br>
	     * 30 - Proibido</br>
	     * 40 - Erro</br>
	     * @return
	     */
	    public Integer getSeverityLevel(){
	    	return wSeverityLevel;
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

	    public static MESSAGE_TYPE get(Severity pSeverity){
			if (pSeverity == null){return null;}
			if (pSeverity == FacesMessage.SEVERITY_ERROR
			 || pSeverity == FacesMessage.SEVERITY_FATAL){
				return MESSAGE_TYPE.ERROR;
			}else if (pSeverity == FacesMessage.SEVERITY_INFO){
				return MESSAGE_TYPE.INFORMATION;
			}else if (pSeverity == FacesMessage.SEVERITY_WARN){
				return MESSAGE_TYPE.IMPORTANT;
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

	/**
	 * Retorna se mensagem foi validada como true.<br/>
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa ou não validada</li>
	 * </ul>
	 */
	public Boolean isMessageValidatedTrue();
	
	/**
	 * Retorna se mensagem foi validada.<br/>
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa</li>
	 * <li>null= Ainda não validada</li>
	 * </ul>
	 */

	public Boolean isMessageValidated();
	/**
	 * Retorna se mensagem foi validada.<br/>
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa</li>
	 * <li>null= Ainda não validada</li>
	 * </ul>
	 */
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
	public Set<String> getMessageSourceIds();
	
	/**
	 * Copia dados de uma mensagem para esta
	 * @return
	 */
	public void copy(IDBSMessage pSourceMessage);

	/**
	 * Verifica se mensagem é iqual a partir da chave da mensagem.
	 * @param pSourceMessage
	 * @return
	 */
	public boolean equals(IDBSMessage pSourceMessage);

	/**
	 * Verifica se mensagem é iqual a partir da chave.
	 * @param pMessageKey
	 * @return
	 */
	public boolean equals(String pMessageKey);

	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	public void setMessageTextParameters(Object... pParameters);

	/**
	 * Retorna lista com os listeners
	 * @return
	 */
	public Set<IDBSMessageListener> getMessageListeners();
	
	/**
	 * Adiciona um listener que receberá os eventos disparados pela mensagem.</br>
	 * Retorna a própria mensagem já com o listener incluído.
	 * @param pMessageListener
	 * @return
	 */
	public IDBSMessage addListener(IDBSMessageListener pMessageListener);
	
	/**
	 * Remove o listener.</br>
	 * Retorna a própria mensagem já com o listener removido.
	 * @param pMessageListener
	 * @return
	 */
	public IDBSMessage removeListener(IDBSMessageListener pMessageListener);

}
