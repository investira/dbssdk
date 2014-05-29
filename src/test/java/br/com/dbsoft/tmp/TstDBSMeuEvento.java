package br.com.dbsoft.tmp;

import br.com.dbsoft.event.DBSEvent;


public class TstDBSMeuEvento extends DBSEvent<Object> {

	public TstDBSMeuEvento(Object pObject) {
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
