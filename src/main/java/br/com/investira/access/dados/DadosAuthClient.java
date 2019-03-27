package br.com.investira.access.dados;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.investira.access.interfaces.IAuthClient;

@JsonInclude(value=Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DadosAuthClient implements IAuthClient {

	private static final long serialVersionUID = 3088281080690582129L;
	
	@JsonProperty("client_name")
	private String wClientName;
	@JsonProperty("client_secret")
	private String wClientSecret;
	@JsonProperty("client_id")
	private Integer wClientId;

	@Override
	public String getClientName() {
		return wClientName;
	}
	@Override
	public void setClientName(String pClientName) {
		wClientName = pClientName;
	}
	@Override
	public String getClientSecret() {
		return wClientSecret;
	}
	@Override
	public void setClientSecret(String pClientSecret) {
		wClientSecret = pClientSecret;
	}
	@Override
	public Integer getClientId() {
		return wClientId;
	}
	@Override
	public void setClientId(Integer pClientId) {
		wClientId = pClientId;
	}

}
