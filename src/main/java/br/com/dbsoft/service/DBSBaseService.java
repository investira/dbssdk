package br.com.dbsoft.service;

import java.sql.Connection;

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
	
	public IDBSMessages getMessages(){
		return wMessages;
	}
	
	//METODOS CONSTRUTORES ================================================
	
}
