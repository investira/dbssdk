package br.com.dbsoft.message;

import java.io.Serializable;
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
public interface IDBSMessage extends Serializable{
   	
	public static enum MESSAGE_TYPE
	{
	    /**
	     * Info
	     */
	    ABOUT 		("a", "Sobre", "-i_information -green", false, 1),
	    /**
	     * Info
	     */
	    SUCCESS		("s", "Sucesso", "-i_sucess -green", false, 1),
	    /**
	     * Info
	     */
	    INFORMATION	("i", "Informação", "-i_information", false, 10),
	    /**
	     * Info
	     */
	    IMPORTANT	("t", "Importante", "-i_important", false, 10),
	    /**
	     * Warning, Question
	     */
	    WARNING 	("w", "Atenção", "-i_warning -yellow", true, 20),
	    /**
	     * Error, Question 
	     * Valor após a validação deste tipo, será sempre False
	     */
	    CONFIRM		("c", "Confirmar", "-i_question_confirm", true, 30),
	    /**
	     * Error, Question 
	     * Valor após a validação deste tipo, será sempre False
	     */
	    IGNORE		("g", "Ignorar", "-i_question_ignore -yellow", true, 31),
	    /**
	     * Error 
	     * Valor após a validação deste tipo deverá ser sempre False.
	     */
	    PROHIBID 	("p", "Proibido", "-i_forbidden -red", false, 40), 
	    /**
	     * Error 
	     * Valor após a validação deste tipo deverá ser sempre False.
	     */
	    ERROR 		("e", "Erro", "-i_error -red", false, 50); 

	    String 	wCode;
	    String 	wName;
	    String 	wIconClass;
	    Boolean wQuestion;
	    Integer wSeverityLevel; 
	    Severity wSeverity;
	    
	    MESSAGE_TYPE (String pCode, String pName, String pIconClass, Boolean pQuestion, Integer pSeverityLevel){
	    	wCode = pCode;
	    	wName = pName;
	    	wIconClass = pIconClass;
	    	wSeverityLevel = pSeverityLevel; 
	    	wQuestion = pQuestion;
			if (wSeverityLevel < 20){
				wSeverity = FacesMessage.SEVERITY_INFO;
			}else if (wSeverityLevel < 30){
				wSeverity = FacesMessage.SEVERITY_WARN;
			}else{
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

	    /**
	     * @return Se mensagem é SEVERITY_WARN.
	     */
	    public Boolean getIsInfo(){
	    	if (wSeverity == FacesMessage.SEVERITY_INFO){
	    		return true;
	    	}
	    	return false;
	    }

	    /**
	     * @return Se mensagem é SEVERITY_INFO. 
	     */
	    public Boolean getIsWarnings(){
	    	if (wSeverity == FacesMessage.SEVERITY_WARN){
	    		return true;
	    	}
	    	return false;
	    }
	    
	    /**
	     * @return Se mensagem é SEVERITY_ERROR. 
	     */
	    public Boolean getIsError(){
	    	if (wSeverity == FacesMessage.SEVERITY_ERROR){
	    		return true;
	    	}
	    	return false;
	    }

	    
	    /**
	     * @return Se mensagem querer pergunta.
	     */
	    public Boolean getIsQuestion(){
	    	return wQuestion;
	    }

	    /**
	     * @return FacesMassage.Severity
	     */
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
	public boolean isMessageValidatedTrue();
	
	/**
	 * Retorna se mensagem foi validada.<br/>
	 * Mensagens de erro e fatais que não são pergutas(Questions), são sempre validadas como false.
	 * @param pMessageKey
	 * @return <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa</li>
	 * <li>null= Ainda não validada</li>
	 * </ul>
	 */
	public Boolean isMessageValidated();

	
	/**
	 * Seta se mensagem foi validada.<br/>
	 * Dispara os <b>IDBSMessageListener</b> que eventualmente existam atrelados a esta mensagem.
	 * @param <ul><li>true= Validada como afirmativa</li>
	 * <li>false= Validada como negativa</li>
	 * <li>null= Ainda não validada</li>
	 * </ul>
	 */
	public void setMessageValidated(Boolean validated);
	
	/**
	 * Reseta mensagem como não validada(null) sem disparar os <b>IDBSMessageListener</b> 
	 * que eventualmente existam atrelados a ela. 
	 */
	public void reset();

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
	public IDBSMessage addMessageListener(IDBSMessageListener pMessageListener);
	/**
	 * Remove o listener.</br>
	 * Retorna a própria mensagem já com o listener removido.
	 * @param pMessageListener
	 * @return
	 */
	public IDBSMessage removeMessageListener(IDBSMessageListener pMessageListener);
	/**
	 * Copia dados de uma mensagem para esta
	 * @return
	 */
	public void copyFrom(IDBSMessage pSourceMessage);

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

	public IDBSMessage clone();

	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	public void setMessageTextParameters(Object... pParameters);

	public Exception getException();
	public void setException(Exception pException);
	
}
