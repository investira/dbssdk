package br.com.dbsoft.tmp;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormatSymbols;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class lixoIO  {

	//É necessário adicionar as classes que serão utilizadas com "addClass" conforme exemplo abaixo 
	@Deployment
    public static WebArchive createDeployment() {
		
//		MavenDependencyResolver xResolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml").goOffline();
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
			.addPackage("br.com.dbsoft.event")
			.addPackage("br.com.dbsoft.annotation")
			.addPackage("br.com.dbsoft.core")
			.addPackage("br.com.dbsoft.error")
			.addPackage("br.com.dbsoft.factory")
			.addPackage("br.com.dbsoft.io")
			.addPackage("br.com.dbsoft.util")
			.addPackage("br.com.dbsoft.dao")
//			.addAsLibraries(xResolver.artifact("joda-time:joda-time:2.1").resolveAsFiles()) // groupId:artifactId:version conforme definido no pom
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return war;
    }


	@Resource(mappedName="junit")
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
	
	@Test
	public void teste(){
		java.text.DecimalFormatSymbols xD = new DecimalFormatSymbols();
		char xDecimalPoint = xD.getDecimalSeparator();
		System.out.println("ENTROU:" + xDecimalPoint);
		//wCn.getMetaData().
	}	
	
}
