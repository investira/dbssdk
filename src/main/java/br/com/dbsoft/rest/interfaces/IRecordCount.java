package br.com.dbsoft.rest.interfaces;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.dbsoft.rest.dados.DadosRecordCount;

@JsonSubTypes({@JsonSubTypes.Type(value=DadosRecordCount.class)})
public interface IRecordCount extends Serializable {

	public Integer getRecordCount();
	public void setRecordCount(Integer pRecordCount);
	
}
