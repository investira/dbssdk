package br.com.dbsoft.endpointReturn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosSearchControl implements ISearchControl {

	private static final long serialVersionUID = 2302464238524452487L;

	@JsonProperty("sort")
	private String 	wSort;
	@JsonProperty("page")
	private Integer wPage;
	@JsonProperty("offset")
	private Integer wOffset;
	@JsonProperty("size")
	private Integer wSize;
	
	@Override
	public String getSort() {
		return wSort;
	}
	@Override
	public void setSort(String pSort) {
		wSort = pSort;
	}
	@Override
	public Integer getPage() {
		return wPage;
	}
	@Override
	public void setPage(Integer pOffset) {
		wPage = pOffset;
	}
	@Override
	public Integer getOffset() {
		return wOffset;
	}
	@Override
	public void setOffset(Integer pOffset) {
		wOffset = pOffset;
	}
	@Override
	public Integer getSize() {
		return wSize;
	}
	@Override
	public void setSize(Integer pLimit) {
		wSize = pLimit;
	}
	
}
