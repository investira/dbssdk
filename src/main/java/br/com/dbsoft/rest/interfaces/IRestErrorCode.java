package br.com.dbsoft.rest.interfaces;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import br.com.dbsoft.rest.dados.DadosRestErrorCode;

@JsonSubTypes({@JsonSubTypes.Type(value=DadosRestErrorCode.class)})
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="@class", defaultImpl=DadosRestErrorCode.class)
public interface IRestErrorCode extends Serializable {

	public Integer getStatus();
	public void setStatus(Integer pStatus);

	public String getSource();
	public void setSource(String pSource);
	
	public String getRef();
	public void setRef(String pRef);
	
	
}
