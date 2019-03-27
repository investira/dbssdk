package br.com.investira.access.dados.base;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.investira.access.interfaces.base.IAccessToken;

@JsonInclude(value=Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DadosAccessToken implements IAccessToken {

	private static final long serialVersionUID = -6970662658617672228L;
	
	@JsonProperty("access_token")
	private String wAccessToken;
	
	@Override
	public String getAccessToken() {
		return wAccessToken;
	}

	@Override
	public void setAccessToken(String pAccessToken) {
		wAccessToken = pAccessToken;
	}

}
