package br.com.dbsoft.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TstObject {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test_equal(){
		String xS1 = "1";
		String xS2 = "1";
		Double xD1 = 1D;
		Double xD2 = 1D;
		assertEquals(false, DBSObject.isEqual(1, 1D));
		assertEquals(true, DBSObject.isEqual(1, 1));
		assertEquals(true, DBSObject.isEqual(1D, 1D));
		assertEquals(false, DBSObject.isEqual(1D, null));
		assertEquals(false, DBSObject.isEqual(null, 1D));
		assertEquals(false, DBSObject.isEqual("2", "1"));
		assertEquals(true, DBSObject.isEqual(xS1, xS2));
		assertEquals(true, DBSObject.isEqual(xD1, xD2));
		assertEquals(true, DBSObject.isEqual(xD1, 1D));
		assertEquals(true, DBSObject.isEqual(1D, xD1));
	}

}
