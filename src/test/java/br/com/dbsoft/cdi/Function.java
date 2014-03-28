package br.com.dbsoft.cdi;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Function {
	
	@Inject @Named("B") @DBQualifierB
	static DBObjeto xS;
	
	public static DBObjeto funcaoTeste(){
		return xS;
	}
	
}
