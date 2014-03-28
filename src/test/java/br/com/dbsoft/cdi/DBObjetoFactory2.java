package br.com.dbsoft.cdi;

import java.io.Serializable;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;


//import javax.enterprise.inject.Disposes;
//import javax.enterprise.inject.Produces;
//import javax.inject.Named;

public class DBObjetoFactory2 implements Serializable {
	private static final long serialVersionUID = 1L;
		
		
		@Produces 
		public DBObjeto2 createString(){
			System.out.println("CRIOU OBJETO 2");
			return new DBObjeto2("ALO 2");
		}
		
		public void disposeString(@Disposes DBObjeto2 pDBObjeto2){
			pDBObjeto2 = null;
		}

}
