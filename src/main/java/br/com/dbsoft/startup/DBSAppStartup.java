package br.com.dbsoft.startup;

import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDKInitializer;


/**
 * Classe para ser utilizada em substituição ao ServletContextListener como listener inicial da aplicação.<br/>
 * Os valores da classe DBSApp serão preenchidos automaticamente partir deste listener.
 * Deve-se utilizar a anotação @WebListener em substuição da definição do listener no web.xml
 */
public abstract class DBSAppStartup implements ServletContextListener{

	protected Logger wLogger =  Logger.getLogger(this.getClass());

	private Integer wDelay = 4;
	
	/**
	 * Tempo antes de efetivamente inicializar o servidor.<br/>
	 * O tempo mínimo é de 5 segundos, pois tempos menores aumentam o risco de não estar ainda disponível as URLs do servidor
	 * @return
	 */
	public Integer getDelay() {return wDelay;}
	/**
	 * Tempo antes de efetivamente inicializar o servidor.<br/>
	 * O tempo mínimo é de 4 segundos, pois tempos menores aumentam o risco de não estar ainda disponível as URLs do servidor
	 * @return
	 */
	public void setDelay(Integer pDelay) {
		if (pDelay !=null && pDelay >= 4){
			wDelay = pDelay;
		}else{
			wDelay = 4;
		}
	}



	@Override
	public void contextInitialized(ServletContextEvent pSce) {
		pvInitDBSApp(pSce);
		if (beforeStart()){ 
			ScheduledExecutorService wScheduler = Executors.newSingleThreadScheduledExecutor();
			wScheduler.schedule(wGetHost, getDelay(), TimeUnit.SECONDS);
		}else{
			onError();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent pSce) {
		wLogger.info("STOPPING:" + pvGetDescription());
		afterStop();
		wLogger.info("STOPPED:" + pvGetDescription());
	}
	
	/**
	 * Método chamado antes do deploy efetivo. Não havendo serviços ativos ainda.<br/>
	 * Caso queira efetuar chamadas http para a própria aplicação, por exemplo, utilize o <b>afterStart</b>.
	 * @return
	 */
	public abstract boolean beforeStart();
	
	/**
	 * Método chamado após o deploy.<br/>
	 * A chamada a este método é efetuada com um delay mínimo, conforme definido no atributo.<br/> 
	 * Este delay é necesário para aguardar o deploy total da aplicação.
	 *
	 */
	public abstract void afterStart();
	
	
	/**
	 * Disparado quando no undeploy da aplicação.<br/>
	 * Não é disparado caso o servidor de aplicação se ja interrompido. 
	 */
	public abstract void afterStop();

	public void onError(){}

	
	//PRIVATE --------------------------------------------------------------------------------------------
	
	private void pvInitDBSApp(ServletContextEvent pSce){
		try {
			DBSApp.AppLocalPath = pSce.getServletContext().getResource(File.separator);
			DBSSDKInitializer.onStarting();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private Runnable wGetHost = new Runnable(){
	
		@Override
		public void run() {
			wLogger.info("STARTING (getting Host Info):" + pvGetDescription());
			boolean xOk = false;
			Long xTime = System.currentTimeMillis();
			//Efetua nova tentativa até não ocorrer erro ou ultrapassar timeout de 30 segundos
			while (!xOk){
				if ((System.currentTimeMillis() - xTime) > 30000){
					wLogger.error("START TIMEOUT:" + pvGetDescription());
					break;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				xOk = DBSSDKInitializer.onStartingGetInfo();
			}
			if (xOk){ 
				afterStart();
				wLogger.info("STARTED:" + pvGetDescription());
			}else{
				onError();
			}
		}
		
	};
	

	private String pvGetDescription(){
		return DBSApp.getAppDescription() + ":" + this.getClass().getSimpleName();
	}
	
	
}
