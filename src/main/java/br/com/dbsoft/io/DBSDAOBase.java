package br.com.dbsoft.io;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.dbsoft.annotation.DBSTableModel;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.util.DBSIO;

/**
 * @param <DataModelClass> Classe Model da tabela do banco de dados ou classe com atributos homônimos as colunas com as quais se deseje trabalhar no DAO.<br/>
 * É necessário também passar esta classe no construtor.  
 */
public abstract class DBSDAOBase<DataModelClass> implements Serializable, IDBSDAOEventsListener {
	
	private static final long serialVersionUID = -217620999386905654L;

	protected Logger			wLogger = Logger.getLogger(this.getClass());
	
	private List<IDBSDAOEventsListener>		wEventListeners = new ArrayList<IDBSDAOEventsListener>();
	private boolean 						wRowPositionChanged = false;
	private boolean							wExecuteOnlyChangedValues = true;
	
	protected Class<DataModelClass>			wDataModelClass = null;
	protected DataModelClass                wDataModel;
	private boolean							wCallMoveEvent = true;
	private int								wRowsCountAfterRefresh = 0;
	private boolean							wShowColumnNameNotFoundMessage = true;
	
	protected static final String UKName = "DBSUK";

	DBSDAOBase(){}

	DBSDAOBase(Class<DataModelClass> pDataModelClass){
		this.setDataModelClass(pDataModelClass);
	}

	/**
	 * Classe que receberá as chamadas dos eventos quando ocorrerem.<br/>
	 * Para isso, classe deverá implementar a interface DBSTarefa.TarefaEventos<br/>
	 * Lembre-se de remove-la utilizando removeEventListener quando a classe for destruida, para evitar que ela seja chamada quando já não deveria. 
	 * @param pEventListener Classe
	 */
	public void addEventListener(IDBSDAOEventsListener pEventListener) {
		if (!wEventListeners.contains(pEventListener)){
			wEventListeners.add(pEventListener);
		}
	}

	public void removeEventListener(IDBSDAOEventsListener pEventListener) {
		if (wEventListeners.contains(pEventListener)){
			wEventListeners.remove(pEventListener);
		}
	}
	
	public Class<DataModelClass> getDataModelClass() {
		return wDataModelClass;
	}

	public final void setDataModelClass(Class<DataModelClass> pDataModelClass) {
		this.wDataModelClass = pDataModelClass;
		pvCreateDataModel();
	}
	
	/**
	 * Atualizada a propriedade/coluna do wDataModel local com o valor informado
	 * @param pColumnName Nome da propriedade/coluna da classe
	 * @param pValue Valor
	 */
	protected final boolean pvSetLocalDataModelValue(String pColumnName, Object pValue){
		return pvSetDataModelValue(wDataModel, pColumnName, pValue);
	}
	
	/**
	 * Atualizada a propriedade/coluna do wDataModel local com o valor informado
	 * @param pColumnIndex Número da propriedade/coluna da classe
	 * @param pValue Valor
	 */
	protected final void pvSetLocalDataModelValue(int pColumnIndex, Object pValue){
		pvSetDataModelValue(wDataModel, pColumnIndex, pValue);
	}
	
	/**
	 * Retorna o valor da propriedade/coluna do wDataModel local
	 * @param pColumnName Nome da coluna Nome da propriedade/coluna da classe
	 * @return Valor da coluna
	 */
	protected final <A> A pvGetLocalDataModelValue(String pColumnName){
		return DBSIO.getDataModelFieldValue(wDataModel, pColumnName);
		//return pvGetDataModelValue(wDataModel, pColumnName);
	}

	/**
	 * Atualizada a propriedade/coluna do DataModel informado com o valor informado
	 * @param pDataModel DataModel a ser utilizado
	 * @param pColumnName Nome da propriedade/coluna da classe
	 * @param pValue Valor
	 */
	protected final boolean pvSetDataModelValue(DataModelClass pDataModel, String pColumnName, Object pValue){
		if (pDataModel == null){return false;}
		String xColumnName = pColumnName.toUpperCase().trim();
		return DBSIO.setDataModelFieldsValue(pDataModel, xColumnName, pValue);
	}	

	/**
	 * Atualizada a propriedade/coluna do DataModel informado com o valor informado
	 * @param pDataModel DataModel a ser utilizado
	 * @param pColumnIndex Número da propriedade/coluna da classe
	 * @param pValue Valor
	 */
	protected final void pvSetDataModelValue(DataModelClass pDataModel, int pColumnIndex, Object pValue){
		DBSIO.setDataModelFieldsValue(pDataModel, pColumnIndex, pValue);
	}	
	
	/**
	 Cria dataModel em branco conforme a class informada no construtor. 
	 */
	protected final void pvCreateDataModel(){
		wDataModel = createDataModel();
	}
	

	/**
	 * Indicador se exibe mensagem de alerta quando pesquisar 
	 * o valor de uma coluna que não exista na query.<br/>
	 * O padrão é true.
	 * @return
	 */
	public boolean getShowColumnNameNotFoundMessage() {
		return wShowColumnNameNotFoundMessage;
	}

	/**
	 * Indicador se exibe mensagem de alerta quando pesquisar 
	 * o valor de uma coluna que não exista na query.<br/>
	 * O padrão é true.
	 * @return
	 */
	public void setShowColumnNameNotFoundMessage(
			boolean pShowColumnNameNotFoundMessage) {
		wShowColumnNameNotFoundMessage = pShowColumnNameNotFoundMessage;
	}
	

	/**
	 * Retorna o valor do atributo/coluna da classe genérica 
	 * @param pDataModel Classe generica
	 * @param pColumnName Nome da Coluna
	 * @return 
	 */
	//TODO Rotina substituida por DBSIO.getDataModelValue. Verificar a necessidade de manter este código
//	@SuppressWarnings("unchecked")
//	protected final <A> A pvGetDataModelValue(T pDataModel, String pColumnName){
//		//Verifica se coluna existe como sendo uma propriedade de pDataModel
//		if (wDataModelPropertiesNames.containsKey(pColumnName)){
//			//Se pDataModel não for nulo e possuir propriedades
//			if (pDataModel != null &&
//				pDataModel.getClass().getDeclaredFields().length > 0){
//				try {
//					Field xField = wDataModel.getClass().getDeclaredField(pColumnName);
//					xField.setAccessible(true);
//					return (A) xField.get(pDataModel);
//					//return (A) xField.getGenericType();
//				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//					e.printStackTrace();
//					return null;
//				}
//			}
//			return null;
//		}else{
//			return null;
//		}
//	}	

	/**
	 * Lé integralmente os registros da origem dos dados e retorna ono formato List 
	 * @param pReturnListDataModel
	 * @return
	 * @throws DBSIOException
	 */
	protected abstract <A> List<A> pvGetList(boolean pReturnListDataModel) throws DBSIOException;

	/**
	 * Copia os valores do DataModel para os valores locais
	 * Ignora, se data model for nulo.
	 * @param pDataModel
	 */
	protected void pvCopyDataModelFieldsValueToCommandValue(DataModelClass pDataModel){
		pvCopyDataModelFieldsValueToCommandValue(pDataModel, false);
	}
	
	/**
	 * Copia os valores do DataModel para os valores locais.
	 * Ignora, se data model for nulo.
	 * @param pDataModel
	 * @param pOriginalValue - Indica se são valores originals, para controle de quais colunas tiveram seus valores alterados
	 */
	protected void pvCopyDataModelFieldsValueToCommandValue(DataModelClass pDataModel, boolean pOriginalValue){
		//Verifica se o DataModel ou a Linha estão nulos ou o ResultSet
		if (pDataModel == null	||
			pDataModel.getClass().getDeclaredFields().length == 0){ 
			return;
		}
		//Obtem os atributos do DataModel passado
		Field xFields[] = pDataModel.getClass().getDeclaredFields();
		//Loop para percorrer os atributos do Objeto passado
		for (Field xField : xFields) {
			try { 
				//Ignora se campo tiver sido declarado como static
				if (!java.lang.reflect.Modifier.isStatic(xField.getModifiers())){
					xField.setAccessible(true);
					Annotation xAnnotationTmp = xField.getType().getAnnotation(DBSTableModel.class);
					if (!(xAnnotationTmp instanceof DBSTableModel)){
						setValue(xField.getName(), xField.get(pDataModel), pOriginalValue);
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {  
				wLogger.error(e);
			}
		}
	}

	/**
	 * Cria instancia do dataModel conforme a class informada no construtor.
	 * @return Classe criada
	 */
	public final DataModelClass createDataModel(){
		try {
			if (wDataModelClass != null){
				return wDataModelClass.newInstance();
			}else{
				return null;
			}
		} catch (Exception e) {
			wLogger.error(e);
			return null;
		}
	}
	
	
	/**
	 * Indica se posição do registro atual foi alterado
	 * @param pChanged
	 */
	public final boolean getRowPositionChanged(){
		return wRowPositionChanged;
	}
	

	/**
	 * Indica se posição do registro atual foi alterado
	 * @param pChanged
	 */
	protected final void setRowPositionChanged(boolean pChanged){
		wRowPositionChanged = pChanged;
	}
	
	/**
	 * Indica se efetuará o insert ou update utilizando todos os valores das colunas da tabela ou
	 * somente os que foram alterados pelo usuário via setValue.<br/>
	 * Isto permite que se aproveite o registro corrente como fonte de dados 
	 * para a inclusão ou alteração de um outro registro, sem precisar preencher todas as colunas.<br/>
	 * O padrão é <i>true</i>.	 
	 * @return 
	 */
	public boolean getExecuteOnlyChangedValues() {
		return wExecuteOnlyChangedValues;
	}

	
	/**
	 * Indica se efetuará o insert ou update utilizando todos os valores das colunas da tabela ou
	 * somente os que foram alterados pelo usuário via setValue.<br/>
	 * Isto permite que se aproveite o registro corrente como fonte de dados 
	 * para a inclusão ou alteração de um outro registro, sem precisar preencher todas as colunas.<br/>
	 * O padrão é <i>true</i>.	 
	 * @param pExecuteOnlyChangedValues
	 */
	public void setExecuteOnlyChangedValues(boolean pExecuteOnlyChangedValues) {
		wExecuteOnlyChangedValues = pExecuteOnlyChangedValues;
	}

	
	/**
	 * Retorna o total de linhas da Query no caso de ter efetuado uma pesquisa via <b>open</b>.<br/>
	 * Retorna o total de linhas da tabela no caso de <b>não</b> ter efetuado uma pesquisa via <b>open</b>, mas ter definido a <b>CommandTableName</b>.<br/>
	 * Não considera as linhas posivelmente inseridas manualmente e não comitadas. 
	 * @return Quantidade.
	 * @throws DBSIOException 
	 */
	public final int getRowsCountAfterRefresh(){
		return wRowsCountAfterRefresh;
	}

	/**
	 * Seta o total de linhas da Query no caso de ter efetuado uma pesquisa via <b>open</b>.<br/>
	 * Seta o total de linhas da tabela no caso de <b>não</b> ter efetuado uma pesquisa via <b>open</b>, mas ter definido a <b>CommandTableName</b>.<br/>
	 * Não considera as linhas posivelmente inseridas manualmente e não comitadas. 
	 * @param pRowsCount
	 * @throws DBSIOException
	 */
	protected final void setRowsCountAfterRefresh(int pRowsCount){
		wRowsCountAfterRefresh = pRowsCount;
	}
	

	
	/**
	 * retorna as colunas 
	 * @return
	 * @throws DBSIOException 
	 */
	public final List<DataModelClass> getListDataModel() throws DBSIOException{
		return pvGetList(true);
	}

	/**
	 * retorna as colunas 
	 * @return
	 * @throws DBSIOException 
	 */
	public final List<DBSRow> getListRow() throws DBSIOException{
		return pvGetList(true);
	}
	
	
	/**
	 * @return DataModel
	 */
	public final DataModelClass getDataModel(){
		return wDataModel;
	}

	public final void setDataModel(DataModelClass pDataModel) {
		this.wDataModel = pDataModel;
	}
	
	/**
	 * Salva os valores como sendo os originais, para posteriormente poder saber quais colunas sobreram alteração de valor
	 * @param pDataModelOld
	 */
	public final void setDataModelValueOriginal(DataModelClass pDataModel){
		if (pDataModel != null){
			pvCopyDataModelFieldsValueToCommandValue(pDataModel, true);
		}
	}
	
	/**
	 * Move para o registro anterior ao primeiro registro.
	 * @throws DBSIOException
	 */
	public abstract void moveBeforeFirstRow() throws DBSIOException;

	/**
	 * Move para o primeiro registro.
	 * @throws DBSIOException
	 */
	public abstract boolean moveFirstRow() throws DBSIOException;
	
	/**
	 * Move para o próximo registro.<br/>
	 * Não permite avançar caso esteja no último registro.
	 * ATENÇÃO: Em caso de loop, certifique-se de chamar <b>moveBeforeFirstRow</b> antes de utilizar este método para não ignorar o primeiro registro.
	 * @throws DBSIOException
	 */
	public abstract boolean moveNextRow() throws DBSIOException;
	
	/**
	 * Move para o registro anterior.<br/>
	 * Não permite retroceder caso esteja no priméiro registro.
	 * @throws DBSIOException
	 */
	public abstract boolean movePreviousRow() throws DBSIOException;
	
	/**
	 * Move para o último registro.
	 * @throws DBSIOException
	 */
	public abstract boolean moveLastRow() throws DBSIOException;

	/**
	 * Exclui e retorna a quantidade de registros excluidos
	 * @return Quantidade de linhas afetadas
	 * @throws DBSIOException
	 */	
	public abstract int executeDelete() throws DBSIOException;
	public synchronized final int executeDelete(DataModelClass pDataModel) throws DBSIOException{
		setDataModel(pDataModel);
		return this.executeDelete();
	}

	/**
	 * Atualiza registro 
	 * @return Quantidade de linhas afetadas
	 * @throws DBSIOException 
	 */
	public abstract int executeUpdate() throws DBSIOException;
	public synchronized final int executeUpdate(DataModelClass pDataModel) throws DBSIOException{
		setDataModel(pDataModel);
		return this.executeUpdate();
	}
	
	/**
	 * Executa o insert da tabela definida como CommandTable;
	 * @returnQuantidade de linhas afetadas
	 * @throws DBSIOException
	 */	
	public abstract int executeInsert() throws DBSIOException;
	public synchronized final int executeInsert(DataModelClass pDataModel) throws DBSIOException{
		setDataModel(pDataModel);
		return this.executeInsert();
	}

	/**
	 * Inclui registro se nenhum registro foi encontrado para atualização
	 * @return Quantidade de linhas afetadas
	 * @throws DBSIOException 
	 */	
	public abstract int executeMerge() throws DBSIOException;
	public synchronized final int executeMerge(DataModelClass pDataModel) throws DBSIOException{
		setDataModel(pDataModel);
		return this.executeMerge();
	}


	/**
	 * Fecha Arquivo
	 * @throws DBSIOException
	 */
	public abstract void close() throws DBSIOException;
	
	/**
	 * Abre o arquivo 
	 * @throws DBSIOException
	 */
	public abstract boolean open() throws DBSIOException;
	
	/**
	 * Abre o arquivo ou comando SQL
	 * @throws DBSIOException
	 */
	public abstract boolean open(String pFileOrSQL) throws DBSIOException;
	
	/**
	 * Retorna a quantidade de linha
	 * @return
	 * @throws DBSIOException 
	 */
	public abstract int getRowsCount() throws DBSIOException;
	
	/**
	 * Retorna o valor da coluna informada
	 * @param pColumnName
	 * @return
	 */
	public abstract <A> A getValue(String pColumnName);

	
	/**
	 * Seta o valor da coluna informada.
	 * @param pColumnName Nome da Coluna
	 * @param pValue Valor
	 */
	public abstract void setValue(String pColumnName, Object pValue);
	
	/**
	 * Seta o valor da coluna informada.
	 * @param pColumnName Nome da Coluna
	 * @param pValue Valor
	 */
	public abstract void setValue(String pColumnName, Object pValue, boolean pOriginalValue);


	/**
	 * Retorna a mensagem vinculada a esta coluna.<br/>
	 * Esta mensagem serve para informar qualquer tipo de aviso/erro referente ao valor nela contido.<br/>
	 * Será retornado o valor nulo quando não houve mensagem.<br/>
	 * A mensagem sempre será apagada após o valor da coluna ter sido alterado.
	 */
	public abstract IDBSMessage getMessage(String pColumnName);


	/**
	 * retorna as colunas 
	 * @return
	 */
	public abstract Collection<DBSColumn> getColumns();

	/**
	 * retorna a coluna a partir no nome informado
	 * @return
	 */
	public abstract DBSColumn getColumn(String pColumnName);
	
	/**
	 * retorna a coluna a partir no número informado
	 * @return
	 */
	public abstract DBSColumn getColumn(int pColumnIndex);

	/**
	 * Retorna uma string contendo os nomes das colunas que formam o UK que será responsável 
	 * por identificar um linha única, podente haver colunas de tabelas diferentes.<br/>
	 * No caso de haver mais de uma coluna como UK, os nomes das colunas serão separados por vírgula.
	 * @return
	 */
	public abstract String getUK();


	/**
	 * Retorna o valor da UK corrente.<br/>
	 * A UK deverá ser formada por uma só coluna, caso contrário o valor não será retornado.
	 * @return
	 */
	public abstract Object getUKValue();
	
	
	//=== EVENTOS =====================================================================================
	protected boolean pvFireEventBeforeOpen(){
		DBSDAOEvent xE = new DBSDAOEvent(this);

		//reseta a quantidade de linhas antes do open
		setRowsCountAfterRefresh(0);

		//Chame o método((evento) local para quando esta classe for extendida
		beforeOpen(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).beforeOpen(xE);
        }
		
		return xE.isOk();
	}
	
	protected void pvFireEventAfterOpen(boolean pOk) throws DBSIOException{
		DBSDAOEvent xE = new DBSDAOEvent(this);
		xE.setOk(pOk);
		//Chame o método((evento) local para quando esta classe for extendida
		afterOpen(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).afterOpen(xE);
        }
		setRowsCountAfterRefresh(getRowsCount());

	}
	
	protected boolean pvFireEventBeforeClose() {
		DBSDAOEvent xE = new DBSDAOEvent(this);
		//Chame o método((evento) local para quando esta classe for extendida
		beforeClose(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).beforeClose(xE);
        }
		return xE.isOk();
	}
	protected void pvFireEventAfterClose(boolean pOk){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		xE.setOk(pOk);
		//Chame o método((evento) local para quando esta classe for extendida
		afterClose(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).afterClose(xE);
        }
	}
	
	//INSERT
	protected boolean pvFireEventBeforeInsert() {
		DBSDAOEvent xE = new DBSDAOEvent(this);
		//Chame o método((evento) local para quando esta classe for extendida
		beforeInsert(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).beforeInsert(xE);
        }
		return xE.isOk();
	}
	protected void pvFireEventAfterInsert(boolean pOk){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		xE.setOk(pOk);
		//Chame o método((evento) local para quando esta classe for extendida
		afterInsert(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).afterInsert(xE);
        }
	}
	
	//UPDATE
	protected boolean pvFireEventBeforeUpdate(){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		//Chame o método((evento) local para quando esta classe for extendida
		beforeUpdate(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).beforeUpdate(xE);
        }
		return xE.isOk();
	}
	protected void pvFireEventAfterUpdate(boolean pOk){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		xE.setOk(pOk);
		//Chame o método((evento) local para quando esta classe for extendida
		afterUpdate(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).afterUpdate(xE);
        }
	}
	
	//DELETE
	protected boolean pvFireEventBeforeDelete(){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		//Chame o método((evento) local para quando esta classe for extendida
		beforeDelete(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).beforeDelete(xE);
        }
		return xE.isOk();
	}
	protected void pvFireEventAfterDelete(boolean pOk){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		xE.setOk(pOk);
		//Chame o método((evento) local para quando esta classe for extendida
		afterDelete(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).afterDelete(xE);
        }
	}
	
	//MERGE
	protected boolean pvFireEventBeforeMerge(){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		//Chame o método((evento) local para quando esta classe for extendida
		beforeMerge(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).beforeMerge(xE);
        }
		return xE.isOk();
	}
	protected void pvFireEventAfterMerge(boolean pOk){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		xE.setOk(pOk);
		//Chame o método((evento) local para quando esta classe for extendida
		afterMerge(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).afterMerge(xE);
        }
	}
	
	//READ
	protected boolean pvFireEventBeforeRead(){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		//Chame o método((evento) local para quando esta classe for extendida
		beforeRead(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).beforeRead(xE);
        }
		return xE.isOk();
	}
	protected void pvFireEventAfterRead(boolean pOk){
		DBSDAOEvent xE = new DBSDAOEvent(this);
		xE.setOk(pOk);
		//Chame o método((evento) local para quando esta classe for extendida
		afterRead(xE);
		//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).afterRead(xE);
        }
	}

	//MOVE
	protected boolean pvFireEventBeforeMove(){
		if (wCallMoveEvent){ 
			DBSDAOEvent xE = new DBSDAOEvent(this);
			//Chame o método((evento) local para quando esta classe for extendida
			beforeMove(xE);
			//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).beforeMove(xE);
	        }
			return xE.isOk();
		}else{
			return true;
		}
		
	}
	protected void pvFireEventAfterMove(boolean pOk){
		if (wCallMoveEvent){
			DBSDAOEvent xE = new DBSDAOEvent(this);
			xE.setOk(pOk);
			//Chame o método((evento) local para quando esta classe for extendida
			afterMove(xE);
			//Chama a método((evento) dentro das classe que foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).afterMove(xE);
	        }
		}
	}

	

	
//
//	protected void pvEventAfterInsert(boolean pOk){
//		afterInsert(this, pOk);
//		for (int xX=0; xX<wEventListeners.size(); xX++){
//			wEventListeners.get(xX).afterInsert(this, pOk);
//        }
//	}

}
