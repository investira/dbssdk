package br.com.dbsoft.pagedSearch;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.com.dbsoft.util.DBSObject;

public abstract class DBSPagedSearch<DataModelClass> implements IDBSPagedSeach<DataModelClass>{
	
	private List<DataModelClass>	wLista = null;	
	private String					wSearchParam;
	private Integer					wCurrentIndex = -1;
	private Integer 				wPageSize = 20;
	protected Connection			wConnection;
	
	public DBSPagedSearch(Connection pConnection, Integer pPageSize) {
		wConnection = pConnection;
		wPageSize = pPageSize;
	}

	@Override
	public String getSearchParam() {
		return wSearchParam;
	}
	@Override
	public void setSearchParam(String pSearchParam) {
		wSearchParam = pSearchParam;
	}
	@Override
	public List<DataModelClass> getList() {
		if (DBSObject.isNull(wLista)) {
			newSearch();
		}
		return wLista;
	}

	public Integer getPageSize() {
		return wPageSize;
	}
	
	public Integer getCurrentIndex(){
		return wCurrentIndex;
	}
	
	
	//MÉTODOS PÚBLICOS ===========================================================================
	@Override
	public void newSearch() {
//		System.out.println("Pesquisando com parametro: "+ wPesquisaParam);
//		Long xStartTime = System.currentTimeMillis();
		//Inicia uma nova consulta
		beforeNewSearch();
		wLista = new ArrayList<>();
		wCurrentIndex = 0;
		pvSearch();
	}
	@Override
	public void searchMore() {
		pvSearch();
	}

	//MÉTODOS PRIVADOS ===========================================================================
	private void pvSearch() {
		search();
		wCurrentIndex += wLista.size();
	}
	
	protected void beforeNewSearch(){}
	
	//MÉTODOS ABSTRATOS ===========================================================================
	protected abstract void search();
}
