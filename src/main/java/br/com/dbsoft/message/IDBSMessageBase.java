package br.com.dbsoft.message;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import org.joda.time.DateTime;

public interface IDBSMessageBase extends Serializable {

	public static enum MESSAGE_TYPE {
	    /**
	     * Sobre</br>
	     * Chama action novamente após fechar para recuperar o outcome
	     */
	    ABOUT 		("a", "Sobre", "-i_information", false, 1),
	    
	    /**
	     * Sucesso</br>
	     * Chama action novamente após fechar para recuperar o outcome
	     */
	    SUCCESS		("s", "Sucesso", "-i_sucess", false, 1),
	    
	    /**
	     * Info</br>
	     * Chama action novamente após fechar para recuperar o outcome
	     */
	    INFORMATION	("i", "Informação", "-i_information", false, 10),
	    
	    /**
	     * Importante</br>
	     * Chama action novamente após fechar para recuperar o outcome
	     */
	    IMPORTANT	("t", "Importante", "-i_important", false, 11),
	    
	    /**
	     * Warning, Question
	     * Sim: Chama action novamente após fechar com mensagem validada como <b>true</b></br>
	     * Não: Chama action novamente após fechar com mensagem validada como <b>false</b>.
	     */
	    WARNING 	("w", "Atenção", "-i_warning", true, 20),
	    
	    /**
	     * Error, Question</br>
	     * Sim: Chama action novamente após fechar</br>
	     * Não: interrompe o outcome 
	     */
	    CONFIRM		("c", "Confirmar", "-i_question_confirm", true, 30),
	    
	    /**
	     * Error, Question</br>
	     * Sim: Chama action novamente após fechar</br>
	     * Não: interrompe o outcome 
	     */
	    IGNORE		("g", "Ignorar", "-i_question_ignore", true, 31),
	    
	    /**
	     * Error</br>
	     * Interrompe o outcome do action</br> 
	     * Valor após a validação deste tipo deverá ser sempre False.
	     */
	    PROHIBID 	("p", "Proibido", "-i_forbidden", false, 40), 

	    /**
	     * Error</br>
	     * Interrompe o outcome do action</br> 
	     * Valor após a validação deste tipo deverá ser sempre False.
	     */
	    ERROR 		("e", "Erro", "-i_error", false, 50); 
	
	    String 	wCode;
	    String 	wName;
	    String 	wIconClass;
	    Boolean wQuestion;
	    Integer wSeverityLevel; 
	    Severity wSeverity;
	    String 	wInputStyleClass;
	    
	    MESSAGE_TYPE (String pCode, String pName, String pIconClass, Boolean pQuestion, Integer pSeverityLevel){
		    	wCode = pCode;
		    	wName = pName;
		    	wIconClass = pIconClass;
		    	wSeverityLevel = pSeverityLevel; 
		    	wQuestion = pQuestion;
		    	wInputStyleClass = " -msg_" + pCode + " ";
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
	    
	    public String getInputStyleClass(){
	    		return wInputStyleClass;
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
	
	public String getMessageText();
	public void setMessageText(String pMessageText);

	public MESSAGE_TYPE getMessageType();
	public void setMessageType(MESSAGE_TYPE pMessageType);
	
	public Integer getMessageCode();
	public void setMessageCode(Integer pMessageCode);
	
	public DateTime getMessageTime();
	public void setMessageTime(DateTime pTime);
}
