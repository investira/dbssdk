package br.com.dbsoft.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.dbsoft.util.DBSIO;

public class TstIO {
	
	@Test 
	public void tablesFromQuery(){
		String xSQL;
		xSQL = "SELECT * FROM TESTE1, TESTE2 WHERE TESTE1.CAMPO = TESTE2.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL, true).toString(), "[TESTE1, TESTE2]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false).toString(), "[TESTE1, TESTE2]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false, "TESTE1").toString(), "[TESTE1]");
		assertEquals(DBSIO.getTableFromQuery(xSQL, false, "TESTE2"), "TESTE2");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false, "TESTE2").toString(), "[TESTE2]");
		assertEquals(DBSIO.getTableFromQuery(xSQL, false, "TESTE2"), "TESTE2");
		
		xSQL = "SELECT * FROM TESTE1, TESTE2 WHERE TESTE1.CAMPO = TESTE2.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL, true).toString(), "[TESTE1, TESTE2]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false).toString(), "[TESTE1, TESTE2]");
		assertEquals(DBSIO.getTableFromQuery(xSQL, true,"TESTE1"), "TESTE1");
		assertEquals(DBSIO.getTableFromQuery(xSQL, true,"TESTE2"), "TESTE2");
		assertEquals(DBSIO.getTableFromQuery(xSQL, false,"TESTE1"), "TESTE1");
		assertEquals(DBSIO.getTableFromQuery(xSQL, false,"TESTE2"), "TESTE2");
		
		xSQL = "SELECT * FROM TESTE1 AS A, TESTE2 AS B WHERE TESTE1.CAMPO = TESTE2.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL, true).toString(), "[A, B]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false).toString(), "[TESTE1, TESTE2]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false, "A").toString(), "[TESTE1]");
		assertEquals(DBSIO.getTableFromQuery(xSQL, false, "A"), "TESTE1");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false, "B").toString(), "[TESTE2]");
		assertEquals(DBSIO.getTableFromQuery(xSQL, false, "B"), "TESTE2");
		assertEquals(DBSIO.getTableFromQuery(xSQL, true, "TESTE2"), "B");
		assertEquals(DBSIO.getTableFromQuery(xSQL, false, "A"), "TESTE1");
		assertEquals(DBSIO.getTableFromQuery(xSQL, true, "A"), null); 
		assertEquals(DBSIO.getTableFromQuery(xSQL, true, "B"), null); 
		assertEquals(DBSIO.getTableFromQuery(xSQL, true, "X"), null);
		assertEquals(DBSIO.getTableFromQuery(xSQL, false, "X"), null);
		
		xSQL = "SELECT * FROM TESTE1 LEFT JOIN TESTE2 ON TESTE1.CAMPO = TESTE2.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL, true).toString(), "[TESTE1, TESTE2]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false).toString(), "[TESTE1, TESTE2]");
		
		xSQL = "SELECT * FROM TESTE1 LEFT JOIN TESTE2 AS B ON A.CAMPO = B.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL, true).toString(), "[TESTE1, B]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false).toString(), "[TESTE1, TESTE2]");
		
		xSQL = "SELECT * FROM TESTE1 AS A LEFT JOIN TESTE2 ON A.CAMPO = B.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL, true).toString(), "[A, TESTE2]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false).toString(), "[TESTE1, TESTE2]");
		
		xSQL = "SELECT * FROM TESTE1 as A LEFT JOIN TESTE2 AS B ON A.CAMPO = B.CAMPO ";
		assertEquals(DBSIO.getTablesFromQuery(xSQL, true).toString(), "[A, B]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false).toString(), "[TESTE1, TESTE2]");
		
		xSQL = "SELECT * FROM TESTE1 as A LEFT JOIN TESTE2 AS B ON A.CAMPO = B.CAMPO, " +
							 "TESTE3 WHERE TESTE1.CAMPO = TESTE2.CAMPO";
		assertEquals(DBSIO.getTablesFromQuery(xSQL, true).toString(), "[A, B, TESTE3]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false).toString(), "[TESTE1, TESTE2, TESTE3]");
		
		xSQL = "SELECT * FROM TESTE1 as A LEFT JOIN TESTE2 AS B ON A.CAMPO = B.CAMPO, " +
				 "TESTE3 LEFT JOIN TESTE4 ON TESTE3.CAMPO = TESTE4.CAMPOM WHERE TESTE1.CAMPO = TESTE2.CAMPO";
		assertEquals(DBSIO.getTablesFromQuery(xSQL, true).toString(), "[A, B, TESTE3, TESTE4]");
		assertEquals(DBSIO.getTablesFromQuery(xSQL, false).toString(), "[TESTE1, TESTE2, TESTE3, TESTE4]");
		
		assertEquals("CA_casa", DBSIO.getTableFromQuery("Select * from CA_casa", true));
		assertEquals("teste", DBSIO.getTableFromQuery("Select * from CA_casa as teste where a=b group by a", true));
		assertEquals("teste", DBSIO.getTableFromQuery("Select c1 as a1, c2  from CA_casa as teste where a=b group by a", true));
		assertEquals("CA_casa", DBSIO.getTableFromQuery("Select c1 as a1, c2  from CA_casa where a=b group by a", true));
		assertEquals("teste", DBSIO.getTableFromQuery("Select * from CA_casa as teste", true));
		
	}

}
