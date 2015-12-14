package br.com.dbsoft.crud;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessages;
import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;

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

	
	public ICrudAction getCrudAction();
	
	public boolean isOk();
	
	//Actions=======================================================================================================
	/**
	 * Lê registro a partir das informações enviadas.<br/>
	 * <ul><b>Eventos disparados</b>
	 * <li>beforeRead</li>
	 * <li>onRead</li>
	 * <li>afterRead</li>
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
	 * <li>beforeEdit</li>
	 * <li>beforeMerge</li>
	 * <li>validate</li>
	 * <li>beforeRead(dados da chave do registro)</li>
	 * <li>onRead(dados da chave do registro)</li>
	 * <li>afterRead(dados do registro se for encontrado)</li>
	 * <li>onMerge</li>
	 * <li>afterMerge(merge efetuado com sucesso)</li>
	 * <li>onError(merge não efetuado)</li>
	 * <li>afterEdit</li>
	 * </ul>
	 * @param pDataModelClass
	 * @return Quantidade de registros efetados.
	 * @throws DBSIOException
	 */
	public Integer merge(DataModelClass pDataModelClass) throws DBSIOException;
	
	
	/**
	 * Excluir registro a partir das informações enviadas.<br/>
	 * <ul><b>Eventos disparados</b>
	 * <li>beforeEdit</li>
	 * <li>beforeDelete</li>
	 * <li>validate</li>
	 * <li>beforeRead(dados da chave do registro)</li>
	 * <li>onRead(dados da chave do registro)</li>
	 * <li>afterRead(dados do registro se for encontrado)</li>
	 * <li>onDelete</li>
	 * <li>afterDelete(quando delete efetuado com sucesso)</li>
	 * <li>onError(delete não efetuado)</li>
	 * <li>afterEdit</li>
	 * </ul>
	 * @param pDataModelClass
	 * @return Quantidade de registros efetados.
	 * @throws DBSIOException
	 */
	public Integer delete(DataModelClass pDataModelClass) throws DBSIOException;
	
	
	/**
	 * Retorna texto da mensagem que está na fila
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public IDBSMessages getMessages();

	
}
