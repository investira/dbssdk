package br.com.dbsoft.message;


import org.joda.time.DateTime;


/**
 * @author ricardo.villar
 *
 */
public class DBSMessage implements IDBSMessage{

	private String			wMessageTextOriginal;
	private String			wMessageText;
	private Boolean			wValidated = null; 
	private MESSAGE_TYPE	wMessageType;
	private Exception		wException;
	private String			wMessageTooltip = "";
	private DateTime		wTime;
	private String			wMessageKey = null;
	private Integer			wMessageCode = 0;
	
	//Construtores============================
	public DBSMessage(){}
	
	public DBSMessage(MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageText, 0, pMessageType, pMessageText);
	}
	
	public DBSMessage(MESSAGE_TYPE pMessageType, Integer pMessageCode, String pMessageText){
		pvSetMessage(pMessageText, pMessageCode, pMessageType, pMessageText);
	}

	public DBSMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText){
		pvSetMessage(pMessageKey,0, pMessageType, pMessageText);
	}
	//=========================================
	
	@Override
	public String getMessageKey(){
		return wMessageKey; 
	}

	@Override
	public void setMessageKey(String pMessageKey){
		wMessageKey = pMessageKey; 
	}
	
	@Override
	public String getMessageText() {
		return wMessageText;
	}
	@Override
	public void setMessageText(String pMessageText) {
		//Seta a chave como o próprio texto caso não tenha seja nula.
		if (wMessageKey == null){
			setMessageKey(pMessageText);
		}
		wMessageText = pMessageText;
	}

	@Override
	public MESSAGE_TYPE getMessageType() {
		return wMessageType;
	}
	/**
	 * Retorna o tipo de mensagem 
	 * @param pMessageType 
	 */
	@Override
	public void setMessageType(MESSAGE_TYPE pMessageType) {
		wMessageType = pMessageType;
	}
	
	/**
	 * Código da mensagem.
	 * @return
	 */
	@Override
	public Integer getMessageCode() {
		return wMessageCode;
	}

	/**
	 * Retorna o código da mensagem, 
	 * @param pMessageCode
	 */
	@Override
	public void setMessageCode(Integer pMessageCode) {
		wMessageCode = pMessageCode;
	}
	
	/**
	 * @return Null = ainda não validada.<br/> True = Validada para verdadeiro.<br/> False = Validada para falso 
	 */
	@Override
	public Boolean isValidated() {
		return wValidated;
	}
	
	@Override
	public void setValidated(Boolean validated) {
		wValidated = validated;
	}
	@Override
	public Exception getException() {
		return wException;
	}
	/**
	 * Configura a exception vinculada a mensagem caso exista
	 * @param pException
	 */
	@Override
	public void setException(Exception pException) {
		this.wException = pException;
	}
	@Override
	public String getMessageTooltip() {
		return wMessageTooltip;
	}
	@Override
	public void setMessageTooltip(String pMessageTooltip) {
		this.wMessageTooltip = pMessageTooltip;
	}
	
	/**
	 * Incorpora os parametros a mensagem padrão definida no construtor.<br/>
	 * A mensagem padrão deverá conter o simbolo %s nas posições que se deseja incluir os parametros informados.
	 * @param pParameters
	 */
	@Override
	public void setMessageTextParameters(Object... pParameters){
		if (wMessageTextOriginal != null){
			this.setMessageText(String.format(wMessageTextOriginal, pParameters));
		}else{
			this.setMessageText(String.format(this.getMessageText(), pParameters));
		}
	}

	/**
	 * Horário que a mensagem foi criada
	 * @return
	 */
	@Override
	public DateTime getTime() {
		return wTime;
	}

	/**
	 * Horário que a mensagem foi criada
	 */
	@Override
	public void setTime(DateTime pTime) {
		wTime = pTime;
	}


	//PRIVATE =========================
	private void pvSetMessage(String pMessageKey, Integer pMessageCode, MESSAGE_TYPE pMessageType, String pMessageText){
		setMessageKey(pMessageKey);
		setMessageCode(pMessageCode);
		setMessageText(pMessageText);
		setMessageType(pMessageType);
		wMessageTextOriginal = pMessageText;
	}


}
