package br.com.dbsoft.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class DBSRowTest {

	@Test
	public void test_setRowValues(){
		DBSRow xRow = new DBSRow();
		xRow.MergeColumn("COLUNA1", 10);
		xRow.MergeColumn("COLUNA2", 4);
		xRow.MergeColumn("COLUNA3", 20);
		xRow.setRowValues("01/01/2010ABCD12345678901234567890");
		assertEquals("01/01/2010",xRow.getValue("COLUNA1"));
		assertEquals("ABCD", xRow.getValue("COLUNA2"));
		assertEquals("12345678901234567890", xRow.getValue("COLUNA3"));
		xRow.setRowValues("01/01/2010ABCD12345678901234567");
		assertEquals("12345678901234567", xRow.getValue("COLUNA3"));
		xRow.setRowValues("01/01/2010AB");
		assertEquals("                    ", xRow.getValue("COLUNA3"));
		//System.out.println(xRow.getValue("COLUNA3"));
	}
	
//	@Test(expected = StringIndexOutOfBoundsException.class)
//	public void test_setRowValues2(){
//		DBSRow xRow = new DBSRow();
//		xRow.MergeColumn("COLUNA1", 10);
//		xRow.MergeColumn("COLUNA2", 4);
//		xRow.MergeColumn("COLUNA3", 20);
//		xRow.setRowValues("01/01/2010ABCD12345678901234567");
//	}
	
}
