package br.com.dbsoft.core;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;


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
			UNIX			("Unix OS Based", 			"x11"),
			ANDROID 		("Android OS", 				"android"),
			BADA			("Bada", 					"bada"),
			BEOS			("Beos", 					"beos"),
			BLACKBERRY_OS	("Blackberry OS", 			"blackberry, bb"),
			CHROME_OS		("Chrome OS", 				"cros"), //X11
			DARWIN			("Darwin", 					"darwin"),
			FIRE_OS			("Fire OS", 				"kindle fire, kf"),
			FREE_BSD		("FreeBSD", 				"freebsd"),
			HAIKU			("Haiku", 					"haiku"),
			HP_WEBOS		("HP webOS", 				"hpwos"),
			IOS 			("iOS", 					"iphone, ipod, ipad"),
			IRIX			("Irix", 					"irix"),			
			LINUX 			("Linux OS", 				"linux"),
			LIVEAREA		("Livearea", 				"playstation vita"),
			MAC_OS 			("Mac OS", 					"mac, macos x "),
			OPEN_BSD		("OpenBSD", 				"openbsd"),
			RIM_OS 			("RIM", 					"rim tablet os"),
			SUNOS			("SunOS", 					"sunos"),
			SYMBIAN 		("Symbian", 				"symbianos"),
			WEBOS 			("webOS", 					"webos"),
			WINDOWS 		("Microsoft Windows", 		"windows"),
			WINDOWSPHONE 	("Microsoft WindowsPhone", 	"windows phone");
			
			private String 	wName;
			private String 	wUserAgent;
			public String 	getName() {return wName;}
			public String 	getUserAgent() {return wUserAgent;}
			
			private OS(String pName, String pUserAgent) {
				wName = pName;
				wUserAgent = pUserAgent;
			}
			
			public static OS get(Integer pCode) {
				if (DBSObject.isNull(pCode)) {
					return null;
				}
				switch (pCode) {
					case 0:
						return UNIX;
					case 1:
						return ANDROID;
					case 2:
						return BADA;
					case 3:
						return BEOS;
					case 4:
						return BLACKBERRY_OS;
					case 5:
						return CHROME_OS;
					case 6:
						return DARWIN;
					case 7:
						return FIRE_OS;
					case 8:
						return FREE_BSD;
					case 9:
						return HAIKU;
					case 10:
						return HP_WEBOS;
					case 11:
						return IOS;
					case 12:
						return IRIX;		
					case 13:
						return LINUX;
					case 14:
						return LIVEAREA;
					case 15:
						return MAC_OS;
					case 16:
						return OPEN_BSD;
					case 17:
						return RIM_OS;
					case 18:
						return SUNOS;
					case 19:
						return SYMBIAN;
					case 20:
						return WEBOS;
					case 21:
						return WINDOWS;
					case 22:
						return WINDOWSPHONE;
					default:
						return null;
				}
			}
			
			public static OS getOSFromUserAgent(String pUserAgent) {
				OS xOS = null;
				String xUserAgent = pUserAgent.toLowerCase();
				if (DBSString.contains(xUserAgent, DBSString.toArrayList(BADA.wUserAgent, ","))) {
					return BADA;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(BEOS.wUserAgent, ","))) {
					return BEOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(BLACKBERRY_OS.wUserAgent, ","))) {
					return BLACKBERRY_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(CHROME_OS.wUserAgent, ","))) {
					return CHROME_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(DARWIN.wUserAgent, ","))) {
					return DARWIN;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(FIRE_OS.wUserAgent, ","))) {
					return FIRE_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(FREE_BSD.wUserAgent, ","))) {
					return FREE_BSD;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(HAIKU.wUserAgent, ","))) {
					return HAIKU;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(HP_WEBOS.wUserAgent, ","))) {
					return HP_WEBOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IRIX.wUserAgent, ","))) {
					return IRIX;		
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(LIVEAREA.wUserAgent, ","))) {
					return LIVEAREA;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(OPEN_BSD.wUserAgent, ","))) {
					return OPEN_BSD;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(RIM_OS.wUserAgent, ","))) {
					return RIM_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(SUNOS.wUserAgent, ","))) {
					return SUNOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(SYMBIAN.wUserAgent, ","))) {
					return SYMBIAN;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WEBOS.wUserAgent, ","))) {
					return WEBOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WINDOWSPHONE.wUserAgent, ","))) {
					return WINDOWSPHONE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WINDOWS.wUserAgent, ","))) {
					return WINDOWS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IOS.wUserAgent, ","))) {
					return IOS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(MAC_OS.wUserAgent, ","))) {
					return MAC_OS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(ANDROID.wUserAgent, ","))) {
					return ANDROID;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(LINUX.wUserAgent, ","))) {
					return LINUX;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(UNIX.wUserAgent, ","))) {
					return UNIX;
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
				if (DBSObject.isEmpty(pUserAgent)) {return "";}
				String xBrowserVer = pUserAgent;
				if (xBrowserVer.contains("Chrome")) {
					//Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36
					xBrowserVer = xBrowserVer.substring(xBrowserVer.lastIndexOf("Chrome"), xBrowserVer.lastIndexOf(" "));
				} else if (xBrowserVer.contains("Firefox")) {
					//Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:59.0) Gecko/20100101 Firefox/59.0
					xBrowserVer = xBrowserVer.substring(xBrowserVer.lastIndexOf(" ")+1);
				} else if (xBrowserVer.contains("Safari") && xBrowserVer.contains("Version")) {
					//Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/604.5.6 (KHTML, like Gecko) Version/11.0.3 Safari/604.5.6
					xBrowserVer = xBrowserVer.substring(xBrowserVer.lastIndexOf(")")+2, xBrowserVer.lastIndexOf(" "));
				} else {
					//iPhone //Mozilla/5.0 (iPhone; CPU iPhone OS 11_2 like Mac OS X) AppleWebKit/604.4.7 (KHTML, like Gecko) Mobile/15C107
					//Android //Mozilla/5.0 (Linux; Android 7.1.1; Moto Z2 Play Build/NPSS26.118-19-1-6; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/65.0.3325.109 Mobile Safari/537.36
					xBrowserVer = xBrowserVer.substring(xBrowserVer.lastIndexOf(")")+2);
				}
				return xBrowserVer;
			}
		}

		public enum DEVICE {
			UNKNOW 				("Unknow", 				"", 										false),
			MACINTOSH 			("Mac", 				"macintosh", 								false),
			TV_LG				("TV LG", 				"webos", 									false),
			TV					("TV", 					"smart-tv, smarttv, googletv, philipstv", 	false),
			ZUNE 				("Zune", 				"zune", 									true),
			IE_MOBILE 			("IEMobile", 			"iemobile", 								true),
			TABLET 				("Tablet", 				"tablet", 									true),
			KINDLE 				("Amazon Kindle", 		"kindle, kf", 								true),
			PLAYBOOK 			("Playbook", 			"playbook", 								true),
			GOOGLE_NEXUS 		("Google Nexus", 		"nexus", 									true),
			MOTOROLA_XOOM 		("Motorola Xoom", 		"xoom", 									true),
			IPOD 				("iPod", 				"ipod", 									true),
			IPAD 				("iPad", 				"ipad", 									true),
			IPHONE 				("iPhone", 				"iphone", 									true),
			BLACKBERRY 			("BlackBerry", 			"blackberry, bb", 							true),
			WINDOWS_PHONE 		("Windows Phone", 		"windows phone", 							true),
			WINDOWS 			("Windows", 			"windows", 									false),
			GENREIC_ANDROID 	("Generic Android", 	"android", 									true);
			
			private String 		wName;
			private String 		wUserAgent;
			private boolean 	wMobile;
			public String 	getName() {return wName;}
			public String 	getUserAgent() {return wUserAgent;}
			public boolean 	isMobile() {return wMobile;}
			
			private DEVICE(String pName, String pUserAgent, boolean pMobile) {
				wName = pName;
				wUserAgent = pUserAgent;
				wMobile = pMobile;
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
					return TV_LG;
				case 3:
					return TV;
				case 4:
					return ZUNE;
				case 5:
					return IE_MOBILE;
				case 6:
					return TABLET;
				case 7:
					return KINDLE;
				case 8:
					return PLAYBOOK;
				case 9:
					return GOOGLE_NEXUS;
				case 10:
					return MOTOROLA_XOOM;
				case 11:
					return IPOD;
				case 12:
					return IPAD;
				case 13:
					return IPHONE;
				case 14:
					return BLACKBERRY;
				case 15:
					return WINDOWS_PHONE;
				case 16:
					return WINDOWS;
				case 17:
					return GENREIC_ANDROID;
				default:
					return UNKNOW;
				}
			}
			
			public static String getDeviceNameFromUserAgent(String pUserAgent) {
				return getDeviceFromUserAgent(pUserAgent).wName;
			}
			
			public static DEVICE getDeviceFromUserAgent(String pUserAgent) {
				String xUserAgent = pUserAgent.toLowerCase();
				if (DBSString.contains(xUserAgent, DBSString.toArrayList(MACINTOSH.getUserAgent(), ","))) {
					return MACINTOSH;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(TV_LG.getUserAgent(), ","))) {
					return TV_LG;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(TV.getUserAgent(), ","))) {
					return TV;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(ZUNE.getUserAgent(), ","))) {
					return ZUNE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IE_MOBILE.getUserAgent(), ","))) {
					return IE_MOBILE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(TABLET.getUserAgent(), ","))) {
					return TABLET;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(KINDLE.getUserAgent(), ","))) {
					return KINDLE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(PLAYBOOK.getUserAgent(), ","))) {
					return PLAYBOOK;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(GOOGLE_NEXUS.getUserAgent(), ","))) {
					return GOOGLE_NEXUS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(MOTOROLA_XOOM.getUserAgent(), ","))) {
					return MOTOROLA_XOOM;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IPOD.getUserAgent(), ","))) {
					return IPOD;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IPAD.getUserAgent(), ","))) {
					return IPAD;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(IPHONE.getUserAgent(), ","))) {
					return IPHONE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(BLACKBERRY.getUserAgent(), ","))) {
					return BLACKBERRY;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WINDOWS_PHONE.getUserAgent(), ","))) {
					return WINDOWS_PHONE;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(WINDOWS.getUserAgent(), ","))) {
					return WINDOWS;
				} else if (DBSString.contains(xUserAgent, DBSString.toArrayList(GENREIC_ANDROID.getUserAgent(), ","))) {
					return GENREIC_ANDROID;
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