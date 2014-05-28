package br.com.dbsoft.io;

public class DBSDAOTxtTesteModel {

	private char[] TIPO = new char[10]; 
	private char[] FILLER = new char[20];
	
	public char[] getTIPO() {
		return TIPO;
	}
	public void setTIPO(char[] pTIPO) {
		TIPO = pTIPO;
	}
	public char[] getFILLER() {
		return FILLER;
	}
	public void setFILLER(char[] pFILLER) {
		FILLER = pFILLER;
	}
}
