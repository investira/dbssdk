package br.com.dbsoft.tmp;

import br.com.dbsoft.event.DBSEvent;


public class DBSMeuEvento extends DBSEvent<Object> {

	public DBSMeuEvento(Object pObject) {
		super(pObject);
		// TODO Auto-generated constructor stub
	}

	private boolean wCancela;

	public boolean isCancela() {
		return wCancela;
	}

	public void setCancela(boolean cancela) {
		wCancela = cancela;
	}
	
}
