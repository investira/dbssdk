package br.com.dbsoft.rest.dados;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.dbsoft.rest.interfaces.IIncludeAtivo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosIncludeAtivo implements IIncludeAtivo {

	private static final long serialVersionUID = -6721214881313090311L;
	
	@JsonProperty("includeAtivo")
	private boolean wIncludeAtivo = false;
	
	@Override
	public boolean getIncludeAtivo() {
		return wIncludeAtivo;
	}

	@Override
	public void setIncludeAtivo(boolean pIncludeAtivo) {
		wIncludeAtivo = pIncludeAtivo;
	}

}
