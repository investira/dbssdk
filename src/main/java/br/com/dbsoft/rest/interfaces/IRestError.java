package br.com.dbsoft.rest.interfaces;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.dbsoft.rest.dados.DadosRestError;

@JsonSubTypes({
    @JsonSubTypes.Type(value=DadosRestError.class)
})
public interface IRestError extends Serializable {

	public String getText();
	public void setText(String pText);
	
	public Integer getCode();
	public void setCode(Integer pCode);
	
	public Object getStack();
	public void setStack(Object pStack);
	
	public Integer getStatusCode();
	public void setStatusCode(Integer pCode);
}
