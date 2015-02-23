package br.com.dbsoft.util;

import org.junit.Test;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.task.DBSTask;
import br.com.dbsoft.task.DBSTaskEvent;



public class TstTask {

	
	@Test
	public void test_toInteger(){

		DBSTask<Object> xTask = new DBSTask<Object>(){
			@Override
			public void afterRun(DBSTaskEvent pEvent) throws DBSIOException {
				System.out.println("afterRun");
				wLogger.info("afterRun");
			}
			@Override
			public void beforeRun(DBSTaskEvent pEvent) throws DBSIOException {
				System.out.println("beforeRun");
				wLogger.info("beforeRun");
			}
			
			@Override
			public void step(DBSTaskEvent pEvent) throws DBSIOException {
				error();
			}
			
			@Override
			public void ended(DBSTaskEvent pEvent) throws DBSIOException {
				System.out.println("ended");
				wLogger.info("ended");
			}
			@Override
			public void error(DBSTaskEvent pEvent) throws DBSIOException {
				System.out.println("error");
				wLogger.info("error");
			}
			@Override
			public void interrupted(DBSTaskEvent pEvent) throws DBSIOException {
				System.out.println("interrupted");
				wLogger.info("interrupted");
			}
		};
//		xTask.reset();
		xTask.setSteps(3);
		xTask.setMultiTask(false);
		xTask.setRetryOnErrorSeconds(1);
		xTask.setRetryOnErrorTimes(2);
		try {
			xTask.run();
		} catch (DBSIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
