package br.com.investira.access.dados;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.dbsoft.json.resolver.DBSDateWithTimeDeserializerNew;
import br.com.dbsoft.json.resolver.DBSDateWithTimeSerializerNew;
import br.com.investira.access.dados.base.DadosAccessToken;
import br.com.investira.access.interfaces.IAuthToken;

@JsonInclude(value=Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DadosAuthToken extends DadosAccessToken implements IAuthToken {
	
	private static final long serialVersionUID = -473534194072057490L;
	
	@JsonProperty("refresh_token")
	private String 	wRefreshToken;
	@JsonProperty("token_type")
	private String 	wTokenType = "Bearer";
	@JsonProperty("expires")
	@JsonSerialize(using = DBSDateWithTimeSerializerNew.class)
	@JsonDeserialize(using = DBSDateWithTimeDeserializerNew.class)
	private Date 	wExpires;

	@Override
	public String getRefreshToken() {
		return wRefreshToken;
	}
	@Override
	public void setRefreshToken(String pRefreshToken) {
		wRefreshToken = pRefreshToken;
	}
	@Override
	public String getTokenType() {
		return wTokenType;
	}
	@Override
	public Date getExpires() {
		return wExpires;
	}
	
}
