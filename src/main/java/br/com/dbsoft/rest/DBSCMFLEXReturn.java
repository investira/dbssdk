package br.com.dbsoft.rest;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe para as respostas dos serviços rest
 * 
 * @param <C>
 */
public class DBSCMFLEXReturn<C> {

	@JsonProperty("StatusEnum")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private Integer wStatusEnum;
	
	@JsonProperty("Status")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wStatus;
	
	@JsonProperty("Mensagem")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private String wMensagem;
	
	@JsonProperty("Id")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private Integer wId;
	
	@JsonProperty("Resultados")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private ArrayList<DBSResultadosCMFLEX<C>> wResultados;
	
	public Integer getStatusEnum() {
		return wStatusEnum;
	}
	public void setStatusEnum(Integer pStatusEnum) {
		wStatusEnum = pStatusEnum;
	}

	public String getStatus() {
		return wStatus;
	}
	public void setStatus(String pStatus) {
		wStatus = pStatus;
	}
	
	public String getMensagem() {
		return wMensagem;
	}
	public void setMensagem(String pMensagem) {
		wMensagem = pMensagem;
	}
	
	public Integer getId() {
		return wId;
	}
	public void setId(Integer pId) {
		wId = pId;
	}
	
	public ArrayList<DBSResultadosCMFLEX<C>> getResultados() {
		return wResultados;
	}
	
	public void setResultados(ArrayList<DBSResultadosCMFLEX<C>> pResultados) {
		wResultados = pResultados;
	}
}
