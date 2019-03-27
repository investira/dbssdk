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
import br.com.investira.access.interfaces.IAuthCode;

@JsonInclude(value=Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DadosAuthCode implements IAuthCode {
	
	private static final long serialVersionUID = 6433984979641137572L;
	
	@JsonProperty("code")
	private String 	wCode;
	@JsonProperty("issued")
	@JsonSerialize(using = DBSDateWithTimeSerializerNew.class)
	@JsonDeserialize(using = DBSDateWithTimeDeserializerNew.class)
	private Date	wIssued;
	@JsonProperty("expires")
	@JsonSerialize(using = DBSDateWithTimeSerializerNew.class)
	@JsonDeserialize(using = DBSDateWithTimeDeserializerNew.class)
	private Date	wExpires;
	@JsonProperty("payload")
	private Object	wPayload;
	@JsonProperty("verified")
	@JsonSerialize(using = DBSDateWithTimeSerializerNew.class)
	@JsonDeserialize(using = DBSDateWithTimeDeserializerNew.class)
	private Date	wVerified;

	@Override
	public String getCode() {
		return wCode;
	}
	@Override
	public void setCode(String pCode) {
		wCode = pCode;
	}
	@Override
	public Date getIssued() {
		return wIssued;
	}
	@Override
	public void setIssued(Date pIssued) {
		wIssued = pIssued;
	}
	@Override
	public Date getExpires() {
		return wExpires;
	}
	@Override
	public void setExpires(Date pExpires) {
		wExpires = pExpires;
	}
	@Override
	public Object getPayload() {
		return wPayload;
	}
	@Override
	public void setPayload(Object pPayload) {
		wPayload = pPayload;
	}
	@Override
	public Date getVerified() {
		return wVerified;
	}
	@Override
	public void setVerified(Date pVerified) {
		wVerified = pVerified;
	}

}
