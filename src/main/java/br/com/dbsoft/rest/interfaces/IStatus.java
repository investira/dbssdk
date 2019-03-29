package br.com.dbsoft.rest.interfaces;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.dbsoft.rest.dados.DadosStatus;

@JsonSubTypes({
    @JsonSubTypes.Type(value=DadosStatus.class)
})
public interface IStatus extends Serializable {
	
	public Boolean getStatus();
	public void setStatus(Boolean pStatus);

}
