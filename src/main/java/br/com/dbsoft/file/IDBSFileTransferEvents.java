package br.com.dbsoft.file;

public interface IDBSFileTransferEvents {

	/**
	 * Evento ocorre antes de iniciar o filetransfer.<br/>
	 * Este evento ocorre somente uma vez, mesmo que esteja sendo efetuado o transfer de diversos arquivos.<br/>
	 * Para tratar arquivos individualmente, utilize o evento <b>beforeSave</b>.<br/>  
	 * Para impedir o transfer, deve-se setar <b>setOk(False)</b>.
	 * Neste evento deve-se configurar o caminho local onde o arquivo será salvo utilizando o método <b>setLocalPath</b>, 
	 * bem como os listners ser houver.<br/>
	 */
	public void beforeFileTransfer(DBSFileTransferEvent pFileTransfer);

	/**
	 * Chamada quando é finalizada a execução
	 */
	public void afterFileTransfer(DBSFileTransferEvent pFileTransfer);

	/**
	 * Chamada antes de efetivamente iniciar a transfeência do arquivo.
	 * Para impedir o transfer, deve-se setar <b>setOk(False)</b>.
	 */
	public void beforeSave(DBSFileTransferEvent pFileTransfer);

	/**
	 * Chamada após o arquivo ter sido salvo.<br/>
	 * Evneot não será chamado em caso de erro.
	 */
	public void afterSave(DBSFileTransferEvent pFileTransfer);

	/**
	 * Chamada durante o processo de transferência
	 */
	public void transferring(DBSFileTransferEvent pFileTransfer);

	
	/**
	 * Chamada quando é iniciada a execução
	 */
	public void interrupted(DBSFileTransferEvent pFileTransfer);
	
	/**
	 * Chamada quando a execução mudou de situação(Executando, Parado ou Agendada)
	 * Local onde deverá ser implementado a atualização deste dado no banco de dados, se for o caso
	 */
	public void transferStateChanged(DBSFileTransferEvent pFileTransfer);
	
}
