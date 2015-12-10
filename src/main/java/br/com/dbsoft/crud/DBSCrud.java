package br.com.dbsoft.crud;

import java.sql.Connection;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessages;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessages;
import br.com.dbsoft.util.DBSIO;

/**
 * Controle de operação que envolve registro de movimentação e atualização de saldo.
 *
 * @param <DataModelClass>
 */
public abstract class DBSCrud<DataModelClass> implements IDBSCrud<DataModelClass>, IDBSCrudEventsListener<DataModelClass>{
	
	protected Logger	wLogger = Logger.getLogger(this.getClass());
	
	protected Connection 				wConnection;
	protected Action					wAction = CrudAction.NONE; 
	protected boolean					wOk = true;
	private   IDBSMessages<IDBSMessage>	wMessages = new DBSMessages<IDBSMessage>();
	private	  boolean					wAutoCommit = false;	  
	
	@SuppressWarnings("rawtypes")
	private List<IDBSCrudEventsListener>	wEventListeners = new ArrayList<IDBSCrudEventsListener>();
	
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
	public final Action getAction() {
		return wAction;
	}
	
	@Override
	public final boolean isOk() {
		return wOk;
	}

	@Override
	public final DataModelClass read(DataModelClass pDataModel) throws DBSIOException{
		pvInitializeAction(CrudAction.READING);
		DataModelClass xDataModel = pvRead(pDataModel);
		pvFinalizeAction();
		return xDataModel;
	}

	
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
	
	@Override
	@SuppressWarnings("rawtypes")
	public final IDBSMessages getMessages(){
		return wMessages;
	}

	
	@Override
	public void beforeEdit(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}
	
	@Override
	public void beforeRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	@Override
	public void beforeDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}
	
	@Override
	public void beforeMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	@Override
	public void afterEdit(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}
	
	@Override
	public void afterRead(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	@Override
	public void afterDelete(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	@Override
	public void afterMerge(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}

	@Override
	public void onError(IDBSCrudEvent<DataModelClass> pEvent) throws DBSIOException {
	}


	/**
	 * Retorna texto da mensagem que está na fila
	 * @return
	 */

	//MÉTODOS PRINCIPAIS =======================================================================================
	protected final void pvInitializeAction(Action pAction){
		wAction = pAction;
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
			wAction = CrudAction.NONE;
		}
	}

	protected final void pvSetOk(boolean pOk){
		wOk = pOk;
	}
	
	//MÉTODOS PRINCIPAIS ACTION =======================================================================================
	private DataModelClass pvRead(DataModelClass pDataModel) throws DBSIOException{
		if (pDataModel==null){
			pvFireEventOnError(pDataModel);
			return null;
		}
		DataModelClass xDataModel = null;
		if (pvFireEventBeforeRead(pDataModel)){
			xDataModel = pvFireEventOnRead(pDataModel);
			if (xDataModel != null){
				pvFireEventAfterRead(xDataModel);
			}
		}
		return xDataModel;
	}

	private Integer pvMerge(DataModelClass pDataModel) throws DBSIOException{
		if (pDataModel==null){
			pvFireEventOnError(pDataModel);
			return 0;
		}
		boolean xOk = false;
		Integer	xCount = 0;
		//Retira lançamento anterior do saldo se lançamento existir
		//Le lançamento
		//Cria savepoint para retornar em caso de erro
		Savepoint xSavePoint = DBSIO.beginTrans(wConnection, "balance");

		if (pvFireEventValidate(pDataModel)){
			//Lê dado anterior se existir e dispara eventos beforeRead e afterRead para dar a oportunidade do cliente efetuar alguma procedimento com os dados anteriores
			pvRead(pDataModel);
			if (isOk()
			 && pvFireEventBeforeMerge(pDataModel)){
				//Insere lançamento
				xCount = pvFireEventOnMerge(pDataModel);
				if (isOk()){
					pvFireEventAfterMerge(pDataModel);
				}
			}
		}
		if (!isOk()){
			//Desfaz qualquer modificação no banco de dados efetuada a partir do save point
			DBSIO.endTrans(wConnection, xOk, xSavePoint);
			pvFireEventOnError(pDataModel);
		}
		return xCount;
	}
	
	private Integer pvDelete(DataModelClass pDataModel) throws DBSIOException{
		if (pDataModel==null){
			pvFireEventOnError(pDataModel);
			return 0;
		}
		boolean xOk = false;
		Integer xCount = 0;
		
		//Cria savepoint para retornar em caso de erro
		Savepoint xSavePoint = DBSIO.beginTrans(wConnection, "balance");

		//Retira do saldo lançamento antigo
		pDataModel = pvRead(pDataModel);
		if (isOk()){
			if (pvFireEventBeforeDelete(pDataModel)){
				xCount = pvFireEventOnDelete(pDataModel);
				if (isOk()){
					pvFireEventAfterDelete(pDataModel);
				}
			}
		}
		if (!isOk()){
			//Desfaz qualquer modificação no banco de dados efetuada a partir do save point
			DBSIO.endTrans(wConnection, xOk, xSavePoint);
			pvFireEventOnError(pDataModel);
		}
		return xCount;
	}

	@SuppressWarnings("unchecked")
	private void pvAfterEventFire(IDBSCrudEvent<DataModelClass> pEvent){
		pvSetOk(pEvent.isOk());
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
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
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
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
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
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
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
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
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
	private DataModelClass pvFireEventOnRead(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		DataModelClass xDataModel = null;
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			xDataModel = onRead(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).onRead(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		if (xE.isOk()){pvSetOk(xDataModel != null);}
		return xDataModel;
	}

	@SuppressWarnings("unchecked")
	private Integer pvFireEventOnMerge(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		Integer xCount = 0;
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			xCount = onMerge(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).onMerge(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		if (xE.isOk()){pvSetOk(xCount > 0);}
		return xCount;
	}

	@SuppressWarnings("unchecked")
	private Integer pvFireEventOnDelete(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		Integer xCount = 0;
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			xCount = onDelete(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).onDelete(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		if (xE.isOk()){pvSetOk(xCount > 0);}
		return xCount;
	}

	@SuppressWarnings("unchecked")
	private void pvFireEventOnError(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			onError(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).onError(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
	}
	
	@SuppressWarnings("unchecked")
	private Boolean pvFireEventValidate(DataModelClass pDataModel) throws DBSIOException{
		IDBSCrudEvent<DataModelClass> xE = new DBSCrudEvent<DataModelClass>(this, pDataModel);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			onValidate(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).onValidate(xE); 
	        }
		}catch(Exception e){
			DBSIO.throwIOException(e);
		}
		pvAfterEventFire(xE);
		return xE.isOk();
	}



}

