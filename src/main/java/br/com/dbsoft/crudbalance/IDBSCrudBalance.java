package br.com.dbsoft.crudbalance;

import br.com.dbsoft.crud.IDBSCrud;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;

public interface IDBSCrudBalance<DataModelClass> extends IDBSCrud<DataModelClass> {
	
	public interface ICrudBalanceAction extends ICrudAction{
		CrudBalanceAction REPROCESSING = CrudBalanceAction.REPROCESSING;
	}
	public enum CrudBalanceAction implements ICrudAction {
		REPROCESSING	("Reprocessing", 10);
		
		private String 	wName;
		private int 	wCode;
		
		private CrudBalanceAction(String pName, int pCode) {
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
		public CrudBalanceAction get(int pCode) {
			switch (pCode) {
			case 10:
				return REPROCESSING;
			}
			return null;
		}
	}

	// Mensagens
	IDBSMessage MsgErroCalculandoSaldo 					= new DBSMessage(MESSAGE_TYPE.ERROR, "Erro calculando saldo");
			
	//Actions
	public boolean reprocessBalance(DataModelClass pDataModel) throws DBSIOException;
	
	
}
