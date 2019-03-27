package br.com.investira.access.dados;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.investira.access.interfaces.IAuthUser;

@JsonInclude(value=Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DadosAuthUser implements IAuthUser {

	private static final long serialVersionUID = -1173383455452188522L;
	
	@JsonProperty("_rid")
	private String wRID;
	@JsonProperty("username")
	private String wUsername;
	@JsonProperty("name")
	private String wName;
	@JsonProperty("name_last")
	private String wNameLast;
	@JsonProperty("name_first")
	private String wNameFirst;
	@JsonProperty("name_middle")
	private String wNameMiddle;
	@JsonProperty("phone")
	private String wPhone;
	@JsonProperty("image_url")
	private String wImageURL;
	@JsonProperty("verified")
	private String wVerified;
	@JsonProperty("country")
	private String wCountry;
	@JsonProperty("mobile")
	private String wMobile;
	@JsonProperty("cnpj")
	private String wCNPJ;
	@JsonProperty("cpf")
	private String wCPF;
	@JsonProperty("password")
	private String wPassword;

	@Override
	public String getRID() {
		return wRID;
	}
	@Override
	public void setRID(String pRID) {
		wRID = pRID;
	}
	@Override
	public String getUsername() {
		return wUsername;
	}

	@Override
	public void setUsername(String pUsername) {
		wUsername = pUsername;
	}

	@Override
	public String getName() {
		return wName;
	}

	@Override
	public void setName(String pName) {
		wName = pName;
	}

	@Override
	public String getNameLast() {
		return wNameLast;
	}

	@Override
	public void setNameLast(String pNameLast) {
		wNameLast = pNameLast;
	}

	@Override
	public String getNameFirst() {
		return wNameFirst;
	}

	@Override
	public void setNameFirst(String pNameFirst) {
		wNameFirst = pNameFirst;
	}

	@Override
	public String getNameMiddle() {
		return wNameMiddle;
	}

	@Override
	public void setNameMiddle(String pNameMiddle) {
		wNameMiddle = pNameMiddle;
	}

	@Override
	public String getCPF() {
		return wCPF;
	}

	@Override
	public void setCPF(String pCPF) {
		wCPF = pCPF;
	}

	@Override
	public String getCNPJ() {
		return wCNPJ;
	}

	@Override
	public void setCNPJ(String pCNPJ) {
		wCNPJ = pCNPJ;
	}

	@Override
	public String getPhone() {
		return wPhone;
	}

	@Override
	public void setPhone(String pPhone) {
		wPhone = pPhone;
	}

	@Override
	public String getMobile() {
		return wMobile;
	}

	@Override
	public void setMobile(String pMobile) {
		wMobile = pMobile;
	}

	@Override
	public String getCountry() {
		return wCountry;
	}

	@Override
	public void setCountry(String pCountry) {
		wCountry = pCountry;
	}

	@Override
	public String getVerified() {
		return wVerified;
	}

	@Override
	public void setVerified(String pVerified) {
		wVerified = pVerified;
	}

	@Override
	public String getImageURL() {
		return wImageURL;
	}

	@Override
	public void setImageURL(String pImageURL) {
		wImageURL = pImageURL;
	}

	@Override
	public String getPassword() {
		return wPassword;
	}
	@Override
	public void setPassword(String pPassword) {
		wPassword = pPassword;
	}
}
