package br.com.dbsoft.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.util.DBSDate.PERIODICIDADE;

/**
 * @author ricardo.villar
 *
 */
public class TstDBSDate extends TestCase {

	String wUrl="jdbc:oracle:thin:@192.168.0.20:1521:XE";
	String wUser="dbsoft";
	String wPassword="dbs0ft";
	Connection wConexao;
	
	@Override
	@Before
	public void setUp() {
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
	public void test_getDias() throws ParseException{
		//DIAS CORRIDOS NO MES DE NOVEMBRO DE 2013
		int xDias = DBSDate.getDias(wConexao, DBSDate.toDate("14/01/2013"), DBSDate.toDate("07/03/2014"), false, -1);
		assertEquals(417, xDias);
		
		//DIAS ÚTEIS NO MES DE NOVEMBRO DE 2013
		xDias = DBSDate.getDias(wConexao, DBSDate.toDate("14/01/2013"), DBSDate.toDate("07/03/2014"), true, -1);
		assertEquals(289, xDias);
		
		xDias = DBSDate.getDias(wConexao, DBSDate.toDate("01/07/2000"), DBSDate.toDate("07/03/2014"), true, -1);
		assertEquals(3436, xDias);
		
		xDias = DBSDate.getDias(wConexao, DBSDate.toDate("15/12/2012"), DBSDate.toDate("15/01/2013"), true, -1, "RF");
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
		assertEquals(-1, DBSDate.getMeses(DBSDate.toDate("05/02/2011"), DBSDate.toDate("05/01/2011")));
		assertEquals(0, DBSDate.getMeses(DBSDate.toDate("05/01/2011"), DBSDate.toDate("05/01/2011")));
		assertEquals(1, DBSDate.getMeses(DBSDate.toDate("05/01/2011"), DBSDate.toDate("05/02/2011")));
		assertEquals(2, DBSDate.getMeses(DBSDate.toDate("05/11/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(3, DBSDate.getMeses(DBSDate.toDate("05/10/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(4, DBSDate.getMeses(DBSDate.toDate("05/09/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(5, DBSDate.getMeses(DBSDate.toDate("05/08/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(6, DBSDate.getMeses(DBSDate.toDate("05/07/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(7, DBSDate.getMeses(DBSDate.toDate("05/06/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(8, DBSDate.getMeses(DBSDate.toDate("05/05/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(9, DBSDate.getMeses(DBSDate.toDate("05/04/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(10, DBSDate.getMeses(DBSDate.toDate("05/03/2010"), DBSDate.toDate("05/01/2011")));
		assertEquals(11, DBSDate.getMeses(DBSDate.toDate("05/01/2011"), DBSDate.toDate("05/12/2011")));
		assertEquals(12, DBSDate.getMeses(DBSDate.toDate("05/01/2010"), DBSDate.toDate("05/01/2011")));
		//TESTANDO getMeses(Calendar pDataInicio, Calendar pDataFim)
		assertEquals(0, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/01/2011")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(1, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/01/2011")), DBSDate.toCalendar(DBSDate.toDate("05/02/2011"))));
		assertEquals(2, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/11/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(3, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/10/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(4, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/09/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(5, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/08/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(6, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/07/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(7, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/06/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(8, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/05/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(9, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/04/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(10, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/03/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(11, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/01/2011")), DBSDate.toCalendar(DBSDate.toDate("05/12/2011"))));
		assertEquals(12, DBSDate.getMeses(DBSDate.toCalendar(DBSDate.toDate("05/01/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
	}
	@Test
	public void test_getAnos(){
		//TESTANDO getAnos(Date pDataInicio, Date pDataFim)
		assertEquals(0, DBSDate.getAnos(DBSDate.toDate("05/01/2011"), DBSDate.toDate("05/01/2011")));
		assertEquals(1, DBSDate.getAnos(DBSDate.toDate("31/12/2011"), DBSDate.toDate("01/01/2012")));
		assertEquals(-1, DBSDate.getAnos(DBSDate.toDate("05/11/2010"), DBSDate.toDate("05/01/2009")));
		//TESTANDO getAnos(Calendar pDataInicio, Calendar pDataFim)
		assertEquals(0, DBSDate.getAnos(DBSDate.toCalendar(DBSDate.toDate("05/01/2011")), DBSDate.toCalendar(DBSDate.toDate("05/01/2011"))));
		assertEquals(1, DBSDate.getAnos(DBSDate.toCalendar(DBSDate.toDate("31/12/2011")), DBSDate.toCalendar(DBSDate.toDate("01/01/2012"))));
		assertEquals(-1, DBSDate.getAnos(DBSDate.toCalendar(DBSDate.toDate("05/11/2010")), DBSDate.toCalendar(DBSDate.toDate("05/01/2009"))));
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
		assertEquals(DBSDate.toDate("28/02/2013"), DBSDate.getDateAdd(DBSDate.toDate("31/10/2009"), 1216));
	}
	
	@Test
	public void test_isDiaUtil() {
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDiaUtil(wConexao, DBSDate.toDate("01/01/2006"), -1));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDiaUtil(wConexao, DBSDate.toDate("01/10/2011")));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDiaUtil(wConexao, DBSDate.toDate("02/10/2011")));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDiaUtil(wConexao, DBSDate.toDate("12/10/2011")));
		assertFalse("TESTE ESPERAVA FALSE", DBSDate.isDiaUtil(wConexao, DBSDate.toDate("15/11/2011")));
		
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDiaUtil(wConexao, DBSDate.toDate("14/11/2011")));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDiaUtil(wConexao, DBSDate.toDate("10/10/2011")));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDiaUtil(wConexao, DBSDate.toDate("11/10/2011")));
		assertTrue("TESTE ESPERAVA TRUE", DBSDate.isDiaUtil(wConexao, DBSDate.toDate("13/10/2011")));
	}
	@Test
	public void test_getFeriados() {
		int xFeriados = DBSDate.getFeriados(wConexao, DBSDate.toDate("01/01/2011"), DBSDate.toDate("01/01/2012"), -1);
		assertEquals(9, xFeriados);//12
		
		xFeriados = DBSDate.getFeriados(wConexao, DBSDate.toDate("01/01/2011"), DBSDate.toDate("01/01/2013"), -1);
		assertEquals(22, xFeriados);//26
		
		xFeriados = DBSDate.getFeriados(wConexao, DBSDate.toDate("01/01/1999"), DBSDate.toDate("01/01/2013"), -1);
		assertEquals(139, xFeriados);//145
		
	}
	@Test
	public void test_getFinaisDeSemana(){
		assertEquals(2, DBSDate.getFinaisDeSemana(DBSDate.toDate("07/10/2011"), DBSDate.toDate("10/10/2011")));
		assertEquals(1462, DBSDate.getFinaisDeSemana(DBSDate.toDate("01/01/1999"), DBSDate.toDate("01/01/2013")));
		assertEquals(1173, DBSDate.getFinaisDeSemana(DBSDate.toDate("15/07/2000"), DBSDate.toDate("12/10/2011")));
		assertEquals(1170, DBSDate.getFinaisDeSemana(DBSDate.toDate("15/07/2000"), DBSDate.toDate("01/10/2011")));
	}
	@Test
	public void test_getDiasDoAno() throws ParseException{
		assertEquals(251, DBSDate.getDiasDoAno(wConexao, 2011, true, -1));
		assertEquals(251, DBSDate.getDiasDoAno(wConexao, 2011, true));
		assertEquals(365, DBSDate.getDiasDoAno(wConexao, 2011, false));
		assertEquals(250, DBSDate.getDiasDoAno(wConexao, 2009, true));
	}
	@Test
	public void test_getProximaData(){
		Date d1, d2;
		//assertEquals(DBSDate.toDate("01/02/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("01/01/2011"), 1200, true, -1));
		assertEquals(DBSDate.toDate("01/02/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("01/01/2011"), 31, false, -1));
		assertEquals(DBSDate.toDate("14/02/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("01/01/2011"), 31, true, -1));
		assertEquals(DBSDate.toDate("14/01/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("14/02/2011"), -31, false, -1));
		assertEquals(DBSDate.toDate("31/12/2010"), DBSDate.getProximaData(wConexao, DBSDate.toDate("14/02/2011"), -31, true, -1));
		assertEquals(DBSDate.toDate("03/01/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("15/02/2011"), -31, true, -1));
		assertEquals(DBSDate.toDate("13/10/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("01/10/2011"), 8, true, -1));
		assertEquals(DBSDate.toDate("14/10/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("01/10/2011"), 9, true, -1));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("01/10/2011"), 0, true, -1));
		assertEquals(DBSDate.toDate("01/10/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("01/10/2011"), 0, false, -1));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("03/10/2011"), 0, false, -1));
		d1 = DBSDate.getProximaData(wConexao, DBSDate.toDate("01/10/2011"), 10, true, -1);
		d2 = DBSDate.toDate("17/10/2011");
		assertEquals(d1.toString(), d2.toString());
		assertEquals(DBSDate.toDate("19/10/2011"), DBSDate.getProximaData(wConexao, DBSDate.toDate("18/10/2011"), 1, false));
	}
	@Test
	public void test_getNomeDaSemana(){
		assertEquals("", DBSDate.getNomeDaSemana(null));
		assertEquals("", DBSDate.getNomeDaSemana(DBSDate.toDate("29/02/2011")));
		assertEquals("Domingo", DBSDate.getNomeDaSemana(DBSDate.toDate("02/10/2011")));
		assertEquals("Segunda-feira", DBSDate.getNomeDaSemana(DBSDate.toDate("03/10/2011")));
		assertEquals("Terça-feira", DBSDate.getNomeDaSemana(DBSDate.toDate("04/01/2011")));
		assertEquals("Quarta-feira", DBSDate.getNomeDaSemana(DBSDate.toDate("05/01/2011")));
		assertEquals("Quinta-feira", DBSDate.getNomeDaSemana(DBSDate.toDate("06/01/2011")));
		assertEquals("Sexta-feira", DBSDate.getNomeDaSemana(DBSDate.toDate("07/01/2011")));
		assertEquals("Sábado", DBSDate.getNomeDaSemana(DBSDate.toDate("08/01/2011")));
	}
	@Test
	public void test_getNomeDoMes(){
		assertEquals("", DBSDate.getNomeDoMes(DBSDate.toDate("01/13/2011")));
		assertEquals("Janeiro", DBSDate.getNomeDoMes(DBSDate.toDate("01/01/2011")));
		assertEquals("Fevereiro", DBSDate.getNomeDoMes(DBSDate.toDate("01/02/2011")));
		assertEquals("Março", DBSDate.getNomeDoMes(DBSDate.toDate("01/03/2011")));
		assertEquals("Abril", DBSDate.getNomeDoMes(DBSDate.toDate("01/04/2011")));
		assertEquals("Maio", DBSDate.getNomeDoMes(DBSDate.toDate("01/05/2011")));
		assertEquals("Junho", DBSDate.getNomeDoMes(DBSDate.toDate("01/06/2011")));
		assertEquals("Julho", DBSDate.getNomeDoMes(DBSDate.toDate("01/07/2011")));
		assertEquals("Agosto", DBSDate.getNomeDoMes(DBSDate.toDate("01/08/2011")));
		assertEquals("Setembro", DBSDate.getNomeDoMes(DBSDate.toDate("01/09/2011")));
		assertEquals("Outubro", DBSDate.getNomeDoMes(DBSDate.toDate("01/10/2011")));
		assertEquals("Novembro", DBSDate.getNomeDoMes(DBSDate.toDate("01/11/2011")));
		assertEquals("Dezembro", DBSDate.getNomeDoMes(DBSDate.toDate("01/12/2011")));
	}
	@Test
	public void test_getPrimeiroDiaDoMes(){
		assertEquals(null, DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("31012011"), false));
		assertEquals(DBSDate.toDate("01/01/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("31/01/2011"), false));
		assertEquals(DBSDate.toDate("03/01/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("31/01/2011"), true));
		assertEquals(DBSDate.toDate("01/02/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("21/02/2011"), false));
		assertEquals(DBSDate.toDate("01/02/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("21/02/2011"), true));
		assertEquals(DBSDate.toDate("01/03/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/03/2011"), false));
		assertEquals(DBSDate.toDate("01/03/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/03/2011"), true));
		assertEquals(DBSDate.toDate("01/04/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/04/2011"), false));
		assertEquals(DBSDate.toDate("01/04/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/04/2011"), true));
		assertEquals(DBSDate.toDate("01/05/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/05/2011"), false));
		assertEquals(DBSDate.toDate("02/05/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/05/2011"), true));
		assertEquals(DBSDate.toDate("01/06/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/06/2011"), false));
		assertEquals(DBSDate.toDate("01/06/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/06/2011"), true));
		assertEquals(DBSDate.toDate("01/07/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/07/2011"), false));
		assertEquals(DBSDate.toDate("01/07/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/07/2011"), true));
		assertEquals(DBSDate.toDate("01/08/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/08/2011"), false));
		assertEquals(DBSDate.toDate("01/08/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/08/2011"), true));
		assertEquals(DBSDate.toDate("01/09/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/09/2011"), false));
		assertEquals(DBSDate.toDate("01/09/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/09/2011"), true));
		assertEquals(DBSDate.toDate("01/10/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/10/2011"), false));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/10/2011"), true));
		assertEquals(DBSDate.toDate("01/11/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/11/2011"), false));
		assertEquals(DBSDate.toDate("01/11/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/11/2011"), true));
		assertEquals(DBSDate.toDate("01/12/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/12/2011"), false));
		assertEquals(DBSDate.toDate("01/12/2011"), DBSDate.getPrimeiroDiaDoMes(wConexao, DBSDate.toDate("15/12/2011"), true));
	}
	@Test
	public void test_getUltimoDiaDoMes(){
		assertEquals(null, DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("31012011"), false));
		assertEquals(DBSDate.toDate("31/01/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("31/01/2011"), false));
		assertEquals(DBSDate.toDate("31/01/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("31/01/2011"), true));
		assertEquals(DBSDate.toDate("28/02/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("01/02/2011"), false));
		assertEquals(DBSDate.toDate("28/02/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("01/02/2011"), true));
		assertEquals(DBSDate.toDate("31/03/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("31/03/2011"), false));
		assertEquals(DBSDate.toDate("31/03/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("31/03/2011"), true));
		assertEquals(DBSDate.toDate("30/04/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/04/2011"), false));
		assertEquals(DBSDate.toDate("29/04/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/04/2011"), true));
		assertEquals(DBSDate.toDate("31/05/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/05/2011"), false));
		assertEquals(DBSDate.toDate("31/05/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/05/2011"), true));
		assertEquals(DBSDate.toDate("30/06/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/06/2011"), false));
		assertEquals(DBSDate.toDate("30/06/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/06/2011"), true));
		assertEquals(DBSDate.toDate("31/07/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/07/2011"), false));
		assertEquals(DBSDate.toDate("29/07/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/07/2011"), true));
		assertEquals(DBSDate.toDate("31/08/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/08/2011"), false));
		assertEquals(DBSDate.toDate("31/08/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/08/2011"), true));
		assertEquals(DBSDate.toDate("30/09/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/09/2011"), false));
		assertEquals(DBSDate.toDate("30/09/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/09/2011"), true));
		assertEquals(DBSDate.toDate("31/10/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/10/2011"), false));
		assertEquals(DBSDate.toDate("31/10/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/10/2011"), true));
		assertEquals(DBSDate.toDate("30/11/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/11/2011"), false));
		assertEquals(DBSDate.toDate("30/11/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/11/2011"), true));
		assertEquals(DBSDate.toDate("31/12/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/12/2011"), false));
		assertEquals(DBSDate.toDate("30/12/2011"), DBSDate.getUltimoDiaDoMes(wConexao, DBSDate.toDate("15/12/2011"), true));
	}
	@Test
	public void test_getDiasDoMes() throws ParseException{
		assertEquals(31, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("31/01/2011"), false));
		assertEquals(21, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("31/01/2011"), true));
		assertEquals(28, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/02/2011"), false));
		assertEquals(20, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/02/2011"), true));
		assertEquals(31, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/03/2011"), false));
		assertEquals(21, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/03/2011"), true));
		assertEquals(30, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/04/2011"), false));
		assertEquals(19, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/04/2011"), true));
		assertEquals(31, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/05/2011"), false));
		assertEquals(22, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/05/2011"), true));
		assertEquals(30, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/06/2011"), false));
		assertEquals(21, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/06/2011"), true));
		assertEquals(31, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/07/2011"), false));
		assertEquals(21, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/07/2011"), true));
		assertEquals(31, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/08/2011"), false));
		assertEquals(23, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/08/2011"), true));
		assertEquals(30, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/09/2011"), false));
		assertEquals(21, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/09/2011"), true));
		assertEquals(31, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/10/2011"), false));
		assertEquals(20, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/10/2011"), true));
		assertEquals(30, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/11/2011"), false));
		assertEquals(20, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/11/2011"), true));
		assertEquals(31, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/12/2011"), false));
		assertEquals(22, DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("15/12/2011"), true));
	}
	@Test
	public void test_getUltimoDiaDoAno(){
		assertNull(DBSDate.getUltimoDiaDoAno(wConexao, DBSDate.toDate("31012011"), false));
		assertEquals(DBSDate.toDate("31/12/2011"), DBSDate.getUltimoDiaDoAno(wConexao, DBSDate.toDate("31/12/2011"), false));
		assertEquals(DBSDate.toDate("30/12/2011"), DBSDate.getUltimoDiaDoAno(wConexao, DBSDate.toDate("31/12/2011"), true));
		assertEquals(DBSDate.toDate("31/12/2011"), DBSDate.getUltimoDiaDoAno(wConexao, DBSDate.toDate("01/01/2011"), false));
		assertEquals(DBSDate.toDate("30/12/2011"), DBSDate.getUltimoDiaDoAno(wConexao, DBSDate.toDate("01/01/2011"), true));
		assertEquals(DBSDate.toDate("31/12/2012"), DBSDate.getUltimoDiaDoAno(wConexao, DBSDate.toDate("31/12/2012"), false));
		assertEquals(DBSDate.toDate("28/12/2012"), DBSDate.getUltimoDiaDoAno(wConexao, DBSDate.toDate("31/12/2012"), true));
	}
	@Test
	public void test_getPrimeiroDiaDoAno(){
		assertNull(DBSDate.getPrimeiroDiaDoAno(wConexao, DBSDate.toDate("31012011"), false));
		assertEquals(DBSDate.toDate("01/01/2011"), DBSDate.getPrimeiroDiaDoAno(wConexao, DBSDate.toDate("31/12/2011"), false));
		assertEquals(DBSDate.toDate("03/01/2011"), DBSDate.getPrimeiroDiaDoAno(wConexao, DBSDate.toDate("31/12/2011"), true));
		assertEquals(DBSDate.toDate("01/01/2011"), DBSDate.getPrimeiroDiaDoAno(wConexao, DBSDate.toDate("01/01/2011"), false));
		assertEquals(DBSDate.toDate("03/01/2011"), DBSDate.getPrimeiroDiaDoAno(wConexao, DBSDate.toDate("01/01/2011"), true));
		assertEquals(DBSDate.toDate("01/01/2012"), DBSDate.getPrimeiroDiaDoAno(wConexao, DBSDate.toDate("31/12/2012"), false));
		assertEquals(DBSDate.toDate("02/01/2012"), DBSDate.getPrimeiroDiaDoAno(wConexao, DBSDate.toDate("31/12/2012"), true));
	}
	@Test
	public void test_getNumeroDoMes(){
		assertEquals(0, DBSDate.getNumeroDoMes(""));
		assertEquals(0, DBSDate.getNumeroDoMes("marco"));
		assertEquals(1, DBSDate.getNumeroDoMes("janeiro"));
		assertEquals(2, DBSDate.getNumeroDoMes("fevereiro"));
		assertEquals(3, DBSDate.getNumeroDoMes("março"));
		assertEquals(4, DBSDate.getNumeroDoMes("abril"));
		assertEquals(5, DBSDate.getNumeroDoMes("maio"));
		assertEquals(6, DBSDate.getNumeroDoMes("junho"));
		assertEquals(7, DBSDate.getNumeroDoMes("julho"));
		assertEquals(8, DBSDate.getNumeroDoMes("agosto"));
		assertEquals(9, DBSDate.getNumeroDoMes("setembro"));
		assertEquals(10, DBSDate.getNumeroDoMes("outubro"));
		assertEquals(11, DBSDate.getNumeroDoMes("novembro"));
		assertEquals(12, DBSDate.getNumeroDoMes("dezembro"));
	}
	@Test
	public void test_getProximaSemana(){
		Date d1, d2;
		assertNull("TESTE ESPERAVA NULL", DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01102011"), 10, 1, false));
		assertNull("TESTE ESPERAVA NULL", DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 10, 0, false));
		assertNull("TESTE ESPERAVA NULL", DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 10, 8, false));
		assertNull("TESTE ESPERAVA NULL", DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 10, 8, false));
		assertEquals(DBSDate.toDate("16/10/2011"), DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 10, 1, false));
		d1 = DBSDate.toDate("24/10/2011");
		d2 = DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 15, 7, true);
		assertEquals(d1.toString(), d2.toString());
		d1 = DBSDate.toDate("21/10/2011");
		d2 = DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 15, 6, false);
		assertEquals(d1.toString(), d2.toString());
		d1 = DBSDate.toDate("21/10/2011");
		d2 = DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 15, 6, true);
		assertEquals(d1.toString(), d2.toString());
		assertEquals(DBSDate.toDate("24/10/2011"), DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 20, 1, true));
		assertEquals(DBSDate.toDate("09/09/2011"), DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), -20, 1, true));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 0, 1, true));
		assertEquals(DBSDate.toDate("02/10/2011"), DBSDate.getProximaSemana(wConexao, DBSDate.toDate("01/10/2011"), 0, 1, false));
	}
	@Test
	public void test_getProximoAniversario(){
		assertNull("TESTE ESPERAVA NULL", DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("01102011"), 10, PERIODICIDADE.MENSAL, false,-1));
		assertEquals(DBSDate.toDate("01/10/2011"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("01/10/2011"), 0, PERIODICIDADE.MENSAL, false));
		assertEquals(DBSDate.toDate("02/10/2011"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.DIARIA, false));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.DIARIA, true));
		assertEquals(DBSDate.toDate("02/10/2011"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.DIARIA, false));
		assertEquals(DBSDate.toDate("01/11/2011"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.MENSAL, false));
		assertEquals(DBSDate.toDate("01/11/2011"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("01/10/2011"), 1, PERIODICIDADE.MENSAL, true));
		assertEquals(DBSDate.toDate("01/01/2012"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("01/10/2011"), 3, PERIODICIDADE.MENSAL, false));
		assertEquals(DBSDate.toDate("02/01/2012"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("01/10/2011"), 3, PERIODICIDADE.MENSAL, true));
		assertEquals(DBSDate.toDate("12/10/2011"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("12/09/2011"), 1, PERIODICIDADE.MENSAL, false));
		assertEquals(DBSDate.toDate("13/10/2011"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("12/09/2011"), 1, PERIODICIDADE.MENSAL, true));
		assertEquals(DBSDate.toDate("28/02/2013"), DBSDate.getProximoAniversario(wConexao, DBSDate.toDate("29/02/2012"), 1, PERIODICIDADE.ANUAL, true));
	}
	@Test
	public void test_getVencimento(){
		Date d1,d2;
		assertNull("TESTE ESPERAVA NULL", DBSDate.getVencimento(wConexao, 1, DBSDate.toDate("01102011"), PERIODICIDADE.MENSAL, 0, false, "RF"));
		assertEquals(DBSDate.toDate("01/10/2011"), DBSDate.getVencimento(wConexao, 1, DBSDate.toDate("01/10/2011"), PERIODICIDADE.DIARIA, 10, false, "RF"));
		assertEquals(DBSDate.toDate("03/10/2011"), DBSDate.getVencimento(wConexao, 1, DBSDate.toDate("01/10/2011"), PERIODICIDADE.DIARIA, 10, true, "RF"));
		assertEquals(DBSDate.toDate("11/10/2011"), DBSDate.getVencimento(wConexao, 2, DBSDate.toDate("01/10/2011"), PERIODICIDADE.DIARIA, 10, true, "RF"));
		assertEquals(DBSDate.toDate("01/08/2012"), DBSDate.getVencimento(wConexao, 2, DBSDate.toDate("01/10/2011"), PERIODICIDADE.MENSAL, 10, true, "RF"));
		assertEquals(DBSDate.toDate("01/08/2012"), DBSDate.getVencimento(wConexao, 2, DBSDate.toDate("01/10/2011"), PERIODICIDADE.MENSAL, 10, false, "RF"));
		d1 = DBSDate.toDate("12/08/2011");
		d2 = DBSDate.getVencimento(wConexao, 2, DBSDate.toDate("12/10/2010"), PERIODICIDADE.MENSAL, 10, false, "RF");
		assertEquals(d1.toString(), d2.toString());
		assertEquals(DBSDate.toDate("12/08/2011"), DBSDate.getVencimento(wConexao, 2, DBSDate.toDate("12/10/2010"), PERIODICIDADE.MENSAL, 10, true, "RF"));
		assertEquals(DBSDate.toDate("12/10/2011"), DBSDate.getVencimento(wConexao, 2, DBSDate.toDate("12/10/2010"), PERIODICIDADE.ANUAL, 1, false, "RF"));
		assertEquals(DBSDate.toDate("13/10/2011"), DBSDate.getVencimento(wConexao, 2, DBSDate.toDate("12/10/2010"), PERIODICIDADE.ANUAL, 1, true, "RF"));
	}

	@Test
	public void teste_getDiasDoMes() {
		//DIAS UTEIS DO MES DE DEZEMBRO DE 2012 - RF
		int xDias = DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("01/12/2012"), true, -1, "RF");
		assertEquals(20, xDias);
		
		//DIAS UTEIS DO MES DE DEZEMBRO DE 2012
		xDias = DBSDate.getDiasDoMes(wConexao, DBSDate.toDate("01/12/2012"), true, -1);
		assertEquals(18, xDias);
	}
}
