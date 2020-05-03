package br.com.dbsoft.startup;

import java.io.File;
import java.net.MalformedURLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDKInitializer;
import br.com.dbsoft.core.IDBSSDKInitializer;

/**
 * Classe para ser utilizada em substituição ao ServletContextListener como listener inicial da aplicação.<br/>
 * Os valores da classe DBSApp serão preenchidos automaticamente partir deste listener.
 * Deve-se utilizar a anotação @WebListener em substuição da definição do listener no web.xml
 */
public abstract class DBSAppStartup implements ServletContextListener, IDBSSDKInitializer{

	protected Logger wLogger =  Logger.getLogger(DBSAppStartup.class);

//	ScheduledExecutorService wScheduler;

	public DBSAppStartup() {
		DBSSDKInitializer.addEventListener(this);
	}
	
	@Override
	public final void contextInitialized(ServletContextEvent pSCE) {
		wLogger.info(">>> STARTING:" + pvGetDescription());
		if (beforeStart(pSCE)){ 
			try {
//				DBSApp.AppLocalPath = pSCE.getServletContext().getResource(File.separator);
				DBSApp.AppLocalPath = pSCE.getServletContext().getResource("/");
			} catch (MalformedURLException e) {
				wLogger.error(e);
			}
			//Efetua chamada de forma assincrona para evitar que fique travado enquanto aguarda o deploy integral da aplicação
//			ScheduledExecutorService wScheduler = Executors.newSingleThreadScheduledExecutor();
//			wScheduler.schedule(wStart, 0, TimeUnit.SECONDS); 
		}else{
			onError();
		}
	}

	@Override
	public final void contextDestroyed(ServletContextEvent pSCE) {
		wLogger.info(">>> STOPPING:" + pvGetDescription());
		DBSSDKInitializer.removeEventListener(this);
		afterStop();
		wLogger.info(">>> STOPPED:" + pvGetDescription());
	}
	
	/**
	 * Método chamado antes do deploy efetivo. Não havendo serviços ativos ainda.<br/>
	 * Caso queira efetuar chamadas http para a própria aplicação, por exemplo, utilize o <b>afterStart</b>.
	 * @return
	 */
	@Override
	public abstract boolean beforeStart(ServletContextEvent pSCE);
	
	/**
	 * Método chamado após o deploy.<br/>
	 * A chamada a este método é efetuada com um delay mínimo, conforme definido no atributo.<br/> 
	 * Este delay é necesário para aguardar o deploy total da aplicação.
	 *
	 */
	@Override
	public abstract void afterStart();
	
	/**
	 * Disparado quando no undeploy da aplicação.<br/>
	 * Não é disparado caso o servidor de aplicação se ja interrompido. 
	 */
	@Override
	public abstract void afterStop();

	@Override
	public void onError(){}

	@Override
	public final void fireStarted(){
		wLogger.info(">>> STARTED:" + pvGetDescription());
		afterStart();
	}
	
	//PRIVATE --------------------------------------------------------------------------------------------

	
	private String pvGetDescription(){
		return DBSApp.getAppDescription() + ":" + this.getClass().getSimpleName();
	}
	
	
}
