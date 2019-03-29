package br.com.dbsoft.rest.dados;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.dbsoft.rest.interfaces.IStatus;

@JsonInclude(value=Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DadosStatus implements IStatus {

	private static final long serialVersionUID = -6552296817145232368L;

	@JsonProperty("status")
	private Boolean wStatus;
	
	@Override
	public Boolean getStatus() {
		return wStatus;
	}

	@Override
	public void setStatus(Boolean pStatus) {
		wStatus = pStatus;
	}

}
