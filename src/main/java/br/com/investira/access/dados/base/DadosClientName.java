package br.com.investira.access.dados.base;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.investira.access.interfaces.base.IClientName;

@JsonInclude(value=Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DadosClientName implements IClientName {

	private static final long serialVersionUID = -4587403905846440996L;
	
	@JsonProperty("client_name")
	private String wClientName;

	@Override
	public String getClientName() {
		return wClientName;
	}

	@Override
	public void setClientName(String pClientName) {
		wClientName = pClientName;
	}

}
