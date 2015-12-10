package br.com.dbsoft.io;

public interface IDBSDAOEventsListener{
	
	/**
	 * Chamado antes de executar o open(Recorset ou file)
	 * No DAO com <b>setType(TYPE.FIXED_COLUMNS)<b/> este evento deve ser utilizado para configurar as colunas fixas;
	 * @param pEvent Informações do evento
	 */
	public void beforeOpen(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de executar a pesquisa SQL do Open ou Refresh.<br/>
	 * @param pEvent Informações do evento
	 */
	public void afterOpen(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de fechar o Recorset ou file
	 * @param pEvent Informações do evento
	 */
	public void beforeClose(DBSDAOEvent pEvent);
	/**
	 * Chamado antes de fechar o Recorset ou file
	 * @param pEvent Informações do evento
	 */
	public void afterClose(DBSDAOEvent pEvent);

	/**
	 * Chamado antes de executar o comando de insert
	 * @param pEvent Informações do evento
	 */
	public void beforeInsert(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de executar a pesquisa SQL do Open ou Refresh
	 * @param pEvent Informações do evento
	 */
	public void afterInsert(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de executar de leitura de um registro
	 * Neste momento, pode-se reconfigurar o layout das colunas fixas no DBSDBSTxt.
	 * @param pEvent Informações do evento
	 */
	public void beforeRead(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de executar a leitura de um registro
	 * @param pEvent Informações do evento
	 */
	public void afterRead(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de executar o comando de update
	 * @param pEvent Informações do evento
	 */
	public void beforeUpdate(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de executar o update
	 * @param pEvent Informações do evento
	 */
	public void afterUpdate(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de executar o comando de merge. Não disparando os eventos de insert e update
	 * @param pEvent Informações do evento
	 */
	public void beforeMerge(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de executar o merge(Inclui se não existe/Altera se existe)
	 * @param pEvent Informações do evento
	 */
	public void afterMerge(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de executar o comando de delete
	 * @param pEvent Informações do evento
	 */
	public void beforeDelete(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de executar o comando de delete
	 * @param pEvent Informações do evento
	 */
	public void afterDelete(DBSDAOEvent pEvent);
	

	/**
	 * Chamado antes de mudar a posição atual nos registros
	 * @param pEvent Informações do evento
	 */
	public void beforeMove(DBSDAOEvent pEvent);
	
	/**
	 * Chamado depois de muda a posição atual nos registros
	 * @param pEvent Informações do evento
	 */
	public void afterMove(DBSDAOEvent pEvent);
	
}

