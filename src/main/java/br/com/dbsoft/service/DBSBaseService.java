package br.com.dbsoft.service;

import java.sql.Connection;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.dbsoft.message.DBSMessages;
import br.com.dbsoft.message.IDBSMessages;

/**
 * @author jose.avila@dbsoft.com.br
 */
public abstract class DBSBaseService {

	protected Logger wLogger =  Logger.getLogger(this.getClass());

	protected 	Connection 		wConnection;
	private 	IDBSMessages 	wMessages = new DBSMessages(true);
	private		int				wStatusCode = HttpServletResponse.SC_OK;
	
	public IDBSMessages getMessages(){
		return wMessages;
	}

	public int getStatusCode() {
		return wStatusCode;
	}
	public void setStatusCode(int pStatusCode) {
		wStatusCode = pStatusCode;
	}

	//METODOS CONSTRUTORES ================================================
	
}
