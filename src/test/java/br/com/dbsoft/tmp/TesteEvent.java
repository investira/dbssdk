package br.com.dbsoft.tmp;

import java.io.Serializable;


import br.com.dbsoft.util.DBSNumber;



public class TesteEvent implements Serializable, IDBSEventListener {

	private static final long serialVersionUID = 1L;

	//@Test
	public void TestaEvento(){
		DBSMeuEvento xE = new DBSMeuEvento(this);
		beforeQuery(xE);

	}

	public final static String getStringWithHtmlLineFeed(String pMessageText){
		String xS[] = pMessageText.split("\\s+"); 
		String xMessageText = "";
		Double xLarguraMax = (double) pMessageText.length();
		xLarguraMax = DBSNumber.exp(xLarguraMax, 0.70);
		xLarguraMax = DBSNumber.round(xLarguraMax, 0);
		int xI = 0;
		for (String xPalavra :xS){
			xI = xI + xPalavra.length() + 1;
			xMessageText = xMessageText + xPalavra +  " ";
			if (xI >= xLarguraMax){
				xMessageText = xMessageText.trim() + "<br>";
				xI = 0;
			}
		}
		return xMessageText.trim();
	}
	
//	@Test
//	public void TesteB(){
//		for (int xX = 0; xX < 10000; xX++){
//			DBSString.CorretorOrtografico2("Fundo de Investimento em Cotas de Fundo de Investimento");
//		}
//	}

	@Override
	public void beforeQuery(DBSMeuEvento pDBSEvent) {
		pDBSEvent.setOk(true);
		pDBSEvent.setCancela(true);
	}




}
