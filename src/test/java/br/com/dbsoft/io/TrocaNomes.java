package br.com.dbsoft.io;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSIO;

import br.com.dbsoft.util.DBSString;

public class TrocaNomes {
	String url="jdbc:mysql://localhost:3306/ifeed?zeroDateTimeBehavior=convertToNull";
//	String url="jdbc:oracle:thin:@192.168.0.20:1521:xe";
	String user="dbsoft";
	String password="dbs0ft";
	Connection wConexao;

	@Before
	public void setup() throws Exception {
		Class.forName(DBSSDK.JDBC_DRIVER.MYSQL);
		//Class.forName(DBSSDK.JDBC_DRIVER.ORACLE);
		wConexao = DriverManager.getConnection(url, user, password);
		wConexao.setAutoCommit(false);
	}
	
	@After
	public void tearDown() throws Exception {
		wConexao.close();
	}
	
	//@Test
	public void readLoop(){
		try {
			@SuppressWarnings("rawtypes")
			DBSDAO xDAO = new DBSDAO(wConexao, "COR_PESSOA");
			xDAO.open("Select * from Cor_Pessoa");
			xDAO.moveBeforeFirstRow();
			while(xDAO.moveNextRow()){
				System.out.println(xDAO.getValue("Pessoa"));
				xDAO.setValue("Pessoa", DBSString.toProper((String) xDAO.getValue("Pessoa")));
//				if (xDAO.getValue("Complemento")!=null){
//					xDAO.setValue("Complemento", DBSString.toProper((String) xDAO.getValue("Complemento")));
//				}
				if (xDAO.getValue("Endereco")!=null){
					xDAO.setValue("Endereco", DBSString.toProper((String) xDAO.getValue("Endereco")));
				}
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

	@Test
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
