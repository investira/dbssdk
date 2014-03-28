package br.com.dbsoft.cdi;


public class DBObjeto {
	public String wString = "vazio";
	
	//@Inject
	//DBObjeto2 xObj;
	
	DBObjeto(String pString){
		wString = pString;
		System.out.println("OBJECTO CONSTRUCTOR");
		//System.out.println(xObj.getTexto());
	}
	
	public String getTexto(){
		return wString;
	}
	
}
