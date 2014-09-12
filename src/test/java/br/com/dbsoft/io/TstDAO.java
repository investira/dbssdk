package br.com.dbsoft.io;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.dbsoft.io.DBSDAO.COMMAND;
import br.com.dbsoft.util.DBSIO;
import br.com.dbsoft.util.DBSString;

//O SERVIDOR(JBOSS) PRECISA ESTÁ RODANDO 
@RunWith(Arquillian.class)
public class TstDAO {
	
	//É necessário adicionar as classes que serão utilizadas com "addClass" conforme exemplo abaixo 
	@Deployment
    public static WebArchive createDeployment() {
		
		MavenDependencyResolver xResolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml").goOffline();
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
			.addPackage("br.com.dbsoft.event")
			.addPackage("br.com.dbsoft.annotation")
			.addPackage("br.com.dbsoft.message")
			.addPackage("br.com.dbsoft.core")
			.addPackage("br.com.dbsoft.error")
			.addPackage("br.com.dbsoft.factory")
			.addPackage("br.com.dbsoft.io")
			.addPackage("br.com.dbsoft.util")
			.addPackage("br.com.dbsoft.dao")
			.addAsLibraries(xResolver.artifact("joda-time:joda-time:2.1").resolveAsFiles()) // groupId:artifactId:version conforme definido no pom
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return war;
    }


	@Resource(mappedName="junit")
	//@Resource(mappedName="ifeed")
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
	
//
//	//@Test
//	public void getReadDA(){
//		try {
//			System.out.println("Inicio: " + getMethodName() + " ==================================");
//			DBSDAO<DownloadAgendaModel> xDAO = new DBSDAO<DownloadAgendaModel>(DownloadAgendaModel.class, wCn);
//			xDAO.open("Select * From ifd_Download_Agenda DA, ifd_Download D where DA.Download_ID = D.Download_ID ");
//			System.out.println(xDAO.getDataModel().getDownload().getDownload());
////			List<DownloadAgendaModel> xL = new ArrayList<DownloadAgendaModel>();
////			xL = xDAO.getListDataModel();
//			xDAO.close();
//			System.out.println("Fim:   " + getMethodName() + " ==================================");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	//@Test
	public void getBigQuery(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			DBSDAO<TstDAOModel> xDAO = new DBSDAO<TstDAOModel>(TstDAOModel.class, wCn);
			String xSQL = "Select 'Empresa Aberta' as Tipo, Pessoa as Descricao, Pessoa_ID ID From Cor_Pessoa where pessoa like '%a%'  and tipo = 1   " +
					"UNION Select 'Fundo de Investimento IN.CVM 409' as Tipo, Pessoa as Descricao, Pessoa_ID ID From Cor_Pessoa where pessoa like '%a%'  and tipo = 2 " + 
					"UNION Select 'Índicador Econômico' as Tipo, indicador as Descricao, Indicador_ID ID From atv_indicador where indicador like '%a%'  and tipo = 1  " +
					"UNION Select 'Juros' as Tipo, indicador as Descricao, Indicador_ID ID From atv_indicador where indicador like '%a%'  and tipo = 2  " +
					"UNION Select 'Câmbio' as Tipo, indicador as Descricao, Indicador_ID ID From atv_indicador where indicador like '%a%'  and tipo = 3  " +
					"UNION Select Ativo_Tipo as Tipo, Ativo as Descricao, Ativo_ID ID From atv_ativo a, atv_ativo_base ab, atv_ativo_tipo at where (a.ativo like '%a%'  or ab.ativo_base like '%a%'  or at.ativo_tipo like '%a%') and a.ativo_base_id = ab.ativo_base_id and ab.ativo_tipo_id = at.ativo_tipo_id  " +
					" order by tipo, descricao ";
			xDAO.open(xSQL);
			List<TstDAOModel> xL = new ArrayList<TstDAOModel>();
			xL = xDAO.getListDataModel();
//			Assert.assertEquals("TESTE 2", xL.get(1).getCRUD());
//			Assert.assertEquals("TESTE 5", xL.get(4).getCRUD());
//			for (int xX=0;xX<xL.size();xX++){
//				System.out.println(xL.get(xX).getCRUD());
//			}
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		
	//Insert diretamente da tabela. Sem necessidade de pesquisa via 'select'
	//Utilizando o nome das colunas
	@Test
	public void insertLoop(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			DBSIO.executeSQL(wCn, "Delete from crud");
			@SuppressWarnings("rawtypes")
			DBSDAO xDAO = new DBSDAO(wCn, "CRUD");
			Assert.assertEquals(0, xDAO.getRowsCount());
			//xDAO.setAutoIncrementValueRetrieve(false); 
			for (int xX=1; xX < 6; xX++){
				xDAO.setValue("CRUD_ID", xX); //Informa o ID, ignorando o autoincrement
				xDAO.setValue("CRUD", "TESTE " + xX);
				xDAO.executeInsert();
				System.out.println(xDAO.getValue("CRUD_ID"));
				xDAO.pvRestoreColumnsValuesOriginal();
			}
			DBSIO.endTrans(wCn, true);
			Assert.assertEquals(5, xDAO.getRowsCount());
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void deleteAll(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			wCn.setAutoCommit(true);
			Assert.assertEquals(5, DBSIO.executeSQL(wCn, "Delete from crud"));
			wCn.setAutoCommit(false);
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	//Insert diretamente da tabela. Sem necessidade de pesquisa via 'select'
	//Utilizando o DataModel
	@Test
	public void insertLoopDataModel(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			DBSIO.executeSQL(wCn, "Delete from crud");
			
			DBSDAO<TstDAOModel> xDAO = new DBSDAO<TstDAOModel>(TstDAOModel.class, wCn, "CRUD");
			Assert.assertEquals(0, xDAO.getRowsCount());
			for (int xX=1; xX < 6; xX++){
				xDAO.getDataModel().setCRUD_ID(xX);
				xDAO.getDataModel().setCRUD("TESTE " + xX);
				xDAO.executeInsert();
				System.out.println(xDAO.getValue("CRUD_ID"));
				xDAO.pvRestoreColumnsValuesOriginal();
			}
			DBSIO.endTrans(wCn, true);
			Assert.assertEquals(5, xDAO.getRowsCount());
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void readLoop(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			@SuppressWarnings("rawtypes")
			DBSDAO xDAO = new DBSDAO(wCn);
			xDAO.open("Select * from crud");
			xDAO.moveBeforeFirstRow();
			while(xDAO.moveNextRow()){
				System.out.println(DBSString.toProper((String)xDAO.getValue("crud")));
			}
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void readDataModel(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			DBSDAO<TstDAOModel> xDAO = new DBSDAO<TstDAOModel>(TstDAOModel.class, wCn);
			xDAO.open("Select * from crud where crud_id = 1");
			Assert.assertEquals("TESTE 1", xDAO.getDataModel().getCRUD());
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateDataModel(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			DBSDAO<TstDAOModel> xDAO = new DBSDAO<TstDAOModel>(TstDAOModel.class, wCn);
			xDAO.open("Select * from crud where crud_id = 1");
			xDAO.getDataModel().setCRUD("TESTE 10");
			xDAO.executeUpdate();
			Assert.assertEquals("TESTE 10", xDAO.getDataModel().getCRUD());
			xDAO.getDataModel().setCRUD("TESTE 1");
			xDAO.executeUpdate();
			Assert.assertEquals("TESTE 1", xDAO.getDataModel().getCRUD());
			DBSIO.endTrans(wCn, true);
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void mergeDataModel(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			DBSDAO<TstDAOModel> xDAO = new DBSDAO<TstDAOModel>(TstDAOModel.class, wCn, "CRUD");
			//Altera o nome
			xDAO.getDataModel().setCRUD_ID(2);
			xDAO.getDataModel().setCRUD("TESTE 200");
			xDAO.executeMerge();
			DBSIO.endTrans(wCn, true);
			//Verifica se a alteração foi correta
			xDAO.open("Select * from crud where crud_id = 2");
			Assert.assertEquals("TESTE 200", xDAO.getDataModel().getCRUD());
			//Insere novo registro
			xDAO.getDataModel().setCRUD_ID(6);
			xDAO.getDataModel().setCRUD("TESTE 6");
			xDAO.executeMerge();
			DBSIO.endTrans(wCn, true);
			//Verifica se a inclusão foi correta
			xDAO.open("Select * from crud where crud_id = 6");
			Assert.assertEquals("TESTE 6", xDAO.getDataModel().getCRUD());
			//Altera o nome
			xDAO.getDataModel().setCRUD_ID(2);
			xDAO.getDataModel().setCRUD("TESTE 2");
			xDAO.executeMerge();
			DBSIO.endTrans(wCn, true);
			//Verifica se a alteração foi correta
			xDAO.open("Select * from crud where crud_id = 2");
			Assert.assertEquals("TESTE 2", xDAO.getDataModel().getCRUD());
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getDadoDataModel(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			TstDAOModel xDT = new TstDAOModel();
			xDT = DBSIO.getDadoDataModel(wCn, TstDAOModel.class, "Select * From CRUD where Crud_ID=2");
			Assert.assertEquals("TESTE 2", xDT.getCRUD());
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getDadoDataModelList(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			DBSDAO<TstDAOModel> xDAO = new DBSDAO<TstDAOModel>(TstDAOModel.class, wCn);
			xDAO.open("Select * from CRUD");
			List<TstDAOModel> xL = new ArrayList<TstDAOModel>();
			xL = xDAO.getListDataModel();
			Assert.assertEquals("TESTE 2", xL.get(1).getCRUD());
			Assert.assertEquals("TESTE 5", xL.get(4).getCRUD());
//			for (int xX=0;xX<xL.size();xX++){
//				System.out.println(xL.get(xX).getCRUD());
//			}
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getCopy(){
		try {
			System.out.println("Inicio: " + getMethodName() + " ==================================");
			DBSDAO<TstDAOModel> xDAO = new DBSDAO<TstDAOModel>(TstDAOModel.class, wCn);
			xDAO.open("Select * from CRUD where CRUD_ID = 1");
			xDAO.getDataModel().setCRUD_ID(null);
			//xDAO.executeInsert();
			System.out.println(xDAO.getSQLExecuteCommand(COMMAND.INSERT));
			DBSIO.endTrans(wCn, false);
			xDAO.close();
			System.out.println("Fim:   " + getMethodName() + " ==================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	

	

}
