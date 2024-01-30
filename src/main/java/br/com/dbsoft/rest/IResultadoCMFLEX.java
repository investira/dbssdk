package br.com.dbsoft.rest;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import br.com.dbsoft.rest.interfaces.ISearchControl;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="@class", defaultImpl=DadosResultadoCMFLEX.class)
@JsonSubTypes({
      @JsonSubTypes.Type(value=DadosResultadoCMFLEX.class)
  }) 
public interface IResultadoCMFLEX extends ISearchControl{
	
	public String getChave();
	public void setChave(String pChave);
	
	public String getValor();
	public void setValor(String pValor);
	
	public String getMensagem();
	public void setMensagem(String pMensagem);
}