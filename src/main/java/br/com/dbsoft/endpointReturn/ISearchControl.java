package br.com.dbsoft.endpointReturn;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes({
    @JsonSubTypes.Type(value=DadosSearchControl.class)
})
public interface ISearchControl extends Serializable{

	public String getSort();
	public void setSort(String pSort);
	
	public Integer getPage();
	public void setPage(Integer pPage);
	
	public Integer getOffset();
	public void setOffset(Integer pOffset);
	
	public Integer getSize();
	public void setSize(Integer pSize);
	
}
