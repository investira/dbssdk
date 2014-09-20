package br.com.dbsoft.io;

import java.io.Serializable;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.COLUMN.HORIZONTALALIGNMENT;
import br.com.dbsoft.core.DBSSDK.COLUMN.VERTICALALIGNMENT;
import br.com.dbsoft.core.DBSSDK.IO.DATATYPE;
import br.com.dbsoft.util.DBSIO;


/**
 * @author ricardo.villar
 *
 */
public class DBSColumn implements Serializable{

	//######################################################################################################### 
	//## Working Variables                                                                                    #
	//#########################################################################################################
	
	protected Logger			wLogger = Logger.getLogger(this.getClass());

	private static final long 		serialVersionUID = 1733510151561276507L;
	
	private String 					wTableName;
	private String 					wColumnName;
    private DATATYPE				wDataType;
    private boolean					wAutoIncrement;
    private int 	 				wSize;
    private boolean  				wPK;
    private Object 					wValue;
    private Object 					wValueOriginal;
    private Object 					wValueDefault = null;
    private boolean					wChanged;
    private int 	 				wInputSize;
    private DATATYPE  				wInputDataType;
    private String  				wInputFormat;
    private boolean  				wInputAllowNull;
    private String 					wDisplayFormat = "";
    private boolean  				wDisplayMerge;
    private boolean  				wDisplayWordWarp = false;
    private String 	 				wDisplayColumnName = "";
    private VERTICALALIGNMENT 		wDisplayVerticalAlignment;
    private HORIZONTALALIGNMENT 	wDisplayHorizontalAlignment;
    private boolean 				wDisplayColumn = true;
    private int						wDisplaySize;
    private boolean 				wReadOnly;
    private boolean  				wComboBox;
    private boolean  				wAllowSort;
    //Dim wsAlign As DBSCnsColumnAlign

//	@Override
//	protected DBSColumn clone() throws CloneNotSupportedException {
//		return (DBSColumn) super.clone();
//	}
//	
    
    DBSColumn(){
        wTableName = "";	
        wColumnName = "";
        wDataType = DATATYPE.STRING;
        wSize = 0;
        wPK = false;
        wInputSize = 0;
        wInputDataType = DATATYPE.STRING;
        wInputAllowNull = true;
        wDisplayFormat = "";
        wDisplayMerge = false;
        wDisplayWordWarp = false;
        wDisplaySize = 0;
        wDisplayColumnName = "";
        wDisplayVerticalAlignment =  VERTICALALIGNMENT.CENTER;
        wDisplayHorizontalAlignment = HORIZONTALALIGNMENT.LEFT;
        wDisplayColumn = true;
        wReadOnly = true;
        wComboBox = false;
        wAllowSort = true;
        this.restoreValueDefault();
    }
    
    /**
     * Copia o valor default como sendo o valor corrente.
     */
	public final <T> void restoreValueDefault(){
        wChanged = false;
        //T xValueDefault = getValueDefault();
        try {
            wValue = getValueDefault(); //xValueDefault;
            wValueOriginal = wValue;
		} catch (Exception xE) {
			wLogger.error(xE);
		}
    }
    
	/**
     * Restaura os valores originais para o valor atual.
     */
	public final <T> void restoreValueOriginal(){
        wChanged = false;
        try {
            wValue = getValueOriginal();
            wValueOriginal = wValue;
		} catch (Exception xE) {
			wLogger.error(xE);
		}
    }
    
	/**
	 * Força que o valor original seja iqual o valor atual
	 */
	public final void copyValueToValueOriginal(){
		setValue(getValue(), true);
	}

	/**
	 * Força que o valor original seja iqual o valor atual
	 */
	public final void copyDefaultToValueOriginal(){
		setValue(getValueDefault(), true);
	}

	//######################################################################################################### 
	//## Public Properties                                                                                    #
	//#########################################################################################################
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Nome da Tabela
	 * @return 
	 */
	public final String getTableName() {
		return wTableName;
	}
	public final void setTableName(String pTableName) {
		wTableName = pTableName.toUpperCase().trim();
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Nome da columa conforme está definido no banco de dados
	 * @return
	 */
	public final String getColumnName() {
		return wColumnName;
	}
	public final void setColumnName(String pColumnName) {
		wColumnName = pColumnName.toUpperCase().trim();
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Tipo de dado da coluna conforme constante DBSSDK.IO.DATATYPE
	 * @return
	 */
	public final DATATYPE getDataType(){
		
		return wDataType;
	}
	public final void setDataType(DATATYPE pDataType){
//		if (getColumnName().equals("HABILITADO")){
//			System.out.println("Type: " + pDataType);
//		}		
		wDataType = pDataType;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Tamanho do campo
	 * @return
	 */
	public final int getSize() {
		return wSize;
	}
	/**
	 * Tamanho do campo
	 * @param pSize
	 */
	public final void setSize(int pSize) {
		wSize = pSize;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Indica se coluna é PK
	 * @return
	 */
	public final boolean getPK() {
		return wPK;
	}
	/**
	 * Indica se coluna é PK.
	 * Deve-se utilizar este atributo, somente quando não houver PK na tabela ou se deseje forçar uma chave,<br/> 
	 * não podendo, contudo, ter menos campos que a própria PK ou UK que por ventura existir. 
	 * @param pPK
	 */
	public final void setPK(boolean pPK) {
		wPK = pPK;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Valor da coluna
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public final <T> T  getValue() {
//		return (T) wValue;
//	}

	/**
	 * Valor da coluna
	 * @return
	 */

	public final <T> T getValue() {
		return DBSIO.<T>getDataTypeConvertedValue(getDataType(), wValue);
	}

	public final void setValue(Object pValue) {
		wValue = DBSIO.getDataTypeConvertedValue(getDataType(), pValue);
		this.setChanged(true);//Indica que valor foi alterado pelo usuário
	}
	
	public final void setValue(Object pValue, boolean pOriginalValue) {
		this.setValue(pValue);
		if (pOriginalValue) {
			this.setValueOriginal(pValue);
			this.setChanged(false);
		}
	}
	
    //--------------------------------------------------------------------------------------------------------	
	/**
	 * Valor da coluna antes da alteração
	 * @return
	 */
	public final <T> T getValueOriginal() {
		return DBSIO.<T>getDataTypeConvertedValue(getDataType(), wValueOriginal);
	}

	public final void setValueOriginal(Object pValueOriginal) {
		wValueOriginal = DBSIO.getDataTypeConvertedValue(getDataType(), pValueOriginal);
		this.setChanged(false);//Indica que valor resetado ao estado inicial
	}

    //--------------------------------------------------------------------------------------------------------	
	/**
	 * Valor da default coluna conforme definido no banco de dados ou informado pelo usuário
	 * O valor default será utilizado quando o valor da coluna for resetado em reserValue
	 * @return
	 */
	public final <T> T getValueDefault() {
		return DBSIO.<T>getDataTypeConvertedValue(getDataType(), wValueDefault);
	}

	public final void setValueDefault(Object pValueDefault) {
		//Não é efetuada a conversão, pois o valor default pode ser um comando SQL informado
		//diretamente da definição da coluna do banco de dados como por exemplo:CURRENT_TIMESTAMP
		//		wValueDefault = DBSIO.getDataTypeConvertedValue(getDataType(), pValueDefault);
		wValueDefault = pValueDefault;
	}

	//--------------------------------------------------------------------------------------------------------
	/**
	 * Indica se a coluna é autoincrement/sequence
	 * @return
	 */
	public final boolean isAutoIncrement() {
		return wAutoIncrement;
	}
	public final void setAutoIncrement(boolean pAutoIncrement) {
		wAutoIncrement = pAutoIncrement;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * Indica se valor do campo foi manipulado pelo usuário
	 * @return
	 */
	public final boolean getChanged() {
		return wChanged;
	}
	public final void setChanged(boolean pChanged) {
		wChanged = pChanged;
	}

	//--------------------------------------------------------------------------------------------------------
	/**
	 * Tamanho máximo do campo para digitação
	 * @return
	 */
	public final int getInputSize() {
		return wInputSize;
	}
	public final void setInputSize(int pInputSize) {
		wInputSize = pInputSize;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Tipo de dado do campo para digitação
	 * @return
	 */
	public final DATATYPE getInputDataType(){
		return wInputDataType;
	}
	public final void setInputDataType(DATATYPE pDataType){
		wInputDataType = pDataType;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Mascará de formado do campo para digitação
	 * @return
	 */
	public final String getInputFormat() {
		return wInputFormat;
	}
	public final void setInputFormat(String pInputFormat) {
		wInputFormat = pInputFormat;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Permite que valor seja nulo
	 * @return
	 */
	public final boolean getInputAllowNull() {
		return wInputAllowNull;
	}
	public final void setInputAllowNull(boolean pInputAllowNull) {
		wInputAllowNull = pInputAllowNull;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Mascará para formado para impressão
	 * @return
	 */
	public final String getDisplayFormat() {
		return wDisplayFormat;
	}
	public final void setDisplayFormat(String pDisplayFormat) {
		wDisplayFormat = pDisplayFormat;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Agrupa colunas quando os valores forem semelhante, impedindo a impressão repetida.
	 * @return
	 */
	public final boolean getDisplayMerge() {
		return wDisplayMerge;
	}
	public final void setDisplayMerge(boolean pDisplayMerge) {
		wDisplayMerge = pDisplayMerge;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Pula de linha quando o valor for superior ao tamanho da coluna.
	 * @return
	 */
	public final boolean getDisplayWordWarp() {
		return wDisplayWordWarp;
	}
	public final void setWordWarp(boolean pDisplayWordWarp) {
		wDisplayWordWarp = pDisplayWordWarp;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * título do cebeçalho
	 * @return
	 */
	public final String getDisplayColumnName() {
		return wDisplayColumnName;
	}
	public final void setDisplayColumnName(String pDisplayColumnName) {
		wDisplayColumnName = pDisplayColumnName;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Alinhamento vertical conforme constante DBSSDK.COLUMN.VERTICALALIGNMENT
	 * @return
	 */
	public final VERTICALALIGNMENT getDisplayVerticalAlignment(){
		return wDisplayVerticalAlignment;
	}
	public final void setDisplayVerticalAlignment(VERTICALALIGNMENT pDisplayVerticalAlignment){
		wDisplayVerticalAlignment = pDisplayVerticalAlignment;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Alinhamento horizontal conforme constante DBSSDK.COLUMN.HORIZONTALALIGNMENT
	 * @return
	 */
	public final HORIZONTALALIGNMENT getHorizontalAlignment(){
		return wDisplayHorizontalAlignment;
	}
	public final void setDisplayHorizontalAlignment(HORIZONTALALIGNMENT pDisplayHorizontalAlignment){
		wDisplayHorizontalAlignment = pDisplayHorizontalAlignment;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Indica se coluna será exibida
	 * @return
	 */
	public final boolean getDisplayColumn() {
		return wDisplayColumn;
	}
	public final void setDisplayColumn(boolean pDisplayColumn) {
		wDisplayColumn = pDisplayColumn;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Largura da coluna
	 * @return
	 */
	public final int getDisplaySize() {
		return wDisplaySize;
	}
	public final void setDisplaySize(int pDisplaySize) {
		wDisplaySize = pDisplaySize;
	}	
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Se coluna permite alteração do valor
	 * @return
	 */
	public final boolean getReadOnly() {
		return wReadOnly;
	}
	public final void setReadOnly(boolean pReadOnly) {
		wReadOnly = pReadOnly;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Se coluna � um combobox
	 * @return
	 */
	public final boolean getComboBox() {
		return wComboBox;
	}
	public final void setComboBox(boolean pComboBox) {
		wComboBox = pComboBox;
	}
    //--------------------------------------------------------------------------------------------------------
	/**
	 * Se coluna pode ser considerada para ordenação
	 * @return
	 */
	public final boolean getAllowSort() {
		return wAllowSort;
	}
	public final void setAllowSort(boolean pAllowSort) {
		wAllowSort = pAllowSort;
	}
    //--------------------------------------------------------------------------------------------------------

	

	
}	

	

