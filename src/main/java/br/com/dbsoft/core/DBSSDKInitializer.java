package br.com.dbsoft.core;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
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


public class DBSSDKInitializer implements ServletContainerInitializer {

	private static Integer wMBeanCount = 0;
	
	@Override
	public void onStartup(Set<Class<?>> pC, ServletContext pCtx) throws ServletException {
		pvSetSystem();
		pvInitCorretorOrtografico();
		pvInitDBSApp(pCtx);
		
		System.out.println(">>> DBSSDK Initialized for " + DBSApp.getAppDescription());
	}

	/**
	 * Recupera valores de atributos que só estão disponívels após o deploy 
	 * @return
	 */
	public static boolean onStartingGetInfo(){
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
//			System.out.println("MalformedURLException() ==============");
			e.printStackTrace();
		} catch (Exception e) {
			e.setStackTrace(null);
		}
		return false;
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
		if (xMBS== null){
			System.out.println("MBS NULL ==========================================");
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
	private void pvInitCorretorOrtografico(){
		DBSString.corretorOrtografico("");
	}

	/**
	 *Configura System
	 */
	private void pvSetSystem(){
		//Configuração para que os valores vázios"" não sejam convertidos para "0" e sim para nulo 
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

	
	/**
	 * Informação do DBSApp
	 * @param pCtx
	 */
	private void pvInitDBSApp(ServletContext pCtx){
		//Informação do DBSApp
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
		//		System.out.println("==pvInitDBSApp CONTEXT PATH1:" + DBSApp.ContextPath);
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
		    e.printStackTrace();
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
			e.printStackTrace();
		}
		return null;
	}

}
