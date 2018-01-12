package br.com.dbsoft.mail;
import java.util.ArrayList;
import java.util.List;


public class DBSEmailMessage {
	
	private DBSEmailAddress 		wFrom = new DBSEmailAddress();
	private List<DBSEmailAddress> 	wTo = new ArrayList<DBSEmailAddress>();
	private List<DBSEmailAddress> 	wCC = new ArrayList<DBSEmailAddress>();
	private List<DBSEmailAddress> 	wBCC = new ArrayList<DBSEmailAddress>();
	private String 					wSubject;
	private String 					wText;
	private List<String> 			wAttachments = new ArrayList<String>();
	private Boolean					wIsHtmlContent = true;
	
	/**
	 * Dados de quem esta enviando o e-mail.
	 * @return
	 */
	public DBSEmailAddress getFrom() {return wFrom;}

	/**
	 * Lista principal de endereços que receberão o e-mail
	 * @return
	 */
	public List<DBSEmailAddress> getTo() {return wTo;}

	/**
	 * Lista de endereço em cópia
	 * @return
	 */
	public List<DBSEmailAddress> getCC() {return wCC;}
	
	/**
	 * Lista de endereço em cópia oculta
	 * @return
	 */
	public List<DBSEmailAddress> getBCC() {return wBCC;}

	public String getSubject() {return wSubject;}
	public void setSubject(String pSubject) {wSubject = pSubject;}
	
	public String getText() {return wText;}
	public void setText(String pText) {wText = pText;}
	
	public List<String> getAttachments() {return wAttachments;}
	
	/**
	 * Se conteúdo da mensagem é HTML.<br/>
	 * O padrão é true.
	 * @return
	 */
	public Boolean getIsHtmlContent() {return wIsHtmlContent;}
	/**
	 * Se conteúdo da mensagem é HTML.<br/>
	 * O padrão é true.
	 * @param pHtmlContent
	 */
	public void setIsHtmlContent(Boolean pHtmlContent) {wIsHtmlContent = pHtmlContent;}
	
	
}
