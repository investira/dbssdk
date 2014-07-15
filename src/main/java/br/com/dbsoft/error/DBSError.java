package br.com.dbsoft.error;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.IO.DB_SERVER;
import br.com.dbsoft.factory.DBSLinkedProperties;
import br.com.dbsoft.util.DBSIO;
import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;

public class DBSError {
	
	public static class CODES{
		public static Integer GENERIC = -1;
		public static Integer NO_CONNECTION = 1;
		public static Integer INTEGRITY_CONSTRAINT = 2;
		public static Integer INVALID_LOGIN = 3;
		public static Integer LARGE_VALUE = 4;
		public static Integer PASSWORD_EXPIRED = 5;
		public static Integer DUPLICATED_KEY = 6;
	}
	
	protected static Logger				wLogger = Logger.getLogger(DBSError.class);
	private static DBSLinkedProperties 	wExceptionMessages = new DBSLinkedProperties();

	//######################################################################################################### 
	//## Public Methods                                                                                     #
	//#########################################################################################################
	/**
	 * Retorna texto traduzido da mensagem de erro
	 * @param pSQLException
	 * @return
	 */
	public static String getErrorMessage(String pErrorCode){
		if(wExceptionMessages.isEmpty()){
			pvErrorMessageInit();
		}
		String xMsg = DBSObject.getNotNull(wExceptionMessages.get(pErrorCode),"").toString();
//		if (!xMsg.equals("")){
//			xMsg = xMsg + "[" + pErrorCode + "]";
//		}
		return xMsg;
	}
	
	
	/**
	 * Retorna o primeiro exception que iniciou o erro
	 * @param pThrowable
	 * @return
	 */
	public static SQLException getFirstSQLException(Throwable pThrowable, Throwable pSavedSQLException){
		if (pThrowable.getCause() == null){
			if (pSavedSQLException instanceof SQLException){
				return (SQLException) pSavedSQLException;
			}else{
				return null;
			}
		}else{
			if (pThrowable.getCause() instanceof SQLException){
				return getFirstSQLException(pThrowable.getCause(), pThrowable.getCause());
			}else{
				return getFirstSQLException(pThrowable.getCause(), pSavedSQLException);
			}
		}
	}
	
	/**
	 * Retorna código sem saber qual o banco de dados.
	 * Esta função só deverá ser utilizada ao testar abria a conexão
	 * @param e
	 * @return
	 */
	private static Integer pvToCodes(SQLException e){
		int xCode = e.getErrorCode();
		if (xCode == 1017){
			return CODES.INVALID_LOGIN;
		}else if (xCode == 12505){
			return CODES.NO_CONNECTION;
		}else if (xCode == 28001){
			return CODES.PASSWORD_EXPIRED;
		}
		return null;
	}
	
	/**
	 * Retorna o código de erro DBSoft a partir do código de erro do banco conforme o fabricante
	 * @param e
	 * @param pConnection
	 * @return
	 */
	public static Integer toCodes(SQLException e, Connection pConnection){
		if (pConnection == null){
			return pvToCodes(e);
		}else{
			DB_SERVER xDBP = DBSIO.getDataBaseProduct(pConnection);
			if (xDBP == DB_SERVER.ORACLE){
				return pvToCodes_ORACLE(e);
			}else if (xDBP == DB_SERVER.MYSQL){
				return pvToCodes_MYSQL(e);
			}else if (xDBP == DB_SERVER.POSTGRESQL){ 
				return pvToCodes_POSTGRESQL(e);
			}else if (xDBP == DB_SERVER.SQLSERVER ||
					  xDBP == DB_SERVER.SYBASE){
				return pvToCodes_SQLSERVER(e); 
			}
		}
		return null;
	}
	
	private static Integer pvToCodes_ORACLE(SQLException e){
		int xCode = e.getErrorCode();
		if (xCode == 1 || xCode == 2290){
			return CODES.INTEGRITY_CONSTRAINT;
		}else if (xCode == 1017){
			return CODES.INVALID_LOGIN;
		}else if (xCode == 12505){
			return CODES.NO_CONNECTION;
		}else if (xCode == 12899){
			return CODES.LARGE_VALUE;
		}else if (xCode == 28001){
			return CODES.PASSWORD_EXPIRED;
		}
		return CODES.GENERIC;
	}
	
	private static Integer pvToCodes_MYSQL(SQLException e){
		int xCode = e.getErrorCode();
		if (xCode == 1451){
			return CODES.INTEGRITY_CONSTRAINT;
		}else if (xCode == 1062){
			return CODES.DUPLICATED_KEY;
		}
		return CODES.GENERIC;
	}
	
	private static Integer pvToCodes_POSTGRESQL(SQLException e){
		return CODES.GENERIC;
	}
	private static Integer pvToCodes_SQLSERVER(SQLException e){
		return CODES.GENERIC;
	}


	/**
	 * Carrega os dicionarios uma única vez para melhor a performance
	 */
	private static void pvErrorMessageInit(){
		try {
			wExceptionMessages.load(DBSString.class.getResourceAsStream("/META-INF/error_messages_br.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
//	/**
//	 * Exibe no console os erros de exception
//	 * @param pException Exception recebido
//	 * @param pAdicionalMsg Mensagem adicional
//	 * @return true = Erro fatal / false = Erro tradado 
//	 */
//	public static boolean showException(Exception pException, String pAdicionalMsg){
//		String xMensagem = "";
//
//		if (pException instanceof java.lang.NullPointerException ){
//			xMensagem = "Conteúdo nulo";
//			//			}else if (pException instanceof javax.ejb.EJBException){
//			//				javax.ejb.EJBException xE = (EJBException) pException;
//		}else if (pException instanceof java.sql.SQLException){
//			return pvSQLException((java.sql.SQLException) pException);
//		}
////		else{
////			xMensagem = pException.getMessage();
////		}
//		if (pAdicionalMsg != ""){
//			xMensagem = pAdicionalMsg;
//		}
//		pvPrintException(pException, xMensagem);
//		return true;
//	}
	
//	//Overload
//	public static boolean showException(Exception pException){
//		return showException(pException,"");
//	}
//	
	/**
	 * Exibe no console, a mensagem de erro de valida�‹o
	 * @param pMethodName Nome da rotina onde ocorreu o erro de valida�‹o
	 * @param pMessage Mensagem a ser exibida
	 */
	public static void showValidationErro(String pMethodName, String pMessage){
		System.out.println("Erro:" + pMethodName);
		System.out.println("\t" + pMessage);
		//Log
		String xClasse = pMethodName;
		Logger xLogger = Logger.getLogger(xClasse);
		xLogger.error(pMessage);
	}
	
//	/**
//	 * Verifica se Ž erro de registro lockado.
//	 * @param pException Exception a ser analisada
//	 * @return true = erro / false = ignora erro
//	 */
//	public static boolean isRecordLockedException(Exception pException){
//		if (pException instanceof java.sql.SQLException){
//			java.sql.SQLException xxSQLException = (SQLException) pException;
//			String xxErr = xxSQLException.getSQLState();
//
//			if(xxErr.equals("61000")){ //resource error ORA-00018 .. 00035 ORA-00050 .. 00068 ORA-02376 .. 02399 ORA-04020 .. 04039
//				if (xxSQLException.getErrorCode() == 54){//Lock exist and NOWAIT was used
//					return true; 
//				}
//			}	
//		}
//		return false;
//	}	
	
//	/**
//	 * Verifica se Ž erro de registro duplicado
//	 * @param pException Exception a ser analisada
//	 * @return true = erro / false = ignora erro
//	 */
//	public static boolean isIntegrityViolationException(Exception pException){
//		if (pException instanceof java.sql.SQLException){
//			java.sql.SQLException xxSQLException = (SQLException) pException.getCause();
//			String xxErr = xxSQLException.getSQLState();
//
//			if(xxErr.equals("23000")){ //integrity constraint violation ORA-00001 ORA-02290 .. 02299
//				if (xxSQLException.getErrorCode() == 1){//Registro j‡ existe(Chave duplicada)
//					return true; 
//				}
//			}	
//		}
//		return false;
//	}	

	//######################################################################################################### 
	//## Private Methods                                                                                     #
	//#########################################################################################################
	
//	/**
//	 * Exibe no console os error encontrados
//	 * @param pException a ser impressa
//	 * @param pMensagem Mensagem adicional a ser incorporada a mensagem padr‹o
//	 */
//	private static void pvPrintException(Exception pException, String pMensagem){
//		StackTraceElement[] xT =  pException.getStackTrace();
//		String xS = "\n\t";
//		
//		pMensagem = "Exception:" + pException.toString() + ":" + pMensagem;
//		if (pException.getSuppressed()!=null && pException.getSuppressed().length>0){
//			pMensagem = pMensagem + ":" + pException.getSuppressed()[0].getMessage();
//		}
//		for (int x=0; x!=xT.length-1; x++) {
//			pMensagem = pMensagem +
//						 xS + 
//						 "Linha=" + DBSFormat.getFormattedNumber((double)xT[x].getLineNumber(), " 00000;-00000") + ":"  + 
//						 xT[x].getClassName() + ":" +
//						 xT[x].getFileName() + ":" + 
//						 xT[x].getMethodName();
//		}
//		System.out.println(pMensagem);
//		//pException.printStackTrace();
//		String xClasse = xT[0].getClassName();
//		Logger xLogger = Logger.getLogger(xClasse);
//		xLogger.error(pMensagem);
//	}
//	
//	/**
//	 * Analisa expecicifamente a exception SQLException
//	 * @param pSQLException Exception a ser analisada
//	 * @return true = erro / false = ignora erro
//	 */
//	private static boolean pvSQLException(java.sql.SQLException pSQLException){
//		pSQLException.fillInStackTrace();
//		String xxMensagem  = "";
//		int xxErrVendor = pSQLException.getErrorCode();
//		SQLException xxException = pSQLException;
//		if (xxErrVendor==0){
//			xxException = (SQLException) pSQLException.getCause();
//			xxErrVendor = xxException.getErrorCode();
//		}
//		if (xxErrVendor == 1){
//			xxMensagem = "Registro duplicado!";
//		}else if (xxErrVendor == 54){
//			xxMensagem = "Registro em uso!";
//		}else if (xxErrVendor == 17011){
//			xxMensagem = "Resultset posicionado em registro vázio!";
//		}else{
//			xxMensagem = xxException.getMessage();
//		}
//		xxMensagem =  xxErrVendor + ":" + xxMensagem;
//		pvPrintException(pSQLException, xxMensagem);
//
//
////		if (xxErr.equals("01000")){ //Warning
////		}else if(xxErr.equals("01001")){ //cursor operation conflict
////		}else if(xxErr.equals("01002")){ //disconnect error
////		}else if(xxErr.equals("01003")){ //null value eliminated in set function
////		}else if(xxErr.equals("01004")){ //string data - right truncation
////		}else if(xxErr.equals("01005")){ //insufficient item descriptor areas
////		}else if(xxErr.equals("01006")){ //privilege not revoked
////		}else if(xxErr.equals("01007")){ //privilege not granted
////		}else if(xxErr.equals("01008")){ //implicit zero-bit padding
////		}else if(xxErr.equals("01009")){ //search condition too long for info schema
////		}else if(xxErr.equals("0100A")){ //query expression too long for info schema
////		}else if(xxErr.equals("02000")){ //no data ORA-01095, ORA-01403
////		}else if(xxErr.equals("07000")){ //dynamic SQL error
////		}else if(xxErr.equals("07001")){ //using clause does not match parameter specs
////		}else if(xxErr.equals("07002")){ //using clause does not match target specs
////		}else if(xxErr.equals("07003")){ //cursor specification cannot be executed
////		}else if(xxErr.equals("07004")){ //using clause required for dynamic parameters
////		}else if(xxErr.equals("07005")){ //prepared statement not a cursor specification
////		}else if(xxErr.equals("07006")){ //restricted datatype attribute violation
////		}else if(xxErr.equals("07007")){ //using clause required for result fields
////		}else if(xxErr.equals("07008")){ //invalid descriptor count /SQL-02126
////		}else if(xxErr.equals("07009")){ //invalid descriptor index
////		}else if(xxErr.equals("08000")){ //connection exception
////		}else if(xxErr.equals("08001")){ //SQL client unable to establish SQL connection
////		}else if(xxErr.equals("08002")){ //connection name in use
////		}else if(xxErr.equals("08003")){ //connection does not exist SQL-02121
////		}else if(xxErr.equals("08004")){ //SQL server rejected SQL connection
////		}else if(xxErr.equals("08006")){ //connection failure
////		}else if(xxErr.equals("0A001")){ //feature not supported ORA-03000 .. 03099
////		}else if(xxErr.equals("21000")){ //cardinality violation ORA-01427 SQL-02112
////		}else if(xxErr.equals("22000")){ //ata exception
////		}else if(xxErr.equals("22001")){ //string data - right truncation ORA-01401 ORA-01406
////		}else if(xxErr.equals("22002")){ //null value - no indicator parameter ORA-01405 SQL-02124
////		}else if(xxErr.equals("22003")){ //numeric value out of range ORA-01426 ORA-01438 ORA-01455 ORA-01457
////		}else if(xxErr.equals("22005")){ //error in assignment
////		}else if(xxErr.equals("22007")){ //invalid date-time format
////		}else if(xxErr.equals("22008")){ //date-time field overflow ORA-01800 .. 01899
////			xxMensagem = "Data inv‡lida![" +  xxMensagem+ "]";
////		}else if(xxErr.equals("22009")){ //invalid time zone displacement value
////		}else if(xxErr.equals("22011")){ //substring error
////		}else if(xxErr.equals("22012")){ //division by zero ORA-01476
////		}else if(xxErr.equals("22015")){ //interval field overflow
////		}else if(xxErr.equals("22018")){ //invalid character value for cast
////		}else if(xxErr.equals("22019")){ //invalid escape character ORA-00911 ORA-01425
////		}else if(xxErr.equals("22021")){ //character not in repertoire
////		}else if(xxErr.equals("22022")){ //indicator overflow ORA-01411
////		}else if(xxErr.equals("22023")){ //invalid parameter value ORA-01025 ORA-01488 ORA-04000 .. 04019
////		}else if(xxErr.equals("22024")){ //unterminated C string ORA-01479 .. 01480
////		}else if(xxErr.equals("22025")){ //invalid escape sequence ORA-01424
////		}else if(xxErr.equals("22026")){ //string data - length mismatch
////		}else if(xxErr.equals("22027")){ //trim error
////		}else if(xxErr.equals("23000")){ //integrity constraint violation ORA-00001 ORA-02290 .. 02299
////			if (xxErrVendor == 1){//Lock exist and NOWAIT was used
////				xxMensagem = "Registro j‡ existe[" +  xxMensagem+ "]";
////			}
////		}else if(xxErr.equals("24000")){ //invalid cursor state ORA-01001 .. 01003 ORA-01410 ORA-08006 SQL-02114 SQL-02117 SQL-02118 SQL-02122
////		}else if(xxErr.equals("25000")){ //invalid transaction state
////		}else if(xxErr.equals("26000")){ //invalid SQL statement name
////		}else if(xxErr.equals("27000")){ //triggered data change violation
////		}else if(xxErr.equals("28000")){ //invalid authorization specification
////		}else if(xxErr.equals("2A000")){ //direct SQL syntax error or access rule violation
////		}else if(xxErr.equals("2B000")){ //dependent privilege descriptors still exist
////		}else if(xxErr.equals("2C000")){ //invalid character set name
////		}else if(xxErr.equals("2D000")){ //invalid transaction termination
////		}else if(xxErr.equals("2E000")){ //invalid connection name
////		}else if(xxErr.equals("33000")){ //invalid SQL descriptor name
////		}else if(xxErr.equals("34000")){ //invalid cursor name
////		}else if(xxErr.equals("35000")){ //invalid condition number
////		}else if(xxErr.equals("37000")){ //dynamic SQL syntax error or access rule violation
////		}else if(xxErr.equals("3C000")){ //ambiguous cursor name
////		}else if(xxErr.equals("3D000")){ //invalid catalog name
////		}else if(xxErr.equals("3F000")){ //invalid schema name
////		}else if(xxErr.equals("40000")){ //transaction rollback ORA-02091 .. 02092
////		}else if(xxErr.equals("40001")){ //serialization failure
////		}else if(xxErr.equals("40002")){ //integrity constraint violation
////		}else if(xxErr.equals("40003")){ //statement completion unknown
////		}else if(xxErr.equals("42000")){ //systax error or access rule violation ORA-00022 ORA-00251 ORA-00900 .. 00999 ORA-01031 ORA-01490 .. 01493 ORA-01700 .. 01799 ORA-01900 .. 02099 ORA-02140 .. 02289 ORA-02420 .. 02424 ORA-02450 .. 02499 ORA-03276 .. 03299 ORA-04040 .. 04059 ORA-04070 .. 04099
////			if (xxErrVendor == 904){
////				xxMensagem = "Nome de coluna inv‡lido![" + xxMensagem+ "]";
////			}
////		}else if(xxErr.equals("44000")){ //with check option violation ORA-01402
////		}else if(xxErr.equals("60000")){ //system errors ORA-00370 .. 00429 ORA-00600 .. 00899 ORA-06430 .. 06449 ORA-07200 .. 07999 ORA-09700 .. 09999
////		}else if(xxErr.equals("61000")){ //resource error ORA-00018 .. 00035 ORA-00050 .. 00068 ORA-02376 .. 02399 ORA-04020 .. 04039
////			if (xxErrVendor == 54){//Lock exist and NOWAIT was used
////				xxMensagem = "Registro em uso![" +  xxMensagem+ "]";
////			}
////		}else if(xxErr.equals("62000")){ //multi-threaded server and detached process errors ORA-00100 .. 00120 ORA-00440 .. 00569
////		}else if(xxErr.equals("63000")){ //Oracle*XA and two-task interface errors ORA-00150 .. 00159 SQL-02128 ORA-02700 .. 02899 ORA-03100 .. 03199 ORA-06200 .. 06249
////		}else if(xxErr.equals("64000")){ //control file, database file, and redo file errors; archival and media recovery errors ORA-00200 .. 00369 ORA-01100 .. 01250
////		}else if(xxErr.equals("65000")){ //PL/SQL errors ORA-06500 .. 06599
////		}else if(xxErr.equals("66000")){ //SQL*Net driver errors ORA-06000 .. 06149 ORA-06250 .. 06429 ORA-06600 .. 06999 ORA-12100 .. 12299 ORA-12500 .. 12599
////		}else if(xxErr.equals("67000")){ //licensing errors ORA-00430 .. 00439
////		}else if(xxErr.equals("69000")){ //SQL*Connect errors ORA-00570 .. 00599 ORA-07000 .. 07199
////		}else if(xxErr.equals("72000")){ //SQL execute phase errors ORA-01400 .. 01489 ORA-01495 .. 01499 ORA-01500 .. 01699 ORA-02400 .. 02419 ORA-02425 .. 02449 ORA-04060 .. 04069 ORA-08000 .. 08190 ORA-12000 .. 12019 ORA-12300 .. 12499 ORA-12700 .. 21999 
////		}else if(xxErr.equals("82100")){ //out of memory (could not allocate) SQL-02100
////		}else if(xxErr.equals("82101")){ //inconsistent cursor cache: unit cursor/global cursor mismatch SQL-02101
////		}else if(xxErr.equals("82102")){ //nconsistent cursor cache: no global cursor entry SQL-02102
////		}else if(xxErr.equals("82103")){ //inconsistent cursor cache: out of range cursor cache reference SQL-02103
////		}else if(xxErr.equals("82104")){ //inconsistent host cache: no cursor cache available SQL-02104
////		}else if(xxErr.equals("82105")){ //inconsistent cursor cache: global cursor not found SQL-02105
////		}else if(xxErr.equals("82106")){ //inconsistent cursor cache: invalid Oracle cursor number SQL-02106
////		}else if(xxErr.equals("82107")){ //program too old for runtime library SQL-02107
////		}else if(xxErr.equals("82108")){ //invalid descriptor passed to runtime library SQL-02108
////		}else if(xxErr.equals("82109")){ //inconsistent host cache: host reference is out of range SQL-02109
////		}else if(xxErr.equals("82110")){ //inconsistent host cache: invalid host cache entry type SQL-02110
////		}else if(xxErr.equals("82111")){ //heap consistency error SQL-02111
////		}else if(xxErr.equals("82112")){ //unable to open message file SQL-02113
////		}else if(xxErr.equals("82113")){ //code generation internal consistency failed SQL-02115
////		}else if(xxErr.equals("82114")){ //reentrant code generator gave invalid context SQL-02116
////		}else if(xxErr.equals("82115")){ //invalid hstdef argument SQL-02119
////		}else if(xxErr.equals("82116")){ //first and second arguments to sqlrcn both null SQL-02120
////		}else if(xxErr.equals("82117")){ //invalid OPEN or PREPARE for this connection SQL-02122
////		}else if(xxErr.equals("82118")){ //application context not found SQL-02123
////		}else if(xxErr.equals("82119")){ //connect error; can't get error text SQL-02125
////		}else if(xxErr.equals("82120")){ //precompiler/SQLLIB version mismatch. SQL-02127
////		}else if(xxErr.equals("82121")){ //FETCHed number of bytes is odd SQL-02129
////		}else if(xxErr.equals("82122")){ //EXEC TOOLS interface is not available SQL-02130
////		}else if(xxErr.equals("90000")){ //debug events ORA-10000 .. 10999
////		}else if(xxErr.equals("99999")){ //catch all others
////		}else if(xxErr.equals("HZ000")){ //remote database access
////		}
////		pvPrintException(xxE, xxMensagem);
//		return true;
//	}



}
