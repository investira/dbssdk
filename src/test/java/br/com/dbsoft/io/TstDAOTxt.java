package br.com.dbsoft.io;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.core.DBSSDK.IO.DATATYPE;
import br.com.dbsoft.io.DBSDAOTxt.TYPE;
import br.com.dbsoft.util.DBSBoolean;
import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSFormat;
import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;

public class TstDAOTxt {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	//@Test
//	public <T> void  test_readDelimited() throws UnsupportedEncodingException {
//		DBSDAOTxt<T> xDAO = new DBSDAOTxt<T>("/Users/ricardovillar/Downloads/ifeed/CVM/CIAS_ABERTAS", DBSSDK.FILE.ENCODE.ISO_8859_1,TYPE.DELIMITED_COLUMNS,";",true);
//		assertTrue("TESTE ESPERAVA TRUE", xDAO.loadFile());
//		//System.out.println(xDAO.getValue(1));
//	}
	
//	@Test
	public void test_append() {
		DBSDAOTxt<Object> xDAO = new DBSDAOTxt<Object>("/Users/ricardo.villar/ifeed/debentures/pub/debentures.txt", DBSSDK.FILE.ENCODE.ISO_8859_1);
		DBSDAOTxt<Object> xDAOWrite = new DBSDAOTxt<Object>("/Users/ricardo.villar/ifeed/debentures/pub/new.txt", DBSSDK.FILE.ENCODE.ISO_8859_1);
	    if (xDAO.loadFile()){
	    	System.out.println("ABRIU");
	    	while (xDAO.moveNextRow()){
	    		System.out.println(xDAO.getRowValues());
	    		xDAOWrite.append(xDAO.getRowValues().toString());
	    	}
	    }else{
	    	System.out.println("NÃO ABRIU");
	    }
//	    
//		if (xDAOTxt.loadFile()) {
//	    	xDAOTxtWriter.open();
//    		xDAOTxt.moveBeforeFirstRow();
//    		while (xDAOTxt.moveNextRow()) {
//    			xRowIndex++;
//    			xLinha = DBSString.toString(xDAOTxt.getRowValues());
//    			if (xRowIndex == 3) {
//    				//Acrescenta um Delimitador ao final da linha do Header
//    				xLinha = xLinha + DELIMITADOR;
//    			}
//    			xDAOTxtWriter.append(xLinha + "\n");
//    		}
//    		xDAOTxtWriter.close();
//    		xDAOTxt.close();
//	    } else {
//	    	xOk = false;
//	    	
//	    }
	}

//	@Test
	public void test_loop1() {
		DBSDAOTxt<Object> xDAO = new DBSDAOTxt<Object>("/users/jose.avila/downloads/ifeed/CVM/CIAS_ABERTAS/SPW_CIA_ABERTA.TXT", DBSSDK.FILE.ENCODE.ISO_8859_1);
	    if (xDAO.loadFile()){
	    	System.out.println("ABRIU");
	    	while (xDAO.moveNextRow()){
	    		System.out.println(xDAO.getRowValues());
	    	}
	    }else{
	    	System.out.println("NÃO ABRIU");
	    }
	}

//	@Test
	public void test_loop2() {
		DBSDAOTxt<Object> xDAO = new DBSDAOTxt<Object>("/Users/ricardo.villar/ifeed/debentures/pub/debentures.txt", DBSSDK.FILE.ENCODE.ISO_8859_1);
	    if (xDAO.open()){
	    	System.out.println("ABRIU");
	    	while (xDAO.readLine()){
	    		System.out.println(xDAO.getRowValues());
	    	}
		    xDAO.close();
	    }else{
	    	System.out.println("NÃO ABRIU");
	    }
	}
	
	@Test
	public void test_loop3() {
		DBSDAOTxt<Object> xDAO = new DBSDAOTxt<Object>();
		xDAO.setHeaderDefinesColumnsNames(true);
		xDAO.setColumnsDelimiter(";");
		xDAO.setKeepData(true);
		xDAO.setFileName("/users/jose.avila/downloads/ifeed/CVM/CIAS_ABERTAS/SPW_CIA_ABERTA.TXT");
	    if (xDAO.loadFile()){
	    	System.out.println("ABRIU");
	    	while (xDAO.moveNextRow()){
	    		DBSRow xRowData = pvGetRowFixedColumn(xDAO.getColumnsHeader(), DBSString.toString(xDAO.getRowValues()));
	    		System.out.println(xRowData.getValue("DENOMINACAO SOCIAL"));
	    	}
	    }else{
	    	System.out.println("NÃO ABRIU");
	    }
	}
	
	/**
	 * Cria linha a partir dos dados lidos, separando os valores conforme o tamanho(size) definido para cada coluna.
	 * @param pLine
	 */
	private DBSRow pvGetRowFixedColumn(List<String> pColumnsHeader, String pLine){
		DBSRow xRow = new DBSRow();
		//Cria as colunas
		for (String xColumnName : pColumnsHeader) {
			xRow.MergeColumn(xColumnName);
		}
		//Move a linha lida para a linha criada. O setRowValue faz o distribuição automaticamente pelas colunas
		xRow.setRowValues(pLine, ";");
		
		return xRow;

	}
	
	//@Test
	public void test_readAll() throws UnsupportedEncodingException {
		DBSDAOTxt xDAO = new DBSDAOTxt("/Users/ricardovillar/downloads/cvm/empresas.txt", DBSSDK.FILE.ENCODE.ISO_8859_1);
		assertTrue("TESTE ESPERAVA TRUE", xDAO.loadFile());
	}
	
	//@Test
	public void test_writeAllDelimited() throws UnsupportedEncodingException {
		DBSDAOTxt xDAO = new DBSDAOTxt("/Users/ricardovillar/Downloads/ifeed/CVM/CIAS_ABERTAS/SPW_CI2.TXT", DBSSDK.FILE.ENCODE.ISO_8859_1,TYPE.DELIMITED_COLUMNS,";",true);
		xDAO.loadFile();
//		System.out.println(xDAO.getRowsCount()); //2049
		while(xDAO.moveNextRow()){
			System.out.println(xDAO.getRowValues() + "\r\f");
		}
		//assertTrue("TESTE ESPERAVA TRUE", xDAO.append());
	}

	//@Test
	public void test_writeAllDelimited2() throws UnsupportedEncodingException {
		DBSDAOTxt    	xDAOTxt = new DBSDAOTxt<Object>();
		xDAOTxt.setConvertToASCII(true);
		xDAOTxt.setHeaderDefinesColumnsNames(true);
		xDAOTxt.setColumnsDelimiter(";");
		xDAOTxt.setKeepData(false);
		xDAOTxt.open("/Users/ricardovillar/Downloads/ifeed/CVM/CIAS_ABERTAS/SPW_CI2.TXT");
		//System.out.println(xDAOTxt.getRowValues());
		while (xDAOTxt.readLine()) {
			if (xDAOTxt.getColumns().size() > 0){
				System.out.println(xDAOTxt.getRowValues() + "\r\f");
			}
		}
		//assertTrue("TESTE ESPERAVA TRUE", xDAO.append());
	}
	
	//@Test
	public void test_writeAll() throws UnsupportedEncodingException {
		DBSDAOTxt xDAORead = new DBSDAOTxt("/Users/ricardovillar/downloads/cvm/empresas.txt", DBSSDK.FILE.ENCODE.ISO_8859_1, TYPE.DELIMITED_COLUMNS,";",  true);
		DBSDAOTxt xDAOWrite = new DBSDAOTxt("/Users/ricardovillar/downloads/cvm/empresas2.txt");
		xDAORead.setTrimValues(true);
		String xStr;
		xDAORead.setNumberOfRowsToIgnoreHeader(1);
		xDAORead.setNumberOfRowsToIgnoreFooter(1);
		xDAORead.loadFile();
		while(xDAORead.moveNextRow()){
			
			xStr = xDAORead.getRowValues().toString();
			System.out.println(xStr);
			xDAOWrite.appendLine(xStr);
		}
		//assertTrue("TESTE ESPERAVA TRUE", xDAO.append());
	}

//	@Test
	public void test_fixedSize() throws UnsupportedEncodingException {
		DBSDAOTxt<Object> xDAORead = new DBSDAOTxt<Object>("/Users/jose.avila/Downloads/BDIN/BDIN.TXT", DBSSDK.FILE.ENCODE.ISO_8859_1, TYPE.FIXED_COLUMNS);
		xDAORead.setConvertToASCII(true);
		xDAORead.setHeaderDefinesColumnsNames(false);
		xDAORead.setKeepData(false);
		//Registra a classe que será chamada na ocorrência dos eventos de leitura
		xDAORead.addEventListener(new xProcessRead(xDAORead));
		
		assertTrue("TESTE ESPERAVA TRUE", xDAORead.open());
	}
	
//	@Test
	public void test_BDIN() throws UnsupportedEncodingException {
		String xArquivoBDIN = "/Users/jose.avila/Downloads/ifeed/BMF/BDIN/BDIN";
		DBSDAOTxt<Object> xDAORead = new DBSDAOTxt<Object>(xArquivoBDIN, DBSSDK.FILE.ENCODE.ISO_8859_1, TYPE.FIXED_COLUMNS);
		xDAORead.setConvertToASCII(true);
		xDAORead.setHeaderDefinesColumnsNames(false);
		xDAORead.setKeepData(false);
		xDAORead.setTrimValues(false);
		//Registra a classe que será chamada na ocorrência dos eventos de leitura
		xDAORead.addEventListener(new xProcessRead(xDAORead));
		
		assertTrue("TESTE ESPERAVA TRUE", xDAORead.loadFile());
	}

//	@Test
	public void test_Selic() throws UnsupportedEncodingException {
		String xArquivoBDIN = "/Users/jose.avila/Downloads/ifeed/SELIC/20131101ASEL007.TXT";
		DBSDAOTxt<Object> xDAORead = new DBSDAOTxt<Object>(xArquivoBDIN, DBSSDK.FILE.ENCODE.ISO_8859_1, TYPE.FIXED_COLUMNS);
		xDAORead.setConvertToASCII(true);
		xDAORead.setHeaderDefinesColumnsNames(false);
		xDAORead.setKeepData(false);
		xDAORead.setTrimValues(true);
		//Registra a classe que será chamada na ocorrência dos eventos de leitura
		xDAORead.addEventListener(new xLayoutSELIC(xDAORead));
		
		assertTrue("TESTE ESPERAVA TRUE", xDAORead.loadFile());
	}
	
//	@Test
	public void test_ANDIMA() throws UnsupportedEncodingException {
		DBSDAOTxt<Object> xDAOTxt = new DBSDAOTxt<Object>("/Users/jose.avila/Downloads/ifeed/ANDIMA/Ms131101.TXT", DBSSDK.FILE.ENCODE.ISO_8859_1,TYPE.DELIMITED_COLUMNS,"@",false);
		xDAOTxt.setConvertToASCII(true);
		if (xDAOTxt.open()) {
			while(xDAOTxt.readLine()) {
				if (!DBSObject.isEmpty(xDAOTxt.getRowValues()) && xDAOTxt.getCurrentRow() > 2) {
					System.out.println(xDAOTxt.getValue(0));
					System.out.println(xDAOTxt.getRowValues() + "\r\f");
				}
			}
			xDAOTxt.close();
		}
		//assertTrue("TESTE ESPERAVA TRUE", xDAO.append());
	}
	
	private class xProcessRead implements IDBSDAOEventsListener{
		
		DBSDAOTxt<Object> 	wDAOTxt;
		Date				wDataCot;
		String 				wLineData;
		DBSRow 				wRowHeader = new DBSRow();
		DBSRow 				wRowIndices = new DBSRow();
		DBSRow 				wRowNegociacoesPapel = new DBSRow();
		DBSRow 				wRowNegociacoesBDI = new DBSRow();
		DBSRow 				wRowOscilacoesMercado = new DBSRow();
		DBSRow 				wRowOscilacoesAcoes = new DBSRow();
		DBSRow 				wRowMaisNegociadasMercadoAVista = new DBSRow();
		DBSRow 				wRowMaisNegociadas = new DBSRow();
		DBSRow 				wRowIOPV = new DBSRow();
		DBSRow 				wRowBDRs = new DBSRow();
		DBSRow 				wRowTrailer = new DBSRow();
		
		xProcessRead(DBSDAOTxt<Object> pDAO){
			wDAOTxt = pDAO;
		}

		@Override
		public void beforeOpen(DBSDAOEvent pEvent) {
			System.out.println("ProcessingBegin: ");

			//Header - 00
			wRowHeader.clear();
			wRowHeader.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowHeader.MergeColumn("NOME", DATATYPE.STRING, 8, null);
			wRowHeader.MergeColumn("CodigoOrigem", DATATYPE.INT, 8, null);
			wRowHeader.MergeColumn("CodigoDestino", DATATYPE.INT, 4, null);
			wRowHeader.MergeColumn("DataGeracaoArquivo", DATATYPE.STRING, 8, null);
			wRowHeader.MergeColumn("DataPregao", DATATYPE.STRING, 8, null);
			wRowHeader.MergeColumn("HoraGeracao", DATATYPE.TIME, 4, null);
			wRowHeader.MergeColumn("Reserva", DATATYPE.STRING, 308, null);
			
			//Resumo Diário dos Índices - 01
			wRowIndices.clear();
			wRowIndices.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowIndices.MergeColumn("IDENTI", DATATYPE.INT, 2, null);
			wRowIndices.MergeColumn("NOMIND", DATATYPE.STRING, 30, null);
			wRowIndices.MergeColumn("IDCABE", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("IDCMIN", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("IDCMAX", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("IDCMED", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("IDCLIQ", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("IDMAXA", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("DATMAX", DATATYPE.STRING, 8, null);
			wRowIndices.MergeColumn("IDMINA", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("DATMIN", DATATYPE.STRING, 8, null);
			wRowIndices.MergeColumn("IDCFEC", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("SINEVO", DATATYPE.STRING, 1, null);
			wRowIndices.MergeColumn("EVOIND", DATATYPE.DOUBLE, 5, null);
			wRowIndices.MergeColumn("SINONT", DATATYPE.STRING, 1, null);
			wRowIndices.MergeColumn("EVONTE", DATATYPE.DOUBLE, 5, null);
			wRowIndices.MergeColumn("SINSEM", DATATYPE.STRING, 1, null);
			wRowIndices.MergeColumn("EVOSEM", DATATYPE.DOUBLE, 5, null);
			wRowIndices.MergeColumn("SI1SEM", DATATYPE.STRING, 1, null);
			wRowIndices.MergeColumn("EV1SEM", DATATYPE.DOUBLE, 5, null);
			wRowIndices.MergeColumn("SINMES", DATATYPE.STRING, 1, null);
			wRowIndices.MergeColumn("EVOMES", DATATYPE.DOUBLE, 5, null);
			wRowIndices.MergeColumn("SI1MES", DATATYPE.STRING, 1, null);
			wRowIndices.MergeColumn("EV1MES", DATATYPE.DOUBLE, 5, null);
			wRowIndices.MergeColumn("SINANO", DATATYPE.STRING, 1, null);
			wRowIndices.MergeColumn("EVOANO", DATATYPE.DOUBLE, 5, null);
			wRowIndices.MergeColumn("SI1ANO", DATATYPE.STRING, 1, null);
			wRowIndices.MergeColumn("EV1ANO", DATATYPE.DOUBLE, 5, null);
			wRowIndices.MergeColumn("ACOALT", DATATYPE.INT, 3, null);
			wRowIndices.MergeColumn("ACOBAI", DATATYPE.INT, 3, null);
			wRowIndices.MergeColumn("ACOEST", DATATYPE.INT, 3, null);
			wRowIndices.MergeColumn("ACOTOT", DATATYPE.INT, 3, null);
			wRowIndices.MergeColumn("NNGIND", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("QTDIND", DATATYPE.INT, 15, null);
			wRowIndices.MergeColumn("VOLIND", DATATYPE.DOUBLE, 17, null);
			wRowIndices.MergeColumn("IDCMDP", DATATYPE.INT, 6, null);
			wRowIndices.MergeColumn("RESERVA", DATATYPE.STRING, 148, null);
			
			//Resumo Diário de Negociações por Papel - Mercado - 02
			wRowNegociacoesPapel.clear();
			wRowNegociacoesPapel.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowNegociacoesPapel.MergeColumn("CODBDI", DATATYPE.STRING, 2, null);
			wRowNegociacoesPapel.MergeColumn("DESBDI", DATATYPE.STRING, 30, null);
			wRowNegociacoesPapel.MergeColumn("NOMRES", DATATYPE.STRING, 12, null);
			wRowNegociacoesPapel.MergeColumn("ESPECI", DATATYPE.STRING, 10, null);
			wRowNegociacoesPapel.MergeColumn("INDCAR", DATATYPE.STRING, 1, null);
			wRowNegociacoesPapel.MergeColumn("CODNEG", DATATYPE.STRING, 12, null);
			wRowNegociacoesPapel.MergeColumn("TPMERC", DATATYPE.INT, 3, null);
			wRowNegociacoesPapel.MergeColumn("NOMERC", DATATYPE.STRING, 15, null);
			wRowNegociacoesPapel.MergeColumn("PRAZOT", DATATYPE.STRING, 3, null);
			wRowNegociacoesPapel.MergeColumn("PREABE", DATATYPE.DOUBLE, 11, null);
			wRowNegociacoesPapel.MergeColumn("PREMAX", DATATYPE.DOUBLE, 11, null);
			wRowNegociacoesPapel.MergeColumn("PREMIN", DATATYPE.DOUBLE, 11, null);
			wRowNegociacoesPapel.MergeColumn("PREMED", DATATYPE.DOUBLE, 11, null);
			wRowNegociacoesPapel.MergeColumn("PREULT", DATATYPE.DOUBLE, 11, null);
			wRowNegociacoesPapel.MergeColumn("SINOSC", DATATYPE.STRING, 1, null);
			wRowNegociacoesPapel.MergeColumn("OSCILA", DATATYPE.DOUBLE, 5, null);
			wRowNegociacoesPapel.MergeColumn("PREOFC", DATATYPE.DOUBLE, 11, null);
			wRowNegociacoesPapel.MergeColumn("PREOFV", DATATYPE.DOUBLE, 11, null);
			wRowNegociacoesPapel.MergeColumn("TOTNEG", DATATYPE.INT, 5, null);
			wRowNegociacoesPapel.MergeColumn("QUATOT", DATATYPE.INT, 15, null);
			wRowNegociacoesPapel.MergeColumn("VOLTOT", DATATYPE.DOUBLE, 17, null);
			wRowNegociacoesPapel.MergeColumn("PREEXE", DATATYPE.DOUBLE, 11, null);
			wRowNegociacoesPapel.MergeColumn("DATVEN", DATATYPE.STRING, 8, null);
			wRowNegociacoesPapel.MergeColumn("INDOPC", DATATYPE.INT, 1, null);
			wRowNegociacoesPapel.MergeColumn("NOMIND", DATATYPE.STRING, 15, null);
			wRowNegociacoesPapel.MergeColumn("FATCOT", DATATYPE.INT, 7, null);
			wRowNegociacoesPapel.MergeColumn("PTOEXE", DATATYPE.DOUBLE, 13, null);
			wRowNegociacoesPapel.MergeColumn("CODISI", DATATYPE.STRING, 12, null);
			wRowNegociacoesPapel.MergeColumn("DISMES", DATATYPE.INT, 3, null);
			wRowNegociacoesPapel.MergeColumn("ESTILO", DATATYPE.INT, 1, null);
			wRowNegociacoesPapel.MergeColumn("NOMEST", DATATYPE.STRING, 15, null);
			wRowNegociacoesPapel.MergeColumn("ICOATV", DATATYPE.INT, 3, null);
			wRowNegociacoesPapel.MergeColumn("OSCPRE", DATATYPE.DOUBLE, 7, null);
			wRowNegociacoesPapel.MergeColumn("RESERVA", DATATYPE.STRING, 44, null);
			
			//Resumo Diário de Negociações por Código de BDI - 03
			wRowNegociacoesBDI.clear();
			wRowNegociacoesBDI.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowNegociacoesBDI.MergeColumn("CODBDI", DATATYPE.INT, 2, null);
			wRowNegociacoesBDI.MergeColumn("DESBDI", DATATYPE.STRING, 30, null);
			wRowNegociacoesBDI.MergeColumn("TOTNEG", DATATYPE.INT, 5, null);
			wRowNegociacoesBDI.MergeColumn("QUATOT", DATATYPE.INT, 15, null);
			wRowNegociacoesBDI.MergeColumn("VOLTOT", DATATYPE.DOUBLE, 17, null);
			wRowNegociacoesBDI.MergeColumn("TOTNEG2", DATATYPE.INT, 9, null);
			wRowNegociacoesBDI.MergeColumn("RESERVA", DATATYPE.STRING, 270, null);
			
			//Maiores Oscilações no Mercado a Vista - 04
			wRowOscilacoesMercado.clear();
			wRowOscilacoesMercado.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowOscilacoesMercado.MergeColumn("INDOSC", DATATYPE.STRING, 1, null);
			wRowOscilacoesMercado.MergeColumn("NOMRES", DATATYPE.STRING, 12, null);
			wRowOscilacoesMercado.MergeColumn("ESPECI", DATATYPE.STRING, 10, null);
			wRowOscilacoesMercado.MergeColumn("PREULT", DATATYPE.DOUBLE, 11, null);
			wRowOscilacoesMercado.MergeColumn("TOTNEG", DATATYPE.INT, 5, null);
			wRowOscilacoesMercado.MergeColumn("OSCPRE", DATATYPE.DOUBLE, 5, null);
			wRowOscilacoesMercado.MergeColumn("CODMEG", DATATYPE.STRING, 12, null);
			wRowOscilacoesMercado.MergeColumn("OSCILA", DATATYPE.DOUBLE, 7, null);
			wRowOscilacoesMercado.MergeColumn("RESERVA", DATATYPE.STRING, 285, null);
			
			//Maiores Oscilações das ações do Ibovespa - 05
			wRowOscilacoesAcoes.clear();
			wRowOscilacoesAcoes.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowOscilacoesAcoes.MergeColumn("INDOSC", DATATYPE.STRING, 1, null);
			wRowOscilacoesAcoes.MergeColumn("NOMRES", DATATYPE.STRING, 12, null);
			wRowOscilacoesAcoes.MergeColumn("ESPECI", DATATYPE.STRING, 10, null);
			wRowOscilacoesAcoes.MergeColumn("PREULT", DATATYPE.DOUBLE, 11, null);
			wRowOscilacoesAcoes.MergeColumn("TOTNEG", DATATYPE.INT, 5, null);
			wRowOscilacoesAcoes.MergeColumn("OSCPRE", DATATYPE.DOUBLE, 5, null);
			wRowOscilacoesAcoes.MergeColumn("CODMEG", DATATYPE.STRING, 12, null);
			wRowOscilacoesAcoes.MergeColumn("OSCILA", DATATYPE.DOUBLE, 7, null);
			wRowOscilacoesAcoes.MergeColumn("RESERVA", DATATYPE.STRING, 285, null);
			
			//As mais Negociadas no Mercado a Vista - 06
			wRowMaisNegociadasMercadoAVista.clear();
			wRowMaisNegociadasMercadoAVista.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowMaisNegociadasMercadoAVista.MergeColumn("NOMRES", DATATYPE.STRING, 12, null);
			wRowMaisNegociadasMercadoAVista.MergeColumn("ESPECI", DATATYPE.STRING, 10, null);
			wRowMaisNegociadasMercadoAVista.MergeColumn("QUATOT", DATATYPE.INT, 15, null);
			wRowMaisNegociadasMercadoAVista.MergeColumn("VOLTOT", DATATYPE.DOUBLE, 17, null);
			wRowMaisNegociadasMercadoAVista.MergeColumn("CODMEG", DATATYPE.STRING, 12, null);
			wRowMaisNegociadasMercadoAVista.MergeColumn("RESERVA", DATATYPE.STRING, 282, null);
			
			//As Mais Negociadas - 07
			wRowMaisNegociadas.clear();
			wRowMaisNegociadas.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowMaisNegociadas.MergeColumn("TPMERC", DATATYPE.INT, 3, null);
			wRowMaisNegociadas.MergeColumn("NOMERC", DATATYPE.STRING, 20, null);
			wRowMaisNegociadas.MergeColumn("NOMRES", DATATYPE.STRING, 12, null);
			wRowMaisNegociadas.MergeColumn("ESPECI", DATATYPE.STRING, 10, null);
			wRowMaisNegociadas.MergeColumn("INDOPC", DATATYPE.INT, 2, null);
			wRowMaisNegociadas.MergeColumn("NOMIND", DATATYPE.STRING, 15, null);
			wRowMaisNegociadas.MergeColumn("PREEXE", DATATYPE.DOUBLE, 11, null);
			wRowMaisNegociadas.MergeColumn("DATVEN", DATATYPE.STRING, 8, null);
			wRowMaisNegociadas.MergeColumn("PRAZOT", DATATYPE.INT, 3, null);
			wRowMaisNegociadas.MergeColumn("QUATOT", DATATYPE.INT, 15, null);
			wRowMaisNegociadas.MergeColumn("VOLTOT", DATATYPE.DOUBLE, 17, null);
			wRowMaisNegociadas.MergeColumn("PARTIC", DATATYPE.DOUBLE, 5, null);
			wRowMaisNegociadas.MergeColumn("CODMEG", DATATYPE.STRING, 12, null);
			wRowMaisNegociadas.MergeColumn("ICOATV", DATATYPE.INT, 3, null);
			wRowMaisNegociadas.MergeColumn("RESERVA", DATATYPE.STRING, 212, null);
			
			//Resumo Diário dos IOPV‟s - 08
			wRowIOPV.clear();
			wRowIOPV.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowIOPV.MergeColumn("IDENTI", DATATYPE.INT, 2, null);
			wRowIOPV.MergeColumn("SIGLA", DATATYPE.STRING, 4, null);
			wRowIOPV.MergeColumn("NOMRES", DATATYPE.STRING, 12, null);
			wRowIOPV.MergeColumn("NOMIND", DATATYPE.STRING, 30, null);
			wRowIOPV.MergeColumn("IDCABE", DATATYPE.DOUBLE, 7, null);
			wRowIOPV.MergeColumn("IDCMIN", DATATYPE.DOUBLE, 7, null);
			wRowIOPV.MergeColumn("IDCMAX", DATATYPE.DOUBLE, 7, null);
			wRowIOPV.MergeColumn("IDCMED", DATATYPE.DOUBLE, 7, null);
			wRowIOPV.MergeColumn("IDCFEC", DATATYPE.DOUBLE, 7, null);
			wRowIOPV.MergeColumn("SINEVO", DATATYPE.STRING, 1, null);
			wRowIOPV.MergeColumn("EVOIND", DATATYPE.DOUBLE, 5, null);
			wRowIOPV.MergeColumn("RESERVA", DATATYPE.STRING, 259, null);
			
			//BDR's Não Patrocinados – Valor de Referência - 09
			wRowBDRs.clear();
			wRowBDRs.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowBDRs.MergeColumn("CODNEG", DATATYPE.STRING, 12, null);
			wRowBDRs.MergeColumn("NOMRES", DATATYPE.STRING, 12, null);
			wRowBDRs.MergeColumn("ESPECI", DATATYPE.STRING, 10, null);
			wRowBDRs.MergeColumn("VALREF", DATATYPE.DOUBLE, 11, null);
			wRowBDRs.MergeColumn("RESERVA", DATATYPE.STRING, 303, null);
			
			//Trailer - 99
			wRowTrailer.clear();
			wRowTrailer.MergeColumn("TIPO", DATATYPE.INT, 2, null);
			wRowTrailer.MergeColumn("NOME", DATATYPE.STRING, 8, null);
			wRowTrailer.MergeColumn("CodigoOrigem", DATATYPE.INT, 8, null);
			wRowTrailer.MergeColumn("CodigoDestino", DATATYPE.INT, 4, null);
			wRowTrailer.MergeColumn("DataGeracaoArquivo", DATATYPE.STRING, 8, null);
			wRowTrailer.MergeColumn("TOTAL", DATATYPE.INT, 9, null);
			wRowTrailer.MergeColumn("Reserva", DATATYPE.STRING, 311, null);
		}
		
		@Override
		public void beforeRead(DBSDAOEvent pEvent) {
			wLineData = wDAOTxt.getLineData();
			String xTipo = DBSString.getSubString(wLineData, 1, 2);
			if (xTipo.equals("00")) {
				wDAOTxt.setFixedColumns(wRowHeader);
			} else if (xTipo.equals("01")) {
				wDAOTxt.setFixedColumns(wRowIndices);
			} else if (xTipo.equals("02")) {
				wDAOTxt.setFixedColumns(wRowNegociacoesPapel);
			} else if (xTipo.equals("03")) {
				wDAOTxt.setFixedColumns(wRowNegociacoesBDI);
			} else if (xTipo.equals("04")) {
				wDAOTxt.setFixedColumns(wRowOscilacoesMercado);
			} else if (xTipo.equals("05")) {
				wDAOTxt.setFixedColumns(wRowOscilacoesAcoes);
			} else if (xTipo.equals("06")) {
				wDAOTxt.setFixedColumns(wRowMaisNegociadasMercadoAVista);
			} else if (xTipo.equals("07")) {
				wDAOTxt.setFixedColumns(wRowMaisNegociadas);
			} else if (xTipo.equals("08")) {
				wDAOTxt.setFixedColumns(wRowIOPV);
			} else if (xTipo.equals("09")) {
				wDAOTxt.setFixedColumns(wRowBDRs);
			} else if (xTipo.equals("99")) {
				wDAOTxt.setFixedColumns(wRowTrailer);
			}
		}

		@Override
		public void afterRead(DBSDAOEvent pEvent) {
			Integer xTipo = wDAOTxt.getValue("TIPO");
			if (xTipo == 0) {
				wDataCot = DBSDate.toDateYYYYMMDD(DBSString.toString(wDAOTxt.getValue("DataPregao")));
				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
				System.out.println("NOME: " + wDAOTxt.getValue("NOME"));
				System.out.println("Data: " + DBSFormat.getFormattedDate(wDataCot));
			} else if (xTipo == 1) {
				String 		xProduto = wDAOTxt.getValue("NOMIND");
				String 		xComplemento = "*";
				String 		xCodigo1 = wDAOTxt.getValue("NOMIND");
				String		xSeuId = xCodigo1;
				Integer		xQuantidade = wDAOTxt.getValue("QTDIND");
				Double		xAbertura = DBSNumber.divide(DBSNumber.toDouble(wDAOTxt.getValue("IDCABE")), 100.0).doubleValue();
				Double		xMinima = DBSNumber.divide(DBSNumber.toDouble(wDAOTxt.getValue("IDCMIN")), 100.0).doubleValue();
				Double		xMaxima = DBSNumber.divide(DBSNumber.toDouble(wDAOTxt.getValue("IDCMAX")), 100.0).doubleValue();
				Double		xMedia = DBSNumber.divide(DBSNumber.toDouble(wDAOTxt.getValue("IDCMED")), 100.0).doubleValue();
				Double		xFechamento = DBSNumber.divide(DBSNumber.toDouble(wDAOTxt.getValue("IDCFEC")), 100.0).doubleValue();
				Double		xVolume = DBSNumber.divide(DBSNumber.toDouble(wDAOTxt.getValue("VOLIND")), 100.0).doubleValue();
				Integer		xNegocios = DBSNumber.toInteger(wDAOTxt.getValue("NNGIND"));
				
				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
				System.out.println("Identificação: " + wDAOTxt.getValue("IDENTI"));
				System.out.println("Produto: " + xProduto);
				System.out.println("Quantidade: " + xQuantidade);
				System.out.println("Abertura: " + xAbertura);
				System.out.println("Minima: " + xMinima);
				System.out.println("Maxima: " + xMaxima);
				System.out.println("Media: " + xMedia);
				System.out.println("Fechamento: " + xFechamento);
				System.out.println("Volume: " + xVolume);
				System.out.println("Negocios: " + xNegocios);
			} else if (xTipo.equals(2)) {
//				System.out.println(wDAOTxt.getLineData());
				String 		xCodBDI = wDAOTxt.getValue("CODBDI");
				String 		xProduto = DBSString.toString(wDAOTxt.getValue("NOMRES")).trim();
	            String 		xComplemento = DBSString.toString(wDAOTxt.getValue("ESPECI")).trim();
	            Integer		xLote = DBSNumber.toInteger(wDAOTxt.getValue("FATCOT"));
	            String 		xIsin = DBSString.toString(wDAOTxt.getValue("CODISI")).trim();
	            String		xCodigo1 = wDAOTxt.getValue("CODNEG");
	            String		xCodigo2 = DBSString.getSubString(DBSString.toString(wDAOTxt.getValue("CODNEG")), 4, 8);
            	Date		xVencimento = DBSDate.toDateYYYYMMDD(DBSString.toString(wDAOTxt.getValue("DATVEN")));
            	Double		xPu = DBSNumber.divide(wDAOTxt.getValue("PREEXE"), 100.0).doubleValue();
            	Double		xAbertura = DBSNumber.divide(wDAOTxt.getValue("PREABE"), 100.0).doubleValue();
            	Double		xMedia = DBSNumber.divide(wDAOTxt.getValue("PREMED"), 100.0).doubleValue();
            	Double		xFechamento = DBSNumber.divide(wDAOTxt.getValue("PREULT"), 100.0).doubleValue();
            	Double		xMaxima = DBSNumber.divide(wDAOTxt.getValue("PREMAX"), 100.0).doubleValue();
            	Double		xMinima = DBSNumber.divide(wDAOTxt.getValue("PREMIN"), 100.0).doubleValue();
            	Double		xVolume = DBSNumber.divide(wDAOTxt.getValue("VOLTOT"), 100.0).doubleValue();
            	Integer		xNegocios = DBSNumber.toInteger(wDAOTxt.getValue("TOTNEG"));
            	Integer		xQuantidade = DBSNumber.toInteger(wDAOTxt.getValue("QUATOT"));
				
				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
//				System.out.println("Código BDI: " + xCodBDI);
//				System.out.println("Descrição BDI: " + wDAOTxt.getValue("DESBDI"));
				System.out.println("Produto: " + xProduto + " " + xComplemento);
//				System.out.println("Complemento: " + xComplemento);
//				System.out.println("Lote: " + xLote);
				System.out.println("Codigo Negociação: " + xCodigo1);
				System.out.println("Isin: " + xIsin);
//				System.out.println("Codigo1: " + xCodigo1);
//				System.out.println("Codigo2: " + xCodigo2);
				System.out.println("Vencimento: " + DBSFormat.getFormattedDate(xVencimento));
				System.out.println("Pu: " + xPu);
				System.out.println("Abertura: " + xAbertura);
				System.out.println("Media: " + xMedia);
				System.out.println("Fechamento: " + xFechamento);
				System.out.println("Maxima: " + xMaxima);
				System.out.println("Minima: " + xMinima);
				System.out.println("Volume: " + xVolume);
				System.out.println("Negocios: " + xNegocios);
				System.out.println("Quantidade: " + xQuantidade);
			} else if (xTipo.equals(3)) {
//				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
//				System.out.println("Código BDI: " + wDAOTxt.getValue("CODBDI"));
//				System.out.println("Descrição BDI: " + wDAOTxt.getValue("DESBDI"));
			} else if (xTipo.equals(4)) {
//				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
//				System.out.println("Oscilação: " + wDAOTxt.getValue("INDOSC"));
//				System.out.println("Nome: " + wDAOTxt.getValue("NOMRES"));
			} else if (xTipo.equals(5)) {
//				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
//				System.out.println("Oscilação: " + wDAOTxt.getValue("INDOSC"));
//				System.out.println("NOME: " + wDAOTxt.getValue("NOMRES"));
			} else if (xTipo.equals(6)) {
//				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
//				System.out.println("NOME: " + wDAOTxt.getValue("NOMRES"));
			} else if (xTipo.equals(7)) {
//				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
//				System.out.println("Tipo Mercado: " + wDAOTxt.getValue("TPMERC"));
//				System.out.println("Nome Mercado: " + wDAOTxt.getValue("NOMERC"));
//				System.out.println("Nome: " + wDAOTxt.getValue("NOMRES"));
			} else if (xTipo.equals(8)) {
//				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
//				System.out.println("Identificação: " + wDAOTxt.getValue("IDENTI"));
//				System.out.println("Sigla: " + wDAOTxt.getValue("SIGLA"));
//				System.out.println("NOME: " + wDAOTxt.getValue("NOMRES"));
			} else if (xTipo.equals(9)) {
//				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
//				System.out.println("Código de Negociação: " + wDAOTxt.getValue("CODNEG"));
//				System.out.println("Nome: " + wDAOTxt.getValue("NOMRES"));
			} else if (xTipo.equals(99)) {
				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
				System.out.println("Nome: " + wDAOTxt.getValue("NOME"));
				System.out.println("Total: " + wDAOTxt.getValue("TOTAL"));
			}
		}

		@Override
		public void beforeClose(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterClose(DBSDAOEvent pEvent) {
			System.out.println("ProcessingEnd");
			
		}

		@Override
		public void beforeInsert(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterInsert(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeUpdate(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterUpdate(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeMerge(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterMerge(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeDelete(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterDelete(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeMove(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterMove(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterOpen(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class xLayoutSELIC implements IDBSDAOEventsListener{
		
		DBSDAOTxt<Object> 	wDAOTxt;
		Date				wDataCot;
		String 				wLineData;
		DBSRow 				wRowHeader = new DBSRow();
		DBSRow 				wRowRegistro = new DBSRow();
		DBSRow 				wRowTrailer = new DBSRow();
		
		xLayoutSELIC(DBSDAOTxt<Object> pDAO){
			wDAOTxt = pDAO;
		}

		@Override
		public void beforeOpen(DBSDAOEvent pEvent) {
			System.out.println("ProcessingBegin: ");

			//Header - 00
			wRowHeader.clear();
			wRowHeader.MergeColumn("TIPO", DATATYPE.INT, 1, null);
			wRowHeader.MergeColumn("NOME", DATATYPE.STRING, 10, null);
			wRowHeader.MergeColumn("DATAMOVIMENTO", DATATYPE.STRING, 8, null);
			wRowHeader.MergeColumn("DATAGERACAO", DATATYPE.STRING, 8, null);
			wRowHeader.MergeColumn("HORAGERACAO", DATATYPE.TIME, 6, null);
			wRowHeader.MergeColumn("VERSAO", DATATYPE.STRING, 6, null);
			
			//Registros - 01
			wRowRegistro.clear();
			wRowRegistro.MergeColumn("TIPO", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("SEPARADOR1", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("CODIGO", DATATYPE.INT, 6, null);
			wRowRegistro.MergeColumn("SEPARADOR2", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("VENCIMENTO", DATATYPE.STRING, 8, null);
			wRowRegistro.MergeColumn("SEPARADOR3", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("ISIN", DATATYPE.STRING, 12, null);
			wRowRegistro.MergeColumn("SEPARADOR", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("SIGLA", DATATYPE.STRING, 10, null);
			wRowRegistro.MergeColumn("SEPARADOR4", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("NEGOCIACAO", DATATYPE.STRING, 1, null);
			wRowRegistro.MergeColumn("SEPARADOR5", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("BLOQUEIO", DATATYPE.STRING, 1, null);
			wRowRegistro.MergeColumn("SEPARADOR6", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PRIMEIRAEMISSAO", DATATYPE.STRING, 8, null);
			wRowRegistro.MergeColumn("SEPARADOR7", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("POSICAOCUSTODIA", DATATYPE.INT, 13, null);
			wRowRegistro.MergeColumn("SEPARADOR8", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("TIPORENDIMENTO", DATATYPE.STRING, 3, null);
			wRowRegistro.MergeColumn("SEPARADOR9", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("RESGATE", DATATYPE.DOUBLE, 16, null); //8 Decimais
			wRowRegistro.MergeColumn("SEPARADOR10", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("DATABASE", DATATYPE.STRING, 8, null);
			wRowRegistro.MergeColumn("SEPARADOR11", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("VALORDATABASE", DATATYPE.DOUBLE, 16, null); //8 Decimais
			wRowRegistro.MergeColumn("SEPARADOR12", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("ATUALIZARVALORBASE", DATATYPE.STRING, 10, null);
			wRowRegistro.MergeColumn("SEPARADOR13", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("CUPOMJUROS", DATATYPE.STRING, 1, null);
			wRowRegistro.MergeColumn("SEPARADOR14", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PERIODICIDADEJUROS", DATATYPE.STRING, 3, null);
			wRowRegistro.MergeColumn("SEPARADOR15", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("TAXAJUROS", DATATYPE.DOUBLE, 5, null); //2 Decimais
			wRowRegistro.MergeColumn("SEPARADOR16", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("CAPITALIZACAOJUROS", DATATYPE.STRING, 3, null);
			wRowRegistro.MergeColumn("SEPARADOR17", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("DESMEMBRAMENTO", DATATYPE.STRING, 1, null);
			wRowRegistro.MergeColumn("SEPARADOR18", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PUJUROS", DATATYPE.DOUBLE, 16, null); //8 Decimais
			wRowRegistro.MergeColumn("SEPARADOR19", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("AMORTIZACAO", DATATYPE.STRING, 1, null);
			wRowRegistro.MergeColumn("SEPARADOR20", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PERIODICIDADEAMORTIZACAO", DATATYPE.STRING, 3, null);
			wRowRegistro.MergeColumn("SEPARADOR21", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PERCENTUALAMORTIZACAO", DATATYPE.DOUBLE, 10, null); //8 Decimais
			wRowRegistro.MergeColumn("SEPARADOR22", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PARCELAS", DATATYPE.INT, 3, null);
			wRowRegistro.MergeColumn("SEPARADOR23", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PUAMORTIZACAO", DATATYPE.DOUBLE, 16, null); //8 Decimais
			wRowRegistro.MergeColumn("SEPARADOR24", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PULASTRO", DATATYPE.DOUBLE, 16, null); //8 Decimais
			wRowRegistro.MergeColumn("SEPARADOR25", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("VALORNOMINALATUALIZADO", DATATYPE.DOUBLE, 16, null); //8 Decimais
			wRowRegistro.MergeColumn("SEPARADOR26", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PURETORNORESGATE", DATATYPE.DOUBLE, 16, null); //8 Decimais
			wRowRegistro.MergeColumn("SEPARADOR27", DATATYPE.INT, 1, null);
			wRowRegistro.MergeColumn("PURESGATE", DATATYPE.DOUBLE, 16, null); //8 Decimais
			
			//Trailer - 9
			wRowTrailer.clear();
			wRowTrailer.MergeColumn("TIPO", DATATYPE.INT, 1, null);
			wRowTrailer.MergeColumn("SEPARADOR", DATATYPE.INT, 1, null);
			wRowTrailer.MergeColumn("TOTAL", DATATYPE.INT, 7, null);
		}
		
		@Override
		public void beforeRead(DBSDAOEvent pEvent) {
			wLineData = wDAOTxt.getLineData();
			String xTipo = DBSString.getSubString(wLineData, 1, 1);
			if (xTipo.equals("0")) {
				wDAOTxt.setFixedColumns(wRowHeader);
			} else if (xTipo.equals("1")) {
				wDAOTxt.setFixedColumns(wRowRegistro);
			} else if (xTipo.equals("9")) {
				wDAOTxt.setFixedColumns(wRowTrailer);
			}
		}

		@Override
		public void afterRead(DBSDAOEvent pEvent) {
			Integer xTipo = wDAOTxt.getValue("TIPO");
			if (xTipo == 0) {
				wDataCot = DBSDate.toDateYYYYMMDD(DBSString.toString(wDAOTxt.getValue("DATAMOVIMENTO")));
				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
				System.out.println("NOME: " + wDAOTxt.getValue("NOME"));
				System.out.println("Data: " + DBSFormat.getFormattedDate(wDataCot));
			} else if (xTipo.equals(1)) {
				Date xVencimento = DBSDate.toDateYYYYMMDD(DBSString.toString(wDAOTxt.getValue("VENCIMENTO")));
				Boolean xNegociacao = DBSBoolean.toBoolean(DBSString.toString(wDAOTxt.getValue("NEGOCIACAO")));
				Double xResgate = DBSNumber.divide(wDAOTxt.getValue("RESGATE"), 100000000.0).doubleValue();
				Double xPuLastro = DBSNumber.divide(wDAOTxt.getValue("PULASTRO"), 100000000.0).doubleValue();
				Double xTaxaJuros = DBSNumber.divide(wDAOTxt.getValue("TAXAJUROS"), 100.0).doubleValue();
				
				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
				System.out.println("Código: " + wDAOTxt.getValue("CODIGO"));
				System.out.println("ISIN: " + wDAOTxt.getValue("ISIN"));
				System.out.println("Ativo: " + wDAOTxt.getValue("SIGLA"));
				System.out.println("Vencimento: " + DBSFormat.getFormattedDate(xVencimento));
				System.out.println("Possibilidade de Negociação: " + xNegociacao);
				System.out.println("Valor de Resgate: " + xResgate);
				System.out.println("Taxa de Juros: " + xTaxaJuros);
				System.out.println("Pu Lastro: " + xPuLastro);
			} else if (xTipo.equals(9)) {
				System.out.println("Tipo: " + wDAOTxt.getValue("TIPO"));
				System.out.println("Total: " + wDAOTxt.getValue("TOTAL"));
			}
		}

		@Override
		public void beforeClose(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterClose(DBSDAOEvent pEvent) {
			System.out.println("ProcessingEnd");
			
		}

		@Override
		public void beforeInsert(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterInsert(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeUpdate(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterUpdate(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeMerge(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterMerge(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeDelete(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterDelete(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeMove(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterMove(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterOpen(DBSDAOEvent pEvent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
