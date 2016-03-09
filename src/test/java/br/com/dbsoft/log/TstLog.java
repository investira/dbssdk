/**
 * 
 */
package br.com.dbsoft.log;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author Avila
 */
public class TstLog {

//	@Test
	public void test_listarLog() {

		assertNotNull("TESTE ESPERAVA NOTNULL",
				DBSLog.loadLogFile("C:/jboss-as-7.1.1.Final/standalone/deployments/logs/ifeedin.log"));
	}
	
	private static org.apache.log4j.Logger wLogger = Logger.getLogger(TstLog.class);

	@Test
	public void test_DBSDailyRollingFileAppender() throws MalformedURLException {
		wLogger.trace("Trace");
		wLogger.debug("Debug");
		wLogger.info("Info");
		wLogger.warn("Warn");
		wLogger.error("Error");
		wLogger.fatal("Fatal");
	}
}
