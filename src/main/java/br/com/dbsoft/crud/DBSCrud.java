package br.com.dbsoft.crud;

import java.sql.Connection;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.io.DBSDAO;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.DBSMessages;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessageBase.MESSAGE_TYPE;
import br.com.dbsoft.message.IDBSMessages;
import br.com.dbsoft.util.DBSIO;

/**
 * Controle de operação que envolve registro de movimentação e atualização de saldo.
 *
 * @param <DataModelClass>
 */
public abstract class DBSCrud<DataModelClass> implements IDBSCrud<DataModelClass>, IDBSCrudEventsListener<DataModelClass>{
	
	protected Logger	wLogger = Logger.getLogger(this.getClass());
	
	protected Connection 				wConnection = null;
	protected boolean					wOk = true;
	
	protected DBSDAO<Object> 			wDAO;

	protected IDBSMessage	   			wMsgCampoNaoInformado = new DBSMessage(MESSAGE_TYPE.ERROR, "%s não infomado. %s");	 
	protected IDBSMessage	   			wMsgErroGenerico = new DBSMessage(MESSAGE_TYPE.ERROR, "%s");
	protected IDBSMessage	   			wMsgWarningGenerico = new DBSMessage(MESSAGE_TYPE.WARNING, "%s");
	protected IDBSMessage	   			wMsgMergeIndisponivel = new DBSMessage(MESSAGE_TYPE.ERROR, "Operação de Merge indisponível.");
	protected IDBSMessage	   			wMsgDeleteIndisponivel = new DBSMessage(MESSAGE_TYPE.ERROR, "Operação de Delete indisponível.");
	protected IDBSMessage	   			wMsgReadIndisponivel = new DBSMessage(MESSAGE_TYPE.ERROR, "Operação de Read indisponível.");

	private   ICrudAction				wCrudAction = CrudAction.NONE; 
	private   IDBSMessages				wMessages = new DBSMessages(true);
	private	  boolean					wAutoCommit = false;
	private   DataModelClass			wDataModelRead = null;
	

	@SuppressWarnings("rawtypes")
	private List<IDBSCrudEventsListener>	wEventListeners = new ArrayList<IDBSCrudEventsListener>();
	
	/**
	 * É aconselhável que qualquer DBSCrud dentro de outro DBSCrud seja instanciado com <b>AutoCommit = false</b>, 
	 * para evitar conflito com o DBSCrud pai.
	 * @param pConnection Cone
	 * @param pAutoCommit
	 */
	public DBSCrud() {}
	
	/**
	 * É aconselhável que qualquer DBSCrud dentro de outro DBSCrud seja instanciado com <b>AutoCommit = false</b>, 
	 * para evitar conflito com o DBSCrud pai.
	 * @param pConnection Cone
	 * @param pAutoCommit
	 */
	public DBSCrud(Connection pConnection, boolean pAutoCommit) {
		wConnection = pConnection;
		wAutoCommit = pAutoCommit;
	}

	/**
	 * Classe que receberá as chamadas dos eventos quando ocorrerem.<br/>
	 * Para isso, classe deverá implementar a interface DBSTarefa.TarefaEventos<br/>
	 * Lembre-se de remove-la utilizando removeEventListener quando a classe for destruida, para evitar que ela seja chamada quando já não deveria. 
	 * @param pEventListener Classe
	 */
	@SuppressWarnings("rawtypes")
	public final void addEventListener(IDBSCrudEventsListener pEventListener) {
		if (!wEventListeners.contains(pEventListener)){
			wEventListeners.add(pEventListener);
		}
	}

	@SuppressWarnings("rawtypes")
	public final void removeEventListener(IDBSCrudEventsListener pEventListener) {
		if (wEventListeners.contains(pEventListener)){
			wEventListeners.remove(pEventListener);
		}
	}

	@Override
	public final ICrudAction getCrudAction() {
		return wCrudAction;
	}
	
	@Override
	public final boolean isOk() {
		return wOk;
	}

	@Override
	public final DataModelClass read(DataModelClass pDataModel) throws DBSIOException{
		pvInitializeAction(CrudAction.READING);
		pvRead(pDataModel);
		pvFinalizeAction();
		return wDataModelRead;
	}

	

	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrud#validate(java.lang.Object)
	 */
	@Override
	public IDBSMessages validate(DataModelClass pDataModelClass) throws DBSIOException {
		pvFireEventOnValidate(pDataModelClass);
		return getMessages();
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrud#merge(java.lang.Object)
	 */
	@Override
	public final Integer merge(DataModelClass pDataModel) throws DBSIOException{
		pvInitializeAction(CrudAction.MERGING);
		Integer xCount = 0;
		if (pvFireEventBeforeEdit(pDataModel)){
			xCount = pvMerge(pDataModel);
		}
		pvFireEventAfterEdit(pDataModel);
		pvFinalizeAction();
		return xCount;
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrud#delete(java.lang.Object)
	 */
	@Override
	public final Integer delete(DataModelClass pDataModel) throws DBSIOException{
		pvInitializeAction(CrudAction.DELETING);
		Integer xCount = 0;
		if (pvFireEventBeforeEdit(pDataModel)){
			xCount = pvDelete(pDataModel);
		}
		pvFireEventAfterEdit(pDataModel);
		pvFinalizeAction();
		return xCount;
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrud#getMessages()
	 */
	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrud#getMessages()
	 */
	@Override
	public final IDBSMessages getMessages(){
		return wMessages;
	}
	//Abstract methods=============================================================
	/**
	 * Neste método deve-se implementar a leitura integral do registro no banco de dados 
	 * a partir das informações passadas no <b>DataModelClass</b>.<br/>
	 * Deve-se criar <b>novo</b> objeto do tipo <b>DataModelClass</b>, setar seus valores e retorná-lo.<br/>
	 * Deve-se retornar <b>null</b> caso o registro não seja encontrado.<br/>
	 * Para indicar problemas e parar a execução deve-se setar <b>pEvent.setOk(false)</b>.
	 * @param pEvent
	 * @return Novo <b>DataModelClass</b> com os dados lidos
	 * @throws DBSIOException
	 */
	protected abstract DataModelClass onRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Neste método deve-se implementar a exclusão no banco de dados das informações passadas no <b>DataModelClass</b>.<br/>
	 * Deve-se retornar a quantidade de registros afetados.<br/>
	 * Para indicar problemas e parar a execução deve-se setar <b>pEvent.setOk(false)</b>.
	 * @param pEvent
	 * @return Quantidade de registros efetados.
	 */
	protected abstract Integer onDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	/**
	 * Neste método deve-se implementar o merge no banco de dados das informações passadas no <b>DataModelClass</b>.<br/>
	 * Lembrando que para efetuar <b>insert</b> em campos autoincrement a <b>PK</b> deve estar nula, caso contrário será efetuado um <b>update</b>.<br/>
	 * Deve-se retornar a quantidade de registros afetados.<br/>
	 * Para indicar problemas e parar a execução deve-se setar <b>pEvent.setOk(false)</b>.
	 * @param pEvent
	 * @return Quantidade de registros efetados.
	 */
	protected abstract Integer onMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	
	/**
	 * Evento para validação dos dados.<br/>
	 * Para indicar problemas na validação deve-se setar <b>pEvent.setOk(false)</b>.<br/>
	 * @param pEvent
	 * @throws DBSIOException
	 */
	protected abstract void onValidate(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException;

	
	//Eventos ====================================================================
	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrudEventsListener#beforeEdit(br.com.dbsoft.crud.IDBSCrudEvent)
	 */
	@Override
	public void beforeEdit(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrudEventsListener#beforeRead(br.com.dbsoft.crud.IDBSCrudEvent)
	 */
	@Override
	public void beforeRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrudEventsListener#beforeDelete(br.com.dbsoft.crud.IDBSCrudEvent)
	 */
	@Override
	public void beforeDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrudEventsListener#beforeMerge(br.com.dbsoft.crud.IDBSCrudEvent)
	 */
	@Override
	public void beforeMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrudEventsListener#afterEdit(br.com.dbsoft.crud.IDBSCrudEvent)
	 */
	@Override
	public void afterEdit(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}
	
	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrudEventsListener#afterRead(br.com.dbsoft.crud.IDBSCrudEvent)
	 */
	@Override
	public void afterRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrudEventsListener#afterDelete(br.com.dbsoft.crud.IDBSCrudEvent)
	 */
	@Override
	public void afterDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrudEventsListener#afterMerge(br.com.dbsoft.crud.IDBSCrudEvent)
	 */
	@Override
	public void afterMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	/* (non-Javadoc)
	 * @see br.com.dbsoft.crud.IDBSCrudEventsListener#afterError(br.com.dbsoft.crud.IDBSCrudEvent)
	 */
	@Override
	public void afterError(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}


	/**
	 * Retorna texto da mensagem que está na fila
	 * @return
	 */

	//MÉTODOS PRINCIPAIS =======================================================================================
	protected final void pvInitializeAction(ICrudAction pAction){
		wCrudAction = pAction;
		getMessages().clear();
		pvSetOk(true);
	}
	
	protected final void pvFinalizeAction(){
		try {
			//Efetua o commit caso esteja ok e tenha sido configurado como autocommit
			if (isOk() 
			&& wAutoCommit){
				pvSetOk(DBSIO.endTrans(wConnection, isOk()));
			}
		} catch (DBSIOException e) {
			pvSetOk(false);
		}finally {
			wCrudAction = CrudAction.NONE;
		}
	}

	protected final void pvSetOk(boolean pOk){
		wOk = pOk;
	}
	
	//MÉTODOS PRINCIPAIS ACTION =======================================================================================
	private void pvRead(DataModelClass pDataModel) throws DBSIOException{
		wDataModelRead = null;
		if (pDataModel==null){
			pvFireEventAfterError(pDataModel);
		}
		if (pvFireEventBeforeRead(pDataModel)){
			pvFireEventOnRead(pDataModel);
			if (wDataModelRead != null){
				pvFireEventAfterRead(pDataModel);
			}
		}
	}

	private Integer pvMerge(DataModelClass pDataModel) throws DBSIOException{
		if (pDataModel==null){
			pvFireEventAfterError(pDataModel);
			return 0;
		}
		Integer	xCount = 0;
		//Retira lançamento anterior do saldo se lançamento existir
		//Le lançamento
		//Cria savepoint para retornar em caso de erro
		Savepoint xSavePoint = DBSIO.beginTrans(wConnection, "crud");

		if (pvFireEventBeforeMerge(pDataModel)){
			if (pvFireEventOnValidate(pDataModel)){
				//Lê dado anterior se existir e dispara eventos beforeRead e afterRead para dar a oportunidade do cliente efetuar alguma procedimento com os dados anteriores
				pvRead(pDataModel);
				if (isOk()){
					//Compra os dados originais para saber se houve alteração
					if (!DBSIO.dataModelValuesIsEqual(wDataModelRead, pDataModel)){
						//Insere lançamento
						xCount = pvFireEventOnMerge(pDataModel);
						if (isOk()){
							pvFireEventAfterMerge(pDataModel);
						}
					} else {
						getMessages().add(new DBSMessage(MESSAGE_TYPE.INFORMATION, "Não houve alteração."));
					}
				}
			}
		}
		if (!isOk()){
			//Desfaz qualquer modificação no banco de dados efetuada a partir do save point
			DBSIO.endTrans(wConnection, false, xSavePoint);
			pvFireEventAfterError(pDataModel);
		}
		return xCount;
	}
	
	private Integer pvDelete(DataModelClass pDataModel) throws DBSIOException{
		if (pDataModel==null){
			pvFireEventAfterError(pDataModel);
			return 0;
		}

		Integer xCount = 0;
		
		//Cria savepoint para retornar em caso de erro
		Savepoint xSavePoint = DBSIO.beginTrans(wConnection, "crud");

		//Retira do saldo lançamento antigo
		if (pvFireEventBeforeDelete(pDataModel)){
			if (pvFireEventOnValidate(pDataModel)){
				pvRead(pDataModel);
				if (isOk()){
					xCount = pvFireEventOnDelete(wDataModelRead);
					if (isOk()){
						pvFireEventAfterDelete(wDataModelRead);
					}
				}
			}
		}
		if (!isOk()){
			//Desfaz qualquer modificação no banco de dados efetuada a partir do save point
			DBSIO.endTrans(wConnection, false, xSavePoint);
			pvFireEventAfterError(pDataModel);
		}
		return xCount;
	}

	private void pvAfterEventFire(IDBSCrudEvent<DataModelClass> pEvent){
		//Seta set o crud está ok.
		pvSetOk(pEvent.isOk());
		//Adiciona as messagens recebidos do evento(caso existam), as mesagens do crud.
		getMessages().addAll(pEvent.getMessages());
	}
	

	//EVENTOS =======================================================================================
	@SuppressWarnings("unchecked")
	private boolean pvFireEventBeforeEdit(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			beforeEdit(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).beforeEdit(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		return xE.isOk();
	}

	@SuppressWarnings("unchecked")
	private boolean pvFireEventBeforeRead(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			beforeRead(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).beforeRead(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		return xE.isOk();
	}

	@SuppressWarnings("unchecked")
	private boolean pvFireEventBeforeMerge(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			beforeMerge(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).beforeMerge(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		return xE.isOk();
	}
	
	@SuppressWarnings("unchecked")
	private boolean pvFireEventBeforeDelete(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			beforeDelete(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).beforeDelete(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		return xE.isOk();
	}

	@SuppressWarnings("unchecked")
	private void pvFireEventAfterRead(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel, wDataModelRead);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			afterRead(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).afterRead(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
	}
	
	@SuppressWarnings("unchecked")
	private void pvFireEventAfterEdit(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel, wDataModelRead);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			afterEdit(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).afterEdit(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
	}

	@SuppressWarnings("unchecked")
	private void pvFireEventAfterDelete(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel, wDataModelRead);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			afterDelete(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).afterDelete(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
	}

	@SuppressWarnings("unchecked")
	private void pvFireEventAfterMerge(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel, wDataModelRead);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			afterMerge(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).afterMerge(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
	}

	@SuppressWarnings("unchecked")
	private void pvFireEventAfterError(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel, wDataModelRead);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			afterError(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).afterError(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
	}

	private DataModelClass pvFireEventOnRead(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		wDataModelRead = null ;
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			wDataModelRead = onRead(xE); 
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		return wDataModelRead;
	}

	private Integer pvFireEventOnMerge(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel, wDataModelRead);
		Integer xCount = 0;
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			xCount = onMerge(xE);
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		if (xE.isOk()){pvSetOk(xCount > 0);}
		return xCount;
	}

	private Integer pvFireEventOnDelete(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel, wDataModelRead);
		Integer xCount = 0;
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			xCount = onDelete(xE);
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		if (xE.isOk()){pvSetOk(xCount > 0);}
		return xCount;
	}

	private Boolean pvFireEventOnValidate(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			onValidate(xE);
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		return xE.isOk();
	}



}

