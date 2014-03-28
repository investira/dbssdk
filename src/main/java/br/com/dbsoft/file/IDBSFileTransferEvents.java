package br.com.dbsoft.file;

public interface IDBSFileTransferEvents {
	/**
	 * Chamada quando é iniciada a execução
	 */
	public void started(DBSFileTransfer pFileTransfer);

	/**
	 * Chamada durante a execução
	 */
	public void transferring(DBSFileTransfer pFileTransfer);

	/**
	 * Chamada quando é finalizada a execução
	 */
	public void ended(DBSFileTransfer pFileTransfer);
	
	/**
	 * Chamada quando é iniciada a execução
	 */
	public void interrupted(DBSFileTransfer pFileTransfer);
	
	/**
	 * Chamada quando a execução mudou de situação(Executando, Parado ou Agendada)
	 * Local onde deverá ser implementado a atualização deste dado no banco de dados, se for o caso
	 */
	public void transferStateChanged(DBSFileTransfer pFileTransfer);
	
}
