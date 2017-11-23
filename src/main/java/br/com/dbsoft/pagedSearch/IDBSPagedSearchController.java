package br.com.dbsoft.pagedSearch;

import java.util.List;

import br.com.dbsoft.message.IDBSMessages;

/**
 * Interface para pesquisas paginadas ({@link DBSPagedSearchController}).
 * @author jose.avila@dbsoft.com.br
 *
 * @param <DataModelClass>
 */
public interface IDBSPagedSearchController<DataModelClass> {

	/**
	 * Retorna texto da mensagem que está na fila
	 * @return
	 */
	public IDBSMessages getMessages();
	
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
