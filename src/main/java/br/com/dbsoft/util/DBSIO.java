package br.com.dbsoft.util;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import javax.sql.DataSource;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;

import br.com.dbsoft.annotation.DBSTableModel;
import br.com.dbsoft.core.DBSSDK.IO.DATATYPE;
import br.com.dbsoft.core.DBSSDK.IO.DB_SERVER;
import br.com.dbsoft.core.DBSSDK.UI.COMBOBOX;
import br.com.dbsoft.core.DBSSDK.UI.COMBOBOX.NULL_TEXT;
import br.com.dbsoft.error.DBSError;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.io.DBSColumn;
import br.com.dbsoft.io.DBSDAO;
import br.com.dbsoft.io.DBSDAO.COMMAND;
import br.com.dbsoft.io.DBSResultDataModel;
import br.com.dbsoft.util.DBSFormat.MASK;

/**
 * @author ricardo.villar
 *
 */
public class DBSIO{

	protected static Logger		wLogger = Logger.getLogger(DBSIO.class);
	

	public static String[] 		COLUMNS_IGNORED = new String[]{
													"DSC_TRILHA_AUDIT",
													"NOM_USU_INCL",
													"DAT_INCL",
													"NOM_USU_ALTER",
													"DAT_ALTER",
													};
	
	public static String		LOCK_FOR_UPDATE = " FOR UPDATE";
	
	public static enum MOVE_DIRECTION{
		BEFORE_FIRST,
		FIRST,
    	PREVIOUS,
    	NEXT,
    	LAST;
    } 
	
	public static enum SORT_DIRECTION {
		ASCENDING 			("A", "ASC", " -i_sort_asc"),
		DESCENDING			("D", "DESC", " -i_sort_des"),
		NONE 				("", "", " -i_sort");
		
		private String 	wCode;
		private String 	wSQL;
		private String 	wIcon;
		
		private SORT_DIRECTION(String pCode, String pSQL, String pIcon) {
			this.wCode = pCode;
			this.wSQL = pSQL;
			this.wIcon = pIcon;
		}

		public String getCode() {
			return wCode;
		}

		public String getSQL() {
			return wSQL;
		}
		
		public String getIcon() {
			return wIcon;
		}
		public static SORT_DIRECTION get(String pName) {
			pName = DBSObject.getNotEmpty(pName, " ").toUpperCase().substring(0, 1);
			switch (pName) {
			case "A":
				return ASCENDING;
			case "D":
				return DESCENDING;
			default:
				return NONE;
			}
		}
	}
	
	
	//ABRINDO conexão - DIRETA		
	//	try {
	//		Class.forName("com.mysql.jdbc.Driver").newInstance(); 
	//		wsCn = DBSIO.getConnection("jdbc:mysql://localhost:3306/dbsweb", "usuario","senha");
	//	} catch (Exception e) {
	//		throw new RuntimeException (e);
	//	}		
	//ABRINDO conexão - CONECTION POOL	
	//	try {
	//		InitialContext xIC = new InitialContext();
	//		wsDS = (DataSource) xIC.lookup("java:/dbsweb");
	//	} catch (NamingException e) {
	//		e.printStackTrace();
	//	}
	//	wsCn = wsDS.getConnection();
	
	//ABRINDO conexão - INJECT
	//@Resource(mappedName="dbsweb")
	//private DataSource wsDS;
	//private Connection wsCn;
	//private DBSDAO wsDao;
	//wsCn = wsDS.getConnection();
	//wsDao = new DBSDAO(wsCn);
	/**
	 * Retorna o banco de dados utilizado
	 * @param pCn conexão a partir do qual serãoverificado o fabricante do bando de dados
	 * @return Fabricante a partir de enDataBaseProduct
	 * @throws DBSIOException 
	 */
	public static DB_SERVER getDataBaseProduct(Connection pCn) {
		if (pCn == null){
			return null;
		}
		String xDBPN;
		try {
			xDBPN = pCn.getMetaData().getDatabaseProductName().toUpperCase(); 
		} catch (SQLException e) {
			wLogger.error("Fabrincante do banco de dados não encontrado", e);
			return null;
		}
		if (xDBPN.equals("ORACLE")){
			return DB_SERVER.ORACLE;
		}else if (xDBPN.equals("MICROSOFT SQL SERVER")){
			return DB_SERVER.SQLSERVER;
		}else if (xDBPN.equals("MYSQL")){
			return DB_SERVER.MYSQL;
		}else{
			return null;
		}
	}
	/**
	 * Faz conexão com o banco de dados através do DataSource informado.
	 * @param pDS
	 * @return true = Sem erro ; false = Com erro
	 * @throws DBSIOException 
	 */	
	public static Connection openConnection(DataSource pDS) throws DBSIOException{
		return openConnection(pDS, 0, null, null);
	}

	/**
	 * Faz conexão com o banco de dados através do DataSource informado.
	 * @param pDS
	 * @param pTimeout
	 * @return true = Sem erro ; false = Com erro
	 * @throws DBSIOException 
	 */
	public static Connection openConnection(DataSource pDS, int pTimeout) throws DBSIOException{
		return openConnection(pDS, pTimeout, null, null);
	}
	
	/**
	 * Faz conexão com o banco de dados através do DataSource informado.
	 * @param pDS
	 * @param pTimeout
	 * @param pUserName Nome do usuário
	 * @param pUserPassword Senha do usuário
	 * @return true = Sem erro ; false = Com erro
	 * @throws DBSIOException 
	 */
	public static Connection openConnection(DataSource pDS, int pTimeout, String pUserName, String pUserPassword) throws DBSIOException{
		boolean xOk = false;
		int xI = 0;
		Connection xCn = null;
		while (!xOk){
			xI ++;
			try {
				pDS.setLoginTimeout(0);// 0 = Usa timeout do servidor
				if (pUserName != null
				 && pUserPassword != null){
					xCn = pDS.getConnection(pUserName, pUserPassword);
				}else{
					xCn = pDS.getConnection();
				} 
				xCn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); 
				xCn.setAutoCommit(false);
				return xCn;
//			} catch (SQLException e) {
//				wLogger.error("Não foi possível criar conexão:" + e.getLocalizedMessage());
			} catch (Throwable e) {
				wLogger.error("Error getConnection:" + e.getLocalizedMessage()); 
				pvGetConnectionTimeout(xCn, e, pTimeout, xI); 
			}
		}
		return null;
	}

	/**
	 * Faz conexão direta com o banco de dados
	 * @param pConnectionString String de conexão
	 * @param pUserName Nome do usuário
	 * @param pUserPassword Senha do usuário
	 * @return true = Sem erro ; false = Com erro
	 * @throws DBSIOException 
	 */
	public static Connection openConnection(String pConnectionString, String pUserName, String pUserPassword) throws DBSIOException{
	    // Força o load dos JDBC drivers.
	    Enumeration<Driver> xD = java.sql.DriverManager.getDrivers();
	    while (xD.hasMoreElements()) {
	    	@SuppressWarnings("unused")
			Object driverAsObject = xD.nextElement();
	      //System.out.println("JDBC Driver=" + driverAsObject);
	    }
	
		Connection xCn = null;
		try {
			xCn = DriverManager.getConnection(pConnectionString, pUserName, pUserPassword);
			xCn.setAutoCommit(false);
			xCn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			return xCn; 
		} catch (SQLException e) {
			throwIOException(e);
			return null;
		}
	}
	/**
	 * Fecha a conexão com o banco de dados.<br/>
	 * Ignora se conexão estiver fechada.
	 * @param pCn conexão a ser fechada.
	 * @return true = Sem erro / false = Com erro
	 * @throws DBSIOException 
	 */
	public static boolean closeConnection(Connection pCn){
//		System.out.println("CLOSE CONNECTION ************************ FIM");
		try{
			if (pCn != null){
				if (!pCn.isClosed()){
					try {
						if (!pCn.getAutoCommit()){ //Se não for autocommit, força o rollback. O close connection é efetuado em entro try pois o getAutocommit pode dar exception e acabar não efetuado o close. Interrupted attempting lock.
							pCn.rollback(); //Força rollback, pois normalmente é efetuado o commit automático quando é fechada a conexão com o banco
						}
					} catch (SQLException e) {
						return false;
					}finally{
						if (!pCn.isClosed()){
							pCn.close();
						}
					}
				}
			}
			return true;
		}catch(SQLException e){
			wLogger.error("DBSIO:closeConnection:rollback", e);
			return false;
		}
	}
	
	/**
	 * Retorna se conexão está aberta.
	 * @param pCn
	 * @return
	 */
	public static boolean isConnectionOpened(Connection pCn){
		try{
			if (pCn != null){
				if (!pCn.isClosed()){
					return true;
				}
			}
		}catch(SQLException e){
			wLogger.error("DBSIO:isConnectionClosed", e);
		}
		return false;
	}
	
	/**
	 * Dispara novo DBSIOException, criado a partir da Exception informada
	 * @param pMessage
	 * @param pException
	 * @param pCn
	 * @throws DBSIOException
	 */
	public static void throwIOException(String pMessage, SQLException pException, Connection pCn) throws DBSIOException{
		SQLException xE = DBSError.getFirstSQLException(pException, pException);
		throw new DBSIOException(pMessage, xE, pCn);
	}

	/**
	 * Dispara novo DBSIOException, criado a partir da Exception informada
	 * @param pException
	 * @param pCn
	 * @throws DBSIOException
	 */
	public static void throwIOException(SQLException pException, Connection pCn) throws DBSIOException{
		SQLException xE = DBSError.getFirstSQLException(pException, pException);
		throw new DBSIOException(xE, pCn);
	}
	
	/**
	 * Dispara novo DBSIOException, criado a partir da Exception informada
	 * @param pException
	 * @param pCn
	 * @throws DBSIOException
	 */
	public static void throwIOException(Throwable pException, Connection pCn) throws DBSIOException{
		SQLException xE = DBSError.getFirstSQLException(pException, pException);
		throw new DBSIOException(xE, pCn);
	}

	/**
	 * Dispara novo DBSIOException, criado a partir da Exception informada
	 * @param pException
	 * @throws DBSIOException
	 */
	public static void throwIOException(SQLException pException) throws DBSIOException{
		SQLException xE = DBSError.getFirstSQLException(pException, pException);
		throw new DBSIOException(xE);
	}

	/**
	 * Dispara novo DBSIOException, criado a partir da Exception informada
	 * @param pMessage
	 * @throws DBSIOException
	 */
	public static void throwIOException(String pMessage) throws DBSIOException{
		throw new DBSIOException(pMessage);
	}
	
	/**
	 * Dispara novo DBSIOException, criado a partir da Exception informada
	 * @param pException
	 * @throws DBSIOException
	 */
	public static void throwIOException(Exception pException) throws DBSIOException{
		throw new DBSIOException(pException);
	}
	/**
	 * Retorna Resulset com o MetaData da Tabela
	 * Nomes válidos das colunas 
	 * TABLE_SCHEM
	 * TABLE_NAME
	 * COLUMN_NAME
	 * TYPE_NAME
	 * COLUMN_DEF
	 * COLUMN_SIZE
	 * DATA_TYPE
	 * DECIMAL_DIGITS
	 * REMARKS
	 * CHAR_OCTET_LENGTH
	 * ORDINAL_POSITION
	 * NULLABLE
	 * @param pCn conexão com o banco de dados
	 * @param pTableName Nome da tabela que se deseja saber o MetaData
	 * @return ResultSet com o MetaData da tabela
	 * @throws DBSIOException 
	 */
	public static ResultSet getTableColumnsMetaData(Connection pCn, String pTableName) throws DBSIOException{
		pTableName = pTableName.trim();
		try {
			DatabaseMetaData xDMD = pCn.getMetaData();
			return  xDMD.getColumns(null, null, pTableName, null);
		} catch (SQLException e) {
			throwIOException(e, pCn);
			return null;
		}
	}

	/**
	 * Retorna o nome da tabela que faz parte da SQL caso exista somente uma.<br/>
	 * Caso exista mais de uma retorna nulo.
	 * @param pSQLQuery Query SQL
	 * @param pReturnAlias Se retorna o nome real da tabela ou o <i>Alias</i> se existir. 
	 * @return
	 */
	public static String getTableFromQuery(String pSQLQuery, boolean pReturnAlias){
		List<String> xT;
		xT = getTablesFromQuery(pSQLQuery, pReturnAlias);
		if (xT.size()==1){
			return xT.get(0).toString();
		}else{
			return null;
		}
	}

	/**
	 * Retorna o nome da tabela que representa o <i>Alias</i> informado, 
	 * ou retorna o nome do <i>Alias</i> que representa a tabela informada.<br/>
	 * Caso não exista <i>Alias</i> será retornardo o mesmo noma da tabela.
	 * @param pSQLQuery Query SQL
	 * @param pReturnAlias Se retorna o nome real da tabela ou o <i>Alias</i> se existir. 
	 * @param pNameToFind Se <b>pReturnAlias=true</b> retorna o nome do <i>Alias</i> que representa a tabela informada em <b>pTableNameToFind</b>. Se tabela não tiver <i>Alias</i>, retorna o próprio nome da tabela.<br/>
	 *   					   Se <b>pReturnAlias=false</b> retorna o nome da tabela que representa o <i>Alias</i> informado em <b>pTableNameToFind</b>.<br/>
	 *   					   Se for nulo, retorna todas as tabelas ou <i>Alias</i> da query.
	 * @return
	 */
	public static String getTableFromQuery(String pSQLQuery, boolean pReturnAlias, String pNameToFind){
		List<String> xT;
		xT = getTablesFromQuery(pSQLQuery, pReturnAlias, pNameToFind);
		if (xT.size()==1){
			return xT.get(0).toString();
		}else{
			return null;
		}
	}

	/**
	 * Retorna nomes da PK de uma tabela
	 * @param pCn conexão com o banco de dados
	 * @param pTableName Nome da tabela que se deseja saber as PKs
	 * @return String com nos nomes das PKs, separadas por vírgula
	 * @throws DBSIOException 
	 */
	public static List<String> getPrimaryKeys(Connection pCn, String pTableName) throws DBSIOException{
		pTableName = pTableName.trim();
		DatabaseMetaData xDMD = null;
		ResultSet xRS = null;
		List<String> xPKs = null;
		try {
			xDMD = pCn.getMetaData();
			if (getDataBaseProduct(pCn) == DB_SERVER.SQLSERVER) {
				xRS = xDMD.getPrimaryKeys(pCn.getCatalog(), "DBSFND", pTableName);
			}else {
				xRS = xDMD.getPrimaryKeys(pCn.getCatalog(), pCn.getCatalog(), pTableName);
			}
			xPKs = new ArrayList<String>();
			while (xRS.next()) {
				if (!xPKs.contains(xRS.getString("COLUMN_NAME"))){
					xPKs.add(xRS.getString("COLUMN_NAME"));
				}
			}
			xDMD = null;
			closeResultSet(xRS);
			return xPKs;
		} catch (SQLException e) {
			xPKs = null;
			wLogger.error("getPrimaryKeys", e);
			return xPKs;
		} 
	}
	

	/**
	 * Recupera e 'locka' os registros a partir de uma Query SQL, utilizando a conexão com o banco
	 * @param pCn conexão a ser utilizada para executa a Query
	 * @param pQuerySQL Query a ser executada
	 * @return ResultSet com os registros
	 * @throws DBSIOException 
	 */	
	public static boolean lockRecord(Connection pCn, String pQuerySQL) throws DBSIOException{
		//Remove comandos 'For update' e 'Nowait' caso já tenha sido incluido
		pQuerySQL = DBSString.changeStr(pQuerySQL, " FOR UPDATE", "", false);
		pQuerySQL = DBSString.changeStr(pQuerySQL, " NOWAIT", "", false);

		//Inclui os comandos de lock
		executeSQL(pCn, pQuerySQL + LOCK_FOR_UPDATE + " NOWAIT");
		return true;
	}
	
	/**
	 * Retorna a quantidade de linhas de uma pesquisa (ResultSet)
	 * @param pResultSet ResultSet a ser pesquisado
	 * @return Quantidade de linhas existentes
	 * @throws DBSIOException 
	 */
	public static int getResultSetRowsCount(ResultSet pResultSet) throws DBSIOException{
		if (!DBSObject.isEmpty(pResultSet)){
			try {
				int xSaveRow = pResultSet.getRow();
				int xCurRow;
				pResultSet.last();
				xCurRow = pResultSet.getRow();
				if (xCurRow>0){
					if (xSaveRow == 0){
						pResultSet.beforeFirst();
					}
					return xCurRow;
				}else{
					return 0;
				}
			} catch (SQLException e) {
				throwIOException(e);
				return 0;
			}
		}else{
			return 0;
		}
	}
	
	/**
	 * Retorna a quantidade de registros de uma tabela
	 * @param pCn Conexão com o banco de dados
	 * @param pTableName Nome da tabela
	 * @return Quantidade de registros da tabela
	 * @throws DBSIOException
	 */
	public static int getTableRowsCount(Connection pCn, String pTableName) throws DBSIOException {
		return getSQLRowsCount(pCn, "Select 'foo' from " + pTableName);
	}
	
	/**
	 * Retorna a quantidade de registros da query informada em <b>pSQLStatement</b>.<br/>
	 * Em uma query simples, só é necessário informar o SQL a partir do comando <b>FROM</b>(inclusive).<br/>
	 * ex:FROM table WHERE a=1.<br/>
	 * @param pCn Conexão com o bando de dados
	 * @param pSQLStatement Comando SQL
	 * @return
	 */
	public static int getSQLRowsCount(Connection pCn, String pSQLStatement) {
		if (DBSObject.isEmpty(pSQLStatement) ||
			DBSString.getInStr(pSQLStatement, "From ", false)==0) {
			wLogger.error("getSQLRowsCount: Comando SQL inválido:" + pSQLStatement);
			return 0;
		}
		if (pCn==null){
			return 0;
		}
		pSQLStatement = pSQLStatement.trim();
//		int xI = DBSString.getInStr(" " + pSQLStatement, " From ", false);
//		pSQLStatement = DBSString.getSubString(pSQLStatement, xI, pSQLStatement.length());
		if (pSQLStatement.toLowerCase().startsWith("from")){
			if (DBSIO.getDataBaseProduct(pCn) == DB_SERVER.SQLSERVER) {
				// Count(1) não funciona no sql server
				pSQLStatement = "Select Count(*) " + pSQLStatement; //Pesquisa com único select
			}else {
				pSQLStatement = "Select Count(1) " + pSQLStatement; //Pesquisa com único select
			}
		}else{
			if (DBSIO.getDataBaseProduct(pCn) != DB_SERVER.SQLSERVER) {
				//Retira o '*' da syntaxe para evitar erro de duplicidade de nome de coluna
				pSQLStatement = DBSString.changeStr(pSQLStatement, " * ", " 1 ");
				pSQLStatement = "Select Count(1) From (" + pSQLStatement + ") foo"; //Pesquisa com multiplos selects
			} else {
				pSQLStatement = "Select Count(*) From (" + pSQLStatement + ") foo"; //Pesquisa com multiplos selects
			}
		}
		int xCount = 0;

		ResultSet xRS = null;
		try {
			xRS = DBSIO.openResultSet(pCn, pSQLStatement);
			DBSIO.moveFirst(xRS);
			xCount = xRS.getInt(1);
		} catch (SQLException | DBSIOException e) {
			wLogger.error("getSQLRowsCount", e);
		} finally{
			DBSIO.closeResultSet(xRS);
		}

		return xCount;
	}
	
	/**
	 * Retorna lista com chave e texto para ser utlizado na exibição de combobox.
	 * @param pCn Objeto da conexão
	 * @param pSQL Nome da tabela
	 * @param pTextColumnsNames Nomes das colunas(separadas por espaco ou vírgula) que terão os seus respectivos conteúdos exibidos
	 * @param pKeyColumnName Nome da coluna que será utilizada como chave
	 * @param pNullText Texto a ser exibido para a seleção null
	 * @return
	 * @throws DBSIOException
	 */
	public static LinkedHashMap<Object, String> getList(Connection pCn, String pSQL, String pTextColumnsNames, String pKeyColumnName, NULL_TEXT pNullText) {
		DBSDAO<Object> 					xDAO = new DBSDAO<Object>(pCn);
		LinkedHashMap<Object, String> 	xValues = new LinkedHashMap<Object, String>();
		String[] 						xColumns;
		String							xValue;
		String							xTextValue = "";
		String 							xSeparador = ", ";
		int								xCount = 0;
		pSQL = DBSString.changeStr(pSQL, "%s", ""); //Remove string '%s' normalmente utilizada para inserir texto em determinada posição na string, via String.format
		pTextColumnsNames = DBSString.changeStr(pTextColumnsNames, ",", " ");
		xColumns = pTextColumnsNames.split("\\s+");

		if (pCn == null ||
			pSQL == null ||
			pTextColumnsNames == null ||
			pKeyColumnName ==  null){
			return null;
		}

		//Inclui o texto do item nulo
		if (pNullText != NULL_TEXT.NAO_EXIBIR){
			xValues.put(COMBOBOX.NULL_VALUE, pNullText.toString());
		}
		
		try {
			//Inclui os dados recuperados do banco de dados, 
			//se existir dados e tiverem sido informada(s) a(s) coluna(s) que será(ão) utilizada(s) para exibição
			if (xDAO.open(pSQL) &&
				xColumns.length > 0){
				xDAO.moveBeforeFirstRow();
				while (xDAO.moveNextRow()){
					//Monta o texto a partir dos valores das colunas
					xTextValue = "";
					xCount = 0;
					for (String xC: xColumns){
						xCount++;
						xValue = DBSObject.getNotEmpty(xDAO.getValue(xC),"").toString().trim();
						if (!DBSObject.isEmpty(xValue) &&
							!xValue.equals("*") &&
							!xValue.equals("")){
							if (xCount > 1) {
								xTextValue = xTextValue + xSeparador + xValue;
							} else {
								xTextValue = xTextValue + xValue;
							}
						}
					}
					//Adiciona texto e chave a lista
					xValues.put(xDAO.getValue(pKeyColumnName), xTextValue);
				}
				xDAO.close();
			}
		} catch (DBSIOException e) {
			wLogger.error("getList",e);
		}
		return xValues;
	}
	
	/**
	 * Retorna lista com chave e texto para ser utlizado na exibição de combobox. Não havendo item nulo
	 * @param pCn Objeto da conexão
	 * @param pSQL Nome da tabela
	 * @param pTextColumnsNames Nomes das colunas(separadas por espaco ou vírgula) que terão os seus respectivos conteúdos exibidos
	 * @param pKeyColumnName Nome da coluna que será utilizada como chave
	 * @param pNullText Texto a ser exibido para a seleção null
	 * @return
	 * @throws DBSIOException
	 */
	public static LinkedHashMap<Object, String> getList(Connection pCn, String pSQL, String pTextColumnsNames, String pKeyColumnName) {
		return getList(pCn, pSQL, pTextColumnsNames, pKeyColumnName, NULL_TEXT.NAO_EXIBIR);
	}
	
	


	/**
	 * Move o valor para a coluna conforme o index do atributo do DataModel informado
	 * @param pDataModel Instancia da classe
	 * @param pColumnIndex Nome do atributo/coluna
	 * @param pValue valor do atributo
	 */
	public final static <T> boolean setDataModelFieldsValue(T pDataModel, int pColumnIndex, Object pValue){
		//Loop nos tributos do classe
		int xX=-1;
		if (pDataModel != null){
			//Loop em todos os atributos da classe para contar a posição
			for (Field xField:pDataModel.getClass().getDeclaredFields()){
				xX++;
				if (xX == pColumnIndex){
					return pvSetDataModelFieldValue(pDataModel, xField, pValue);
				}
			}
		}
		return false;
	}

	/**
	 * Retorna o valor do atributo/coluna da classe genérica 
	 * @param <T>
	 * @param pDataModel Classe generica
	 * @param pFieldName Nome da Coluna
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T getDataModelFieldValue(Object pDataModel, String pFieldName){
		Field xField = null;
		if (pDataModel != null){
			xField = getDataModelField(pDataModel, pFieldName);
			if (xField != null){
				try {
					xField.setAccessible(true);
					return (T) xField.get(pDataModel);
					//return (A) xField.getGenericType();
				} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
					wLogger.error("getDataModelFieldValue", e);
					return null;
				}
			}else{
				return null;
			}
		}else{
			return null;
		}
	}		
	
	/**
	 * Move o valor informado para a coluna do tributo da classe informada conforme o nome informado
	 * @param pDataModel Instancia da classe
	 * @param pColumnName Nome do atributo/coluna
	 * @param pValue valor do atributo
	 */	
	public final static <T> boolean setDataModelFieldsValue(T pDataModel, String pColumnName, Object pValue){
		if (pDataModel != null){ 
			for (Field xField:pDataModel.getClass().getDeclaredFields()){
				//Verifica se o campo é um datamodel (Anotação @DataModel)
				//e se for, irá procurar dentro dos fields do datamodel recursivamente
				Annotation xAnnotationTmp = xField.getType().getAnnotation(DBSTableModel.class);
				if (xAnnotationTmp instanceof DBSTableModel){
					try {
						//Cria datamodel para receber os valores 
						Object xO = new Object();
						xField.setAccessible(true);
						//Se o xfield for nulo..
						if (xField.get(pDataModel)==null){
							//Cria novo objeto/DataModel do mesmo tipo do campo 
							xO = xField.getType().newInstance();
							//Seta o field original com o objeto recem criado
							xField.set(pDataModel, xO);
						}else{
							//Recupera o objeto do datamodel
							xO = xField.get(pDataModel);
						}
						//Chamada recursiva para busca o field com o mesmo nome da coluna informada
						setDataModelFieldsValue(xO, pColumnName, pValue);
					} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
						e.printStackTrace();
						
					}
				//Se for um campo comum, seta o valor
				}else{
					//System.out.println("FIELD VALUE:" + xField.getName()); 
					if (xField.getName().toUpperCase().trim().equals(pColumnName.toUpperCase().trim())){
						return pvSetDataModelFieldValue(pDataModel, xField, pValue);
					}
				}
			}
		}
		return false;
	}	
	
	/**
	 * Copia os valores do dataModel origem(pSourceDataModel) para o dataModel destino(pTargetDataModel), desde que encontre campos com o mesmo nome.<br/>
	 * Esta rotina ainda não contempla datamodel recursivo, mas aceita classes extendidas. 
	 * @param pSourceDataModel DataModel origem
	 * @param pTargetDataModel DataModel destino
	 * @return Retorna <b>pTargetDataModel<b> destino já preenchido.
	 */
	public final static <T, T2> T2 copyDataModelFieldsValue(T pSourceDataModel, T2 pTargetDataModel){
		if (pSourceDataModel != null && pTargetDataModel != null){
			pvCopyDataModelFieldsValue(pSourceDataModel.getClass(), pSourceDataModel, pTargetDataModel);
		}
		return pTargetDataModel;
	}	
	
		
	/**
	 * Retorna se dois datamodels contém os mesmos valores
	 * @param pSourceDataModelClass
	 * @param pSourceDataModel
	 * @param pTargetDataModel
	 * @return
	 */
	public final static <T,T2> boolean isEqualDataModelFieldsValues(Class<?> pSourceDataModelClass, T pSourceDataModel, T2 pTargetDataModel){
		for (Field xField:pSourceDataModelClass.getDeclaredFields()){
			// Verifica se o campo é um datamodel da dbsoft (Anotação @DBSTableModel)
			// Se for irá procurar dentro dos fields do datamodel
			Annotation xAnnotationTmp = xField.getType().getAnnotation(DBSTableModel.class);
			if (xAnnotationTmp instanceof DBSTableModel){
				//Chamada recursiva não implementada
			}else{
				Field yField = findDataModelField(pTargetDataModel.getClass(), xField.getName());
				xField.setAccessible(true);
				if (yField != null){
					try {
						yField.setAccessible(true);
						Object xValue = DBSObject.toClassValue(xField.get(pSourceDataModel), xField.getType());
						Object yValue = DBSObject.toClassValue(yField.get(pSourceDataModel), yField.getType());
						if (!DBSObject.isEqual(xValue, yValue)){
							return false;
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						wLogger.error("copyDataModelFieldsValue", e);
					}
				}
			}
			return true;
		}

		//Chamada recurva para procurar campo na superclass, se existir
		if (pSourceDataModelClass.getSuperclass() != null){
			return isEqualDataModelFieldsValues(pSourceDataModelClass.getSuperclass(), pSourceDataModel, pTargetDataModel);
		}
		return false;
	}
	
	public final static <T, T2> boolean dataModelValuesIsEqual(T pSourceDataModel, T pTargetDataModel) {
		if (pSourceDataModel != null && pTargetDataModel != null){
			return pvDataModelFieldsValueIsEqual(pSourceDataModel.getClass(), pSourceDataModel, pTargetDataModel);
		}
		return false; 
	}
//	/**
//	 * Retorna se dois datamodels contém os mesmos valores
//	 * @param pSourceDataModelClass
//	 * @param pSourceDataModel
//	 * @param pTargetDataModel
//	 * @return
//	 */
//	public final static <T,T2> boolean isEqualDataModelMethodValues(Class<T> pSourceDataModelClass, T pSourceDataModel, T2 pTargetDataModel){
//		for (Method xMethod:pSourceDataModelClass.getDeclaredMethods()){
//			// Verifica se o campo é um datamodel da dbsoft (Anotação @DBSTableModel)
//			// Se for irá procurar dentro dos fields do datamodel
//				Method yMethod = findDataModelMethod(pTargetDataModel.getClass(), xMethod.getName());
//				if (yMethod != null){
//					try {
//						//Executa o método para retornar o valor
//						Object xValue = xMethod.invoke(xMethod);
//						Object yValue = xMethod.invoke(yMethod);
//						if (!DBSObject.isEqual(xValue, yValue)){
//							return false;
//						}
//					} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
//						wLogger.error("isEqualDataModelMethodValues", e);
//					}
//				}
//			return true;
//		}
//
//		//Chamada recurva para procurar campo na superclass, se existir
//		if (pSourceDataModelClass.getSuperclass() != null){
//			return isEqualDataModelMethodValues(pSourceDataModelClass.getSuperclass(), pSourceDataModel, pTargetDataModel);
//		}
//		return false;
//	}
	
	//######################################################################################################### 
	//## Private Methods                                                                                      #
	//#########################################################################################################
	
	//	/**
	//	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	//	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	//	 * @param pDataModel DataModel com os atributos e respectivos valores
	//	 * @param pCn Conexão com o banco
	//	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	//	 * @return Quantidade de registros afetados
	//	 * @throws DBSIOException
	//	 */
	//	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand) throws DBSIOException{
	//		//Procura pela anotação DataModel para posteriomente ler o nome da tabela 
	//		Annotation xAnnotationTmp = pDataModel.getClass().getAnnotation(DataModel.class);
	//		if (xAnnotationTmp instanceof DataModel){
	//			DataModel xAnnotation = (DataModel) xAnnotationTmp;
	//			//Se o nome da tabela foi informado
	//			if (!DBSObject.isEmpty(xAnnotation.tablename())){
	//				return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, pDAOCommand, xAnnotation.tablename(), "");
	//			}
	//		}
	//		return -1;
	//	}
		
	/**
	 * Busca pelo campo na class ou na superclass recursivamente se existir.<br/>
	 * Campos são todas variáveis declaradas.
	 * @param pClass
	 * @param pFieldName
	 * @return
	 */
	public static Field findDataModelField(Class<?> pClass, String pFieldName) {
	    pFieldName = pFieldName.toUpperCase().trim();
		for (Field yField:pClass.getDeclaredFields()){
			if (pFieldName.equals(yField.getName().toUpperCase().trim())){
				return yField;
			}
		}
		if (pClass.getSuperclass() != null){
			return findDataModelField(pClass.getSuperclass(), pFieldName);
		}
		return null;
	}
	
	/**
	 * Busca pelo campo na class ou na superclass recursivamente se existir.<br/>
	 * Campos são todas variáveis declaradas.
	 * @param pClass
	 * @param pMethodName
	 * @return
	 */
	public static Method findDataModelMethod(Class<?> pClass, String pMethodName) {
	    pMethodName = pMethodName.trim().toUpperCase();
		for (Method yMethod:pClass.getDeclaredMethods()){
			if (pMethodName.equals(yMethod.getName().trim().toUpperCase())){
				return yMethod;
			}
		}
		if (pClass.getSuperclass() != null){
			return findDataModelMethod(pClass.getSuperclass(), pMethodName);
		}
		return null;
	}
	/**
	 * Copia os valores das colunas de um DAO para outro.
	 * @param pDaoSource Origem dos dados
	 * @param pDaoTarget Destino dos dados
	 */
	public static void copyDAOFieldsValues(DBSDAO<?> pDaoSource, DBSDAO<?> pDaoTarget) {
		copyDAOFieldsValues(pDaoSource, pDaoTarget, true);
	}
		
	/**
	 * Copia os valores das colunas de um DAO para outro.
	 * @param pDaoSource Origem dos dados
	 * @param pDaoTarget Destino dos dados
	 * @param pShowColumnNameNotFoundMessage Indica se exibe mensagem de erro caso não exista o nome da coluna na tabela.
	 */
	public static void copyDAOFieldsValues(DBSDAO<?> pDaoSource, DBSDAO<?> pDaoTarget, boolean pShowColumnNameNotFoundMessage) {
		if (pDaoSource==null
		 || pDaoTarget==null){return;}
		//Salva configuração atual
		boolean xShowColumnNameNotFoundMessageSource = pDaoSource.getShowColumnNameNotFoundMessage();
		boolean xShowColumnNameNotFoundMessageTarget = pDaoTarget.getShowColumnNameNotFoundMessage();
		//Altera para configuração informada
		pDaoSource.setShowColumnNameNotFoundMessage(pShowColumnNameNotFoundMessage);
		pDaoTarget.setShowColumnNameNotFoundMessage(pShowColumnNameNotFoundMessage);
		//Copia valores colunas
		for (DBSColumn xColumn : pDaoSource.getColumns()) {
			pDaoTarget.setValue(xColumn.getColumnName(), xColumn.getValue());
		}
		//Restaura configuração
		pDaoSource.setShowColumnNameNotFoundMessage(xShowColumnNameNotFoundMessageSource);
		pDaoTarget.setShowColumnNameNotFoundMessage(xShowColumnNameNotFoundMessageTarget);
	}

	/**
	 * Retorna campo a partir no nome do propriedade existente no datamodel 
	 * @param pDataModel
	 * @param pFieldName
	 * @return
	 */
	public final static <T> Field getDataModelField(T pDataModel, String pFieldName){
		Field xField = null;
		if (pDataModel != null){ 
			for (Field xDeclaredField:pDataModel.getClass().getDeclaredFields()){
				//Verifica se o campo é um datamodel (Anotação @DataModel)
				//Se for irá procurar dentro dos field do datamodel
				Annotation xAnnotationTmp = xDeclaredField.getType().getAnnotation(DBSTableModel.class);
				if (xAnnotationTmp instanceof DBSTableModel){
					try {
						//Cria novo objeto
						Object xInnerDataModel = new Object();
						xDeclaredField.setAccessible(true);
						//Se o xfield for nulo..
						if (xDeclaredField.get(pDataModel)==null){
							//Cria novo objeto do tipo do DataModel
							xInnerDataModel = xDeclaredField.getType().newInstance();
							//Seta o field original com o objeto recem criado
							xDeclaredField.set(pDataModel, xInnerDataModel);
						}else{
							//Recupera o objeto do datamodel
							xInnerDataModel = xDeclaredField.get(pDataModel);
						}
						//Chamada recursiva para busca o field com o mesmo nome da coluna informada
						xField = getDataModelField(xInnerDataModel, pFieldName);
					} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
						wLogger.error("getDataModelField", e);
					}
				}else{
					//System.out.println("FIELD VALUE:" + xField.getName()); 
					if (xDeclaredField.getName().toUpperCase().trim().equals(pFieldName.toUpperCase().trim())){
						xField = xDeclaredField;
					}
				}
				//Se achou o campo sai do loop
				if (xField != null){
					break;
				}
			}
		}
		return xField;
	}	

	
	/**
	 * Reseta valores da datamodel
	 * @param pDataModel
	 */
	public final static <T> void resetDataModelFieldsValue(T pDataModel){
		//Loop nos tributos do classe
		if (pDataModel != null){
			for (Field xField:pDataModel.getClass().getDeclaredFields()){
				xField.setAccessible(true);
				try {
					xField.set(pDataModel, null);
				} catch (SecurityException | IllegalAccessException e) {
					wLogger.error("resetDataModelFieldsValue",e);
				}
			}
		}
	}
	
	//######################################################################################################### 
	//## Private Methods                                                                                      #
	//#########################################################################################################
	
	//	/**
	//	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	//	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	//	 * @param pDataModel DataModel com os atributos e respectivos valores
	//	 * @param pCn Conexão com o banco
	//	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	//	 * @return Quantidade de registros afetados
	//	 * @throws DBSIOException
	//	 */
	//	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand) throws DBSIOException{
	//		//Procura pela anotação DataModel para posteriomente ler o nome da tabela 
	//		Annotation xAnnotationTmp = pDataModel.getClass().getAnnotation(DataModel.class);
	//		if (xAnnotationTmp instanceof DataModel){
	//			DataModel xAnnotation = (DataModel) xAnnotationTmp;
	//			//Se o nome da tabela foi informado
	//			if (!DBSObject.isEmpty(xAnnotation.tablename())){
	//				return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, pDAOCommand, xAnnotation.tablename(), "");
	//			}
	//		}
	//		return -1;
	//	}
		
	/**
	 * Objetivo: Indica ao Banco de Dados o inicio do processo de transação.
	 * @param pCn conexão com o Banco de dados;
	 * @param pSavePoint Quando informado, nome do Savepoint que se deseja criar;
	 * @return Valor de pSavePoint se não ocorrer erro e nulo ser ocorrer.
	 * @throws DBSIOException 
	 */
	public static Savepoint beginTrans(Connection pCn, String pSavePoint) throws DBSIOException{
		if (pCn == null){
			return null;
		}
		try {
			if (pCn.isClosed()){
				wLogger.error("DBSIO:beginTrans:Conexão fechada");
				return null;
			}else{
				pCn.setAutoCommit(false);
				if (pSavePoint == null){
					return pCn.setSavepoint();
				}else{
					return pCn.setSavepoint(pSavePoint);
				}
			}
		} catch (SQLException e) {
			throwIOException(e, pCn);
			return null;
		}
	}

	/**
	 * Objetivo: Indica ao Banco de Dados o inicio do processo de transação
	 * @param pCn conexão com o Banco de dados;
	 * @return true - Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	public static boolean beginTrans(Connection pCn) throws DBSIOException{
		if (beginTrans(pCn, null) == null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Executa o Commit ou Rollback no Banco de Dados dependendo dos erros informados
	 * @param pCn conexão com o Banco de dados;
	 * @param pOk Indicador se houve erro no processo anterior a chamada;
	 * @param pSavePoint Quando informado, nome do Savepoint que se deseja efetuar rollback;
	 * @return true - Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	public static boolean endTrans(Connection pCn, boolean pOk, Savepoint pSavePoint) throws DBSIOException{
		if (pCn == null){
			return false;
		}
		if (pOk ==true){
			//Ignora commit se for houver savepoint
			//O commit só pode ser dado na transação inteira
			if (DBSObject.isEmpty(pSavePoint)){
				endTrans(pCn, pOk);
			}
		}else{
			pvRollbackTrans(pCn, pSavePoint);
		}
		return true;
	}
	public static boolean endTrans(Connection pCn, boolean pOk) throws DBSIOException{
		if (pCn == null){
			return false;
		}
		if (pOk ==true){
			pvCommitTrans(pCn);
		}else{
			pvRollbackTrans(pCn);
		}
		return true;
	}
	
	/**
	 * Retorna se o registro corrente é o primeiro.
	 * @param pResultSet
	 * @return
	 */
	public static boolean isFirstRow(ResultSet pResultSet){
		if (pResultSet != null){
			try {
				return (pResultSet.getRow() == 1);
			} catch (SQLException e) {
				wLogger.error(e);
			}
		}
		return false;
	}
	
	/**
	 * Retorna se o registro corrente é o primeiro.
	 * @param pDataModel Data Model contendo os registros
	 * @return
	 */
	public static boolean isFirstRow(@SuppressWarnings("rawtypes") javax.faces.model.DataModel pDataModel){
		if (pDataModel != null){
			return (pDataModel.getRowIndex() == 0);
		}
		return false;
	}

	/**
	 * Retorna se é a o registro corrente é o último.
	 * @param pResultSet
	 * @return
	 */
	public static boolean isLastRow(ResultSet pResultSet) {
		if (pResultSet != null){
			try {
				return (pResultSet.getRow() == getResultSetRowsCount(pResultSet));
			} catch (DBSIOException | SQLException e) {
				wLogger.error(e);
			}
		}
		return false;
	}

	/**
	 * Retorna se é a o registro corrente é o último.
	 * @param pDataModel Data Model contendo os registros
	 * @return
	 */
	public static boolean isLastRow(@SuppressWarnings("rawtypes") javax.faces.model.DataModel pDataModel) {
		if (pDataModel != null){
			return (pDataModel.getRowIndex() == (pDataModel.getRowCount()-1));
		}
		return false;
	}

	
	/**
	 * Move o cursor para o registro anterior ao primeiro.
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */	
	public static void moveBeforeFirst(ResultSet pResultSet) throws DBSIOException{
		if (pResultSet != null){
			try {
				if (!pResultSet.isClosed()){
					pResultSet.beforeFirst();
				}
			} catch (SQLException e) {
				throwIOException(e);
			}
		}
	}

	/**
	 * Move o cursor para o registro anterior ao primeiro.
	 * @param pDataModel Data Model contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */	
	public static void moveBeforeFirst(@SuppressWarnings("rawtypes") javax.faces.model.DataModel pDataModel){
		pvMoveResultDataModel(pDataModel, MOVE_DIRECTION.BEFORE_FIRST);
	}

	/**
	 * Move o cursor para o primeiro registro se existir
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */	
	public static boolean moveFirst(ResultSet pResultSet) throws DBSIOException{
		if (pResultSet != null){
			try {
				if (!pResultSet.isClosed()){
					return pResultSet.first();
				}
			} catch (SQLException e) {
				throwIOException(e);
				return false;
			}
		}
		return false;
	}

	/**
	 * Move o cursor para o primeiro registro se existir.
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */	
	public static boolean moveFirst(@SuppressWarnings("rawtypes") javax.faces.model.DataModel pDataModel) {
		return pvMoveResultDataModel(pDataModel, MOVE_DIRECTION.FIRST);
	}


	/**
	 * Move o cursor para o registro anterior se existir
	 * @param pDataModel Data Model contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */	
	public static boolean movePrevious(ResultSet pResultSet) throws DBSIOException{
		if (pResultSet != null){
			try {
				if (!pResultSet.isClosed()){
					return pResultSet.previous();
				}
			} catch (SQLException e) {
				throwIOException(e);
				return false;
			}
		}
		return false;
	}

	/**
	 * Move o cursor para o registro anterior se existir
	 * @param pDataModel Data Model contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */	
	public static boolean movePrevious(@SuppressWarnings("rawtypes") javax.faces.model.DataModel pDataModel) {
		return pvMoveResultDataModel(pDataModel, MOVE_DIRECTION.PREVIOUS);
	}

	/**
	 * Move o cursor para o próximo registro se existir.
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */
	public static boolean moveNext(ResultSet pResultSet) throws DBSIOException{
		if (pResultSet != null){
			try {
				if (!pResultSet.isClosed()){
					return pResultSet.next();
				}
			} catch (SQLException e) {
				throwIOException(e);
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Move o cursor para o próximo registro se existir.
	 * @param pDataModel Data Model contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */
	public static boolean moveNext(@SuppressWarnings("rawtypes") javax.faces.model.DataModel pDataModel) throws DBSIOException{
		return pvMoveResultDataModel(pDataModel, MOVE_DIRECTION.NEXT);
	}

	/**
	 * Move o cursor para o último registro
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */
	public static boolean moveLast(ResultSet pResultSet) throws DBSIOException{
		if (pResultSet != null){
			try {
				if (!pResultSet.isClosed()){
					pResultSet.last();
					return true;
				}
			} catch (SQLException e) {
				throwIOException(e);
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Move o cursor para o último registro.
	 * @param pDataModel Data Model contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */
	public static boolean moveLast(@SuppressWarnings("rawtypes") javax.faces.model.DataModel pDataModel) throws DBSIOException{
		return pvMoveResultDataModel(pDataModel, MOVE_DIRECTION.LAST);
	}



	/**
	 * Retorna o valor de uma coluna da tabela desejada segundo o Critério informado, convertida para o tipo de class informado.
	 * Caso a pesquisa retorne mais de um registro, será retornada a informação do primeiro registro.
	 * @param pCn Conexão com o banco de dados
	 * @param pTableName Nome da tabela
	 * @param pCriterio Critério de seleção para a pesquisa do registro desejado(Clausula Where).
	 * Quando criar a string do critério, utilize as funções toSQL... contidas nesta classe para certificar que os valores estarão de acordo com o tipo de dado que se refere
	 * @param pColumnName Nome da columa da qual se deseja o valor
	 * @param pReturnedClass class para a qual o valor será obrigatoriamente convertido 
	 * @return valor da coluna 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDado(Connection pCn, String pTableName, String pCriterio, String pColumnName, Class<?> pReturnedClass) throws DBSIOException{
		return (T) DBSObject.toClassValue(getDado(pCn, pTableName, pCriterio, pColumnName), pReturnedClass);
	}
	
	/**
	 * Retorna o valor de uma coluna da tabela desejada segundo o <b>pCritério</b> informado.
	 * Caso a pesquisa retorne mais de um registro, será retornada a informação do primeiro registro.
	 * @param pCn Conexão com o banco de dados
	 * @param pTableName Nome da tabela
	 * @param pCriterio Critério de seleção para a pesquisa do registro desejado<b>(SQL da cláusula WHERE, NÃO devendo conter o aprópria termo 'WHERE')</b>.
	 * Quando criar a string do critério, utilize as funções toSQL... contidas nesta classe para certificar que os valores estarão de acordo com o tipo de dado que se refere
	 * @param pColumnName Nome da columa da qual se deseja o valor
	 * @return valor da coluna 
	 */
	public static <T> T getDado(Connection pCn, String pTableName, String pCriterio, String pColumnName) throws DBSIOException{
		if (pCn == null){
			return null;
		}

		String xSQL = "Select " + pColumnName;
		
		if (!pTableName.equals("")){
			xSQL = xSQL + " From " + pTableName;
		}
		
		if (!pCriterio.equals("")){
			xSQL = xSQL + " Where " + pCriterio;
		}

		return getDado(pCn, xSQL, pColumnName);
	}	

	/**
	 * Retorna o valor de uma coluna a partir da query informada.<br/>
	 * Caso a pesquisa retorne mais de um registro, será retornada a informação do primeiro registro.
	 * @param pCn Conexão com o banco de dados
	 * @param pSQL Sintaxe sql contendo todas as informações para efetuar a query.
	 * @param pColumnName Nome da columa da qual se deseja o valor
	 * @param pReturnedClass class para a qual o valor será obrigatoriamente convertido 
	 * @return valor da coluna 
	 * @throws DBSIOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDado(Connection pCn, String pSQL, String pColumnName, Class<?> pReturnedClass) throws DBSIOException{
		return (T) DBSObject.toClassValue(getDado(pCn, pSQL, pColumnName), pReturnedClass);
	}

	/**
	 * Retorna o valor de uma coluna a partir da query informada.<br/>
	 * Caso a pesquisa retorne mais de um registro, será retornada a informação do primeiro registro.
	 * @param pCn Conexão com o banco de dados
	 * @param pSQL Sintaxe sql contendo todas as informações para efetuar a query.
	 * @param pColumnName Nome da columa da qual se deseja o valor
	 * @return valor da coluna 
	 * @throws DBSIOException
	 */
	public static <T> T getDado(Connection pCn, String pSQL, String pColumnName) throws DBSIOException{
		if (pCn == null
		 || pSQL == null
		 || pColumnName == null){
			return null;
		}

		DBSDAO<Object> xDAO = new DBSDAO<Object>(pCn);
		if (xDAO.open(pSQL)){
			if (xDAO.getRowsCount() > 0){
				String xColumnName = pColumnName;
				int xI = pColumnName.indexOf(".");
				//Recupera somente a parte no nome após o "." caso exista.
				if (xI > 0){
					xColumnName = DBSString.getSubString(pColumnName, xI + 2, pColumnName.length());
				}
				//Salva valor
				T xDado = xDAO.getValue(xColumnName); 
				xDAO.close();
				return xDado;
			}
			xDAO.close();
		}
		return null;
	}	

	
	/**
	 * Retorna os valores dos atributos do dataModel informado a partir das colunas que possuam o mesmo nome.<br/>Serão retornados os dados do primeiro registro encontrado conforme resultado do SQL informado.
	 * @param pCn Conexão com o banco de dados
	 * @param pSQL Nome da tabela
	 * @return DataModel 
	 */
	public static <T> T getDadoDataModel(Connection pCn, Class<T> pDataModelClass, String pSQL) throws DBSIOException{
		DBSDAO<T> xDAO = new DBSDAO<T>(pDataModelClass, pCn);
		T xDataModel = xDAO.createDataModel();
		xDAO.open(pSQL);
		if (xDAO.moveFirstRow()){
			xDataModel = xDAO.getDataModel();
		}
		xDAO.close();
		return xDataModel;
	}
	
	/**
	 * Grava os valores dos atributos do dataModel 
	 * @param pCn Conexão com o banco de dados
	 * @param <T>
	 * @param pSQL Nome da tabela
	 * @return A quantidade de registros afetados 
	 */
	@SuppressWarnings("unchecked")
	public static <T> int setDadoDataModel(Connection pCn, T pDataModel, String pTableName) throws DBSIOException{
		DBSDAO<T> xDAO = new DBSDAO<T>((Class<T>) pDataModel.getClass(), pCn, pTableName);
		return xDAO.executeMerge(pDataModel);
	}	
	
	/**
	 * Retorna SQL de INSERT, UPDATE, DELETE, LOCK e SELECT criado a partir dos dados contídos no DBSDAOBase informado
	 * serão utilizadas as colunas da tabela informada em COMMANDTABLE 
	 * @param pDAO Dados que serão utilizados para a criação do SQL
	 * @param pCommand Tipo de comando que se deja criar
	 * @return SQL com o comando informado, preenchido com os dados de pDAO 
	 * @throws DBSIOException 
	 */
	public static <T> String getDAOSQLCommand(DBSDAO<T> pDAO, COMMAND pCommand) throws DBSIOException{
		return getDAOSQLCommand(pDAO, pCommand, "");
	}
	
	/**
	 * Retorna SQL de INSERT, UPDATE, DELETE, LOCK e SELECT criado a partir dos dados contídos no DBSDAOBase informado
	 * serão utilizadas as colunas da tabela informada em COMMANDTABLE 
	 * @param pDAO Dados que serão utilizados para a criação do SQL
	 * @param pCommand Tipo de comando que se deja criar
	 * @return SQL com o comando informado, preenchido com os dados de pDAO 
	 * @throws DBSIOException 
	 */
	public static <T> String getDAOSQLCommand(DBSDAO<T> pDAO, COMMAND pCommand, String pAdditionalSQLWhereCondition) throws DBSIOException{
		String 	xSQL = "";
		String 	xSQLColumns = "";
		String 	xSQLWhere = "";
		boolean xOk = true;
		if (pDAO == null 
		 || DBSObject.isEmpty(pDAO.getCommandTableName())){
			wLogger.error("DBSIO.getDAOSQLCommand: DAO ou tabela de comando não informada.");
			return "";
		}
		if (pCommand.equals(COMMAND.INSERT)){
			xSQL = "INSERT INTO " + pDAO.getCommandTableName() + " ";
		}else if(pCommand==COMMAND.UPDATE){
			xSQL = "UPDATE " + pDAO.getCommandTableName() + " SET ";
		}else if(pCommand.equals(COMMAND.DELETE)){
			xSQL = "DELETE FROM " + pDAO.getCommandTableName() + " ";
		}else if(pCommand.equals(COMMAND.LOCK) 
			  || pCommand.equals(COMMAND.SELECT)){
			xSQL = "SELECT ";
		}
		
		//SET
		//INSERT, UPDATE E SELECT
		if (pCommand!=COMMAND.DELETE){
			xSQLColumns = pvGetDAOSQLCommandColumns(pDAO, pCommand);
			xSQL += xSQLColumns;
			xOk = !(xSQLColumns.equals(""));
		}
		/* Se no update do MERGE não houver nenhum campo para ser alterado,
		 * retorna comando de SELECT para verificar se o registro realmente existe,
		 * evitando que seja tentado efetuar um INSERT desnecessário.*/
		if (pCommand ==COMMAND.UPDATE 
		 && pDAO.isMerging()
		 && !xOk){
			return getDAOSQLCommand(pDAO, COMMAND.SELECT); 
		}
		
		//FROM
		//SELECT E LOCK
		if (xOk){
			if(pCommand.equals(COMMAND.LOCK) 
			|| pCommand.equals(COMMAND.SELECT)){
				xSQL += " FROM " + pDAO.getCommandTableName() + " ";
				xOk = !(xSQLColumns == "");
			}
		}
		
		//WHERE
		//UPDATE, DELETE E SELECT
		if (xOk){
			if (pCommand!=COMMAND.INSERT){
				xSQLWhere = pvGetDAOSQLCommandWhere(pDAO, pCommand);
				if (pAdditionalSQLWhereCondition!=null &&
					!pAdditionalSQLWhereCondition.equals("")){
					xSQLWhere += " " + pAdditionalSQLWhereCondition.trim() + " ";
				}
				xSQL += xSQLWhere;
			}
		}else{
			xSQL = "";
		}
		return xSQL;
	}

	//######################################################################################################### 
	//## Private Methods                                                                                      #
	//#########################################################################################################
	
	//	/**
	//	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	//	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	//	 * @param pDataModel DataModel com os atributos e respectivos valores
	//	 * @param pCn Conexão com o banco
	//	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	//	 * @return Quantidade de registros afetados
	//	 * @throws DBSIOException
	//	 */
	//	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand) throws DBSIOException{
	//		//Procura pela anotação DataModel para posteriomente ler o nome da tabela 
	//		Annotation xAnnotationTmp = pDataModel.getClass().getAnnotation(DataModel.class);
	//		if (xAnnotationTmp instanceof DataModel){
	//			DataModel xAnnotation = (DataModel) xAnnotationTmp;
	//			//Se o nome da tabela foi informado
	//			if (!DBSObject.isEmpty(xAnnotation.tablename())){
	//				return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, pDAOCommand, xAnnotation.tablename(), "");
	//			}
	//		}
	//		return -1;
	//	}
		
	/**
	 * Retorna os registros conforme a QuerySQL informada.<br/>
	 * As colunas poderão ser acessadas como atributos de uma classe diretamente. 
	 * Os nomes dos atributos são os próprios nomes definidos as colunas da QuerySQL.
	 * Exemplo de código xhtlm <b>#{table.campo}</b><br/>
	 * Não existe close para o ResultDataModel.
	 * @param pCn
	 * @param pQuerySQL
	 * @return
	 * @throws DBSIOException
	 */
	@SuppressWarnings("unchecked")
	public static DBSResultDataModel openResultDataModel(Connection pCn, String pQuerySQL) throws DBSIOException{
		ResultSet 			xResultSet;
		Result				xResult;
		DBSResultDataModel	xResultDataModel;
		xResultSet = openResultSet(pCn, pQuerySQL);
		xResult = ResultSupport.toResult(xResultSet);
		//wResultDataModel é necessário para consulta com html pois possibilita o acesso as colunas do registro
		xResultDataModel = new DBSResultDataModel(xResult.getRows());
		xResult = null;
		closeResultSet(xResultSet);
		return xResultDataModel;
	}

	/**
	 * Fecha resultset
	 * @param pResultSet
	 */
	public static void closeResultSet(ResultSet pResultSet){
		if (pResultSet != null){
			Statement xST = null;
			try {
				xST = pResultSet.getStatement();
				if (xST != null){
					xST.close();
				}
			} catch (SQLException e) {
				wLogger.error("closeResultSet:statement", e);
			}

			try {
				pResultSet.close();
			} catch (SQLException e) {
				wLogger.error("closeResultSet:resultset", e);
			}
			xST = null;
			pResultSet = null;
		}
	}
	
	/**
	 * Recupera os registros a partir de uma Query SQL, utilizando a conexão JDBC com o banco
	 * @param pCN conexão a ser utilizada para executa a Query
	 * @param pQuerySQL Query a ser executada
	 * @return ResultSet com os registros
	 * @throws DBSIOException 
	 */
public static ResultSet openResultSet(Connection pCn, String pQuerySQL) throws DBSIOException{
		ResultSet xResultSet = null;
		pQuerySQL = pQuerySQL.trim();
		Statement xST = null;
		try{
//			PreparedStatement xPS = pCn.prepareStatement(pQuerySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//			xPS.execute();
//			xResultSet = xPS.getResultSet();

			//ALBERTO: Alterado para atender a nova versão 8.0 do MySQL;
			DB_SERVER xDataBase = getDataBaseProduct(pCn);
			if (xDataBase == DB_SERVER.ORACLE 
					|| xDataBase == DB_SERVER.SQLSERVER) {
				xST = pCn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			} else {
				xST = pCn.createStatement();
			}
			xResultSet = xST.executeQuery(pQuerySQL);
		}catch(SQLException e){
			throwIOException(pQuerySQL, e, pCn);
			try {
				xST.close();
			} catch (SQLException e1) {
				e1.addSuppressed(e);
				wLogger.error("openResultSet", e1);
			}
		}
		return xResultSet;
	}
	/**
	 * Executa um comando sql diretamente no banco de dados
	 * @param pCn Conexão com o banco de dados
	 * @param pSQL Comando SQL
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException 
	 * @throws Throwable 
	 * @throws Exception 
	 */
	public static int executeSQL(Connection pCn, String pSQL) throws DBSIOException {
		if (pCn==null){
			wLogger.error("executeSQL:Conexão nula.");
			return 0;
		}
		if (pSQL == null){
			wLogger.error("executeSQL:SQL nulo.");
			return 0;
		}
		if (pSQL.contains("(+)")){
			wLogger.error("executeSQL:Join precisa ser padrão ANSI:" + pSQL);
			return 0;
		}
		int xCount=0;
		Statement xST = null;
		try {
			// + ResultSet.HOLD_CURSORS_OVER_COMMIT
			if (DBSIO.getDataBaseProduct(pCn) != DB_SERVER.SQLSERVER) {
				xST = pCn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
			}else {
				xST = pCn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			}
			xST.execute(pSQL); 
			xCount = xST.getUpdateCount();
			//Se foi um Select, recupera a quantidade de registros lidos
			if (xCount==-1){
				xCount = DBSIO.getResultSetRowsCount(xST.getResultSet());
			}
			xST.close(); //Incluido para evitar o erro: ORA-01000: maximum open cursors exceeded
			xST = null;  //Incluido para evitar o erro: ORA-01000: maximum open cursors exceeded
			return xCount;
		} catch (SQLException e) {
			xST = null;  //Incluido para evitar o erro: ORA-01000: maximum open cursors exceeded
			DBSIO.endTrans(pCn, false);
			
			throwIOException(pSQL, e, pCn);

			return 0;
		}
	}
	
	/**
	 * Executa um comando sql diretamente no banco de dados e recupera os valores do autoincremento efetuados pelo banco
	 * @param <T>
	 * @param pCn Conexão com o banco de dados
	 * @param pSQL Comando SQL
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException 
	 * @throws Throwable 
	 * @throws Exception 
	 */
	public static <T> int executeSQLInsertAutoIncremented(Connection pCn, String pSQL, DBSDAO<T> pDAO) throws DBSIOException {
		if (pCn == null || pSQL == null || pDAO == null){
			return 0;
		}
		int xCount=0;
		try {
			if (pDAO.containsColumn(pDAO.getPK())){
				DATATYPE xDT = pDAO.getColumn(pDAO.getPK()).getDataType();
				//Somente busca recuperar o valor gerado caso a coluna definida como PK seja numérica
				if (xDT == DATATYPE.DECIMAL
				 || xDT == DATATYPE.INT
				 || xDT == DATATYPE.DOUBLE
				 ||	xDT == DATATYPE.ID){
					DB_SERVER xDBP = getDataBaseProduct(pCn);
					//Se for Oracle, o autoincrement é implementado via sequence.
					if (xDBP == DB_SERVER.ORACLE){ 
						//Executa comando no banco
						xCount = executeSQL(pCn, pSQL);
						BigDecimal xId;
						xId = pvGetOracleSequenceValue(pCn, pSQL, pDAO.getColumn(pDAO.getPK()).getColumnName());
						pDAO.setValue(pDAO.getPK(), xId);
					}else{
						PreparedStatement xPS;
						ResultSet xRS;
						//pCn.prepareStatement
						xPS = pCn.prepareStatement(pSQL, PreparedStatement.RETURN_GENERATED_KEYS);
						//Executa o INSERT
						xPS.executeUpdate();
						//Recupera Recordset contendo os valores gerados para as columnas como autoincrement ou sequences 
						xRS = xPS.getGeneratedKeys();
						//Loop para recuperas as colunas com os respectivos valores
						while (DBSIO.moveNext(xRS)){
							for (int xX=1; xX <= xRS.getMetaData().getColumnCount(); xX++){
								//Atualiza os valores em memória, das colunas que tiveram seu valores gerados automaticamente após o insert  
								pDAO.setValue(pDAO.getPK(), xRS.getInt(xX)); //Assume que as colunas com autoincrement/sequences são integer
							}
						}
						//Recupera a quantidade de registros efetados.
						xCount = xPS.getUpdateCount();
						DBSIO.closeResultSet(xRS);
						xPS.close();
					}
					return xCount;
				}else{
					return executeSQL(pDAO.getConnection(), pSQL);
				}
			}else{
				throwIOException("PK:" + pDAO.getPK() + " inexistente na tabela " + pDAO.getCommandTableName());
			}
		} catch (SQLException e) {
			//Verifica se o erro é tratado
			DBSIO.endTrans(pCn, false);

			throwIOException(pSQL, e, pCn);
			return 0;
		}
		return 1;
	}

	/**
	 * Executa comando SQL diretamente no banco de dados
	 * @param pDAO
	 * @param pDAOCommand Comando conforme constante COMMAND(Insert/update/delete/lock/select) 
	 * @return Quantidade de registros afetados. -1 nenhum registro afetado
	 * @throws DBSIOException 
	 */
	public static <T> int executeDAOCommand(DBSDAO<T> pDAO, COMMAND pDAOCommand) throws DBSIOException{
		return executeDAOCommand(pDAO, pDAOCommand, true, "");
	}
	
	public static <T> int executeDAOCommand(DBSDAO<T> pDAO, COMMAND pDAOCommand, String pAdditionalSQLWhereCondition) throws DBSIOException{
		return executeDAOCommand(pDAO, pDAOCommand, true, pAdditionalSQLWhereCondition);
	}

	public static <T> int executeDAOCommand(DBSDAO<T> pDAO, COMMAND pDAOCommand, boolean pAutoIncrementValueRetrieve) throws DBSIOException{
		return executeDAOCommand(pDAO, pDAOCommand, pAutoIncrementValueRetrieve, "");
	}

	/**
	 * Executa comando SQL diretamente no banco de dados
	 * @param pDAO
	 * @param pDAOCommand Comando conforme constante COMMAND(Insert/update/delete/lock/select) 
	 * @param pAdditionalSQLWhereCondition Texto da condição(sem 'WHERE') a ser adicionada a cláusula 'WHERE' que já será gerada automaticamente. <br/>
	 * @return Quantidade de registros afetados. -1 nenhum registro afetado
	 * @throws DBSIOException 
	 */
	public static <T> int executeDAOCommand(DBSDAO<T> pDAO, COMMAND pDAOCommand, boolean pAutoIncrementValueRetrieve, String pAdditionalSQLWhereCondition) throws DBSIOException{
		if (pDAO.getCommandTableName().equals("")){
			wLogger.error("Tabela não informada para efetuar o " + pDAOCommand.toString());
			return 0;
		}
		if (pDAO.getCommandColumns().size() == 0){
			wLogger.error("A tabela " + pDAO.getCommandTableName() + " não possui colunas para efetuar o " + pDAOCommand.toString() + ". Verifique se o nome está correto, inclusive quanto a nome minúsculo e maiúsculo.");
			return 0;
		}
		String xSQLCommand = getDAOSQLCommand(pDAO, pDAOCommand, pAdditionalSQLWhereCondition); 
		//TODO DEIXAR COMENTANDO O SYSTEM.OUT ABAIXO
//		System.out.println(xSQLCommand);
		if (DBSObject.isEmpty(xSQLCommand)){
			return 0;
		}else{
			int xCount = 0;
			if (pDAOCommand == COMMAND.INSERT  
			 && pAutoIncrementValueRetrieve){
				xCount = executeSQLInsertAutoIncremented(pDAO.getConnection(), xSQLCommand, pDAO);
			}else{
				xCount = executeSQL(pDAO.getConnection(), xSQLCommand);
			}
			return xCount;
		}
	}

	/**
	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	 * @param pCn Conexão com o banco de dados
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @param pTableName Nome da tabela que receberá o comando
	 * @param pPK Nomes das colunas que serão utilizadas como chave primária. Caso omitido, será utilizado o PK de própria tabela
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelCommand(Connection pCn, T pDataModel, COMMAND pDAOCommand, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, null, pDAOCommand, pTableName, pPK);
	}	

	/**
	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	 * @param pCn Conexão com o banco de dados
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @param pTableName Nome da tabela que receberá o comando
	 * @param pPK Nomes das colunas que serão utilizadas como chave primária. Caso omitido, será utilizado o PK de própria tabela
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> int executeDataModelCommand(Connection pCn, T pDataModel, T pDataModelValueOriginal, COMMAND pDAOCommand, String pTableName, String pPK) throws DBSIOException {
		int xI = -1;
		if (pCn!=null &&
			pDataModel != null){
			if (!DBSObject.isEmpty(pTableName)){
				DBSDAO<T> xDAO = null;
				try {
					xDAO = new DBSDAO<T>((Class<T>) pDataModel.getClass(), pCn, pTableName, pPK);
					if (pDAOCommand == COMMAND.INSERT){
						xI = xDAO.executeInsert(pDataModel);
					}else if(pDAOCommand == COMMAND.UPDATE){
						//Seta os valores originais para posteriormente poder comparar quais valores foram alterados
						if (pDataModelValueOriginal==null){
							//Como não existe valores anteriores para comparar, forçar para considerar todos os campos, independentemente se forão ou não informados
							xDAO.setExecuteOnlyChangedValues(false);
						}else{
							xDAO.setDataModelValueOriginal(pDataModelValueOriginal);
						}
						xI = xDAO.executeUpdate(pDataModel);
						//Se houve alteração
						if (xI > 0){
							//Copia os valores atuais como sendo também os valores originals
							copyDataModelFieldsValue(pDataModel, pDataModelValueOriginal);
						}else{//Se não houve alteração
							//Restaura os valores atuais os valores originais
							copyDataModelFieldsValue(pDataModelValueOriginal, pDataModel);
						}
					}else if(pDAOCommand == COMMAND.DELETE){
						xI = xDAO.executeDelete(pDataModel);
					}
				} catch(DBSIOException e){
					throw e;
				} finally{
					try {
						xDAO.close();
					} catch (DBSIOException e) {
						wLogger.error("executeDataModelCommand3", e);
					}
				}
			}
		}
		return xI;
	}	

	//setDataModelValueOriginal
	
	/**
	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	 * @param pCn Conexão com o banco de dados
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @param pTableName Nome da tabela que receberá o comando
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelCommand(Connection pCn, T pDataModel, COMMAND pDAOCommand, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, pDAOCommand, pTableName, "");
	}	

	public static <T> int executeDataModelCommand(Connection pCn, T pDataModel, T pDataModelValueOriginal, COMMAND pDAOCommand, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, pDataModelValueOriginal, pDAOCommand, pTableName, "");
	}	
	
	/**
	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pCn Conexão com o banco de dados
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelCommand(Connection pCn, T pDataModel, COMMAND pDAOCommand) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, null, pDAOCommand);
	}
	
//	/**
//	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
//	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
//	 * @param pDataModel DataModel com os atributos e respectivos valores
//	 * @param pCn Conexão com o banco
//	 * @param pDAOCommand Comando(Insert, Update ou Delete)
//	 * @return Quantidade de registros afetados
//	 * @throws DBSIOException
//	 */
//	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand) throws DBSIOException{
//		//Procura pela anotação DataModel para posteriomente ler o nome da tabela 
//		Annotation xAnnotationTmp = pDataModel.getClass().getAnnotation(DataModel.class);
//		if (xAnnotationTmp instanceof DataModel){
//			DataModel xAnnotation = (DataModel) xAnnotationTmp;
//			//Se o nome da tabela foi informado
//			if (!DBSObject.isEmpty(xAnnotation.tablename())){
//				return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, pDAOCommand, xAnnotation.tablename(), "");
//			}
//		}
//		return -1;
//	}
	
	/**
	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pCn Conexão com o banco de dados
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelCommand(Connection pCn, T pDataModel, T pDataModelValueOriginal, COMMAND pDAOCommand) throws DBSIOException{
		DBSTableModel xAnnotation = getAnnotationDataModel(pDataModel);
		if (xAnnotation == null){
			return -1;
		}
		return executeDataModelCommand(pCn, pDataModel, pDataModelValueOriginal, pDAOCommand, xAnnotation.tablename(), "");
	}
	
	/**
	 * Executa o merge dos dados contidos no DataModel. Isto é, se existir o registro, altera os dados, se não existir, inclui
	 * É utilizada a chave primária da tabela
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pCn Conexão com o banco de dados
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelMerge(Connection pCn, T pDataModel) throws DBSIOException{
		return executeDataModelMerge(pCn, pDataModel, null);
	}	

	/**
	 * Executa o merge dos dados contidos no DataModel. 
	 * Se existir o registro efatua uma alteração de dados, se não existir, inclui novo registro.
	 * É utilizada a chave primária da tabela para identificar se registro existe.<br/>
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela.
	 * @param pCn Conexão com o banco de dados
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pDataModelValueOriginal DataModel com os atributos e respectivos valores originais
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelMerge(Connection pCn, T pDataModel, T pDataModelValueOriginal) throws DBSIOException{
		int xN=-1;
		if (pCn!=null){
			try{
				Savepoint xS  = DBSIO.beginTrans(pCn, "EXECUTEMERGE"); //Cria savepoint interno para retornar em caso de erro já que o update pode funcionar mais o insert não
				xN = executeDataModelCommand(pCn, pDataModel, pDataModelValueOriginal, COMMAND.UPDATE);//Atualiza registro, se existir
				if (xN==0){ //Se não foi atualiza registro algum...
					xN = executeDataModelCommand(pCn, pDataModel, COMMAND.INSERT); //Insere novo registro
				}
				if (xN<=0){ //Se nehum registro foi alterado é pq houve erro
					DBSIO.endTrans(pCn,false,xS); //ignora Update ou Insert em caso de erro. Rollback até EXECUTEMERGE
				}
			}catch(DBSIOException e){
				throw e;
			}finally{
				
			}
		}
		return xN;
	}	
	
	/**
	 * Executa o insert dos dados contidos no DataModel. 
	 * É utilizada a chave primária da tabela
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pCn Conexão com o banco de dados
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelInsert(Connection pCn, T pDataModel) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, COMMAND.INSERT); //Insere novo registro
	}
	public static <T> int executeDataModelInsert(Connection pCn, T pDataModel, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, COMMAND.INSERT, pTableName); //Insere novo registro
	}
	public static <T> int executeDataModelInsert(Connection pCn, T pDataModel, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, COMMAND.INSERT, pTableName, pPK); //Insere novo registro
	}
	
	/**
	 * Executa o delete dos dados contidos no DataModel. 
	 * É utilizada a chave primária da tabela
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pCn Conexão com o banco
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelDelete(Connection pCn, T pDataModel) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, COMMAND.DELETE); //Insere novo registro
	}	
	public static <T> int executeDataModelDelete(Connection pCn, T pDataModel, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, COMMAND.DELETE, pTableName); //Insere novo registro
	}	
	public static <T> int executeDataModelDelete(Connection pCn, T pDataModel, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, COMMAND.DELETE, pTableName, pPK); //Insere novo registro
	}	
	
	/**
	 * Executa o update dos dados contidos no DataModel. 
	 * É utilizada a chave primária da tabela
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pCn Conexão com o banco
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelUpdate(Connection pCn, T pDataModel) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, COMMAND.UPDATE); //Insere novo registro
	}	
	public static <T> int executeDataModelUpdate(Connection pCn, T pDataModel, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, COMMAND.UPDATE, pTableName); //Insere novo registro
	}	
	public static <T> int executeDataModelUpdate(Connection pCn, T pDataModel, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, COMMAND.UPDATE, pTableName, pPK); //Insere novo registro
	}	
	//######################################################################################################### 
	//## Private Methods                                                                                      #
	//#########################################################################################################
	
	//	/**
	//	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	//	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	//	 * @param pDataModel DataModel com os atributos e respectivos valores
	//	 * @param pCn Conexão com o banco
	//	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	//	 * @return Quantidade de registros afetados
	//	 * @throws DBSIOException
	//	 */
	//	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand) throws DBSIOException{
	//		//Procura pela anotação DataModel para posteriomente ler o nome da tabela 
	//		Annotation xAnnotationTmp = pDataModel.getClass().getAnnotation(DataModel.class);
	//		if (xAnnotationTmp instanceof DataModel){
	//			DataModel xAnnotation = (DataModel) xAnnotationTmp;
	//			//Se o nome da tabela foi informado
	//			if (!DBSObject.isEmpty(xAnnotation.tablename())){
	//				return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, pDAOCommand, xAnnotation.tablename(), "");
	//			}
	//		}
	//		return -1;
	//	}
		
	public static <T> int executeDataModelUpdate(Connection pCn, T pDataModel, T pDataModelValueOriginal) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, pDataModelValueOriginal, COMMAND.UPDATE); //Insere novo registro
	}	
	public static <T> int executeDataModelUpdate(Connection pCn, T pDataModel, T pDataModelValueOriginal, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, pDataModelValueOriginal, COMMAND.UPDATE, pTableName); //Insere novo registro
	}	
	public static <T> int executeDataModelUpdate(Connection pCn, T pDataModel, T pDataModelValueOriginal, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pCn, pDataModel, pDataModelValueOriginal, COMMAND.UPDATE, pTableName, pPK); //Insere novo registro
	}	
	
	
	
	/**
	 * Retorna anotação se classe tem annotation @DataModel.<br/>
	 * Anotação DataModel é a anotação DBSoft que identifica se é um model de tabela de banco 
	 * @param pDataModel
	 * @return Anotação se existir
	 */
	public static <T> DBSTableModel getAnnotationDataModel(T pDataModel){
		//Procura pela anotação DataModel para posteriomente ler o nome da tabela 
		Annotation xAnnotationTmp = pDataModel.getClass().getAnnotation(DBSTableModel.class); 
		if (xAnnotationTmp instanceof DBSTableModel){
			DBSTableModel xAnnotation = (DBSTableModel) xAnnotationTmp;
			//Se o nome da tabela foi informado
			if (DBSObject.isEmpty(xAnnotation.tablename())){
				wLogger.error("Model " + xAnnotation.getClass() + " sem parametro tablename.");
				return null;
			}
			return xAnnotation;
		}
		return null;
	}
	
	/**
	 * Inserir linha em branco ao resultaDataModel do DAO<br/>.
	 * Linha é crida somente na memória. Usuário deverá implementar a inclusão na tabela.
	 * @throws DBSIOException 
	 */
	public static void insertEmptyRow(DBSDAO<?> pDAO) throws DBSIOException{
		boolean xAdd = true;
		DBSResultDataModel xR = pDAO.getResultDataModel();
		@SuppressWarnings("unchecked")
		ArrayList<SortedMap<String, Object>> xListNew = (ArrayList<SortedMap<String, Object>>) IteratorUtils.toList(xR.iterator());
		//Verifica a última linha já é uma linha em branco, para evitar a incluisão desnecessária de uma nova linha.
		if (xListNew.size()>0){
			for (Entry<String, Object> xColumn:xListNew.get(xListNew.size()-1).entrySet()){
				//Considera se é uma linha em branco se a coluna que contém a PK estiver nula.
				DBSColumn xC = pDAO.getCommandColumn(xColumn.getKey());
				if (xC !=null){
					if (xC.getPK()){
						if (xColumn.getValue() == null){
							xAdd = false;
							break;
						}
					}
				}
			}
		}
		//Adiciona nova linha
		if (xAdd){
			xListNew.add(new TreeMap<String, Object>());
			pDAO.getResultDataModel().setWrappedData(xListNew.toArray());
			pDAO.setCurrentRowIndex(pDAO.getResultDataModel().getRowCount()-1);
		}
	}


	/**
	 * Retorna os valores dos atributos do dataModel informado a partir das colunas encontradas no primeiro regostro do select informado.
	 * @param pCn Objeto da conexão
	 * @param pSQL Nome da tabela
	 * @return DataModel 
	 */
	public static <T> List<T> getListDataModel(Connection pCn, Class<T> pDataModelClass, String pSQL) throws DBSIOException{
		DBSDAO<T> xDAO = new DBSDAO<T>(pDataModelClass, pCn);
		List<T> xListDataModel = new ArrayList<T>();
		xDAO.open(pSQL); 
		xListDataModel = xDAO.getListDataModel();
		xDAO.close();
		return xListDataModel;
	}
	

	/**
	 * Retorna novo Index conforme a posição desejada.
	 * @param pDataModel
	 * @param pDirection
	 */
	public static int getIndexAfterMove(Integer pCurrentIndex, Integer pMaxIndex, MOVE_DIRECTION pDirection){
		int xRowIndex = 0;
		switch(pDirection){
			case BEFORE_FIRST: xRowIndex = -1; //Anterior ao primeiro 
					break;
			case FIRST: xRowIndex = 0; //Primeiro
					break;
			case PREVIOUS: xRowIndex = pCurrentIndex - 1; //Anterior
					//Não permite retornar para o registro Anterior ao primeiro
					if (xRowIndex < 0){xRowIndex = 0;}
					break;
			case NEXT: xRowIndex = pCurrentIndex + 1; //Próximo
					//Não permite avançar a um registro posterior ao último
					if (xRowIndex >= pMaxIndex){xRowIndex = pMaxIndex -1;}
					break;
			case LAST: xRowIndex = pMaxIndex - 1; // Último
					break;
		}
		return xRowIndex;
	}
	

	/**
	 * Retorna se novo posicionamento é válido.
	 * @param pCurrentIndex
	 * @param pNewIndex
	 * @param pMaxIndex
	 * @param pDirection
	 * @return
	 */
	public static boolean getIndexAfterMoveIsOk(Integer pCurrentIndex, Integer pNewIndex, Integer pMaxIndex, MOVE_DIRECTION pDirection){
		//Se por para posicionar em um registro válido(diferente do anterior ao primeiro)
		if (pDirection != MOVE_DIRECTION.BEFORE_FIRST){
			//Retorna false, se não houve mudança de posição, quando de uma movimentação para o registro anterior ou próximo, desque exsitam registros.
			if (pMaxIndex > 0){
				if (pDirection == MOVE_DIRECTION.PREVIOUS
				 || pDirection == MOVE_DIRECTION.NEXT){
					//Retorna false, se não houve mudança de posição
					if (pCurrentIndex.equals(pNewIndex)){
						return false;
					}
				}
			}else{
				return false;
			}
		}
		return true;
	}
	/**
	 * Substitui o 'Select * ' generico por 'Select tabela.*' com o nome da tabela  
	 * @param pQuerySQL Query SQL a ser tratada
	 * @return Query SQL modificada
	 */
	public static String changeAsteriskFromQuerySQL(String pQuerySQL){
		String xQuerySQL = pQuerySQL;
		//Obriga que existem somentes espaços simples
		xQuerySQL = DBSString.changeStr(xQuerySQL, "  ", " ");
		//Inclui coluna DBSPK no select
		if (DBSString.getInStr(xQuerySQL," * ") > 0){
			List<String> xT;
			String xS = "";
			xT = getTablesFromQuery(xQuerySQL, true, null);
			//Substitui os 'Select * ' por 'Select tabela.* ' 
			for (int x=0; x <= xT.size()-1; x++){
				if (xS.equals("")){
					xS = " " + xT.get(x) + ".*";
				}else {
					xS = xS + ", " + xT.get(x) + ".*";
				}
			}
			xQuerySQL = DBSString.changeStr(xQuerySQL, " * ", xS + " ");
		}
		return xQuerySQL;
	}
	
	
	/**
	 * Verifica se objeto está vazio ou nulo 
	 * @param pObj Objeto a ser testado
	 * @return true = vazio | false = preenchido. 
	 */
	public static Boolean isEmpty(Object pObj){
		if (DBSObject.isEmpty(pObj)){
			return true;
		}
		if (pObj instanceof String){
			String xObj = (String) pObj;
			if (xObj.equals("*") ||
				xObj.equals("") ||	
				xObj.equals("00:00:00")){
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica se objeto está vazio ou nulo 
	 * @param pObj Objeto a ser testado
	 * @return true = vazio | false = preenchido. 
	 * @throws DBSIOException 
	 */
	public static boolean isResultSetOnValidRow(ResultSet pResultSet) throws DBSIOException{
		try {
			if(pResultSet.isBeforeFirst() ||
					pResultSet.isAfterLast() || 
					!(pResultSet.getRow() > 0)){
				return false;
			}else{
				return true;
			}
		} catch (SQLException e) {
			throwIOException(e);
			return false;
		}
	}

	/**
	 * Retorna se coluna está na lista de colunas que devem ser ignoradas.
	 * Normalmente são colunas vinculadas a trilhas de auditoria.
	 * @param pColumnName
	 * @return
	 */
	public static boolean isColumnsIgnored(String pColumnName){
		pColumnName = pColumnName.trim().toUpperCase();
		return (DBSString.findStringInArray(DBSIO.COLUMNS_IGNORED, pColumnName) > -1);
	}

	/**
	 * Retorna colume da coluna padronizado
	 * @param pColumnName
	 * @return
	 */
	public static String getNormalizedColumnName(String pColumnName){
		String wColumnName = null;
		if (pColumnName != null){
			wColumnName = pColumnName.toUpperCase().trim();
			int	xI = wColumnName.indexOf(".");
			if (wColumnName.indexOf(".") > 0){
				wColumnName = wColumnName.substring(xI + 1);
			}
		}
		return wColumnName;
	}
	/**
	 * Retorna string com a coluna e o valor formatado com a sintaxe adaptada ao padrão do banco da dados 
	 * @param pCn Conexão com o banco de dados
	 * @param pDataType Tipo de dado do valor informado no objeto pValue
	 * @param pValue Objeto contendo o valor a ser utilizado
	 * @param pFieldName Nome da coluna do sql
	 * @return String concatenando a coluna com valor adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException
	 */
	public static String getFieldValueSql(Connection pCn, DATATYPE pDataType, Object pValue, String pFieldName) throws DBSIOException{
		if (pCn == null){
			return null;
		}
		String xValueSQLFormated;
		
		xValueSQLFormated = toSQLValue(pCn, pDataType, pValue);
		if (xValueSQLFormated == null
		 || xValueSQLFormated.toLowerCase().contains("null")){
			return toSQLNull(pCn, pFieldName);
		}
		return pFieldName + " = " + xValueSQLFormated;
	}

	/**
	 * Retorna a data atual segundo o banco de dados.
	 * @param pCn
	 * @return
	 */
	public static Date getDataBaseDate(Connection pCn) {
		Date xData = null;
		if (getDataBaseProduct(pCn) == DB_SERVER.ORACLE) {
	        try {
				xData = DBSDate.toDate(getDado(pCn, "dual", "", "SYSDATE"));
			} catch (DBSIOException e) {
				wLogger.error("getServerDate", e);
			}
		} else if (getDataBaseProduct(pCn) == DB_SERVER.MYSQL){
			try {
				xData = DBSDate.toDate(getDado(pCn, "", "", "SYSDATE()"));
			} catch (DBSIOException e) {
				wLogger.error("getServerDate", e);
			}
		}
		return xData;
	}

	//######################################################################################################### 
	//## Private Methods                                                                                      #
	//#########################################################################################################
	
	//	/**
	//	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	//	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	//	 * @param pDataModel DataModel com os atributos e respectivos valores
	//	 * @param pCn Conexão com o banco
	//	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	//	 * @return Quantidade de registros afetados
	//	 * @throws DBSIOException
	//	 */
	//	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand) throws DBSIOException{
	//		//Procura pela anotação DataModel para posteriomente ler o nome da tabela 
	//		Annotation xAnnotationTmp = pDataModel.getClass().getAnnotation(DataModel.class);
	//		if (xAnnotationTmp instanceof DataModel){
	//			DataModel xAnnotation = (DataModel) xAnnotationTmp;
	//			//Se o nome da tabela foi informado
	//			if (!DBSObject.isEmpty(xAnnotation.tablename())){
	//				return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, pDAOCommand, xAnnotation.tablename(), "");
	//			}
	//		}
	//		return -1;
	//	}
		
	//	@SuppressWarnings("unchecked")
	//	public static <T> T getDataTypeConvertedValue(DATATYPE pDataType, Object pValue) {
	//		return (T) pValue;
	//	}
	//	
	/**
	 * Retorna o valor convertido conforme o DataType
	 * @param pDataType
	 * @param pValue
	 * @return
	 * @throws DBSIOException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toDataTypeValue(DATATYPE pDataType, Object pValue) {
		if (pDataType == null
		 || pValue == null){
			return null;
		}
		//Como alguns Bancos de dados o boolean é definido como numérico
		//converte o valor para 0(false) ou -1(true)
		if (pValue instanceof Boolean){
			if (pDataType == DATATYPE.INT || 
				pDataType == DATATYPE.DECIMAL || 
				pDataType == DATATYPE.DOUBLE){
				pValue = DBSIO.toSQLBoolean(pValue);
			}
		}

		Object xValue = null;
		try{
			Class<?> xClass = pDataType.getJavaClass();
			if (xClass.isAssignableFrom(Boolean.class)){
				xValue = DBSBoolean.toBoolean(pValue);
			}else if (xClass.isAssignableFrom(String.class)){
				xValue = DBSString.toString(pValue);
			}else if (xClass.isAssignableFrom(Integer.class)){
				if (pValue.toString().startsWith("((") && pValue.toString().endsWith("))")) {
					//CAMPOS COM VALORES DEFAULT NO SQLSERVER FICAM ENTRE (())
					xValue = DBSNumber.toInteger(DBSNumber.getOnlyNumber(pValue.toString()));
				}else {
					xValue = DBSNumber.toInteger(pValue);
				}
			}else if (xClass.isAssignableFrom(Double.class)){
				if (pValue.toString().startsWith("((") && pValue.toString().endsWith("))")) {
					//CAMPOS COM VALORES DEFAULT NO SQLSERVER FICAM ENTRE (())
					xValue = DBSNumber.toDouble(DBSNumber.getOnlyNumber(pValue.toString()));
				}else {
					xValue = DBSNumber.toDouble(pValue);
				}
			}else if (xClass.isAssignableFrom(BigDecimal.class)){
				if (pValue.toString().startsWith("((") && pValue.toString().endsWith("))")) {
					//CAMPOS COM VALORES DEFAULT NO SQLSERVER FICAM ENTRE (())
					xValue = DBSNumber.toBigDecimal(DBSNumber.getOnlyNumber(pValue.toString()));
				}else {
					xValue = DBSNumber.toBigDecimal(pValue);
				}
			}else if (xClass.isAssignableFrom(Long.class)){
				xValue = DBSNumber.toLong(pValue);
			}else if (xClass.isAssignableFrom(Timestamp.class)){
				xValue = DBSDate.toTimestamp(pValue);
			}else if (xClass.isAssignableFrom(Date.class)){
				xValue = DBSDate.toDate(pValue);
			}else if (xClass.isAssignableFrom(Time.class)){
				xValue = DBSDate.toTime((String) pValue);
			}else{
				xValue = pValue;
			}
			return (T) xValue;
		}catch(Exception e){
			wLogger.error(e);
			return (T) xValue;
		}finally{
			if (!DBSObject.isEmpty(pValue)
			  && xValue == null){
				wLogger.error("Conversão não foi possível: Valor[" + pValue + "] não pode ser convertido para [" + pDataType.toString() + "]");
			}
		}
	}

	
	/**
	 * Retorna o tipo de dado DBSoft que corresponde ao tipo de dado original do banco de dados
	 * @param pCn conexão a partir do qual serãoverificado o fabricante do bando de dados
	 * @param pDataTypeOriginal Tipo de dado original recuperado diretamente da coluna do banco de dados 
	 * @return Tipo de dado DBSoft que corresponde ao tipo de dado original
	 * @throws DBSIOException 
	 */
	public static DATATYPE toDataType(Connection pCn, int pDataTypeOriginal, int pPrecision){
		DB_SERVER xDBP;
		xDBP = getDataBaseProduct(pCn);
		if (xDBP != null){
			if (xDBP == DB_SERVER.ORACLE){
				if (pDataTypeOriginal == 2 || //NUMBER 
					pDataTypeOriginal == 3 || //NUMBER
					pDataTypeOriginal == 4){
					if (pPrecision == 0 ||
						pPrecision > 14){
						return DATATYPE.DECIMAL;
					}
					if (pPrecision < 10){
						return DATATYPE.INT;
					}else{
						return DATATYPE.DOUBLE;
					}
				}else if (pDataTypeOriginal == 6){ 	//FLOAT
					return DATATYPE.DECIMAL;
				}else if(pDataTypeOriginal == 12 ||
						 pDataTypeOriginal == 1){ //VARCHAR2 OU CHAR
					return DATATYPE.STRING;
				}else if(pDataTypeOriginal == 91){ //DATE
					return DATATYPE.DATE;
				}else if(pDataTypeOriginal == 92){ //TIME
					return DATATYPE.TIME;
				}else if(pDataTypeOriginal == 93){ //DATETIME
					return DATATYPE.DATETIME;
				}else if(pDataTypeOriginal == -7){ //BOOLEAN
					return DATATYPE.BOOLEAN;
				}
			}else if (xDBP.equals(DB_SERVER.MYSQL)) {
				if (pDataTypeOriginal == 3){		//DECIMAL 
					return DATATYPE.DECIMAL;
				}else if (pDataTypeOriginal == 4){	//INT 
					return DATATYPE.INT;
				}else if (pDataTypeOriginal == 6){	//FLOAT
					return DATATYPE.DECIMAL;
				}else if (pDataTypeOriginal == 8){  //DOUBLE
					return DATATYPE.DOUBLE;
				}else if(pDataTypeOriginal == 12){ //VARCHAR2
					return DATATYPE.STRING;
				}else if(pDataTypeOriginal == 91){ //DATE
					return DATATYPE.DATE;
				}else if(pDataTypeOriginal == 92){ //TIME
					return DATATYPE.TIME;
				}else if(pDataTypeOriginal == 93){ //DATETIME
					return DATATYPE.DATETIME;
				}else if(pDataTypeOriginal == -7){ //BOOLEAN
					return DATATYPE.BOOLEAN;
				}
			}else if(xDBP.equals(DB_SERVER.SQLSERVER)) {
				if (pDataTypeOriginal == 2 || //NUMBER 
					pDataTypeOriginal == 3 || //NUMBER
					pDataTypeOriginal == 4){
					if (pPrecision == 0 ||
						pPrecision > 14){
						return DATATYPE.DECIMAL;
					}
					if (pPrecision < 11){
						return DATATYPE.INT;
					}else{
						return DATATYPE.DOUBLE;
					}
				}else if (pDataTypeOriginal == 3){		//DECIMAL 
					return DATATYPE.DECIMAL;
				}else if (pDataTypeOriginal == 4){	//INT 
					return DATATYPE.INT;
				}else if (pDataTypeOriginal == 6){	//FLOAT
					return DATATYPE.DECIMAL;
				}else if (pDataTypeOriginal == 8){  //DOUBLE
					return DATATYPE.DOUBLE;
				}else if(pDataTypeOriginal == 12){ //VARCHAR2
					return DATATYPE.STRING;
				}else if(pDataTypeOriginal == 91){ //DATE
					return DATATYPE.DATE;
				}else if(pDataTypeOriginal == 92){ //TIME
					return DATATYPE.TIME;
				}else if(pDataTypeOriginal == 93){ //DATETIME
					return DATATYPE.DATETIME;
				}else if(pDataTypeOriginal == -7){ //BOOLEAN
					return DATATYPE.BOOLEAN;
				}
			}
		}
		return DATATYPE.STRING;
	}

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
	 * @param pCn conexão com o banco de dados
	 * @param pDataType Tipo de dado do valor informado no objeto pValue
	 * @param pValue Objecto contendo o valor a ser utilizado
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String toSQLValue(Connection pCn, DATATYPE pDataType, Object pValue){
		if (pDataType == DATATYPE.STRING){
			return toSQLString(pCn, pValue);
		}else if (pDataType == DATATYPE.BOOLEAN){
			return toSQLBoolean(pCn, pValue);
		}else if (pDataType == DATATYPE.DATE){
			return toSQLDate(pCn, pValue);
		}else if (pDataType == DATATYPE.TIME){
			return toSQLTime(pCn, pValue);
		}else if (pDataType == DATATYPE.DATETIME){
			return toSQLDateTime(pCn, pValue);
		}else if (pDataType == DATATYPE.DECIMAL || 
				  pDataType == DATATYPE.DOUBLE ||
				  pDataType == DATATYPE.INT ||
				  pDataType == DATATYPE.ID) {
			return toSQLNumber(pCn, pValue);
		}else if (pDataType == DATATYPE.COMMAND){
			return DBSString.toString(pValue);
		}else{
			return null;
		}
	}
	/**
		 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
		 * @param pCn conexão com o banco da dados
		 * @param pValue Valor a ser utilizado  
		 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
		 */
		public static String toSQLString(Connection pCn, Object pValue){
			String xValue = DBSString.toString(pValue);
			if (xValue==null){
				return "NULL";
			}
			xValue = DBSString.changeStr(xValue, "'", "''");
			xValue = DBSString.changeStr(xValue, "\\", "\\\\");
			//TODO ENCONTRAR UMA FORMA MELHOR PARA A SITUAÇÃO ABAIXO - AVILA
			//DEVE-SE RECEBER A STRING JÁ CONVERTIDA, ISTO É, SEM OS CARACTERES ABAIXO - RICARDO
			xValue = DBSString.changeStr(xValue, "╔", "É");
			xValue = DBSString.changeStr(xValue, "ß", "á");
			return "'" + xValue + "'";
		}

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
	 * @param pCn conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String toSQLDate(Connection pCn, Object pValue){
		if (pValue==null){
			return "NULL";
		}
		DB_SERVER xDBP = getDataBaseProduct(pCn);
		if(xDBP!=null){ 
			if (xDBP == DB_SERVER.ORACLE){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedDate(DBSDate.toDate(pValue)) + "','dd/mm/yyyy')";
				}else{
					return "''";
				}
			}else if (xDBP == DB_SERVER.POSTGRESQL){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedDate(DBSDate.toDate(pValue)) + "','dd/mm/yyyy')";
				}else{
					return "''";
				}
			}else if(xDBP == DB_SERVER.SQLSERVER){
	//			if (pValue==null){
	//				return "0";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "'" + DBSFormat.getFormattedDateCustom(DBSDate.toDate(pValue), "yyyy-MM-dd") + "'" ;
				}else{
					return "0";
				}
			}else if(xDBP == DB_SERVER.MYSQL){
	//			if (pValue==null){
	//				return null;
	//			}else if (DBSDate.isDate(pValue.toString())){ 
				if (DBSDate.isDate(pValue.toString())){ 
					//return "STR_TO_DATE('" + DBSFormat.getFormattedDate((Date)pValue) + "','%d/%m/%Y')";
					return "STR_TO_DATE('" + DBSFormat.getFormattedDate(DBSDate.toDate(pValue))+ "','%d/%m/%Y')";
				}else{
					return "0";
				}
			}
		}
		return "'" + pValue + "'";
	}

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
	 * @param pCn conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String toSQLDateTime(Connection pCn, Object pValue){
		if (pValue==null){
			return "NULL";
		}
		DB_SERVER xDBP = getDataBaseProduct(pCn);
		if(xDBP!=null){ 
			if (xDBP == DB_SERVER.ORACLE){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedDateTimes(DBSDate.toTimestamp(pValue)) + "','dd/mm/yyyy hh24:mi:ss')";
				}else{
					return "''";
				}
			}else if (xDBP == DB_SERVER.POSTGRESQL){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedDateTimes(DBSDate.toTimestamp(pValue)) + "','dd/mm/yyyy hh24:mi:ss')";
				}else{
					return "''";
				}
			}else if(xDBP == DB_SERVER.SQLSERVER){
	//			if (pValue==null){
	//				return "0";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "'" + DBSFormat.getFormattedDateCustom(DBSDate.toTimestamp(pValue),"yyyy-MM-dd hh:mm:ss") + "'"; //"DateValue('" + DBSFormat.getFormattedDateTimes(DBSDate.toTimestamp(pValue)) + "')";
				}else{
					return "0";
				}
			}else if(xDBP == DB_SERVER.MYSQL){
	//			if (pValue==null){
	//				return "0";
	//			}else if (DBSDate.isDate(pValue.toString())){
	//				return "STR_TO_DATE('" + DBSFormat.getFormattedDate((Date)pValue) + "','%d/%m/%Y %T')";
				if (DBSDate.isDate(pValue.toString())){
					return "TIMESTAMP('" + pValue.toString() + "')";
				}else{
					return "0";
				}
			}
		}
		return "'" + pValue + "'";
	}

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
	 * @param pCn conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String toSQLTime(Connection pCn, Object pValue) {
		if (pValue==null){
			return "NULL";
		}
		DB_SERVER xDBP = getDataBaseProduct(pCn);
		if(xDBP!=null){ 
			if (xDBP == DB_SERVER.ORACLE){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedTimes((Date)pValue) + "','hh24:mi:ss')";
				}else{
					return "''";
				}
			}else if (xDBP == DB_SERVER.POSTGRESQL){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedTimes((Date)pValue) + "','hh24:mi:ss')";
				}else{
					return "''";
				}
			}else if(xDBP == DB_SERVER.SQLSERVER){
	//			if (pValue==null){
	//				return "0";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "('" + DBSFormat.getFormattedTimes((Date)pValue) + "')";
				}else{
					return "0";
				}
			}else if(xDBP == DB_SERVER.MYSQL){
	//			if (pValue==null){
	//				return "0";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "STR_TO_DATE('" + DBSFormat.getFormattedTimes((Date)pValue) + "',%T')";
				}else{
					return "0";
				}
			}
		}
		return "'" + pValue + "'";
	}

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
	 * @param pCn conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String toSQLNumber(Connection pCn, Object pValue) {
		if (pValue==null){
			return "NULL";
		}
		String xValue = pValue.toString();
		
		//Evita a coversão se número for inteiro
		if (DBSNumber.isInteger(pValue)){
			return xValue;
		}
		
		//Converte valor para String para evitar números em notação científica. 
		if (pValue instanceof Number){
			xValue = DBSNumber.toBigDecimal(DBSNumber.toDouble(pValue)).toPlainString();
		}
		
		xValue = "'" + xValue + "'";

		DB_SERVER xDBP;
		xDBP = getDataBaseProduct(pCn);
		if (xDBP != null){
			if (xDBP == DB_SERVER.ORACLE){
				String xNLS = ".,"; //Decimal e separador de grupo
				if (DBSNumber.isNumber(pValue.toString())){
					return "To_Number(" + xValue + ",'9999999999999999999999999999999D999999999999999999999999999999','NLS_NUMERIC_CHARACTERS = ''" + xNLS + "''')";
				}else{
					return "''";
				}
			}else if(xDBP == DB_SERVER.SQLSERVER){
				if (DBSNumber.isNumber(pValue.toString())){
					return "CAST(" + xValue + " AS DECIMAL(38,23))";
				}else{
					return "0";
				}
			}else if(xDBP == DB_SERVER.MYSQL){
				if (DBSNumber.isNumber(pValue.toString())){
					return "CAST(" + xValue + " AS DECIMAL(60,30))";
				}else{
					return "0";
				}
			}else if(xDBP == DB_SERVER.DB2){
				if (DBSNumber.isNumber(pValue.toString())){
					return "CAST(" + xValue + " AS DECIMAL(60,30))";
				}else{
					return "0";
				}
			}
		}
		return "'" + xValue + "'";
	}

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada para -1(true) e 0(false). 
	 * @param pCn conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 */
	public static String toSQLBoolean(Connection pCn, Object pValue){
		return toSQLBoolean(pCn, pValue, false);
	}
	

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada para -1(true) e 0(false). 
	 * @param pCn
	 * @param pValue
	 * @param pDefaultValue
	 * @return
	 */
	public static String toSQLBoolean(Connection pCn, Object pValue, Boolean pDefaultValue){
		Boolean xB = DBSBoolean.toBoolean(pValue, pDefaultValue);
		if (xB){
			return "-1";
		}else{
			return "0";
		}
	}

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada para -1(true) e 0(false) 
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 */
	public static String toSQLBoolean(Object pValue){
		return toSQLBoolean(null, pValue);
	}

	/**
	 * Retorna uma string sql com a sintaxe formatada 
	 * @param pCampo Campo do sql a ser formatado
	 * @param pValue True ou False
	 * @return Retorna uma string sql com a sintaxe Boolean
	 */
	public static String toSQLBoolean(String pCampo, Boolean pValue){
		if (DBSObject.isEmpty(pCampo) ||
			DBSObject.isEmpty(pValue)){
			return "";
		}
		if (pValue){
			return " (" + pCampo + " = 1 or " + pCampo + " = -1) ";
		}
		return " " + pCampo + " = 0 ";
	}

	/**
	 * Retorna String com comando SQL formatado para dados Nulos conforme o tipo de Banco de Dados
	 * @param pCn conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 */
	public static String toSQLNull(Connection pCn, String pColumnName){
		return pColumnName + " IS NULL ";
	}

	/**
	 * Retorna String com comando SQL formatado para dados nulos ou iqual ao valor informado.<br/>
	 * O valor informado deverá já estar convertido com o commando toSQL.<br/>
	 * ex:toSQLNullOrValue(Conexao, "NOMEDACOLUNA", toSQLBoolean(Conexao, "NOMEDACOLUNA", valor));
	 * @param pCn
	 * @param pColumnName
	 * @param pValue String com o comando apropriado de conversão. 
	 * @return
	 */
	public static String getSQLWhereNullOrValue(Connection pCn, String pColumnName, String pValue){
		return getSQLWhereNullOrValue(pCn, pColumnName, pValue, "=");
	}
	
	/**
	 * Retorna String com comando SQL formatado para dados nulos ou dentro da condição com o valor informado.<br/>
	 * O valor informado deverá já estar convertido com o commando toSQL.<br/>
	 * ex:toSQLNullOrValue(Conexao, "NOMEDACOLUNA", toSQLBoolean(Conexao, "NOMEDACOLUNA", valor), ">=");
	 * @param pCn
	 * @param pColumnName
	 * @param pValue String com o comando apropriado de conversão. 
	 * @return
	 */
	public static String getSQLWhereNullOrValue(Connection pCn, String pColumnName, String pValue, String pCondition){
		return "(" + toSQLNull(pCn, pColumnName) + "OR " + pColumnName.trim() + " " + pCondition.trim() + " " + pValue.trim() + ")";
	}
	
	/**
		 * Retorna o valor convertido conforme o DataType
		 * @param pDataType
		 * @param pValue
		 * @return
		 */
	//	public static final Object getDataTypeConvertedValue(DATATYPE pDataType, Object pValue){
	//  	    if (pDataType==DATATYPE.BOOLEAN){
	//	      	if (pValue == null ||
	//      			pValue.equals("0") ||
	//      			pValue.equals("") ||
	//      			pValue.equals("false")){
	//	      		return false;
	//	        }else if (pValue.equals("-1") ||
	//  	        		  pValue.equals("1") ||
	//	        		  pValue.equals("true")){
	//	        	return true;
	//	        }
	//  	    }
	//  	    return pValue;
	//	}
		
		public static String toSQLTrunc(Connection pCn, String pValue, String pCasas) throws DBSIOException {
			DB_SERVER xDBP = getDataBaseProduct(pCn);
			if (xDBP == DB_SERVER.ORACLE){
				return " TRUNC(" + pValue + ", " + pCasas + ") ";
			}else if (xDBP == DB_SERVER.MYSQL){
				return " TRUNCATE(" + pValue + ", " + pCasas + ") ";
			} else {
				return " TRUNCATE(" + pValue + ", " + pCasas + ") ";
			}
		}
		
	/**
	 * Retorna String com comando IFF ou Decode formatado conforme o tipo de Banco de Dados
	 * @param pCn Conexão com o banco para itentificar qual o fabricante
	 * @param pSeCampo Dado do Campo a ser testado;
	 * @param pIgualA Dado a ser comparado ao campo pSeCampo;
	 * @param pUsa Dado a ser considerado quando pSeCampo for Igual a pIgualA;
	 * @param pSenao Dado a ser considerado quando pSeCampo for Diferente a pIgualA.
	 * @return String com comando formatado.
	 */
	public static String toSQLIf(Connection pCn, String pSeCampo, String pIgualA, String pUsa, String pSenao) {
//        return "(CASE WHEN " + pSeCampo + "=" + pIgualA + " THEN " + pUsa + " ELSE " + pSenao + " END)";
		DB_SERVER xDBP = getDataBaseProduct(pCn);
		if (xDBP == DB_SERVER.ORACLE) {
	        return "Decode(" + pSeCampo + "," + pIgualA + "," + pUsa + "," + pSenao + ")";
	    } else if (xDBP == DB_SERVER.MYSQL) {
	        return "if(" + pSeCampo + "=" + pIgualA + "," + pUsa + "," + pSenao + ")";
	    } else {
	    	return "iif(" + pSeCampo + "=" + pIgualA + "," + pUsa + "," + pSenao + ")";
	    }
	}

	/**
	 * Retorna String com comando dateDiff ou a subtração de duas datas
	 * @param pCn Conexão com o banco para itentificar qual o fabricante
	 * @param pData1 Primeira Data;
	 * @param pData2 Segunda Data
	 * @return String com comando formatado.
	 */
	public static String toSQLSubstr(Connection pCn, String pCampo, int pStart, int pLength) {
		DB_SERVER xDBP = getDataBaseProduct(pCn);
		if (xDBP == DB_SERVER.ORACLE) {
			return "SUBSTR(" + pCampo + "," + pStart + ","+pLength + ")"  ;
	    } else {
			return "SUBSTRING(" + pCampo + "," + pStart + ","+pLength + ")"  ;
	    }
	}

	
	/**
	 * Retorna String com comando dateDiff ou a subtração de duas datas
	 * @param pCn Conexão com o banco para itentificar qual o fabricante
	 * @param pData1 Primeira Data;
	 * @param pData2 Segunda Data
	 * @return String com comando formatado.
	 */
	public static String toSQLDateDiff(Connection pCn, String pData1, String pData2) {
		DB_SERVER xDBP = getDataBaseProduct(pCn);
		if (xDBP == DB_SERVER.SQLSERVER) {
			return "datediff( day,"+ pData2 +"," + pData1 + " )" ;
	    } else {
	    	return "CAST( " + pData1 + " AS DATE )" + "-" + " CAST(" + pData2 + " AS DATE)";
	    }
	}

	/**
	 * Retorna String com o comando Bitwise AND que executa comparação binária entre o pCampo e o pValor
	 * @param pCn Conexão com o banco para itentificar qual o fabricante
	 * @param pCampo
	 * @param pValor
	 * @return
	 */
	public static String toSQLBitAnd(Connection pCn, String pCampo, int pValor) {
		DB_SERVER xDBP = getDataBaseProduct(pCn);
		if (xDBP == DB_SERVER.ORACLE) {
	        return " BITAND(" + pCampo + "," + pValor + ") = " + pValor;
	    } else if (xDBP == DB_SERVER.MYSQL) {
	    	return " (" + pCampo + " & " + pValor + ") = " + pValor;
	    } else {
	    	return " (" + pCampo + " & " + pValor + ") = " + pValor;
	    }
	}
	
	/**
	 * Recupera o nome das tabelas ou o <i>Alias</i> que fazem parte da Query SQL.
	 * @param pSQLQuery Query SQL a ser resquisada
	 * @param pReturnAlias Se retorna o nome real da tabela ou o <i>Alias</i> se existir. 
	 * @return Array com os nomes da tabelas, sendo a primeira, index = 0.
	 */
	public static List<String> getTablesFromQuery(String pSQLQuery, boolean pReturnAlias){
		return getTablesFromQuery(pSQLQuery, pReturnAlias, null);
	}

	/**
	 * Recupera o nome das tabelas ou o <i>Alias</i> que fazem parte da Query SQL.
	 * Caso não exista <i>Alias</i> será retornardo o mesmo noma da tabela.
	 * @param pSQLQuery Query SQL a ser resquisada
	 * @param pReturnAlias Se retorna o nome real da tabela ou o <i>Alias</i> se existir. 
	 * @param pNameToFind Se <b>pReturnAlias=true</b> retorna o nome do <i>Alias</i> que representa a tabela informada em <b>pTableNameToFind</b>.Se tabela não tiver <i>Alias</i>, retorna o próprio nome da tabela.<br/>
	 *   					   Se <b>pReturnAlias=false</b> retorna o nome da tabela que representa o <i>Alias</i> informado em <b>pTableNameToFind</b>.<br/>
	 *   					   Se for nulo, retorna todas as tabelas ou <i>Alias</i> da query.
	 * @return Array com os nomes da tabelas, sendo a primeira, index = 0.
	 */
	public static List<String> getTablesFromQuery(String pSQLQuery, boolean pReturnAlias, String pNameToFind){
		int xI;
		int xF;
		boolean xFindName = !DBSObject.isEmpty(pNameToFind);
		String xS = pSQLQuery.trim(); 
		String xAliasName;
		List<String> xTmps;
		List<String> xBlocks;
		List<String> xTables =  new ArrayList<String>();
		xI = DBSString.getInStr(xS.toUpperCase(), " FROM ",false);
		if (xI == 0){
			return null;
		}else {
			xI+=6; //Adiciona 6 para desconsiderar a própria string " FROM "
		}
		//Determina onde termina a clausula FROM
		xF = DBSString.getInStr(xS," WHERE ", false);
		if (xF==0){
			xF = DBSString.getInStr(xS," LIKE ", false);
			if (xF==0){
				xF = DBSString.getInStr(xS," GROUP BY ", false);
				if (xF==0){
					xF = DBSString.getInStr(xS," HAVING ", false);
					if (xF==0){
						xF = DBSString.getInStr(xS," ORDER BY ", false);
						if (xF==0){
							xF = xS.length();
						}
					}
				}
			}
		}
		
		//Recupera somente o texto referente a clausula "FROM" do texto original mantendo a caixa do texto
		xS = DBSString.getSubString(pSQLQuery, xI, xF - xI + 1);
	
		//Exclui textos de comandos
		xS = DBSString.changeStr(xS, " INNER ", " ", false);
		xS = DBSString.changeStr(xS, " OUTER ", " ", false);
		xS = DBSString.changeStr(xS, " LEFT ", " ", false);
		xS = DBSString.changeStr(xS, " RIGHT ", " ", false);
		xS = DBSString.changeStr(xS, " NATURAL ", " ", false);
		
		//Recupera os nomes das tabelas ou o Alias 
		xBlocks = DBSString.toArrayList(xS,",");
		for (String xBlock:xBlocks){
			xTmps = DBSString.toArrayList(xBlock," JOIN ", false);
			for (String xTable:xTmps){
				int x = 0;
				x = DBSString.getInStr(xTable," ON ", false);
				if (x > 0){
					//Considera o texto anterior ao " ON " como sendo o nome da tabela
					xTable = DBSString.getSubString(xTable, 1, x);
				}
				x = DBSString.getInStr(xTable," AS ", false);
				if (x > 0){
					//Salva o Alias
					xAliasName = DBSString.getSubString(xTable, x + 4, xTable.length());
					//Salva o Nome da tabela
					xTable = DBSString.getSubString(xTable, 1, x - 1);
				}else{
					//Alias será igual ao nome.
					xAliasName = xTable;
				}
	
				//Se for para retornar somente o alias que representa a tabela informada ou vice-versa.
				if (xFindName){
					if (pReturnAlias){
						//Retorna o Alias se o nome da tabela informado for iqual a tabela lida
						if (pNameToFind.equalsIgnoreCase(xTable)){
							xTables.add(xAliasName.trim());
							break;
						}
					}else{
						//Retorna a tabela se o nome do alias informado for iqual ao alias lido
						if (pNameToFind.equalsIgnoreCase(xAliasName)){
							xTables.add(xTable.trim());
							break;
						}
					}
				}else{
					if (pReturnAlias){
						xTables.add(xAliasName.trim());
					}else{
						xTables.add(xTable.trim());
					}
				}
			}
		}
		return xTables;
	}
	
	
	//######################################################################################################### 
	//## Private Methods                                                                                      #
	//#########################################################################################################

	//	/**
	//	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	//	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	//	 * @param pDataModel DataModel com os atributos e respectivos valores
	//	 * @param pCn Conexão com o banco
	//	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	//	 * @return Quantidade de registros afetados
	//	 * @throws DBSIOException
	//	 */
	//	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand) throws DBSIOException{
	//		//Procura pela anotação DataModel para posteriomente ler o nome da tabela 
	//		Annotation xAnnotationTmp = pDataModel.getClass().getAnnotation(DataModel.class);
	//		if (xAnnotationTmp instanceof DataModel){
	//			DataModel xAnnotation = (DataModel) xAnnotationTmp;
	//			//Se o nome da tabela foi informado
	//			if (!DBSObject.isEmpty(xAnnotation.tablename())){
	//				return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, pDAOCommand, xAnnotation.tablename(), "");
	//			}
	//		}
	//		return -1;
	//	}
		
	/**
	 * Retorna sequence do registro recem incluído
	 * @param pCn
	 * @param pSQL
	 * @param pSequenceName
	 * @return
	 * @throws DBSIOException
	 */
	private static BigDecimal pvGetOracleSequenceValue(Connection pCn, String pSQL, String pSequenceName) throws DBSIOException {
		if (pCn == null || pSQL == null || pSequenceName == null){
			return DBSNumber.toBigDecimal(0D);
		}
		BigDecimal xId;
		xId = DBSNumber.toBigDecimal(getDado(pCn, "DUAL", "", pSequenceName.trim().toUpperCase() + ".CURRVAL"));
		xId = DBSNumber.toBigDecimal(DBSObject.getNotNull(xId, -1));
		return xId;
	}

	private static void pvGetConnectionTimeout(Connection pCn, Throwable e, int pTimeout, int pTimes) throws DBSIOException{
		if (pTimeout == 0) {
			throwIOException(e, pCn);
		}else{
			wLogger.error("Sem conexão. Fazendo nova tentativa em seguida.");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				throwIOException("Tarefa interrompida");
			}
			pTimes ++;
			if (pTimes == pTimeout){
				throwIOException("Tempo expirado na tentativa de conexão");
			}
		}
	}

	
	/**
	 * Executa o comando Commit no Banco de Dados
	 * @param pCn conexão com o Banco de dados
	 * @return true = Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	private static boolean pvCommitTrans(Connection pCn) throws DBSIOException{
		try {
			if (pCn.isClosed()){
				wLogger.error("DBSIO:pvCommitTrans:Conexão fechada");
				return false;
			}else{
				pCn.commit();
				return true;
			}
		} catch (SQLException e) {
			throwIOException(e);
			return false;
		}
	}

	/**
	 * Desfaz as modificações no banco de dados, efetuada desde o Savepoint informado
	 * @param pCn conexão com o banco de dados
	 * @param pSavePoint Savepoint a partir do qual serão desfeitas as modificações
	 * @return true = Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	private static boolean pvRollbackTrans(Connection pCn, Savepoint pSavePoint) throws DBSIOException{
		try {
			if (pCn.isClosed()){
				wLogger.error("DBSIO:pvRollbackTrans:Conexão fechada");
				return false;
			}else{
				if (DBSObject.isEmpty(pSavePoint)){ 
					pCn.rollback();
					return true;
				}else{
					pCn.rollback(pSavePoint);
					return true;
				}
			}
		} catch (SQLException e) {
			throwIOException(e, pCn);
			return false;
		}
	}
	
	/**
	 * Desfaz as modificações no banco de dados
	 * @param pCn conexão com o banco de dados
	 * @return true = Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	private static boolean pvRollbackTrans(Connection pCn) throws DBSIOException{
		return pvRollbackTrans(pCn, null);
	}

	/**
	 * Move o valor informado para a coluna do tributo da classe informada
	 * @param pDataModel Instancia da classe
	 * @param pField atributo/coluna
	 * @param pValue valor do atributo
	 */
	private final static <T> boolean pvSetDataModelFieldValue(T pDataModel, Field pField, Object pValue){
		if (pDataModel != null){
			int xM = 0x0;
			xM |= Modifier.FINAL;
			//Verifica se atributo do campo é FINAL, o que faz com que não seja possível alterar o valor
			if ((pField.getModifiers() & xM) != xM){
				pField.setAccessible(true);
				try {
					//Converte o valor para o tipo de classe do campo que receberá
					pValue = DBSObject.toClassValue(pValue, pField.getType()); 
					//Move o valor
					pField.set(pDataModel, pValue);
					return true;
				} catch (IllegalArgumentException e) {
					if (pValue == null){
						wLogger.error("Valor nulo. " + e.getMessage());
					}else{
						wLogger.error("Valor inválido. " + e.getMessage());
					}
				} catch (SecurityException | IllegalAccessException e) {
					wLogger.error("pvSetDataModelFieldValue", e);
				}
			}
		}else{
			wLogger.error("DataModel nulo!");
		}
		return false;
	}

	
	/**
	 * Retorna o conteúdo do WHERE para a construção da SQL de comando do DBSDAOBase
	 * pvIOBuildSqlCommandWhere
	 * @param <A>
	 * @param pDAO DBSDAOBase a ser utilizado
	 * @param pCommand Comando conforme constante COMMAND
	 * @return WHERE do comando SQL preenchido
	 * @throws DBSIOException 
	 */
	private static <T> String pvGetDAOSQLCommandWhere(DBSDAO<T> pDAO, COMMAND pCommand) throws DBSIOException{
		//String xDummyDataField = "";
		String xSQLWhere = "";

		for (DBSColumn xColumn:pDAO.getCommandColumns()){
			//Se coluna pertence a pesquisa..
			if (!xColumn.getColumnName().equals("")){//Se coluna estiver realmente vinculada a uma coluna na tabela
				if (xColumn.getPK()){ //Se columna for PK
					//Recupera valor via getValue, para deixar a cargo do DAO a origem do valor(dataModel ou commandColumns);  
					Object xValue = pDAO.getValue(xColumn.getColumnName());
					if (!xSQLWhere.equals("")){
						xSQLWhere = xSQLWhere + " AND ";
					}
					if (xValue==null){
						xSQLWhere = xSQLWhere + toSQLNull(pDAO.getConnection(), pDAO.getCommandTableName() + "." + xColumn.getColumnName());
					}else{
						xSQLWhere = xSQLWhere + pDAO.getCommandTableName() + "." + xColumn.getColumnName() + "=" + toSQLValue(pDAO.getConnection(), xColumn.getDataType(), xValue);
					}
				}
			}
		}
		if (!xSQLWhere.equals("")){
			xSQLWhere = " WHERE " + xSQLWhere;
		}
		if (pCommand.equals(COMMAND.LOCK)){
			xSQLWhere = xSQLWhere + " FOR UPDATE NOWAIT";
		}
		return xSQLWhere;
	}

	/**
	 * Construi string com os valores das colunas para o INSERT, UPDATE ou SELECT.<br/>
	 * @param pDAO
	 * @param pCommand
	 * @return
	 * @throws DBSIOException
	 */
	private static final <T> String pvGetDAOSQLCommandColumns(DBSDAO<T> pDAO, COMMAND pCommand) throws DBSIOException{
		String xSQLColumns = "";
		String xVirgula = "";
		if (pCommand==COMMAND.LOCK){
			xSQLColumns = " * ";
		}else if(pCommand==COMMAND.INSERT){
			xSQLColumns = "(";
			for (DBSColumn xColumn:pDAO.getCommandColumns()){
				//Se coluna pertence a pesquisa..
				if (!DBSObject.isNull(xColumn.getValue())){//Se coluna estiver realmente vinculada a uma coluna na tabela
					//Se valor foi informado pelo usuário ou não
					if (!pDAO.getExecuteOnlyChangedValues() 
					 || xColumn.getChanged()){ 
						xSQLColumns += xVirgula + pDAO.getCommandTableName() + "." + xColumn.getColumnName();
						xVirgula = ",";
					}
				}
			}
			xVirgula = "";
			xSQLColumns += ") VALUES(";
			for (DBSColumn xColumn:pDAO.getCommandColumns()){
				//Se coluna pertence a pesquisa..
				if (!DBSObject.isNull(xColumn.getValue())){//Se coluna estiver realmente vinculada a uma coluna na tabela
					//Se valor foi informado pelo usuário ou não
					if (!pDAO.getExecuteOnlyChangedValues() 
					  || xColumn.getChanged()){ 
						xSQLColumns += xVirgula + toSQLValue(pDAO.getConnection(), xColumn.getDataType(), xColumn.getValue());
						xVirgula = ",";
					}
				}
			}
			xSQLColumns += ")";
		}else if(pCommand==COMMAND.UPDATE){
			/*Comentado em 14/07/2014 para evitar update sem haver alteração de valor.
			 * Este mecanismo era utilizado para forçar que possíveis triggers fossem disparados quando ocorresse o update.
			*/
//			String xSQLColumnNecessaria = "";
			for (DBSColumn xColumn:pDAO.getCommandColumns()){
				//Se coluna pertence a pesquisa..
				if (!xColumn.getColumnName().equals("")){//Se coluna estiver realmente vinculada a uma coluna na tabela
					//Verifica se a coluna é PK para evitar que seja alterada
					if (!xColumn.getPK()){
						
						//Atualiza a coolna quando...
						if ((pDAO.getCurrentRowIndex() == -1 && xColumn.getChanged()) //Não ha registro corrente e valor foi informado via setValue
					      || (pDAO.getCurrentRowIndex() != -1 //Ja existe um registro corrente o valor é diferente do anterior, ou se deseja atualizar todas as colunas
					      	&& (!DBSObject.getNotNull(xColumn.getValue(),"").equals(DBSObject.getNotNull(xColumn.getValueOriginal(),""))
					      	 || !pDAO.getExecuteOnlyChangedValues()))){ 
							xSQLColumns += xVirgula + pDAO.getCommandTableName() + "." + xColumn.getColumnName() + "=" + toSQLValue(pDAO.getConnection(), xColumn.getDataType(), xColumn.getValue());
							xVirgula = ",";
						}
						
						//Verifica se houve alteração de valor indepententemente do valor ter sido informado pelo usuário.
//						if (!pDAO.getExecuteOnlyChangedValues() 
//						 || !DBSObject.getNotNull(xColumn.getValue(),"").equals(DBSObject.getNotNull(xColumn.getValueOriginal(),""))){
//							xSQLColumns += xVirgula + pDAO.getCommandTableName() + "." + xColumn.getColumnName() + "=" + getValueSQLFormated(pDAO.getConnection(), xColumn.getDataType(), xColumn.getValue());
//							xVirgula = ",";
//						}
					}
				}
			}
			/*
			 * Comentado: ver comentário acima
			 */
			//Se não existem colunas que sofrerão o update, por não ter havido alteração de valores das respectivas colunas...
//			if (xSQLColumns.equals("")){
//				//Força a existência de pelo menos uma coluna para obrigar que o update seja realizado
//				xSQLColumns = xSQLColumnNecessaria;
//			}
		}else if(pCommand==COMMAND.SELECT){
			for (DBSColumn xColumn:pDAO.getCommandColumns()){
				//Se coluna pertence a pesquisa..
				if (!xColumn.getColumnName().equals("")){//Se coluna estiver realmente vinculada a uma coluna na tabela
//					//Se valor foi informado pelo usuário ou não
//					if (!pDAO.getExecuteOnlyChangedValues() 
//					  || xColumn.getChanged()){ 
						xSQLColumns += xVirgula + pDAO.getCommandTableName() + "." + xColumn.getColumnName();
						xVirgula = ",";
//					}
				}
			}
		}
		return xSQLColumns;
	}

	/**
	 * Posiciona em novo registro dentro do pDataModel, conforme a direção selecionada.
	 * @param pDAODataModel
	 * @param pDirection
	 * @return true = encontrou registro; false = NÃO encontrou.
	 */
	private static boolean pvMoveResultDataModel(@SuppressWarnings("rawtypes") javax.faces.model.DataModel pDataModel, MOVE_DIRECTION pDirection){
		if (pDataModel !=null){
			int xRowIndex = getIndexAfterMove(pDataModel.getRowIndex(), pDataModel.getRowCount(), pDirection);
			if (getIndexAfterMoveIsOk(pDataModel.getRowIndex(), xRowIndex, pDataModel.getRowCount(), pDirection)){
				pDataModel.setRowIndex(xRowIndex);
				return true;
			}
		}
		return false;
	}

		
	/**
	 * Copia os valores do dataModel origem(pSourceDataModel) para o dataModel destino(pTargetDataModel), desde que encontre campos com o mesmo nome.<br/>
	 * Esta rotina ainda não contempla datamodel recursivo, mas aceita classes extendidas. 
	 * @param pSourceDataModelClass Classe do datamodel origem
	 * @param pSourceDataModel DataModel origem
	 * @param pTargetDataModel DataModel destino
	 */	
	private final static <T,T2> void pvCopyDataModelFieldsValue(Class<?> pSourceDataModelClass, T pSourceDataModel, T2 pTargetDataModel){
		for (Field xField:pSourceDataModelClass.getDeclaredFields()){
			// Verifica se o campo é um datamodel da dbsoft (Anotação @DBSTableModel)
			// Se for irá procurar dentro dos fields do datamodel
			Annotation xAnnotationTmp = xField.getType().getAnnotation(DBSTableModel.class);
			if (xAnnotationTmp instanceof DBSTableModel){
				//Chamada recursiva não implementada
			}else{
				Field yField = findDataModelField(pTargetDataModel.getClass(), xField.getName());
				if (yField != null){
					try {
						xField.setAccessible(true);
						pvSetDataModelFieldValue(pTargetDataModel, yField, xField.get(pSourceDataModel));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						wLogger.error("copyDataModelFieldsValue", e);
					}
				}
			}
		}

		//Chamada recurva para procurar campo na superclass, se existir
		if (pSourceDataModelClass.getSuperclass() != null){
			pvCopyDataModelFieldsValue(pSourceDataModelClass.getSuperclass(), pSourceDataModel, pTargetDataModel);
		}
	}
	
	private final static <T,T2> boolean pvDataModelFieldsValueIsEqual(Class<?> pSourceDataModelClass, T pSourceDataModel, T2 pTargetDataModel){
		boolean 					xIguais = true;
		List<Field> xDeclaredFields = new ArrayList<Field>();
		for (Field xField:pSourceDataModelClass.getDeclaredFields()){
			xDeclaredFields.add(xField);
		}
		for (Field xField:pSourceDataModelClass.getSuperclass().getDeclaredFields()){
			xDeclaredFields.add(xField);
		}
		for (Field xField:xDeclaredFields){
			// Verifica se o campo é um datamodel da dbsoft (Anotação @DBSTableModel)
			// Se for irá procurar dentro dos fields do datamodel
			Annotation xAnnotationTmp = xField.getType().getAnnotation(DBSTableModel.class);
			if (xAnnotationTmp instanceof DBSTableModel){
				//Chamada recursiva não implementada
			}else{
				Field yField = findDataModelField(pTargetDataModel.getClass(), xField.getName());
				if (yField != null){
					try {
						xField.setAccessible(true);
						if (!DBSObject.isEqual(xField.get(pSourceDataModel), xField.get(pTargetDataModel))) {
							xIguais = false;
//							pMessages.add(new DBSMessage(MESSAGE_TYPE.ERROR, "Valor alterado."), xField.getName());
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						wLogger.error("copyDataModelFieldsValue", e);
					}
				}
			}
		}

//		//Chamada recurva para procurar campo na superclass, se existir
//		if (pSourceDataModelClass.getSuperclass() != null){
//			xMessages.addAll(pvDataModelFieldsValueIsEqual(pSourceDataModelClass.getSuperclass(), pSourceDataModel, pTargetDataModel));
//		}
//		return xMessages;
		return xIguais;
	}
	
}
