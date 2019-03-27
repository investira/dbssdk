package br.com.investira.access.interfaces;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.investira.access.dados.DadosAuthClient;

@JsonSubTypes({@JsonSubTypes.Type(value=DadosAuthClient.class)})
public interface IAuthClient extends Serializable{
	
	public String getClientName();
	public void setClientName(String pClientName);

	public String getClientSecret();
	public void setClientSecret(String pClientSecret);
	
	public Integer getClientId();
	public void setClientId(Integer pClientId);
}
