package br.com.dbsoft.pagedSearch;

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
public abstract class DBSPagedSearchController<DataModelClass> {
	
	private 	List<DataModelClass>	wLista = null;
	private		DataModelClass			wSelectedItem;
	private		Integer					wSelectedRow;
	private 	String					wSearchParam;
	private 	Integer					wCurrentIndex = -1;
	private 	Integer 				wPageSize = 20; //Valor Default
	
	/**
	 * Construtor Padrão.
	 * @param pConnection Conexão com o Banco de Dados
	 * @param pPageSize Quantidade de registros retornados por página de pesquisa.
	 */
	public DBSPagedSearchController(Integer pPageSize) {
		wPageSize = pPageSize;
	}

	/**
	 * Retorna a lista resultado da pesquisa.
	 */
	public List<DataModelClass> getList() {
		if (DBSObject.isNull(wLista)) {
			newSearch();
		}
		return wLista;
	}
	
	/**
	 * Retorna o Objeto selecionado (via click) na lista de resultados.<br/>
	 * Esta seleção só é feita se o atributo keyColumnName estiver configurado.
	 * @return
	 */
	public DataModelClass getSelectedItem() {
		return wSelectedItem;
	}

	/**
	 * Configura o Objeto selecionado (via click) na lista de resultados.<br/>
	 * Esta seleção só é feita se o atributo keyColumnName estiver configurado.
	 * @param pSelectedItem
	 */
	public void setSelectedItem(DataModelClass pSelectedItem) {
		wSelectedItem = pSelectedItem;
	}
	
	public Integer getSelectedRow() {
		return wSelectedRow;
	}

	public void setSelectedRow(Integer pSelectedRow) {
		wSelectedRow = pSelectedRow;
	}
	
	/**
	 * Retorna o Parametro de Pesquisa.
	 * Use-o na pesquisa do Search, mesmo que tenha definido um outro campo no atributo VALOR. 
	 */
	public String getSearchParam() {
		return wSearchParam;
	}
	/**
	 * Configura o Parametro de Pesquisa.
	 */
	public void setSearchParam(String pSearchParam) {
		wSearchParam = pSearchParam;
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
	public void newSearch() {
		//Inicia uma nova consulta
		beforeNewSearch();
		wLista = new ArrayList<DataModelClass>();
		wCurrentIndex = 0;
		searchMore();
	}
	
	/**
	 * Efetua uma pesquisa continuando de onde a pesquisa anterior parou (index de pesquisa).
	 */
	public void searchMore() {
		search();
		wCurrentIndex += wLista.size();
	}
	
	public void selectItem() {
		setSelectedItem(getList().get(getSelectedRow()));
//		System.out.println("Objeto "+ getSelectedItem() +" selecionado.");
//		System.out.println("Objeto na posição "+ getSelectedRow() +" selecionado.");
	}

	//MÉTODOS PRIVADOS ===========================================================================
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
