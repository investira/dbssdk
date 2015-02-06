package br.com.dbsoft.mail;
import java.util.ArrayList;
import java.util.List;


public class DBSEmailMessage {
	
	private String wFrom;
	private String wFromName;
	private String wTo;
	private String wToName;
	private String wSubject;
	private String wText;
	private List<String> wAttachments = new ArrayList<String>();
	
	public String getFrom() {return wFrom;}
	public void setFrom(String pFrom) {wFrom = pFrom;}
	
	public String getFromName() {return wFromName;}
	public void setFromName(String pFromName) {wFromName = pFromName;}

	public String getTo() {return wTo;}
	public void setTo(String pTo) {wTo = pTo;}

	public String getToName() {return wToName;}
	public void setToName(String pToName) {wToName = pToName;}
	
	public String getSubject() {return wSubject;}
	public void setSubject(String pSubject) {wSubject = pSubject;}
	
	public String getText() {return wText;}
	public void setText(String pText) {wText = pText;}
	
	public List<String> getAttachments() {
		return wAttachments;
	}
	
	
}
