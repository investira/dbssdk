package br.com.dbsoft.rest.dados;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.dbsoft.rest.interfaces.IRestError;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosRestError implements IRestError{

	private static final long serialVersionUID = 3059562643463340866L;
	
	@JsonProperty("text")
	private String			wText;
	@JsonProperty("code")
	private Integer			wCode = 0;
	@JsonProperty("status")
	private Integer			wStatus = 0;
	
	@Override
	public String getText() {
		return wText;
	}
	@Override
	public void setText(String pText) {
		wText = pText;
	}
	@Override
	public Integer getCode() {
		return wCode;
	}
	@Override
	public void setCode(Integer pCode) {
		wCode = pCode;
	}
	@Override
	public Integer getStatus() {
		return wStatus;
	}
	@Override
	public void setStatus(Integer pStatus) {
		wStatus = pStatus;
	}
}
