package br.com.dbsoft.tmp;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.io.DBSDAO;
import br.com.dbsoft.util.DBSIO;



public class lixoIO  {

//	String url = "jdbc:mysql://ifeed.com.br:3306/ifeed?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url = "jdbc:mysql://ifeed.com.br:3306/dbsfnd?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url="jdbc:mysql://localhost:3306/dbsfnd?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url="jdbc:mysql://localhost:3306/ifeed?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url="jdbc:mysql://192.168.0.106:3306/dbsfnd?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url="jdbc:mysql://192.168.0.106:3306/ifeed?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
//	String url="jdbc:oracle:thin:@192.168.0.20:1521:xe";
	String url="jdbc:mysql://localhost:3306/junit?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
	String user="teste";
	String password="teste";
	Connection wConexao;
	Connection wConexao2;

	@Before
	public void setup() throws Exception {
		Class.forName(DBSSDK.JDBC_DRIVER.MYSQL);
		Class.forName(DBSSDK.JDBC_DRIVER.ORACLE);
		wConexao = DriverManager.getConnection(url, user, password);
		wConexao.setAutoCommit(false);
		wConexao2 = DriverManager.getConnection(url, user, password);
		wConexao2.setAutoCommit(false);
	}
	
	@After
	public void tearDown() throws Exception {
		wConexao.close();
	}	
	
	@Test
	public void DeadLock() throws DBSIOException{
		DBSIO.beginTrans(wConexao);
		DBSIO.beginTrans(wConexao2);
		String xSQL = "Select * From Crud where crud_id = 1 ";
		DBSDAO<Object> xDAO = new DBSDAO<Object>(wConexao, "CRUD");
		DBSDAO<Object> xDAO2 = new DBSDAO<Object>(wConexao2, "CRUD");
		if (xDAO.open(xSQL)){
			xDAO.setValue("CRUD", "CRUD 23");
			xDAO.executeUpdate();
			xDAO.close();
			if (xDAO2.open(xSQL)){
				xDAO2.setValue("CRUD", "CRUD 24");
				xDAO2.executeUpdate();
				xDAO2.close();
			}
			xDAO.close();
		}
		DBSIO.endTrans(wConexao, true);
		DBSIO.endTrans(wConexao2, true);
		
	}
	

	
}
