package br.com.dbsoft.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import br.com.dbsoft.util.DBSNumber;

public class DBSNumberTest {

	@Test
	public void test_subtract() {
		Double xRetorno;
		xRetorno = 1585481D;
		assertEquals(xRetorno, DBSNumber.subtract(1947652D, 362171D));
		xRetorno =  (-80D);
		assertEquals(xRetorno, DBSNumber.subtract( 20D,100D));
		xRetorno =  (-100D);
		assertEquals(xRetorno, DBSNumber.subtract( 0D,100D));
		xRetorno =  (100D);
		assertEquals(xRetorno, DBSNumber.subtract( 100D,0D));
		xRetorno =  null;
		assertEquals(xRetorno, DBSNumber.subtract( null,0D));
		xRetorno =  null;
		assertEquals(xRetorno, DBSNumber.subtract( 100D,null));
		xRetorno =  null;
		assertEquals(xRetorno, DBSNumber.subtract( null,100D));		    
		xRetorno =  0.10D;
		assertEquals(xRetorno, DBSNumber.subtract( 1.40D,1.30D));		    
		xRetorno =  -0.10D;
		assertEquals(xRetorno, DBSNumber.subtract( 1.20D,1.30D));		    
		xRetorno =  -0.10D;
		assertEquals(xRetorno, DBSNumber.subtract( 1.20D,1.30D));		    
		xRetorno =  -71605453132D;
		assertEquals(xRetorno, DBSNumber.subtract( 12233223332D,83838676464D));		    
		xRetorno =  -71605453132D;
		assertEquals(xRetorno, DBSNumber.subtract( 12233223332.9090978877788D,83838676464.9090978877788D));		    

	}
	
	@Test
	public void test_add() {

		assertEquals((Double)2D, DBSNumber.add(1D, 1D));
		assertEquals((Double)60D, DBSNumber.add(20D, 40D));
		assertEquals((Double)60D, DBSNumber.add(40D, 20D));
		assertEquals(null, DBSNumber.add(null, 20D));
		assertEquals((Double)150000000D, DBSNumber.add(100000000D, 50000000D));
		assertEquals((Double)1D, DBSNumber.add(.99D, 0.01D));
		assertEquals((Double)4D, DBSNumber.add(2D, 2D));		
		assertEquals((Double)2.0000000000000000000002D, DBSNumber.add(1.0000000000000000000001D, 1.0000000000000000000001D));
		assertEquals(null, DBSNumber.add(1.0000000000000000000001D, null));
	}
    
	@Test
	public void test_multiply() {
    	
		assertEquals((Double)2D, DBSNumber.multiply(2D, 1D));    	
		assertEquals((Double)(-2D), DBSNumber.multiply(2D, -1D));
		assertEquals((Double)890989.10901D, DBSNumber.multiply(9889989D, 0.09009D));
		assertEquals((Double)300D, DBSNumber.multiply(150D, 2D));
		assertEquals((Double)(-300D), DBSNumber.multiply(-150D, 2D));
		assertEquals((Double)(-300D), DBSNumber.multiply(150D, -2D));
		assertEquals((Double)987676662D, DBSNumber.multiply(987676662D, 1D));
		assertEquals((Double)987676662D, DBSNumber.multiply(1D,987676662D));
		assertEquals(null, DBSNumber.multiply(null,987676662D));
		assertEquals(null, DBSNumber.multiply(987676662D,null));
		assertEquals((Double)2.0000000000000000000002, DBSNumber.multiply(1.0000000000000000000001D, 2D)) ;
		assertEquals((Double)2.0000000000000000000002D, DBSNumber.multiply(2D,1.0000000000000000000001D))  ;
    }

    @Test
	public void test_divide() {
    	BigDecimal x,y,r;
    	x = new BigDecimal("1");
    	y = new BigDecimal("3");
    	r = new BigDecimal("0.333333333333333333333333333334");
    	assertEquals(r, DBSNumber.divide(x, y));
	    assertEquals((Double)2D, DBSNumber.divide(4D, 2D));    	
		assertEquals((Double)(-2D), DBSNumber.divide(-4D, 2D));
		assertEquals((Double)1.1, DBSNumber.divide(2.2D, 2D));
		assertEquals((Double)75D, DBSNumber.divide(150D, 2D));
		assertEquals((Double)(-300D), DBSNumber.divide(-600D, 2D));
		assertEquals((Double)(-300D), DBSNumber.divide(600D, -2D));
		assertEquals((Double)987676662D, DBSNumber.divide(987676662D, 1D));
		assertEquals((Double)2.0000000000000000000002D, DBSNumber.divide(4.0000000000000000000004D, 2D));
		assertEquals((Double)2D, DBSNumber.divide(4.04D, 2.02D));	
		assertEquals(null, DBSNumber.divide(999D, null));
		assertEquals(null, DBSNumber.divide(null, 9999D));
    }    

	@Test
	public void test_exp() {
		
	    assertEquals((Double)16D, DBSNumber.exp(4D, 2D));    	
		assertEquals((Double)(16D), DBSNumber.exp(-4D, 2D));
		assertEquals((Double)4.840000000000001, DBSNumber.exp(2.2D, 2D));
		assertEquals((Double)506250000D, DBSNumber.exp(150D, 4D));
		assertEquals((Double)(81D), DBSNumber.exp(9D, 2D));
		assertEquals((Double)(0.001953125D), DBSNumber.exp(2D, -9D));
		assertEquals((Double)987676662D, DBSNumber.exp(987676662D, 1D));
		assertEquals((Double)16D, DBSNumber.exp(4.0000000000000000000004D, 2D));
		assertEquals((Double)16.783802388556726D, DBSNumber.exp(4.04D, 2.02D));	
		assertEquals(null, DBSNumber.exp(999D, null));
		assertEquals(null, DBSNumber.exp(null, 9999D));		
		
	}
	
	@Test
	public void test_log() {
		
		
	    assertEquals((Double)1.3862943611198906, DBSNumber.log(4D));    	
	    assertEquals((Double)2.995732273553991D, DBSNumber.log(20D));    	
	    assertEquals((Double)null, DBSNumber.log(null));
	    
	}

	@Test
	public void test_sign() {
		assertEquals((Double)1D, DBSNumber.sign(90D));
		assertEquals((Double)1D, DBSNumber.sign(4.0000000000000000000004D));
		assertEquals((Double)0D, DBSNumber.sign(0D));
		assertEquals((Double)(-1D), DBSNumber.sign(-89D));
		assertEquals((Double)(-1D), DBSNumber.sign(-1002020D));
		assertEquals(null, DBSNumber.sign(null));
	}

	@Test
	public void test_abs() {
		assertEquals((Double)10000D, DBSNumber.abs(-10000D));
		assertEquals((Double)80000D, DBSNumber.abs(-80000D));	
		assertEquals((Double)1.22112D, DBSNumber.abs(-1.22112D));	
		assertEquals((Double)123322.223332222322112D, DBSNumber.abs(-123322.223332222322112D));	
		assertEquals((Double)123322.223332222322112D, DBSNumber.abs(123322.223332222322112D));	
		assertEquals(null, DBSNumber.abs(null));	
	}
	
	@Test
	public void test_trunc() {
        
		assertEquals((Double)10000D, DBSNumber.trunc(10000.998898989D, 0));
		assertEquals((Double)10000.88D, DBSNumber.trunc(10000.8898989D, 2));
		assertEquals((Double)10000.889D, DBSNumber.trunc(10000.8898989D, 3));
		assertEquals((Double)10000.8898D, DBSNumber.trunc(10000.8898989D, 4));
		assertEquals((Double)10000.9999D, DBSNumber.trunc(10000.99999D, 4));
		
		BigDecimal xxResult = new BigDecimal("10000.9999999999") ;
		BigDecimal xxValor = new BigDecimal("10000.9999999999999999999888877878") ;		
		assertEquals(xxResult, DBSNumber.trunc(xxValor, 10));

		xxResult = new BigDecimal("838383838383838383838992929292910000.99999999998888888888") ;
		xxValor = new BigDecimal("838383838383838383838992929292910000.9999999999888888888877666666766655") ;
		assertEquals(xxResult, DBSNumber.trunc(xxValor, 20));

		xxResult = new BigDecimal("838383838383838383838992929292910000.99") ;
		xxValor = new BigDecimal("838383838383838383838992929292910000.9999999999888888888877666666766655") ;
		assertEquals(xxResult, DBSNumber.trunc(xxValor, 2));
		
		assertEquals(null,  DBSNumber.trunc(xxValor,null));
		
		Double xValor = DBSNumber.divide(100000000.0, 1000.0);
		xValor = DBSNumber.multiply(xValor, DBSNumber.toDoubleNotZero(1, 1));
		xxResult = DBSNumber.toBigDecimal(100000.0);
		xValor = DBSNumber.trunc(xValor, 14);
		assertEquals(xxResult, DBSNumber.toBigDecimal(xValor));
	}

	@Test
	public void test_round() {

		assertEquals((Double)10001D, DBSNumber.round(10000.998898989D, 0));
		assertEquals((Double)10000.9989D, DBSNumber.round(10000.998898989D, 4));

		BigDecimal xxResult = new BigDecimal("10001.00") ;
		BigDecimal xxValor = new BigDecimal("10000.9999999999999999999888877878") ;		
		assertEquals(xxResult, DBSNumber.round(xxValor, 2));

		xxResult = new BigDecimal("10000.8432") ;
		xxValor = new BigDecimal("10000.8432129999999999999888877878") ;		
		assertEquals(xxResult, DBSNumber.round(xxValor, 4));

		xxResult = new BigDecimal("10000.848888") ;
		xxValor = new BigDecimal("10000.848887878878787878787878787878787878787878877878") ;		
		assertEquals(xxResult, DBSNumber.round(xxValor, 6));

		xxResult = new BigDecimal("10000.3434343434") ;
		xxValor = new BigDecimal("10000.34343434343434348488878788787878787878787878787878787878788778788768788774") ;		
		assertEquals(xxResult, DBSNumber.round(xxValor, 10));

		xxResult = new BigDecimal("10000.343434343434") ;
		xxValor = new BigDecimal( "10000.34343434343434348488878788787878787878787878787878787878788778788768788774") ;		
		assertEquals(xxResult, DBSNumber.round(xxValor, 12));

		xxResult = new BigDecimal("10000.343434343434") ;
		xxValor = new BigDecimal( "10000.34343434343434348488878788787878787878787878787878787878788778788768788774") ;		
		assertEquals(null, DBSNumber.round(xxValor, null));

		xxResult = new BigDecimal("2") ;
		xxValor = new BigDecimal( "1.9") ;		
		assertEquals(xxResult, DBSNumber.round(xxValor, 0));
		assertEquals(xxResult, DBSNumber.round(xxValor, 0));

	}
	
	@Test    
	public void test_inte() {
		
		assertEquals((Double)10000D, DBSNumber.inte(10000.998898989D));
		assertEquals((Double)10000D, DBSNumber.inte(10000.989D));
		assertEquals((Double)100001222D, DBSNumber.inte(100001222.998898989D));
		assertEquals((Double)10000D, DBSNumber.inte(10000D));
		assertEquals((Double)10000D, DBSNumber.inte(10000D));
		assertEquals((Double)10000D, DBSNumber.inte(10000D));
		assertEquals(null, DBSNumber.inte(null));
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
		
		assertEquals((Double) 10000.99, DBSNumber.toNumber("1000099",2));
		assertEquals((Double) 1000.099, DBSNumber.toNumber("1000099",3));
		assertEquals((Double) 100.0099, DBSNumber.toNumber("1000099",4));
		assertEquals((Double) 10.00099, DBSNumber.toNumber("1000099",5));
		assertEquals((Double) 1.000099, DBSNumber.toNumber("1000099",6));
		assertEquals((Double) .1000099, DBSNumber.toNumber("1000099",7));
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

}
