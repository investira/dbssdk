package br.com.dbsoft.io;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSIO;

//O SERVIDOR(JBOSS) PRECISA ESTÁ RODANDO 
@RunWith(Arquillian.class)
public class TstDAO_DBSOFT {
	
	//É necessário adicionar as classes que serão utilizadas com "addClass" conforme exemplo abaixo 
	@Deployment
    public static WebArchive createDeployment() {
		
		MavenDependencyResolver xResolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml").goOffline();
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
			.addPackage("br.com.dbsoft.event")
			.addPackage("br.com.dbsoft.annotation")
			.addPackage("br.com.dbsoft.core")
			.addPackage("br.com.dbsoft.error")
			.addPackage("br.com.dbsoft.factory")
			.addPackage("br.com.dbsoft.io")
			.addPackage("br.com.dbsoft.util")
			.addPackage("br.com.dbsoft.dao")
			.addPackage("br.com.dbsoft.message")
			.addPackage("org.jboss.arquillian.protocol.jmx.Serializer")
//			.addPackage("org.joda.time")
			.addAsLibraries(xResolver.artifact("joda-time:joda-time:2.1").resolveAsFiles()) // groupId:artifactId:version conforme definido no pom
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return war;
    }


	//@Resource(mappedName="ifeed")
//	@Resource(mappedName="dbsfnd")
	@Resource(mappedName="dbsoft")
	private DataSource wDS;

	Connection wCn;
	
	@Before
	public void openCn(){
		try {
			wCn = wDS.getConnection();
			wCn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void closeCn(){
		try {
			wCn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//Recupera nome do método sendo executado 
	public static String getMethodName()
	{
	  final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	  return ste[2].getMethodName(); //Thank you Tom Tresansky
	}

	@Test
	public void readLoop(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			@SuppressWarnings("rawtypes")
			DBSDAO xDAO = new DBSDAO(wCn, "CA_POSDIA");
			xDAO.open("Select PU, QUANTIDADE, FINANCEIRO from CA_POSDIA where  CARTEIRA_ID = 2 AND POSID_ID = 23023 AND DATA=" + DBSIO.toSQLDate(wCn, "31/10/2008"));
			//xDAO.open("Select * from IFD_DOWNLOAD");
			xDAO.moveBeforeFirstRow();
			while(xDAO.moveNextRow()){
				//System.out.println("DOWNLOAD_ID:" + xDAO.getValue("DOWNLOAD_id"));
				System.out.println("PU:" + xDAO.getValue("PU"));
				System.out.println("FINANCEI:" + xDAO.getValue("FINANCEIRO"));
			}
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void copy(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			Date xDataHoje = DBSDate.toDate(14, 1, 2013);
			Date xDataAmanha = DBSDate.toDate(15, 1, 2013);
			DBSDAO<Object> xDAO = new DBSDAO<Object>(wCn,"CA_PL");
			xDAO.setExecuteOnlyChangedValues(false);
			String xSQL;
			xSQL = "Select * " +
					 "From CA_PL " + 
					"Where Data=" + DBSIO.toSQLDate(wCn, xDataHoje);
		    if (xDAO.open(xSQL)){
		    	xDAO.moveBeforeFirstRow();
		    	while (xDAO.moveNextRow()){
		    		xDAO.setValue("DATA", xDataAmanha);
		    		xDAO.setValue("QUANTIDADE_ABR", xDAO.getValue("QUANTIDADE"));
		    		xDAO.setValue("PL_ABR", xDAO.getValue("PL"));
		    		xDAO.executeMerge();
		    	}
		    	DBSIO.endTrans(wCn, true);
		    	xDAO.close();
		    }
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void getReaproveitamentoDeDAO(){
		try {
			System.out.println("Inicio: ==================================");
			DBSDAO<Object> xDAO = new DBSDAO<Object>(wCn);
			String xSQL = "select sum(Quantidade) sQuantidade " +
							"from ca_movimento " + 
						   "where posid_id = 48796 ";
			xDAO.open(xSQL);
				System.out.println(xDAO.getValue("sQUANTIDADE"));
			xDAO.close();

			xSQL = "select sum(Quantidade) sQuantidade " +
					 "from ca_movimento " + 
				    "where posid_id = 52891 ";
			xDAO.open(xSQL);
				System.out.println(xDAO.getValue("sQUANTIDADE"));
			xDAO.close();
			System.out.println("Fim:   ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

}
