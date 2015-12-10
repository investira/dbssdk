package br.com.dbsoft.balance;

import br.com.dbsoft.crud.IDBSCrud;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;

public interface IDBSBalance<DataModelClass> extends IDBSCrud<DataModelClass> {
	
	public enum BalanceAction implements Action {
		REPROCESSING	("Reprocessing", 7);
		
		private String 	wName;
		private int 	wCode;
		
		private BalanceAction(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public BalanceAction get(int pCode) {
			switch (pCode) {
			case 7:
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
