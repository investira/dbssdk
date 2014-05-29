package br.com.dbsoft.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.dbsoft.util.DBSIO;

public class TstIO {
	
	@Test 
	public void tablesFromQuery(){
		String xSQL;
		xSQL = "SELECT * FROM TESTE1, TESTE2 WHERE TESTE1.CAMPO = TESTE2.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL).toString(), "[TESTE1, TESTE2]");
		
		xSQL = "SELECT * FROM TESTE1, TESTE2 WHERE TESTE1.CAMPO = TESTE2.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL).toString(), "[TESTE1, TESTE2]");
		
		xSQL = "SELECT * FROM TESTE1 AS A, TESTE2 AS B WHERE TESTE1.CAMPO = TESTE2.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL).toString(), "[A, B]");
		
		xSQL = "SELECT * FROM TESTE1 LEFT JOIN TESTE2 ON TESTE1.CAMPO = TESTE2.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL).toString(), "[TESTE1, TESTE2]");
		
		xSQL = "SELECT * FROM TESTE1 LEFT JOIN TESTE2 AS B ON A.CAMPO = B.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL).toString(), "[TESTE1, B]");
		
		xSQL = "SELECT * FROM TESTE1 AS A LEFT JOIN TESTE2 ON A.CAMPO = B.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL).toString(), "[A, TESTE2]");
		
		xSQL = "SELECT * FROM TESTE1 as A LEFT JOIN TESTE2 AS B ON A.CAMPO = B.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL).toString(), "[A, B]");
		
		xSQL = "SELECT * FROM TESTE1 as A LEFT JOIN TESTE2 AS B ON A.CAMPO = B.CAMPO, " +
							 "TESTE3 WHERE TESTE1.CAMPO = TESTE2.CAMPO";
		assertEquals(DBSIO.getTablesFromQuery(xSQL).toString(), "[A, B, TESTE3]");
		
		xSQL = "SELECT * FROM TESTE1 as A LEFT JOIN TESTE2 AS B ON A.CAMPO = B.CAMPO, " +
				 "TESTE3 LEFT JOIN TESTE4 ON TESTE3.CAMPO = TESTE4.CAMPOM WHERE TESTE1.CAMPO = TESTE2.CAMPO";
		assertEquals(DBSIO.getTablesFromQuery(xSQL).toString(), "[A, B, TESTE3, TESTE4]");
	}

}
