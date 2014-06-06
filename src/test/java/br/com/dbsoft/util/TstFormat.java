package br.com.dbsoft.util;

import junit.framework.TestCase;

import org.junit.Test;

import br.com.dbsoft.util.DBSFormat.NUMBER_SIGN;

public class TstFormat extends TestCase {

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
		assertEquals("1.234.567,12", DBSFormat.getFormattedNumber(1234567.1234567, DBSFormat.getNumberMask(2)));
		assertEquals("1.234.567,12", DBSFormat.getFormattedNumber(1234567.1234567, DBSFormat.getNumberMask(2)));
		assertEquals("1.234.567", DBSFormat.getFormattedNumber(1234567.1234567, DBSFormat.getNumberMask(0)));
		
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
		assertEquals("(1.234.567,123456)", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.PARENTHESES, DBSFormat.getNumberMask(6)));
		assertEquals("CR 1.234.567,123456", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.CRDB_PREFIX,  DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123456 CR", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123456", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.MINUS_PREFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123456", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.MINUS_SUFFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123456", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123456", DBSFormat.getFormattedNumber(1234567.1234567, NUMBER_SIGN.PARENTHESES, DBSFormat.getNumberMask(6)));
		assertEquals("DB 1.234.567,123456", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.CRDB_PREFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123456 DB", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(6)));
		assertEquals("-1.234.567,123456", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.MINUS_PREFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123456-", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.MINUS_SUFFIX, DBSFormat.getNumberMask(6)));
		assertEquals("1.234.567,123456", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.NONE, DBSFormat.getNumberMask(6)));
		assertEquals("(1.234.567,123456)", DBSFormat.getFormattedNumber(-1234567.1234567, NUMBER_SIGN.PARENTHESES, DBSFormat.getNumberMask(6)));		
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
