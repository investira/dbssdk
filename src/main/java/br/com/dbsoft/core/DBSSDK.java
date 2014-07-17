package br.com.dbsoft.core;


public final class DBSSDK {
	public static final String DOMAIN = "br.com.dbsoft";
	
	public static final int VERDADEIRO = -1;
	public static final int FALSO = 0;
	
	public final static class UI
	{
		public final static class PREFIX{
			public static final String CRUD = "fl";
			public static final String FILTER = "ft";
			public static final String AUX = "fx";
			public static final String MENU = "mn";
			public static final String BUTTON = "bt";
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
	
	public final static class FILE
	{
		public final static class ENCODE{
			public static final String US_ASCII = "US-ASCII";
			public static final String UTF_8 = "UTF-8";
			public static final String ISO_8859_1 = "ISO-8859-1";
			public static final String ISO_8859_6 = "ISO-8859-6";
		}
		
		public enum PROTOCOL {
			HTTP,
			HTTPS,
			SSH,
			SFTP,
			FTP,
			FTPS,
			UDP;
		}	
		
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
			WINDOWS,
			WINDOWSPHONE,
			MACOS,
			IOS,
			ANDROID,
			RIM,
			LINUX,
			WEBOS,
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
