package br.com.dbsoft.util;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSFormat;
import br.com.dbsoft.util.DBSFormat.NUMBER_SIGN;
import br.com.dbsoft.util.DBSString;

public class TstString {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
//	@Test
//	public void test_ascii(){
//		System.out.println(DBSString.changeStr("Odebrecht  Defesa  e  Tecnologia S.A.", "  ", " ",false));
////		System.out.println(DBSString.toASCII("É"));
////		pValue = DBSString.changeStr((String)pValue, "╔", "É");
////		pValue = DBSString.changeStr((String)pValue, "ß", "á");
//		assertEquals("╔", DBSString.toASCII("É"));
//	}
//	@Test
//	public void test_toArray() {
//
//		ArrayList <String> xA = new ArrayList<String>();
//		ArrayList <String> xB=  new ArrayList<String>();
//		ArrayList <String> xC=  new ArrayList<String>();
//		xA.add("d");
//		xA.add("d");
//		xA.add("d");
//		xA.add("d");
//
//		xB.add("dxdxdxd");
//		
//		xC.add("");
//		xC.add("");
//		xC.add("");
//		xC.add("");
//		
//		assertEquals(xA, DBSString.toArray("d d d d", " "));
//		assertEquals(xA, DBSString.toArray("dXdXdXd", "X"));
//		assertEquals(xA, DBSString.toArray("dxdxdxd", "X",false));
//		assertEquals(xB, DBSString.toArray("dxdxdxd", "X"));
////		String xS =  new String("abcd");
////		xS.indexOf("a", 1);
//
//		assertEquals(xC, DBSString.toArray(";;;", ";"));
//	}
//
//	
//	@Test
//	public void test_getInStr() {
//		assertEquals(3, DBSString.getInStr("XXAXXXXXXXX", "A"));
//		assertEquals(7, DBSString.getInStr("GFGFGFFABIOHDHDHDH", "FABIO"));
//		assertEquals(0, DBSString.getInStr("GFGFGFFABIOHDHDHDH", "JOSE"));
//		assertEquals(0, DBSString.getInStr("", "HJHJ"));
//		assertEquals(0, DBSString.getInStr("AAXX", "xx"));
//		assertEquals(0, DBSString.getInStr("AAA", ""));
//		assertEquals(0, DBSString.getInStr(null, ""));
//		assertEquals(0, DBSString.getInStr("GHJGHJGJH", null));
//		assertEquals(0, DBSString.getInStr(null, null));
//
//		assertEquals(3, DBSString.getInStr("XXAXXXXXXXX", "A",1));
//		assertEquals(7, DBSString.getInStr("GFGFGFFABIOHDHDHDH", "FABIO",5));
//		assertEquals(0, DBSString.getInStr("GFGFGFFABIOHDHDHDHJOSE", "JOSE",20));
//		assertEquals(0, DBSString.getInStr("", "HJHJ",0));
//		assertEquals(0, DBSString.getInStr("AAXX", "xx",0));
//		assertEquals(2, DBSString.getInStr("AAA", "A",2));
//		assertEquals(0, DBSString.getInStr(null, "",0));
//		assertEquals(0, DBSString.getInStr("GHJGHJGJH", null,0));
//		assertEquals(0, DBSString.getInStr(null, null,0));
//		
//	}
//	
//	@Test
//	public void test_getStringCount() {
//		
//		assertEquals(6, DBSString.getStringCount("AAAXAAXXXAAXXXAAXAABABABAA", "AA"));
//		assertEquals(4, DBSString.getStringCount("AAAXAAXXXAAXXXAAXA", "AA"));
//		assertEquals(0, DBSString.getStringCount("AAAXAAXXXAAXXXAAXA", "aa"));
//		assertEquals(0, DBSString.getStringCount(null, "aa"));
//		assertEquals(0, DBSString.getStringCount("AAAAAA", null));
//		assertEquals(0, DBSString.getStringCount(null, null));
//	}
//	
//	@Test
//	public void test_changeStr() {
//		assertEquals("jony  fabio  neto", DBSString.changeStr("jony, fabio, neto",",", " ", false));
//		assertEquals("jony fabio neto", DBSString.changeStr("jony,fabio,neto",",", " ", false));
//		assertEquals(":abcxyz", DBSString.changeStr(":abc :xyz"," :", ""));
//		assertEquals("jony", DBSString.changeStr("FABIO","FABIO", "jony"));
//		assertEquals("FABIO", DBSString.changeStr("FABIO","Fabio", "jony", true));
//		assertEquals("jony", DBSString.changeStr("FÁBIO","Fábio", "jony", false));
//		assertEquals("jony neto", DBSString.changeStr("jony fabio neto","fabio ", "", false));
//		assertEquals("jony neto", DBSString.changeStr("jony Fábio neto","fÁbio ", "", false));
//		assertEquals("aassfabioaasss", DBSString.changeStr("aassFABIOaasss","FABIO", "fabio"));
//		assertEquals("abcdefgh", DBSString.changeStr("abcfgh","abc", "abcde"));
//		assertEquals("abcdefgh", DBSString.changeStr("abcfgh","abc", "abcde", false));
//		assertEquals("A @@ x Á @@ y A @ z", DBSString.changeStr("A @@ x Á @@ y a @@ z","a @@", "A @", true));
//		assertEquals("A @ x A @ y A @ z", DBSString.changeStr("A @ x A @ y A @ z","a @", "A @@", true));
//		assertEquals("A @ x Á @@ y A @ z", DBSString.changeStr("A @@ x Á @@ y A @@ z","a @@", "A @", false));
//		assertEquals("A @@ x A @@ y A @@ z", DBSString.changeStr("A @ x A @ y A @ z","a @", "A @@", false));
//	}
//	
//	@Test
//	public void test_getNotEmpyt() {
//		assertEquals("jony", DBSString.getNotEmpty("","jony"));
//		assertEquals("", DBSString.getNotEmpty("",""));
//		assertEquals("", DBSString.getNotEmpty(null,""));
//	}
//	
//	@Test
//	public void test_ToProper() {
//		assertEquals("Jony", DBSString.toProper("jony"));
//		assertEquals("Jony Fabio", DBSString.toProper("jony fabio"));
//		assertEquals(" Jony   Fabio", DBSString.toProper(" jony   fabio"));
//		assertEquals("Árvore Ébano", DBSString.toProper("árvore ébano"));
//		assertEquals("Árvore,Ébano", DBSString.toProper("árvore,ébano"));
//		assertEquals("Árvore,Ébano", DBSString.toProper("árvore,ébano"));
//	}
//
//	@Test
//	public void test_getNotNull() {
//		assertEquals("jony", DBSString.getNotNull("","jony"));
//		assertEquals("", DBSString.getNotNull("",""));
//		assertEquals("", DBSString.getNotNull(null,""));
//		assertEquals("Fabio", DBSString.getNotNull("Fabio","jony"));
//	}
//	
//	@Test
//	public void test_joinString() {
//		assertEquals("A, a", DBSString.joinString("A", "a"));
//		assertEquals("a", DBSString.joinString(null, "a"));
//		assertEquals("a, b, c", DBSString.joinString(null, "a","b","c"));
//		assertEquals("a, b, c", DBSString.joinString(null, "a","b","c", null));
//		assertEquals("a, b", DBSString.joinString(null, "a","b",null, null));
//		assertEquals("1, 4", DBSString.joinString("1",null,null,null,"4"));
//		assertEquals("1, 2, 3", DBSString.joinString("1","2","3",null,null,null));
//		assertEquals("1", DBSString.joinString("1"));
//		assertEquals("1, 2", DBSString.joinString("1","2",null));
//		assertEquals("1, 2", DBSString.joinString("1",null,"2"));
//		assertEquals("1", DBSString.joinString(null,null,"1"));
//	}
//	
//	@Test
//	public void test_getNumeroSemPonto() {
//	   
//		assertEquals("189988", DBSString.getNumeroSemPonto(18.9988233, 5, 4));
//		assertEquals("18339988", DBSString.getNumeroSemPonto(1833.9988233, 5, 4));
//		assertEquals("1833333998", DBSString.getNumeroSemPonto(1833333.998, 2, 3));
//		assertEquals("183333400", DBSString.getNumeroSemPonto(1833333.998, 2, 2));
//		
//	}
//
//	@Test
//	public void test_subString() {
//		assertEquals("abc", DBSString.getSubString("abcdefgh", 1, 3));
//		assertEquals("bcd", DBSString.getSubString("abcdefgh", 2, 3));
//		assertEquals("bcdefgh", DBSString.getSubString("abcdefgh", 2, 100));
//	
//	}
//	
//	@Test
//	public void test_repeat() {
//	   
//		assertEquals("aaa", DBSString.repeat("a", 3));
//		assertEquals(null, DBSString.repeat(null, 3));
//		assertEquals("aBaB", DBSString.repeat("aB", 2));
//	}
//
//
//
//	@Test
//	public void test_getFormattedDate() {
//		
//		Date xxDate = DBSDate.toDate("12/11/2011 23:59");
//		assertEquals("12/11/2011", DBSFormat.getFormattedDate(xxDate));
//
//		xxDate = DBSDate.toDate("22","04","2011");
//		assertEquals("22/04/2011", DBSFormat.getFormattedDate(xxDate));
//		
//	}
//	
//	@Test
//	public void test_getFormattedDateTime() {
//		
//		Date xxDate = DBSDate.toDateDMYHMS("12/11/2011 22:14:00");
//		assertEquals("12/11/2011 22:14:00", DBSFormat.getFormattedDateTime(xxDate));
//
//		xxDate = DBSDate.toDateDMYHMS("22/04/2011 16:34:12");
//		assertEquals("22/04/2011 16:34:12", DBSFormat.getFormattedDateTime(xxDate));
//
//		xxDate = DBSDate.toDateDMYHMS("22/4/2011 16:34:12");
//		assertEquals("22/04/2011 16:34:12", DBSFormat.getFormattedDateTime(xxDate));
//
//		xxDate = DBSDate.toDateYMDHMS("2011/04/22 16:34:12");
//		assertEquals("22/04/2011 16:34:12", DBSFormat.getFormattedDateTime(xxDate));
//
//		xxDate = DBSDate.toDateDMYHMS("22/04/2011 00:00:00");
//		assertEquals("22/04/2011 00:00:00", DBSFormat.getFormattedDateTime(xxDate));
//	}
//
//	@Test
//	public void test_getFormattedNumber() {
//		
//		double xxNumero = 2340;
//		assertEquals("2.340,00", DBSFormat.getFormattedNumber(xxNumero, "###,###,##0.00") );
//	    xxNumero = 2340;
//		assertEquals("2340", DBSFormat.getFormattedNumber(xxNumero, "####") );
//		xxNumero = 1234543456.34;
//		assertEquals("1.234.543.456,34", DBSFormat.getFormattedNumber(xxNumero, "###,###,###,##0.00") );
//		xxNumero = .34;
//		assertEquals("0,34", DBSFormat.getFormattedNumber(xxNumero, "###,###,###,##0.00") );
//		xxNumero = 2340;
//		assertEquals("2.340,00", DBSFormat.getFormattedNumber(xxNumero, DBSFormat.getNumberMask(2)) );
//		xxNumero = 29340.23;
//		assertEquals("29.340,23", DBSFormat.getFormattedNumber(xxNumero, DBSFormat.getNumberMask(2)) );
//		xxNumero = 29340.1999;
//		assertEquals("29.340,20", DBSFormat.getFormattedNumber(xxNumero, DBSFormat.getNumberMask(2)) );
//		xxNumero = 29340.2376654565656;
//		assertEquals("29.340,23767", DBSFormat.getFormattedNumber(xxNumero, DBSFormat.getNumberMask(5)) );
//		xxNumero = 29340.2376654565656;
//		assertEquals("29.340,23766 CR", DBSFormat.getFormattedNumber(xxNumero, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(5)) );
//		xxNumero = 29340.277;
//		assertEquals("29.340,27 CR", DBSFormat.getFormattedNumber(xxNumero, NUMBER_SIGN.CRDB_SUFFIX, DBSFormat.getNumberMask(2)) );
//		xxNumero = 29340.277;
//		assertEquals("29.340,28", DBSFormat.getFormattedNumber(xxNumero, DBSFormat.getNumberMask(2,true,0)) );
//		xxNumero = 29340.277;
//		assertEquals("29.340,28", DBSFormat.getFormattedNumber(xxNumero, DBSFormat.getNumberMask(2,true,-1)) );
//		
//	}
	
	@Test
	public void test_Corretor(){
//		assertEquals("participação", DBSString.corretorOrtografico("participacao"));
//		assertEquals("participações", DBSString.corretorOrtografico("participacoes"));
////		System.out.println(DBSString.CorretorOrtografico("Brc Securitizadora Sa"));
//		assertEquals("Acordo", DBSString.corretorOrtografico("Acordo"));
//		assertEquals("Alcool", DBSString.corretorOrtografico("Alcol"));
//		assertEquals("Aço", DBSString.corretorOrtografico("Aco"));
//		assertEquals("Teste S.A.", DBSString.corretorOrtografico("Teste sa"));
//		assertEquals("Teste   S.A.", DBSString.corretorOrtografico("Teste   s/a"));
//		assertEquals("Teste  S.A.", DBSString.corretorOrtografico("Teste  s.a"));
//		assertEquals("Teste  S.A. metal", DBSString.corretorOrtografico("Teste  s.a metal"));
//		assertEquals("Teste  S.A. metal", DBSString.corretorOrtografico("Teste  sa metal"));
//		assertEquals("FI de FI", DBSString.corretorOrtografico("Fundo de Investimento de Fundo de Investimento"));
//		assertEquals("FIC de FI", DBSString.corretorOrtografico("Fundo de Investimento em Cotas de Fundo de Investimento"));
//		assertEquals("Brasil Corporativo FIC de FI Multimercado Crédito Privado", DBSString.corretorOrtografico(DBSString.toProper("Brasil Corporativo FIC de Fundo de Invest. Multimercado Crédito Privado")));
//		assertEquals("Ejas Multimercado Crédito Privado Investimento No Exterior - FIC de FI",DBSString.corretorOrtografico(DBSString.toProper("EJAS MULTIMERCADO CRÉDITO PRIVADO INVESTIMENTO NO EXTERIOR - FDO DE INVEST.EM COTAS DE FDOS DE INVES")));
//		assertEquals("FIC de FI Multimercado Crédito Privado",DBSString.corretorOrtografico(DBSString.toProper("FDO DE INVEST.EM COTAS DE FDOS DE INVEST.MULTIMERCADO CRÉDITO PRIVADO")));
//		assertEquals("FIC de FI Previdenciário Caixa Multimercado Renda Variável 0/15 150", DBSString.corretorOrtografico(DBSString.toProper("FDO DE INV EM COTAS DE FDOS DE INV PREVIDENCIÁRIO CAIXA MULTIMERCADO RENDA VARIÁVEL 0/15 150")));
//		assertEquals("Crédito Privado - FIC de FI Multimercado",DBSString.corretorOrtografico(DBSString.toProper("CRÉDITO PRIVADO-FDO.DE INVEST.EM COTAS DE FUNDOS DE INVESTIMENTO MULTIMERCADO")));
//		assertEquals("FIC de FI Multimercado Crédito Privado",DBSString.corretorOrtografico(DBSString.toProper("FUNDO DE INVESTIMENTO DE COTAS DE FUNDOS DE INVESTIMENTO MULTIMERCADO CRÉDITO PRIVADO")));
		String xResult;
		xResult = DBSString.corretorOrtografico(DBSString.toProper("VERTRA INSTITUCIONAL FUNDO DE INVESTIMENTO EM COTAS DE FUNDOS DE INVESTIMENTO EM ACOES"));
		assertEquals("Vertra Institucional FIC de FIA",xResult);
		xResult = DBSString.corretorOrtografico(DBSString.toProper("BNP FUNDO DE INVESTIMENTO MULTIMERCADO CREDITO PRIVADO - INVESTIMENTO NO EXTERIOR"));
		assertEquals("Bnp FI Multimercado Crédito Privado - Investimento No Exterior",xResult);
		xResult = DBSString.corretorOrtografico(DBSString.toProper("BB ACOES DIVIDENDOS FUNDO DE INVESTIMENTO EM COTAS DE FUNDOS DE INVESTIMENTO"));
		assertEquals("BB Ações Dividendos FIC de FI",xResult);
	}

}
