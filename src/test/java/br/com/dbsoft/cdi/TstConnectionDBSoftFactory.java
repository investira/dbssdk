package br.com.dbsoft.cdi;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.sql.DataSource;

public class TstConnectionDBSoftFactory {

	@Resource(mappedName="dbsoft")
	private DataSource wDS;
	
	@Produces
	public Connection openConnection(InjectionPoint injectionPoint){
		try {
			Connection xCn = wDS.getConnection();
			xCn.setAutoCommit(false);
			return xCn;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void closeConnection(@Disposes Connection pCn){
		try {
			pCn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
