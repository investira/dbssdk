package br.com.dbsoft.rest.interfaces;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.dbsoft.rest.dados.DadosIncludeAtivo;

@JsonSubTypes({
      @JsonSubTypes.Type(value=DadosIncludeAtivo.class)
  })
public interface IIncludeAtivo extends Serializable {

	public boolean getIncludeAtivo();
	public void setIncludeAtivo(boolean pIncludeAtivo);
	
}
