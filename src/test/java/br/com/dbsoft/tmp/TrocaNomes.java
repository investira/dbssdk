package br.com.dbsoft.tmp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

import org.junit.After;
import org.junit.Before;


import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.io.DBSDAO;
import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSIO;
import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSString;

public class TrocaNomes {
//	String url = "jdbc:mysql://ifeed.com.br:3306/ifeed?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url = "jdbc:mysql://ifeed.com.br:3306/dbsfnd?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url="jdbc:mysql://localhost:3306/dbsfnd?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
	String url="jdbc:mysql://localhost:3306/ifeed?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url="jdbc:mysql://192.168.0.106:3306/dbsfnd?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url="jdbc:mysql://192.168.0.106:3306/ifeed?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url="jdbc:oracle:thin:@192.168.0.20:1521:xe";
	String user="usuario";
	String password="senha";
	Connection wConexao;

	@Before
	public void setup() throws Exception {
		Class.forName(DBSSDK.JDBC_DRIVER.MYSQL);
		Class.forName(DBSSDK.JDBC_DRIVER.ORACLE);
		wConexao = DriverManager.getConnection(url, user, password);
		wConexao.setAutoCommit(false);
	}
	
	@After
	public void tearDown() throws Exception {
		wConexao.close();
	}

//	@Test
	public void testaUpdate() throws DBSIOException{

		@SuppressWarnings("rawtypes")
		DBSDAO xDAO = new DBSDAO(wConexao, "SEG_GRUPO");
		xDAO.open("SELECT * FROM SEG_GRUPO WHERE GRUPO_ID=2");
//		xDAO.setValue("GRUPO_ID", 2);
//		xDAO.setValue("GRUPO", DBSDate.getNowTime().toString());
		xDAO.setValue("GRUPO_ID", null);
		xDAO.setValue("GRUPO", "ttt");
		xDAO.setExecuteOnlyChangedValues(false);
		xDAO.executeInsert();
		DBSIO.endTrans(wConexao, true);
	}
		
//	@Test
	public void trocaAtivoTipo(){
		try {
			@SuppressWarnings("rawtypes")
			DBSDAO xDAO = new DBSDAO(wConexao, "GR_COTACAO");
			Double 		xDouble = DBSNumber.toDouble(200D);
			BigDecimal 	xBigDecimal = DBSNumber.toBigDecimal("1000");
			Integer 	xInteger = DBSNumber.toInteger("1");
			Date 		xData = DBSDate.toDate(19,9,2014);
			xDAO.open("Select * from GR_COTACAO WHERE DATA = " + DBSIO.toSQLDate(wConexao, xData));
			if (xDAO.getRowsCount()==1){
				xDAO.moveFirstRow();
//				xDouble = xDAO.getValue("ABERTURA");
//				xDouble = xDAO.<Double>getValueX("ABERTURA");
				System.out.println(xDouble);
			}
//				xDAO.moveBeforeFirstRow();
//				while(xDAO.moveNextRow()){
//					System.out.println(xDAO.getValue("DATA"));
//				}
//			}else{
				xDAO.setValue("DATA", xData);
				xDAO.setValue("POSID_ID", 2);

				xDAO.setValue("ABERTURA", xInteger);
				xDAO.setValue("media", xDouble);
				xDAO.setValue("FECHAMENTO", xBigDecimal);
				xDAO.setValue("MINIMA", null);
				xDAO.setValue("MAXIMA", "6");
				xDAO.setValue("AJUSTE", 7);
				xDAO.setValue("DELTA", 8D);
				xDAO.setValue("VOLUME", "123.456");
				xDAO.setValue("NEGOCIOS", "78,9");
				xDAO.setValue("REPETIDA", true);
//				xDAO.executeInsert();
				xDAO.executeMerge();
				
//			}
			DBSIO.endTrans(wConexao, true);
			xDAO.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	
//	@Test
	public void trocaProdutoTipo(){
		try {
			@SuppressWarnings("rawtypes")
			DBSDAO xDAO = new DBSDAO(wConexao, "GR_PRODUTO_TIPO");
			xDAO.open("Select * from GR_PRODUTO_TIPO");
			xDAO.moveBeforeFirstRow();
			while(xDAO.moveNextRow()){
				System.out.println(xDAO.getValue("Produto_Tipo"));
				xDAO.setValue("Produto_Tipo", DBSString.corretorOrtografico(DBSString.toProper((String) xDAO.getValue("Produto_Tipo"))));
//				if (xDAO.getValue("Complemento")!=null){
//					xDAO.setValue("Complemento", DBSString.toProper((String) xDAO.getValue("Complemento")));
//				}
//				if (xDAO.getValue("Endereco")!=null){
//					xDAO.setValue("Endereco", DBSString.corretorOrtografico(DBSString.toProper((String) xDAO.getValue("Endereco"))));
//				}
				xDAO.executeUpdate();
				//System.out.println(xDAO.getValue("Cidade"));
			}
			DBSIO.endTrans(wConexao, true);
			xDAO.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	//@Test
	public void readLoop2(){
		try {
			@SuppressWarnings("rawtypes")
			DBSDAO xDAO = new DBSDAO(wConexao);
			xDAO.open("Select * from Cor_Estado");
			xDAO.moveBeforeFirstRow();
			while(xDAO.moveNextRow()){
				System.out.println(xDAO.getValue("Estado"));
				//xDAO.setValue("Estado", DBSString.toProper((String) xDAO.getValue("Estado")));
				//xDAO.executeUpdate();
				//System.out.println(xDAO.getValue("Cidade"));
			}
			DBSIO.endTrans(wConexao, true);
			xDAO.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	//@Test
	public void troca_nomes_centrus() throws DBSIOException {
		DBSDAO<Object> xDAO = new DBSDAO<Object>(wConexao, "PESSOA");
		System.out.println(Runtime.getRuntime().maxMemory() + "====================================") ;   
		xDAO.open("Select * from pessoa order by cd_centrus");
		String xNomeProprioAnterior = "CORRETORA";
		String xSobrenomeAnterior = "";
		String xSiglaAnterior = "CORRETORA";
		String xNomeProprioAtual = "CORRETORA";
		String xSobrenomeAtual="";
		String xSiglaAtual="";
		String xNovoNome;
		String xNovaSigla;
		xDAO.moveBeforeFirstRow();
		while(xDAO.moveNextRow()){
			if (!DBSString.toArray(xDAO.getValue("no_pessoa").toString(), " ").get(0).equals(xNomeProprioAtual)){
				xNomeProprioAnterior = xNomeProprioAtual;
				xSobrenomeAnterior = xSobrenomeAtual;
				xSiglaAnterior = xSiglaAtual;
			}
			xNomeProprioAtual = DBSString.toArray(xDAO.getValue("no_pessoa").toString(), " ").get(0);
			if (DBSString.toArray(xDAO.getValue("no_pessoa").toString(), " ").size()>1){
				xSobrenomeAtual = DBSString.toArray(xDAO.getValue("no_pessoa").toString(), " ").get(1);
			}
			xNovoNome = DBSString.changeStr(xDAO.getValue("no_pessoa").toString(), xNomeProprioAtual, xNomeProprioAnterior);
			xNovoNome = DBSString.changeStr(xNovoNome, xSobrenomeAtual, xSobrenomeAnterior);
			System.out.println("OLD:" + xDAO.getValue("no_pessoa"));
			xDAO.setValue("no_pessoa",  DBSString.getSubString(xNovoNome, 1, 50));
			System.out.println("NEW:" + xDAO.getValue("no_pessoa"));
			if(xDAO.getValue("sg_pessoa")!=null){
				xSiglaAtual = DBSString.toArray(xDAO.getValue("sg_pessoa").toString(), " ").get(0);
				xNovaSigla = DBSString.changeStr(xDAO.getValue("sg_pessoa").toString(), xSiglaAtual, xSiglaAnterior);
				xDAO.setValue("sg_pessoa", DBSString.getSubString(xNovaSigla,1,15));
			}
			xDAO.executeUpdate();
		}
		//assertTrue("TESTE ESPERAVA NOTNULL",xDAO.executeMerge()>0);
		DBSIO.endTrans(wConexao, true);
		xDAO.close();
		System.out.println("FIM====================================");
	}	

//	@Test
	public void troca_nomes_centrus_usuario() throws DBSIOException {
		DBSDAO<Object> xDAO = new DBSDAO<Object>(wConexao, "AC_USUARIO");
		System.out.println(Runtime.getRuntime().maxMemory() + "====================================") ;   
		xDAO.open("Select * from AC_USUARIO");
		String xNomeDaColuna = "COMPLETO";
		String xNomeProprioAnterior = "LUIZ";
		String xSobrenomeAnterior = "";
		String xNomeProprioAtual = "CORRETORA";
		String xSobrenomeAtual="";
		String xNovoNome;
		xDAO.moveBeforeFirstRow();
		while(xDAO.moveNextRow()){
			if (!DBSString.toArray(xDAO.getValue(xNomeDaColuna).toString(), " ").get(0).equals(xNomeProprioAtual)){
				xNomeProprioAnterior = xNomeProprioAtual;
				xSobrenomeAnterior = xSobrenomeAtual;
			}
			xNomeProprioAtual = DBSString.toArray(xDAO.getValue(xNomeDaColuna).toString(), " ").get(0);
			if (DBSString.toArray(xDAO.getValue(xNomeDaColuna).toString(), " ").size()>1){
				xSobrenomeAtual = DBSString.toArray(xDAO.getValue(xNomeDaColuna).toString(), " ").get(1);
			}
			xNovoNome = DBSString.changeStr(xDAO.getValue(xNomeDaColuna).toString(), xNomeProprioAtual, xNomeProprioAnterior);
			xNovoNome = DBSString.changeStr(xNovoNome, xSobrenomeAtual, xSobrenomeAnterior);
			System.out.println("OLD:" + xDAO.getValue(xNomeDaColuna));
			xDAO.setValue(xNomeDaColuna,  DBSString.getSubString(xNovoNome, 1, 50));
			System.out.println("NEW:" + xDAO.getValue(xNomeDaColuna));
			xDAO.executeUpdate();
		}
		//assertTrue("TESTE ESPERAVA NOTNULL",xDAO.executeMerge()>0);
		DBSIO.endTrans(wConexao, true);
		xDAO.close();
		System.out.println("FIM====================================");
	}	
}
