package br.com.dbsoft.rest.interfaces;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import br.com.dbsoft.rest.dados.DadosSearchControl;

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
