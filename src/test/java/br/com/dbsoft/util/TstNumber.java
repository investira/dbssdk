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
}
