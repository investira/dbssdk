package br.com.dbsoft.startup;

import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;

import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.dbsoft.startup.DBSApp.PROJECT_STAGE;
import br.com.dbsoft.util.DBSNumber;

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
		if (pSce.getServletContext().getInitParameter("javax.faces.PROJECT_STAGE").toLowerCase().equals("production")){
			DBSApp.ProjectStage = PROJECT_STAGE.PRODUCTION;
		}else{
			DBSApp.ProjectStage = PROJECT_STAGE.DEVELOPMENT;
		}
		DBSApp.AppName = pSce.getServletContext().getServletContextName();
		DBSApp.ContextPath = pSce.getServletContext().getContextPath();
		wLogger.info("STARTING:" + DBSApp.AppName + "\t" + DBSApp.ProjectStage.toString());
		if (beforeStart()){
			ScheduledExecutorService wScheduler = Executors.newSingleThreadScheduledExecutor();
			wScheduler.schedule(wRun, getDelay(), TimeUnit.SECONDS);
		}else{
			onError();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent pSce) {
		wLogger.info("STOPPING:" + DBSApp.AppName);
		afterStop();
		wLogger.info("STOPPED:" + DBSApp.AppName);
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
	private Runnable wRun = new Runnable(){
	
		@Override
		public void run() {
			boolean xOk = false;
			Long xTime = System.currentTimeMillis();
			//Efetua nova tentativa até não ocorrer erro ou ultrapassar timeout de 10 segundos
			while (!xOk){
				if ((System.currentTimeMillis() - xTime) > 10000){
					break;
				}
				xOk = getInfo();
			}
			if (xOk){ 
				afterStart();
				wLogger.info("STARTED:" + DBSApp.AppName);
			}else{
				onError();
			}
		}
		
		private boolean getInfo(){
			try {
				Object xHttpPort = null;
				Object xHttpsPort = null;
				Object xLocalHost = null;
				xHttpPort = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http"), "port");
				xHttpsPort = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=https"), "port");
				xLocalHost = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:interface=public"), "inet-address");
				if (xLocalHost !=null){
					if (xHttpPort != null){
						DBSApp.URLHttp = new URL("http",xLocalHost.toString(), DBSNumber.toInteger(xHttpPort.toString()), DBSApp.ContextPath);
					}
					if (xHttpsPort != null){
						DBSApp.URLHttps = new URL("https",xLocalHost.toString(), DBSNumber.toInteger(xHttpsPort.toString()), DBSApp.ContextPath);
					}
				}
				return true;
			} catch (AttributeNotFoundException e) {
				//Ignore
			} catch (InstanceNotFoundException | MalformedObjectNameException
					| MBeanException | ReflectionException | MalformedURLException e) {
				wLogger.error(e);
			} catch (Exception e) {
				wLogger.error(e);
			}
			return false;
		}
	};;
}
