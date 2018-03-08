package br.com.dbsoft.core;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.dbsoft.util.DBSObject;


public final class DBSSDK {
	public static final String DOMAIN = "br.com.dbsoft";
	
	public static final int VERDADEIRO = -1;
	public static final int FALSO = 0;
	
	public static final class UI
	{
		public static enum ID_PREFIX{
			APPLICATION 		("ap"),					
			MENU				("mn"),
			FORM				("fr"),
			TABLE				("tb"),
			BUTTON				("bt"),
			FIELD_CRUD			("fl"),
			FIELD_FILTER		("ft"),
			FIELD_AUX			("fx"),
			OBJECT				("ob"),
			DEAD_END			("de");
			
			private String 	wName;
			
			private ID_PREFIX(String pName) {
				this.wName = pName;
			}

			public String getName() {
				return wName;
			}

			public static ID_PREFIX get(String pPrefixo) {
				String xString = pPrefixo.trim().toLowerCase();
				if (xString.equals(APPLICATION.getName())){
					return APPLICATION;
				}else if (xString.equals(MENU.getName())){
					return MENU;
				}else if (xString.equals(FORM.getName())){
					return FORM;
				}else if (xString.equals(TABLE.getName())){
					return TABLE;
				}else if (xString.equals(BUTTON.getName())){
					return BUTTON;
				}else if (xString.equals(FIELD_CRUD.getName())){
					return FIELD_CRUD;
				}else if (xString.equals(FIELD_FILTER.getName())){
					return FIELD_FILTER;
				}else if (xString.equals(FIELD_AUX.getName())){
					return FIELD_AUX;
				}else if (xString.equals(DEAD_END.getName())){
					return DEAD_END;
				}
				return null;
			}
		}
		
		public static final class COMBOBOX{
			public static final String NULL_VALUE = ""; //Valor do item null na lista. Para mantém conformidade com o JSF que por não se possível enviar NULL, irá considerar o valor 'vázio' como nulo.
			
			public enum NULL_TEXT{
				NAO_EXIBIR,
				NENHUM,
				NENHUMA,
				DEFAULT,
				PADRAO,
				TODOS,
				TODAS,
				BRANCO,
				INEXISTENTE,
				NAO_SELECIONADO,
				NAO_SELECIONADA;
				
				String toString;
	
				NULL_TEXT(String toString) {
					this.toString = toString;
				}
	
				NULL_TEXT() {}
	
				@Override
				public String toString() {
					switch (this) {
						case NAO_EXIBIR:
							return "";
						case NENHUM:
							return "(Nenhum)";
						case NENHUMA:
							return "(Nenhuma)";
						case DEFAULT:
							return "(Default)";
						case PADRAO:
							return "(Padrão)";
						case TODOS:
							return "(Todos)";
						case TODAS:
							return "(Todas)";
						case BRANCO:
							return "";
						case INEXISTENTE:
							return "(Inexistente)";
						case NAO_SELECIONADO:
							return "(Não selecionado)";
						case NAO_SELECIONADA:
							return "(Não selecionada)";
						default:
							return "";
					}
				}
			}
		}
	}

	public static final class ENCODE{
		public static final String US_ASCII = "US-ASCII";
		public static final String UTF_8 = "UTF-8";
		public static final String ISO_8859_1 = "ISO-8859-1";
		public static final String ISO_8859_6 = "ISO-8859-6";
	}
	
	public static final class CONTENT_TYPE{
		public static final String APPLICATION_JSON = "application/json";
		public static final String APPLICATION_JAVA_SERIALIZED_OBJECT = "application/x-java-serialized-object";
		public static final String APPLICATION_PDF = "application/pdf";
		public static final String APPLICATION_XML = "application/xml";
		public static final String APPLICATION_XLS = "application/excel";
		public static final String APPLICATION_XLSX = "application/excel";
		public static final String TEXT_PLAIN = "text/plain";
		public static final String TEXT_EVENT_STREAM = "text/event-stream";
		public static final String TEXT_HTML = "text/html";
		public static final String TEXT_JAVASCRIPT = "text/javascript";
	}
	
	
	public static final class FILE{
		public enum TYPE{
			HTML,
			XML,
			TXT,
			CVS,
			DOC,
			XLS,
			ZIP,
			RAR,
			BIN,
			DMG,
			PDF,
			FOLDER,
			GENERAL;
		}
		public static class EXTENSION{
			public static final String PDF = ".pdf";
			public static final String HTML = ".html";
			public static final String XML = ".xml";
			public static final String XLS = ".xls";
			public static final String XLSX = ".xlsx";
			public static final String JASPER= ".jasper";
			public static final String JRXML = ".jrxml";
			public static final String ZIP = ".zip";
			public static final String DOC = ".doc";
			public static final String RAR = ".rar";
			public static final String TXT = ".txt";
			public static final String CSV = ".csv";
		}	
	}
	
	public static final class NETWORK{
		public static enum PROTOCOL {
			HTTP		("HTTP"),
			HTTPS		("HTTPS"),
			SSH			("SSH"),
			SFTP		("SFTP"),
			FTP			("FTP"),
			FTPS		("FTPS"),
			UDP			("UDP"),
			SSL			("SSL"),
			TLS			("TLS"),
			STARTTLS	("STARTTLS");
		
			private String 	wName;
			public String getName() {return wName;}
			
			private PROTOCOL(String pName) {
				wName = pName;
			}

			public static PROTOCOL get(String pName) {
				switch (pName) {
				case "HTTP":
					return HTTP;
				case "HTTPS":
					return HTTPS;
				case "SSH":
					return SSH;
				case "SFTP":
					return SFTP;
				case "FTP":
					return FTP;
				case "FTPS":
					return FTPS;
				case "UDP":
					return UDP;
				case "SSL":
					return SSL;
				case "TLS":
					return TLS;
				case "STARTTLS":
					return STARTTLS;
				default:
					return null;
				}
			}
			
		}
		
		public static enum METHOD {
			POST	("POST"),
			GET		("GET");
		
			private String 	wName;
			public String getName() {return wName;}
			
			private METHOD(String pName) {
				wName = pName;
			}

			public static METHOD get(String pName) {
				switch (pName) {
				case "POST":
					return POST;
				case "GET":
					return GET;
				default:
					return null;
				}
			}
		}
		
	}
	
	public static final class TABLE {
		public static String FERIADO = "";
	}
	


	public static final class JDBC_DRIVER {
	    public static final String MYSQL = "com.mysql.jdbc.Driver";
	    public static final String ORACLE = "oracle.jdbc.driver.OracleDriver";
	}
	
	/**
	 * @author ricardo.villar
	 *
	 */
	public static final class SYSTEM_PROPERTY{
		public static final String SERVER_BASE_DIR = "jboss.server.base.dir";
		public static final String CONFIG_URL = "jboss.server.config.url";
		public static final String BIND_ADDRESS = "jboss.bind.address";
		public static final String USER_LANGUAGE = "user.language";
		public static final String USER_LANGUAGE_FORMAT = "user.language.format";
		public static final String USER_TIMEZONE =  "user.timezone";
		public static final String PATH_SEPARATOR =  "path.separator";
		public static final String JAVA_VERSION =  "java.version";
		}
	
	public static final class SYS {
		public enum APP_SERVER { //Aplication Server
			JBOSS,
			WEBSPHERE,
			GLASSFISH,
			WILDFLY;
		}
		
		public enum OS {
			MACOS,
			IOS,
			ANDROID,
			RIM,
			LINUX,
			WEBOS,
			WINDOWS,
			WINDOWSPHONE,
			SYMBIAN;
			
			public static OS get(Integer pCode) {
				if (DBSObject.isNull(pCode)) {
					return null;
				}
				switch (pCode) {
				case 0:
					return MACOS;
				case 1:
					return IOS;
				case 2:
					return ANDROID;
				case 3:
					return RIM;
				case 4:
					return LINUX;
				case 5:
					return WEBOS;
				case 6:
					return WINDOWS;
				case 7:
					return WINDOWSPHONE;
				case 8:
					return SYMBIAN;
				default:
					return null;
				}
			}
			
			public static OS getOSFromUserAgent(String pUserAgent) {
				OS xOS = null;
				if (pUserAgent.contains("Mac")) {
					xOS = OS.MACOS;
				} else if (pUserAgent.contains("Win")) {
					xOS = OS.WINDOWS;
				} else if (pUserAgent.contains("X11")) {
					xOS = OS.LINUX;
				} else if (pUserAgent.contains("Linux")) {
					xOS = OS.LINUX;
				}
				return xOS;
			}
		}

		public enum APP_CLIENT {
			WEB,
			DOTNET,
			OBJC,
			JAVA;
		}

		public static enum WEB_CLIENT {
			DEFAULT		("Defaut", ""),
			CHROME		("Chrome", "-webkit-"),
			FIREFOX		("Mozilla", "-moz-"),
			OPERA		("Opera", "-o-"),
			SAFARI		("Safari", "-webkit-"),
			MICROSOFT	("Microsoft", "-ms-");
		
			private String 	wCSSPrefix;
			private String 	wUserAgent;
			public String getCSSPrefix() {return wCSSPrefix;}
			public String getUserAgent() {return wUserAgent;}
			
			private WEB_CLIENT(String pUserAgent, String pCSSPrefix) {
				wUserAgent = pUserAgent;
				wCSSPrefix = pCSSPrefix;
			}

			public static WEB_CLIENT get(String pCSSPrefix) {
				switch (pCSSPrefix) {
				case "-webkit-":
					return CHROME;
				case "-moz-":
					return FIREFOX;
				case "-o-":
					return OPERA;
				case "-ms-":
					return MICROSOFT;
				default:
					return DEFAULT;
				}
			}
			
			public static WEB_CLIENT get(Integer pCode) {
				if (DBSObject.isNull(pCode)) {
					return null;
				}
				switch (pCode) {
				case 1:
					return CHROME;
				case 2:
					return FIREFOX;
				case 3:
					return OPERA;
				case 4:
					return SAFARI;
				case 5:
					return MICROSOFT;
				default:
					return DEFAULT;
				}
			}
			
			public static WEB_CLIENT getBrowserFromUserAgent(String pUserAgent) {
				WEB_CLIENT xBrowser = WEB_CLIENT.DEFAULT; //Desconhecido
				if (pUserAgent.contains("MSIE")) {
				    xBrowser = WEB_CLIENT.MICROSOFT;
				} else if (pUserAgent.contains("Chrome")) {
					xBrowser = WEB_CLIENT.CHROME;
				} else if (pUserAgent.contains("Firefox")) {
					xBrowser = WEB_CLIENT.FIREFOX;
				} else if (pUserAgent.contains("Safari") && !pUserAgent.contains("Chrome")) {
					xBrowser = WEB_CLIENT.SAFARI;
				} else if (pUserAgent.contains("Opera")) {
					xBrowser = WEB_CLIENT.OPERA;
				}
				return xBrowser;
			}
			
			public static String getBrowserVersionFromUserAgent(String pUserAgent) {
				String xBrowserVer = pUserAgent;
				xBrowserVer = xBrowserVer.substring(xBrowserVer.lastIndexOf(")")+2, xBrowserVer.lastIndexOf(" "));
				return xBrowserVer;
			}
		}

		public enum DEVICE {
			
			UNKNOW 				("Unknow", 			""),
			MACINTOSH 			("Mac", 				"Macintosh"),
			PHONE 				("Phone", 			"Phone"),
			DROID 				("Droid", 			"DROID"),
			ANDROID 				("Android", 			"Android"),
			WEBOS 				("webOS", 			"webOS"),
			IPHONE 				("iPhone", 			"iPhone"),
			IPOD 				("iPod", 			"iPod"),
			IPAD 				("iPad", 			"iPad"),
			BLACKBERRY 			("BlackBerry", 		"BlackBerry"),
			WINDOWS_PHONE 		("Windows Phone", 	"Windows Phone"),
			ZUNE_WP7 			("ZuneWP7", 			"ZuneWP7"),
			IE_MOBILE 			("IEMobile", 		"IEMobile"),
			TABLET 				("Tablet", 			"Tablet"),
			KINDLE 				("Amazon Kindle", 	"Kindle"),
			PLAYBOOK 			("Playbook", 		"Playbook"),
			GOOGLE_NEXUS 		("Google Nexus", 	"Nexus"),
			MOTOROLA_XOOM 		("Motorola Xoom", 	"Xoom"),
			SAMSUNG_NOTE 		("Samsung Note", 	"SAMSUNG-SGH-I717"),
			SAMSUNG_NOTE_2 		("Samsung Note 2", 	"GT-N7100"),
			SAMSUNG_NOTE_3 		("Samsung Note 3", 	"SM-N900T"),
			SAMSUNG_TAB_4 		("Samsung Tab 4", 	"SM-T330NU");
			
			private String 	wName;
			private String 	wUserAgent;
			public String getName() {return wName;}
			public String getUserAgent() {return wUserAgent;}
			
			private DEVICE(String pName, String pUserAgent) {
				wName = pName;
				wUserAgent = pUserAgent;
			}
			
			public static DEVICE get(Integer pCode) {
				if (DBSObject.isNull(pCode)) {
					return null;
				}
				switch (pCode) {
				case 0:
					return UNKNOW;
				case 1:
					return MACINTOSH;
				case 2:
					return PHONE;
				case 3:
					return DROID;
				case 4:
					return ANDROID;
				case 5:
					return WEBOS;
				case 6:
					return IPHONE;
				case 7:
					return IPOD;
				case 8:
					return IPAD;
				case 9:
					return BLACKBERRY;
				case 10:
					return WINDOWS_PHONE;
				case 11:
					return ZUNE_WP7;
				case 12:
					return IE_MOBILE;
				case 13:
					return TABLET;
				case 14:
					return KINDLE;
				case 15:
					return PLAYBOOK;
				case 16:
					return GOOGLE_NEXUS;
				case 17:
					return MOTOROLA_XOOM;
				case 18:
					return SAMSUNG_NOTE;
				case 19:
					return SAMSUNG_NOTE_2;
				case 20:
					return SAMSUNG_NOTE_3;
				case 21:
					return SAMSUNG_TAB_4;
				default:
					return UNKNOW;
				}
			}
			
			public static String getDeviceNameFromUserAgent(String pUserAgent) {
				return getDeviceFromUserAgent(pUserAgent).wName;
			}
			
			public static DEVICE getDeviceFromUserAgent(String pUserAgent) {
				if (pUserAgent.contains("Macintosh")) {
					return MACINTOSH;
				} else if (pUserAgent.contains("Phone")) {
					return PHONE;
				} else if (pUserAgent.contains("DROID")) {
					return DROID;
				} else if (pUserAgent.contains("Android")) {
					return ANDROID;
				} else if (pUserAgent.contains("webOS")) {
					return WEBOS;
				} else if (pUserAgent.contains("iPhone")) {
					return IPHONE;
				} else if (pUserAgent.contains("iPod")) {
					return IPOD;
				} else if (pUserAgent.contains("BlackBerry")) {
					return BLACKBERRY;
				} else if (pUserAgent.contains("Windows Phone")) {
					return WINDOWS_PHONE;
				} else if (pUserAgent.contains("ZuneWP7")) {
					return ZUNE_WP7;
				} else if (pUserAgent.contains("IEMobile")){ 
					return IE_MOBILE;
					
				//touch/tablet detection
				} else if (pUserAgent.contains("Tablet")) {
					return TABLET;
				} else if (pUserAgent.contains("iPad")) {
					return IPAD;
				} else if (pUserAgent.contains("Kindle")) {
					return KINDLE;
				} else if (pUserAgent.contains("Playbook")) {
					return PLAYBOOK;
				} else if (pUserAgent.contains("Nexus")) {
					return GOOGLE_NEXUS;
				} else if (pUserAgent.contains("Xoom")) {
					return MOTOROLA_XOOM;
				} else if (pUserAgent.contains("SM-N900T")) { //Samsung Note 3
					return SAMSUNG_NOTE_3;
				} else if (pUserAgent.contains("GT-N7100")) { //Samsung Note 2
					return SAMSUNG_NOTE_2;
				} else if (pUserAgent.contains("SAMSUNG-SGH-I717")) { //Samsung Note
					return SAMSUNG_NOTE;
				} else if (pUserAgent.contains("SM-T330NU")){ //Samsung Tab 4
					return SAMSUNG_TAB_4;
				}
				return UNKNOW;
			}
		}
	}

	public static class COLUMN {
	    public enum VERTICALALIGNMENT{
			TOP, //
			CENTER, //
			BOTTON; //
		}

		public enum HORIZONTALALIGNMENT {
			LEFT, //
			CENTER, //
			RIGHT; //
	    }
	}

	
	public static final class IO {

		public static final String VERSION_COLUMN_NAME = "VERSION"; //Nome da coluna que deverá existir na tabela caso queira efetuar o controle de lock otimista;)
		
		public static enum DATATYPE {
	        NONE 	(Object.class),   	//Tipo NÃO definido
	        STRING	(String.class),   	//Tipo String, quando vazio(""), converte para Null.       
	        DECIMAL	(BigDecimal.class), //Tipo Decimal
	        INT		(Integer.class),	//Tipo Inteiro
	        DOUBLE	(Double.class),		//Tipo Double
	        DATE	(Date.class),     	//Tipo de dado de Data, contendo somente a data. Desprezando hora, se hourver
	        DATETIME(Timestamp.class), 	//Tipo de dado de Data, contendo data e hora, inclui da hora,minuto e segundo zerado se NÃO informado
	        TIME	(Time.class),     	//Tipo de dado de hora, contendo somente a data. Desprezando hora, se hourver
	        BOOLEAN	(Boolean.class),  	//Tipo boleano, onde True=-1 e False=0
	        COMMAND	(String.class),  	//NÃO faz qualquer conversão do dado
	        PICTURE	(Object.class),  	//Imagem
	        ID		(Long.class);       //Tipo numérico utilizadao como chave. 0(zero) ou -1 será convertido para Null
	    
			Class<?> wJavaClass;
			
			DATATYPE (Class<?> pJavaClass){
				wJavaClass = pJavaClass;
			}
			
			public Class<?> getJavaClass(){
				return wJavaClass;
			}
			
		}


	    public static enum DB_SERVER{
	    	ORACLE,
	    	SQLSERVER,
	    	SYBASE,
	    	MYSQL,
	    	ACCESS,
	    	DB2,
	    	POSTGRESQL,
	    	FIREBIRD,
	    	INGRE,
	    	APACHEDERBY,
	    	SQLLITE
	    }
	}
	
	/**
	 * Procura por um nó recursivamente que inicie com o valor do atributo informado
	 * @param pNodes
	 * @param pAttributeName
	 * @param pAttributeValue
	 * @return
	 */
	public static Node NodeListFindNode(NodeList pNodes, String pAttributeName, String pAttributeValue){
		Node xNodeReturn = null;
		for (int xI=0; xI < pNodes.getLength() -1; xI++){
			Node xNode = pNodes.item(xI);
			if (xNode.getAttributes() != null){
				Node xNodeAtt = xNode.getAttributes().getNamedItem(pAttributeName);
				if (xNodeAtt != null){
					String xValue = xNodeAtt.getNodeValue();
					if (xValue.startsWith(pAttributeValue)){
						return xNode;
					}
				}
			}
			xNodeReturn = NodeListFindNode(xNode.getChildNodes(), pAttributeName, pAttributeValue);
			if (xNodeReturn != null){
				return xNodeReturn;
			}
		}
		return null;
	}
}


//package org.agoncal.sample.javaee.jbossutil;
//
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.client.HttpClient;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.jboss.resteasy.client.jaxrs.ResteasyClient;
//import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
//import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
//
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
///**
// * This class allows to "dialog" with the JBoss administration API through HTTP. To administer JBoss we can either,
// * use the CLI ($JBOSS_HOME/bin/jboss-cli.sh), a browser or curl. For example, to know the status of the JBoss server
// * JBoss (running) we can :
// * <ul>
// * <li>Cli : :read-attribute(name=server-state)</li>
// * <li>Browser : http://localhost:9990/management?operation=attribute&name=server-state</li>
// * <li>cUrl : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type: application/json" -d  '{"operation":"read-attribute","name":"server-state","json.pretty":1}'</li>
// * </ul>
// * <p/>
// * This utility class sends HTTP requests to the JBoss administration REST APIs and to know if JBoss is up and running, if the web application (war/ear) is
// * deployed, if the server is listening on an HTTP port.... For this class to work, it needs to connect to a JBoss in localhost on
// * port 9990 in DIGEST mode. User and password are set on variables JBOSS_ADMIN_USER and JBOSS_ADMIN_PASSWORD.
// */
//public class JBossUtil {
//
//	public static final String JBOSS_ADMIN_USER = "admin";
//	public static final String JBOSS_ADMIN_PASSWORD = "admin";
//	public static final String JBOSS_MANAGEMENT_URL = "http://localhost:9990/management";
//
//	/**
//	 * This method returns a connected HTTP client (user/password). This HTTP client is then used in all the following methods.
//	 *
//	 * @return A RestEasy HTTP client
//	 */
//	private static ResteasyClient getClient() {
//		// Setting digest credentials
//		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(JBOSS_ADMIN_USER, JBOSS_ADMIN_PASSWORD);
//		credentialsProvider.setCredentials(AuthScope.ANY, credentials);
//		HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
//		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpclient, true);
//
//		// Creating HTTP client
//		return new ResteasyClientBuilder().httpEngine(engine).build();
//	}
//
//
//	// Used to test this class
//	public static void main(String[] args) {
//		System.out.println(isJBossUpAndRunning());
//		System.out.println(isJBoss620EAP());
//		System.out.println(isWebappDeployed("myWay"));
//		System.out.println(isDatasourceDeployed("myDS"));
//		System.out.println(isHTTPListenerOk());
//	}
//
//	/**
//	 * This method checks that JBoss is up and running by doing an HTTP call and checking the body returns "running". This method is equivalent to the following command :
//	 * <p/>
//	 * <ul>
//	 * <li>Cli :   :read-attribute(name=server-state)</li>
//	 * <li>Browser : http://localhost:9990/management?operation=attribute&name=server-state</li>
//	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"operation":"read-attribute","name":"server-state","json.pretty":1}'</li>
//	 * </ul>
//	 */
//	public static boolean isJBossUpAndRunning() {
//
//		Response response;
//		try {
//			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).queryParam("operation", "attribute").queryParam("name", "server-state");
//			response = target.request(MediaType.APPLICATION_JSON).get();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("running");
//
//	}
//
//	/**
//	 * This method checks that JBoss is version "6.2.0.GA". This method is equivalent to the following command :
//	 * <p/>
//	 * <ul>
//	 * <li>CLI :   :read-attribute(name=product-version)</li>
//	 * <li>Browser : http://localhost:9990/management/?operation=attribute&name=product-version</li>
//	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"operation":"read-attribute","name":"product-version","json.pretty":1}'</li>
//	 * </ul>
//	 */
//	public static boolean isJBoss620EAP() {
//
//		Response response;
//		try {
//			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).queryParam("operation", "attribute").queryParam("name", "product-version");
//			response = target.request(MediaType.APPLICATION_JSON).get();
//		} catch (Exception e) {
//			return false;
//		}
//
//		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("6.2.0.GA");
//	}
//
//
//	/**
//	 * This method checks that the war/ear is deployed. This method is equivalent to the following command :
//	 * <p/>
//	 * <ul>
//	 * <li>CLI     : /deployment=sampleJavaEEJBossUtil.war:read-attribute(name=status)</li>
//	 * <li>Browser : http://localhost:9990/management/deployment/sampleJavaEEJBossUtil.war?operation=attribute&name=status</li>
//	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"address":[{"deployment":"sampleJavaEEJBossUtil.war"}],"operation":"read-attribute","name": "enabled","json.pretty":1}'</li>
//	 * </ul>
//	 */
//	public static boolean isWebappDeployed(String warName) {
//
//		Response response;
//		try {
//			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).path("deployment").path(warName).queryParam("operation", "attribute").queryParam("name", "status");
//			response = target.request(MediaType.APPLICATION_JSON).get();
//		} catch (Exception e) {
//			return false;
//		}
//
//		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("OK");
//	}
//
//	/**
//	 * This method checks that the datasource is enabled. This method is equivalent to the following command :
//	 * <p/>
//	 * <ul>
//	 * <li>CLI     : /subsystem=datasources/data-source=ExampleDS:read-attribute(name=enabled)</li>
//	 * <li>Browser : http://localhost:9990/management/subsystem/datasources/data-source/ExampleDS?operation=attribute&name=enabled</li>
//	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"address":["subsystem","datasources","data-source","ExampleDS"], "operation":"read-attribute","name": "enabled","json.pretty":1}'</li>
//	 * </ul>
//	 */
//	public static boolean isDatasourceDeployed(String datasourceName) {
//
//		Response response;
//		try {
//
//			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).path("subsystem").path("datasources").path("data-source").path(datasourceName).queryParam("operation", "attribute").queryParam("name", "enabled");
//			response = target.request(MediaType.APPLICATION_JSON).get();
//		} catch (Exception e) {
//			return false;
//		}
//
//		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("true");
//	}
//
//	/**
//	 * This method checks that the HTTP listener is ok. This method is equivalent to the following command :
//	 * <p/>
//	 * <ul>
//	 * <li>CLI     : /subsystem=web/connector=http:read-attribute(name=enabled)</li>
//	 * <li>Browser : http://localhost:9990/management/subsystem/web/connector/http?operation=attribute&name=enabled</li>
//	 * <li>Curl    : curl --digest 'http://user:password@localhost:9990/management' --header "Content-Type:application/json" -d '{"address":["subsystem","web","connector","http"], "operation":"read-attribute", "name":"enabled","json.pretty":1}'</li>
//	 * </ul>
//	 */
//	public static boolean isHTTPListenerOk() {
//
//		Response response;
//		try {
//
//			WebTarget target = getClient().target(JBOSS_MANAGEMENT_URL).path("subsystem").path("web").path("connector").path("http").queryParam("operation", "attribute").queryParam("name", "enabled");
//			response = target.request(MediaType.APPLICATION_JSON).get();
//		} catch (Exception e) {
//			return false;
//		}
//
//		return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("true");
//	}
//}