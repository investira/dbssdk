package br.com.dbsoft.crudbalance;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Savepoint;

import br.com.dbsoft.crud.DBSCrud;
import br.com.dbsoft.crud.IDBSCrudEvent;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSIO;

/**
 * Controle de operação que envolve registro de movimentação e atualização de saldo.
 *
 * @param <DataModelClass>
 */
public abstract class DBSCrudBalance<DataModelClass> extends DBSCrud<DataModelClass> implements IDBSCrudBalance<DataModelClass> {
	
	protected Date						wDate;
	
	public DBSCrudBalance(Connection pConnection, boolean pAutoCommit) {
		super(pConnection, pAutoCommit);
	}
	
	/**
	 * Calcula e salva saldo.
	 * @param pOperarionData
	 * @return
	 */
	protected abstract boolean onSaveBalance(DataModelClass pOperationData, Boolean pInsert) throws DBSIOException;

	/**
	 * Incrementa data
	 * @throws DBSIOException
	 */
	protected abstract void pvIncrementDate() throws DBSIOException;


	@SuppressWarnings("unchecked")
	@Override
	public final void afterRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
		if (getCrudAction() == CrudAction.MERGING
		 || getCrudAction() == CrudAction.DELETING){
			//Retira lançamento antigo do saldo
			if (!onSaveBalance(pEvent.getDataModelRead(), false)){
				pEvent.getMessages().add(MsgErroCalculandoSaldo);
				pEvent.setOk(false);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final void afterMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
		//Inclui lançamento novo no saldo
		if (!onSaveBalance(pEvent.getDataModel(), true)){
			pEvent.getMessages().add(MsgErroCalculandoSaldo);
			pEvent.setOk(false);
		}
	}


	/**
	 * Recalculando o saldo considerando que o registro do lançamento já existe, porém não está sendo considerado no saldo.<br/>
	 * @return Quantidade de registros efetados.
	 * @throws DBSIOException
	 */
	@Override
	public final boolean reprocessBalance(DataModelClass pDataModel) throws DBSIOException{
		pvInitializeAction(CrudBalanceAction.REPROCESSING);
		boolean xOk = pvReprocessBalance(pDataModel);
		pvFinalizeAction();
		return xOk;
	} 

	//PRIVATE ==========================================================================
	private final boolean pvReprocessBalance(DataModelClass pDataModel) throws DBSIOException{
		boolean xOk = false;
		DataModelClass xDataModelRead = read(pDataModel);
		if (xDataModelRead!=null){
			//Cria savepoint para retornar em caso de erro
			Savepoint xSavePoint = DBSIO.beginTrans(wConnection, "balance");
			//Retira do saldo o lançamento antigo
			xOk = onSaveBalance(xDataModelRead, true);
			DBSIO.endTrans(wConnection, xOk, xSavePoint);
		}
		return xOk;
	} 

}

