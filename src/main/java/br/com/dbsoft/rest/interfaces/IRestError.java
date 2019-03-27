package br.com.dbsoft.rest.interfaces;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import br.com.dbsoft.rest.dados.DadosRestError;

@JsonSubTypes({@JsonSubTypes.Type(value=DadosRestError.class)})
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="@class", defaultImpl=DadosRestError.class)
public interface IRestError extends Serializable {

	public String getDescription();
	public void setDescription(String pText);
	
	public IRestErrorCode getCode();
	public void setCode(IRestErrorCode pCode);
	
}
