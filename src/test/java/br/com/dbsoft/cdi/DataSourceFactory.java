package br.com.dbsoft.cdi;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.sql.DataSource;

@Named
public class DataSourceFactory {
	@Resource(mappedName="dbsoft")
	private DataSource wDS;

	
	public DataSource getDataSource(){
		return wDS;
	}
}
