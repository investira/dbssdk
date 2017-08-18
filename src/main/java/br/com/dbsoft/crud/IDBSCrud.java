package br.com.dbsoft.crud;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessageBase.MESSAGE_TYPE;
import br.com.dbsoft.message.IDBSMessages;

public interface IDBSCrud<DataModelClass> {

	public interface ICrudAction{
		CrudAction NONE = CrudAction.NONE;
		CrudAction READING = CrudAction.READING;
		CrudAction MERGING = CrudAction.MERGING;
		CrudAction DELETING = CrudAction.DELETING;

		public String getName();
		public int getCode();
		public ICrudAction get(int pCode);
	}
	
	public enum CrudAction implements ICrudAction {
		NONE 			("Not Editing", 0),
		READING			("Reading", 1),
		MERGING 		("Merging", 2),
		DELETING 		("Deleting", 3);
		
		private String 	wName;
		private int 	wCode;
		
		private CrudAction(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		@Override
		public String getName() {
			return wName;
		}

		@Override
		public int getCode() {
			return wCode;
		}
		
		@Override
		public ICrudAction get(int pCode) {
			switch (pCode) {
			case 0:
				return NONE;
			case 1:
				return READING;
			case 2:
				return MERGING;
			case 3:
				return DELETING;
			default:
				return NONE;
			}
		}
	}
	
	// Mensagens
	IDBSMessage MsgErroNoPadrao					= new DBSMessage(MESSAGE_TYPE.ERROR, "Erro");

	/**
	 * Retorna qual a ação o crud esta.
	 * @return
	 */
	public ICrudAction getCrudAction();
	
	/**
	 * Retorna se esta OK.
	 * @return
	 */
	public boolean isOk();
	
	//Actions=======================================================================================================
	/**
	 * Lê registro a partir das informações enviadas.<br/>
	 * <ul><b>Eventos disparados</b>
	 * <li>beforeRead(recebe:dados da chave do registro)</li>
	 * <li>onRead(recebe:dados da chave do registro e dados do registro se for encontrado / retorna:dados do registro se for encontrado)</li>
	 * <li>afterRead(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * </ul>
	 * @param pDataModelClass
	 * @return Dados do registro lido
	 * @throws DBSIOException
	 */
	public DataModelClass read(DataModelClass pDataModelClass) throws DBSIOException;

	/**
	 * Incluir ou Alterar registro a partir das informações enviadas.<br/>
	 * Em caso de crud com autoincrement, deve-se enviar campo autoincrement com valor nulo
	 * para evitar que seja efetuado o update, caso a chave exista.
	 * <ul><b>Eventos disparados</b>
	 * <li>beforeEdit(recebe:dados da chave do registro)</li>
	 * <li>beforeMerge(recebe:dados da chave do registro)</li>
	 * <li>validate(recebe:dados da chave do registro)</li>
	 * <li>beforeRead(recebe:dados da chave do registro)</li>
	 * <li>onRead(recebe:dados da chave do registro e dados do registro se for encontrado / retorna:dados do registro se for encontrado)</li>
	 * <li>afterRead(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * <li>onMerge(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * <li>afterMerge(merge efetuado com sucesso)(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * <li>onError(merge não efetuado)(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * <li>afterEdit(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * </ul>
	 * @param pDataModelClass
	 * @return Quantidade de registros efetados.
	 * @throws DBSIOException
	 */
	public Integer merge(DataModelClass pDataModelClass) throws DBSIOException;
	
	
	/**
	 * Excluir registro a partir das informações enviadas.<br/>
	 * <ul><b>Eventos disparados</b>
	 * <li>beforeEdit(recebe:dados da chave do registro)</li>
	 * <li>beforeDelete(recebe:dados da chave do registro)</li>
	 * <li>validate(recebe:dados da chave do registro)</li>
	 * <li>beforeRead(recebe:dados da chave do registro)</li>
	 * <li>onRead(recebe:dados da chave do registro e dados do registro se for encontrado / retorna:dados do registro se for encontrado)</li>
	 * <li>afterRead(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * <li>onDelete(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * <li>afterDelete(deletee efetuado com sucesso)(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * <li>onError(merge não efetuado)(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * <li>afterEdit(recebe:dados da chave do registro e dados do registro se for encontrado)</li>
	 * </ul>
	 * @param pDataModelClass
	 * @return Quantidade de registros efetados.
	 * @throws DBSIOException
	 */
	public Integer delete(DataModelClass pDataModelClass) throws DBSIOException;

	/**
	 * Valida sem efetuar qualquer modificação.<br/>
	 * <ul><b>Evento disparado</b>
	 * <li>validate(recebe:dados da chave do registro)</li>
	 * </ul>
	 * @param pDataModelClass
	 * @throws DBSIOException
	 */
	public IDBSMessages validate(DataModelClass pDataModelClass) throws DBSIOException;

	
	/**
	 * Retorna texto da mensagem que está na fila
	 * @return
	 */
	public IDBSMessages getMessages();

	
}
