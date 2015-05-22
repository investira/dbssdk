package br.com.dbsoft.util;

import junit.framework.TestCase;

import org.junit.Test;

import br.com.dbsoft.util.DBSFormat.NUMBER_SIGN;


public class TstFormat extends TestCase {

	@Test
	public void test_phone(){
		String xStr;
		xStr = "AAA";
		assertEquals(null, DBSFormat.getPhoneNumber(xStr));
		xStr = "";
		assertEquals(null, DBSFormat.getPhoneNumber(xStr));
		xStr = " ";
		assertEquals(null, DBSFormat.getPhoneNumber(xStr));
		xStr = " A ";
		assertEquals(null, DBSFormat.getPhoneNumber(xStr));
		xStr = "+12125168899";
		assertEquals("12-12516-889", DBSFormat.getPhoneNumber(xStr));
		xStr = "0800 1234567";
		assertEquals("0-800-123-4567", DBSFormat.getPhoneNumber(xStr));
		xStr = "08001234567";
		assertEquals(null, DBSFormat.getPhoneNumber(xStr));
		xStr = "+00102125168899";
		assertEquals(null, DBSFormat.getPhoneNumber(xStr));
		xStr = "0215168899";
		assertEquals(null, DBSFormat.getPhoneNumber(xStr));
		xStr = "+102125168899";
		assertEquals("102-12516-889", DBSFormat.getPhoneNumber(xStr));
		xStr = "+5521987651234";
		assertEquals("+5-521-98765-1234", DBSFormat.getPhoneNumber(xStr));
		xStr = "021987651234";
		assertEquals("21-98765-1234", DBSFormat.getPhoneNumber(xStr));
		xStr = "21 987651234";
		assertEquals("21-98765-1234", DBSFormat.getPhoneNumber(xStr));
		xStr = "+55 021 987651234";
		assertEquals("+55-21-98765-1234", DBSFormat.getPhoneNumber(xStr));
		xStr = "21-987651234";
		assertEquals("21-98765-1234", DBSFormat.getPhoneNumber(xStr));
		xStr = "(21)987651234";
		assertEquals("21-98765-123", DBSFormat.getPhoneNumber(xStr));
		xStr = "+55 (021) 516--8899";
		assertEquals("+55-21-516-8899", DBSFormat.getPhoneNumber(xStr));
		xStr = "02125168899";
		assertEquals(null, DBSFormat.getPhoneNumber(xStr));
		xStr = "2125168899";
		assertEquals(null, DBSFormat.getPhoneNumber(xStr));
		xStr = "21 25168899";
		assertEquals("21-2516-8899", DBSFormat.getPhoneNumber(xStr));
		xStr = "21-5168899";
		assertEquals("21-516-8899", DBSFormat.getPhoneNumber(xStr));
		xStr = "021 5168899";
		assertEquals("21-516-8899", DBSFormat.getPhoneNumber(xStr));
		
	}
	
	@Test
	public void test_isDate() {
		assertEquals("01/02/2003 04:05:06", DBSFormat.getFormattedDateCustom(DBSDate.toDateDMYHMS("01/02/2003 04:05:06"), "dd/MM/yyyy HH:mm:ss"));
		assertEquals("01/02/2003 04:05", DBSFormat.getFormattedDateCustom(DBSDate.toDateDMYHMS("01/02/2003 04:05:06"), "dd/MM/yyyy HH:mm"));
		assertEquals("01/02/2003 04", DBSFormat.getFormattedDateCustom(DBSDate.toDateDMYHMS("01/02/2003 04:05:06"), "dd/MM/yyyy HH"));
		assertEquals("01/02/2003", DBSFormat.getFormattedDateCustom(DBSDate.toDateDMYHMS("01/02/2003 04:05:06"), "dd/MM/yyyy"));
		assertEquals("01/02/2003", DBSFormat.getFormattedDate(DBSDate.toDateDMYHMS("01/02/2003 04:05:06")));
		assertEquals("04:05", DBSFormat.getFormattedDateCustom(DBSDate.toDateDMYHMS("01/02/2003 04:05:06"), "HH:mm"));
		assertEquals("04:05:06", DBSFormat.getFormattedTime(DBSDate.toDateDMYHMS("01/02/2003 04:05:06")));
	}
	
	@Test
	public void test_numberDecimal(){
		assertEquals("1.234.567,12", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(2)));
		assertEquals("1.234.567,12", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(2)));
		assertEquals("1.234.567", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(0)));
		
		assertEquals("CR 1.234.567", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.CRDB_PREFIX,  DBSFormat.getNumberMask(0)));
		assertEquals("1.234.567 CR", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(0)));
		assertEquals("1.234.567", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.MINUS_PREFIX, DBSFormat.getNumberMask(0)));
		assertEquals("1.234.567", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.MINUS_SUFFIX, DBSFormat.getNumberMask(0)));
		assertEquals("1.234.567", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(0)));
		assertEquals("1.234.567", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.PARENTHESES, DBSFormat.getNumberMask(0)));
		assertEquals("DB 1.234.567", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.CRDB_PREFIX, DBSFormat.getNumberMask(0)));
		assertEquals("1.234.567 DB", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(0)));
		assertEquals("-1.234.567", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.MINUS_PREFIX, DBSFormat.getNumberMask(0)));
		assertEquals("1.234.567-", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.MINUS_SUFFIX, DBSFormat.getNumberMask(0)));
		assertEquals("1.234.567", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(0)));
		assertEquals("(1.234.567,123457)", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.PARENTHESES, DBSFormat.getNumberMask(6)));
		assertEquals("CR 1.234.567,123457", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.CRDB_PREFIX,  DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123457 CR", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123457", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.MINUS_PREFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123457", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.MINUS_SUFFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123457", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123457", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.PARENTHESES, DBSFormat.getNumberMask(6)));
		assertEquals("DB 1.234.567,123457", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.CRDB_PREFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123457 DB", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(6)));
		assertEquals("-1.234.567,123457", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.MINUS_PREFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123457-", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.MINUS_SUFFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123457", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(6)));
		assertEquals("(1.234.567,123457)", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.PARENTHESES, DBSFormat.getNumberMask(6)));		
	}
	
	@Test
	public void test_getFormattedNumber() {
		
		double xxNumero = 2340;
		assertEquals("2.340,00", DBSFormat.getFormattedNumber(xxNumero, 2) );
	    xxNumero = 2340;
		assertEquals("2.340", DBSFormat.getFormattedNumber(xxNumero, 0) );
		xxNumero = 1234543456.34;
		assertEquals("1.234.543.456,34", DBSFormat.getFormattedNumber(xxNumero, 2) );
		xxNumero = .34;
		assertEquals("0,34", DBSFormat.getFormattedNumber(xxNumero, 2) );
		xxNumero = 2340;
		assertEquals("2.340,00", DBSFormat.getFormattedNumber(xxNumero, 2) );
		xxNumero = 29340.23;
		assertEquals("29.340,23", DBSFormat.getFormattedNumber(xxNumero, 2) );
		xxNumero = 29340.1999;
		assertEquals("29.340,20", DBSFormat.getFormattedNumber(xxNumero, 2) );
		xxNumero = 29340.2376654565656;
		assertEquals("29.340,23767", DBSFormat.getFormattedNumber(xxNumero, 5) );
		xxNumero = 29340.2376654565656;
		assertEquals("29.340,23767 CR", DBSFormat.getFormattedNumber(xxNumero, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(5)) );
		xxNumero = 29340.277;
		assertEquals("29.340,28 CR", DBSFormat.getFormattedNumber(xxNumero, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(2)) );
		xxNumero = 29340.277;
		assertEquals("29.340,28", DBSFormat.getFormattedNumber(xxNumero, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(2,true,0)) );
		xxNumero = 29340.277;
		assertEquals("29.340,28", DBSFormat.getFormattedNumber(xxNumero, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(2,true,-1)) );
		
	}

	@Test
	public void test_mask(){
		assertEquals("123.456.7  . ",DBSFormat.getFormattedMask("1/2345.67","AAA.AAA.AAA.A"," "));
		assertEquals("123.X45.67 . ",DBSFormat.getFormattedMask("1/2345.67","AAA.XAA.AAA.A"," "));
		assertEquals("12B.345.67 . ",DBSFormat.getFormattedMask("1/2345.67","AAB.AAA.AAA.A"," "));
		assertEquals("123/456.7  . ",DBSFormat.getFormattedMask("1/2345.67","A9A/AAA.AAA.A"," "));
		assertEquals("123/456.789.0",DBSFormat.getFormattedMask("1/2345.6789.0123456","A9A/AAA.AAA.A"," "));
		assertEquals("123.456.789/01",DBSFormat.getFormattedMask("1/2345.6789.0123456","999.999.999/99"," "));
		assertEquals("123.456.789/0123-45",DBSFormat.getFormattedMask("1/2345.6789.0123456","999.999.999/9999-99"," "));
		assertEquals("123/456.700.0",DBSFormat.getFormattedMask("1/2345.67","A9A/AAA.AAA.A","0"));
		//System.out.println(DBSString.isAlphabetic("Awsw"));
		//MaskFormatter mk = new MaskFormatter("##/##/####");
	}
	

}
