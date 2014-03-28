package br.com.dbsoft.balance;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Savepoint;

import org.apache.log4j.Logger;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSIO;

/**
 * Controle de operação que envolve registro de movimentação e atualização de saldo.
 *
 * @param <OperationDataClass>
 */
public abstract class DBSBalance<OperationDataClass> {
	
	protected static Logger	wLogger = Logger.getLogger(DBSBalance.class);
	
	protected Connection 			wConnection;
	protected OperationDataClass 	wOperationData;
	protected Date					wDate;

	
	public DBSBalance(Connection pConnection) {
		wConnection = pConnection;
	}
	
	
	public OperationDataClass getOperationData(){
		return wOperationData;
	}
	
	public void setOperationData(OperationDataClass pOperationData){
		wOperationData = pOperationData;
	}

	/**
	 * Retorna dados da movimentação a partir do conteúdo dos dados informados no <b>OperationData</b> original.<br/>
	 * Deve-se criar novo objeto do tipo <b>OperationDataClass</b> e setar seus valores, a partir da pesquisa no
	 * banco de dados, utilizando as informações do <b>OperationDataClass original</b>.
	 * Deve-se retornar null caso o lançamento não seja encontrado.
	 * @param pOperationId
	 * @return
	 */
	protected abstract OperationDataClass pvReadEntry() throws DBSIOException;

	/**
	 * Excluir movimentação utilizando os dados informados no <b>OperationData</b>.
	 * @param pOperationId
	 * @return Quantidade de registros efetados.
	 */
	protected abstract Integer pvDeleteEntry() throws DBSIOException;
	
	/**
	 * Insert ou Update da movimentação informada no <b>OperationData</b>.<br/>
	 * Para efetuar <b>insert</b> a <b>PK</b> deve estar nula, caso contrário será efetuado um update.
	 * @param pOperarionData
	 * @return Quantidade de registros efetados.
	 */
	protected abstract Integer pvMergeEntry() throws DBSIOException;
	
	/**
	 * Incrementa <b>wData<b/> para a próxima data.
	 * @throws DBSIOException
	 */
	protected abstract void pvIncrementDate() throws DBSIOException;

	
	/**
	 * Calcula e salva saldo.
	 * @param pOperarionData
	 * @return
	 */
	protected abstract boolean pvSaveBalance(OperationDataClass pOperationData, Boolean pInsert) throws DBSIOException;


	/**
	 * Evento disparado ao final do merge executado com sucesso
	 * @return
	 * @throws DBSIOException
	 */
	protected boolean afterMerge() throws DBSIOException{return true;}
	
	/**
	 * Evento disparado ao final do delete executado com sucesso
	 * @return
	 * @throws DBSIOException
	 */
	protected boolean afterDelete() throws DBSIOException{return true;}

	/**
	 * Método público para incluir movimentação ou altera caso já exista, conforme a <b>PK</b> informada.<br/>
	 * É necessário que a <b>PK</b> esteja nula para inclusão em tabelas com autoincrement.<br/>
	 * @return Quantidade de registros efetados.
	 * @throws DBSIOException
	 */
	public final Integer merge() throws DBSIOException{
		boolean xOk = true;
		Integer	xCount = 0;
		//Retira lançamento anterior do saldo se lançamento existir
		//Le lançamento
		//Cria savepoint para retornar em caso de erro
		Savepoint xSavePoint = DBSIO.beginTrans(wConnection, "balance");
		
		OperationDataClass xOperationData;
		xOperationData = pvReadEntry();
		if (xOperationData!=null){
			//Retira lançamento antigo do saldo
			xOk = pvSaveBalance(xOperationData, false);
		}

		//Atualiza saldo com novo lançamento 
		xOk = pvSaveBalance(wOperationData, true);
		if (xOk){
			//Insere lançamento
			xCount = pvMergeEntry();
			xOk = xCount > 0;
		}
		if (xOk){
			afterMerge();
		}else{
			//Desfaz qualquer modificação no banco de dados efetuada a partir do save point
			DBSIO.endTrans(wConnection, xOk, xSavePoint);
		}
		return xCount;
	}

	/**
	 * Método público para exclui movimentação.
	 * @return Quantidade de registros efetados.
	 * @throws DBSIOException
	 */
	public final Integer delete() throws DBSIOException{
		boolean xOk = true;
		Integer xCount = 0;
		//Retira do saldo lançamento antigo
		wOperationData = pvReadEntry();
		if (wOperationData!=null){
			//Cria savepoint para retornar em caso de erro
			Savepoint xSavePoint = DBSIO.beginTrans(wConnection, "balance");
			//Retira do saldo lançamento antigo
			xOk = pvSaveBalance(wOperationData, false);
			if (xOk){
				xCount = pvDeleteEntry();
				xOk = xCount > 0;
			}
			if (xOk){
				afterDelete();
			}else{
				//Desfaz qualquer modificação no banco de dados efetuada a partir do save point
				DBSIO.endTrans(wConnection, xOk, xSavePoint);
			}
		}
		return xCount;
	}

	/**
	 * Recalculando o saldo considerando que o registro do lançamento já exsite, porém não está sendo considerado no saldo.<br/>
	 * @return Quantidade de registros efetados.
	 * @throws DBSIOException
	 */
	public final boolean reprocessBalance() throws DBSIOException{
		boolean xOk = false;
		//Retira do saldo lançamento antigo
		wOperationData = pvReadEntry();
		if (wOperationData!=null){
			//Cria savepoint para retornar em caso de erro
			Savepoint xSavePoint = DBSIO.beginTrans(wConnection, "balance");
			//Retira do saldo lançamento antigo
			xOk = pvSaveBalance(wOperationData, true);
			DBSIO.endTrans(wConnection, xOk, xSavePoint);
		}
		return xOk;
	} 
}

