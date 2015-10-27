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
import br.com.dbsoft.util.DBSNumber;

/**
 * Classe para ser utilizada em substituição ao ServletContextListener.
 * Deve-se utilizar a anotação @WebListener em substuição da definição do listiner no web.xml
 */
public abstract class DBSStartup implements ServletContextListener{

	protected Logger wLogger =  Logger.getLogger(this.getClass());

	private Integer wDelay = 4;
	
	private static String wAppName = null;
	private static String wContextPath = null;
	
	private static URL wURLHttp = null;
	private static URL wURLHttps = null;
	
	/**
	 * URL do servidor HTTP
	 * @return
	 */
	public static URL getURLHttp(){return wURLHttp;}
	/**
	 * URL do servidor HTTPs
	 * @return
	 */
	public static URL getURLHttps(){return wURLHttps;}

	
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

	private Runnable wRun = new Runnable(){

		@Override
		public void run() {
			boolean xOk = false;
			wLogger.info("Starting:\t" + wAppName);
			Long xTime = System.currentTimeMillis();
			//Efetua nova tentativa até não ocorrer erro ou ultrapassar timeout de 10 segundos
			while (!xOk){
				if ((System.currentTimeMillis() - xTime) > 10000){
					break;
				}
				xOk = getInfo();
			}
			if (xOk 
			 && onStarted()){
				wLogger.info("Started:\t" + wAppName);
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
						wURLHttp = new URL("http",xLocalHost.toString(), DBSNumber.toInteger(xHttpPort.toString()), wContextPath);
					}
					if (xHttpsPort != null){
						wURLHttps = new URL("https",xLocalHost.toString(), DBSNumber.toInteger(xHttpsPort.toString()), wContextPath);
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
	};
	
	@Override
	public void contextInitialized(ServletContextEvent pSce) {
		wContextPath = pSce.getServletContext().getContextPath();
		wAppName = pSce.getServletContext().getContextPath();
		ScheduledExecutorService wScheduler = Executors.newSingleThreadScheduledExecutor();
		wScheduler.schedule(wRun, getDelay(), TimeUnit.SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent pSce) {
		wLogger.info("Stopping:\t" + wAppName);
		onStopped();
		wLogger.info("Stopped:\t" + wAppName);
	}


	public abstract boolean onStarted();
	
	public abstract void onStopped();

	public abstract void onError();
}
