package br.com.dbsoft.cdi;

import java.io.Serializable;


import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;

public class DBObjetoFactory implements Serializable {
	private static final long serialVersionUID = 1L;
		
		@Named("A")
		@Produces
		public DBObjeto createStringA(InjectionPoint injectionPoint){
			System.out.println("CRIOU A");
			return new DBObjeto("ALO");
		}
		
		public void disposeString(@Disposes DBObjeto pDBString){
			pDBString = null;
		}

		@DBQualifierB
		@Named("B")
		@Produces
		public DBObjeto createStringB(InjectionPoint injectionPoint){
			System.out.println("CRIOU B");
			return new DBObjeto("BYE");
		}
		
}
