package br.com.investira.access.interfaces.base;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.investira.access.dados.base.DadosAccessToken;

@JsonSubTypes({
      @JsonSubTypes.Type(value=DadosAccessToken.class)
  })
public interface IAccessToken extends Serializable{
	
	public String getAccessToken();
	public void setAccessToken(String pAccessToken);

}
