package br.com.dbsoft.core;


public final class DBSSDK {
	public static final String DOMAIN = "br.com.dbsoft";
	
	public static final int VERDADEIRO = -1;
	public static final int FALSO = 0;
	
	public final static class UI
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
		
		public final static class COMBOBOX{
			public final static String NULL_VALUE = ""; //Valor do item null na lista. Para mantém conformidade com o JSF que por não se possível enviar NULL, irá considerar o valor 'vázio' como nulo.
			
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

	public final static class ENCODE{
		public static final String US_ASCII = "US-ASCII";
		public static final String UTF_8 = "UTF-8";
		public static final String ISO_8859_1 = "ISO-8859-1";
		public static final String ISO_8859_6 = "ISO-8859-6";
	}
	
	public final static class CONTENT_TYPE{
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
	
	
	public final static class FILE{
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
	
	public final static class NETWORK{
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
	}
	
	public final static class TABLE {
		public static String FERIADO = "";
	}
	


	public final static class JDBC_DRIVER {
	    public static final String MYSQL = "com.mysql.jdbc.Driver";
	    public static final String ORACLE = "oracle.jdbc.driver.OracleDriver";
	}
	
	public final static class APP_SERVER_PROPERTY{
		public final static class PATH{
			public static final String JBOSS = "jboss.server.base.dir";
		}
	}
	
	public final static class SYS {
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
		}

		public enum APP_CLIENT {
			WEB,
			DOTNET,
			OBJC,
			JAVA;
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

	
	public final static class IO {

		public static final String VERSION_COLUMN_NAME = "VERSION"; //Nome da coluna que deverá existir na tabela caso queira efetuar o controle de lock otimista;)
		
		public static enum DATATYPE {
	        NONE,     //Tipo NÃO definido
	        STRING,   //Tipo String, quando vazio(""), converte para Null.       
	        DECIMAL,  //Tipo Decimal
	        INT,	  //Tipo Inteiro
	        DOUBLE,   //Tipo Double
	        DATE,     //Tipo de dado de Data, contendo somente a data. Desprezando hora, se hourver
	        DATETIME, //Tipo de dado de Data, contendo data e hora, inclui da hora,minuto e segundo zerado se NÃO informado
	        TIME,     //Tipo de dado de hora, contendo somente a data. Desprezando hora, se hourver
	        BOOLEAN,  //Tipo boleano, onde True=-1 e False=0
	        COMMAND,  //NÃO faz qualquer conversão do dado
	        PICTURE,  //Imagem
	        ID;       //Tipo numérico utilizadao como chave. 0(zero) ou -1 será convertido para Null
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