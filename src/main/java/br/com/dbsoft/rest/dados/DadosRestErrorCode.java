package br.com.dbsoft.rest.dados;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.dbsoft.rest.interfaces.IRestErrorCode;

@JsonInclude(value=Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DadosRestErrorCode implements IRestErrorCode{

	private static final long serialVersionUID = -5901728908626285842L;
	
	@JsonProperty("status")
	private Integer			wStatus;
	@JsonProperty("source")
	private String			wSource;
	@JsonProperty("ref")
	private String			wRef;
	
	@Override
	public Integer getStatus() {
		return wStatus;
	}
	@Override
	public void setStatus(Integer pStatus) {
		wStatus = pStatus;
	}
	@Override
	public String getSource() {
		return wSource;
	}
	@Override
	public void setSource(String pSource) {
		wSource = pSource;
	}
	@Override
	public String getRef() {
		return wRef;
	}
	@Override
	public void setRef(String pRef) {
		wRef = pRef;
	}

	
}
