package br.com.investira.access.interfaces;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.investira.access.dados.DadosAuthToken;

@JsonSubTypes({@JsonSubTypes.Type(value=DadosAuthToken.class)})
public interface IAuthToken extends Serializable {

	public String getAccessToken();
	public void setAccessToken(String pAccessToken);
	
	public String getRefreshToken();
	public void setRefreshToken(String pRefreshToken);

	public String getTokenType();
	
	public Date getExpires();
	
}
