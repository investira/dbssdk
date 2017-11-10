package br.com.dbsoft.pagedSearch;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.com.dbsoft.util.DBSObject;

/**
 * Controle para pesquisas paginadas.
 *  
 * @author jose.avila@dbsoft.com.br
 *
 * @param <DataModelClass>
 */
public abstract class DBSPagedSearch<DataModelClass> implements IDBSPagedSeach<DataModelClass>{
	
	private List<DataModelClass>	wLista = null;	
	private String					wSearchParam;
	private Integer					wCurrentIndex = -1;
	private Integer 				wPageSize = 20;
	protected Connection			wConnection;
	
	/**
	 * Construtor Padrão.
	 * @param pConnection Conexão com o Banco de Dados
	 * @param pPageSize Quantidade de registros retornados por página de pesquisa.
	 */
	public DBSPagedSearch(Connection pConnection, Integer pPageSize) {
		wConnection = pConnection;
		wPageSize = pPageSize;
	}

	/**
	 * Retorna o Parametro de Pesquisa.
	 */
	@Override
	public String getSearchParam() {
		return wSearchParam;
	}
	/**
	 * Configura o Parametro de Pesquisa.
	 */
	@Override
	public void setSearchParam(String pSearchParam) {
		wSearchParam = pSearchParam;
	}
	
	/**
	 * Retorna a lista resultado da pesquisa.
	 */
	@Override
	public List<DataModelClass> getList() {
		if (DBSObject.isNull(wLista)) {
			newSearch();
		}
		return wLista;
	}

	/**
	 * Quantidade de registros retornados por página de pesquisa.
	 * @return
	 */
	public Integer getPageSize() {
		return wPageSize;
	}
	
	/**
	 * Retorna o Index corrente da pesquisa.
	 * @return
	 */
	public Integer getCurrentIndex(){
		return wCurrentIndex;
	}
	
	
	//MÉTODOS PÚBLICOS ===========================================================================
	/**
	 * Efetua uma Nova pesquisa.
	 * Esta ação limpa a lista e zera o index de pesquisas.
	 */
	@Override
	public void newSearch() {
		//Inicia uma nova consulta
		beforeNewSearch();
		wLista = new ArrayList<>();
		wCurrentIndex = 0;
		pvSearch();
	}
	
	/**
	 * Efetua uma pesquisa continuando de onde a pesquisa anterior parou (index de pesquisa).
	 */
	@Override
	public void searchMore() {
		pvSearch();
	}

	//MÉTODOS PRIVADOS ===========================================================================
	/**
	 * Efetua uma pesquisa e incrementa o Index corrente.
	 */
	private void pvSearch() {
		search();
		wCurrentIndex += wLista.size();
	}
	
	/**
	 * Método executado antes de uma Nova Pesquisa (newSearch).
	 */
	protected void beforeNewSearch(){}
	
	//MÉTODOS ABSTRATOS ===========================================================================
	/**
	 * Método de Pesquisa. Os resultados devem ser adicionados a lista principal utiliando o método getList().addAll().
	 * Caso a pesquisa retorne blocos de resultados, estes podem ser adicionados 
	 * sob demanda à lista de resultado (getList()). 
	 */
	protected abstract void search();
}
