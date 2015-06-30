package br.com.dbsoft.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.dbsoft.util.DBSNumber;

public class TstNumber {

	@Test
	public void test_toInteger(){

//		System.out.println(DBSNumber.trunc("0,00", 2).equals(BigDecimal.ZERO));
//		System.out.println(DBSNumber.trunc("1,00", 2).equals(BigDecimal.ONE));
//		System.out.println(DBSNumber.round("0,00", 2).equals(BigDecimal.ZERO));
//		System.out.println(DBSNumber.round("1,00", 2).equals(BigDecimal.ONE));

//		System.out.println(DBSNumber.trunc("0,00", 2).equals(DBSNumber.toBigDecimal(0)));
//		System.out.println(DBSNumber.trunc("0,00", 2).equals(BigDecimal.ZERO));
//		System.out.println(DBSNumber.trunc("0,00", 2).equals(0));
//		System.out.println(DBSNumber.trunc("0,00", 2).equals(0D));
//		System.out.println(DBSNumber.trunc("0,00", 2) == BigDecimal.ZERO);

		
		
//		System.out.println(DBSNumber.abs(1D).doubleValue() < -5 );
//		System.out.println(DBSNumber.abs(1D).doubleValue() > -5 );//<
//		System.out.println(DBSNumber.toDouble(10D).compareTo(20D)); //<
//		System.out.println(DBSNumber.toDouble(20D).compareTo(10D)); //>
//		System.out.println(DBSNumber.toDouble(10D).compareTo(10D)); //=
//		System.out.println(DBSNumber.toDouble(10D).compareTo(-10D)); //>
//		System.out.println(DBSNumber.toDouble(-10D).compareTo(10D)); //<
//		System.out.println(DBSNumber.toDouble(10D) >= 0D); //<
//		System.out.println(DBSNumber.toDouble(20D).compareTo(10D)); //>
//		System.out.println(DBSNumber.toDouble(10D).compareTo(10D)); //=
//		System.out.println(DBSNumber.toDouble(10D).compareTo(-10D)); //>
//		System.out.println(DBSNumber.toDouble(-10D).compareTo(10D)); //<
		assertEquals((Integer)12, DBSNumber.toInteger("12"));
		assertEquals((Integer)12, DBSNumber.toInteger("12,345"));
		assertEquals((Integer)0, DBSNumber.toInteger("0,345"));
		assertEquals((Integer)1234567890, DBSNumber.toInteger("1234567890,1234567890"));
		assertEquals((Integer)(-12), DBSNumber.toInteger("-12"));
		assertEquals((Integer)(-12), DBSNumber.toInteger("-12,345"));
		assertEquals((Integer)(-12), DBSNumber.toInteger(-12.345));
		assertEquals((Integer)0, DBSNumber.toInteger(-0.345));
		assertEquals((Integer)1234, DBSNumber.toInteger(1234.345));
	}

	@Test
	public void test_toDouble(){
		assertEquals((Double)12.0, DBSNumber.toDouble("12"));
		assertEquals((Double)12.345, DBSNumber.toDouble("12,345"));
		assertEquals((Double)0.345, DBSNumber.toDouble("0,345"));
		assertEquals((Double)1234567890.1234567890, DBSNumber.toDouble("1234567890,1234567890"));
		assertEquals((Double)(-12D), DBSNumber.toDouble("-12"));
		assertEquals((Double)(-12.345), DBSNumber.toDouble("-12,345"));
		assertEquals((Double)(-12.345), DBSNumber.toDouble(-12.345));
		assertEquals((Double)(-0.345), DBSNumber.toDouble(-0.345));
		assertEquals((Double)1234.345, DBSNumber.toDouble(1234.345));
	}

	@Test
	public void test_toBigDecimal() {
		assertEquals("0.1", DBSNumber.toBigDecimal("0,100").toString());
		assertEquals("12345678.0000000000000000000000000000002", DBSNumber.toBigDecimal("12345678,0000000000000000000000000000002").toString());
		assertEquals("12345678", DBSNumber.toBigDecimal("12345678,000000000000000000000000000000").toString());
		assertEquals(new BigDecimal(1), DBSNumber.toBigDecimal("1"));
		assertEquals(new BigDecimal("1.23"), DBSNumber.toBigDecimal("1,23"));
		assertEquals(new BigDecimal("-1.23"), DBSNumber.toBigDecimal("-1,23"));
		assertEquals(new BigDecimal(12D), DBSNumber.toBigDecimal(12D));
		assertEquals(new BigDecimal(-12D), DBSNumber.toBigDecimal(-12D));
		assertEquals(new BigDecimal("-12.345678"), DBSNumber.toBigDecimal("-12,345678"));
		assertEquals(new BigDecimal("-12.345678"), DBSNumber.toBigDecimal(-12.345678D));
		assertTrue(DBSNumber.toBigDecimal(1).doubleValue() == 1D);
		assertTrue(DBSNumber.toBigDecimal(12.3456).doubleValue() == 12.3456D);
		assertTrue(DBSNumber.toBigDecimal("12,3456").doubleValue() == 12.3456D);
	}


	@Test
	public void test_subtract() {
//		Double xD = 0.15;
//		Double yD = 0.13;
//		System.out.println(DBSNumber.subtract(xD, yD));
//		System.out.println(xD - yD);
//		System.out.println(DBSNumber.toBigDecimal(100D).equals(DBSNumber.toBigDecimal(100D)));
		assertEquals("0", DBSNumber.subtract(null, 0D).toString());
		assertEquals("0.02", DBSNumber.subtract(0.15, 0.13).toString());
		assertEquals("1585481", DBSNumber.subtract(1947652D, 362171D).toString());
		assertEquals("-80", DBSNumber.subtract( 20D,100D).toString());
		assertEquals("-100", DBSNumber.subtract( 0D,100D).toString());
		assertEquals("100", DBSNumber.subtract( 100D,0D).toString());
		assertEquals("-10", DBSNumber.subtract(null, 10D).toString());
		assertEquals("-10", DBSNumber.subtract(null, "10,00").toString());
		assertEquals("0", DBSNumber.subtract(null, 0D).toString());
		assertEquals("0", DBSNumber.subtract(0D, null).toString());
		assertEquals("0", DBSNumber.subtract(0D, null, 0).toString());
		assertEquals("100", DBSNumber.subtract(100D, null).toString());
		assertEquals("-100", DBSNumber.subtract(null, 100D).toString());		    
		assertEquals("0.1", DBSNumber.subtract( 1.40D,1.30D).toString());		    
		assertEquals("-0.1", DBSNumber.subtract( 1.20D,1.30D).toString());		    
		assertEquals("-0.1", DBSNumber.subtract( 1.20D,1.30D).toString());		    
		assertEquals("-71605453132", DBSNumber.subtract( "12233223332","83838676464").toString());		    
		assertEquals("-71605453132", DBSNumber.subtract( "12233223332,9090978877788","83838676464,9090978877788").toString());		    
		assertEquals("-71605453132.0000000000001", DBSNumber.subtract( "12233223332,9090978877787","83838676464,9090978877788").toString());
	}
	
	@Test
	public void test_add() {
		assertEquals("2", DBSNumber.add(1D, 1D).toString());
		assertEquals("60", DBSNumber.add(20D, 40D).toString());
		assertEquals("60", DBSNumber.add(40D, 20D).toString());
		assertEquals("20", DBSNumber.add(null, 20D).toString());
		assertEquals("30", DBSNumber.add(20D, null, 10D, null).toString());
		assertEquals("150000000", DBSNumber.add(100000000D, 50000000D).toString());
		assertEquals("1", DBSNumber.add(.99D, 0.01D).toString());
		assertEquals("4", DBSNumber.add(2D, 2D).toString());		
		assertEquals("2", DBSNumber.add(1.0000000000000000000001D, 1.0000000000000000000001D).toString());
		assertEquals("2.0000000000000000000002", DBSNumber.add("1,0000000000000000000001", "1,0000000000000000000001").toString());
		assertEquals("3.0000000000000000000002", DBSNumber.add("1,0000000000000000000001", "1,0000000000000000000001","1").toString());
		assertEquals("2.0000000000000000000002", DBSNumber.add("1,0000000000000000000001", "1,0000000000000000000001","1","-1").toString());
		assertEquals("2.0000000000000000000002", DBSNumber.add("1,0000000000000000000001", "1,0000000000000000000001","1",-1).toString());
		assertEquals("1", DBSNumber.add(1.0000000000000000000001D, null).toString());
	}
    
	@Test
	public void test_multiply() {
		assertEquals("2", DBSNumber.multiply(2D, 1D).toString());    	
		assertEquals("2", DBSNumber.multiply(2D, 1D).toString());    	
		assertEquals("-110", DBSNumber.multiply(100D, -1.1D).toString());
		assertEquals("890989.10901", DBSNumber.multiply(9889989D, 0.09009D).toString());
		assertEquals("100.001", DBSNumber.multiply(100, 1.0000100).toString());
		assertEquals("300", DBSNumber.multiply(150D, 2D).toString());
		assertEquals("-300", DBSNumber.multiply(-150D, 2D).toString());
		assertEquals("-300", DBSNumber.multiply(150D, -2D).toString());
		assertEquals("0", DBSNumber.multiply(987676662D, 1D, 0).toString());
		assertEquals("24", DBSNumber.multiply(2, 3, 4).toString());
		assertEquals("26.691", DBSNumber.multiply(2.1, 3.1, 4.1).toString());
		assertEquals("987676662", DBSNumber.multiply(1D,987676662D).toString());
		assertEquals("3.0000000000000000000003", DBSNumber.multiply("1,0000000000000000000001D", 3D).toString());
		assertEquals("2.0000000000000000000002", DBSNumber.multiply(2D, "1,0000000000000000000001D").toString());
    }

//	@Test
//	public void m2(){
//		BigDecimal xD1 = DBSNumber.toBigDecimal("1234567890,1234567890123456789");
//		BigDecimal xD2 = DBSNumber.toBigDecimal("1,12345678");
//		for (int xI=0; xI<2600; xI++){
//			xD1 =  xD1.multiply(xD2);
////			System.out.println(xD1);
//		}
//	}
//	
//	@Test
//	public void m1(){
//		BigDecimal xD1 = DBSNumber.toBigDecimal("1234567890,1234567890123456789");
//		BigDecimal xD2 = DBSNumber.toBigDecimal("1,12345678");
//		for (int xI=0; xI<2600; xI++){
//			xD1 = DBSNumber.multiply(xD1, xD2);
////			System.out.println(xD1);
//		}
//	}



    @Test
	public void test_divide() {
    	assertEquals("0.333333333333333333333333333333", DBSNumber.divide(1D, 3D).toString());
	    assertEquals("2", DBSNumber.divide(4D, 2D).toString());    	
		assertEquals("-2", DBSNumber.divide(-4D, 2D).toString());
		assertEquals("1.1", DBSNumber.divide(2.2D, 2D).toString());
		assertEquals("75", DBSNumber.divide(150D, 2D).toString());
		assertEquals("-3000", DBSNumber.divide(-6000D, 2D).toString());
		assertEquals("987676662", DBSNumber.divide(987676662D, 1D).toString());
		assertEquals("2.0000000000000000000002", DBSNumber.divide("4,0000000000000000000004", 2D).toString());
		assertEquals(null, DBSNumber.divide(999D, null));
		assertEquals("0", DBSNumber.divide(null, 9999D).toString());
		assertEquals("5", DBSNumber.divide(100, 2, 10).toString());
    }    

	@Test
	public void test_exp() {
	    assertEquals("16", DBSNumber.exp(4D, 2D).toString());    	
		assertEquals("16", DBSNumber.exp(-4D, 2D).toString());
		assertEquals("4.840000000000001", DBSNumber.exp(2.2D, 2D).toString()); //Problema de precisão não resolvido utilizando o Double
		assertEquals("506250000", DBSNumber.exp(150D, 4D).toString());
		assertEquals("81", DBSNumber.exp(9D, 2D).toString());
		assertEquals("0.001953125", DBSNumber.exp(2D, -9D).toString());
		assertEquals("987676662", DBSNumber.exp(987676662D, 1D).toString());
		assertEquals("16", DBSNumber.exp("4,0000000000000000000004", 2D).toString());
		assertEquals("16.783802388556726", DBSNumber.exp(4.04D, 2.02D).toString());	
		assertEquals("16", DBSNumber.exp("4,0000000000000000000004", 2D).toString()); //Precisão até 16 digitos. Supererior poderá ser ignorada
		assertEquals("16.000000000000036", DBSNumber.exp("4,000000000000004", 2D).toString());
		assertEquals("81.00000000000006", DBSNumber.exp("9,000000000000004", 2D).toString());
		assertEquals("9801", DBSNumber.exp("99,000000000000004", 2D).toString());
		assertEquals("1", DBSNumber.exp(999D, null).toString());
		assertEquals("0", DBSNumber.exp(null, 9999D).toString());		
		
	}
    
	@Test
	public void test_log() {
		
	    assertEquals("1.3862943611198906", DBSNumber.log(4D).toString());    	
	    assertEquals("2.995732273553991", DBSNumber.log(20D).toString());    	
	    assertEquals(null, DBSNumber.log(null));
	    
	}

	@Test
	public void test_sign() {
		assertEquals("1", DBSNumber.sign(90D).toString());
		assertEquals("1", DBSNumber.sign(4.0000000000000000000004D).toString());
		assertEquals("0", DBSNumber.sign(0D).toString());
		assertEquals("-1", DBSNumber.sign(-89D).toString());
		assertEquals("-1", DBSNumber.sign(-1002020D).toString());
		assertEquals("-1", DBSNumber.sign("-1234567890,123456789").toString());
		assertEquals("-1", DBSNumber.sign("-838383838383838383838992929292910000,9999999999888888888877666666766655").toString());
		assertEquals("0", DBSNumber.sign(null).toString());
	}

	@Test
	public void test_abs() {
//		Double xS = -10000D;
//		DBSNumber xF = new DBSNumber();
//		Double xD;
//		BigDecimal xB;
//		Long xL;
//		Integer xI;
//		xB = xF.abs(xS);
//		System.out.println(xB);
//		
//		xD = xF.abs(xS);
//		System.out.println(xD);
//		
//		xL = xF.abs(xS);
//		System.out.println(xL);
//		
//		xI = xF.abs(xS);
//		System.out.println(xI);
//		xD = DBSNumber.abs(-80000D);	
//		System.out.println(xD);
//		xD = DBSNumber.abs(-1.22112D);	
//		System.out.println(xD);
//		xD = DBSNumber.abs(12345.6789012345678901D);	
//		System.out.println(xD);
//		xD = DBSNumber.abs(-12345.6789012345678901D);
//		System.out.println(xD);
//		xD = DBSNumber.abs("-12345,6789012345678901");
//		System.out.println(xD);
//		xD = DBSNumber.abs("-838383838383838383838992929292910000,9999999999888888888877666666766655");
//		System.out.println(xD);
//		xD = DBSNumber.abs(null);
//		System.out.println(xD);
//		
		assertEquals("10000.0", DBSNumber.abs(-10000D).toString());
		assertEquals("80000.0", DBSNumber.abs(-80000D).toString());	
		assertEquals("1.22112", DBSNumber.abs(-1.22112D).toString());	
		assertEquals("12345.678901234567", DBSNumber.abs(12345.6789012345678901D).toString());	
		assertEquals("12345.678901234567", DBSNumber.abs(-12345.6789012345678901D).toString());
		assertEquals("12345.6789012345678901", DBSNumber.abs("-12345,6789012345678901").toString());
		assertEquals("838383838383838383838992929292910000.9999999999888888888877666666766655", DBSNumber.abs("-838383838383838383838992929292910000,9999999999888888888877666666766655").toString());
		assertEquals("0.0", DBSNumber.abs((Double) null).toString());
	}
	
	@Test
	public void test_trunc() {
		assertTrue(DBSNumber.trunc("0,00", 2).equals(BigDecimal.ZERO));
		assertTrue(DBSNumber.trunc("1,00", 2).equals(BigDecimal.ONE));
		assertTrue(DBSNumber.trunc("0,00", 2).equals(BigDecimal.ZERO));
		assertTrue(DBSNumber.trunc("1,00", 2).equals(BigDecimal.ONE));
        
		assertEquals("10000.0", DBSNumber.trunc(10000.998898989D, 0).toString());
		assertEquals("10000.88", DBSNumber.trunc(10000.8898989D, 2).toString());
		assertEquals("10000.889", DBSNumber.trunc(10000.8898989D, 3).toString());
		assertEquals("10000.8898", DBSNumber.trunc(10000.8898989D, 4).toString());
		assertEquals("10000.9999", DBSNumber.trunc(10000.99999D, 4).toString());
		assertEquals((Double)100001222D, DBSNumber.trunc(100001222.998898989D, 0));
		assertEquals("10000.9999999999", DBSNumber.trunc("10000,9999999999999999999888877878", 10).toString());
		assertEquals("838383838383838383838992929292910000.99999999998888888888", DBSNumber.trunc("838383838383838383838992929292910000,9999999999888888888877666666766655", 20).toString());
		assertEquals("838383838383838383838992929292910000.99", DBSNumber.trunc("838383838383838383838992929292910000,9999999999888888888877666666766655", 2).toString());
		assertEquals("1000.0",  DBSNumber.trunc(1000.987,null).toString());
		assertEquals("0.0",  DBSNumber.trunc((Double)null, 2).toString());
		assertEquals("0.0",  DBSNumber.trunc((Double)null, null).toString());
	}

	@Test
	public void test_round() {
		assertTrue(DBSNumber.round("0,00", 2).equals(BigDecimal.ZERO));
		assertTrue(DBSNumber.round("1,00", 2).equals(BigDecimal.ONE));
		assertFalse(DBSNumber.round("0,00", 2) == BigDecimal.ZERO);
		assertFalse(DBSNumber.round("1,00", 2) == BigDecimal.ONE);

		assertEquals("10001.0", DBSNumber.round(10000.998898989D,0).toString());
		assertEquals("10000.9989", DBSNumber.round(10000.998898989D, 4).toString());
		assertEquals("838383838383838383838992929292910001", DBSNumber.round("838383838383838383838992929292910000,9999999999888888888877666666766655", 2).toString());
		assertEquals("1.5", DBSNumber.round("1,45", 1).toString());
		assertEquals("1.4", DBSNumber.round("1,44", 1).toString());
		assertEquals("10001", DBSNumber.round("10000,9999999999999999999888877878", 2).toString());
		assertEquals("10000.8", DBSNumber.round("10000,8432129999999999999888877878", 1).toString());
		assertEquals("10000.9", DBSNumber.round("10000,9432129999999999999888877878", 1).toString());
		assertEquals("10001", DBSNumber.round("10000,8432129999999999999888877878", 0).toString());
		assertEquals("10001", DBSNumber.round("10000,9432129999999999999888877878", 0).toString());
		assertEquals("10000.8432", DBSNumber.round("10000,8432129999999999999888877878", 4).toString());
		assertEquals("123460", DBSNumber.round("123456,8432129999999999999888877878", -1).toString());
		assertEquals("123500", DBSNumber.round("123456,8432129999999999999888877878", -2).toString());
		assertEquals("166670", DBSNumber.round("166666,8432129999999999999888877878", -1).toString());
		assertEquals("200000", DBSNumber.round("199999,8432129999999999999888877878", -1).toString());
		assertEquals("0", DBSNumber.round("199999,8432129999999999999888877878", -10).toString());
		assertEquals("2", DBSNumber.round("1,9", 0).toString());
		assertEquals("-10000.8432", DBSNumber.round("-10000,8432129999999999999888877878", 4).toString());
		assertEquals("-123460", DBSNumber.round("-123456,8432129999999999999888877878", -1).toString());
		assertEquals("1001.0",  DBSNumber.round(1000.987,null).toString());
		assertEquals("0.0",  DBSNumber.round((Double)null, 2).toString());
		assertEquals("0.0",  DBSNumber.round((Double)null, null).toString());
	}
	
	@Test    
	public void test_inte() {
		assertEquals("10000.0", DBSNumber.inte(10000.998898989D).toString());
		assertEquals("10000.0", DBSNumber.inte(10000.989D).toString());
		assertEquals("0.0", DBSNumber.inte(0000.989D).toString());
		assertEquals((Double)100001222D, DBSNumber.inte(100001222.998898989D));
		assertEquals((Double)0D, DBSNumber.inte(0.998898989D));
		assertEquals("838383838383838383838992929292910000", DBSNumber.inte("838383838383838383838992929292910000,9999999999888888888877666666766655").toString());
		assertEquals("0", DBSNumber.inte("0000,9999999999888888888877666666766655").toString());
		assertEquals("0.0", DBSNumber.inte((Double)null).toString());
	}

	@Test
	public void test_isNumber() {
		
		assertEquals(true, DBSNumber.isNumber("10000.99889898"));
		assertEquals(false, DBSNumber.isNumber("10000.A9889898"));	
		assertEquals(true, DBSNumber.isNumber("8778278278728728728827287872872872810000.99889898"));
		assertEquals(false, DBSNumber.isNumber("j10000.A9889898"));	
		assertEquals(false, DBSNumber.isNumber("100,098,9830.99889898"));
		assertEquals(true, DBSNumber.isNumber("0.01"));
		assertEquals(true, DBSNumber.isNumber("3.047830632E7"));
		assertEquals(true, DBSNumber.isNumber("3.047830632e7"));
		assertEquals(true, DBSNumber.isNumber("3.047830632E+7"));
		assertEquals(false, DBSNumber.isNumber("e3.047830632E7"));
		assertEquals(false, DBSNumber.isNumber(null));
	
	}

	@Test
	public void test_toNumber() {
		
		assertEquals("10000.99", DBSNumber.toNumber("1000099",2).toString());
		assertEquals("1000.099", DBSNumber.toNumber("1000099",3).toString());
		assertEquals("100.0099", DBSNumber.toNumber("1000099",4).toString());
		assertEquals("10.00099", DBSNumber.toNumber("1000099",5).toString());
		assertEquals("1.000099", DBSNumber.toNumber("1000099",6).toString());
		assertEquals("0.1000099", DBSNumber.toNumber("1000099",7).toString());
		assertEquals(null, DBSNumber.toNumber(null,7));
		assertEquals(null, DBSNumber.toNumber("1000099",null));
	}

	@Test
	public void test_getOnlyNumber() {
		assertEquals("191850", DBSNumber.getOnlyNumber("SADAD1ASSDFF9a1SADASDASD8.5FASDFSSF0"));
		assertEquals("1918502424", DBSNumber.getOnlyNumber("SADAD1ASSDFF9a1SADASDASD8.5FASDFSSF02424HJGJH"));
		assertEquals("191850", DBSNumber.getOnlyNumber("DFGDGDFGSADAD1ASSDFF9a1SADASDASD8.5XCVXVXCXV!!###$$$%%%���$$$$$$FASDFSSF0"));
		assertEquals(null, DBSNumber.getOnlyNumber(null));
		assertEquals("9919185099", DBSNumber.getOnlyNumber("99SADAD1ASSDFF9a1SADASDASD8.5FASDFSSF0.,,,x..,.,.,,.99"));
	}
	
	@Test
	public void test_getNotZero() {
		assertEquals((Double)10D, DBSNumber.toDoubleNotZero(10D, 1000D));
		assertEquals((Double)1000D, DBSNumber.toDoubleNotZero(00D, 1000D));
		assertEquals((Double)1000D, DBSNumber.toDoubleNotZero(null, 1000D));
		assertEquals((Double)10D, DBSNumber.toDoubleNotZero(10D, null));
		assertEquals((Double)1D, DBSNumber.toDoubleNotZero(0D, null));
		assertEquals((Double)10D, DBSNumber.toDoubleNotZero(10D, 1000D));
		assertEquals((Double)10D, DBSNumber.toDoubleNotZero(10));
		assertEquals((Double)1D, DBSNumber.toDoubleNotZero(0));
		assertEquals((Double)1D, DBSNumber.toDoubleNotZero(null));
	}

	@Test
	public void test_isNumeric() {
		assertEquals(true, DBSNumber.isNumber("1"));
		assertEquals(true, DBSNumber.isNumber("1,34"));
		assertEquals(true, DBSNumber.isNumber("1.34"));
		assertEquals(false, DBSNumber.isNumber("111,222.22"));
		assertEquals(true, DBSNumber.isNumber("0000,00"));
		assertEquals(true, DBSNumber.isNumber("0000.00"));
		assertEquals(false, DBSNumber.isNumber("0.000,00"));
		assertEquals(false, DBSNumber.isNumber("0,000.00"));
		assertEquals(false, DBSNumber.isNumber("1A"));
	}
	@Test
	public void test_setSign() {
		assertEquals("-123.45678910123", DBSNumber.toPositive("123,45678910123", false).toString());
		assertEquals("-123.45678910123", DBSNumber.toPositive("-123,45678910123", false).toString());
		assertEquals("123.45678910123", DBSNumber.toPositive("123,45678910123", true).toString());
		assertEquals("123.45678910123", DBSNumber.toPositive("-123,45678910123", true).toString());
	}

	@Test
	public void test_average() {
		List<Double> 	xAmostra = new ArrayList<Double>(); 
		BigDecimal		xMedia;
		xAmostra.add(1D);
		xAmostra.add(1D);
		xAmostra.add(1D);
		xMedia = DBSNumber.average(xAmostra);
		assertEquals("1,00", DBSFormat.getFormattedNumber(xMedia, 2));
		
		xAmostra.add(1.5D);
		xAmostra.add(2D);
		xAmostra.add(1.25D);
		xAmostra.add(1.25D);
		xAmostra.add(2.5D);
		xAmostra.add(2.25D);
		xAmostra.add(2.25D);
		xMedia = DBSNumber.average(xAmostra);
		assertEquals("1,60", DBSFormat.getFormattedNumber(xMedia, 2));
	}
	
	@Test
	public void test_desvioPadrao() {
		List<Double> 	xAmostra = new ArrayList<Double>(); 
		BigDecimal		xDesvioPadrao;
		xAmostra.add(-1.24D);
		xAmostra.add(11.23D);
		xAmostra.add(8.08D);

		xDesvioPadrao = DBSNumber.desvioPadrao(xAmostra);
		assertEquals("6,48", DBSFormat.getFormattedNumber(xDesvioPadrao, 2));
		
		xAmostra.add(-2.94D);
		xAmostra.add(0.80D);
		xAmostra.add(-8.04D);
		xAmostra.add(9.05D);
		xAmostra.add(-5.53D);
		xAmostra.add(7.68D);
		xAmostra.add(9.88D);
		xAmostra.add(-10.44D);
		xAmostra.add(-8.48D);
		xAmostra.add(-6.43D);
		xAmostra.add(-11.03D);
		xAmostra.add(-24.80D);
		xAmostra.add(-1.77D);
		xAmostra.add(2.61D);
		xAmostra.add(4.66D);
		
		xDesvioPadrao = DBSNumber.desvioPadrao(xAmostra);
		assertEquals("9,35", DBSFormat.getFormattedNumber(xDesvioPadrao, 2));
		
		xAmostra.add(-2.84D);
		xAmostra.add(7.18D);
		xAmostra.add(15.55D);
		xAmostra.add(12.49D);
		xAmostra.add(-3.26D);
		xAmostra.add(6.41D);
		xAmostra.add(3.15D);
		xAmostra.add(8.90D);
		xAmostra.add(0.05D);
		xAmostra.add(8.94D);
		xAmostra.add(2.30D);
		xAmostra.add(-4.65D);
		xAmostra.add(1.68D);
		xAmostra.add(5.28D);
		xAmostra.add(-4.04D);
		xAmostra.add(-6.64D);
		xAmostra.add(-3.35D);
		xAmostra.add(10.80D);
		
		xDesvioPadrao = DBSNumber.desvioPadrao(xAmostra);
		assertEquals("8,32", DBSFormat.getFormattedNumber(xDesvioPadrao, 2));
	}
	
	@Test
	public void test_distribuicaoNormalInvertida() {
		Double xValor;
		xValor = DBSNumber.distribuicaoNormalInvertida(0.01D, 0D, 1D).doubleValue();
		assertEquals("-2,32635", DBSFormat.getFormattedNumber(xValor, 5));
		
		xValor = DBSNumber.distribuicaoNormalInvertida(0.05D, 0D, 1D).doubleValue();
		assertEquals("-1,64485", DBSFormat.getFormattedNumber(xValor, 5));
		
		xValor = DBSNumber.distribuicaoNormalInvertida(0.1D, 0D, 1D).doubleValue();
		assertEquals("-1,28155", DBSFormat.getFormattedNumber(xValor, 5));
	}
	
	@Test
	public void test_calculaVar() {
		List<Double> 	xAmostra = new ArrayList<Double>();
		List<Double>	xListaVariacao = new ArrayList<Double>();
		Double xMedia;
		Double xDesvioPadrao;
		Double xDistribuicao1;
		Double xDistribuicao5;
		Double xDistribuicao10;
		Double xVAR1;
		Double xVAR5;
		Double xVAR10;
		
		xAmostra.add(69962.32D);
		xAmostra.add(70317.79D);
		xAmostra.add(71091.03D);
		xAmostra.add(70578.83D);
		xAmostra.add(70057.20D);
		xAmostra.add(70127.04D);
		xAmostra.add(70423.44D);
		xAmostra.add(71632.90D);
		xAmostra.add(70721.44D);
		xAmostra.add(70940.22D);
		xAmostra.add(70609.07D);
		xAmostra.add(70919.75D);
		xAmostra.add(70058.08D);
		xAmostra.add(69561.53D);
		xAmostra.add(69133.09D);
		xAmostra.add(69426.57D);
		xAmostra.add(68709.22D);
		xAmostra.add(68050.71D);
		xAmostra.add(66697.57D);
		xAmostra.add(66574.88D);
		xAmostra.add(67847.34D);
		xAmostra.add(66688.48D);
		xAmostra.add(66764.84D);
		xAmostra.add(65269.15D);
		xAmostra.add(65362.04D);
		xAmostra.add(65771.33D);
		xAmostra.add(64217.52D);
		xAmostra.add(64577.83D);
		xAmostra.add(65755.66D);
		xAmostra.add(66557.55D);
		xAmostra.add(66341.39D);
		xAmostra.add(67576.62D);
		xAmostra.add(67684.99D);
		xAmostra.add(68066.82D);
		xAmostra.add(67258.66D);
		xAmostra.add(66439.83D);
		xAmostra.add(66910.48D);
		xAmostra.add(66948.99D);
		xAmostra.add(66902.53D);
		xAmostra.add(67383.22D);
		xAmostra.add(66242.63D);
		xAmostra.add(67281.51D);
		xAmostra.add(68145.53D);
		xAmostra.add(68012.10D);
		xAmostra.add(67263.75D);
		xAmostra.add(66040.66D);
		xAmostra.add(66684.60D);
		xAmostra.add(67169.25D);
		xAmostra.add(67005.22D);
		xAmostra.add(66002.57D);
		xAmostra.add(66215.93D);
		xAmostra.add(66879.89D);
		xAmostra.add(66689.61D);
		xAmostra.add(67578.33D);
		xAmostra.add(67795.51D);
		xAmostra.add(67532.97D);
		xAmostra.add(67765.94D);
		xAmostra.add(67192.82D);
		xAmostra.add(67418.76D);
		xAmostra.add(67997.06D);
		xAmostra.add(68586.70D);
		xAmostra.add(69268.29D);
		xAmostra.add(69703.80D);
		xAmostra.add(69837.52D);
		xAmostra.add(69036.91D);
		xAmostra.add(69176.12D);
		xAmostra.add(68718.01D);
		xAmostra.add(68164.36D);
		xAmostra.add(66896.23D);
		xAmostra.add(66486.49D);
		xAmostra.add(66278.89D);
		xAmostra.add(66684.21D);
		xAmostra.add(65415.49D);
		xAmostra.add(66158.09D);
		xAmostra.add(67058.02D);
		xAmostra.add(66972.37D);
		xAmostra.add(67144.26D);
		xAmostra.add(66264.47D);
		xAmostra.add(65673.21D);
		xAmostra.add(66132.86D);
		xAmostra.add(65462.75D);
		xAmostra.add(64318.18D);
		xAmostra.add(63615.50D);
		xAmostra.add(63407.01D);
		xAmostra.add(64417.34D);
		xAmostra.add(64621.97D);
		xAmostra.add(64876.88D);
		xAmostra.add(63775.82D);
		xAmostra.add(64003.16D);
		xAmostra.add(63235.30D);
		xAmostra.add(62829.68D);
		xAmostra.add(63673.34D);
		xAmostra.add(62840.61D);
		xAmostra.add(62367.36D);
		xAmostra.add(62596.52D);
		xAmostra.add(62345.18D);
		xAmostra.add(63336.75D);
		xAmostra.add(63388.44D);
		xAmostra.add(64098.57D);
		xAmostra.add(64294.96D);
		xAmostra.add(63953.93D);
		xAmostra.add(64620.08D);
		xAmostra.add(63411.48D);
		xAmostra.add(64218.08D);
		xAmostra.add(64340.50D);
		xAmostra.add(63067.73D);
		xAmostra.add(63217.85D);
		xAmostra.add(63032.97D);
		xAmostra.add(63468.82D);
		xAmostra.add(62697.16D);
		xAmostra.add(62022.92D);
		xAmostra.add(62204.83D);
		xAmostra.add(61603.74D);
		xAmostra.add(60880.62D);
		xAmostra.add(61059.98D);
		xAmostra.add(61168.24D);
		xAmostra.add(61423.61D);
		xAmostra.add(61194.09D);
		xAmostra.add(61016.72D);
		xAmostra.add(61216.98D);
		xAmostra.add(62303.37D);
		xAmostra.add(62333.97D);
		xAmostra.add(62403.64D);
		xAmostra.add(63394.34D);
		xAmostra.add(63891.31D);
		xAmostra.add(63038.81D);
		xAmostra.add(62565.46D);
		xAmostra.add(62207.33D);
		xAmostra.add(61513.24D);
		xAmostra.add(60223.63D);
		xAmostra.add(59704.75D);
		xAmostra.add(60669.89D);
		xAmostra.add(59679.35D);
		xAmostra.add(59478.01D);
		xAmostra.add(58837.61D);
		xAmostra.add(59082.13D);
		xAmostra.add(59119.71D);
		xAmostra.add(60262.95D);
		xAmostra.add(60270.47D);
		xAmostra.add(59970.54D);
		xAmostra.add(59339.90D);
		xAmostra.add(58288.46D);
		xAmostra.add(58708.25D);
		xAmostra.add(58823.45D);
		xAmostra.add(58535.74D);
		xAmostra.add(57310.78D);
		xAmostra.add(56017.22D);
		xAmostra.add(52811.36D);
		xAmostra.add(52949.22D);
		xAmostra.add(48668.29D);
		xAmostra.add(51150.90D);
		xAmostra.add(51395.29D);
		xAmostra.add(53343.11D);
		xAmostra.add(53473.35D);
		xAmostra.add(54651.83D);
		xAmostra.add(54323.61D);
		xAmostra.add(55073.02D);
		xAmostra.add(53134.10D);
		xAmostra.add(52447.63D);
		xAmostra.add(52440.23D);
		xAmostra.add(53786.63D);
		xAmostra.add(53795.70D);
		xAmostra.add(52953.30D);
		xAmostra.add(53350.79D);
		xAmostra.add(54860.73D);
		xAmostra.add(55385.03D);
		xAmostra.add(56495.12D);
		xAmostra.add(58118.20D);
		xAmostra.add(56531.62D);
		xAmostra.add(54998.41D);
		xAmostra.add(56607.30D);
		xAmostra.add(57623.63D);
		xAmostra.add(55778.39D);
		xAmostra.add(55685.47D);
		xAmostra.add(55543.97D);
		xAmostra.add(56286.04D);
		xAmostra.add(56381.46D);
		xAmostra.add(57210.11D);
		xAmostra.add(57102.78D);
		xAmostra.add(56378.63D);
		xAmostra.add(55981.90D);
		xAmostra.add(53280.28D);
		xAmostra.add(53230.36D);
		xAmostra.add(53747.52D);
		xAmostra.add(53920.36D);
		xAmostra.add(53270.36D);
		xAmostra.add(53384.67D);
		xAmostra.add(52324.42D);
		xAmostra.add(50791.53D);
		xAmostra.add(50686.34D);
		xAmostra.add(51013.85D);
		xAmostra.add(52290.37D);
		xAmostra.add(51243.62D);
		xAmostra.add(53273.11D);
		xAmostra.add(53838.47D);
		xAmostra.add(54601.07D);
		xAmostra.add(55030.45D);
		xAmostra.add(53911.33D);
		xAmostra.add(55031.93D);
		xAmostra.add(54966.13D);
		xAmostra.add(54009.98D);
		xAmostra.add(55255.23D);
		xAmostra.add(56891.97D);
		xAmostra.add(56285.99D);
		xAmostra.add(57143.79D);
		xAmostra.add(59270.13D);
		xAmostra.add(59513.13D);
		xAmostra.add(58338.39D);
		xAmostra.add(57322.75D);
		xAmostra.add(58196.30D);
		xAmostra.add(58669.92D);
		xAmostra.add(59198.77D);
		xAmostra.add(59026.13D);
		xAmostra.add(57549.74D);
		xAmostra.add(57321.81D);
		xAmostra.add(58546.97D);
		xAmostra.add(58258.23D);
		xAmostra.add(58559.99D);
		xAmostra.add(56988.90D);
		xAmostra.add(56731.34D);
		xAmostra.add(56284.59D);
		xAmostra.add(55878.44D);
		xAmostra.add(54972.08D);
		xAmostra.add(55279.88D);
		xAmostra.add(54894.49D);
		xAmostra.add(56017.35D);
		xAmostra.add(55299.76D);
		xAmostra.add(56874.98D);
		xAmostra.add(58143.42D);
		xAmostra.add(57885.85D);
		xAmostra.add(58910.48D);
		xAmostra.add(59536.16D);
		xAmostra.add(58662.83D);
		xAmostra.add(57455.02D);
		xAmostra.add(58236.46D);
		xAmostra.add(57346.86D);
		xAmostra.add(57494.85D);
		xAmostra.add(56646.87D);
		xAmostra.add(56331.15D);
		xAmostra.add(56096.93D);
		xAmostra.add(55298.33D);
		xAmostra.add(56864.85D);
		xAmostra.add(56653.37D);
		xAmostra.add(57347.87D);
		xAmostra.add(57701.07D);
		xAmostra.add(57669.48D);
		xAmostra.add(58005.20D);
		xAmostra.add(56533.76D);
		xAmostra.add(56754.08D);
		
		Double xValorAnterior = null;
		Double xVariacao;
		//Calcula a Variação
		for (Double xValor : xAmostra) {
			if (DBSObject.isNull(xValorAnterior)) {
				xValorAnterior = xValor;
				xVariacao = 0D;
			} else {
				//Calcula as variações do Índice
//				xVariacao = DBSNumber.divide(xValor, xValorAnterior).doubleValue();
//				xVariacao = DBSNumber.multiply(DBSNumber.subtract(xVariacao, 1), 100).doubleValue();
				xVariacao = DBSNumber.divide(DBSNumber.subtract(xValor, xValorAnterior), xValorAnterior).doubleValue();
			}
			xListaVariacao.add(xVariacao);
		}
		
		xMedia = DBSNumber.average(xListaVariacao).doubleValue();
		xDesvioPadrao = DBSNumber.desvioPadrao(xListaVariacao).doubleValue();
		xDistribuicao1 = DBSNumber.distribuicaoNormalInvertida(0.01D, 0D, 1D).doubleValue();
		xDistribuicao5 = DBSNumber.distribuicaoNormalInvertida(0.05D, 0D, 1D).doubleValue();
		xDistribuicao10 = DBSNumber.distribuicaoNormalInvertida(0.1D, 0D, 1D).doubleValue();
		
		xVAR1 = DBSNumber.add(xMedia, DBSNumber.multiply(xDistribuicao1, xDesvioPadrao)).doubleValue();
		xVAR5 = DBSNumber.add(xMedia, DBSNumber.multiply(xDistribuicao5, xDesvioPadrao)).doubleValue();
		xVAR10 = DBSNumber.add(xMedia, DBSNumber.multiply(xDistribuicao10, xDesvioPadrao)).doubleValue();
		
		System.out.println("VAR 1%: "+ xVAR1 +" = "+ DBSFormat.getFormattedNumber(DBSNumber.multiply(xVAR1, 100), 2) +"%");
		System.out.println("VAR 5%: "+ xVAR5 +" = "+ DBSFormat.getFormattedNumber(DBSNumber.multiply(xVAR5, 100), 2) +"%");
		System.out.println("VAR 10%: "+ xVAR10 +" = "+ DBSFormat.getFormattedNumber(DBSNumber.multiply(xVAR10, 100), 2) +"%");
	}
}
