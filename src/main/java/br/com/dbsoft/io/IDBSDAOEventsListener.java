package br.com.dbsoft.io;

public interface IDBSDAOEventsListener {
	
	/**
	 * Chamado antes de exeutar o open(Recorset ou file)
	 * @param pEvent Informações do evento
	 */
	public abstract void beforeOpen(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de exeutar a pesquisa SQL do Open ou Refresh
	 * @param pEvent Informações do evento
	 */
	public abstract void afterOpen(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de fechar o Recorset ou file
	 * @param pEvent Informações do evento
	 */
	public abstract void beforeClose(DBSDAOEvent pEvent);
	/**
	 * Chamado antes de fechar o Recorset ou file
	 * @param pEvent Informações do evento
	 */
	public abstract void afterClose(DBSDAOEvent pEvent);

	/**
	 * Chamado antes de exeutar o comando de insert
	 * @param pEvent Informações do evento
	 */
	public abstract void beforeInsert(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de exeutar a pesquisa SQL do Open ou Refresh
	 * @param pEvent Informações do evento
	 */
	public abstract void afterInsert(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de exeutar de leitura de um registro
	 * Neste momento, pode-se reconfigurar o layout das colunas fixas no DBSDBSTxt.
	 * @param pEvent Informações do evento
	 */
	public abstract void beforeRead(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de exeutar a leitura de um registro
	 * @param pEvent Informações do evento
	 */
	public abstract void afterRead(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de exeutar o comando de update
	 * @param pEvent Informações do evento
	 */
	public abstract void beforeUpdate(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de exeutar o update
	 * @param pEvent Informações do evento
	 */
	public abstract void afterUpdate(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de exeutar o comando de merge. Não disparando os eventos de insert e update
	 * @param pEvent Informações do evento
	 */
	public abstract void beforeMerge(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de exeutar o merge(Inclui se não existe/Altera se existe)
	 * @param pEvent Informações do evento
	 */
	public abstract void afterMerge(DBSDAOEvent pEvent);
	
	/**
	 * Chamado antes de exeutar o comando de delete
	 * @param pEvent Informações do evento
	 */
	public abstract void beforeDelete(DBSDAOEvent pEvent);
	/**
	 * Chamado depois de exeutar o comando de delete
	 * @param pEvent Informações do evento
	 */
	public abstract void afterDelete(DBSDAOEvent pEvent);
	

	/**
	 * Chamado antes de mudar a posição atual nos registros
	 * @param pEvent Informações do evento
	 */
	public abstract void beforeMove(DBSDAOEvent pEvent);
	
	/**
	 * Chamado depois de muda a posição atual nos registros
	 * @param pEvent Informações do evento
	 */
	public abstract void afterMove(DBSDAOEvent pEvent);
	

	//public abstract voidbeforeCancel(DBSDAOBase<?> pDAO);
	/**
	 * Chamado depois de exeutar o comando de cancelamento
	 * @param pEvent Informações do evento
	 * @param pOk Se operação foi bem sucedida
	 */
	//public abstract void afterCancel(DBSDAOBase<?> pDAO, boolean pOk);
	
	/**
	 * Chamado antes de exeutar o comando de refresh 
	 * @param pEvent Informações do evento
	 * @return Se deseja continuar
	 */
	//public abstract voidbeforeRefresh(DBSDAOBase<?> pDAO);
	/**
	 * Chamado depois de exeutar o comando de refresh
	 * @param pEvent Informações do evento
	 * @param pOk Se operação foi bem sucedida
	 */
	//public abstract void afterRefresh(DBSDAOBase<?> pDAO, boolean pOk);
	
	
//	public abstract voidbeforeLoad(DBSDAOBase<?> pDAO);
//	public abstract void afterLoad(DBSDAOBase<?> pDAO);
//	
//	public abstract voidbeforeUnload(DBSDAOBase<?> pDAO);
//	public abstract void afterUnload(DBSDAOBase<?> pDAO);
//	
//	public abstract voidbeforePrint(DBSDAOBase<?> pDAO);
//	public abstract void afterPrint(DBSDAOBase<?> pDAO);
//	
//
//	public abstract voidbeforeReprove(DBSDAOBase<?> pDAO);
//	public abstract void afterReprove(DBSDAOBase<?> pDAO);
//	
//	public abstract voidbeforeAprove(DBSDAOBase<?> pDAO);
//	public abstract void afterAprove(DBSDAOBase<?> pDAO);
}

