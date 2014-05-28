package br.com.dbsoft.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.IO.DATATYPE;
import br.com.dbsoft.util.DBSString;

/**
 * Armazenar e controlar uma lista de DBSColumns
 *
 */
public class DBSRow implements Serializable {

	
	//######################################################################################################### 
	//## Constants                                                                                    #
	//#########################################################################################################
	private static final long serialVersionUID = -5571583250404451543L;

	

	//######################################################################################################### 
	//## Working Variables                                                                                    #
	//#########################################################################################################
	protected static Logger			wLogger = Logger.getLogger(DBSRow.class);

	private LinkedHashMap<String, DBSColumn> wColumns =  new LinkedHashMap<String, DBSColumn>();
	
	
	/**
	 * Excluir todas as colunas e
	 */
	public final void clear(){
		wColumns.clear();
	}

	/**
	 * Força que o valor original seja o valor atual 
	 */
	public final void copyValueToValueOriginal(){
		for (DBSColumn xColumn: wColumns.values()){
			xColumn.copyValueToValueOriginal();
		}
	}
	
	/**
	 * Força que o valor original seja o default 
	 */
	public final void copyDefaultToValueOriginal(){
		for (DBSColumn xColumn: wColumns.values()){
			xColumn.copyDefaultToValueOriginal();
		}
	}

	/**
	 * Restaura os valores originais para o valor atual.
	 */
	public final void restoreValuesOriginal(){
		for (DBSColumn xColumn: wColumns.values()){
			xColumn.restoreValueOriginal();
		}
	}

	/**
	 * Restaura os valores originais para o valor default.
	 */
	public final void restoreValuesDefault(){
		for (DBSColumn xColumn: wColumns.values()){
			xColumn.restoreValueDefault();
		}
	}
	
	/**
	 * Restaura os valores originais para o valor atual.
	 */
	public final void setChanged(){
		for (DBSColumn xColumn: wColumns.values()){
			xColumn.setChanged(true);
		}
	}
	
	//######################################################################################################### 
	//## Public Properties                                                                                    #
	//#########################################################################################################
	
	public final boolean containsKey(String pColumnName){
		pColumnName = pvGetColumnName(pColumnName);
		return wColumns.containsKey(pColumnName);
	}
	public final boolean containsValue(DBSColumn pValue){
		return wColumns.containsValue(pValue);
	}

	
	/**
	 * @return Quantidade de colunas existentes na linha
	 */
	public final int size(){
		return wColumns.size();
	}

	public final DBSColumn put(String pColumnName, DBSColumn pColumn){
		if (pColumnName == null){
			return null;
		}
		pColumnName = pvGetColumnName(pColumnName);
		return wColumns.put(pColumnName, pColumn);
	}
	
	public final DBSColumn remove(String pColumnName){
		if (pColumnName == null){
			return null;
		}
		pColumnName = pvGetColumnName(pColumnName);
		return wColumns.remove(pColumnName);
	}

	/** Retorna uma coluna a partir do nome informado
	 * @param pColumnName
	 * @return Coluna ou null se não for encontrada
	 */
	public final DBSColumn getColumn(String pColumnName){
		if (pColumnName == null){
			return null;
		}
		pColumnName = pvGetColumnName(pColumnName);
		if (wColumns.containsKey(pColumnName)){
			return wColumns.get(pColumnName);
		}else{
			return null;
		}
	}

	/** Retorna uma coluna a partir do indice informado
	 * @param pColumnIndex
	 * @return 
	 */	
	public final DBSColumn getColumn(int pColumnIndex){
		int xX=-1;
		for (Entry<String, DBSColumn> xC: wColumns.entrySet()){
			xX++;
			if(xX==pColumnIndex){
				return xC.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Retorna o valor original da coluna. 
	 * Valor que será utilizado para comparação posreriormente para identificar se houve alteração dos dados.
	 * @param pColumnName
	 * @return
	 */
	public final <T> T getValueOriginal(String pColumnName){
		DBSColumn xC = this.getColumn(pColumnName);
		if (xC == null){
			wLogger.error("ERRO: Coluna '" + pColumnName + "' inexistente");
			return null;
		}
		return xC.getValueOriginal();
	}
	
	/**
	 * Retorna o valor valor da coluna informada
	 * @param pColumnName Nome da Coluna. Caso coluna não exista, retorna null.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T> T getValue(String pColumnName){
		DBSColumn xC = this.getColumn(pColumnName);
		if (xC == null){
			wLogger.error("ERRO: Coluna '" + pColumnName + "' inexistente");
			return null;
		}
		return (T) xC.getValue();
	}
	
	public final <T> void setValue(String pColumnName, T pValue){
		this.setValue(pColumnName, pValue, false);
	}

	public final <T> void setValue(String pColumnName, T pValue, boolean pOriginalValue) {
		this.getColumn(pColumnName).setValue(pValue, pOriginalValue);
	}
	
	/**
	 * @return Retorna todas as colunas da linha
	 */
	public final Collection<DBSColumn> getColumns(){
		return wColumns.values();
	}

	

	//######################################################################################################### 
	//## Public Methods                                                                                       #
	//#########################################################################################################

	
	/**
	 * Move de uma só vez, todos os valores para as colunas a partir de uma única string.
	 * A string informada será dividida em colunas conforme o delimitador informado
	 * @param pString
	 * @param pColumnDelimiter
	 * @return true=sem erro; false=com erro
	 */
	public final boolean setRowValues(String pString, String pColumnDelimiter){
		//Se não houver coluna definida
		if (wColumns.size()==0){
			return false;
		}
		//Recupera os valores de cada coluna
		ArrayList<String> xColumnsValues =  DBSString.toArray(pString, pColumnDelimiter);
		Iterator<DBSColumn> xColumns = wColumns.values().iterator();
		int xX = 0;
		DBSColumn xColumn; 
		while (xColumns.hasNext()){
			//Preenche o valor da coluna com o valor obtido a partir da string
			xColumn = xColumns.next();
			xColumn.setValue(xColumnsValues.get(xX), true);
			xX++;
		}
		return true;
	}

	/**
	 * Move de uma só vez, todos os valores para as colunas a partir de uma única string.
	 * A string informada será dividida em colunas conforme o tamanho definido em cada coluna.
	 * Caso a string informada seja menor, os restante das colunas será preenchido com brancos.
	 * Caso a string informada sema maior, o restante da string será desprezado
	 * @param pString
	 * @return true=sem erro; false=com erro
	 */
	public final boolean setRowValues(String pString){
		//Se não houver coluna definida
		if (wColumns.size()==0){
			return false;
		}
		Iterator<DBSColumn> xColumns = wColumns.values().iterator();
		int xI = 1;
//		String xIniciais = "";
		DBSColumn xColumn; 
		try {
			while (xColumns.hasNext()){
				xColumn = xColumns.next();
//				xIniciais = xIniciais + "[" + xColumn.getColumnName() + "," + xI + "] - ";
				//Move valor para a coluna
				if (xI<pString.length()){
					xColumn.setValue(DBSString.getSubString(pString, xI, xColumn.getSize()));
				}else{
					xColumn.setValue(DBSString.repeat(" ", xColumn.getSize()));
				}
				xI += xColumn.getSize(); //Incrementa para a posição na linha
			}
//			System.out.println(xIniciais);
			return true;
		} catch (StringIndexOutOfBoundsException e) {
			throw new StringIndexOutOfBoundsException("Tamanho da string é menor que a soma dos tamanhos das colunas.");
//			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Retorna o conteúdo integral da linha atual, concatenando o delimitador no valor das colunas
	 * @param pColumnDelimiter Delimitador que será utilizado para separar as colunas
	 * @return
	 */
	public final Object getRowValues(String pColumnDelimiter){
		StringBuffer xSB = new StringBuffer("");
		Iterator<DBSColumn> xColumns = wColumns.values().iterator();
		String xDelimitador = ""; //Definie delimitador padão
		while (xColumns.hasNext()){
			xSB.append(xDelimitador); //Adiciona delimitador. Na primeira passagem o delimitador é vázio.
			xSB.append(xColumns.next().getValue()); //Move valor para a linha
			xDelimitador = pColumnDelimiter; //Define delimitador informado pelo usuário
		}
		return xSB;
	}
	
	/**
	 * Inclui coluna na coleção se não existir e atualiza se existir
	 * @param pColumnName Nome da Coluna
	 * @param pColumnValue Valor do campo
	 * @return Coluna incluida/atualizada
	 */
	public final DBSColumn MergeColumn(String pColumnName, String pColumnValue){
		DBSColumn xObj = pvGetColumn(pColumnName);
		xObj.setColumnName(pColumnName);
		xObj.setValue(pColumnValue,true); //Inicializa conteúdo com valores em branco
		return xObj;
	}
	
	/**
	 * Inclui coluna na coleção se não existir e atualiza se existir
	 * @param pColumnName Nome da Coluna
	 * @param pSize tamnho fixo da coluna. Coluna será considerada do tipo String
	 * @return Coluna incluida/atualizada
	 */
	public final DBSColumn MergeColumn(String pColumnName, int pSize){
		return this.MergeColumn(pColumnName, DATATYPE.STRING, pSize, DBSString.repeat(" ", pSize)); //Inicializa conteúdo com valores em branco
	}

	/**
	 * Inclui coluna na coleção se não existir e atualiza se existir
	 * @param pColumnName Nome da Coluna
	 * @return Coluna incluida/atualizada
	 */
	public final DBSColumn MergeColumn(String pColumnName){
		return this.MergeColumn(pColumnName, ""); //Inicializa conteúdo com valores em branco
	}

	/**
	 * Inclui coluna na coleção se não existir e atualiza se existir
	 * @param pColumnName Nome da Coluna
	 * @param pDataType 
	 * @param pSize Tamanho da coluna. Iguala os tamanhos de diplay, input
	 * @return Coluna incluida/atualizada
	 */
	public final DBSColumn MergeColumn(
			String 		pColumnName, 
			DATATYPE	pDataType,
			int 		pSize,
			Object		pValueDefault){
		DBSColumn xObj = this.MergeColumn(pColumnName);
		xObj.setDataType(pDataType);
		//Força o tamanho para 32 posições quando o tamanho recebido do metadado do banco for zerado. Situação que normalmente ocorre na recuperação de metadado do oracle com coluna tipo NUMBER.
		if (pSize == 0D){
			if (pDataType == DATATYPE.DECIMAL
			 || pDataType == DATATYPE.DOUBLE){
				pSize = 32;
			}
		}
		xObj.setSize(pSize);
		xObj.setInputSize(pSize);
		xObj.setDisplaySize(pSize);
		xObj.setValueDefault(pValueDefault);
		return xObj;	
	}
	
	public final DBSColumn MergeColumn(
			String 	pColumnName, 
			boolean pDisplayColumn, 
			String 	pDisplayColumnName,
			String 	pInputFormat, 
			int 	pSize,
			boolean pDataPK,
			boolean pDisplayMerge,
			boolean pIsReadOnly,
			boolean pAllowSort,
			boolean pInputAllowNull) {
		DBSColumn xObj = this.MergeColumn(pColumnName);
		xObj.setDisplayColumn(pDisplayColumn);
		xObj.setDisplayColumnName(pDisplayColumnName);
		xObj.setInputFormat(pInputFormat);
		xObj.setSize(pSize);
		xObj.setInputSize(pSize);
		xObj.setDisplaySize(pSize);
		xObj.setPK(pDataPK);
		xObj.setDisplayMerge(pDisplayMerge);
		xObj.setReadOnly(pIsReadOnly);
		xObj.setAllowSort(pAllowSort);
		xObj.setInputAllowNull(pInputAllowNull);
		return xObj;	
	}

	//######################################################################################################### 
	//## Private Methods                                                                                      #
	//#########################################################################################################

	private final DBSColumn pvGetColumn(String pColumnName){
		DBSColumn xObj;
		pColumnName = pvGetColumnName(pColumnName);
		if (wColumns.containsKey(pColumnName)){ //Cria coluna se não existir
			xObj = wColumns.get(pColumnName); //Retorna coluna existente
		}else{
			xObj = new DBSColumn();
			wColumns.put(pColumnName, xObj);
		}			
		return xObj;
	}
	
	
	/**
	 * Retorna o nome da coluna em caixa alta, trim e sem o nome date tabela, se houver.
	 * @param pColumnName
	 * @return
	 */
	private String pvGetColumnName(String pColumnName){
		if (pColumnName == null){
			return "";
		}
		pColumnName = pColumnName.toUpperCase().trim();
		int	xI = pColumnName.indexOf(".");
		if (xI > 0){
			return pColumnName.substring(xI + 1);
		}else{
			return pColumnName;
		}
	}
}

