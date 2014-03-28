package br.com.dbsoft.cdi;



public class DBObjeto2 {
	private String wString = "objeto2";
	
//	DBObjeto2(){
//	}
//
	DBObjeto2(String pString){
		wString = pString;
		System.out.println("OBJECTO 2 CONSTRUCTOR");
	}
//	
	public String getTexto(){
		return wString;
	}
	
}
