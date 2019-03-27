package br.com.investira.access.interfaces.base;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.investira.access.dados.base.DadosClientName;

@JsonSubTypes({@JsonSubTypes.Type(value=DadosClientName.class)})
public interface IClientName extends Serializable{
	
	public String getClientName();
	public void setClientName(String pClientName);

}
