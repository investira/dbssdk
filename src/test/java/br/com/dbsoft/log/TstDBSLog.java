/**
 * 
 */
package br.com.dbsoft.log;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Avila
 * 
 */
public class TstDBSLog {

	@Test
	public void test_listarLog() {

		assertNotNull("TESTE ESPERAVA NOTNULL",
				DBSLog.loadLogFile("C:/jboss-as-7.1.1.Final/standalone/deployments/logs/ifeedin.log"));
	}

}
