package br.com.dbsoft.core;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import br.com.dbsoft.startup.DBSApp;
import br.com.dbsoft.startup.DBSApp.PROJECT_STAGE;
import br.com.dbsoft.util.DBSString;

//@HandlesTypes({
//    javax.servlet.http.HttpServlet.class
//})
public class DBSSDKInitializer implements ServletContainerInitializer {

	
	@Override
	public void onStartup(Set<Class<?>> pC, ServletContext pCtx) throws ServletException {
		pvInitCorretorOrtografico();
		pvInitSystem();
		pvInitDBSApp(pCtx);
	
		System.out.println("DBSSDK Initialized for " + DBSApp.getAppDescription());
	}

	//============================================================
	//PRIVATE
	//============================================================
	
	//Inicializa corretor ortigráfico
	private void pvInitCorretorOrtografico(){
		DBSString.corretorOrtografico("");
	}

	private void pvInitSystem(){
		//Configuração para que os valores vázios"" não sejam convertidos para "0" e sim para nulo 
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

	//Informação do DBSApp
	private void pvInitDBSApp(ServletContext pCtx){
		//Informação do DBSApp
		String xPS = DBSString.getNotEmpty(pCtx.getInitParameter("javax.faces.PROJECT_STAGE"),"production").toLowerCase();
		if (xPS.equals("production")){
			DBSApp.ProjectStage = PROJECT_STAGE.PRODUCTION;
		}else{
			DBSApp.ProjectStage = PROJECT_STAGE.DEVELOPMENT;
		}
		DBSApp.AppName = pCtx.getServletContextName();
		DBSApp.AppVersion =  DBSString.getNotNull(DBSProperty.getBundleProperty("versao","versao.numero"), "");
		DBSApp.ContextPath = pCtx.getContextPath();
	}
}
