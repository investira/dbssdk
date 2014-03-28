package br.com.dbsoft.util;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.model.ResultDataModel;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.dbsoft.annotation.DataModel;
import br.com.dbsoft.core.DBSSDK.IO.DB_SERVER;
import br.com.dbsoft.core.DBSSDK.IO.DATATYPE;
import br.com.dbsoft.core.DBSSDK.UI.COMBOBOX.NULL_TEXT;
import br.com.dbsoft.error.DBSError;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.io.DBSColumn;
import br.com.dbsoft.io.DBSDAO;
import br.com.dbsoft.io.DBSDAO.COMMAND;


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
	
	private static Object 		wDado = null;
	private static String 		wDadoTableName = "";
	private static String 		wDadoCriterio = "";
	private static String 		wDadoColumnName = "";
	
	
	/**
	 * Retorna o banco de dados utilizado
	 * @param pConnection conexão a partir do qual serãoverificado o fabricante do bando de dados
	 * @return Fabricante a partir de enDataBaseProduct
	 * @throws DBSIOException 
	 */
	public static DB_SERVER getDataBaseProduct(Connection pConnection) {
		if (pConnection == null){
			return null;
		}
		String xDBPN;
		try {
			xDBPN = pConnection.getMetaData().getDatabaseProductName().toUpperCase(); 
		} catch (SQLException e) {
			wLogger.error("Fabrincante do banco de dados não encontrado", e);
			return null;
		}
		if (xDBPN.equals("ORACLE")){
			return DB_SERVER.ORACLE;
		}else if (xDBPN.equals("SQLSERVER")){
			return DB_SERVER.SQLSERVER;
		}else if (xDBPN.equals("MYSQL")){
			return DB_SERVER.MYSQL;
		}else{
			return null;
		}
	}
	//ABRINDO conexão - DIRETA		
	//	try {
	//		Class.forName("com.mysql.jdbc.Driver").newInstance(); 
	//		wsCn = DBSIO.getConnection("jdbc:mysql://localhost:3306/dbsweb", "root","dbs0ft");
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
	 * Faz conexão direta com o banco de dados
	 * @param pConnectionString String de conexão
	 * @param pUserName Nome do usuário
	 * @param pUserPassword Senha do usuário
	 * @return true = Sem erro ; false = Com erro
	 * @throws DBSIOException 
	 */
	public static Connection getConnection(String pConnectionString, String pUserName, String pUserPassword) throws DBSIOException{
	    // Força o load dos JDBC drivers.
	    Enumeration<Driver> xD = java.sql.DriverManager.getDrivers();
	    while (xD.hasMoreElements()) {
	    	@SuppressWarnings("unused")
			Object driverAsObject = xD.nextElement();
	      //System.out.println("JDBC Driver=" + driverAsObject);
	    }
	
		Connection xConnection = null;
		try {
			xConnection = DriverManager.getConnection(pConnectionString, pUserName, pUserPassword);
			xConnection.setAutoCommit(false);
			return xConnection; 
		} catch (SQLException e) {
			wLogger.error("Error getConnection3:" + e.getLocalizedMessage());
			throwIOException(e);
			return null;
		}
	}
	
	public static Connection getConnection(DataSource pDS) throws DBSIOException{
		return getConnection(pDS, 0);
	}

	public static Connection getConnection(DataSource pDS, int pTimeout) throws DBSIOException{
		boolean xOk = false;
		int xI = 0;
		Connection xCn = null;
		while (!xOk){
			xI ++;
			try {
				pDS.setLoginTimeout(1);
				xCn = pDS.getConnection();
				xCn.setAutoCommit(false);
				return xCn;
			} catch (SQLException e) {
				wLogger.error("Error getConnection1:" + e.getLocalizedMessage());
				pvGetConnectionTimeout(xCn, e, pTimeout, xI);
			}
		}
		return null;
	}
	
	/**
	 * Faz conexão direta com o banco de dados
	 * @param pDS
	 * @param pTimeout
	 * @param pUserName Nome do usuário
	 * @param pUserPassword Senha do usuário
	 * @return true = Sem erro ; false = Com erro
	 * @throws DBSIOException 
	 */
	public static Connection getConnection(DataSource pDS, int pTimeout, String ppUserName, String pUserPassword) throws DBSIOException{
//		System.out.println("CREATE CONNECTION *********************** INICIO");
		boolean xOk = false;
		int xI = 0;
		Connection xCn = null;
		while (!xOk){
			xI ++;
			try {
				pDS.setLoginTimeout(1);
				xCn = pDS.getConnection(ppUserName, pUserPassword);  
				xCn.setAutoCommit(false);
				return xCn;
			} catch (SQLException e) {
				wLogger.error("Error getConnection:" + e.getLocalizedMessage()); 
				pvGetConnectionTimeout(xCn, e, pTimeout, xI); 
			}
		}
		return null;
	}

	
	/**
	 * Fecha a conexão com o banco de dados.<br/>
	 * Ignora se conexão estiver fechada.
	 * @param pConnection conexão a ser fechada.
	 * @return true = Sem erro / false = Com erro
	 * @throws DBSIOException 
	 */
	public static boolean closeConnection(Connection pConnection){
//		System.out.println("CLOSE CONNECTION ************************ FIM");
		try{
			if (pConnection != null){
				if (!pConnection.isClosed()){
					try {
						if (!pConnection.getAutoCommit()){ //Se não for autocommit, força o rollback. O close connection é efetuado em entro try pois o getAutocommit pode dar exception e acabar não efetuado o close. Interrupted attempting lock.
							pConnection.rollback(); //Força rollback, pois normalmente é efetuado o commit automático quando é fechada a conexão com o banco
						}
					} catch (SQLException e) {
						return false;
					}finally{
						if (!pConnection.isClosed()){
							pConnection.close();
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
	 * @param pConnection
	 * @return
	 */
	public static boolean isConnectionOpened(Connection pConnection){
		try{
			if (pConnection != null){
				if (!pConnection.isClosed()){
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
	 * @param pConnection
	 * @throws DBSIOException
	 */
	public static void throwIOException(String pMessage, SQLException pException, Connection pConnection) throws DBSIOException{
		SQLException xE = DBSError.getFirstSQLException(pException, pException);
		throw new DBSIOException(pMessage, xE, pConnection);
	}

	/**
	 * Dispara novo DBSIOException, criado a partir da Exception informada
	 * @param pException
	 * @param pConnection
	 * @throws DBSIOException
	 */
	public static void throwIOException(SQLException pException, Connection pConnection) throws DBSIOException{
		SQLException xE = DBSError.getFirstSQLException(pException, pException);
		throw new DBSIOException(xE, pConnection);
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
	 * Recupera os registros a partir de uma Query SQL, utilizando a conexão JDBC com o banco
	 * @param pCN conexão a ser utilizada para executa a Query
	 * @param pQuerySQL Query a ser executada
	 * @return ResultSet com os registros
	 * @throws DBSIOException 
	 */
	public static ResultSet openResultSet(Connection pConnection, String pQuerySQL) throws DBSIOException{
		ResultSet xResultSet = null;
		pQuerySQL = pQuerySQL.trim();
		Statement xST = null;
		try{
//			PreparedStatement xPS = pConnection.prepareStatement(pQuerySQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//			xPS.execute();
//			xResultSet = xPS.getResultSet();
			xST = pConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); //ResultSet.CONCUR_UPDATABLE
			xResultSet = xST.executeQuery(pQuerySQL);
		}catch(SQLException e){
			wLogger.error("openResultSet:" + pQuerySQL, e);
			throwIOException(pQuerySQL, e, pConnection);
			try {
				xST.close();
			} catch (SQLException e1) {
				e1.addSuppressed(e);
				wLogger.error("openResultSet2", e1);
			}
		}
		return xResultSet;
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
//				if (!pResultSet.isClosed()){
					pResultSet.close();
//				}
			} catch (SQLException e) {
				wLogger.error("closeResultSet:resultset", e);
			}
			xST = null;
			pResultSet = null;
		}
	}

	/**
	 * Retorna os registros conforme a QuerySQL informada.<br/>
	 * As colunas poderão ser acessadas como atributos de uma classe diretamente. 
	 * Os nomes dos atributos são os próprios nomes definidos as colunas da QuerySQL.
	 * Exemplo de código xhtlm <b>#{table.campo}</b><br/>
	 * Não existe close para o ResultDataModel.
	 * @param pConnection
	 * @param pQuerySQL
	 * @return
	 * @throws DBSIOException
	 */
	public static ResultDataModel openResultDataModel(Connection pConnection, String pQuerySQL) throws DBSIOException{
		ResultSet 		xResultSet;
		Result			xResult;
		ResultDataModel	xResultDataModel;
		xResultSet = DBSIO.openResultSet(pConnection, pQuerySQL);
		xResult = ResultSupport.toResult(xResultSet);
		xResultDataModel = new ResultDataModel(xResult);
		xResult = null;
		DBSIO.closeResultSet(xResultSet);
		return xResultDataModel;
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
	 * @param pConnection conexão com o banco de dados
	 * @param pTableName Nome da tabela que se deseja saber o MetaData
	 * @return ResultSet com o MetaData da tabela
	 * @throws DBSIOException 
	 */
	public static ResultSet getTableColumnsMetaData(Connection pConnection, String pTableName) throws DBSIOException{
		pTableName = pTableName.trim();
		try {
			DatabaseMetaData xDMD = pConnection.getMetaData();
			return  xDMD.getColumns(null, null, pTableName, null);
		} catch (SQLException e) {
			wLogger.error("getTableColumnsMetaData", e);
			throwIOException(e, pConnection);
			return null;
		}
	}

	/**
	 * Retorna nomes da PK de uma tabela
	 * @param pConnection conexão com o banco de dados
	 * @param pTableName Nome da tabela que se deseja saber as PKs
	 * @return String com nos nomes das PKs, separadas por vírgula
	 * @throws DBSIOException 
	 */
	public static List<String> getPrimaryKeys(Connection pConnection, String pTableName) throws DBSIOException{
		pTableName = pTableName.trim();
		DatabaseMetaData xDMD = null;
		ResultSet xRS = null;
		List<String> xPKs = null;
		try {
			xDMD = pConnection.getMetaData();
			xRS = xDMD.getPrimaryKeys(null, null, pTableName);
			xPKs = new ArrayList<String>();
			while (xRS.next()) {
				xPKs.add(xRS.getString("COLUMN_NAME"));
			}
			return xPKs;
		} catch (SQLException e) {
			xPKs = null;
			wLogger.error("getPrimaryKeys", e);
			return xPKs;
		} finally{
			DBSIO.closeResultSet(xRS);
			xDMD = null;
		}
	}
	

	/**
	 * Recupera e 'locka' os registros a partir de uma Query SQL, utilizando a conexão com o banco
	 * @param pConnection conexão a ser utilizada para executa a Query
	 * @param pQuerySQL Query a ser executada
	 * @return ResultSet com os registros
	 * @throws DBSIOException 
	 */	
	public static boolean lockRecord(Connection pConnection, String pQuerySQL) throws DBSIOException{
		//Remove comandos 'For update' e 'Nowait' caso já tenha sido incluido
		pQuerySQL = DBSString.changeStr(pQuerySQL, " FOR UPDATE", "", false);
		pQuerySQL = DBSString.changeStr(pQuerySQL, " NOWAIT", "", false);

		//Inclui os comandos de lock
		executeSQL(pConnection, pQuerySQL + LOCK_FOR_UPDATE + " NOWAIT");
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
				wLogger.error("getResultSetRowsCount", e);
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
			pSQLStatement = "Select Count(*) " + pSQLStatement; //Pesquisa com único select
		}else{
			pSQLStatement = "Select Count(*) From (" + pSQLStatement + ") foo"; //Pesquisa com multiplos selects
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
		if (!pNullText.equals(NULL_TEXT.NAO_EXIBIR)){
			xValues.put("", pNullText.toString());
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
						xValue = xDAO.getValue(xC);
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
	
	
	
	
//	/**
//	 * Executa um comando sql diretamente no banco de dados e recupera os valores do autoincremento efetuados pelo banco
//	 * @param <T>
//	 * @param pConnection conexão com o banco de dados
//	 * @param pSQL Comando SQL
//	 * @return Quantidade de registros afetados
//	 * @throws DBSIOException 
//	 * @throws Throwable 
//	 * @throws Exception 
//	 */
//	private static <T> int pvGetAutoIncrementedColumnValue(Connection pConnection, String pSQL, DBSDAO<T> pDAO) throws DBSIOException {
//		int xCount=0;
//		PreparedStatement xPS;
//		ResultSet xRS;
//		try {
//			xPS = pConnection.prepareStatement(pSQL, PreparedStatement.RETURN_GENERATED_KEYS);
//			//Executa o INSERT
//			xPS.executeUpdate();
//			//Recupera Recordset contendo os valores gerados para as columnas como autoincrement ou sequences 
//			xRS = xPS.getGeneratedKeys();
//			//Loop para recuperas as colunas com os respectivos valores
//			while (DBSIO.moveNext(xRS)){
//				for (int xX=1; xX <= xRS.getMetaData().getColumnCount(); xX++){
//					//Atualiza os valores em memória, das colunas que tiveram seu valores gerados automaticamente após o insert  
//					pDAO.setValue(pDAO.getPK(), xRS.getInt(xX)); //Assume que as colunas com autoincrement/sequences são integer
//				}
//			}
//			//Recupera a quantidade de registros efetados.
//			xCount = xPS.getUpdateCount();
//			xRS.close();
//			xPS.close();
//			return xCount;
//		} catch (SQLException e) {
//			wLogger.error(pSQL, e);
//			throw new DBSIOException(pSQL, e);
//		}
//	}
//	DATABASEPRODUCT xDBP = getDataBaseProduct(pConnection);
//	if (xDBP.equals(DATABASEPRODUCT.ORACLE)){
//		String xSql = "Select " & pIdName & ".currval From Dual"
//                If IOOpenRecordSet(pDB, xTeste, Sql, adOpenStatic, , True) Then
//                    GetID = xTeste("currval").Value
//                    IOClose xTeste
//                Else
//                    GetID = -1
//                    IOClose xTeste
//                End If			
//	}else{	

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
	public final static <A, T> A getDataModelFieldValue(T pDataModel, String pFieldName){
		Field xField = null;
		if (pDataModel != null){
			xField = getDataModelField(pDataModel, pFieldName);
			if (xField != null){
				try {
					xField.setAccessible(true);
					return (A) xField.get(pDataModel);
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
				Annotation xAnnotationTmp = xField.getType().getAnnotation(DataModel.class);
				if (xAnnotationTmp instanceof DataModel){
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
	 * Copia os valores de uma DataModel para outro, desde que encontre colunas com o mesmo nome.
	 * Esta rotina ainda não contempla datamodel recursivo
	 * @param pSourceDataModel DataModel origem
	 * @param pTargetDataModel DataModel destino
	 */	
	public final static <T, T2> void copyDataModelFieldsValue(T pSourceDataModel, T2 pTargetDataModel){
		if (pSourceDataModel != null && pTargetDataModel != null){ 
			for (Field xField:pSourceDataModel.getClass().getDeclaredFields()){
				//Verifica se o campo é um datamodel (Anotação @DataModel)
				//Se for irá procurar dentro dos fields do datamodel
				Annotation xAnnotationTmp = xField.getType().getAnnotation(DataModel.class);
				if (xAnnotationTmp instanceof DataModel){
				}else{
					for (Field yField:pTargetDataModel.getClass().getDeclaredFields()){
						if (xField.getName().toUpperCase().trim().equals(yField.getName().toUpperCase().trim())){
							try {
								xField.setAccessible(true);
								pvSetDataModelFieldValue(pTargetDataModel, yField, xField.get(pSourceDataModel));
							} catch (IllegalArgumentException
									| IllegalAccessException e) {
								wLogger.error("copyDataModelFieldsValue", e);
							}
							break;
						}
					}
				}
			}
		}
	}	
	
	/**
	 * Copia os valores das colunas de um DAO para outro.
	 * @param pDaoSource DAO Origem
	 * @param pDaoTarget DAO Destino
	 */
	public static void copyDAOFieldsValues(DBSDAO<Object> pDaoSource, DBSDAO<Object> pDaoTarget) {
		if (pDaoSource==null
		 || pDaoTarget==null){return;}
		for (DBSColumn xColumn : pDaoSource.getColumns()) {
			pDaoTarget.setValue(xColumn.getColumnName(), xColumn.getValue());
		}
	}
	
	/**
	 * Move o valor informado para o campo do tributo da classe informada conforme o nome informado
	 * @param pDataModel Instancia da classe
	 * @param pFieldName Nome do atributo/coluna
	 * @param pValue valor do atributo
	 */	
//	public final static <T> void setDataModelValue(T pDataModel, String pFieldName, Object pValue){
//		Field xField = null;
//		if (pDataModel != null){
//			xField = getDataModelField(pDataModel, pFieldName);
//			if (xField != null){
//				pvSetDataModelValue(pDataModel, xField, pValue);
//			}
//		}
//	}	
	
		
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
				Annotation xAnnotationTmp = xDeclaredField.getType().getAnnotation(DataModel.class);
				if (xAnnotationTmp instanceof DataModel){
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
	
	/**
	 * Objetivo: Indica ao Banco de Dados o inicio do processo de transação.
	 * @param pConnection conexão com o Banco de dados;
	 * @param pSavePoint Quando informado, nome do Savepoint que se deseja criar;
	 * @return Valor de pSavePoint se não ocorrer erro e nulo ser ocorrer.
	 * @throws DBSIOException 
	 */
	public static Savepoint beginTrans(Connection pConnection, String pSavePoint) throws DBSIOException{
		if (pConnection == null){
			return null;
		}
		try {
			if (pConnection.isClosed()){
				wLogger.error("DBSIO:beginTrans:Conexão fechada");
				return null;
			}else{
				pConnection.setAutoCommit(false);
				if (pSavePoint == null){
					return pConnection.setSavepoint();
				}else{
					return pConnection.setSavepoint(pSavePoint);
				}
			}
		} catch (SQLException e) {
			throwIOException(e, pConnection);
			return null;
		}
	}

	/**
	 * Objetivo: Indica ao Banco de Dados o inicio do processo de transação
	 * @param pConnection conexão com o Banco de dados;
	 * @return true - Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	public static boolean beginTrans(Connection pConnection) throws DBSIOException{
		if (beginTrans(pConnection, null) == null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Executa o Commit ou Rollback no Banco de Dados dependendo dos erros informados
	 * @param pConnection conexão com o Banco de dados;
	 * @param pOk Indicador se houve erro no processo anterior a chamada;
	 * @param pSavePoint Quando informado, nome do Savepoint que se deseja efetuar rollback;
	 * @return true - Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	public static boolean endTrans(Connection pConnection, boolean pOk, Savepoint pSavePoint) throws DBSIOException{
		if (pConnection == null){
			return false;
		}
		if (pOk ==true){
			//Ignora commit se for houver savepoint
			//O commit só pode ser dado na transação inteira
			if (DBSObject.isEmpty(pSavePoint)){
				endTrans(pConnection, pOk);
			}
		}else{
			pvRollbackTrans(pConnection, pSavePoint);
		}
		return true;
	}
	public static boolean endTrans(Connection pConnection, boolean pOk) throws DBSIOException{
		if (pConnection == null){
			return false;
		}
		if (pOk ==true){
			pvCommitTrans(pConnection);
		}else{
			pvRollbackTrans(pConnection);
		}
		return true;
	}
	
	/**
	 * Recupera o nome da tabela que faz parte da SQL caso exista somente uma.<br/>
	 * Caso exista mais de uma retorna nulo.
	 * @param pSQLQuery
	 * @return
	 */
	public static String getTableFromQuery(String pSQLQuery){
		List<String> xT;
		xT = pvGetTablesFromQuery(pSQLQuery);
		if (xT.size()==1){
			return xT.get(0).toString();
		}else{
			return null;
		}
	}

	/**
	 * Move o cursor para o primeiro registro se existir.
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
	 * Move o cursor para o registro anterior se existir
	 * @param pResultSet Resultset contendo os registros
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
	 * Retorna se o registro corrente é o primeiro.
	 * @param pResultSet
	 * @return
	 */
	public static boolean isFirstRow(ResultSet pResultSet){
		if (pResultSet != null){
			try {
				return (pResultSet.getRow() == 1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Move o cursor para o próximo registro se existir
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
	 * Move o cursor para o primeiro registro se existir
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */	
	public static void moveBeforeFirst(ResultDataModel pResultDataModel){
		if (pResultDataModel != null){
			pResultDataModel.setRowIndex(-1); 
		}
	}

	
	/**
	 * Move o cursor para o primeiro registro se existir
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */	
	public static boolean moveFirst(ResultDataModel pResultDataModel) {
		if (pResultDataModel != null){
			//Verifica se existe algum registro
			if(pResultDataModel.getRowCount()>0){ 
				//Move para o primeiro registro
				pResultDataModel.setRowIndex(0);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Move o cursor para o registro anterior se existir
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */	
	public static boolean movePrevious(ResultDataModel pResultDataModel) {
		//Move para Registro anterior
		if (pResultDataModel != null){
			if(pResultDataModel.getRowCount()>0){ 
				int xI = pResultDataModel.getRowIndex()-1;
				//Verificar se há próximo registro
				if (xI >= 0){
					pResultDataModel.setRowIndex(xI);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Move o cursor para o próximo registro se existir
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */
	public static boolean moveNext(ResultDataModel pResultDataModel){
		if (pResultDataModel != null){
			if(pResultDataModel.getRowCount()>0){ 
				int xI = pResultDataModel.getRowIndex()+1;
				//Verificar se há próximo registro
				if (xI < pResultDataModel.getRowCount()){
					pResultDataModel.setRowIndex(xI);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Move o cursor para o último registro
	 * @param pResultSet Resultset contendo os registros
	 * @return true = encontrou registro; false = NÃO encontrou
	 * @throws DBSIOException 
	 */
	public static boolean moveLast(ResultDataModel pResultDataModel) {
		if (pResultDataModel != null){
			if(pResultDataModel.getRowCount()>0){ 
				pResultDataModel.setRowIndex(pResultDataModel.getRowCount()-1);
				return true;
			}
		}
		return false;
	}
	
	public static boolean isFirstRow(ResultDataModel pResultDataModel){
		if (pResultDataModel != null){
			return (pResultDataModel.getRowIndex() == 0);
		}
		return false;
	}
	
	public static boolean isLastRow(ResultDataModel pResultDataModel) {
		if (pResultDataModel != null){
			return (pResultDataModel.getRowIndex() == pResultDataModel.getRowCount()-1);
		}
		return false;
	}

	/**
	 * Retorna o valor de uma coluna da tabela desejada segundo o Critério informado.
	 * @param pCn Objeto da conexão
	 * @param pTableName Nome da tabela
	 * @param pCriterio Critério de seleção para a pesquisa do registro desejado(Clausula Where).
	 * Quando criar a string do critério, utilize as funções toSQL... contidas nesta classe para certificar que os valores estarão de acordo com o tipo de dado que se refere
	 * @param pColumnName Nome da columa da qual se deseja o valor
	 * @return valor da coluna 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T  getDado(Connection pCn, String pTableName, String pCriterio, String pColumnName) throws DBSIOException{
		return (T) getDado(pCn, pTableName, pCriterio, pColumnName, false);
	}

	/**
	 * Retorna o valor de uma coluna da tabela desejada segundo o Critério informado, convertida para o tipo de class informado.
	 * @param pCn Objeto da conexão
	 * @param pTableName Nome da tabela
	 * @param pCriterio Critério de seleção para a pesquisa do registro desejado(Clausula Where).
	 * Quando criar a string do critério, utilize as funções toSQL... contidas nesta classe para certificar que os valores estarão de acordo com o tipo de dado que se refere
	 * @param pColumnName Nome da columa da qual se deseja o valor
	 * @param pReturnedClass class para a qual o valor será obrigatoriamente convertido 
	 * @return valor da coluna 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDado(Connection pCn, String pTableName, String pCriterio, String pColumnName, Class<?> pReturnedClass) throws DBSIOException{
		return (T) DBSObject.toClass(getDado(pCn, pTableName, pCriterio, pColumnName, false), pReturnedClass);
	}
	
	/**
	 * Retorna o valor de uma coluna da tabela desejada segundo o <b>pCritério</b> informado.
	 * @param pCn Objeto da conexão
	 * @param pTableName Nome da tabela
	 * @param pCriterio Critério de seleção para a pesquisa do registro desejado<b>(SQL da cláusula Where)</b>.
	 * Quando criar a string do critério, utilize as funções toSQL... contidas nesta classe para certificar que os valores estarão de acordo com o tipo de dado que se refere
	 * @param pColumnName Nome da columa da qual se deseja o valor
	 * @return valor da coluna 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDado(Connection pCn, String pTableName, String pCriterio, String pColumnName, boolean pIgnoreCache) throws DBSIOException{
		if (pCn == null){
			return null;
		}
		if (!pIgnoreCache){ 
			if (wDadoTableName.equals(pTableName) &&
			    wDadoCriterio.equals(pCriterio) &&
			    wDadoColumnName.equals(pColumnName)){
				return (T) wDado; //Utiliza último valor retornado
			}
		}
		DBSDAO<Object> xDAO = new DBSDAO<Object>(pCn);
		String xSQL = "Select " + pColumnName + 
				       " From " + pTableName + " ";
		
		if (!pCriterio.equals("")){
			xSQL = xSQL + " Where " + pCriterio;
		}

		if (xDAO.open(xSQL)){
			if (xDAO.getRowsCount() > 0){
				//Salva últimos parametros
				wDadoTableName = pTableName; 
				wDadoCriterio = pCriterio;
				wDadoColumnName = pColumnName;
				String xColumnName = pColumnName;
				int xI = pColumnName.indexOf(".");
				//Recupera somente a parte no nome após o "." caso exista.
				if (xI > 0){
					xColumnName = DBSString.getSubString(pColumnName, xI + 2, pColumnName.length());
				}
				//Salva valor
				wDado = xDAO.getValue(xColumnName); 
				xDAO.close();
				return (T) wDado;
			}
			xDAO.close();
		}
		return null;
	}	

	
	/**
	 * Retorna os valores dos atributos do dataModel informado a partir das colunas que possuam o mesmo nome.<br/>Serão retornados os dados do primeiro registro encontrado conforme resultado do SQL informado.
	 * @param pCn Objeto da conexão
	 * @param pSQL Nome da tabela
	 * @return DataModel 
	 */
	public static <T> T getDadoDataModel(Class<T> pDataModelClass, Connection pCn, String pSQL) throws DBSIOException{
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
	 * @param <T>
	 * @param pCn Objeto da conexão
	 * @param pSQL Nome da tabela
	 * @return A quantidade de registros afetados 
	 */
	@SuppressWarnings("unchecked")
	public static <T> int setDadoDataModel(T pDataModel, Connection pCn, String pTableName) throws DBSIOException{
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

	/**
	 * Executa um comando sql diretamente no banco de dados
	 * @param pConnection conexão com o banco de dados
	 * @param pSQL Comando SQL
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException 
	 * @throws Throwable 
	 * @throws Exception 
	 */
	public static int executeSQL(Connection pConnection, String pSQL) throws DBSIOException {
		if (pConnection==null){
			wLogger.error("executeSQL:Conexão nula.");
			return 0;
		}
		if (pSQL == null){
			wLogger.error("executeSQL:SQL nulo.");
			return 0;
		}
		int xCount=0;
		Statement xST = null;
		try {
			xST = pConnection.createStatement(); 
			xST.execute(pSQL);
			xCount = xST.getUpdateCount();
			xST.close(); //Incluido para evitar o erro: ORA-01000: maximum open cursors exceeded
			xST = null;  //Incluido para evitar o erro: ORA-01000: maximum open cursors exceeded
			return xCount;
		} catch (SQLException e) {
			xST = null;  //Incluido para evitar o erro: ORA-01000: maximum open cursors exceeded
			DBSIO.endTrans(pConnection, false);
			throwIOException(pSQL, e, pConnection);
			return 0;
		}
	}
	
	/**
	 * Executa um comando sql diretamente no banco de dados e recupera os valores do autoincremento efetuados pelo banco
	 * @param <T>
	 * @param pConnection conexão com o banco de dados
	 * @param pSQL Comando SQL
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException 
	 * @throws Throwable 
	 * @throws Exception 
	 */
	public static <T> int executeSQLInsertAutoIncremented(Connection pConnection, String pSQL, DBSDAO<T> pDAO) throws DBSIOException {
		if (pConnection == null || pSQL == null || pDAO == null){
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
					DB_SERVER xDBP = getDataBaseProduct(pConnection);
					//Se for Oracle, o autoincrement é implementado via sequence.
					if (xDBP == DB_SERVER.ORACLE){ 
						//Executa comando no banco
						xCount = executeSQL(pConnection, pSQL);
						BigDecimal xId;
						xId = pvGetOracleSequenceValue(pConnection, pSQL, pDAO.getColumn(pDAO.getPK()).getColumnName());
						pDAO.setValue(pDAO.getPK(), xId);
					}else{
						PreparedStatement xPS;
						ResultSet xRS;
						//pConnection.prepareStatement
						xPS = pConnection.prepareStatement(pSQL, PreparedStatement.RETURN_GENERATED_KEYS);
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
			String xMsg = DBSError.getErrorMessage(e.getSQLState());
			if (xMsg.equals("")){
				wLogger.error(pSQL, e);
			}else{
				wLogger.error(xMsg + ":" + pSQL);
			}
			DBSIO.endTrans(pConnection, false);
			throwIOException(pSQL, e, pConnection);
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
			if (pDAOCommand == COMMAND.INSERT  
			 && pAutoIncrementValueRetrieve){
				return executeSQLInsertAutoIncremented(pDAO.getConnection(), xSQLCommand, pDAO);
			}else{
				return executeSQL(pDAO.getConnection(), xSQLCommand);
			}
		}
	}

	/**
	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pCn Conexão com o banco
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @param pTableName Nome da tabela que receberá o comando
	 * @param pPK Nomes das colunas que serão utilizadas como chave primária. Caso omitido, será utilizado o PK de própria tabela
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelCommand(T pDataModel, Connection pCn, COMMAND pDAOCommand, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pDataModel, null, pCn, pDAOCommand, pTableName, pPK);
	}	

	/**
	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pCn Conexão com o banco
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @param pTableName Nome da tabela que receberá o comando
	 * @param pPK Nomes das colunas que serão utilizadas como chave primária. Caso omitido, será utilizado o PK de própria tabela
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand, String pTableName, String pPK) {
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
						xDAO.setDataModelValueOriginal(pDataModelValueOriginal);
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
				} catch (Exception xE) {
					wLogger.error("executeDataModelCommand", xE);
					try {
						throw xE;
					} catch (Exception e) {
						wLogger.error("executeDataModelCommand2", e);
					}
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
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pCn Conexão com o banco
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @param pTableName Nome da tabela que receberá o comando
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelCommand(T pDataModel, Connection pCn, COMMAND pDAOCommand, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, pDAOCommand, pTableName, "");
	}	

	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, pDAOCommand, pTableName, "");
	}	
	
	/**
	 * Executa o comando informado(Isert, Update ou Delete) dos dados contidos no DataModel
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pCn Conexão com o banco
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelCommand(T pDataModel, Connection pCn, COMMAND pDAOCommand) throws DBSIOException{
		return executeDataModelCommand(pDataModel, null, pCn, pDAOCommand);
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
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pCn Conexão com o banco
	 * @param pDAOCommand Comando(Insert, Update ou Delete)
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelCommand(T pDataModel, T pDataModelValueOriginal, Connection pCn, COMMAND pDAOCommand) throws DBSIOException{
		DataModel xAnnotation = getAnnotationDataModel(pDataModel);
		if (xAnnotation == null){
			return -1;
		}
		return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, pDAOCommand, xAnnotation.tablename(), "");
	}
	
	/**
	 * Retorna anotação se classe tem annotation @DataModel.<br/>
	 * Anotação DataModel é a anotação DBSoft que identifica se é um model de tabela de banco 
	 * @param pDataModel
	 * @return Anotação se existir
	 */
	public static <T> DataModel getAnnotationDataModel(T pDataModel){
		//Procura pela anotação DataModel para posteriomente ler o nome da tabela 
		Annotation xAnnotationTmp = pDataModel.getClass().getAnnotation(DataModel.class); 
		if (xAnnotationTmp instanceof DataModel){
			DataModel xAnnotation = (DataModel) xAnnotationTmp;
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
	 * Executa o merge dos dados contidos no DataModel. Isto é, se existir o registro, altera os dados, se não existir, inclui
	 * É utilizada a chave primária da tabela
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pCn Conexão com o banco
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelMerge(T pDataModel, Connection pCn) throws DBSIOException{
		return executeDataModelMerge(pDataModel, null, pCn);
	}	

	/**
	 * Executa o merge dos dados contidos no DataModel. 
	 * Se existir o registro efatua uma alteração de dados, se não existir, inclui novo registro.
	 * É utilizada a chave primária da tabela para identificar se registro existe.<br/>
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela.
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pDataModelValueOriginal DataModel com os atributos e respectivos valores originais
	 * @param pCn Conexão com o banco
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelMerge(T pDataModel, T pDataModelValueOriginal, Connection pCn) throws DBSIOException{
		int xN=-1;
		if (pCn!=null){
			Savepoint xS  = DBSIO.beginTrans(pCn, "EXECUTEMERGE"); //Cria savepoint interno para retornar em caso de erro já que o update pode funcionar mais o insert não
			xN = executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, COMMAND.UPDATE);//Atualiza registro, se existir
			if (xN==0){ //Se não foi atualiza registro algum...
				xN = executeDataModelCommand(pDataModel, pCn, COMMAND.INSERT); //Insere novo registro
			}
			if (xN<=0){ //Se nehum registro foi alterado é pq houve erro
				DBSIO.endTrans(pCn,false,xS); //ignora Update ou Insert em caso de erro. Rollback até EXECUTEMERGE
			}
		}
		return xN;
	}	
	
	/**
	 * Executa o insert dos dados contidos no DataModel. 
	 * É utilizada a chave primária da tabela
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pCn Conexão com o banco
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelInsert(T pDataModel, Connection pCn) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, COMMAND.INSERT); //Insere novo registro
	}
	public static <T> int executeDataModelInsert(T pDataModel, Connection pCn, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, COMMAND.INSERT, pTableName); //Insere novo registro
	}
	public static <T> int executeDataModelInsert(T pDataModel, Connection pCn, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, COMMAND.INSERT, pTableName, pPK); //Insere novo registro
	}
	
	/**
	 * Executa o delete dos dados contidos no DataModel. 
	 * É utilizada a chave primária da tabela
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pCn Conexão com o banco
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelDelete(T pDataModel, Connection pCn) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, COMMAND.DELETE); //Insere novo registro
	}	
	public static <T> int executeDataModelDelete(T pDataModel, Connection pCn, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, COMMAND.DELETE, pTableName); //Insere novo registro
	}	
	public static <T> int executeDataModelDelete(T pDataModel, Connection pCn, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, COMMAND.DELETE, pTableName, pPK); //Insere novo registro
	}	
	
	/**
	 * Executa o update dos dados contidos no DataModel. 
	 * É utilizada a chave primária da tabela
	 * O DataModel deverá conter a anotação @DataModel com a informação do nome da tabela 
	 * @param pDataModel DataModel com os atributos e respectivos valores
	 * @param pCn Conexão com o banco
	 * @return Quantidade de registros afetados
	 * @throws DBSIOException
	 */
	public static <T> int executeDataModelUpdate(T pDataModel, Connection pCn) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, COMMAND.UPDATE); //Insere novo registro
	}	
	public static <T> int executeDataModelUpdate(T pDataModel, Connection pCn, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, COMMAND.UPDATE, pTableName); //Insere novo registro
	}	
	public static <T> int executeDataModelUpdate(T pDataModel, Connection pCn, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pCn, COMMAND.UPDATE, pTableName, pPK); //Insere novo registro
	}	
	public static <T> int executeDataModelUpdate(T pDataModel, T pDataModelValueOriginal, Connection pCn) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, COMMAND.UPDATE); //Insere novo registro
	}	
	public static <T> int executeDataModelUpdate(T pDataModel, T pDataModelValueOriginal, Connection pCn, String pTableName) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, COMMAND.UPDATE, pTableName); //Insere novo registro
	}	
	public static <T> int executeDataModelUpdate(T pDataModel, T pDataModelValueOriginal, Connection pCn, String pTableName, String pPK) throws DBSIOException{
		return executeDataModelCommand(pDataModel, pDataModelValueOriginal, pCn, COMMAND.UPDATE, pTableName, pPK); //Insere novo registro
	}	
	
	
	
	/**
	 * Retorna os valores dos atributos do dataModel informado a partir das colunas encontradas no primeiro regostro do select informado.
	 * @param pCn Objeto da conexão
	 * @param pSQL Nome da tabela
	 * @return DataModel 
	 */
	public static <T> List<T> getListDataModel(Class<T> pDataModelClass, Connection pCn, String pSQL) throws DBSIOException{
		DBSDAO<T> xDAO = new DBSDAO<T>(pDataModelClass, pCn);
		List<T> xListDataModel = new ArrayList<T>();
		xDAO.open(pSQL); 
		xListDataModel = xDAO.getListDataModel();
		xDAO.close();
		return xListDataModel;
	}
	

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
	 * @param pConnection conexão com o banco de dados
	 * @param pDataType Tipo de dado do valor informado no objeto pValue
	 * @param pValue Objecto contendo o valor a ser utilizado
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String getValueSQLFormated(Connection pConnection, DATATYPE pDataType, Object pValue){
		if (pDataType == DATATYPE.STRING){
			return toSQLString(pConnection, pValue);
		}else if (pDataType == DATATYPE.BOOLEAN){
			return toSQLBoolean(pConnection, pValue);
		}else if (pDataType == DATATYPE.DATE){
			return toSQLDate(pConnection, pValue);
		}else if (pDataType == DATATYPE.TIME){
			return toSQLTime(pConnection, pValue);
		}else if (pDataType == DATATYPE.DATETIME){
			return toSQLDateTime(pConnection, pValue);
		}else if (pDataType == DATATYPE.DECIMAL || 
				  pDataType == DATATYPE.DOUBLE ||
				  pDataType == DATATYPE.INT ||
				  pDataType == DATATYPE.ID) {
			return toSQLNumber(pConnection, pValue);
		}else if (pDataType == DATATYPE.COMMAND){
			return DBSString.toString(pValue);
		}else{
			return null;
		}
	}
	
	/**
	 * Retorna o valor convertido conforme o DataType
	 * @param pDataType
	 * @param pValue
	 * @return
	 * @throws DBSIOException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getDataTypeConvertedValue(DATATYPE pDataType, T pValue) {
		if (pValue == null){
			return pValue;
		}
		//Como alguns Bancos de dados o boolean é definido como numérico
		//converte o valor para 0(false) ou -1(true)
		if (pValue instanceof Boolean){
			if (pDataType == DATATYPE.INT || 
				pDataType == DATATYPE.DECIMAL || 
				pDataType == DATATYPE.DOUBLE){
				pValue = (T) DBSIO.toSQLBoolean(pValue);
			}
		}
		switch (pDataType){
		case INT:
			return (T) DBSNumber.toInteger(pValue);
		case BOOLEAN:
			return (T) DBSBoolean.toBoolean(pValue);
		case COMMAND:
			return pValue;
		case DATE:
			return (T) DBSDate.toDate(pValue);
		case DATETIME:
			return (T) DBSDate.toTimestamp(pValue);
		case DECIMAL:
			return (T) DBSNumber.toBigDecimal(pValue);
		case DOUBLE:
			return (T) DBSNumber.toDouble(pValue);
		case ID:
			return (T) DBSNumber.toInteger(pValue);
		case NONE:
			return pValue;
		case PICTURE:
			return pValue;
		case STRING:
			return pValue;
		case TIME:
			return (T) DBSDate.toTime((String) pValue);
		default:
			return pValue;
		}
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
			xT = pvGetTablesFromQuery(xQuerySQL);
			//Substitui os 'Select * ' por 'Select tabela.* ' 
			for (int x=0; x <= xT.size()-1; x++){
				if (xS == ""){
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
			wLogger.error("isResultSetOnValidRow", e);
			throwIOException(e);
			return false;
		}
	}

	/**
	 * Retorna se coluna está na lista de colunas que dever ser ignoradas
	 * Normalmente são colunas vinculadas a trilhas de auditoria
	 * @param pColumnName
	 * @return
	 */
	public static boolean isColumnsIgnored(String pColumnName){
		pColumnName = pColumnName.trim().toUpperCase();
		return (DBSString.findStringInArray(DBSIO.COLUMNS_IGNORED, pColumnName) > -1);
	}

	/**
	 * Retorna string com a coluna e o valor formatado com a sintaxe adaptada ao padrão do banco da dados 
	 * @param pConnection Conexão com o banco de dados
	 * @param pDataType Tipo de dado do valor informado no objeto pValue
	 * @param pValue Objeto contendo o valor a ser utilizado
	 * @param pFieldName Nome da coluna do sql
	 * @return String concatenando a coluna com valor adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException
	 */
	public static String getFieldValueSql(Connection pConnection, DATATYPE pDataType, Object pValue, String pFieldName) throws DBSIOException{
		if (pConnection == null){
			return null;
		}
		String xValueSQLFormated;
		
		xValueSQLFormated = getValueSQLFormated(pConnection, pDataType, pValue);
		if (xValueSQLFormated == null
		 || xValueSQLFormated.toLowerCase().contains("null")){
			return toSQLNull(pConnection, pFieldName);
		}
		return pFieldName + " = " + xValueSQLFormated;
	}

	/**
	 * Retorna a data atual segundo o banco de dados.
	 * @param pConnection
	 * @return
	 */
	public static Date getServerDate(Connection pConnection) {
		Date xData = null;
		if (getDataBaseProduct(pConnection) == DB_SERVER.ORACLE) {
	        try {
				xData = DBSDate.toDate(getDado(pConnection, "dual", "", "SYSDATE"));
			} catch (DBSIOException e) {
				wLogger.error("getServerDate", e);
			}
		}
		return xData;
	}

	/**
	 * Retorna o tipo de dado DBSoft que corresponde ao tipo de dado original do banco de dados
	 * @param pConnection conexão a partir do qual serãoverificado o fabricante do bando de dados
	 * @param pDataTypeOriginal Tipo de dado original recuperado diretamente da coluna do banco de dados 
	 * @return Tipo de dado DBSoft que corresponde ao tipo de dado original
	 * @throws DBSIOException 
	 */
	public static DATATYPE toDataType(Connection pConnection, int pDataTypeOriginal, int pPrecision){
		DB_SERVER xDBP;
		xDBP = getDataBaseProduct(pConnection);
		if (xDBP != null){
			if (xDBP == DB_SERVER.ORACLE){
				if (pDataTypeOriginal == 2 || //NUMBER 
					pDataTypeOriginal == 3 || //NUMBER
					pDataTypeOriginal == 4){
					if (pPrecision == 0 ||
						pPrecision > 14){
						return DATATYPE.DECIMAL;
					}
					if (pPrecision < 9){
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
			}
		}
		return DATATYPE.STRING;
	}

	/**
		 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
		 * @param pConnection conexão com o banco da dados
		 * @param pValue Valor a ser utilizado  
		 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
		 */
		public static String toSQLString(Connection pConnection, Object pValue){
			if (pValue==null){
				return "NULL";
			}
	//		if (pValue == null){
	//			return "''";
	//		}else{
				pValue = DBSString.changeStr((String)pValue, "'", "''");
				pValue = DBSString.changeStr((String)pValue, "\\", "\\\\");
				//TODO ENCONTRAR UMA FORMA MELHOR PARA A SITUAÇÃO ABAIXO - AVILA
				//DEVE-SE RECEBER A STRING JÁ CONVERTIDA, ISTO É, SEM OS CARACTERES ABAIXO - RICARDO
				pValue = DBSString.changeStr((String)pValue, "╔", "É");
				pValue = DBSString.changeStr((String)pValue, "ß", "á");
				return "'" + pValue + "'";
	//		}
		}

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
	 * @param pConnection conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String toSQLDate(Connection pConnection, Object pValue){
		if (pValue==null){
			return "NULL";
		}
		DB_SERVER xDBP = getDataBaseProduct(pConnection);
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
					return "DateValue('" + DBSFormat.getFormattedDate(DBSDate.toDate(pValue)) + "')";
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
	 * @param pConnection conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String toSQLDateTime(Connection pConnection, Object pValue){
		if (pValue==null){
			return "NULL";
		}
		DB_SERVER xDBP = getDataBaseProduct(pConnection);
		if(xDBP!=null){ 
			if (xDBP == DB_SERVER.ORACLE){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedDateTime(DBSDate.toTimestamp(pValue)) + "','dd/mm/yyyy hh24:mi:ss')";
				}else{
					return "''";
				}
			}else if (xDBP == DB_SERVER.POSTGRESQL){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedDateTime(DBSDate.toTimestamp(pValue)) + "','dd/mm/yyyy hh24:mi:ss')";
				}else{
					return "''";
				}
			}else if(xDBP == DB_SERVER.SQLSERVER){
	//			if (pValue==null){
	//				return "0";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "DateValue('" + DBSFormat.getFormattedDateTime(DBSDate.toTimestamp(pValue)) + "')";
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
	 * @param pConnection conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String toSQLTime(Connection pConnection, Object pValue) {
		if (pValue==null){
			return "NULL";
		}
		DB_SERVER xDBP = getDataBaseProduct(pConnection);
		if(xDBP!=null){ 
			if (xDBP == DB_SERVER.ORACLE){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedTime((Date)pValue) + "','hh24:mi:ss')";
				}else{
					return "''";
				}
			}else if (xDBP == DB_SERVER.POSTGRESQL){
	//			if (pValue==null){
	//				return "''";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "To_Date('" + DBSFormat.getFormattedTime((Date)pValue) + "','hh24:mi:ss')";
				}else{
					return "''";
				}
			}else if(xDBP == DB_SERVER.SQLSERVER){
	//			if (pValue==null){
	//				return "0";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "DateValue('" + DBSFormat.getFormattedTime((Date)pValue) + "')";
				}else{
					return "0";
				}
			}else if(xDBP == DB_SERVER.MYSQL){
	//			if (pValue==null){
	//				return "0";
	//			}else if (DBSDate.isDate(pValue.toString())){
				if (DBSDate.isDate(pValue.toString())){
					return "STR_TO_DATE('" + DBSFormat.getFormattedTime((Date)pValue) + "',%T')";
				}else{
					return "0";
				}
			}
		}
		return "'" + pValue + "'";
	}

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada ao padrão do banco da dados 
	 * @param pConnection conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 * @throws DBSIOException 
	 */
	public static String toSQLNumber(Connection pConnection, Object pValue) {
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
		xDBP = getDataBaseProduct(pConnection);
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
					return "cDbl(" + xValue + ")";
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
	 * @param pConnection conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 */
	public static String toSQLBoolean(Connection pConnection, Object pValue){
		return toSQLBoolean(pConnection, pValue, false);
	}
	

	/**
	 * Retorna string com a sintaxe formatada com o valor adaptada para -1(true) e 0(false). 
	 * @param pConnection
	 * @param pValue
	 * @param pDefaultValue
	 * @return
	 */
	public static String toSQLBoolean(Connection pConnection, Object pValue, Boolean pDefaultValue){
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
	 * @param pConnection conexão com o banco da dados
	 * @param pValue Valor a ser utilizado  
	 * @return String com a sintaxe adaptada a sintaxe padrão do banco informado
	 */
	public static String toSQLNull(Connection pConnection, String pColumnName){
		return pColumnName + " IS NULL ";
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
	 * @param pSeCampo Dado do Campo a ser testado;
	 * @param pIgualA Dado a ser comparado ao campo pSeCampo;
	 * @param pUsa Dado a ser considerado quando pSeCampo for Igual a pIgualA;
	 * @param pSenao Dado a ser considerado quando pSeCampo for Diferente a pIgualA.
	 * @return String com comando formatado.
	 */
	public static String toSQLIf(Connection pConnection, String pSeCampo, int pIgualA, String pUsa, String pSenao) {
//        return "(CASE WHEN " + pSeCampo + "=" + pIgualA + " THEN " + pUsa + " ELSE " + pSenao + " END)";
		DB_SERVER xDBP = getDataBaseProduct(pConnection);
		if (xDBP == DB_SERVER.ORACLE) {
	        return "Decode(" + pSeCampo + "," + pIgualA + "," + pUsa + "," + pSenao + ")";
	    } else {
	        return "iif(" + pSeCampo + "=" + pIgualA + "," + pUsa + "," + pSenao + ")";
	    }
	}

	/**
	 * Retorna sequence do registro recem incluído
	 * @param pConnection
	 * @param pSQL
	 * @param pSequenceName
	 * @return
	 * @throws DBSIOException
	 */
	private static BigDecimal pvGetOracleSequenceValue(Connection pConnection, String pSQL, String pSequenceName) throws DBSIOException {
		if (pConnection == null || pSQL == null || pSequenceName == null){
			return DBSNumber.toBigDecimal(0D);
		}
		BigDecimal xId;
		xId = DBSNumber.toBigDecimal(getDado(pConnection, "DUAL", "", pSequenceName.trim().toUpperCase() + ".CURRVAL"));
		xId = DBSNumber.toBigDecimal(DBSObject.getNotNull(xId, -1));
		return xId;
	}

	//######################################################################################################### 
	//## Private Methods                                                                                      #
	//#########################################################################################################
	private static void pvGetConnectionTimeout(Connection pConnection, SQLException e, int pTimeout, int pTimes) throws DBSIOException{
		if (pTimeout == 0) {
			throwIOException(e, pConnection);
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
	 * @param pConnection conexão com o Banco de dados
	 * @return true = Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	private static boolean pvCommitTrans(Connection pConnection) throws DBSIOException{
		try {
			if (pConnection.isClosed()){
				wLogger.error("DBSIO:pvCommitTrans:Conexão fechada");
				return false;
			}else{
				pConnection.commit();
				return true;
			}
		} catch (SQLException e) {
			wLogger.error("DBSIO:pvCommitTrans",e);
			throwIOException(e);
			return false;
		}
	}

	/**
	 * Desfaz as modificações no banco de dados, efetuada desde o Savepoint informado
	 * @param pConnection conexão com o banco de dados
	 * @param pSavePoint Savepoint a partir do qual serão desfeitas as modificações
	 * @return true = Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	private static boolean pvRollbackTrans(Connection pConnection, Savepoint pSavePoint) throws DBSIOException{
		try {
			if (pConnection.isClosed()){
				wLogger.error("DBSIO:pvRollbackTrans:Conexão fechada");
				return false;
			}else{
				if (DBSObject.isEmpty(pSavePoint)){
					pConnection.rollback();
					return true;
				}else{
					pConnection.rollback(pSavePoint);
					return true;
				}
			}
		} catch (SQLException e) {
			wLogger.error("DBSIO:pvRollbackTrans", e);
			throwIOException(e, pConnection);
			return false;
		}
	}
	
	/**
	 * Desfaz as modificações no banco de dados
	 * @param pConnection conexão com o banco de dados
	 * @return true = Sem erro; false = Com erro
	 * @throws DBSIOException 
	 */
	private static boolean pvRollbackTrans(Connection pConnection) throws DBSIOException{
		return pvRollbackTrans(pConnection, null);
	}


	/**
	 * Recupera o nome das tabelas que fazem parte da Query SQL
	 * @param pSQLQuery Query SQL a ser resquisada
	 * @return Array com os nomes da tabelas, sendo a primeira, index = 0
	 */
	private static List<String> pvGetTablesFromQuery(String pSQLQuery){
		int xI;
		int xF;
		String xS = pSQLQuery.trim(); 
		List<String> xT;
		List<String> xT0;
		xI = DBSString.getInStr(xS, " FROM ",false);
		if (xI == 0){
			return null;
		}else {
			xI+=6; //Adiciona 6 para desconsiderar a própria string " FROM "
		}
		//Tedermina onde termina a clausula from
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
		
		//Recupera somente o texto referente a clausula "FROM" do texto original mantendo para manter a caixa do texto
		xS = DBSString.getSubString(pSQLQuery, xI, xF - xI + 1);

		//Exclui textos de comandos
		xS = DBSString.changeStr(xS, " INNER ", " ", false);
		xS = DBSString.changeStr(xS, " OUTER ", " ", false);
		xS = DBSString.changeStr(xS, " LEFT ", " ", false);
		xS = DBSString.changeStr(xS, " RIGHT ", " ", false);
		xS = DBSString.changeStr(xS, " NATURAL ", " ", false);

		//Separa as tabelas delimitadas por virgulas ou do comando "JOIN"
		xT0 = DBSString.toArray(xS,",");
		xT = xT0;
		xT0 = DBSString.toArray(xS," JOIN ", false);
		//'Se existir tabela delimitadas por "JOIN"
		if (xT0.size() > 1) {
			//Merge dos arrays
			xT0.addAll(xT);
		}
		//Recupera o alias da tabela caso tenha sido utilizado o "AS" no "FROM"
		for (int x=0; x < xT.size(); x++){
			if (DBSString.getInStr(xT.get(x)," AS ", false) > 0){
				xT0 = DBSString.toArray(xT.get(x), " AS ", false);
				if (xT0.size()>1){
					xT.set(x, xT0.get(1));
				}
			}
		}
		return xT;
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
					pValue = DBSObject.toClass(pValue, pField.getType()); 
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
						xSQLWhere = xSQLWhere + toSQLNull(pDAO.getConnection(), xColumn.getColumnName());
					}else{
						xSQLWhere = xSQLWhere + xColumn.getColumnName() + "=" + getValueSQLFormated(pDAO.getConnection(), xColumn.getDataType(), xValue);
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
	 * Construi string com os valores das colunas para o INSERT, UPDATE ou SELECT
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
				if (!xColumn.getColumnName().equals("")){//Se coluna estiver realmente vinculada a uma coluna na tabela
					//Se valor foi informado pelo usuário ou não
					if (!pDAO.getExecuteOnlyChangedValues() 
					 || xColumn.getChanged()){ 
						xSQLColumns += xVirgula + xColumn.getColumnName();
						xVirgula = ",";
					}
				}
			}
			xVirgula = "";
			xSQLColumns += ") VALUES(";
			for (DBSColumn xColumn:pDAO.getCommandColumns()){
				//Se coluna pertence a pesquisa..
				if (!xColumn.getColumnName().equals("")){//Se coluna estiver realmente vinculada a uma coluna na tabela
					//Se valor foi informado pelo usuário ou não
					if (!pDAO.getExecuteOnlyChangedValues() 
					  || xColumn.getChanged()){ 
						xSQLColumns += xVirgula + getValueSQLFormated(pDAO.getConnection(), xColumn.getDataType(), xColumn.getValue());
						xVirgula = ",";
					}
				}
			}
			xSQLColumns += ")";
		}else if(pCommand==COMMAND.UPDATE){
			String xSQLColumnNecessaria = "";
			for (DBSColumn xColumn:pDAO.getCommandColumns()){
				//Se coluna pertence a pesquisa..
				if (!xColumn.getColumnName().equals("")){//Se coluna estiver realmente vinculada a uma coluna na tabela
					//Verifica se a coluna é PK para evitar que seja alterada
					if (!xColumn.getPK()){
						//Verifica se houve alteração de valor indepententemente do valor ter sido informado pelo usuário.
						if (!pDAO.getExecuteOnlyChangedValues() 
						 || !DBSObject.getNotNull(xColumn.getValue(),"").equals(DBSObject.getNotNull(xColumn.getValueOriginal(),""))){
							xSQLColumns += xVirgula + xColumn.getColumnName() + "=" + getValueSQLFormated(pDAO.getConnection(), xColumn.getDataType(), xColumn.getValue());
							xVirgula = ",";
						}else{
							xSQLColumnNecessaria = xColumn.getColumnName() + "=" + xColumn.getColumnName();
						}
					}
				}
			}
			//Se não existem colunas que sofrerão o update, por não ter havido alteração de valores das respectivas colunas...
			if (xSQLColumns.equals("")){
				//Força a existência de pelo menos uma coluna para obrigar que o update seja realizado
				xSQLColumns = xSQLColumnNecessaria;
			}
		}else if(pCommand==COMMAND.SELECT){
			for (DBSColumn xColumn:pDAO.getCommandColumns()){
				//Se coluna pertence a pesquisa..
				if (!xColumn.getColumnName().equals("")){//Se coluna estiver realmente vinculada a uma coluna na tabela
//					//Se valor foi informado pelo usuário ou não
//					if (!pDAO.getExecuteOnlyChangedValues() 
//					  || xColumn.getChanged()){ 
						xSQLColumns += xVirgula + xColumn.getColumnName();
						xVirgula = ",";
//					}
				}
			}
		}
		return xSQLColumns;
	}

//ElseIf piRegsMode = DBSCnsRegsMode.RegsModeSelect Then
//  For Each xCol As DBSClsColumns.DBSClsColumn In piRegs.prCommandColumns
//      'Se coluna pertence a pesquisa..
//      If xCol.prDataFieldName <> "" Then
//          If Not xCol.prValue = xCol.prValueOriginal Then
//              xSqlColumns = xSqlColumns & xVirgula & xCol.prDataFieldName
//              xVirgula = ","
//          End If
//      End If
//  Next
//End If
//    'Configura nome da columa e o conte��do
//    Private Function pvIOBuildSqlCommandColumns(ByRef piRegs As DBSClsRegs, _
//                                                ByVal piRegsMode As DBSCnsRegsMode) As String
//        Dim xSqlColumns As String = ""
//        Dim xVirgula As String = ""
//        Try
//            If piRegsMode = DBSCnsRegsMode.RegsModeLock Then
//                xSqlColumns = " * "
//            ElseIf piRegsMode = DBSCnsRegsMode.RegsModeInsert Then
//                xSqlColumns = "("
//                For Each xCol As DBSClsColumns.DBSClsColumn In piRegs.prCommandColumns
//                    'Se coluna pertence a pesquisa..
//                    If xCol.prDataFieldName <> "" Then
//                        If Not xCol.prValue = xCol.prValueOriginal Then
//                            xSqlColumns = xSqlColumns & xVirgula & xCol.prDataFieldName
//                            xVirgula = ","
//                        End If
//                    End If
//                Next
//                xVirgula = ""
//                xSqlColumns = xSqlColumns & ") VALUES("
//                For Each xCol As DBSClsColumns.DBSClsColumn In piRegs.prCommandColumns
//                    'Se coluna pertence a pesquisa..
//                    If xCol.prDataFieldName <> "" Then
//                        If Not xCol.prValue = xCol.prValueOriginal Then
//                            xSqlColumns = xSqlColumns & xVirgula & piRegs.prValueSqlFormated(xCol.prDataFieldName)
//                            xVirgula = ","
//                        End If
//                    End If
//                Next
//                xSqlColumns = xSqlColumns & ") "
//            ElseIf piRegsMode = DBSCnsRegsMode.RegsModeUpdate Then
//                For Each xCol As DBSClsColumns.DBSClsColumn In piRegs.prCommandColumns
//                    'Se coluna pertence a pesquisa..
//                    If xCol.prDataFieldName <> "" Then
//                        If Not xCol.prIsPK Then
//                            If Not xCol.prValue = xCol.prValueOriginal Then
//                                xSqlColumns = xSqlColumns & xVirgula & xCol.prDataFieldName & "=" & piRegs.prValueSqlFormated(xCol.prDataFieldName)
//                                xVirgula = ","
//                            End If
//                        End If
//                    End If
//                Next
//            ElseIf piRegsMode = DBSCnsRegsMode.RegsModeSelect Then
//                For Each xCol As DBSClsColumns.DBSClsColumn In piRegs.prCommandColumns
//                    'Se coluna pertence a pesquisa..
//                    If xCol.prDataFieldName <> "" Then
//                        If Not xCol.prValue = xCol.prValueOriginal Then
//                            xSqlColumns = xSqlColumns & xVirgula & xCol.prDataFieldName
//                            xVirgula = ","
//                        End If
//                    End If
//                Next
//            End If
//            Return xSqlColumns
//        Catch ex As Exception
//            pbShowException(ex)
//            Return Nothing
//        End Try
//    End Function	
	
	
	//'Public Function IsTimeOut(ByVal pTime As Object, ByVal pDelay As Object) As Boolean
	//'    'VISTO
	//'    ' Objetivo: Retorna se foi transcorrido o tempo determinado
	//'    '
	//'    ' Retorno da função:
	//'    '     True         - Tempo transcorrido;
	//'    '     False        - Tempo NÃO transcorrido.
	//'    '
	//'    ' Par���metros:
	//'    ' Entrada.:
	//'    '     pTime        - Hora Inicial;
	//'    '     pDelay       - Intervalo de Tempo em segundos.
	//'    '
	//'    ' Sa���da...:
	//'    '     pTime        - NÃO modificado;
	//'    '     pDelay       - NÃO modificado.
	//'    '-------------------------------------------------------------------------------
	//'    Dim xOut As Boolean
	//'    xOut = CBool(DateDiff("s", pTime, Time) > pDelay)
	//'    IsTimeOut = xOut
	//'End Function	
	
//	//######################################################################################################### 
//	//## JPA                                                                                     #
//	//#########################################################################################################
//	/**
//	 * Recupera os registros a partir de uma Query SQL, utilizando EntityManager(JPA)
//	 * @param pEM EntityManager respons��vel pelos dados 
//	 * @param pQuerySQL Query a ser executada
//	 * @return Registros
//	 */
//	public static List<Object[]> getResultList(EntityManager pEM, String pQuerySQL){
//		try{
//			Query xQ = pEM.createNativeQuery(pQuerySQL);
//			return xQ.getResultList();
//		}catch(Exception e){
//			DBSErro.showException(e);
//			return null;
//		}
//		
//	}
//
//	/**
//	 * Retorna conexão capturada do pool de conexão com o banco de dados(DataSource)
//	 * @return conexão ou null quando houver erro
//	 */
//	public static Connection getConnectionFromPool(){
//		try{
//			return wsDS.getConnection();
//		}catch(Exception e){
//			DBSErro.showException(e);
//			return null;
//		}
//	}
//	
	
}

//
//Public Module DBSModIo
//    Public Enum DBSCnsEditMode
//        EditModeOFF = 0
//        EditModeInsert = 1
//        EditModeUpdate = 2
//        EditModeDelete = 3
//        EditModeSelect = 4
//        EditModeAprove = 8
//        EditModeReprove = 16
//    End Enum
//enDAOMode
//    Public Enum DBSCnsRegsMode
//        RegsModeInsert = 1
//        RegsModeUpdate = 2
//        RegsModeDelete = 3
//        RegsModeSelect = 4
//        RegsModeLock = 5
//    End Enum
//
//    Public Enum DBSCnsDataType
//        DataTypeNone = 0      'Tipo NÃO definido
//        DataTypeID = 1        'Tipo num��rico utilizadao como chave. 0(zero) ou -1 serãoconvertido para Null
//        DataTypeString = 2    'Tipo String, quando vazio(""), converte para Null.
//        DataTypeNumber = 3    'Tipo num��rico
//        DataTypeDate = 4      'Tipo de dado de Data, contendo somente a data. Desprezando hora, se hourver
//        DataTypeDateLong = 5  'Tipo de dado de Data, contendo data e hora, inclui da hora,minuto e segundo zerado se NÃO informado
//        DataTypeBoolean = 6   'Tipo boleano, onde True=-1 e False=0
//        DataTypeCommand = 7   'NÃO faz qualquer convers��o do dado
//        DataTypePicture = 8   'Imagem
//    End Enum
//
//
//
//#Region "Public Subs"
//    'Cria a conexão com o banco de dados
//    Public Function pbIOLogin(ByRef poCn As DBSClsConnection, _
//                              ByVal piDataServerType As DBSCnsDataServerType, _
//                              ByVal piDataBaseName As String) As Boolean
//        'Chama tela de login
//        Dim xDlgLogin As New DBSDlgLogin
//
//        'Se usuário NÃO foi informado...
//        If xDlgLogin.prUsuario <> "" Then
//            Try
//                'Confirma que conexão estão fechada
//                poCn.pbClose()
//                'Abre a conexão
//                Return poCn.pbOpen(piDataServerType, piDataBaseName, xDlgLogin.prUsuario, xDlgLogin.prSenha)
//
//                'If pbIOOpenConnection(poCn, piDataServerType, piDataBaseName, xDlgLogin.prUsuario, xDlgLogin.prSenha) Then
//                '    poCn.prUserName = xDlgLogin.prUsuario
//                '    Return True
//                'Else
//                '    poCn.prUserName = ""
//                '    Return False
//                'End If
//            Catch ex As Exception
//                pbShowException(ex)
//                Return False
//            End Try
//        Else
//            Return False
//        End If
//    End Function
//
//    'Cria Dataread. Aten������o o DataReader deve ser configurado como Nothing antes da chamada, para evitar a mensagem de Warning durante o Desingtime
//    Public Function pbIOOpenDataReader(ByRef piConnection As DBSClsConnection, _
//                                       ByRef poDataReader As OleDb.OleDbDataReader, _
//                                       ByVal piSQLQuery As String) As Boolean
//        'Se conexão NÃO estiver configurada
//        If piConnection Is Nothing Then
//            pbShowMsg(DBSCnsMsgStyle.ErroOk, "conexão NÃO definida!|[pbIOOpenDataReader]")
//            Return False
//        End If
//        Try
//            Dim xCmd As New OleDbCommand(piSQLQuery, piConnection.prConnection, piConnection.prTransation)
//            poDataReader = xCmd.ExecuteReader
//            xCmd.Dispose()
//            Return True
//        Catch ex As Exception
//            pbShowException(ex)
//            Return False
//        End Try
//    End Function
//
//    'Popula o DataSet e Cria o DataAdapter
//    Public Function pbIOOpenDataSet(ByRef piConnection As DBSClsConnection, _
//                                    ByRef poDataAdapter As OleDb.OleDbDataAdapter, _
//                                    ByRef poDataSet As DataSet, _
//                                    ByVal piSQLQuery As String, _
//                           Optional ByVal piReadOnly As Boolean = True) As Boolean
//        'Se conexão NÃO estiver configurada
//        If piConnection Is Nothing Then
//            pbShowMsg(DBSCnsMsgStyle.ErroOk, "conexão NÃO definida!|[pbIOOpenDataSet]")
//            Return False
//        End If
//        If pvIOOpenDataAdapter(piConnection, poDataAdapter, piSQLQuery, piReadOnly) Then
//            Try
//                'poDataSet.Clear()
//                Return pbIORefreshDataSet(piConnection, poDataAdapter, poDataSet)
//
//                ''Limpa os registros do dataset caso exitisse
//                'poDataSet.Clear()
//                'With poDataAdapter
//                '    'Atribui a transação em andamento a pesquisa. Sem isso ocorrer��� erro na utiliza������o de Begin/Commit/Rollback
//                '    .SelectCommand.Transaction = piConnection.prTransation
//                '    'Popula o DataSet
//                '    .Fill(poDataSet)
//                '    .FillSchema(poDataSet, SchemaType.Source)
//                'End With
//                ''Ignora localmente o a verifica���ao das constraints, deixando para esta função para o banco de dados  
//                'poDataSet.EnforceConstraints = False
//                'Return True
//            Catch ex As Exception
//                pbShowException(ex, piSQLQuery)
//                Return False
//            End Try
//        End If
//    End Function
//


//
//    'Popula o DataSet e Cria o DataAdapter
//    Public Function pbIOOpenDataTable(ByRef piConnection As DBSClsConnection, _
//                                      ByRef poDataAdapter As OleDb.OleDbDataAdapter, _
//                                      ByRef poDataTable As DataTable, _
//                                      ByVal piSQLQuery As String) As Boolean
//        'Se conexão NÃO estiver configurada
//        If piConnection Is Nothing Then
//            pbShowMsg(DBSCnsMsgStyle.ErroOk, "conexão NÃO definida!|[pbIOOpenDataSet]")
//            Return False
//        End If
//        If pvIOOpenDataAdapter(piConnection, poDataAdapter, piSQLQuery) Then
//            Try
//                'Atribui a transação em andamento a pesquisa. Sem isso ocorrer��� erro na utiliza������o de Begin/Commit/Rollback
//                poDataAdapter.SelectCommand.Transaction = piConnection.prTransation
//                'Popula o DataSet
//                poDataAdapter.Fill(poDataTable)
//                poDataAdapter.FillSchema(poDataTable, SchemaType.Source)
//                Return True
//            Catch ex As Exception
//                pbShowException(ex, piSQLQuery)
//                Return False
//            End Try
//        End If
//    End Function
//
//    'Cria a conexão com o banco de dados
//    Public Function pbIOOpenConnection(ByRef poCn As DBSClsConnection, _
//                                       ByVal piDataServerType As DBSCnsDataServerType, _
//                                       ByVal piDataBaseName As String, _
//                                       ByVal piUser As String, _
//                                       ByVal piPwd As String) As Boolean
//        Dim xConnectionString As String
//        Try
//            If piDataServerType = DBSCnsDataServerType.DBOracle Then
//                xConnectionString = "Provider=MSDAORA;Data Source=" & piDataBaseName & ";User ID=" & piUser & ";Password=" & piPwd
//            ElseIf piDataServerType = DBSCnsDataServerType.DBSQLServer Then
//                xConnectionString = "Provider=SQLOLEDB;Data Source=" & piDataBaseName & ";Integrated Security=SSPI;Initial Catalog=msdb" & ";User ID=" & piUser & ";Password=" & piPwd
//            Else
//                pbShowMsg(DBSCnsMsgStyle.ErroOk, "Tipo de Banco de Dados NÃO definido(DataServerType)")
//                xConnectionString = ""
//                Return False
//            End If
//            poCn.prConnectionString = xConnectionString
//
//            poCn.prConnection.Open()
//            Return True
//        Catch ex As Exception
//            pbShowException(ex)
//            Return False
//        End Try
//    End Function
//
//    'Fecha a conexão com o banco de dados
//    Public Function pbIOCloseConnection(ByRef ppCn As DBSClsConnection) As Boolean
//        Try
//            ppCn.prConnection.Close()
//            Return True
//        Catch ex As Exception
//            pbShowException(ex)
//            Return False
//        End Try
//    End Function    'Popula o recordset
//
//    'Refresh o conte��do do Dataset
//    Public Function pbIORefreshDataSet(ByRef piConnection As DBSClsConnection, _
//                                       ByRef piDataAdapter As OleDb.OleDbDataAdapter, _
//                                       ByRef ppDataSet As DataSet) As Boolean
//        'Se DataAdapter NÃO estiver configurada
//        If piDataAdapter Is Nothing Then
//            pbShowMsg(DBSCnsMsgStyle.ErroOk, "DataAdapter NÃO definida!|[pbIORefreshDataSet]")
//            Return False
//        End If
//        Try
//            ppDataSet.Clear()
//            With piDataAdapter
//                'Atribui a transação em andamento a pesquisa. Sem isso ocorrer��� erro na utiliza������o de Begin/Commit/Rollback
//                .SelectCommand.Transaction = piConnection.prTransation
//                'Popula o DataSet
//                .Fill(ppDataSet)
//                .FillSchema(ppDataSet, SchemaType.Source)
//            End With
//            ppDataSet.EnforceConstraints = False
//            Return True
//        Catch ex As Exception
//            pbShowException(ex, piDataAdapter.SelectCommand.CommandText)
//            Return False
//        End Try
//    End Function
//    'Refresh o conte��do do Dataset
//    Public Function pbIORefreshDataReader(ByRef ppDataReader As OleDb.OleDbDataReader) As Boolean
//        'Se DataAdapter NÃO estiver configurada
//        If ppDataReader Is Nothing Then
//            pbShowMsg(DBSCnsMsgStyle.ErroOk, "DataReader NÃO definido!|[pbIORefreshDataReader]")
//            Return False
//        End If
//        Try
//            Return True
//        Catch ex As Exception
//            pbShowException(ex)
//            Return False
//        End Try
//    End Function
//
//    'Cria linha na tabela
//    Public Function pbIOAddNewCancel(ByRef ppDataSet As DataSet, _
//                                     ByRef poDataRow As DataRow, _
//                            Optional ByVal piTableName As String = "") As Boolean
//        If ppDataSet Is Nothing Then
//            pbShowMsg(DBSCnsMsgStyle.ErroOk, "DataSet NÃO definido!|[pbIOAddNew]")
//            Return False
//        End If
//        Try
//            'Configura o comando de insert
//            If piTableName = "" Then
//                'Exclui uma linha na mem��ria
//                ppDataSet.Tables(0).Rows.Remove(poDataRow)
//            Else
//                'Exclui uma linha na mem��ria
//                ppDataSet.Tables(piTableName).Rows.Remove(poDataRow)
//            End If
//            Return True
//        Catch ex As Exception
//            pbShowException(ex)
//            Return False
//        End Try
//    End Function
//
//    'Cria linha na tabela
//    Public Function pbIOAddNew(ByRef ppDataSet As DataSet, _
//                               ByRef poDataRow As DataRow, _
//                      Optional ByVal piTableName As String = "") As Boolean
//        'Se o dataset NÃO estiver configurada
//        If ppDataSet Is Nothing Then
//            pbShowMsg(DBSCnsMsgStyle.ErroOk, "DataSet NÃO definido!|[pbIOAddNew]")
//            Return False
//        End If
//        Try
//            'Configura o comando de insert
//            If piTableName = "" Then
//                'Cria uma linha na mem��ria
//                poDataRow = ppDataSet.Tables(0).NewRow
//                'Adiciona a linha a tabela
//                ppDataSet.Tables(0).Rows.Add(poDataRow)
//            Else
//                'Cria uma linha na mem��ria
//                poDataRow = ppDataSet.Tables(piTableName).NewRow
//                'Adiciona a linha a tabela
//                ppDataSet.Tables(piTableName).Rows.Add(poDataRow)
//            End If
//            Return True
//        Catch ex As Exception
//            pbShowException(ex)
//            Return False
//        End Try
//    End Function
//
//    'Exclui linha corrente
//    Public Function pbIODelete(ByVal piDataRow As DataRow) As Boolean
//        Try
//            piDataRow.Delete()
//            Return True
//        Catch ex As Exception
//            pbShowException(ex)
//            Return False
//        End Try
//    End Function
//
//    'Envia comando ao DB. 
//    Public Function pbIOUpdate(ByRef piConnection As DBSClsConnection, ByRef ppDataAdapter As OleDb.OleDbDataAdapter, ByRef piDataSet As DataSet, Optional ByRef poRowsAffected As Integer = 0) As Boolean
//        'Se DataAdapter NÃO estiver configurada
//        If ppDataAdapter Is Nothing Then
//            pbShowMsg(DBSCnsMsgStyle.ErroOk, "DataAdapter NÃO definida!|[pbIOUpdate]")
//            Return False
//        End If
//        Try
//            'Dim xCmd As New OleDbCommandBuilder(ppDataAdapter)
//            With ppDataAdapter
//                .SelectCommand.Transaction = piConnection.prTransation
//                If .UpdateCommand IsNot Nothing Then
//                    .UpdateCommand.Transaction = piConnection.prTransation
//                End If
//                If .InsertCommand IsNot Nothing Then
//                    .InsertCommand.Transaction = piConnection.prTransation
//                End If
//                If .DeleteCommand IsNot Nothing Then
//                    .DeleteCommand.Transaction = piConnection.prTransation
//                End If
//                poRowsAffected = .Update(piDataSet, piDataSet.Tables(0).TableName.ToString)
//                If poRowsAffected = 0 Then
//                    pbShowMsg(DBSCnsMsgStyle.ErroOk, "Nenhum registro foi afetado!")
//                    Return False
//                End If
//            End With
//            Return True
//        Catch ex As Exception
//            pbShowException(ex)
//            Return False
//        End Try
//    End Function
//
//    'Login
//    'Public Function pbLogin() As Boolean
//    'Dim xDlg As New DBSDlgLogin
//    '    With xDlg
//    '        .ShowDialog()
//    '    End With
//    'End Function
//
//    Public Function pbIOExecute(ByRef piCn As DBSClsConnection, _
//                                ByVal piSQLCommand As String) As Integer
//        Try
//            Dim xCmd As New OleDbCommand(piSQLCommand, piCn.prConnection, piCn.prTransation)
//            xCmd.UpdatedRowSource = UpdateRowSource.None
//
//            Return xCmd.ExecuteNonQuery()
//            'xCmd.ex
//        Catch ex As Exception
//            pbShowException(ex, piSQLCommand)
//            Return 0
//        End Try
//    End Function
//
//    Public Function pbIOExecuteReader(ByRef piCn As DBSClsConnection, _
//                                      ByVal piSQLCommand As String) As OleDbDataReader
//        Try
//            Dim xCmd As New OleDbCommand(piSQLCommand, piCn.prConnection, piCn.prTransation)
//            xCmd.UpdatedRowSource = UpdateRowSource.Both
//
//            Return xCmd.ExecuteReader()
//        Catch ex As Exception
//            pbShowException(ex, piSQLCommand)
//            Return Nothing
//        End Try
//    End Function
//

//

//
//    Public Function pbGetEmptyFromObjectType(ByVal piObject As Object) As Object
//        Try
//            pbGetEmptyFromObjectType = Nothing
//            If TypeOf piObject Is String OrElse _
//               TypeOf piObject Is DateTime Then
//                Return ""
//            ElseIf TypeOf piObject Is Decimal OrElse _
//                   TypeOf piObject Is Integer OrElse _
//                   TypeOf piObject Is Double OrElse _
//                   TypeOf piObject Is Long OrElse _
//                   TypeOf piObject Is Int32 OrElse _
//                   TypeOf piObject Is Int16 OrElse _
//                   TypeOf piObject Is Int64 OrElse _
//                   TypeOf piObject Is Boolean Then
//                Return 0
//            End If
//        Catch ex As Exception
//            pbShowException(ex)
//            Return Nothing
//        End Try
//    End Function
//
//
//    Public Function pbIOGetSQLStrAdd(ByRef piCn As DBSClsConnection) As String
//        Return pbIOGetSQLStrAdd(piCn.prDataServerType)
//    End Function
//    Public Function pbIOGetSQLStrAdd(ByVal piDataServerType As DBSCnsDataServerType) As String
//        ' Objetivo: Retorna String com comando utilizado para concatenar string conforme o tipo de Banco de Dados
//        '
//        ' Retorno da função:
//        '     String com comando.
//        '
//        '-------------------------------------------------------------------------------
//        If piDataServerType = DBSCnsDataServerType.DBOracle Then
//            pbIOGetSQLStrAdd = " || "
//        Else
//            pbIOGetSQLStrAdd = " & "
//        End If
//    End Function
//
//    Public Function pbIOToString(ByRef piCn As DBSClsConnection, _
//                                 ByVal piDataFieldName As String, _
//                                 ByVal piColumnDataType As DBSCnsDataType) As String
//        Return pbIOToString(piCn.prDataServerType, piDataFieldName, piColumnDataType)
//    End Function
//
//    Public Function pbIOToString(ByVal piDataServeType As DBSCnsDataServerType, _
//                                 ByVal piDataFieldName As String, _
//                                 ByVal piColumnDataType As DBSCnsDataType) As String
//        Try
//            Dim xS As String = ""
//            If piDataServeType = DBSCnsDataServerType.DBOracle Then
//                If piColumnDataType = DBSCnsDataType.DataTypeCommand Or _
//                    piColumnDataType = DBSCnsDataType.DataTypeNone Or _
//                    piColumnDataType = DBSCnsDataType.DataTypePicture Then
//                    pbShowMsg(DBSCnsMsgStyle.ErroOk, "Tipo " & piColumnDataType.ToString & " NÃO pode ser convertido em String")
//                Else
//                    If piColumnDataType = DBSCnsDataType.DataTypeString Then
//                        xS = piDataFieldName
//                    Else
//                        xS = "to_char(" & piDataFieldName & ","
//                        Select Case piColumnDataType
//                            Case DBSCnsDataType.DataTypeBoolean
//                                xS = xS & "'0'"
//                            Case DBSCnsDataType.DataTypeDate
//                                xS = xS & "'yyyy/mm/dd'"
//                            Case DBSCnsDataType.DataTypeDateLong
//                                xS = xS & "'yyyy/mm/dd,HH24:MI:SSSS'"
//                            Case DBSCnsDataType.DataTypeID
//                                xS = xS & "'0000000000'"
//                            Case DBSCnsDataType.DataTypeNumber
//                                xS = xS & "'000000000000000.0000000000000000'"
//                            Case Else
//                                pbShowMsg(DBSCnsMsgStyle.ErroOk, "Tipo inexistente!")
//                        End Select
//                        xS = xS & ")"
//                    End If
//                End If
//            End If
//            Return xS
//        Catch ex As Exception
//            pbShowException(ex)
//            Return ""
//        End Try
//    End Function
//
//
//#End Region
//
//#Region "Private Subs"
//    'Cria o DataAdapter
//    Private Function pvIOOpenDataAdapter(ByRef piConnection As DBSClsConnection, _
//                                         ByRef poDataAdapter As OleDb.OleDbDataAdapter, _
//                                         ByVal piSQLQuery As String, _
//                                Optional ByVal piReadOnly As Boolean = True) As Boolean
//        'Se conexão NÃO estiver configurada
//        If piConnection Is Nothing Then
//            pbShowMsg(DBSCnsMsgStyle.ErroOk, "conexão NÃO definida!|[pbIOOpenDataAdapter]")
//            Return False
//        End If
//        Try
//            poDataAdapter = New OleDb.OleDbDataAdapter(piSQLQuery, piConnection.prConnection)
//            'With poDataAdapter
//            '    .MissingSchemaAction = MissingSchemaAction.Add
//            '    .MissingMappingAction = MissingMappingAction.Passthrough
//            'End With
//            With poDataAdapter
//                .MissingSchemaAction = MissingSchemaAction.Add
//                .MissingMappingAction = MissingMappingAction.Passthrough
//            End With
//
//            'Cria comando padrão. Podendo ser substituido depois 
//            Dim xCmd As New OleDbCommandBuilder(poDataAdapter)
//
//            'Se NÃO for somente leitura...
//            'Aten������o! S��� ��� permitido acesso a escrita caso a query seja somente em uma ���nica tabela.
//            If Not piReadOnly Then
//                With poDataAdapter
//                    .InsertCommand = xCmd.GetInsertCommand(True) 'True=Procura utilizar a PK na clausuda where para fazer o update
//                    .UpdateCommand = xCmd.GetUpdateCommand(True)
//                    .DeleteCommand = xCmd.GetDeleteCommand(True)
//                End With
//            End If
//
//            With xCmd
//                .SetAllValues = True
//                .ConflictOption = ConflictOption.OverwriteChanges
//            End With
//
//            Return True
//        Catch ex As Exception
//            pbShowException(ex, piSQLQuery)
//            Return False
//        End Try
//    End Function
//
//
//    'Public Function pbIOBuildSQLCommandWhere(ByVal piColumns As DBSClsColumns, ByVal piRegsMode As DBSCnsRegsMode) As String
//    '    Return pvIOBuildSQLCommandWhere(piColumns, piRegsMode)
//    'End Function
//
//    'Private Function pvIOBuildSQLCommandWhere(ByVal piObject As Object, ByVal piRegsMode As DBSCnsRegsMode, Optional ByRef piCn As DBSClsConnection = Nothing) As String
//    '    Try
//    '        Dim xSQLWhere As String = ""
//    '        Dim xColumns As DBSClsColumns
//    '        If TypeOf piObject Is DBSClsColumns Then
//    '            xColumns = piObject
//    '        Else
//    '            xColumns = DirectCast(piObject, DBSClsRegs).prCommandColumns
//    '        End If
//    '        Try
//    '            For Each xCol As DBSClsColumns.DBSClsColumn In xColumns
//    '                If Not xCol.prDataFieldName = "" Then
//    '                    If xCol.prIsPK Then
//    '                        If Not xSQLWhere = "" Then
//    '                            xSQLWhere = xSQLWhere & " AND "
//    '                        End If
//    '                        If xCol.prValue Is Nothing Then
//    '                            xSQLWhere = xSQLWhere & xCol.prDataFieldName & "= NULL"
//    '                        Else
//    '                            If TypeOf piObject Is DBSClsColumns Then
//    '                                xSQLWhere = xSQLWhere & xCol.prDataFieldName & "=" & xCol.prValueSQLFormated(piCn)
//    '                            Else
//    '                                xSQLWhere = xSQLWhere & xCol.prDataFieldName & "=" & DirectCast(piObject, DBSClsRegs).prValueSQLFormated(xCol.prDataFieldName)
//    '                            End If
//    '                        End If
//    '                    End If
//    '                End If
//    '            Next
//    '            If Not xSQLWhere = "" Then
//    '                xSQLWhere = " WHERE " & xSQLWhere
//    '            End If
//    '            If piRegsMode = DBSCnsRegsMode.RegsModeLock Then
//    '                xSQLWhere = xSQLWhere & " FOR UPDATE OF " & xColumns(0).prDataFieldName & "  "
//    '            End If
//    '            Return xSQLWhere
//    '        Catch ex As Exception
//    '            pbShowException(ex)
//    '            Return Nothing
//    '        End Try
//    '    Catch ex As Exception
//    '        pbShowException(ex)
//    '        Return Nothing
//    '    End Try
//    'End Function
//
//
//
//#End Region
//End Module
//
//#Region "VB6 c��digo antigo"
//'Public Enum DBCnsSQLMode
//'    SQLInsert = 1
//'    SQLDelete = 2
//'    SQLUpdate = 3
//'    SQLSelect = 4
//'    SQLLock = 5
//'End Enum
//
//'Public Enum DBCnsDataType
//'    TypeID                  'Tipo num��rico utilizadao como chave. 0(zero) ou -1 serãoconvertido para Null
//'    TypeString              'Tipo String, quando vazio(""), converte para Null.
//'    TypeNumber              'Tipo num��rico
//'    TypeDate                'Tipo de dado de Data, contendo somente a data. Desprezando hora, se hourver
//'    TypeDateLong            'Tipo de dado de Data, contendo data e hora, inclui da hora,minuto e segundo zerado se NÃO informado
//'    TypeBoolean             'Tipo boleano, onde True=-1 e False=0
//'    TypeCommand             'NÃO faz qualquer convers��o do dado
//'End Enum
//
//'Public Enum DBCnsServer
//'    DBAccess
//'    DBSQLServer
//'    DBOracle
//'End Enum
//
//'Public Enum DBCnsIOType
//'    TypeSQLInsertOrUpdate    'Sem uso atualmente
//'    TypeSQLSelect            'Comandos de Select/Update/Insert/Delete atraves de comando SQL
//'    TypeIOMove               'Comandos de Update e Insert diretamente no Recordset
//'End Enum
//
//'Type DBSQLField
//'    FieldName       As String
//'    FieldValue      As Variant
//'    FieldValueSQL   As Variant
//'    FieldType       As DBCnsDataType
//'    FieldIsKey      As Boolean
//'End Type
//
//
//''dbOpenTable dbOpenDynaset dbOpenSnapshot
//'Public SQL As String
//
//'Public Const gFieldVersao = "DBVersao"
//'Public Const gTableSetup = "Setup"
//
//'Public gLockTimeOut As Integer
//'Public gFormTimeOut As Integer
//'Public gServer As DBCnsServer
//'Public gDecimalPoint    As String * 1
//'Public gLockReg As Integer
//'Public gAsync As Integer
//'Public gTransactionCount As Integer
//'Public gLogIO As Boolean
//'Public gErrExecute As Long
//
//'Dim wsTime1 As Object
//
//'Global gLogOrigem       As String
//'Public Function IOOpenConnection(pDB As ADODB.Connection, ByVal pNome As String, ByVal pUsuario As String, ByVal pSenha As String, Optional pODBC As Variant, Optional pVersao As Variant, Optional pShowMsg As Variant) As Integer
//'    'VISTO
//'    ' Objetivo: Abre a conexão com o Banco de Dados
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada:
//'    '     pDB          - Objeto de conexão;
//'    '     pNome        - Nome do Banco a ser efetuado a conexão;
//'    '     pUsuario     - Chave do usuário;
//'    '     pSenha       - senha do usuário;
//'    '     pODBC        - String de conexão ODBC. Quando informada substitui a string de conexão padrão;
//'    '     pVersao      - Vers���o do banco de dados. Utilizado para testar vers���o espec���fica para banco Access;
//'    '     pShowMsg     - Quando informado, indica que se deseja exibir as telas de mensagem quando houver.
//'    '
//'    ' Sa���da:
//'    '     pDB          - NÃO modificado;
//'    '     pNome        - NÃO modificado;
//'    '     pUsuario     - NÃO modificado;
//'    '     pSenha       - NÃO modificado;
//'    '     pODBC        - NÃO modificado;
//'    '     pVersao      - NÃO modificado;
//'    '     pShowMsg     - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xODBC As String
//'    Dim xVERSAO As Long
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//'    gDecimalPoint = ""
//'    If IsMissing(pODBC) Then
//'        xODBC = "Provider=MSDAORA.1;Persist Security Info=True;Data Source=" & pNome & ";User ID=" & pUsuario & ";Password=" & pSenha & ";"
//'    Else
//'        xODBC = pODBC & ";User ID=" & pUsuario & ";Password=" & pSenha & ";"
//'    End If
//'    With pDB
//'        .CommandTimeout = gLockTimeOut
//'        .CursorLocation = adUseClient
//'        .Mode = adModeReadWrite
//'        .ConnectionString = xODBC
//'        '.Open xODBC, pUsuario, pSenha
//'        If gAsync Then
//'            .Open, , , adAsyncConnect 'adAsyncExecute
//'        Else
//'            .Open()
//'        End If
//'    End With
//
//'    IOOpenConnection = IOIsDone(pDB, "CN")
//'    If Not IOOpenConnection Then
//'        If Err = -2147467259 Then
//'            ShowMsg ErroOk, "conexão NÃO realizada!" & "DSN:" & pNome & "#" & Error(Err)
//'        Else
//'            GetErro(Err)
//'        End If
//'        Exit Function
//'    End If
//
//'    If IsCorrompido() And xODBC = "" Then
//'        IOOpenConnection = 0
//'    End If
//
//'    If Err <> 0 Then
//'        IOOpenConnection = 0
//'        If Err = 3044 Then
//'            MsgBox("Banco " & pNome & " NÃO encontrado!", MB_OK + MB_IconStop, gMsgErro)
//'        ElseIf GetOptional(pShowMsg, False) Then
//'            GetErro(Err, pNome)
//'        End If
//'    Else
//'        IOOpenConnection = -1
//'    End If
//'End Function
//'Public Function IOIsDone(pObject As Object, Optional pTexto As Variant) As Boolean
//'    'VISTO
//'    ' Objetivo: Manter o sistema em loop at��� a finaliza������o dos comandos de conexão, pesquisa e execu����o
//'    '           no banco de dados estajam finalizados. este procedimento ��� utilizado na conex���es
//'    '           assincronas com o banco.
//'    '
//'    ' Retorno da função:
//'    '     True         - Procedimento Finalizado;
//'    '     False        - Procedimento saiu por timeout.
//'    '
//'    ' Par���metros:
//'    ' Entrada:
//'    '     pObject      - Objeto que se esta testando a finaliza������o do comando;
//'    '     pTexto       - NÃO usado.
//'    '
//'    ' Sa���da:
//'    '     pObject      - NÃO modificado;
//'    '     pTexto       - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xTime As Object
//'    xTime = Time
//'    If Not Err = 0 Then
//'        IOIsDone = False
//'        Exit Function
//'    End If
//'    On Error Resume Next
//
//'    Do Until ((pObject.State And adStateExecuting) <> adStateExecuting) _
//'         And ((pObject.State And adStateFetching) <> adStateFetching) _
//'         And ((pObject.State And adStateConnecting) <> adStateConnecting)
//'        GoSub LoopTimeOut
//'    Loop
//
//
//'    IOIsDone = Not GetErro(Err)
//'    Exit Function
//
//'LoopTimeOut:
//'    If IsTimeOut(xTime, gLockTimeOut) Then
//'        IOIsDone = False
//'        Err.Raise(3045)
//'        Exit Function
//'    End If
//'    Return
//'End Function
//'Public Function IOOpenRecordSet(pDB As ADODB.Connection, pRecordset As ADODB.Recordset, ByVal pSQL As String, pTipo As CursorTypeEnum, Optional pCursorLocationIsServer As Variant, Optional pShowMsg As Variant) As Integer
//'    'VISTO
//'    ' Objetivo: Abrir Recordset
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB                     - conexão com o banco de dados;
//'    '     pRecordset              - Recordset que controlar�� os registros;
//'    '     pSQL                    - Systax SQL de pesquisa dos registros;
//'    '     pTipo                   - Tipo de Cursor que serãoutilizado conforme
//'    '                               constantes definidas em CURSORTYPEENUM
//'    '     pCursorLocationIsServer - For��a, quando informado, a localiza����o do controle do cursor.
//'    '     pShowMsg                - Indica, quando informado, que a função exibir�� as mensagens que possuir
//'    '
//'    ' Sa���da...:
//'    '     pDB                     - NÃO modificado;
//'    '     pRecordset              - Recordset nulo quando houver erro;
//'    '                               Recordset v��lido quando NÃO houver erro;
//'    '     pSQL                    - NÃO modificado;
//'    '     pTipo                   - NÃO modificado;
//'    '     pCursorLocationIsServer - NÃO modificado;
//'    '     pShowMsg                - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//
//'    If gServer = 0 Then 'Seta vari��vel publica com o tipo de servidor de banco da dados utilizado
//'        gServer = GetServer(pDB)
//'    End If
//
//'    pRecordset = New ADODB.Recordset
//'    With pRecordset
//'        If pTipo = adOpenDynamic Then
//'            If GetOptional(pCursorLocationIsServer, True) Then
//'                .CursorLocation = adUseServer
//'            Else
//'                .CursorLocation = adUseClient
//'            End If
//'            .CursorType = pTipo
//'        Else
//'            .CursorLocation = adUseClient
//'        End If
//'        .LockType = adLockOptimistic ' adLockBatchOptimistic 'Comentado em 06/03/07 - Ricardo Vilar
//'        If gAsync Then
//'            .Open(pSQL, pDB, pTipo, adLockOptimistic, adAsyncExecute + adAsyncConnect + adAsyncFetch)
//'        Else
//'            .Open(pSQL, pDB, pTipo, adLockOptimistic)
//'        End If
//'    End With
//'    If gServer = DBOracle And Not gAsync Then
//'        GoSub LoopTimeOut
//'        If Err = -2147467259 Then
//'            'Err.Raise 3045
//'            Exit Function
//'        ElseIf Err = 3705 Then
//'            Err.Clear()
//'        End If
//'    Else
//'        If Not IOIsDone(pRecordset, "RS" & pSQL) Then
//'            If GetOptional(pShowMsg, False) Then
//'                IOOpenRecordSet = CBool(Not GetErro(Err, pSQL))
//'            End If
//'            Err.Raise(Err)
//'        End If
//'    End If
//'    If Err <> 0 Then
//'        WriteIniString gPathWindows & "\DB.log", "RECORDSET", "SQL" & Date & Time, Error(Err) & "#@" & pSQL
//'    End If
//
//'    IOOpenRecordSet = CBool(Err = 0)
//'    Exit Function
//
//'LoopTimeOut:
//'    xTime = Time
//'    Do Until IsTimeOut(xTime, gLockTimeOut) Or Not Err = -2147467259
//'        pRecordset.Open(pSQL, pDB, pTipo, adLockOptimistic)
//'    Loop
//'    Return
//'End Function
//
//'Public Function IOClose(ByVal pObject As Object) As Integer
//'    'VISTO
//'    ' Objetivo: Fecha conexão do objeto (connection, recordset)
//'    '           O fechamento do objeto NÃO efetua Commit ou Rollback
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pObjeto      - Objeto que se deseja fecha conexão.
//'    '
//'    ' Sa���da...:
//'    '     pObjeto      - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//'    If pObject Is Nothing Then
//'        Exit Function
//'    End If
//'    If pObject.State <> adStateClosed Then
//'        If Not (pObject.State And adStateOpen) = adStateOpen Then
//'            IOCancel(pObject)
//'        Else
//'            pObject.Close()
//'            If TypeOf pObject Is ADODB.Recordset Then
//'                pObject.ActiveConnection = Nothing
//'            End If
//'            IOIsDone(pObject, "CL")
//'        End If
//'    End If
//
//'    pObject = Nothing
//
//'    IOClose = CBool(Err = 0)
//'    Err = xErr
//'End Function
//
//'Public Function IOCancel(ByVal pObject As Object) As Integer
//'    ' Objetivo: Cancela execu����o em andamento do objeto
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pObjeto      - Objeto que se deseja cancelar execu����o.
//'    '
//'    ' Sa���da...:
//'    '     pObjeto      - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//'    pObject.Cancel()
//'    IOIsDone(pObject, "CAN")
//'    IOCancel = CBool(Err = 0)
//'End Function
//
//
//'Public Function IOUpdate(pRecordset As ADODB.Recordset, Optional pShowMsg As Variant, Optional pErr As Variant) As Integer
//'    'VISTO
//'    ' Objetivo: Executa comando de Update do Recordset
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pRecordset   - Recordset a ser efetuado o Update;
//'    '     pShowMsg     - Indica, quando informado, que serão exibidas as mensagens existentes na função
//'    '                    quando for necess��rio;
//'    '     pErr         - Quando informado, retornar�� o c��digo de erro encontrado na função, se houver.
//'    '
//'    ' Sa���da...:
//'    '     pRecordset   - Recordset com os valores atualizados(Sem commit ou rollback);
//'    '     pShowMsg     - NÃO modificado;
//'    '     pErr         - Quando informado, retornar�� o c��digo de erro encontrado na função, se houver.
//'    '-------------------------------------------------------------------------------
//'    Dim xFlag As Boolean
//'    Dim xOk As Boolean
//'    Dim xMode As Object
//'    Dim xLock As Object
//'    Dim xErroMsg As String
//
//'    wsTime1 = Time
//'    If Err = 0 Then
//'        On Error Resume Next
//'        xOk = True
//'        xFlag = False
//'        'Mant��m o loop enquanto estiver em lock e NÃO for Timeout
//'        'xFlag obriga que o loop seja executado pelo menos uma vez
//'        Do Until xFlag And (Not IsLocked() Or IsTimeOut(wsTime1, gLockTimeOut))
//'            xFlag = True
//
//'            pRecordset.Update()
//'            xErroMsg = Error(Err)
//
//'            'Aguarda execu����o do comando pelo banco
//'            IOIsDone(pRecordset, "UP")
//
//'            xLock = IsLocked()
//'        Loop
//'        If xLock Then
//'            Err.Clear()
//'        End If
//'    End If
//
//'    'Comando do objeto foi cancelado
//'    If Err = 3426 Then
//'        Err.Clear()
//'    End If
//
//'    If Err <> 0 Then
//'        xErr = Err
//'        IOCancelUpdate(pRecordset, True)
//'        IOIsDone(pRecordset, "UP")
//'        xOk = False
//'        Err = xErr
//'    End If
//
//'    If Not IsMissing(pErr) Then
//'        pErr = Err
//'    End If
//
//'    IOUpdate = CBool((Err = 0) And xOk)
//
//'    If Err <> 0 Or Not xOk Then
//'        If GetOptional(pShowMsg, False) Then
//'            IOUpdate = CBool(Not GetErro(Err, xErroMsg) And xOk)
//'        End If
//'    End If
//'    IOFreeLock()
//'End Function
//'Public Function IOCancelUpdate(pRecordset As ADODB.Recordset, Optional pShowMsg As Variant) As Integer
//'    ' Objetivo: Cancela o comando de Update em execu����o
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pRecordset   - Recordset a ser cancelado o comando de Update;
//'    '     pShowMsg     - Indica, quando informado, que serão exibidas as mensagens existentes na função
//'    '                    quando for necess��rio;
//'    '
//'    ' Sa���da...:
//'    '     pRecordset   - NÃO modificado;
//'    '     pShowMsg     - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xFlag As Boolean
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//'    wsTime1 = Time
//'    xFlag = False
//'    'Mant��m o loop enquanto estiver em lock e NÃO for Timeout
//'    'xFlag obriga que o loop seja executado pelo menos uma vez
//'    Do Until xFlag And (Not IsLocked() Or IsTimeOut(wsTime1, gLockTimeOut))
//'        xFlag = True
//'        pRecordset.CancelUpdate()
//'        IOIsDone(pRecordset, "CUP")
//'    Loop
//'    IOCancelUpdate = CBool(Err = 0)
//
//'    If Err <> 0 Then
//'        If GetOptional(pShowMsg, False) Then
//'            IOCancelUpdate = Not GetErro(Err, "")
//'        End If
//'    End If
//'    IOFreeLock()
//'End Function
//'Public Function IODelete(pRecordset As ADODB.Recordset, Optional pShowMsg As Variant) As Integer
//'    ' Objetivo: Executa comando de Delete do Recordset
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pRecordset   - Recordset a ser efetuado o Update;
//'    '     pShowMsg     - Indica, quando informado, que serão exibidas as mensagens existentes na função
//'    '                    quando for necess��rio.
//'    '
//'    ' Sa���da...:
//'    '     pRecordset   - NÃO modificado;
//'    '     pShowMsg     - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xFlag As Boolean
//'    wsTime1 = Time
//'    If Err = 0 Then
//'        On Error Resume Next
//'        xFlag = False
//'        Do Until xFlag And (Not IsLocked() Or IsTimeOut(wsTime1, gLockTimeOut))
//'            xFlag = True
//'            pRecordset.Delete()
//'            IOIsDone(pRecordset, "DL")
//'        Loop
//'    End If
//'    IODelete = CBool(Err = 0)
//'    If Err <> 0 Then
//'        If GetOptional(pShowMsg, False) Then
//'            IODelete = Not GetErro(Err)
//'        End If
//'    End If
//'    IOFreeLock()
//'End Function
//
//'Public Function IOEdit(pRecordset As ADODB.Recordset, Optional pShowMsg As Variant) As Boolean
//'    'VISTO
//'    ' Objetivo: Executa comando de Edit do Recordset
//'    '           Esta função existe para manter compatibilidade com o Access
//'    '           No caso do ORACLE, NÃO existe relevancia na execu����o desta rotina
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pRecordset   - Recordset a ser efetuado o Edit;
//'    '     pShowMsg     - Indica, quando informado, que serão exibidas as mensagens existentes na função
//'    '                    quando for necess��rio.
//'    '
//'    ' Sa���da...:
//'    '     pRecordset   - NÃO modificado;
//'    '     pShowMsg     - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xFlag As Boolean
//'    Dim xOk As Boolean
//'    Dim xMode As Object
//
//'    wsTime1 = Time
//'    If Err = 0 Then
//'        On Error Resume Next
//'        xOk = True
//'        xFlag = False
//'        'Mant��m o loop enquanto estiver em lock e NÃO for Timeout
//'        'xFlag obriga que o loop seja executado pelo menos uma vez
//'        Do Until xFlag And (Not IsLocked() Or IsTimeOut(wsTime1, gLockTimeOut))
//'            xFlag = True
//'            IOFreeLock()
//'            'pRecordset.UpdateBatch
//'        Loop
//'    End If
//
//'    IOEdit = CBool(Err = 0)
//
//'    If Err <> 0 Then
//'        If GetOptional(pShowMsg, False) Then
//'            IOEdit = CBool(Not GetErro(Err))
//'        End If
//'    End If
//
//'End Function
//
//'Public Function IOAdd(pRecordset As ADODB.Recordset, Optional pShowMsg As Variant) As Boolean
//'    'VISTO
//'    ' Objetivo: Executa comando de Insert(addnew) no Recordset
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pRecordset   - Recordset a ser efetuado o Insert;
//'    '     pShowMsg     - Indica, quando informado, que serão exibidas as mensagens existentes na função
//'    '                    quando for necess��rio.
//'    '
//'    ' Sa���da...:
//'    '     pRecordset   - Retorna o recordset em mem��ria a ser utiliza para inclus���o;
//'    '     pShowMsg     - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xFlag As Boolean
//'    Dim xOk As Boolean
//'    Dim xMode As Object
//'    Dim xLock As Object
//'    wsTime1 = Time
//'    If Err = 0 Then
//'        On Error Resume Next
//'        xOk = True
//'        xFlag = False
//'        'Mant��m o loop enquanto estiver em lock e NÃO for Timeout
//'        'xFlag obriga que o loop seja executado pelo menos uma vez
//'        Do Until xFlag And (Not IsLocked() Or IsTimeOut(wsTime1, gLockTimeOut))
//'            xFlag = True
//'            pRecordset.AddNew()
//'            IOIsDone(pRecordset, "AD")
//'            xLock = IsLocked()
//'        Loop
//'    End If
//'    IOAdd = CBool(Err = 0)
//'    If Err <> 0 Then
//'        If GetOptional(pShowMsg, False) Then
//'            IOAdd = CBool(Not GetErro(Err))
//'        End If
//'    End If
//'End Function
//
//'Public Function IOExecute(pDB As ADODB.Connection, ByVal pSQL As String, Optional pCount As Long, Optional pShowMsg As Variant, Optional pReturnedError As Variant, Optional pLogIO As Variant) As Boolean
//'    'VISTO
//'    ' Objetivo: Executa comando SQL diretamente na conexão
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB            - conexão que serãoutlizada para efetuar o comando SQL;
//'    '     pSQL           - Comando SQL que se deseja executar;
//'    '     pCount         - Retorna, quando informado, a quantidade de registros efetados pelo comando;
//'    '     pShowMsg       - Indica, quanto informado, se serão exibidas as mensagen que a função achar necess���rias(true/false);
//'    '     pReturnedError - Retorna, quando informado, o c��digo de erro encontrado, caso ocorra;
//'    '     pLogIO         - Boleano que indica, quando informado, que o comando SQL a ser efetuado ser��
//'    '                      armazenado na tabela de log AC_LOG.
//'    '                      True = Comando SQL serãogravado na AC_LOG
//'    '                      False = Comando SQL NÃO serãogravado na AC_LOG
//'    '                      Default = False.
//'    '
//'    ' Sa���da...:
//'    '     pDB            - NÃO modificado;
//'    '     pSQL           - NÃO modificado;
//'    '     pCount         - Retorna, quando informado, a quantidade de registros efetados pelo comando;
//'    '     pShowMsg       - NÃO modificado;
//'    '     pReturnedError - Retorna, quando informado, o c��digo de erro encontrado, caso ocorra;
//'    '     pLogIO         - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xCursor As Integer
//'    Dim xErr As Long
//'    Dim xCount As Long
//'    Dim xLogIO As Boolean
//'    Dim xMode As DBCnsModo
//'    Dim xTime As Object
//'    gErrExecute = 0
//'    xLogIO = GetOptional(pLogIO, False)
//'    xErr = Err
//'    On Error Resume Next
//'    xTime = Time
//
//'    If gAsync Then
//'        pDB.Execute(pSQL, xCount, adAsyncExecute)
//'    Else
//'        pDB.Execute(pSQL, xCount)
//'    End If
//
//'    If Error(Err) <> "" Then
//'        'Recuperando o erro ORACLE
//'        gErrExecute = Mid(Error(Err), InStr(1, (Error(Err)), "ORA-") + 4, 5)
//'        'Movifica n��mero do erro
//'        'caso o erro oracle seja por motivo de integra������o com o LOF
//'        'Os error abaixo(20000,20888) tem tratamento especial na rotina GetErro
//'        If gErrExecute = 20000 Or gErrExecute = 20888 Then
//'            Err = gErrExecute
//'        End If
//
//'    End If
//'    'If gLogArq > 0 Then
//'    '    IOWriteTXT gLogArq, Format(Time - xTime, "hh:mm:ss") & ";" & pSQL
//'    'End If
//'    IOIsDone(pDB)
//
//'    If xLogIO Then
//'        Select Case UCase(Left(pSQL, 6))
//'            Case "INSERT"
//'                xMode = Inclusao
//'            Case "UPDATE"
//'                xMode = Alteracao
//'            Case "DELETE"
//'                xMode = Exclusao
//'        End Select
//'        xOk = IOLog(pDB, App.EXEName, xMode, pSQL)
//'    End If
//
//'    pCount = xCount
//
//'    If Not IsMissing(pReturnedError) Then
//'        pReturnedError = Err
//'    End If
//
//'    If Err <> 0 Then
//'        If GetOptional(pShowMsg, False) Then
//'            IOExecute = CBool(Not GetErro(Err, "#" & Error(Err) & "#" & pSQL))
//'        Else
//'            IOExecute = False
//'            Err.Clear()
//'        End If
//'        Exit Function
//'    Else
//'        IOExecute = True
//'    End If
//'    'Screen.MousePointer = xCursor
//'End Function
//
//'Public Function IORequery(pRecordset As ADODB.Recordset, Optional pPkName As Variant) As Boolean
//'    ' Objetivo: Executa comando de requery no Recordset e procura posicionar o recordset
//'    '           na mesma linha antes de efetuado o requery, conforme chave informada.
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pRecordset   - Recordset a ser efetuado o requery;
//'    '     pPkName      - Colunas que fazem parte da chave.
//'    '                    Se informado, a função tentar��� posicionar o recordset na
//'    '                    mesma linha antes de efetuado o requery.
//'    '
//'    ' Sa���da...:
//'    '     pRecordset   - NÃO modificado;
//'    '     pPkName      - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xPkValue As String
//'    Dim xRecord As ADODB.Recordset
//'    Dim xNeedRequery As Object
//'    On Error Resume Next
//'    IORequery = True
//
//'    If Not IsMissing(pPkName) Then
//'        xPkValue = IOGetPkValue(pRecordset, (pPkName), xNeedRequery)
//'    End If
//
//'    pRecordset.Requery()
//
//'    If Not IsMissing(pPkName) And xPkValue <> "" Then
//'        If Not IOFindFirst(pRecordset, xPkValue, xNeedRequery) Then
//'            IORequery = False
//'        End If
//'    End If
//
//'    If Not IOIsDone(pRecordset, "RQ") Then
//'        IORequery = False
//'    End If
//'End Function
//
//'Public Sub IOCloseAllDatabases(Optional pDB1 As Variant, Optional pDB2 As Variant)
//'    ' Objetivo: Exibe tela de mensagem de erro quando houver
//'    '
//'    ' Retorno da função:
//'    '     True         - NÃO Houve erro;
//'    '     False        - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pErroNumber  - n��mero do erro;
//'    '     pTexto       - Texto complementar a ser exibido na mensagem quando
//'    '                    houver determinados erros.
//'    '
//'    ' Sa���da...:
//'    '     pErroNumber  - NÃO modificado;
//'    '     pTexto       - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xDB As ADODB.Connection
//'    Dim xBase As Integer
//'    Dim xName As String
//'    Dim xDB1 As String
//'    Dim xDB2 As String
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//'    If IsMissing(pDB1) Then xDB1 = "" Else  : xDB1 = pDB1
//'    If IsMissing(pDB2) Then xDB2 = "" Else  : xDB2 = pDB2
//'    xBase = 0
//'    For x = 0 To DBEngine.Workspaces(0).Connections.Count - 1
//'        xName = DBEngine.Workspaces(0).Connections(x - xBase).Name
//'        If Not xName = xDB1 And Not xName = xDB2 Then
//'            IOCloseAllRecordsets(DBEngine.Workspaces(0).Connections(xName))
//'            DBEngine.Workspaces(0).Connections(xName).Close()
//'            xBase = xBase + 1
//'        End If
//'    Next
//'End Sub
//'Public Sub IOCloseAllRecordsets(ByVal pDB As ADODB.Connection)
//'    ' Objetivo: Fecha todos os recordset aberto de conexão informada
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB          - conexão.
//'    '
//'    ' Sa���da...:
//'    '     pDB          - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xTable As ADODB.Recordset
//'    On Error Resume Next
//'    For x = 0 To pDB.Recordsets.Count - 1
//'        IOClose(pDB.Recordsets(0))
//'    Next
//'End Sub
//
//'Public Sub CopyFields(pSource As ADODB.Recordset, pTarget As ADODB.Recordset, Optional pErr As Variant)
//'    Dim xName As String
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//
//'    For Y = 0 To pSource.Fields.Count - 1
//'        xName = pSource(Y).Name
//'        pTarget(xName) = GetNotNull(pSource(xName))
//'    Next
//'    If Not IsMissing(pErr) Then pErr = Err
//'    Err.Clear()
//'End Sub
//'Public Function IORefresh(pRecordset As Control, Optional pShowMsg As Variant) As Boolean
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//'    pRecordset.Refresh()
//'    If Not Err = 0 Then
//'        If GetOptional(pShowMsg, False) Then
//'            IORefresh = CBool(Not GetErro(Err, (pRecordset.Name)))
//'        End If
//'    Else
//'        If pRecordset.Recordset.Updatable Then pRecordset.Recordset.LockEdits = False
//'    End If
//'    IORefresh = CBool(Err = 0)
//'End Function
//'Public Function IOReDoRecordSet(pRecordset As Object, Optional pShowMsg As Variant) As Integer
//'    Dim xWork As ADODB.Recordset
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//'    If TypeOf pRecordset Is Data Then
//'        xWork = pRecordset.Recordset.OpenRecordset()
//'        pRecordset.Recordset = xWork
//'    ElseIf TypeOf pRecordset Is Recordset Then
//'        xWork = pRecordset.OpenRecordset()
//'        pRecordset = xWork
//'    End If
//'    IOReDoRecordSet = CBool(Err = 0)
//'    If Not Err = 0 Then
//'        If GetOptional(pShowMsg, False) Then
//'            IOReDoRecordSet = Not GetErro(Err, pRecordset.Name)
//'        End If
//'    End If
//'End Function
//'Public Function IODBCreate(pNome As String, Optional pShowMsg As Variant) As Boolean
//'    Dim xErr As Long
//'    xErr = Err
//'    On Error Resume Next
//'    pDB = DBEngine.Workspaces(0).CreateDatabase(pNome, dbLangGeneral, dbVersion25)
//'    IODBCreate = CBool(Err = 0)
//'    If Not Err = 0 Then
//'        If GetOptional(pShowMsg, False) Then
//'            IODBCreate = CBool(Not GetErro(Err, (pNome)))
//'        End If
//'    End If
//'    pDB.Close()
//'    IOFreeLock()
//'End Function
//'Public Sub FreeLock()
//'    'SEM USO
//'    IOFreeLock()
//'End Sub
//'Public Sub IOFreeLock()
//'    'VISTO
//'    'SEM USO
//'    'DBEngine.Idle dbFreeLocks
//'End Sub
//'Public Function IOSeek(ByVal pRecordset As ADODB.Recordset, ByVal pCampo As Object) As Boolean
//'    'If pCS Then
//'    '    pRecordset.findfist pCampo
//'    'Else
//'    '    pRecordset.Seek "=", pCampo
//'    'End If
//'End Function

//
//'Function GetDado(pDB As ADODB.Connection, ByVal pTabela As String, ByVal pCriterio As String, ByVal pCampo As String, Optional pForca As Variant) As Variant
//'    'VISTO
//'    ' Objetivo: Retorna o dado de uma coluna da tabela desejado, segundo o Critério informado.
//'    '
//'    ' Retorno da função:
//'    '     Quando OK     - Retorna Dado da Coluna
//'    '     Quando NÃO OK - Retorna vazio "" para colunas do tipo string
//'    '                     Retorna 0 (zero) para colunas do tipo numerico
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB          - Objeto da conexão;
//'    '     pTabela      - Nome da tabela;
//'    '     pCriterio    - Critério de seleção para a pesquisa do registro desejado;
//'    '     pCampo       - Nome da Coluna da tabela que se deseja receber o dado;
//'    '     pForca       - FALSE ou NÃO informado: indica que a função utulizar�� o conteudo
//'    '                    armazenado em mem��ria caso estaja sendo efetuado a mesma pesquisa que a anterior.
//'    '                  - TRUE: indica que a função efetuar�� uma nova pesquisa a tabela/coluna
//'    '                    para retorna o seu conte��do.
//'    '
//'    ' Sa���da...:
//'    '     pDB          - NÃO modificado;
//'    '     pTabela      - NÃO modificado;
//'    '     pCriterio    - NÃO modificado;
//'    '     pCampo       - NÃO modificado;
//'    '     pForca       - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xSnap As ADODB.Recordset
//'    Dim xSQL As String
//'    Static sDado As Object
//'    Static sSQL As String
//'    xSQL = "SELECT " & pCampo & " FROM " & pTabela & " "
//'    If pCriterio <> "" Then
//'        xSQL = xSQL + " WHERE " & pCriterio
//'    End If
//'    If Not IsMissing(pForca) Then
//'        If xSQL = sSQL Then
//'            If Not pForca Then
//'                GetDado = sDado
//'                Exit Function
//'            End If
//'        End If
//'    End If
//'    If IOOpenRecordSet(pDB, xSnap, xSQL, adOpenDynamic, , True) Then
//'        If xSnap.EOF Then
//'            If xSnap(pCampo).Type < vbString Then
//'                GetDado = 0
//'            Else
//'                GetDado = ""
//'            End If
//'        Else
//'            If IsNull(xSnap(pCampo)) Then
//'                If xSnap(pCampo).Type < vbString Then
//'                    GetDado = 0
//'                Else
//'                    GetDado = ""
//'                End If
//'            Else
//'                GetDado = xSnap(pCampo)
//'            End If
//'        End If
//'        IOClose(xSnap)
//'    End If
//'    sSQL = xSQL
//'    sDado = GetDado
//'End Function
//
//'Function GetServer(ByVal pDB As ADODB.Connection) As DBCnsServer
//'    'VISTO
//'    ' Objetivo: Retornar tipo de banco de dados utilizado(constante DBCNSSERVER)
//'    '
//'    ' Retorno da função:
//'    '     Retornar tipo de banco de dados utilizado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB                     - conexão com o banco de dados;
//'    '
//'    ' Sa���da...:
//'    '     pDB                     - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If UCase(Left(pDB.Properties("DBMS Name").Value, 3)) = "ORA" Then
//'        GetServer = DBCnsServer.DBOracle
//'    End If
//'End Function
//
//'Function GetSQLDateFind(ByVal pDate As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando formatado utiliza������o no Find para o tipo de dado DATA
//'    '
//'    ' Retorno da função:
//'    '     Quando OK     - String com data formatada;
//'    '     Quando NÃO OK - string contendo "''"
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDate        - Data a ser utilizada na String.
//'    '
//'    ' Sa���da...:
//'    '     pDate        - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If IsDate(pDate) Then
//'        GetSQLDateFind = "#" & DateValue(pDate) & "#"
//'    Else
//'        GetSQLDateFind = "''"
//'    End If
//'End Function
//
//'Function GetSQLDate(ByVal pDate As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para DATA conforme o tipo de Banco de Dados
//'    '
//'    ' Retorno da função:
//'    '     Quando OK     - String com data formatada;
//'    '     Quando NÃO OK - string contendo "''" para Oracle ou 0(zero) para outros bancos
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDate        - Data a ser utilizada na String.
//'    '
//'    ' Sa���da...:
//'    '     pDate        - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If gServer = DBOracle Then
//'        If IsDate(pDate) Then
//'            GetSQLDate = "To_Date('" & Format(pDate, "dd/mm/yyyy") & "','dd/mm/yyyy')"
//'        Else
//'            GetSQLDate = "''"
//'        End If
//'    Else
//'        If IsDate(pDate) Then
//'            GetSQLDate = "DateValue('" & pDate & "')"
//'        Else
//'            GetSQLDate = 0
//'        End If
//'    End If
//'End Function
//
//'Function GetSQLDateLong(ByVal pDate As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para DATA LONGA conforme o tipo de Banco de Dados
//'    '
//'    ' Retorno da função:
//'    '     Quando OK     - String com data formatada;
//'    '     Quando NÃO OK - string contendo "''" para Oracle ou 0(zero) para outros bancos
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDate        - Data a ser utilizada na String.
//'    '
//'    ' Sa���da...:
//'    '     pDate        - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If gServer = DBOracle Then
//'        If IsDate(pDate) Then
//'            GetSQLDateLong = "To_Date('" & Format(pDate, "dd/mm/yyyy hh:mm:ss") & "','dd/mm/yyyy hh24:mi:ss')"
//'        Else
//'            GetSQLDateLong = "''"
//'        End If
//'    Else
//'        If IsDate(pDate) Then
//'            GetSQLDateLong = "DateValue('" & pDate & "')"
//'        Else
//'            GetSQLDateLong = 0
//'        End If
//'    End If
//'End Function
//
//'Function GetSQLNumber(ByVal pNumber As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para NUMERO conforme o tipo de Banco de Dados
//'    '
//'    ' Retorno da função:
//'    '     Quando OK     - String com numero formatada;
//'    '     Quando NÃO OK - string contendo "''" para Oracle ou "0"(zero) para outros bancos
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pNumber       - Numero a ser utilizada na String.
//'    '
//'    ' Sa���da...:
//'    '     pNumber       - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If Asc(gDecimalPoint) = 0 Then
//'        GoSub ReadIni
//'    ElseIf gDecimalPoint = " " Then
//'        GoSub ReadIni
//'    ElseIf gDecimalPoint = "" Then
//'        GoSub ReadIni
//'    End If
//'    If gServer = DBOracle Then
//'        If IsNumeric(pNumber) Then
//'            If gDecimalPoint = "," Then
//'                GetSQLNumber = ChangeStr("To_Number('" & CDbl(pNumber) & "')", ".", gDecimalPoint)
//'            Else
//'                GetSQLNumber = ChangeStr("To_Number('" & CDbl(pNumber) & "')", ",", gDecimalPoint)
//'            End If
//'        Else
//'            GetSQLNumber = "''"
//'        End If
//'    Else
//'        If IsNumeric(pNumber) Then
//'            GetSQLNumber = "Cdbl('" & pNumber & "')"
//'        Else
//'            GetSQLNumber = "0"
//'        End If
//'    End If
//'    Exit Function
//'ReadIni:
//'    GetConfigFromInis()
//'    gDecimalPoint = GetNotEmpty(GetIniDBSoft("PARAMETRO", "PONTODECIMAL"), ",")
//'    Return
//'End Function
//
//'Function GetSQLIf(ByVal pSeCampo As Object, ByVal pIgualA As Object, ByVal pUsa As Object, ByVal pSeNao As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando IFF ou Decode formatado conforme o tipo de Banco de Dados
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pSeCampo      - Dado do Campo a ser testado;
//'    '     pIgualA       - Dado a ser comparado ao campo pSeCampo;
//'    '     pUsa          - Dado a ser considerado quando pSeCampo for Igual a pIgualA;
//'    '     pSeNao        - Dado a ser considerado quando pSeCampo for Diferente a pIgualA.
//'    '
//'    ' Sa���da...:
//'    '     pSeCampo      - NÃO modificado;
//'    '     pIgualA       - NÃO modificado;
//'    '     pUsa          - NÃO modificado;
//'    '     pSeNao        - NÃO modificado;
//'    '     pNumber       - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If gServer = DBOracle Then
//'        GetSQLIf = "Decode(" & pSeCampo & "," & pIgualA & "," & pUsa & "," & pSeNao & ")"
//'    Else
//'        GetSQLIf = "iif(" & pSeCampo & "=" & pIgualA & "," & pUsa & "," & pSeNao & ")"
//'    End If
//'End Function
//
//'Function GetSQLBoolean(ByVal pBoolean As Boolean) As Integer
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para dado tipo BOLEANO conforme o tipo de Banco de Dados
//'    '
//'    ' Retorno da função:
//'    '     -1           - True;
//'    '      0           - False.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pBoolean     - Dado a ser testado.
//'    '
//'    ' Sa���da...:
//'    '     pBoolean     - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If pBoolean Then
//'        GetSQLBoolean = -1
//'    Else
//'        GetSQLBoolean = 0
//'    End If
//'End Function
//
//
//'Function GetSQLNull(ByVal pFieldName As String) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para dados Nulos conforme o tipo de Banco de Dados
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If gServer = DBOracle Then
//'        GetSQLNull = pFieldName & " is null "
//'    Else
//'        GetSQLNull = " isNull(" & pFieldName & ")"
//'    End If
//
//'End Function
//
//'Function GetSQLDateOrNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pDate As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para campo do tipo data com a seguinte constru����o:
//'    '           (Nome do campo Tipo Date) (Condi����o <> = > <) (Dado) or (Nome do Campos Tipo Date= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pDate        - Dado a ser comparado.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pDate        - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If IsDate(pDate) Then
//'        GetSQLDateOrNull = pFieldName & " " & pCondicao & " " & GetSQLDate(pDate) & " or "
//'    End If
//'    GetSQLDateOrNull = GetSQLDateOrNull & GetSQLNull(pFieldName)
//'End Function
//'Function GetSQLDateLongOrNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pDate As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para campo do tipo Date Long com a seguinte constru����o:
//'    '           (Nome do campo Tipo Date Long) (Condi����o <> = > <) (Dado) or (Nome do Campos Tipo Date Long= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pDate        - Dado a ser comparado.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pDate        - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If IsDate(pDate) Then
//'        GetSQLDateLongOrNull = pFieldName & " " & pCondicao & " " & GetSQLDateLong(pDate) & " or "
//'    End If
//'    GetSQLDateLongOrNull = GetSQLDateLongOrNull & GetSQLNull(pFieldName)
//'End Function
//
//
//'Function GetSQLNumberOrNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pNumber As Object) As String
//'    '****Verificar chamada a SetFieldValue do tipo TyneNumber com pOrNull = True
//'    ' Objetivo: Retorna String com comando SQL formatado para campo do tipo number com a seguinte constru����o:
//'    '           (Nome do campo Tipo number) (Condi����o <> = > <) (Dado) or (Nome do Campos Tipo number= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pNumber      - Dado a ser comparado.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pNumber      - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If IsNumeric(pNumber) Then
//'        GetSQLNumberOrNull = pFieldName & " " & pCondicao & " " & GetSQLNumber(pDate) & " or "
//'    End If
//'    GetSQLNumberOrNull = GetSQLNumberOrNull & GetSQLNull(pFieldName)
//'End Function
//
//'Function GetSQLFieldOrNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pFieldValue As Object) As String
//'    ' Objetivo: Retorna String com comando SQL formatado para campo generico com a seguinte constru����o:
//'    '           (Nome do campo) (Condi����o <> = > <) (Dado) or (Nome do Campos= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pFieldValue  - Valor do Campo.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pFieldValue  - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If GetNotNull(pFieldValue, 0) <> 0 Then
//'        GetSQLFieldOrNull = pFieldName & " " & pCondicao & " " & pFieldValue & " or "
//'    End If
//'    GetSQLFieldOrNull = GetSQLFieldOrNull & GetSQLNull(pFieldName)
//'End Function
//
//'Function GetSQLFieldNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pValue As Object) As String
//'    ' Objetivo: Retorna String com comando SQL formatado para campo generico com a seguinte constru����o:
//'    '           Quando pValue NÃO for nulo:  (Nome do campo) (Condi����o <> = > <) (Dado)
//'    '           Quando pValue for nulo    :  (Nome do Campos= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pValue       - Valor do Campo.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pValue       - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If GetNotNull(pValue, "") = "" Then
//'        GetSQLFieldNull = GetSQLNull(pFieldName)
//'    Else
//'        GetSQLFieldNull = pFieldName & pCondicao & pValue
//'    End If
//'End Function
//
//'Function GetSQLStringOrNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pFieldValue As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para campo do tipo String com a seguinte constru����o:
//'    '           (Nome do campo Tipo String) (Condi����o <> = > <) (Dado) or (Nome do Campos Tipo String= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pFieldValue  - Dado a ser comparado.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pFieldValue  - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If GetNotNull(pFieldValue, 0) <> 0 Then
//'        GetSQLStringOrNull = pFieldName & " " & pCondicao & " '" & Trim(pFieldValue) & "' or "
//'    End If
//'    GetSQLStringOrNull = GetSQLStringOrNull & GetSQLNull(pFieldName)
//'End Function
//
//'Function GetSQLNullFind(ByVal pFieldName As String) As String
//'    GetSQLNullFind = pFieldName & "=NULL"
//'End Function
//
//
//'Function GetSQLStringNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pValue As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para campo String com a seguinte constru����o:
//'    '           Quando pValue NÃO for nulo:  (Nome do campo) (Condi����o <> = > <) (Dado)
//'    '           Quando pValue for nulo    :  (Nome do Campos= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pValue       - Valor do Campo.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pValue       - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If GetNotNull(pValue, "") = "" Then
//'        GetSQLStringNull = GetSQLNull(pFieldName)
//'    Else
//'        GetSQLStringNull = pFieldName & pCondicao & "'" & Trim(pValue) & "'"
//'    End If
//'End Function
//
//'Function GetSQLDateNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pDate As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para campo Date com a seguinte constru����o:
//'    '           Quando pDate for v���lida     :  (Nome do campo) (Condi����o <> = > <) (Dado)
//'    '           Quando pDate NÃO for v���lida :  (Nome do Campos= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pDate        - Data.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pDate        - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If IsDate(pDate) Then
//'        GetSQLDateNull = pFieldName & " " & pCondicao & " " & GetSQLDate(pDate)
//'    Else
//'        GetSQLDateNull = GetSQLNull(pFieldName)
//'    End If
//'End Function
//
//'Function GetSQLDateLongNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pDate As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para campo DateLong com a seguinte constru����o:
//'    '           Quando pDate for v���lida     :  (Nome do campo) (Condi����o <> = > <) (Dado)
//'    '           Quando pDate NÃO for v���lida :  (Nome do Campos= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pDate        - Data.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pDate        - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If IsDate(pDate) Then
//'        GetSQLDateLongNull = pFieldName & " " & pCondicao & " " & GetSQLDateLong(pDate)
//'    Else
//'        GetSQLDateLongNull = GetSQLNull(pFieldName)
//'    End If
//'End Function
//
//'Function GetSQLNumberNull(ByVal pFieldName As String, ByVal pCondicao As String, ByVal pNumber As Object) As String
//'    'VISTO
//'    ' Objetivo: Retorna String com comando SQL formatado para campo Number com a seguinte constru����o:
//'    '           Quando pNumber or v���lida     :  (Nome do campo) (Condi����o <> = > <) (Dado)
//'    '           Quando pNumber NÃO for v���lida :  (Nome do Campos= Nulo)
//'    '
//'    ' Retorno da função:
//'    '     String com comando formatado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pFieldName   - Nome do campo a ser considerado no comando;
//'    '     pCondicao    - Operador da Condi����o (> < = <>) a ser testada;
//'    '     pDate        - Data.
//'    '
//'    ' Sa���da...:
//'    '     pFieldName   - NÃO modificado;
//'    '     pCondicao    - NÃO modificado;
//'    '     pDate        - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    If IsNumeric(pNumber) Then
//'        GetSQLNumberNull = pFieldName & " " & pCondicao & " " & GetSQLNumber(pNumber)
//'    Else
//'        GetSQLNumberNull = GetSQLNull(pFieldName)
//'    End If
//'End Function
//
//
//'Function SetFieldValue(pIOType As DBCnsIOType, pDataType As DBCnsDataType, pValue As Variant, Optional pFieldName As String, Optional pCondicao As String, Optional pOrNull As Variant) As Variant
//'    'VISTO - Verificar pendencias
//'    ' Objetivo: Retorna valor da coluna ajustada conforme o procedimento solicitado
//'    '
//'    ' Retorno da função:
//'    '     Varlor da coluna ajustado;
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pIOType       - Tipo de procedimento conforme constante DBCnsIOType;
//'    '                     Tipo "TypeSQLInsertOrUpdate" sem usu atualmente.
//'    '     pDataType     - Tipo de dado da coluna conforme constante DBCnsDataType;
//'    '     pValue        - Valor da coluna;
//'    '     pFieldName    - Nome da Coluna da tabela
//'    '                     * somente usado no tipo TypeSQLSelect;
//'    '     pCondicao     - Operador da Condi����o (> < = <>) a ser testada
//'    '                     * somente usado no tipo TypeSQLSelect;
//'    '     pOrNull       - Indica se serãoincluido a Condi����o de nulo
//'    '                     al��m da Condi����o do parametro pCondicao
//'    '                     * somente usado no tipo TypeSQLSelect;
//'    '
//'    ' Sa���da...:
//'    '     pIOType       - NÃO modificado;
//'    '     pDataType     - NÃO modificado;
//'    '     pValue        - NÃO modificado;
//'    '     pFieldName    - NÃO modificado;
//'    '     pCondicao     - NÃO modificado;
//'    '     pOrNull       - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    'Tipo "TypeSQLInsertOrUpdate" sem usu atualmente
//'    If pIOType = TypeSQLInsertOrUpdate Then
//'        If pDataType = TypeDate Then
//'            SetFieldValue = GetSQLDate(pValue)
//'        ElseIf pDataType = TypeDateLong Then
//'            SetFieldValue = GetSQLDateLong(pValue)
//'        ElseIf pDataType = TypeNumber Then
//'            SetFieldValue = GetSQLNumber(pValue)
//'        ElseIf pDataType = TypeID Then
//'            SetFieldValue = pValue
//'        ElseIf pDataType = TypeBoolean Then
//'            SetFieldValue = pValue
//'        End If
//'        '------------------------------------------------------------------
//'    ElseIf pIOType = TypeSQLSelect Then
//'        If pDataType = TypeDate Then
//'            If pOrNull Then
//'                SetFieldValue = GetSQLDateOrNull(pFieldName, pCondicao, pValue)
//'            Else
//'                SetFieldValue = GetSQLDateNull(pFieldName, pCondicao, pValue)
//'            End If
//'        ElseIf pDataType = TypeDateLong Then
//'            If pOrNull Then
//'                SetFieldValue = GetSQLDateLongOrNull(pFieldName, pCondicao, pValue)
//'            Else
//'                'Verificar chamada a GetSQLDateNull ao inv���s da GetSQLDataLongNull
//'                SetFieldValue = GetSQLDateNull(pFieldName, pCondicao, pValue)
//'            End If
//'        ElseIf pDataType = TypeNumber Then
//'            If pOrNull Then
//'                'Verificar chamada a SetFieldValue do tipo TyneNumber com pOrNull = True
//'                SetFieldValue = GetSQLNumberOrNull(pFieldName, pCondicao, pValue)
//'            Else
//'                SetFieldValue = GetSQLNumberNull(pFieldName, pCondicao, pValue)
//'            End If
//'        ElseIf pDataType = TypeID Then
//'            If pValue = 0 Or pValue = -1 Then
//'                SetFieldValue = GetSQLNull(pFieldName)
//'            Else
//'                SetFieldValue = pFieldName & " " & pCondicao & " " & pValue
//'            End If
//'        ElseIf pDataType = TypeString Then
//'            If pOrNull Then
//'                SetFieldValue = GetSQLStringOrNull(pFieldName, pCondicao, pValue)
//'            Else
//'                SetFieldValue = GetSQLStringNull(pFieldName, pCondicao, pValue)
//'            End If
//'        ElseIf pDataType = TypeBoolean Then
//'            'Verificar chamada a SetFieldValue do tipo Tyneboolean
//'            SetFieldValue = pFieldName & " " & pCondicao & " " & pValue
//'        End If
//'        '------------------------------------------------------------------
//'    ElseIf pIOType = TypeIOMove Then
//'        If gServer = DBOracle Then
//'            If pDataType = TypeDate Then
//'                SetFieldValue = GetNotEmpty(pValue, Null)
//'            ElseIf pDataType = TypeDateLong Then
//'                SetFieldValue = GetNotEmpty(pValue, Null)
//'            ElseIf pDataType = TypeNumber Then
//'                SetFieldValue = GetNotEmpty(pValue, Null)
//'            ElseIf pDataType = TypeID Then
//'                If pValue = 0 Or pValue = -1 Then
//'                    SetFieldValue = Null
//'                Else
//'                    SetFieldValue = GetNotNull(pValue, Null)
//'                End If
//'            Else
//'                SetFieldValue = GetNotNull(pValue, Null)
//'            End If
//'        Else
//'            SetFieldValue = GetNotNull(pValue, "")
//'        End If
//'    End If
//'End Function
//
//'Function GetTmpId() As String
//'    GetTmpId = Trim(Format(gUsuarioID, "000000")) & Format(Time, "hhmmss") & Format(Rnd(Time) * 1000000, "000000")
//'End Function
//
//'Function GetID(ByVal pDB As ADODB.Connection, ByVal pIdName As String) As Long
//'    'VISTO
//'    ' Objetivo: Retorna numero da Sequence que acabou de ser criada
//'    '
//'    ' Retorno da função:
//'    '     Quando OK     - Valor da sequence;
//'    '     Quando NÃO OK - 0 (Zero).
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB          - conexão com o Banco da dado;
//'    '     pIdName      - Nome da Sequence.
//'    '
//'    ' Sa���da...:
//'    '     pDB          - NÃO modificado;
//'    '     pIdName      - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xTeste As ADODB.Recordset
//'    Dim SQL As String
//'    If gServer = DBOracle Then
//'        SQL = "Select " & pIdName & ".currval From Dual"
//'        If IOOpenRecordSet(pDB, xTeste, SQL, adOpenStatic, , True) Then
//'            GetID = xTeste("currval").Value
//'            IOClose(xTeste)
//'        Else
//'            GetID = -1
//'            IOClose(xTeste)
//'        End If
//'    End If
//'    If Err <> 0 Then
//'        GetID = -1
//'        If Err = 3045 Then
//'            ShowMsg(ErroOk, "Sequence " & UCase(pIdName) & " NÃO foi encontrado!")
//'        Else
//'            GetErro(Err, pIdName)
//'        End If
//'    End If
//'End Function
//
//'Function GetNextSequence(ByVal pDB As ADODB.Connection, ByVal pIdName As String) As Long
//'    ' Objetivo: Retorna numero da proxima Sequence
//'    '
//'    ' Retorno da função:
//'    '     Quando OK     - Valor da sequence;
//'    '     Quando NÃO OK - 0 (Zero).
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB          - conexão com o Banco da dado;
//'    '     pIdName      - Nome da Sequence.
//'    '
//'    ' Sa���da...:
//'    '     pDB          - NÃO modificado;
//'    '     pIdName      - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xTeste As ADODB.Recordset
//'    Dim SQL As String
//'    If gServer = DBOracle Then
//'        SQL = "Select " & pIdName & ".nextval From Dual"
//'        If IOOpenRecordSet(pDB, xTeste, SQL, adOpenStatic, , True) Then
//'            GetNextSequence = xTeste("nextval").Value
//'            IOClose(xTeste)
//'        Else
//'            GetNextSequence = -1
//'            IOClose(xTeste)
//'        End If
//'    End If
//'    If Err <> 0 Then
//'        GetNextSequence = -1
//'        If Err = 3045 Then
//'            ShowMsg(ErroOk, "Sequence " & UCase(pIdName) & " NÃO foi encontrado!")
//'        Else
//'            GetErro(Err, pIdName)
//'        End If
//'    End If
//'End Function
//
//
//'Function GetServerDate(ByVal pDB As ADODB.Connection) As Object
//'    'VISTO
//'    ' Objetivo: Retorna data atual segundo o Banco de dados
//'    '
//'    ' Retorno da função:
//'    '     Quando OK     - Data Atual;
//'    '     Quando NÃO OK - vazio ("").
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB           - conexão com o Banco da dado.
//'    '
//'    ' Sa���da...:
//'    '     pDB           - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xTeste As ADODB.Recordset
//'    Dim SQL As String
//'    If gServer = DBOracle Then
//'        SQL = "Select SYSDATE From Dual"
//'        If IOOpenRecordSet(pDB, xTeste, SQL, adOpenStatic) Then
//'            GetServerDate = xTeste("SYSDATE").Value
//'            IOClose(xTeste)
//'        End If
//'    End If
//'End Function
//
//'Function GetRecordCount(ByVal pDB As ADODB.Connection, ByVal pTableName As String, ByVal pWhere As String) As Integer
//'    'VISTO
//'    ' Objetivo: Retorna Quantidade de Registros de uma tabela a partir do filtro informado.
//'    '
//'    ' Retorno da função:
//'    '     Quando OK     - Quantidade de Registros;
//'    '     Quando NÃO OK - 0 (Zero).
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB          - conexão com o Banco da dado;
//'    '     pTableName   - Nome da Tabela a ser pesquisada;
//'    '     pWhere       - Filtro a ser utilizado na pesquisa.
//'    '
//'    ' Sa���da...:
//'    '     pDB          - NÃO modificado;
//'    '     pTableName   - NÃO modificado;
//'    '     pWhere       - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xSnap As ADODB.Recordset
//'    Dim xSQL As String
//'    xSQL = "Select Count(*) as Tot From " & pTableName
//'    If pWhere <> "" Then
//'        xSQL = xSQL & " Where " & pWhere
//'    End If
//'    If IOOpenRecordSet(pDB, xSnap, xSQL, adOpenStatic, , True) Then
//'        GetRecordCount = GetNotNull(xSnap("TOT"), 0)
//'        IOClose(xSnap)
//'    Else
//'        'GetErro Err, "GetRecordCount"
//'        GetRecordCount = 0
//'    End If
//'End Function
//
//'Function GetRecordMax(ByVal pDB As ADODB.Connection, ByVal pTableName As String, ByVal pWhere As String, ByVal pCampo As String) As Integer
//'    Dim xSnap As ADODB.Recordset
//'    Dim xSQL As String
//'    xSQL = "Select Max(" & pCampo & ") as sMax From " & pTableName & " Where " & pWhere
//'    If IOOpenRecordSet(pDB, xSnap, xSQL, adOpenStatic, , True) Then
//'        GetRecordMax = GetNotNull(xSnap("sMax"), 0)
//'        IOClose(xSnap)
//'    Else
//'        'GetErro Err, "GetRecordCount"
//'        GetRecordMax = 0
//'    End If
//'End Function
//
//'Function IOBookmark(ByVal pRecordset As ADODB.Recordset, ByVal pBookmark As Object, ByVal pShowError As Boolean) As Boolean
//'    On Error Resume Next 'Para evitar que de erro qndeste registro tiver sido excluido por outra se������o
//'    pRecordset.Bookmark = pBookmark
//'    If pShowError Then
//'        IOBookmark = Not GetErro(Err)
//'    Else
//'        If Err = 0 Then
//'            IOBookmark = True
//'        Else
//'            IOBookmark = False
//'        End If
//'    End If
//'End Function
//
//'Function IOLock(pDB As ADODB.Connection, pTableName As String, pWherePK As String, Optional pFieldName As Variant, Optional pShowMsg As Variant) As Boolean
//'    Dim xSQL As DBSSQL
//'    '    If gServer = DBOracle Then
//'    '    Set xSQL = New DBSSQL
//'    '    With xSQL
//'    '        Set .DBConnection = pDB
//'    '        .DBTableName = pTableName
//'    '        .DBWhere = pWhere
//'    '        .DBMode = SQLLock
//'    '        .DBShowMsg = False
//'    '        If Not IsMissing(pFieldName) Then
//'    '            .AddField (pFieldName), 0, TypeID, False
//'    '        End If
//'    '        If .DBExecute(False) Then
//'    '            IOIsLocked = True
//'    '        Else
//'    '            ShowMsg AtencaoOk, "Registro em Uso!"
//'    '            IOIsLocked = True
//'    '        End If
//'    '        .DBClose True
//'    '    End With
//'    '    End If
//'End Function
//
//'Function IOUnLock(pDB As ADODB.Connection, pTableName As String, pWherePK As String, Optional pFieldName As Variant, Optional pShowMsg As Variant) As Boolean
//'    'Dim xSQL    As DBSSQL
//'    '    If gServer = DBOracle Then
//'    '    Set xSQL = New DBSSQL
//'    '    With xSQL
//'    '        Set .DBConnection = pDB
//'    '        .DBTableName = pTableName
//'    '        .DBWhere = pWherePK
//'    '        .DBMode = SQLLock
//'    '        .DBShowMsg = False
//'    '        If Not IsMissing(pFieldName) Then
//'    '            .AddField (pFieldName), 0, TypeID, False
//'    '        End If
//'    '        If .DBExecute(False) Then
//'    '            IOIsLocked = False
//'    '        Else
//'    '            ShowMsg AtencaoOk, "Registro em Uso!"
//'    '            IOIsLocked = True
//'    '        End If
//'    '        .DBClose True
//'    '    End With
//'    '    End If
//'End Function
//
//'Function IOIsLocked(pDB As ADODB.Connection, pTableName As String, pWherePK As String, Optional pFieldName As Variant, Optional pShowMsg As Variant) As Boolean
//'    ' Objetivo: Retorna se determinados registros estãoo bloqueados por outra conexão.
//'    '
//'    ' Retorno da função:
//'    '     True          - estãoo bloqueados;
//'    '     False         - estãoo liberados.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB          - conexão com o Banco da dado;
//'    '     pTableName   - Nome da tabela que serãoefetuado a pesquisa;
//'    '     pWherePK     - Condi����o que serãoutilizada para selecionar os registros que se deseja verificar;
//'    '     pFieldName   - Nome de qualquer um dos campos da tabela.
//'    '
//'    ' Sa���da...:
//'    '     pDB          - NÃO modificado;
//'    '     pTableName   - NÃO modificado;
//'    '     pWherePK     - NÃO modificado;
//'    '     pFieldName   - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xSQL As DBSSQL
//'    Dim xDB As ADODB.Connection
//'    'Set xDB = New Connection
//'    'If Not IOOpenConnection(xDB, "", "", "", gDB.ConnectionString) Then
//'    '    Exit Function
//'    'End If
//'    'IOBeginTrans xDB
//'    xSQL = New DBSSQL
//'    With xSQL
//'        PreencheSQL(xSQL)
//'        .DBTableName = pTableName
//'        .DBWhere = pWherePK
//'        .DBMode = SQLLock
//'        .DBShowMsg = False
//'        If Not IsMissing(pFieldName) Then
//'            .AddField (pFieldName), 0, TypeID, False
//'        End If
//'        If .DBExecute(False) Then
//'            IOIsLocked = False
//'        Else
//'            ShowMsg(AtencaoOk, "Registro em Uso!")
//'            IOIsLocked = True
//'        End If
//'        .DBClose(True)
//'    End With
//'    'IORollBack xDB
//'    'IOClose xDB
//'End Function
//
//'Public Sub MoveAuditoria(ByVal pDB As ADODB.Connection, ByVal pDBSSQLObject As Object, ByVal pModo As Integer)
//'    'VISTO
//'    ' Objetivo: Move Campos padrão relacionados a auditoria(DBSOFT) para o objeto DBSSQL informado
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB           - conexão do Banco de Dados;
//'    '     pDBSSQLObject - Objeto DBSSQL;
//'    '     pModo         - Modo que estão sendo efetuado conforme constante em DBCNSMODO.
//'    '
//'    ' Sa���da...:
//'    '     pDB           - NÃO modificado;
//'    '     pDBSSQLObject - Incorporado colunas de auditoria(DBSOFT) com seus respectivos valores.
//'    '     pModo         - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xDate As Object
//'    xDate = GetServerDate(pDB)
//'    If pModo = DBCnsModo.Inclusao Then
//'        pDBSSQLObject.AddField("INCLUSAO_DATA", xDate, TypeDateLong)
//'    End If
//'    pDBSSQLObject.AddField("ALTERACAO_USUARIO_ID", gUsuarioID, TypeNumber, False)
//'    pDBSSQLObject.AddField("ALTERACAO_DATA", xDate, TypeDateLong, False)
//'End Sub
//
//'Public Function IOFindFirst(pRecordset As ADODB.Recordset, pWhere As String, Optional pNeedRequery As Variant) As Boolean
//'    Dim xSnap As ADODB.Recordset
//'    On Error Resume Next
//
//'    xSnap = pRecordset.Clone()
//'    If Not IsMissing(pNeedRequery) Then
//'        If pNeedRequery Then
//'            xSnap.Requery() 'Incluido em 4/09/2000- O filter esta falhando em alguma situa������es on h�� campos com valor ou data
//'        End If
//'    End If
//'    xSnap.Filter = pWhere
//'    If xSnap.EOF Or Not Err = 0 Then
//'        IOFindFirst = False
//'    Else
//'        IOFindFirst = True
//'        pRecordset.Bookmark = xSnap.Bookmark
//'    End If
//'    IOClose(xSnap)
//'End Function
//
//'Public Function IOGetSQLPkValue(ByVal pRecordset As ADODB.Recordset, ByVal pPkName As String) As String
//'    Dim xCampo() As String
//'    On Error Resume Next
//'    If pRecordset.EOF Then
//'        IOGetSQLPkValue = ""
//'        Exit Function
//'    End If
//'    BreakStringIntoArray(pPkName, xCampo(), ",")
//'    IOGetSQLPkValue = ""
//'    For x = 0 To UBound(xCampo)
//'        If Not GetNotEmpty(xCampo(x), "") = "" Then
//'            If Not IOGetSQLPkValue = "" Then IOGetSQLPkValue = IOGetSQLPkValue & " and "
//'            If pRecordset(xCampo(x)).Type = adChar Or pRecordset(xCampo(x)).Type = adVarChar Then
//'                IOGetSQLPkValue = IOGetSQLPkValue & xCampo(x) & "='" & pRecordset(xCampo(x)) & "'"
//'            ElseIf pRecordset(xCampo(x)).Type = adDBDate Or pRecordset(xCampo(x)).Type = adDBTimeStamp Then
//'                IOGetSQLPkValue = IOGetSQLPkValue & xCampo(x) & "=" & GetSQLDate(pRecordset(xCampo(x))) & " "
//'            ElseIf pRecordset(xCampo(x)).Type = adDouble Or pRecordset(xCampo(x)).Type = adNumeric Or pRecordset(xCampo(x)).Type = adVarNumeric Then
//'                IOGetSQLPkValue = IOGetSQLPkValue & xCampo(x) & "=" & GetSQLNumber(pRecordset(xCampo(x))) & " "
//'            Else
//'                IOGetSQLPkValue = IOGetSQLPkValue & xCampo(x) & "=" & pRecordset(xCampo(x))
//'            End If
//'        End If
//'    Next
//'    GetErro(Err, pPkName)
//'End Function
//
//'Public Function IOGetPkValue(pRecordset As ADODB.Recordset, pPkName As String, Optional pNeedRequery As Variant) As String
//'    Dim xCampo() As String
//'    Dim xCount As Object
//'    Dim xNeedRequery As Boolean
//'    On Error GoTo 0
//'    '    On Error Resume Next
//'    Err.Clear()
//'    If Trim(pPkName) = "" Or pRecordset.EOF Then
//'        IOGetPkValue = ""
//'        Exit Function
//'    End If
//'    BreakStringIntoArray(pPkName, xCampo(), ",")
//'    IOGetPkValue = ""
//'    For x = 0 To UBound(xCampo)
//'        If Not IOGetPkValue = "" Then IOGetPkValue = IOGetPkValue & " AND "
//'        If IsNull(pRecordset(xCampo(x))) Then
//'            IOGetPkValue = IOGetPkValue & GetSQLNullFind(xCampo(x))
//'        Else
//'            If pRecordset(xCampo(x)).Type = adChar Or pRecordset(xCampo(x)).Type = adVarChar Then
//'                IOGetPkValue = IOGetPkValue & xCampo(x) & " = '" & pRecordset(xCampo(x)) & "'"
//'            ElseIf pRecordset(xCampo(x)).Type = adDBDate Or pRecordset(xCampo(x)).Type = adDBTimeStamp Then
//'                IOGetPkValue = IOGetPkValue & xCampo(x) & " = " & GetSQLDateFind(pRecordset(xCampo(x))) '& "#"
//'                xNeedRequery = True
//'            ElseIf pRecordset(xCampo(x)).Type = adNumeric Or pRecordset(xCampo(x)).Type = adDouble Or pRecordset(xCampo(x)).Type = adVarNumeric Or pRecordset(xCampo(x)).Type = adInteger Then
//'                IOGetPkValue = IOGetPkValue & xCampo(x) & " = " & ChangeStr(pRecordset(xCampo(x)), ",", ".", xCount) & " "
//'                If xCount > 0 Then
//'                    xNeedRequery = True
//'                End If
//'            Else
//'                IOGetPkValue = IOGetPkValue & xCampo(x) & " = " & pRecordset(xCampo(x))
//'            End If
//'        End If
//'    Next
//'    If Not IsMissing(pNeedRequery) Then
//'        pNeedRequery = xNeedRequery
//'    End If
//'    GetErro(Err, pPkName)
//'End Function
//
//'Function IOCreateParameter(pCmd As Command, pName As String, pDataType As DBCnsDataType, pDirection As ParameterDirectionEnum, Optional pSize As Variant, Optional pValue As Variant)
//'    Dim xParameter As New Parameter
//'    'pDirection = adParamInputOutput
//'    With pCmd
//'        If pDataType = TypeDate Or pDataType = TypeDateLong Then
//'            If IsMissing(pSize) Then
//'                xParameter = .CreateParameter(pName, adDate, pDirection, , pValue)
//'            Else
//'                xParameter = .CreateParameter(pName, adDate, pDirection, pSize, pValue)
//'            End If
//'        ElseIf pDataType = TypeID Or pDataType = TypeBoolean Then
//'            If IsMissing(pSize) Then
//'                xParameter = .CreateParameter(pName, adDouble, pDirection, , pValue)
//'            Else
//'                xParameter = .CreateParameter(pName, adDouble, pDirection, pSize, pValue)
//'            End If
//'        ElseIf pDataType = TypeNumber Then
//'            If IsMissing(pSize) Then
//'                xParameter = .CreateParameter(pName, adDouble, pDirection, , pValue)
//'            Else
//'                xParameter = .CreateParameter(pName, adDouble, pDirection, pSize, pValue)
//'            End If
//'        ElseIf pDataType = TypeString Then
//'            If IsMissing(pSize) Then
//'                xParameter = .CreateParameter(pName, adVarChar, pDirection, , pValue)
//'            Else
//'                xParameter = .CreateParameter(pName, adVarChar, pDirection, pSize, pValue)
//'            End If
//'        End If
//'        pCmd.Parameters.Append(xParameter)
//'    End With
//'End Function
//
//'Public Function IOLog(ByVal pDB As Connection, ByVal pExeName As String, ByVal pMode As DBCnsModo, ByVal pSQL As String) As Boolean
//'    'VISTO
//'    ' Objetivo: Grava string com systax SQL na tabela AC_LOG.
//'    '
//'    ' Retorno da função:
//'    '     True          - NÃO houve erro;
//'    '     False         - Houve Erro.
//'    '
//'    ' Par���metros:
//'    ' Entrada.:
//'    '     pDB          - conexão com o Banco da dado;
//'    '     pExeName     - Nome do Execut���vel da aplica����o;
//'    '     pMode        - Modo segundo constante definida em DBCnsModo;
//'    '     pSQL         - String com o comando SQL que esta sendo efetuado.
//'    '
//'    ' Sa���da...:
//'    '     pDB          - NÃO modificado;
//'    '     pExeName     - NÃO modificado;
//'    '     pMode        - NÃO modificado;
//'    '     pSQL         - NÃO modificado.
//'    '-------------------------------------------------------------------------------
//'    Dim xSQL As String
//'    Dim ySQL As String
//'    On Error Resume Next
//'    xSQL = ChangeStr(pSQL, "'", "::")
//'    xSQL = UCase(Left(xSQL, 2000))
//'    xSQL = ChangeStr(xSQL, "  ", " ")
//'    xSQL = ChangeStr(xSQL, " = ", "=")
//'    'Verificar a possibilidade de substituir o comando "GetSQLDateLong(GetServerDate(pDB))" por SYSDATE
//'    ySQL = "INSERT INTO AC_LOG (EXENAME,DATA,MODO,USUARIO,SQL,ORIGEM) " & _
//'                        "VALUES('" & pExeName & "'," & GetSQLDateLong(GetServerDate(pDB)) & "," & pMode & ",'" & Left(pDB.Properties("USER ID").Value, 20) & "','" & xSQL & "','" & gLogOrigem & "')"
//'    IOLog = IOExecute(pDB, (ySQL), , True)
//'End Function
//
//''Public Function SetupIO(pDB As ADODB.Connection, ByVal pTabelaSetup As String, ByVal pCampo As String, Optional pDado As Variant, Optional pWRITE As Variant) As Variant
//''Dim xTable As ADODB.Recordset
//''Dim xErr    As Long
//''    xErr = Err
//''    On Error Resume Next
//''    If Not IOOpenRecordSet(pDB, xTable, "Select * From " & pTableSetup, adOpenKeyset) Then
//''        SetupIO = ""
//''    Else
//''        xTable.MoveFirst
//''        SetupIO = xTable(pCampo)
//''        If Not IsMissing(pWRITE) And Not IsMissing(pDado) Then
//''            If pWRITE Then
//''                If IOEdit(xTable) Then
//''                    xTable(pCampo) = pDado
//''                    xOk = IOUpdate(xTable)
//''                End If
//''            End If
//''        End If
//''        IOClose xTable
//''    End If
//''    If (Not Err = 0 And xOk) Then SetupIO = ""
//''    'If GetErro(Err, pDado) Then SetupIO = ""
//''End Function
//''Public Function IsVersaoOk(pDB As ADODB.Connection, pVersao As Variant, Optional pVerOld As Variant) As Variant
//''Dim x As Variant
//''    'Verifica Vers���o do Banco de Dados Access
//''    If pVersao = 0 Then
//''        IsVersaoOk = True
//''        Exit Function
//''    End If
//''    x = SetupIO(pDB, gFieldVersao, "", False)
//''    IsVersaoOk = CBool(Val(x) >= Val(pVersao))
//''    If Not IsMissing(pVerOld) Then pVerOld = Val(x)
//''End Function
//
//
//''---------------------------------------------------------
//''Function IOQueryCreate(pDB As adodb.Connection, pName As String, pSQL As String) As Boolean
//''Dim xLixo   As QueryDef
//''    On Error Resume Next
//''    Set xLixo = pDB.CreateQueryDef(pName, pSQL)
//''    If Err = 3012 Then
//''        Err.Clear
//''        pDB.DeleteQueryDef pName
//''        Set xLixo = pDB.CreateQueryDef(pName, pSQL)
//''    End If
//''    IOQueryCreate = Not GetErro(Err)
//''End Function
//
//''Public Function IOTableCreate(pDB As adodb.Connection, pTableDef As TableDef, pNome As String, Optional pAtribute As Variant, Optional pShowMsg As Variant) As Boolean
//''Dim xAtribute   As Long
//''    On Error Resume Next
//''    If Not IsMissing(pAtribute) Then xAtribute = pAtribute Else: xAtribute = dbAttachedTable
//''    Set pTableDef = pDB.CreateTableDef(pNome, xAtribute)
//''    IOTableCreate = CBool(Err = 0)
//''    If Not Err = 0 Then
//''        If IsMissing(pShowMsg) Then
//''            IOTableCreate = CBool(Not GetErro(Err, (pNome)))
//''        End If
//''    End If
//''    IOFreeLock
//''End Function
//''Public Function IOTableCreateAppend(pDB As adodb.Connection, pTableDef As TableDef, Optional pShowMsg As Variant) As Boolean
//''    On Error Resume Next
//''    pDB.TableDefs.Append pTableDef
//''    IOTableCreateAppend = CBool(Err = 0)
//''    If Not Err = 0 Then
//''        If IsMissing(pShowMsg) Then
//''            IOTableCreateAppend = CBool(Not GetErro(Err, (pNome)))
//''        End If
//''    End If
//''End Function
//''Public Function IOFieldCreate(pTableDef As TableDef, pNome As String, pTipo As Integer, Optional pTamanho As Variant, Optional pShowMsg As Variant) As Boolean
//''Dim xField  As Field
//''Dim xSize   As Integer
//''    On Error Resume Next
//''    If IsMissing(pTamanho) Then xSize = 0 Else: xSize = pTamanho
//''    Set xField = pTableDef.CreateField(pNome, pTipo, xSize)
//''    pTableDef.Fields.Append xField
//''    IOFieldCreate = CBool(Err = 0)
//''    If Not Err = 0 Then
//''        If IsMissing(pShowMsg) Then
//''            IOFieldCreate = CBool(Not GetErro(Err, (pNome)))
//''        End If
//''    End If
//''    'IOTableCreateAppend(pDB As Database, pTableName As String, Optional pShowMsg As Variant) As Boolean
//''End Function
//''Public Function IOIndexCreate(pTableDef As TableDef, pNome As String, pCampos As String, pPrimary As Boolean, pRequired As Boolean, Optional pShowMsg As Variant) As Boolean
//''Dim xField      As Field
//''Dim xIndex      As Index
//''Dim xCampos()   As String
//''    On Error Resume Next
//''    BreakStringIntoArray pCampos, xCampos(), ","
//''    Set xIndex = pTableDef.CreateIndex(pNome)
//''    xIndex.Primary = pPrimary
//''    xIndex.Required = pRequired
//''    For x = 0 To UBound(xCampos)
//''        Set xField = xIndex.CreateField(xCampos(x))
//''        xIndex.Fields.Append xField
//''    Next
//''    pTableDef.Indexes.Append xIndex
//''    IOIndexCreate = CBool(Err = 0)
//''    If Not Err = 0 Then
//''        If GetOptional(pShowMsg, False) Then
//''            IOIndexCreate = CBool(Not GetErro(Err, (pNome)))
//''        End If
//''    End If
//''End Function
//''Public Function TableExist(pDB As adodb.Connection, pTable As String) As Boolean
//''Dim xTabledef   As TableDef
//''    TableExist = False
//''    pDB.TableDefs.Refresh
//''    For Each xTabledef In pDB.TableDefs
//''        If UCase(xTabledef.Name) = UCase(pTable) Then
//''            TableExist = True
//''            Exit Function
//''        End If
//''    Next
//''End Function
//''Public Function IODeleteTable(pDB As adodb.Connection, pNome As String, Optional pShowMsg As Variant) As Boolean
//''    On Error Resume Next
//''    pDB.TableDefs.Delete pNome
//''    IODeleteTable = CBool(Err = 0)
//''    If Not Err = 0 Then
//''        If GetOptional(pShowMsg, False) Then
//''            IODeleteTable = CBool(Not GetErro(Err, (pNome)))
//''        End If
//''    End If
//''End Function
//
//''Public Sub IOErroDeDados()
//''    On Error Resume Next
//''    Err.Raise gErroDeDados
//''End Sub
//
//''============================================================
//''Public Sub CopyDataBase(pSource As String, pTarget As String)
//''    'If Not pSource.Attributes = 0 Then Exit Sub
//''    'pSource.MoveFirst
//''    'Do Until pSource.EOF
//''    '    X = IOAdd(pTarget)
//''    '    CopyFields pSource, pTarget
//''    '    X = IOUpdate(pTarget)
//''    '    pSource.MoveNext
//''    'Loop
//''End Sub
//
//''Public Sub CopyTableRows(pSource As ADODB.Recordset, pTarget As ADODB.Recordset)
//''    'If Not pSource.Attributes = 0 Then Exit Sub
//''    pSource.MoveFirst
//''    Do Until pSource.EOF
//''        x = IOAdd(pTarget)
//''        CopyFields pSource, pTarget
//''        x = IOUpdate(pTarget)
//''        pSource.MoveNext
//''    Loop
//''End Sub
//
//
//#End Region
