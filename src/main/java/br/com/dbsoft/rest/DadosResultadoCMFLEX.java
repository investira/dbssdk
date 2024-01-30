package br.com.dbsoft.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.dbsoft.rest.dados.DadosSearchControl;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties({ "sort", "page", "offset", "size" })
public class DadosResultadoCMFLEX extends DadosSearchControl implements IResultadoCMFLEX{

	private static final long serialVersionUID = -2964462416605160089L;
	
	@JsonProperty("chave")
	private String			wChave;
	
	@JsonProperty("Valor")
	private String			wValor;
	
	@JsonProperty("Mensagem")
	private String			wMensagem;

	@Override
	public String getChave() {
		return wChave;
	}

	@Override
	public void setChave(String pChave) {
		wChave = pChave;
	}

	@Override
	public String getValor() {
		return wValor;
	}

	@Override
	public void setValor(String pValor) {
		wValor = pValor;
	}

	@Override
	public String getMensagem() {
		return wMensagem;
	}

	@Override
	public void setMensagem(String pMensagem) {
		wMensagem = pMensagem;
	}
	
}