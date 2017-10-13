package br.com.dbsoft.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.util.DBSDate.PERIODICIDADE;
import junit.framework.TestCase;

/**
 * @author ricardo.villar
 *
 */
public class TstDate extends TestCase {

	//IFEED
//	String wUrl="jdbc:mysql://localhost:3306/dbsfnd?zeroDateTimeBehavior=convertToNull&amp;useOldAliasMetadataBehavior=true";
	//ORACLE
	String wUrl="jdbc:oracle:thin:@192.168.0.115:1521:XE";
	
	String wUser="dbsfnd";
	String wPassword="dbs0ft";

	Connection wConexao;
	
	@Override
	@Before
	public void setUp() throws ClassNotFoundException {
//		Class.forName(DBSSDK.JDBC_DRIVER.MYSQL); 
//		wwConexao = DriverManager.getConnection(url, user, password);
//		wwConexao.setAutoCommit(false);
		try {
			Class.forName(DBSSDK.JDBC_DRIVER.ORACLE);
			if (wConexao == null || wConexao.isClosed()){
				wConexao = DriverManager.getConnection(wUrl, wUser, wPassword);
				wConexao.setAutoCommit(true);	
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		DBSSDK.TABLE.FERIADO = "GR_FERIADO";
	}

	@Override
	@After
	public void tearDown() throws Exception {
		wConexao.close();
	}
	
	
	@Test
	public void test_isDate() {
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate(""));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("//"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("000000"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("SDASDS"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("SD/AS/DS"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("00/00/00"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("10"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("01/00/00"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("00/01/00"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("00/00/01"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("2000/00/00"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("01/13/10"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("12-01-2011"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDate("2011-31-12"));
		
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDate("2011-12-31"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDate("12/01/2011"));		
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDate("01-12-11"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDate("01/12/11"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDate("01/12/2011"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDate("31/12/2011"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDate("31/12/1900"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDate("31/12/1800"));
	}
	@Test
	public void test_isTime(){
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime(""));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("::"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("000000"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("SDASDS"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("SD:AS:DS"));
//		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("00:00:00"));
//		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("0:0:0"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("01-12-11"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("01/12/11"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("99:99:99"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("25:00:00"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("24:00:00"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("00:60:00"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("00:00:60"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("1:90:90"));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isTime("0h"));
		
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isTime("23:59:59"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isTime("0:00:00"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isTime("0:00:01"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isTime("0:59:59"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isTime("00:00:00"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isTime("00:00:01"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isTime("00:59:59"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isTime("1:59:59"));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isTime("1:9:9"));
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void teste_toDateYMDHMS(){
		assertEquals(DBSDate.toDateYMDHMS("2014-01-02 10:10:01").toLocaleString(), "02/01/2014 10:10:01");
		assertEquals(DBSDate.toDateYMDHMS("2014/01/02 10:10:01").toLocaleString(), "02/01/2014 10:10:01");
		assertEquals(DBSDate.toDateYMDHMS("20140102000201").toLocaleString(), "02/01/2014 00:02:01");
	}
	
	@Test
	public void test_toDate(){
			//TESTANDO Date toDate(String pDia, String pMes, String pAno)
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("00","00","00"));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("","",""));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("SS","AA","BB"));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("10","00","00"));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("00","10","00"));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("00","00","10"));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("32","13","2110"));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("2000","10","10"));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("31","11","2010"));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate("29","02","2010"));
			assertNotNull("TESTE ESPERAVA NOTNULL",DBSDate.toDate("31","12","2011"));
			assertNotNull("TESTE ESPERAVA NOTNULL",DBSDate.toDate("28","02","2011"));
			assertNotNull("TESTE ESPERAVA NOTNULL",DBSDate.toDate("29","02","2012"));
			assertNotNull("TESTE ESPERAVA NOTNULL",DBSDate.toDate("31","12","1900"));
			
			//TESTANDO Date toDate(int pDia, int pMes, int pAno)
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(0,0,0));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(-1,-1,-1));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(null,null,null));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(10,0,0));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(0,10,0));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(0,0,10));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(32,13,2110));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(2000,10,10));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(31,11,2010));
			assertNull("TESTE ESPERAVA NULL",DBSDate.toDate(29,02,2010));
			assertNotNull("TESTE ESPERAVA NOTNULL",DBSDate.toDate(31,12,2011));
			assertNotNull("TESTE ESPERAVA NOTNULL",DBSDate.toDate(28,02,2011));
			assertNotNull("TESTE ESPERAVA NOTNULL",DBSDate.toDate(29,02,2012));
			assertNotNull("TESTE ESPERAVA NOTNULL",DBSDate.toDate(31,12,1900));
			
			//TESTANDO Date toDate(String pData)
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate(""));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("//"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("000000"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("SDASDS"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("SD/AS/DS"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("00/00/00"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("10"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("01/00/00"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("00/01/00"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("00/00/01"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("2000/00/00"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("01/13/10"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("12-01-2011"));
			assertNull("TESTE ESPERAVA NULL", DBSDate.toDate("2011-31-12"));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate("2011-12-31"));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate("12/01/2011"));		
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate("01-12-11"));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate("01/12/11"));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate("01/12/2011"));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate("31/12/2011"));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate("31/12/1900"));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate("31/12/1800"));

			//TESTANDO Date toDate(long pMilliSeconds)
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate(0L));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate(1318561200000L));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate(1318820400000L));
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate(1318816800000L));
			
			//TESTANDO Date toDate(Calendar pData)
			Calendar cal = Calendar.getInstance();
			cal.setTime(DBSDate.toDate("31/12/2010"));
			cal.add(Calendar.DAY_OF_MONTH, -1);
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toDate(cal));

			//TESTANDO Calendar toCalendar(Date pData)
			assertNotNull("TESTE ESPERAVA NOTNULL", DBSDate.toCalendar(DBSDate.toDate("01/01/2011")));
	}
	
	@Test
	public void test_getDias(){
		//DIAS CORRIDOS NO MES DE NOVEMBRO DE 2013
		int xDias = DBSDate.getDays(wConexao, DBSDate.toDate("14/01/2013"), DBSDate.toDate("07/03/2014"), false, -1);
		assertEquals(417, xDias);
		
		//DIAS ÚTEIS NO MES DE NOVEMBRO DE 2013
		xDias = DBSDate.getDays(wConexao, DBSDate.toDate("14/01/2013"), DBSDate.toDate("07/03/2014"), true, -1);
		assertEquals(289, xDias);
		
		xDias = DBSDate.getDays(wConexao, DBSDate.toDate("01/07/2000"), DBSDate.toDate("07/03/2014"), true, -1);
		assertEquals(3436, xDias);
		
		xDias = DBSDate.getDays(wConexao, DBSDate.toDate("15/12/2012"), DBSDate.toDate("15/01/2013"), true, -1, "RF");
		assertEquals(20, xDias);
		
//		assertEquals(400, DBSDate.getDias(wConexao, DBSDate.toDate("14/01/2013"), DBSDate.getProximaData(wConexao, DBSDate.toDate("15/08/2014"), 0, true, -1), true, -1));
//		
//		assertEquals(-2, DBSDate.getDias(wConexao, DBSDate.toDate("03/01/2011"), DBSDate.toDate("01/01/2011"), false, -1));
//		assertEquals(30, DBSDate.getDias(wConexao, DBSDate.toDate("01/01/2010"), DBSDate.toDate("31/01/2010"), false, -1));
//		assertEquals(1, DBSDate.getDias(wConexao, DBSDate.toDate("01/01/2011"), DBSDate.toDate("02/01/2011"), false, -1));
//		assertEquals(2, DBSDate.getDias(wConexao, DBSDate.toDate("01/01/2011"), DBSDate.toDate("03/01/2011"), false, -1));
//
//		assertEquals(308, DBSDate.getDias(wConexao, DBSDate.toDate("31/10/2009"), DBSDate.toDate("21/01/2011"), true, -1));
//		assertEquals(302, DBSDate.getDias(wConexao, DBSDate.toDate("19/10/2009"), DBSDate.toDate("01/01/2011"), true, -1));
//		assertEquals(0, DBSDate.getDias(wConexao, DBSDate.toDate("08/10/2011"), DBSDate.toDate("09/10/2011"), true, -1));
//		assertEquals(-2, DBSDate.getDias(wConexao, DBSDate.toDate("05/01/2011"), DBSDate.toDate("01/01/2011"), true, -1));
//		assertEquals(22, DBSDate.getDias(wConexao, DBSDate.toDate("01/01/2011"), DBSDate.toDate("01/02/2011"), true, -1));
//		assertEquals(-21, DBSDate.getDias(wConexao, DBSDate.toDate("01/02/2011"), DBSDate.toDate("01/01/2011"), true, -1));
	}
	
	@Test
	public void test_getMeses(){
		//TESTANDO getMeses(Date pDataInicio, Date pDataFim)
		assertEquals(-1, DBSDate.getMonths(DBSDate.toDate("05/02/2011"), DBSDate.toDate("05/01/2011")));
		assertEquals(0, DBSDate.getMonths(DBSDate.toDate("05/01/2011"), DBSDate.toDate("05/01/2011")));
		assertEquals(1, DBSDate.getMonths(DBSDate.toDate("05/01/2011"), DBSDate.toDate("05/02/2011")));
		assertEquals(2, DBSDate.getMonths(DBSDate.toDate("05/11/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(3, DBSDate.getMonths(DBSDate.toDate("05/10/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(4, DBSDate.getMonths(DBSDate.toDate("05/09/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(5, DBSDate.getMonths(DBSDate.toDate("05/08/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(6, DBSDate.getMonths(DBSDate.toDate("05/07/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(7, DBSDate.getMonths(DBSDate.toDate("05/06/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(8, DBSDate.getMonths(DBSDate.toDate("05/05/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(9, DBSDate.getMonths(DBSDate.toDate("05/04/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(10, DBSDate.getMonths(DBSDate.toDate("05/03/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(11, DBSDate.getMonths(DBSDate.toDate("05/01/2011"), DBSDate.toDate("05/12/2011")));
		assertEquals(12, DBSDate.getMonths(DBSDate.toDate("05/01/2010"), DBSDate.toDate("05/01/2011")));
		//TESTANDO getMeses(Calendar pDataInicio, Calendar pDataFim)
		assertEquals(0, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/01/2011")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(1, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/01/2011")), DBSDate.toCalendar(DBSDate.toDate("05/02/2011"))));
		assertEquals(2, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/11/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(3, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/10/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(4, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/09/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(5, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/08/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(6, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/07/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(7, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/06/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(8, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/05/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(9, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/04/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(10, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/03/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(11, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/01/2011")), DBSDate.toCalendar(DBSDate.toDate("05/12/2011"))));
		assertEquals(12, DBSDate.getMonths(DBSDate.toCalendar(DBSDate.toDate("05/01/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
	}
	@Test
	public void test_getAnos(){
		//TESTANDO getAnos(Date pDataInicio, Date pDataFim)
		assertEquals(0, DBSDate.getYears(DBSDate.toDate("05/01/2011"), DBSDate.toDate("05/01/2011")));
		assertEquals(1, DBSDate.getYears(DBSDate.toDate("31/12/2011"), DBSDate.toDate("01/01/2012")));
		assertEquals(-1, DBSDate.getYears(DBSDate.toDate("05/11/2010"), DBSDate.toDate("05/01/2009")));
		//TESTANDO getAnos(Calendar pDataInicio, Calendar pDataFim)
		assertEquals(0, DBSDate.getYears(DBSDate.toCalendar(DBSDate.toDate("05/01/2011")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(1, DBSDate.getYears(DBSDate.toCalendar(DBSDate.toDate("31/12/2011")), DBSDate.toCalendar(DBSDate.toDate("01/01/2012"))));
		assertEquals(-1, DBSDate.getYears(DBSDate.toCalendar(DBSDate.toDate("05/11/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2009"))));
	}
	@Test
	public void test_getDateDif(){
//		assertEquals(439, DBSDate.toCalendar(DBSDate.toDate("19/10/2009")).get(Calendar.DATE) -  DBSDate.toCalendar(DBSDate.toDate("01/01/2011")).get(Calendar.DATE));
		assertEquals(447, DBSDate.getDateDif(DBSDate.toDate("31/10/2009"), DBSDate.toDate("21/01/2011")));
		assertEquals(439, DBSDate.getDateDif(DBSDate.toDate("19/10/2009"), DBSDate.toDate("01/01/2011")));
		assertEquals(214, DBSDate.getDateDif(DBSDate.toDate("01/06/2010"), DBSDate.toDate("01/01/2011")));
		assertEquals(40542, DBSDate.getDateDif(DBSDate.toDate("01/01/1900"), DBSDate.toDate("01/01/2011")));
		assertEquals(-40542, DBSDate.getDateDif(DBSDate.toDate("01/01/2011"), DBSDate.toDate("01/01/1900")));
		assertEquals(440, DBSDate.getDateDif(DBSDate.toDate("18/10/2009"), DBSDate.toDate("01/01/2011")));
		assertEquals(441, DBSDate.getDateDif(DBSDate.toDate("17/10/2009"), DBSDate.toDate("01/01/2011")));
		assertEquals(579, DBSDate.getDateDif(DBSDate.toDate("01/06/2009"), DBSDate.toDate("01/01/2011")));
		assertEquals(6, DBSDate.getDateDif(DBSDate.toDate("01/01/2011"), DBSDate.toDate("07/01/2011")));
		assertEquals(51, DBSDate.getDateDif(DBSDate.toDate("15/01/2011"), DBSDate.toDate("07/03/2011")));
		assertEquals(396, DBSDate.getDateDif(DBSDate.toDate("01/12/2009"), DBSDate.toDate("01/01/2011")));
		assertEquals(426, DBSDate.getDateDif(DBSDate.toDate("01/11/2009"), DBSDate.toDate("01/01/2011")));
	}

	@Test
	public void test_getDateAdd(){
		assertEquals(DBSDate.toDate("28/02/2013"), DBSDate.addDays(DBSDate.toDate("31/10/2009"), 1216));
	}
	
	@Test
	public void test_isDiaUtil() {
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isBusinessDay(wConexao, DBSDate.toDate("01/01/2006"), -1));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isBusinessDay(wConexao, DBSDate.toDate("01/10/2011")));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isBusinessDay(wConexao, DBSDate.toDate("02/10/2011")));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isBusinessDay(wConexao, DBSDate.toDate("12/10/2011")));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isBusinessDay(wConexao, DBSDate.toDate("15/11/2011")));
		
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isBusinessDay(wConexao, DBSDate.toDate("14/11/2011")));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isBusinessDay(wConexao, DBSDate.toDate("10/10/2011")));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isBusinessDay(wConexao, DBSDate.toDate("11/10/2011")));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isBusinessDay(wConexao, DBSDate.toDate("13/10/2011")));
	}
	@Test
	public void test_getFeriados() {
		int xFeriados = DBSDate.getHolidays(wConexao, DBSDate.toDate("01/01/2011"), DBSDate.toDate("01/01/2012"), -1);
		assertEquals(9, xFeriados);//12
		
		xFeriados = DBSDate.getHolidays(wConexao, DBSDate.toDate("01/01/2011"), DBSDate.toDate("01/01/2013"), -1);
		assertEquals(22, xFeriados);//26
		
		xFeriados = DBSDate.getHolidays(wConexao, DBSDate.toDate("01/01/1999"), DBSDate.toDate("01/01/2013"), -1);
		assertEquals(139, xFeriados);//145
		
	}
	@Test
	public void test_getFinaisDeSemana(){
		assertEquals(2, DBSDate.getWeekends(DBSDate.toDate("07/10/2011"), DBSDate.toDate("10/10/2011")));
		assertEquals(1462, DBSDate.getWeekends(DBSDate.toDate("01/01/1999"), DBSDate.toDate("01/01/2013")));
		assertEquals(1173, DBSDate.getWeekends(DBSDate.toDate("15/07/2000"), DBSDate.toDate("12/10/2011")));
		assertEquals(1170, DBSDate.getWeekends(DBSDate.toDate("15/07/2000"), DBSDate.toDate("01/10/2011")));
	}
	@Test
	public void test_getDiasDoAno() {
		assertEquals(251, DBSDate.getDaysOfTheYear(wConexao, 2011, true, -1));
		assertEquals(251, DBSDate.getDaysOfTheYear(wConexao, 2011, true));
		assertEquals(365, DBSDate.getDaysOfTheYear(wConexao, 2011, false));
		assertEquals(250, DBSDate.getDaysOfTheYear(wConexao, 2009, true));
	}
	@Test
	public void test_getProximaData(){
		Date d1, d2;
		//assertEquals(DBSDate.toDate("01/02/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("01/01/2011"), 1200, true, -1));
		assertEquals(DBSDate.toDate("01/02/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("01/01/2011"), 31, false, -1));
		assertEquals(DBSDate.toDate("14/02/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("01/01/2011"), 31, true, -1));
		assertEquals(DBSDate.toDate("14/01/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("14/02/2011"), -31, false, -1));
		assertEquals(DBSDate.toDate("31/12/2010"), DBSDate.getNextDate(wConexao, DBSDate.toDate("14/02/2011"), -31, true, -1));
		assertEquals(DBSDate.toDate("03/01/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("15/02/2011"), -31, true, -1));
		assertEquals(DBSDate.toDate("13/10/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("01/10/2011"), 8, true, -1));
		assertEquals(DBSDate.toDate("14/10/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("01/10/2011"), 9, true, -1));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("01/10/2011"), 0, true, -1));
		assertEquals(DBSDate.toDate("01/10/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("01/10/2011"), 0, false, -1));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("03/10/2011"), 0, false, -1));
		d1 = DBSDate.getNextDate(wConexao, DBSDate.toDate("01/10/2011"), 10, true, -1);
		d2 = DBSDate.toDate("17/10/2011");
		assertEquals(d1.toString(), d2.toString());
		assertEquals(DBSDate.toDate("19/10/2011"), DBSDate.getNextDate(wConexao, DBSDate.toDate("18/10/2011"), 1, false));
		
		//proxima data com Timestamp
		Timestamp xT1, xT2;
		xT1 = DBSDate.getNextDate(wConexao, DBSDate.toTimestamp("01/10/2011"), 10, true); //Sem cidade
		xT2 = DBSDate.toTimestamp("17/10/2011");
		assertEquals(xT1.toString(), xT2.toString());
	}
	@Test
	public void test_getNomeDaSemana(){
		assertEquals("", DBSDate.getWeekdayName(null));
		assertEquals("", DBSDate.getWeekdayName(DBSDate.toDate("29/02/2011")));
		assertEquals("Domingo", DBSDate.getWeekdayName(DBSDate.toDate("02/10/2011")));
		assertEquals("Segunda-feira", DBSDate.getWeekdayName(DBSDate.toDate("03/10/2011")));
		assertEquals("Terça-feira", DBSDate.getWeekdayName(DBSDate.toDate("04/01/2011")));
		assertEquals("Quarta-feira", DBSDate.getWeekdayName(DBSDate.toDate("05/01/2011")));
		assertEquals("Quinta-feira", DBSDate.getWeekdayName(DBSDate.toDate("06/01/2011")));
		assertEquals("Sexta-feira", DBSDate.getWeekdayName(DBSDate.toDate("07/01/2011")));
		assertEquals("Sábado", DBSDate.getWeekdayName(DBSDate.toDate("08/01/2011")));
	}
	@Test
	public void test_getNomeDoMes(){
		assertEquals("", DBSDate.getMonthName(DBSDate.toDate("01/13/2011")));
		assertEquals("Janeiro", DBSDate.getMonthName(DBSDate.toDate("01/01/2011")));
		assertEquals("Fevereiro", DBSDate.getMonthName(DBSDate.toDate("01/02/2011")));
		assertEquals("Março", DBSDate.getMonthName(DBSDate.toDate("01/03/2011")));
		assertEquals("Abril", DBSDate.getMonthName(DBSDate.toDate("01/04/2011")));
		assertEquals("Maio", DBSDate.getMonthName(DBSDate.toDate("01/05/2011")));
		assertEquals("Junho", DBSDate.getMonthName(DBSDate.toDate("01/06/2011")));
		assertEquals("Julho", DBSDate.getMonthName(DBSDate.toDate("01/07/2011")));
		assertEquals("Agosto", DBSDate.getMonthName(DBSDate.toDate("01/08/2011")));
		assertEquals("Setembro", DBSDate.getMonthName(DBSDate.toDate("01/09/2011")));
		assertEquals("Outubro", DBSDate.getMonthName(DBSDate.toDate("01/10/2011")));
		assertEquals("Novembro", DBSDate.getMonthName(DBSDate.toDate("01/11/2011")));
		assertEquals("Dezembro", DBSDate.getMonthName(DBSDate.toDate("01/12/2011")));
	}
	@Test
	public void test_getPrimeiroDiaDoMes(){
		assertEquals(null, DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("31012011"), false));
		assertEquals(DBSDate.toDate("01/01/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("31/01/2011"), false));
		assertEquals(DBSDate.toDate("03/01/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("31/01/2011"), true));
		assertEquals(DBSDate.toDate("01/02/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("21/02/2011"), false));
		assertEquals(DBSDate.toDate("01/02/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("21/02/2011"), true));
		assertEquals(DBSDate.toDate("01/03/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/03/2011"), false));
		assertEquals(DBSDate.toDate("01/03/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/03/2011"), true));
		assertEquals(DBSDate.toDate("01/04/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/04/2011"), false));
		assertEquals(DBSDate.toDate("01/04/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/04/2011"), true));
		assertEquals(DBSDate.toDate("01/05/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/05/2011"), false));
		assertEquals(DBSDate.toDate("02/05/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/05/2011"), true));
		assertEquals(DBSDate.toDate("01/06/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/06/2011"), false));
		assertEquals(DBSDate.toDate("01/06/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/06/2011"), true));
		assertEquals(DBSDate.toDate("01/07/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/07/2011"), false));
		assertEquals(DBSDate.toDate("01/07/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/07/2011"), true));
		assertEquals(DBSDate.toDate("01/08/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/08/2011"), false));
		assertEquals(DBSDate.toDate("01/08/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/08/2011"), true));
		assertEquals(DBSDate.toDate("01/09/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/09/2011"), false));
		assertEquals(DBSDate.toDate("01/09/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/09/2011"), true));
		assertEquals(DBSDate.toDate("01/10/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/10/2011"), false));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/10/2011"), true));
		assertEquals(DBSDate.toDate("01/11/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/11/2011"), false));
		assertEquals(DBSDate.toDate("01/11/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/11/2011"), true));
		assertEquals(DBSDate.toDate("01/12/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/12/2011"), false));
		assertEquals(DBSDate.toDate("01/12/2011"), DBSDate.getFirstDayOfTheMonth(wConexao, DBSDate.toDate("15/12/2011"), true));
	}
	@Test
	public void test_getUltimoDiaDoMes(){
		assertEquals(null, DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("31012011"), false));
		assertEquals(DBSDate.toDate("31/01/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("31/01/2011"), false));
		assertEquals(DBSDate.toDate("31/01/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("31/01/2011"), true));
		assertEquals(DBSDate.toDate("28/02/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("01/02/2011"), false));
		assertEquals(DBSDate.toDate("28/02/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("01/02/2011"), true));
		assertEquals(DBSDate.toDate("31/03/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("31/03/2011"), false));
		assertEquals(DBSDate.toDate("31/03/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("31/03/2011"), true));
		assertEquals(DBSDate.toDate("30/04/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/04/2011"), false));
		assertEquals(DBSDate.toDate("29/04/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/04/2011"), true));
		assertEquals(DBSDate.toDate("31/05/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/05/2011"), false));
		assertEquals(DBSDate.toDate("31/05/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/05/2011"), true));
		assertEquals(DBSDate.toDate("30/06/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/06/2011"), false));
		assertEquals(DBSDate.toDate("30/06/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/06/2011"), true));
		assertEquals(DBSDate.toDate("31/07/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/07/2011"), false));
		assertEquals(DBSDate.toDate("29/07/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/07/2011"), true));
		assertEquals(DBSDate.toDate("31/08/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/08/2011"), false));
		assertEquals(DBSDate.toDate("31/08/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/08/2011"), true));
		assertEquals(DBSDate.toDate("30/09/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/09/2011"), false));
		assertEquals(DBSDate.toDate("30/09/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/09/2011"), true));
		assertEquals(DBSDate.toDate("31/10/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/10/2011"), false));
		assertEquals(DBSDate.toDate("31/10/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/10/2011"), true));
		assertEquals(DBSDate.toDate("30/11/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/11/2011"), false));
		assertEquals(DBSDate.toDate("30/11/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/11/2011"), true));
		assertEquals(DBSDate.toDate("31/12/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/12/2011"), false));
		assertEquals(DBSDate.toDate("30/12/2011"), DBSDate.getLastDayOfTheMonth(wConexao, DBSDate.toDate("15/12/2011"), true));
	}
	
	@Test
	public void test_getDiasDoMes(){
		assertEquals(31, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("31/01/2011"), false));
		assertEquals(21, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("31/01/2011"), true));
		assertEquals(28, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/02/2011"), false));
		assertEquals(20, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/02/2011"), true));
		assertEquals(31, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/03/2011"), false));
		assertEquals(21, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/03/2011"), true));
		assertEquals(30, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/04/2011"), false));
		assertEquals(19, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/04/2011"), true));
		assertEquals(31, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/05/2011"), false));
		assertEquals(22, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/05/2011"), true));
		assertEquals(30, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/06/2011"), false));
		assertEquals(21, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/06/2011"), true));
		assertEquals(31, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/07/2011"), false));
		assertEquals(21, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/07/2011"), true));
		assertEquals(31, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/08/2011"), false));
		assertEquals(23, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/08/2011"), true));
		assertEquals(30, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/09/2011"), false));
		assertEquals(21, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/09/2011"), true));
		assertEquals(31, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/10/2011"), false));
		assertEquals(20, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/10/2011"), true));
		assertEquals(30, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/11/2011"), false));
		assertEquals(20, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/11/2011"), true));
		assertEquals(31, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/12/2011"), false));
		assertEquals(22, DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("15/12/2011"), true));
	}
	@Test
	public void test_getUltimoDiaDoAno(){
		assertNull(DBSDate.getLastDayOfTheYear(wConexao, DBSDate.toDate("31012011"), false));
		assertEquals(DBSDate.toDate("31/12/2011"), DBSDate.getLastDayOfTheYear(wConexao, DBSDate.toDate("31/12/2011"), false));
		assertEquals(DBSDate.toDate("30/12/2011"), DBSDate.getLastDayOfTheYear(wConexao, DBSDate.toDate("31/12/2011"), true));
		assertEquals(DBSDate.toDate("31/12/2011"), DBSDate.getLastDayOfTheYear(wConexao, DBSDate.toDate("01/01/2011"), false));
		assertEquals(DBSDate.toDate("30/12/2011"), DBSDate.getLastDayOfTheYear(wConexao, DBSDate.toDate("01/01/2011"), true));
		assertEquals(DBSDate.toDate("31/12/2012"), DBSDate.getLastDayOfTheYear(wConexao, DBSDate.toDate("31/12/2012"), false));
		assertEquals(DBSDate.toDate("31/12/2012"), DBSDate.getLastDayOfTheYear(wConexao, DBSDate.toDate("31/12/2012"), true));
	}
	@Test
	public void test_getPrimeiroDiaDoAno(){
		assertNull(DBSDate.getFirstDayOfTheYear(wConexao, DBSDate.toDate("31012011"), false));
		assertEquals(DBSDate.toDate("01/01/2011"), DBSDate.getFirstDayOfTheYear(wConexao, DBSDate.toDate("31/12/2011"), false));
		assertEquals(DBSDate.toDate("03/01/2011"), DBSDate.getFirstDayOfTheYear(wConexao, DBSDate.toDate("31/12/2011"), true));
		assertEquals(DBSDate.toDate("01/01/2011"), DBSDate.getFirstDayOfTheYear(wConexao, DBSDate.toDate("01/01/2011"), false));
		assertEquals(DBSDate.toDate("03/01/2011"), DBSDate.getFirstDayOfTheYear(wConexao, DBSDate.toDate("01/01/2011"), true));
		assertEquals(DBSDate.toDate("01/01/2012"), DBSDate.getFirstDayOfTheYear(wConexao, DBSDate.toDate("31/12/2012"), false));
		assertEquals(DBSDate.toDate("02/01/2012"), DBSDate.getFirstDayOfTheYear(wConexao, DBSDate.toDate("31/12/2012"), true));
	}
	@Test
	public void test_getNumeroDoMes(){
		assertEquals(0, DBSDate.getWeekdayNumber(""));
		assertEquals(0, DBSDate.getWeekdayNumber("marco"));
		assertEquals(1, DBSDate.getWeekdayNumber("janeiro"));
		assertEquals(2, DBSDate.getWeekdayNumber("fevereiro"));
		assertEquals(3, DBSDate.getWeekdayNumber("março"));
		assertEquals(4, DBSDate.getWeekdayNumber("abril"));
		assertEquals(5, DBSDate.getWeekdayNumber("maio"));
		assertEquals(6, DBSDate.getWeekdayNumber("junho"));
		assertEquals(7, DBSDate.getWeekdayNumber("julho"));
		assertEquals(8, DBSDate.getWeekdayNumber("agosto"));
		assertEquals(9, DBSDate.getWeekdayNumber("setembro"));
		assertEquals(10, DBSDate.getWeekdayNumber("outubro"));
		assertEquals(11, DBSDate.getWeekdayNumber("novembro"));
		assertEquals(12, DBSDate.getWeekdayNumber("dezembro"));
	}
	@Test
	public void test_getProximaSemana(){
		Date d1, d2;
		assertNull("TESTE ESPERAVA NULL", DBSDate.getNextWeek(wConexao, DBSDate.toDate("01102011"), 10, 1, false));
		assertNull("TESTE ESPERAVA NULL", DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 10, 0, false));
		assertNull("TESTE ESPERAVA NULL", DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 10, 8, false));
		assertNull("TESTE ESPERAVA NULL", DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 10, 8, false));
		assertEquals(DBSDate.toDate("16/10/2011"), DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 10, 1, false));
		d1 = DBSDate.toDate("24/10/2011");
		d2 = DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 15, 7, true);
		assertEquals(d1.toString(), d2.toString());
		d1 = DBSDate.toDate("21/10/2011");
		d2 = DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 15, 6, false);
		assertEquals(d1.toString(), d2.toString());
		d1 = DBSDate.toDate("21/10/2011");
		d2 = DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 15, 6, true);
		assertEquals(d1.toString(), d2.toString());
		assertEquals(DBSDate.toDate("24/10/2011"), DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 20, 1, true));
		assertEquals(DBSDate.toDate("09/09/2011"), DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), -20, 1, true));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 0, 1, true));
		assertEquals(DBSDate.toDate("02/10/2011"), DBSDate.getNextWeek(wConexao, DBSDate.toDate("01/10/2011"), 0, 1, false));
	}
	
	@Test
	public void test_getProximoAniversario(){
		assertNull("TESTE ESPERAVA NULL", DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("01102011"), 10, PERIODICIDADE.MENSAL, false,-1));
		assertEquals(DBSDate.toDate("01/10/2011"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("01/10/2011"), 0, PERIODICIDADE.MENSAL, false));
		assertEquals(DBSDate.toDate("02/10/2011"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.DIARIA, false));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.DIARIA, true));
		assertEquals(DBSDate.toDate("02/10/2011"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.DIARIA, false));
		assertEquals(DBSDate.toDate("01/11/2011"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.MENSAL, false));
		assertEquals(DBSDate.toDate("01/11/2011"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.MENSAL, true));
		assertEquals(DBSDate.toDate("01/01/2012"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("01/10/2011"), 3, PERIODICIDADE.MENSAL, false));
		assertEquals(DBSDate.toDate("02/01/2012"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("01/10/2011"), 3, PERIODICIDADE.MENSAL, true));
		assertEquals(DBSDate.toDate("12/10/2011"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("12/09/2011"), 1, PERIODICIDADE.MENSAL, false));
		assertEquals(DBSDate.toDate("13/10/2011"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("12/09/2011"), 1, PERIODICIDADE.MENSAL, true));
		assertEquals(DBSDate.toDate("28/02/2013"), DBSDate.getNextAnniversary(wConexao, DBSDate.toDate("29/02/2012"), 1, PERIODICIDADE.ANUAL, true));
	}
	@Test
	public void test_getVencimento(){
		Date d1,d2;
		assertNull("TESTE ESPERAVA NULL", DBSDate.getDueDate(wConexao, 1, DBSDate.toDate("01102011"), PERIODICIDADE.MENSAL, 0, false, "RF"));
		assertEquals(DBSDate.toDate("01/10/2011"), DBSDate.getDueDate(wConexao, 1, DBSDate.toDate("01/10/2011"), PERIODICIDADE.DIARIA, 10, false, "RF"));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getDueDate(wConexao, 1, DBSDate.toDate("01/10/2011"), PERIODICIDADE.DIARIA, 10, true, "RF"));
		assertEquals(DBSDate.toDate("11/10/2011"), DBSDate.getDueDate(wConexao, 2, DBSDate.toDate("01/10/2011"), PERIODICIDADE.DIARIA, 10, true, "RF"));
		assertEquals(DBSDate.toDate("01/08/2012"), DBSDate.getDueDate(wConexao, 2, DBSDate.toDate("01/10/2011"), PERIODICIDADE.MENSAL, 10, true, "RF"));
		assertEquals(DBSDate.toDate("01/08/2012"), DBSDate.getDueDate(wConexao, 2, DBSDate.toDate("01/10/2011"), PERIODICIDADE.MENSAL, 10, false, "RF"));
		d1 = DBSDate.toDate("12/08/2011");
		d2 = DBSDate.getDueDate(wConexao, 2, DBSDate.toDate("12/10/2010"), PERIODICIDADE.MENSAL, 10, false, "RF");
		assertEquals(d1.toString(), d2.toString());
		assertEquals(DBSDate.toDate("12/08/2011"), DBSDate.getDueDate(wConexao, 2, DBSDate.toDate("12/10/2010"), PERIODICIDADE.MENSAL, 10, true, "RF"));
		assertEquals(DBSDate.toDate("12/10/2011"), DBSDate.getDueDate(wConexao, 2, DBSDate.toDate("12/10/2010"), PERIODICIDADE.ANUAL, 1, false, "RF"));
		assertEquals(DBSDate.toDate("13/10/2011"), DBSDate.getDueDate(wConexao, 2, DBSDate.toDate("12/10/2010"), PERIODICIDADE.ANUAL, 1, true, "RF"));
	}

	@Test
	public void teste_getDiasDoMes() {
		//DIAS UTEIS DO MES DE DEZEMBRO DE 2012 - RF
		int xDias = DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("01/12/2012"), true, -1, "RF");
		assertEquals(20, xDias);
		
		//DIAS UTEIS DO MES DE DEZEMBRO DE 2012
		xDias = DBSDate.getDaysOfTheMonth(wConexao, DBSDate.toDate("01/12/2012"), true, -1);
		assertEquals(20, xDias);
	}

}
