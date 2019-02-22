package br.com.dbsoft.rest.dados;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.dbsoft.rest.interfaces.IRestError;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(value = { "statusCode" }, ignoreUnknown = true)
public class DadosRestError implements IRestError{

	private static final long serialVersionUID = 3059562643463340866L;
	
	@JsonProperty("text")
	private String			wText;
	@JsonProperty("code")
	private Integer			wCode = 0;
	@JsonProperty("stack")
	@JsonInclude(value=Include.NON_NULL, content=Include.NON_NULL)
	private Object 			wStack;
	@JsonProperty("statusCode")
	private Integer			wStatusCode = 0;
	
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
	public Object getStack() {
		return wStack;
	}
	@Override
	public void setStack(Object pStack) {
		wStack = pStack;
	}
	@Override
	public Integer getStatusCode() {
		return wStatusCode;
	}
	@Override
	public void setStatusCode(Integer pStatusCode) {
		wStatusCode = pStatusCode;
	}
	
}
