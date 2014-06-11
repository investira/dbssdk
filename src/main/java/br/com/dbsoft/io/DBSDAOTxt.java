package br.com.dbsoft.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSIO;
import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;

/**
 * @param <DataModelClass> Classe Model da tabela do banco de dados ou classe com atributos homônimos as colunas com as quais se deseje trabalhar no DAO.<br/>
 * É necessário também passar esta classe no construtor.  
 */
public class DBSDAOTxt<DataModelClass> extends DBSDAOBase<DataModelClass>{

	private static final long serialVersionUID = 1857864626212118507L;

	public static enum TYPE{
    	FIXED_COLUMNS,
    	DELIMITED_COLUMNS;
    } 
    
	private String					wFileName;
	private TYPE					wType;
	private String					wColumnsDelimiter;
	private boolean					wHeaderDefinesColumnsNames;
	private boolean					wTrimValues = true;
	private String					wEncode = DBSSDK.FILE.ENCODE.ISO_8859_1;
	private boolean					wKeepData = true;
	private List<String> 			wColumnsHeader=new ArrayList<String>();
	private DBSRow					wFixedColumns = new DBSRow();
	private BufferedReader 			wReader;
	private boolean 				wIsOpened = false;
	private long					wFileSize=0;
	private boolean					wConvertToASCII=false;
	private int						wCurrentRow = -1; //Posição dentro do wList
	private int						wCurrentRowFile; //Posição dentro do Arquivo
	private int						wRowsCountFile = -1;
	private int						wNumberOfRowsToIgnoreHeader=0;
	private int						wNumberOfRowsToIgnoreFooter=0;
	private int						wRowsIgnoredHeader=0;
	private List<DBSRow>			wListRow = new ArrayList<DBSRow>();
	private List<DataModelClass>					wListDataModel = new ArrayList<DataModelClass>();
	private String					wLineData;
	private Boolean					wInterromper = false;
	private List<String>			wListLinesWithError = new ArrayList<String>();

	public DBSDAOTxt(){
	}

	/**
	 * Abre um arquivo para leitura
	 * @param pFileName Nome do arquivo externo
	 */
	public DBSDAOTxt(String pFileName){
		this.setFileName(pFileName);
	}

	/**
	 * Abre um arquivo para leitura
	 * @param pFileName Nome do arquivo externo
	 * @param pEncode Tipo de encode do arquivo
	 */
	public DBSDAOTxt(String pFileName, String pEncode){
		this.setFileName(pFileName);
		this.setEncode(pEncode);
	}

	/**
	 * Abre um arquivo para leitura
	 * @param pFileName Nome do arquivo externo
	 * @param pEncode Tipo de encode do arquivo
	 * @param pColumnsDelimiter Delimitador que separa as colunas
	 */
	public DBSDAOTxt(String pFileName, String pEncode, TYPE pType){
		this.setFileName(pFileName);
		this.setEncode(pEncode);
		this.setType(pType);
	}
	
	/**
	 * Abre um arquivo para leitura
	 * @param pFileName Nome do arquivo externo
	 * @param pEncode Tipo de encode do arquivo
	 * @param pColumnsDelimiter Delimitador que separa as colunas
	 * @param pHeaderIsColumnsDefinitions Indica se a primeira linha(header) contém o nome das colunas
	 */
	public DBSDAOTxt(String pFileName, String pEncode, TYPE pType, String pColumnsDelimiter){
		this.setFileName(pFileName);
		this.setEncode(pEncode);
		this.setType(pType);
		this.setColumnsDelimiter(pColumnsDelimiter);
	}
	
	/**
	 * Abre um arquivo para leitura
	 * @param pFileName Nome do arquivo externo
	 * @param pEncode Tipo de encode do arquivo
	 * @param pColumnsDelimiter Delimitador que separa as colunas
	 * @param pHeaderIsColumnsDefinitions Indica se a primeira linha(header) contém o nome das colunas
	 */
	public DBSDAOTxt(String pFileName, String pEncode, TYPE pType, String pColumnsDelimiter, boolean pHeaderDefinesColumnsNames){
		this.setFileName(pFileName);
		this.setEncode(pEncode);
		this.setType(pType);
		this.setColumnsDelimiter(pColumnsDelimiter);
		this.setHeaderDefinesColumnsNames(pHeaderDefinesColumnsNames);
	}

	/**
	 * Tipo de arquivo TXT
	 * Forma como estão organizados os dados
	 * @return
	 */
	public TYPE getType() {
		return wType;
	}
	public void setType(TYPE pType) {
		this.wType = pType;
	}

	/**
	 * Nome do arquivo que será lido
	 * @return
	 */
	public String getFileName() {
		return wFileName;
	}

	/**
	 * Nome do arquivo que será lido
	 * @return
	 */
	public void setFileName(String wFileName) {
		this.wFileName = wFileName;
	}

	/**
	 *  Delimitador que separa as colunas 
	 * @return
	 */
	public String getColumnsDelimiter() {
		return wColumnsDelimiter;
	}

	/**
	 *  Delimitador que separa as colunas 
	 * @return
	 */
	public void setColumnsDelimiter(String wColumnsDelimiter) {
		this.wColumnsDelimiter = wColumnsDelimiter;
	}

	/**
	 * Tipo de encode do texto.<br/>
	 * Confirme os valores contidos em <b>DBSSDK.FILE.ENCODE</b>
	 * @return
	 */
	public String getEncode() {
		return wEncode;
	}

	/**
	 * Tipo de encode do texto.<br/>
	 * Utilizar os valores contidos em <b>DBSSDK.FILE.ENCODE</b>
	 * @return
	 */
	public void setEncode(String wEncode) {
		this.wEncode = wEncode;
	}
	
	/**
	 * Indica se mantém todos os dados lindos do arquivo na memória
	 * Caso contrário, somente a última linha lida é mantida.
	 * @return
	 */
	public boolean getKeepData() {
		return wKeepData;
	}

	/**
	 * Indica se mantém todos os dados lindo na memória
	 * Caso contrário, somente a última linha lida é mantida.
	 * @return
	 */
	public void setKeepData(boolean wKeepData) {
		this.wKeepData = wKeepData;
	}
	
	/**
	 * Retorna a lista com os nomes das colunas do cabeçalho
	 * @return
	 */
	public List<String> getColumnsHeader() {
		return wColumnsHeader;
	}
	
	/**
	 * Indica se fará conversão dos valores lidos para o código ASCII
	 * @param wConvertToASCII
	 */
	public boolean getConvertToASCII() {
		return wConvertToASCII;
	}
	/**
	 * Indica se fará conversão dos valores lidos para o código ASCII
	 * @param wConvertToASCII
	 */
	public void setConvertToASCII(boolean wConvertToASCII) {
		this.wConvertToASCII = wConvertToASCII;
	}
	
	public Boolean getInterrupt() {return wInterromper;}
	public void setInterrupt(Boolean pInterromper) {wInterromper = pInterromper;}
	
	public List<String> getLinesWithError() {
		return wListLinesWithError;
	}
	
	public <A> void setValue(int pColumnIndex, A pValue){
		 this.setValue(this.getColumnName(pColumnIndex), pValue);
	}

	@Override
	public <A> void setValue(String pColumnName, A pValue, boolean pOriginalValue) {
		wListRow.get(wCurrentRow).setValue(pColumnName, pValue);
		
		this.pvSetLocalDataModelValue(pColumnName, pValue);
	}

	/**
	 * Seta o valor da coluna infomada, considerando a linha corrente
	 * @param <A>
	 * @param pColumnIndex
	 * @return
	 */
	public <A> A getValue(int pColumnIndex){
		return this.getValue(this.getColumnName(pColumnIndex));
	}

	/**
	 * Seta o valor da coluna infomada, considerando a linha corrente
	 * @param pColumnName Nome da Coluna
	 * @return
	 */
	@Override
	public <A> A getValue(String pColumnName) {
		if (wDataModel != null){
			return pvGetLocalDataModelValue(pColumnName);
		}else{
			DBSColumn xC = wListRow.get(wCurrentRow).getColumn(pColumnName);
			if (xC==null){
				return null;
			}else{
				return xC.getValue();
			}
		}
	}
	
	@Override
	public <A> void setValue(String pColumnName, A pValue){
		 wListRow.get(wCurrentRow).setValue(pColumnName, pValue);
	}

	/** 
	 * Indica se exclui os espaços no ínicio é no final do dos valores lidos do arquivo
	 * @return
	 */
	public boolean getTrimValues() {
		return wTrimValues;
	}

	/** 
	 * Indica se exclui os espaços no ínicio é no final do dos valores lidos do arquivo
	 * @return
	 */
	public void setTrimValues(boolean pTrimValues) {
		this.wTrimValues = pTrimValues;
	}
	
	/**
	 * Retorna a quantidade de linha total do arquivo;
	 * @return
	 */
	@Override
	public int getRowsCount() {
		int xC = wRowsCountFile  - wNumberOfRowsToIgnoreHeader - wNumberOfRowsToIgnoreFooter;
		if (xC<0){
			xC = 0;
		}
		return xC;
	}

	
	/**
	 * Retorna o tamanho em Bytes do arquivo
	 * @return
	 */
	public long getFileSize() {
		return wFileSize;
	}
	

	/**
	 * Quantidade de linha que serão ignoradas no inicio
	 * @return
	 */
	public int getNumberOfRowsToIgnoreHeader() {
		return wNumberOfRowsToIgnoreHeader;
	}

	/**
	 * Quantidade de linha que serão ignoradas no inicio
	 * @return
	 */
	public void setNumberOfRowsToIgnoreHeader(int pNumberOfRowsToIgnoreHeader) {
		this.wNumberOfRowsToIgnoreHeader = pNumberOfRowsToIgnoreHeader;
	}

	/**
	 * Quantidade de linha que serão ignoradas no final 
	 * @return
	 */
	public int getNumberOfRowsToIgnoreFooter() {
		return wNumberOfRowsToIgnoreFooter;
	}

	/**
	 * Quantidade de linha que serão ignoradas no final 
	 * @return
	 */
	public void setNumberOfRowsToIgnoreFooter(int pNumberOfRowsToIgnoreFooter) {
		this.wNumberOfRowsToIgnoreFooter = pNumberOfRowsToIgnoreFooter;
	}	

	/**
	 * Indica se a primeira linha do arquivo contém a dafinição dos nomes das colunas
	 * @return
	 */
	public boolean getHeaderDefinesColumnsNames() {
		return wHeaderDefinesColumnsNames;
	}
	
	/**
	 * Indica se a primeira linha do arquivo contém a dafinição dos nomes das colunas
	 * @return
	 */
	public void setHeaderDefinesColumnsNames(boolean pHeaderDefinesColumnsNames) {
		this.wHeaderDefinesColumnsNames = pHeaderDefinesColumnsNames;
	}

	@Override
	public final Collection<DBSColumn> getColumns() {
		return wListRow.get(wCurrentRow).getColumns();
	}

	@Override
	public final DBSColumn getColumn(String pColumnName) {
		return wListRow.get(wCurrentRow).getColumn(pColumnName);
	}

	@Override
	public final DBSColumn getColumn(int pColumnIndex) {
		return wListRow.get(wCurrentRow).getColumn(pColumnIndex);
	}

	/**
	 * Retorna definição das colunas quando foram de tamanho fixo
	 * @return
	 */
	public DBSRow getFixedColumns() {
		return wFixedColumns;
	}

	/**
	 * Retorna definição das colunas quando foram de tamanho fixo
	 * @return
	 */
	public void setFixedColumns(DBSRow wFixedColumns) {
		this.wFixedColumns = wFixedColumns;
	}

	/**
	 * Retorna o conteúdo integral da linha atual.
	 * @param pColumnIndex
	 * @return
	 */
	public Object getRowValues(){
		return wListRow.get(wCurrentRow).getRowValues(wColumnsDelimiter);
	}
	
	/**
	 * Efetua um set nos valores da linha corrente.
	 * Estes valores não são gravados, apenas é feita a modificação em wListRow.
	 * @param pLineData
	 */
	public void setRowValues(String pLineData){
		wListRow.get(wCurrentRow).setRowValues(pLineData, wColumnsDelimiter);
	}
	
	/**
	 * Retorna a linha corrente como DBSRow
	 * @return
	 */
	public DBSRow getRow() {
		return wListRow.get(wCurrentRow);
	}
	
	/**
	 * Adiciona um DBSRow à lista wListRow.
	 * @param pRow
	 * @return false se não conseguiu adicioanar
	 */
	public boolean addRow(DBSRow pRow) {
		boolean xOk = false;
		if (!DBSObject.isEmpty(pRow)) {
			wListRow.add(pRow);
			wCurrentRow = wListRow.size()-1;
			xOk = true;
		}
		return xOk;
	}
	
	/**
	 * Quebra a linha em colunas do DBSRow e adiciona no wListRow.
	 * @param pLineData Linha crua.
	 * @return DBSRow
	 */
	public DBSRow addRow(String pLineData) {
		DBSRow xRow = null;
		if (!DBSObject.isEmpty(pLineData)) {
			xRow = pvGetRowVariableColumn(pLineData);
			addRow(xRow);
		}
		return xRow;
	}
	
	/**
	 * Retorna conteúdo integral da linha crua lida.
	 * @return
	 */
	public String getLineData(){
		return wLineData;
	}

	/**
	 * Recupela o nome da coluna a partir do index.
	 * @param pColumnIndex
	 * @return
	 */
	public String getColumnName(int pColumnIndex){
		if (wHeaderDefinesColumnsNames){
			return wColumnsHeader.get(pColumnIndex);
		}else{
			//Força o index como nome da coluna
			return new Integer(pColumnIndex).toString();
		}
	}
	
	public int getCurrentRow(){
		return wCurrentRow;
	}
	
	/**
	 * Cria arquivo(se não existir) e adiciona, ao final, os dados informados.<br/>
	 * Não utilizar os métodos <b>open() ou close()</b>.
	 * @param pString Dados para serem adicionados ao final do arquivo.
	 * @return true = sem erro; false = com erro
	 */
	public boolean append(String pString){
		Charset xCharset = Charset.forName(wEncode);
		Path xFile = Paths.get(wFileName);

		byte xData[] = pString.getBytes(xCharset);

		try {
			OutputStream xOut = Files.newOutputStream(xFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		    xOut.write(xData, 0, xData.length);
		    xOut.close(); // 22/05/2014 Incluido por Ricardo
		    return true;
		} catch (IOException e) {
			wLogger.error(e);
		    return false;
		}	
	}
	
	/**
	 * Cria arquivo(se não existir) e adiciona ao final os dados informados com CRLF
	 * @param pString Dados para serem adicionados ao final do arquivo
	 * @return true = sem erro; false = com erro
	 */
    public boolean appendLine(String pString){
    	String xStr = pString; 
    	if (xStr == null){
    		xStr = "";
    	}
    	//Inclui enter e line feed
    	xStr = xStr + new String("\r\n");
    	return this.append(xStr);
    }
	
	/**
	 * Abre arquivo para leitura
	 * @return true=sem erro
	 */
    @Override
	public boolean open(String pFileName){
		this.setFileName(pFileName);
		return this.open();
    }

	/**
	 * Abre arquivo para leitura.<br/>
	 * Utilize este método para ler o arquivo sequencialmente via o método <b>readLine()</b>.<br/>
	 * É necessário fechar o arquivo após a leitura utilizando o método <b>close</b>.
	 * Caso queira navegar pelos registros com moveFirst, movePrivious, etc, utilize o método loadFile ao invés do open.
	 * @return true=sem erro
	 */
    @Override
	public boolean open(){
    	boolean xOk = false;
		if (pvFireEventBeforeOpen()){
			if (pvCountLines()){ //Pega a quantidade de linha todal do arquivo
				if (pvOpen()){
					if (wHeaderDefinesColumnsNames){
						//Ignora as primeiras linhas, se assim for definido para fazer.
						for (int xI = 0; xI < getNumberOfRowsToIgnoreHeader(); xI++) {
							try {
								wReader.readLine();
							} catch (IOException e) {
								this.close();
								wLogger.error(e);
								return false;
							}
						}
						//Cria nome colunas a partir da primeira linha do arquivo
						pvCreateColumns();
					}else{//Se não houver demilitador, cria uma coluna que conterá todos os dados
						wColumnsHeader.add(this.getColumnName(1));
					}
					return true;
				}
			}
		}
		pvFireEventAfterClose(xOk);
		return xOk;
	}
	
	
	/**
	 * Fecha Arquivo
	 */
	@Override
	public void close(){
		if (pvFireEventBeforeClose()){
			pvClose();
			pvFireEventAfterClose(true);
		}	

	}	


	/**
	 * Carrega todo conteúdo do arquivo em memória.<br/>
	 * Utilizar o método <b>getRowValues()</b> ou <b>getLineData()</b> para recuperar o conteúdo da linha atual.
	 * Este método, permite a nagevação entre os registro com moveNext, moveFirst, etc.
	 * Não utilizar os métodos <b>open(), close(), readLine()</b>.
	 * @return True = Sem erro; False = Com erro
	 * @throws DBSIOException 
	 */
	public boolean loadFile(){
		Boolean xOk = true;
		//Indica que o processo será iniciado e verifica se deve continuar
		if (open()){
			try {
				String xLineData = null;
				//Le linha, move para xLine. Se conteúdo não for nulo, incluir linha
				while ((xLineData = pvReadLine()) != null ){
					pvGetRow(xLineData);
					if (getInterrupt()) {
						xOk = false;
						break;
					}
				}
				this.moveBeforeFirstRow();
				return xOk;
			}catch(Exception e){
				wLogger.error(e);
				return false;
			}finally{
				close();
			}
		}else{
			return false;
		}
		
	}

	/**
	 * Le próxima linha.<br/>
	 * Este método deve ser utilizado quando arquivo for aberto via o método <b>open()</b> e se deseja ler o seu conteúdo sequencialmente.<br/>
	 * Utilize o retorno deste método para saber se foi lido novo registro com sucesso. 
	 * É importante fechar o arquivo via o método <b>close()</b> após finalizado a utilização deste arquivo.<br/>
	 * Para navegar pelos registros utilizando moveNext, moveFirst, etc, utilize <b>loadFile</b>.
	 * @return True = Novo registro ligo; False = Não há novo registro.
	 * @throws DBSIOException 
	 */
	public boolean readLine(){
		String xLineData =  pvReadLineIgnoringHeaderAndFooter();
		if (xLineData!=null){
			pvGetRow(xLineData);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Método de leitura de arquivo extraído das classes do Addario.
	 * 
	 * @param pOriginalFile
	 * @return BufferedReader
	 */
	@Deprecated
	public BufferedReader readFile(String pOriginalFile) {
		BufferedReader xLeitorLinhaALinha;
		try {
			//String xPrimeiraLinha = "";
			InputStream xFile = new FileInputStream(pOriginalFile);
			xLeitorLinhaALinha = new BufferedReader(new InputStreamReader(
					xFile , "ISO-8859-1"));
			//xPrimeiraLinha = xLeitorLinhaALinha.readLine();
			//System.out.println(xPrimeiraLinha);
			return xLeitorLinhaALinha;
		} catch (IOException e) {
			wLogger.error(e);
			throw new RuntimeException();
		}
	}
	
	/**
	 * Move par a linha antes do inicio.
	 * É necessário que arquivo tenha lido via <b>loadFile</b> para utilizar este método. Não utilizar o open/close/readLine.
	 */	
	@Override
	public void moveBeforeFirstRow(){
		if (pvIsLoaded()){
			wCurrentRow = -1;
			pvSetRowPositionChanged(true);
		}
	}
	
	/**
	 * Move para a primeira linha.
	 * É necessário que arquivo tenha lido via <b>loadFile</b> para utilizar este método. Não utilizar o open/close/readLine.
	 * @return True = moveu; False=Não moveu
	 */
	@Override
	public boolean moveFirstRow(){
		boolean xB = false;
		if (pvIsLoaded()){
			if (wListRow.size()>0){
				wCurrentRow = 0;
				xB = true;
				pvSetRowPositionChanged(xB);
			}
		}
		return xB;
	}
	

	/**
	 * Move para a última linha.
	 * É necessário que arquivo tenha lido via <b>loadFile</b> para utilizar este método. Não utilizar o open/close/readLine.
	 * @return True = moveu; False=Não moveu
	 */
	@Override
	public boolean moveLastRow(){
		boolean xB = false;
		if (pvIsLoaded()){
			if (wListRow.size()>0){
				wCurrentRow = wListRow.size()-1;
				xB = true;
				pvSetRowPositionChanged(xB);
			}
		}
		return xB;
	}	

	/**
	 * Move para a linha anterior.
	 * É necessário que arquivo tenha lido via <b>loadFile</b> para utilizar este método. Não utilizar o open/close/readLine.
	 * @return True = moveu; False=Não moveu
	 */
	@Override
	public boolean movePreviousRow(){
		boolean xB = false;
		if (pvIsLoaded()){
			if (wCurrentRow > wListRow.size()-1){
				wCurrentRow--;
				xB = true;
				pvSetRowPositionChanged(xB);
			}
		}
		return xB;
	}	
	
	/**
	 * Move para a linha posterior.<br/>
	 * É necessário que arquivo tenha lido via <b>loadFile</b> para utilizar este método. Não utilizar o open/close/readLine.
	 * @return True = moveu; False=Não moveu
	 */
	@Override
	public boolean moveNextRow(){
		boolean xB = false;
		if (pvIsLoaded()){
			if (wCurrentRow < wListRow.size()-1){
				wCurrentRow++;
				xB = true;
				pvSetRowPositionChanged(xB);
			}
		}
		return xB;
	}	

	@Override
	public String getUK() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object getUKValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public int executeDelete() throws DBSIOException {
		if (pvFireEventBeforeDelete()){
			pvFireEventAfterDelete(true);
		}
		return 0;
	}

	@Override
	public int executeUpdate() throws DBSIOException {
		if (pvFireEventBeforeUpdate()){
			pvFireEventAfterUpdate(true);
		}
		return 0;
	}


	@Override
	public int executeInsert() throws DBSIOException {
		if (pvFireEventBeforeInsert()){
			pvFireEventAfterInsert(true);
		}
		return 0;
	}


	@Override
	public int executeMerge() throws DBSIOException {
		if (pvFireEventBeforeMerge()){
			pvFireEventAfterMerge(true);
		}
		return 0;
	}

	
	//*****************************************************************************************
	// private methods
	//*****************************************************************************************
	/**
	 * Abre o arquivo local e reseta os registros em mémoria
	 * @return true=sem erro; false=com erro
	 */
	private boolean pvOpen(){
		try{
//			wCurrentRow = 0;
			wCurrentRowFile = -1;
			wListRow.clear();
			wListDataModel.clear();
			//Define o encode
			Charset xCharset = Charset.forName(wEncode);
			//Define o arquivo
			Path xFile = Paths.get(wFileName);
			wReader = Files.newBufferedReader(xFile, xCharset);
			wFileSize = xFile.toFile().length();
			wIsOpened = true;//Indica que arquivo foi aberto
			return true;
		}catch(Exception e){
			wLogger.error(e);
			return false;
		}
	}
	

	/**
	 * Fecha arquivo
	 */
	private void pvClose(){
		wCurrentRow = -1;
		try {
			wReader.close();
			wIsOpened = false;
		} catch (Exception e) {
			wLogger.error(e);
		}
		
	}

	/**
	 * Lê nova linha do arquivo ignorando a quantidade de linha do cabeçalho e do rodapé, conforme definição do usuário
	 * @return String com dados da linha
	 */
	private String pvReadLineIgnoringHeaderAndFooter(){
		//Lê é ignora as linhas conforme a quantidade
		while (wNumberOfRowsToIgnoreHeader!=wRowsIgnoredHeader){
			wRowsIgnoredHeader++;
			pvReadLine();
		}
		if (wCurrentRowFile + 1 < wRowsCountFile-wNumberOfRowsToIgnoreFooter){
			return pvReadLine();
		}else{
			return null;
		}
	}

	/**
	 * Lê nova linha do arquivo
	 * @return String com dados da linha
	 */
	private String pvReadLine(){
		if (wIsOpened){
			try {
				if (wReader.ready()){ //Se exist dados para serem lidos..
					wCurrentRowFile++; //Incrementa marcador da posição atual
					String xLineData = wReader.readLine();
					return xLineData;
				}
				return null;
			} catch (IOException e) {
				this.close();
				wLogger.error(e);
				return null;
			}
		}else{
			return null;
		}
	}


	
	/**
	 * Cria nome das colunas a partir da primeira linha(header)
	 * @param pHeader
	 */
	private void pvCreateColumns(){
		wColumnsHeader.clear();
		wColumnsHeader = DBSString.toArray(pvReadLine(), wColumnsDelimiter, false);
	}

	/**
	 * A partir dos dados lidos, cria linha(DBSRow) e adiciona a lista(se keepData estive habilitado)
	 * @param pLineData
	 * @throws DBSIOException 
	 */
	//TODO
	private void pvGetRow(String pLineData){
		DBSRow 	xRow;
		boolean xOk = true;
		String 	xLineData = pLineData;
		//Converte para ASCII
		if (wConvertToASCII){ 
			xLineData = DBSString.toASCII(pLineData);
		}
		
		//Move conteúdo integral da linha lida 
		wLineData = xLineData;
		
		xOk = pvFireEventBeforeRead();
		
		if (xOk){
			if (wType==TYPE.FIXED_COLUMNS){
				xRow = pvGetRowFixedColumn(xLineData);
			}else{
				xRow = pvGetRowVariableColumn(xLineData);
			}
			if (!DBSObject.isEmpty(xRow)) {
				//Apaga a linha amarmazenada para evitar que o arquivo seja integramente lido para a memória. 
				if(!wKeepData){
					wListRow.clear();
					wListDataModel.clear();
				}
				DataModelClass xDataModel = this.createDataModel();
				if (xDataModel!=null){
					for (int xX=0; xX< xRow.getColumns().size(); xX++){
						//Copia o valor para o respectivo atributo no DataModel
						DBSIO.setDataModelFieldsValue(xDataModel, xX, xRow.getColumn(xX).getValue());
					}
					//Adiciona linha como DataModel
					wListDataModel.add(xDataModel);
				}
				//Adiociona linha já processada se não for vazia 
				addRow(xRow); //Alterado em 28/05/2014 wListRow.add(xRow);
			}
		}
		//Inclui nova linha
		wCurrentRow = wListRow.size()-1;
		pvFireEventAfterRead(xOk);
		wLineData = "";
	}
	
	/**
	 * Cria linha a partir dos dados lidos, separando os valores conforme o delimitador definido
	 * Caso não tenha sido definido um delimitador, toda a linha será incluido em uma única coluna
	 * @param pLine
	 * @throws DBSIOException 
	 */
	private DBSRow pvGetRowVariableColumn(String pLine) {
		//Move os dados da linha para a um array, onde cada item do array é uma das colunas, utilizando o delimitador informado
		ArrayList<String> 	xColumns = DBSString.toArray(pLine, wColumnsDelimiter, false, wTrimValues);
		DBSRow 				xRow = null;
		//Se quantidade de colunas encontrada na leitura da linha for superior a quantidade de colunas definidar no cabeçalho....
		if (wHeaderDefinesColumnsNames && xColumns.size() > wColumnsHeader.size()){
			getLinesWithError().add(pLine);
//			wLogger.warn("Quantidade de colunas lidas é superior a quantidade de colunas definidas no cabeçalho.[Linha:" + (wCurrentRowFile+1) + "]\r" + pLine + "\r");
		}else{
			//Cria nova linha vázia;
			xRow = new DBSRow(); 
			//Loop em todos os itens do array
			for (int xX=0; xX<=xColumns.size()-1;xX++){
				//Se o conteúdo da coluna recupedado do array não for vazio
				if (!DBSObject.isEmpty(xColumns.get(xX))){
					//Adiciona nova coluna a linha com o conteúdo da array contendo a linha lida
					//pvMergeColumn(xRow, getColumnName(xX), xColumns.get(xX));
					xRow.MergeColumn(this.getColumnName(xX), xColumns.get(xX));
				}else{
					//Se o conteúdo da coluna recupado do array for vázio..
					//Se existir um delimitador definido
					if (!DBSObject.isNull(wColumnsDelimiter)){
						//Inclui uma coluna vázio
						//pvMergeColumn(xRow, getColumnName(xX), "");
						xRow.MergeColumn(this.getColumnName(xX), "");
					}
				}
			}
		}
		return xRow;
	}
	
	/**
	 * Cria linha a partir dos dados lidos, separando os valores conforme o tamanho(size) definido para cada coluna.
	 * @param pLine
	 */
	private DBSRow pvGetRowFixedColumn(String pLine){
		
		//Se foi definida qualquer coluna fixa
		if (wFixedColumns.getColumns().size()==0){
			//Inclui uma coluna sendo o tamanho igual a tamanho total da linha lida
			wFixedColumns.MergeColumn("1", pLine.length());
		}
		//Cria nova linha
		DBSRow xRow = new DBSRow();
		//Cria as colunas da linha a partir da definição das colunas fixas
		for (DBSColumn xColumn: wFixedColumns.getColumns()){
			xRow.MergeColumn(xColumn.getColumnName(), xColumn.getDataType(), xColumn.getSize(), DBSString.repeat(" ", xColumn.getDisplaySize()));
		}
		//Move a linha lida para a linha criada. O setRowValue faz o distribuição automaticamente pelas colunas
		xRow.setRowValues(pLine);
		
		return xRow;

	}

	/**
	 * Atualiza a variável local com a quantidade de linhas do arquivo.
	 * Para isso lê arquivo até o fim, fecha para resetar a posição e abre novamente.
	 * @return True = Sem erro; False = Com erro
	 */
	private boolean pvCountLines(){
		wRowsCountFile = 0;
		
		if (pvOpen()){
			try{
				//Le linha, move para xLine. Se conteúdo não for nulo, incluir linha
				while (pvReadLine() != null ){
					wRowsCountFile ++;
				}
				return true;
			}catch(Exception e){
				wLogger.error(e);
				return false;
			}finally{
				pvClose();
			}
		}else{
			return false;
		}
	}

	/**
	 * Informa que posição atual do registro corrente foi alterada ou não
	 * @param pChanged : true = foi alterada ; false = não foi altera
	 * @throws DBSIOException 
	 */
	private void pvSetRowPositionChanged(boolean pChanged) {
		pvSaveValueOriginal();
		super.setRowPositionChanged(pChanged);
	}
	
	private void pvSaveValueOriginal() {
		//Atualiza valores atuais e reseta valor original 
		if (wCurrentRow==-1){
			this.pvCreateDataModel();
		}else{
			for (int xX=0; xX <  wListRow.get(wCurrentRow).getColumns().size(); xX++){
				this.pvSetLocalDataModelValue(xX, wListRow.get(wCurrentRow).getColumn(xX).getValue());
			}		
//			for (DBSColumn xColumn: wListRow.get(wCurrentRow).getColumns()){
//				this.setLocalDataModelValue(xColumn.getColumnName(), wListRow.get(wCurrentRow).getValue(xColumn.getColumnName()));
//			}		
//			for (DBSColumn xColumn: wListRow.get(wCurrentRow).getColumns()){
//				this.setValue(xColumn.getColumnName(), wListRow.get(wCurrentRow).getValue(xColumn.getColumnName()), true);
//			}		
		}
	}

	/**
	 * Retorna se arquivo foi garradado via loadFile e contém registros.
	 * @return
	 */
	private boolean pvIsLoaded(){
		if (wListRow.size() == 0){
//			System.out.println(wFileName + "vázio ou não carregado via loadFile");
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <A> List<A> pvGetList(boolean pReturnListDataModel) throws DBSIOException {
		if (pReturnListDataModel){
			return (List<A>) wListDataModel;
		}else{
			return (List<A>) wListRow;
		}
	}

	@Override
	public void beforeOpen(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}
	
	@Override
	public void afterOpen(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void beforeInsert(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void afterInsert(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void beforeRead(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void afterRead(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void beforeUpdate(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void afterUpdate(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void beforeMerge(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void afterMerge(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void beforeDelete(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void afterDelete(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void beforeMove(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
	@Override
	public void afterMove(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}

	@Override
	public void beforeClose(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}

	@Override
	public void afterClose(DBSDAOEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
		
	}
}
