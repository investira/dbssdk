package br.com.dbsoft.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe para as respostas dos serviços rest
 * 
 * @param <C>
 */
public class DBSResultadosCMFLEX<C> {

	@JsonProperty("Chave")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wChave;
	
	@JsonProperty("Valor")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wValor;
	
	@JsonProperty("Mensagem")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wMensagem;
	
	public String getChave() {
		return wChave;
	}
	public void setChave(String pChave) {
		wChave = pChave;
	}
	
	public String getValor() {
		return wValor;
	}
	public void setValor(String pValor) {
		wValor = pValor;
	}

	public String getMensagem() {
		return wMensagem;
	}
	public void setMensagem(String pMensagem) {
		wMensagem = pMensagem;
	}
}
