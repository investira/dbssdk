package br.com.dbsoft.core;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import br.com.dbsoft.startup.DBSApp;
import br.com.dbsoft.startup.DBSApp.PROJECT_STAGE;
import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSString;

/**
 * @author ricardo.villar
 *
 */
public final class DBSSDKInitializer implements ServletContainerInitializer {

	private static Logger wLogger =  Logger.getLogger(DBSSDKInitializer.class.getName());
	
	private static Integer wMBeanCount = 0;
	
	private static Boolean wStarted = false;
	
	private static ScheduledExecutorService wScheduler;
	
	private static List<IDBSSDKInitializer> wEventListeners = new ArrayList<IDBSSDKInitializer>();
	
	private static Long xTime = System.currentTimeMillis();
	
	/**
	 * Retorna se servidor foi iniciado
	 * @return
	 */
	public static Boolean getStarted(){
		return wStarted;
	}
	
	/**
	 * Classe que receberá as chamadas dos eventos quando ocorrerem.<br/>
	 * Lembre-se de remove-la utilizando removeEventListener quando a classe for destruida, para evitar que ela seja chamada quando já não deveria. 
	 * @param pEventListener Classe
	 */
	public static void addEventListener(IDBSSDKInitializer pEventListener) {
		if (!wEventListeners.contains(pEventListener)){
			wEventListeners.add(pEventListener);
		}
	}

	
	public static void removeEventListener(IDBSSDKInitializer pEventListener) {
		if (wEventListeners.contains(pEventListener)){
			wEventListeners.remove(pEventListener);
		}
	}	
	
	@Override
	public void onStartup(Set<Class<?>> pC, ServletContext pCtx) throws ServletException {
		pCtx.addListener(wServletContextListener);
//		pCtx.addListener(xServletContextAttributeListener);
//		pCtx.addListener(xServletRequestAttributeListener);
//		pCtx.addListener(xServletRequestListener);
//		pCtx.addListener(xHttpSessionAttributeListener);
//		pCtx.addListener(xHttpSessionIdListener);
//		pCtx.addListener(xHttpSessionListener);

		pvSetSystem();
		pvInitCorretorOrtografico();
		pvInitDBSApp(pCtx);
		
		wLogger.log(Level.INFO, ">>> DBSSDK " + DBSApp.getAppDescription());
	}


	private static ServletContextListener wServletContextListener = new ServletContextListener(){
		@Override
		public void contextInitialized(ServletContextEvent pSce) {
			//Efetua chamada de forma assincrona para evitar que fique travado enquanto aguarda o deploy integral da aplicação
			wScheduler = Executors.newSingleThreadScheduledExecutor();
			wScheduler.scheduleAtFixedRate(wGetHost, 1, 1, TimeUnit.SECONDS);
		}
	
		@Override
		public void contextDestroyed(ServletContextEvent pSce) {}
	};

	/**
	 * Tarefa que efetuará a pesquisa para recuperar algumas configurações do servidor e</br>
	 * verificar se ele foi inicializado.
	 */
	private static Runnable wGetHost = new Runnable(){
		@Override
		public void run() {
			if (wStarted){
				wScheduler.shutdown();
			}else if ((System.currentTimeMillis() - xTime) > 300000){ //timeout 5 minutos
				wScheduler.shutdown();
				wLogger.log(Level.INFO, ">>> DBSSDK Reading info Timeout" + DBSApp.getAppDescription());
			}else{
				wStarted = pvStartingGetInfo();
				if (wStarted){
					for (int xX=0; xX<wEventListeners.size(); xX++){
						wEventListeners.get(xX).fireStarted();
				    }
				}
			}
		}
	};
	
	/**
	 * Recupera valores de atributos que só estão disponívels após o deploy 
	 * @return
	 */
	private static boolean pvStartingGetInfo(){
		wLogger.log(Level.INFO, ">>> DBSSDK Reading info:" + DBSApp.getAppDescription());
		Object xHttpPort = null;
		Object xHttpsPort = null;
		Object xLocalHost = null;
		//Le o alias do servidor , caso exista para substituir o localhost
		try {
			xLocalHost = pvGetManagementFactoryPlatformMBeanServerAttribute("jboss.as:interface=public", "inet-address");
			if (xLocalHost !=null){
				//Verificar se foi configurado o virtualhost(configuração é efetuado no arquivo no WEB-INF/jboss-web.xml
				if (DBSApp.VirtualHostName != null){
					xLocalHost = pvGetManagementFactoryPlatformMBeanServerAttribute("jboss.as.expr:subsystem=undertow,server=default-server,host=" + DBSApp.AppName, "alias");
					if (xLocalHost != null){
						xLocalHost = ((String[]) xLocalHost)[0]; //Le primeiro item do alias
					}
				}
				xHttpPort = pvGetManagementFactoryPlatformMBeanServerAttribute("jboss.as:socket-binding-group=standard-sockets,socket-binding=http", "port");
				xHttpsPort = pvGetManagementFactoryPlatformMBeanServerAttribute("jboss.as:socket-binding-group=standard-sockets,socket-binding=https", "port");
				if (xHttpPort != null){
					DBSApp.URLHttp = new URL("http",xLocalHost.toString(), DBSNumber.toInteger(xHttpPort.toString()), DBSApp.ContextPath);
				}
				if (xHttpsPort != null){
					DBSApp.URLHttps = new URL("https",xLocalHost.toString(), DBSNumber.toInteger(xHttpsPort.toString()), DBSApp.ContextPath);
				}
				return true;
			}
		} catch (MalformedURLException e) {
			wLogger.log(Level.INFO, e.getMessage());
		} catch (Exception e) {
			e.setStackTrace(null);
		}
		return false;
	}

	/**
	 * Informação do DBSApp
	 * @param pCtx
	 */
	private static void pvInitDBSApp(ServletContext pCtx){
		String xPS = DBSString.getNotEmpty(pCtx.getInitParameter("javax.faces.PROJECT_STAGE"),"production").toLowerCase();
		if (xPS.equals("production")){
			DBSApp.ProjectStage = PROJECT_STAGE.PRODUCTION;
		}else{
			DBSApp.ProjectStage = PROJECT_STAGE.DEVELOPMENT;
		}
		DBSApp.AppName = pCtx.getServletContextName(); 
		DBSApp.ContextPath = pCtx.getContextPath();
		DBSApp.AppVersion =  DBSString.getNotNull(DBSProperty.getBundleProperty("versao","versao.numero"), "");
		pvSetVirtualHost(pCtx);
	}

	/**
	 * Recupera os atributos do servidor
	 * @param pObjectName
	 * @param pAttibuteName
	 * @return
	 */
	private static Object pvGetManagementFactoryPlatformMBeanServerAttribute(String pObjectName, String pAttibuteName){
		//ManagementFactory.getPlatformMBeanServer().queryNames(null, null) //Le todos as chaves dos atributos do servidor
		ObjectName xON = null;
		MBeanServer xMBS = ManagementFactory.getPlatformMBeanServer(); 
		if (xMBS == null){
			return null;
		}
		try {
			ArrayList<String> xObjs = DBSString.toArrayList(pObjectName, ",");
			String xFullName = "";
			for (String xName:xObjs){
				xFullName += xName;
				xON = new ObjectName(xFullName);
				if (!xMBS.isRegistered(xON)){
					return null;
				}
				xFullName += ",";
			}
			xON = new ObjectName(pObjectName);
			if (xON != null){
				if (xMBS.isRegistered(xON)){
					try {
						//Artifício para carragar os atributos e evitar AttributeNotFoundException
						if (wMBeanCount == 0){
							wMBeanCount = xMBS.getMBeanCount();
						}
						return xMBS.getAttribute(xON, pAttibuteName); 
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
		} catch (Exception e) {
//			e.setStackTrace(null);
//			System.out.println("getManagementFactoryPlatformMBeanServerAttribute() 6 ==============");
		}
		return null;
	}
	//============================================================
	//PRIVATE
	//============================================================
	
	/**
	 *Inicializa corretor ortigráfico 
	 */
	private static void pvInitCorretorOrtografico(){
		DBSString.corretorOrtografico("");
	}

	/**
	 *Configura System
	 */
	private static void pvSetSystem(){
		//Configuração para que os valores vázios"" não sejam convertidos para "0" e sim para nulo 
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

	
	/**
	 * Le arquivo jboss-web.xml para configurar o virtualhost(se houver).
	 */
	private static void pvSetVirtualHost(ServletContext pCtx){
		try {
			DBSApp.VirtualHostName = null;
		    URL xFileName = pCtx.getResource("/WEB-INF/jboss-web.xml"); 
		    if (xFileName != null){
				DocumentBuilderFactory	xDocFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder 		xDocBuilder = xDocFactory.newDocumentBuilder();
				File 					xFile = new File(xFileName.getFile()); 
			    Document 				xDoc = xDocBuilder.parse(xFile);
			    XPath 					xPath = XPathFactory.newInstance().newXPath();
			    DBSApp.VirtualHostName = pvReadJBossWebValue(xDoc, xPath, "jboss-web/virtual-host");
		    }
		} catch (Exception e) {
//		    e.printStackTrace();
			wLogger.log(Level.INFO, e.getMessage());
		}
	}
	
	/**
	 * Retorna conteúdo do nó informado
	 * @param pDoc
	 * @param pPath
	 * @param pNode
	 * @return
	 */
	private static String pvReadJBossWebValue(Document pDoc,  XPath pPath, String pNode){
		try {
		    Node xNode;
			xNode = (Node) pPath.evaluate(pNode, pDoc, XPathConstants.NODE);
			if (xNode != null){
				if(xNode.getFirstChild() != null){
					return xNode.getFirstChild().getNodeValue();
				}
			}
		} catch (XPathExpressionException e) {
			wLogger.log(Level.INFO, e.getMessage());
		}
		return null;
	}
	

//	ServletContextAttributeListener xServletContextAttributeListener = new ServletContextAttributeListener(){
//		@Override
//		public void attributeAdded(ServletContextAttributeEvent pEvent) {
//			System.out.println("ServletContextAttributeListener attributeAdded");
//		}
//
//		@Override
//		public void attributeRemoved(ServletContextAttributeEvent pEvent) {
//			System.out.println("ServletContextAttributeListener attributeRemoved");
//		}
//
//		@Override
//		public void attributeReplaced(ServletContextAttributeEvent pEvent) {
//			System.out.println("ServletContextAttributeListener attributeReplaced");
//		}
//	};
//
//	ServletRequestListener xServletRequestListener = new ServletRequestListener(){
//		@Override
//		public void requestDestroyed(ServletRequestEvent pSre) {
//			System.out.println("ServletRequestListener requestDestroyed");
//		}
//
//		@Override
//		public void requestInitialized(ServletRequestEvent pSre) {
//			System.out.println("ServletRequestListener requestInitialized");
//		}
//	};
//
//	ServletRequestAttributeListener xServletRequestAttributeListener = new ServletRequestAttributeListener(){
//		@Override
//		public void attributeAdded(ServletRequestAttributeEvent pSrae) {
//			System.out.println("ServletRequestAttributeListener attributeAdded");
//		}
//
//		@Override
//		public void attributeRemoved(ServletRequestAttributeEvent pSrae) {
//			System.out.println("ServletRequestAttributeListener attributeRemoved");
//		}
//
//		@Override
//		public void attributeReplaced(ServletRequestAttributeEvent pSrae) {
//			System.out.println("ServletRequestAttributeListener attributeReplaced");
//		}
//	};
//
//	HttpSessionAttributeListener xHttpSessionAttributeListener = new HttpSessionAttributeListener(){
//		@Override
//		public void attributeAdded(HttpSessionBindingEvent pEvent) {
//			System.out.println("HttpSessionAttributeListener attributeAdded");
//		}
//
//		@Override
//		public void attributeRemoved(HttpSessionBindingEvent pEvent) {
//			System.out.println("HttpSessionAttributeListener attributeRemoved");
//		}
//
//		@Override
//		public void attributeReplaced(HttpSessionBindingEvent pEvent) {
//			System.out.println("HttpSessionAttributeListener attributeReplaced");
//		}
//	};
//
//	HttpSessionIdListener xHttpSessionIdListener = new HttpSessionIdListener(){
//		@Override
//		public void sessionIdChanged(HttpSessionEvent pEvent, String pOldSessionId) {
//			System.out.println("HttpSessionIdListener sessionIdChanged");
//		}
//	};
//
//	HttpSessionListener xHttpSessionListener = new  HttpSessionListener(){
//
//		@Override
//		public void sessionCreated(HttpSessionEvent pSe) {
//			System.out.println("HttpSessionListener sessionCreated");
//		}
//
//		@Override
//		public void sessionDestroyed(HttpSessionEvent pSe) {
//			System.out.println("HttpSessionListener sessionDestroyed");
//		}
//		
//	};

}
