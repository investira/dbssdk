package br.com.dbsoft.pagedSearch;

import java.util.List;

/**
 * Interface para pesquisas paginadas ({@link DBSPagedSearch}).
 * @author jose.avila@dbsoft.com.br
 *
 * @param <DataModelClass>
 */
public interface IDBSPagedSeach<DataModelClass> {

	/**
	 * Retorna o Parametro de Pesquisa.
	 * @return String
	 */
	public String getSearchParam();
	/**
	 * Configura o Parametro de Pesquisa.
	 * @param pSearchParam
	 */
	public void setSearchParam(String pSearchParam);
	
	/**
	 * Retorna a lista resultado da pesquisa.
	 * @return
	 */
	public List<DataModelClass> getList();
	
	/**
	 * Efetua uma Nova pesquisa.
	 * Esta ação limpa a lista e zera o index de pesquisas.
	 */
	public void newSearch();
	
	/**
	 * Efetua uma pesquisa continuando de onde a pesquisa anterior parou (index de pesquisa).
	 */
	public void searchMore();
}
