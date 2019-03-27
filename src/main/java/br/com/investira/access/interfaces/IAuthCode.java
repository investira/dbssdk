package br.com.investira.access.interfaces;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.investira.access.dados.DadosAuthCode;

@JsonSubTypes({@JsonSubTypes.Type(value=DadosAuthCode.class)})
public interface IAuthCode extends Serializable {
	
	public String getCode();
	public void setCode(String pCode);
	
	public Date getIssued();
	public void setIssued(Date pIssued);
	
	public Date getExpires();
	public void setExpires(Date pExpires);
	
	public Object getPayload();
	public void setPayload(Object pPayload);
	
	public Date getVerified();
	public void setVerified(Date pVerified);

}
