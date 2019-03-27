package br.com.dbsoft.rest.dados;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.dbsoft.rest.interfaces.IRecordCount;

@JsonInclude(value=Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DadosRecordCount implements IRecordCount {

	private static final long serialVersionUID = -2897715410821351533L;
	
	@JsonProperty("recordCount")
	private Integer wRecordCount;

	@Override
	public Integer getRecordCount() {
		return wRecordCount;
	}
	@Override
	public void setRecordCount(Integer pRecordCount) {
		wRecordCount = pRecordCount;
	}
	
	
}
