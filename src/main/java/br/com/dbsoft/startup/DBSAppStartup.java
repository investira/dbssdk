package br.com.dbsoft.startup;

import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;

import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSString;


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


//	private xL xLL = new xL();
//	private class xL implements ServletContextAttributeListener{

//		@Override
//		public void contextInitialized(ServletContextEvent pSce) {
//			System.out.println("contextInitialized");
//		}
//
//		@Override
//		public void contextDestroyed(ServletContextEvent pSce) {
//			System.out.println("contextDestroyed");
//		}
//
//		@Override
//		public void attributeAdded(ServletContextAttributeEvent pEvent) {
//			System.out.println("attributeAdded\t" + pEvent.getName() + ":" + pEvent.getValue());
//		}
//
//		@Override
//		public void attributeRemoved(ServletContextAttributeEvent pEvent) {
//			System.out.println("attributeRemoved\t" + pEvent.getName() + ":" + pEvent.getValue());
//		}
//
//		@Override
//		public void attributeReplaced(ServletContextAttributeEvent pEvent) {
//			System.out.println("attributeReplaced\t" + pEvent.getName() + ":" + pEvent.getValue());
//		}
//	}
	
	@Override
	public void contextInitialized(ServletContextEvent pSce) {
//		pSce.getServletContext().addListener(xLL);
		wLogger.info("STARTING:" + pvGetDescription()); 
		if (beforeStart()){ 
			ScheduledExecutorService wScheduler = Executors.newSingleThreadScheduledExecutor();
			wScheduler.schedule(wGetHost, getDelay(), TimeUnit.SECONDS);
		}else{
			onError();
		}
//		ManagementFactory.getPlatformMBeanServer().addNotificationListener(name, listener, filter, handback);
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
				xOk = getInfo();
			}
			if (xOk){ 
				afterStart();
				wLogger.info("STARTED:" + pvGetDescription());
			}else{
				onError();
			}
		}
		
		private boolean getInfo(){
			try {
				Object xHttpPort = null;
				Object xHttpsPort = null;
				Object xLocalHost = null;
				//Le o alias do servidor , caso exista para substituir o localhost
				xLocalHost = getManagementFactoryPlatformMBeanServerAttribute("jboss.as.expr:subsystem=undertow,server=default-server,host=" + DBSApp.AppName, "alias");
				if (xLocalHost == null){
					//Le o localhost padrão
					xLocalHost = getManagementFactoryPlatformMBeanServerAttribute("jboss.as:interface=public", "inet-address");
				}else{
					xLocalHost = ((String[]) xLocalHost)[0]; //Le primeiro item do alias
				}
				if (xLocalHost !=null){
					xHttpPort = getManagementFactoryPlatformMBeanServerAttribute("jboss.as:socket-binding-group=standard-sockets,socket-binding=http", "port");
					xHttpsPort = getManagementFactoryPlatformMBeanServerAttribute("jboss.as:socket-binding-group=standard-sockets,socket-binding=https", "port");
					if (xHttpPort != null){
						DBSApp.URLHttp = new URL("http",xLocalHost.toString(), DBSNumber.toInteger(xHttpPort.toString()), DBSApp.ContextPath);
					}
					if (xHttpsPort != null){
						DBSApp.URLHttps = new URL("https",xLocalHost.toString(), DBSNumber.toInteger(xHttpsPort.toString()), DBSApp.ContextPath);
					}
				}
				return true;
			} catch (MalformedURLException e) {
//				System.out.println("MalformedURLException() ==============");
				wLogger.error(e);
			} catch (Exception e) {
				e.setStackTrace(null);
				wLogger.error(e);
			}
			return false;
		}
	};
	
	public static Object getManagementFactoryPlatformMBeanServerAttribute(String pObjectName, String pAttibuteName){
		//ManagementFactory.getPlatformMBeanServer().queryNames(null, null)
		ObjectName xON;
		Object xMBS = ManagementFactory.getPlatformMBeanServer();
		try {
			ArrayList<String> xObjs = DBSString.toArrayList(pObjectName, ",");
			String xFullName = "";
			for (String xName:xObjs){
				xFullName += xName;
				xON = new ObjectName(xFullName);
				if (!((MBeanServer)xMBS).isRegistered(xON)){
					return null;
				}
				xFullName += ",";
			}
			xON = new ObjectName(pObjectName);
			if (xON != null){
				if (((MBeanServer)xMBS).isRegistered(xON)){
					try {
	//					String[] attrNames = {pAttibuteName};
	//					AttributeList list = ManagementFactory.getPlatformMBeanServer().getAttributes(xON, attrNames);
	//					if (list.size() > 0){
	//						return list.get(0);
	//					}
//						System.out.println(xON.toString());
//						System.out.println(xON.getDomain());
//						System.out.println(xON.getKeyPropertyListString());
//						System.out.println(xON.getKeyPropertyList());
//						System.out.println(xMBS.getClass()); //PluggableMBeanServerImpl // org.jboss.as.jmx.PluggableMBeanServerImpl cannot be cast to org.jboss.as.server.jmx.PluggableMBeanServer
						return ((MBeanServer)xMBS).getAttribute(xON, pAttibuteName); 
					} catch (AttributeNotFoundException e) {
						e.setStackTrace(null);
//						System.out.println("getManagementFactoryPlatformMBeanServerAttribute() 1 ==============");
					} catch (InstanceNotFoundException e) {
//						System.out.println("getManagementFactoryPlatformMBeanServerAttribute() 2 ==============");
//						e.printStackTrace();
					} catch (MBeanException e) {
//						System.out.println("getManagementFactoryPlatformMBeanServerAttribute() 3 ==============");
//						e.printStackTrace();
					} catch (ReflectionException e) {
//						System.out.println("getManagementFactoryPlatformMBeanServerAttribute() 4 ==============");
//						e.printStackTrace();
					} catch (Exception e) {
//						System.out.println("getManagementFactoryPlatformMBeanServerAttribute() 5 ==============");
						e.setStackTrace(null);
					}
				}
			}
		} catch (MalformedObjectNameException e1) {
//			System.out.println("getManagementFactoryPlatformMBeanServerAttribute() 0 ==============");
		}
		return null;
	}
	
	//Private==================
	
	
	private String pvGetDescription(){
		return DBSApp.getAppDescription() + ":" + this.getClass().getSimpleName();
	}
}
