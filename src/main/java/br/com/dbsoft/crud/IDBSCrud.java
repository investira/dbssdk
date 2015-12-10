package br.com.dbsoft.crud;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessages;
import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;

public interface IDBSCrud<DataModelClass> {

	public interface Action{}
	
	public enum CrudAction implements Action {
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

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public Action get(int pCode) {
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

	
	public Action getAction();
	
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
	 * <li>validate</li>
	 * <li>beforeRead(registro antigo se houver)</li>
	 * <li>onRead(registro antigo se houver)</li>
	 * <li>afterRead(registro antigo se houver)</li>
	 * <li>beforeMerge</li>
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
	 * <li>beforeRead(registro antigo se houver)</li>
	 * <li>onRead(registro antigo se houver)</li>
	 * <li>afterRead(registro antigo se houver)</li>
	 * <li>beforeDelete</li>
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
