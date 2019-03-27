package br.com.investira.access.interfaces;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.investira.access.dados.DadosAuthUser;

@JsonSubTypes({
    @JsonSubTypes.Type(value=DadosAuthUser.class)
})
public interface IAuthUser extends Serializable {

	public String getRID();
	public void setRID(String pRID);
	
	public String getUsername();
	public void setUsername(String pUsername);	
	
	public String getPassword();
	public void setPassword(String pPassword);
	
	public String getName();
	public void setName(String pName);

	public String getNameLast();
	public void setNameLast(String pNameLast);
	
	public String getNameFirst();
	public void setNameFirst(String pNameFirst);
	
	public String getNameMiddle();
	public void setNameMiddle(String pNameMiddle);	

	public String getCPF();
	public void setCPF(String pCPF);
	
	public String getCNPJ();
	public void setCNPJ(String pCNPJ);
	
	public String getPhone();
	public void setPhone(String pPhone);
	
	public String getMobile();
	public void setMobile(String pMobile);
	
	public String getCountry();
	public void setCountry(String pCountry);
	
	public String getVerified();
	public void setVerified(String pVerified);
	
	public String getImageURL();
	public void setImageURL(String pImageURL);
	
}
