package br.com.dbsoft.pagedSearch;

import java.util.List;

public interface IDBSPagedSeach<DataModelClass> {

	public String getSearchParam();
	public void setSearchParam(String pSearchParam);
	
	public List<DataModelClass> getList();
	
	public void newSearch();
	
	public void searchMore();
}
