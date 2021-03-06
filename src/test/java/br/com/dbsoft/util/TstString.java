package br.com.dbsoft.util;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.dbsoft.util.DBSString;

public class TstString {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void toArrayRegex(){
		ArrayList <String> xA = new ArrayList<String>();
		ArrayList <String> xB=  new ArrayList<String>();
		ArrayList <String> xC=  new ArrayList<String>();
		xA.add("a");
		xA.add("b");
		xA.add("c");
		xA.add("d");
		xA.add("e");
		xA.add("f");
		Assert.assertEquals(xA, DBSString.toArrayListRegex("a b ,c ,d;e;f","[,;\\s]"));
		Assert.assertEquals(xA, DBSString.toArrayListRegex("a,b;c,;,d;e;f","[,;\\s]"));
	}
	
	@Test
	public void emails(){
		assertTrue(DBSEmail.isValidEmailAddress("asdc@asdc.com"));
		assertTrue(DBSEmail.isValidEmailAddress("asdc@asdc.com.br"));
		assertTrue(DBSEmail.isValidEmailAddress("asdc@asdc.com.br.asdc"));
		assertTrue(DBSEmail.isValidEmailAddress("sdc@asdc.com.br.asdc"));
		assertTrue(DBSEmail.isValidEmailAddress("asdc@asdc.br"));
		assertFalse(DBSEmail.isValidEmailAddress("asdc.?[asdc@asdc.com.br.asdc"));
		assertFalse(DBSEmail.isValidEmailAddress("asdcasdc"));
		assertFalse(DBSEmail.isValidEmailAddress(""));
		assertFalse(DBSEmail.isValidEmailAddress("123ed\12@qws"));
		assertFalse(DBSEmail.isValidEmailAddress("asdc@asdc"));
	}
	
	@Test
	public void test_ascii(){
//		System.out.println(DBSString.toASCII("É"));
//		pValue = DBSString.changeStr((String)pValue, "╔", "É");
//		pValue = DBSString.changeStr((String)pValue, "ß", "á");
		assertEquals("╔", DBSString.toASCII("É"));
	}
	
	@Test
	public void test_isAlphabetic(){
		Assert.assertEquals(true,DBSString.isAlphabetic("ad    sd cd"));
		Assert.assertEquals(true,DBSString.isAlphabetic("adsdcd"));
		Assert.assertEquals(false,DBSString.isAlphabetic("ad1    sd cd"));
		Assert.assertEquals(false,DBSString.isAlphabetic("ad1sdcd"));
		Assert.assertEquals(false,DBSString.isAlphabetic("1232"));
		Assert.assertEquals(false,DBSString.isAlphabetic("-="));
		Assert.assertEquals(false,DBSString.isAlphabetic("dbsoft@#-="));
	}
	
	@Test
	public void test_toArrayList() {

		ArrayList <String> xA = new ArrayList<String>();
		ArrayList <String> xB=  new ArrayList<String>();
		ArrayList <String> xC=  new ArrayList<String>();
		xA.add("d");
		xA.add("d");
		xA.add("d");
		xA.add("d");

		xB.add("dxdxdxd");
		
		xC.add("");
		xC.add("");
		xC.add("");
		xC.add("");
		
		assertEquals(xA, DBSString.toArrayList("d d d d", " "));
		assertEquals(xA, DBSString.toArrayList("dXdXdXd", "X"));
		assertEquals(xA, DBSString.toArrayList("dxdxdxd", "X",false));
		assertEquals(xB, DBSString.toArrayList("dxdxdxd", "X"));
//		String xS =  new String("abcd");
//		xS.indexOf("a", 1);

		assertEquals(xC, DBSString.toArrayList(";;;", ";"));
	}

	@Test
	public void test_toArray() {
		String [] xArray;
		xArray = DBSString.toArray("1", "2");
		assertEquals(2, xArray.length);
		xArray = DBSString.toArrayNotNull("1", null, "3", "4");
		assertEquals(3, xArray.length);
		xArray = DBSString.toArrayNotEmpty("1", "2", "3", "", "5");
		assertEquals(4, xArray.length);
	}
	
	@Test
	public void test_getInStr() {
		assertEquals(3, DBSString.getInStr("XXAXXXXXXXX", "A"));
		assertEquals(7, DBSString.getInStr("GFGFGFFABIOHDHDHDH", "FABIO"));
		assertEquals(0, DBSString.getInStr("GFGFGFFABIOHDHDHDH", "JOSE"));
		assertEquals(0, DBSString.getInStr("", "HJHJ"));
		assertEquals(0, DBSString.getInStr("AAXX", "xx"));
		assertEquals(0, DBSString.getInStr("AAA", ""));
		assertEquals(0, DBSString.getInStr(null, ""));
		assertEquals(0, DBSString.getInStr("GHJGHJGJH", null));
		assertEquals(0, DBSString.getInStr(null, null));

		assertEquals(3, DBSString.getInStr("XXAXXXXXXXX", "A",1));
		assertEquals(7, DBSString.getInStr("GFGFGFFABIOHDHDHDH", "FABIO",5));
		assertEquals(0, DBSString.getInStr("GFGFGFFABIOHDHDHDHJOSE", "JOSE",20));
		assertEquals(0, DBSString.getInStr("", "HJHJ",0));
		assertEquals(0, DBSString.getInStr("AAXX", "xx",0));
		assertEquals(2, DBSString.getInStr("AAA", "A",2));
		assertEquals(0, DBSString.getInStr(null, "",0));
		assertEquals(0, DBSString.getInStr("GHJGHJGJH", null,0));
		assertEquals(0, DBSString.getInStr(null, null,0));
		
	}
	
	@Test
	public void test_getStringCount() {
		
		assertEquals(6, DBSString.getStringCount("AAAXAAXXXAAXXXAAXAABABABAA", "AA"));
		assertEquals(4, DBSString.getStringCount("AAAXAAXXXAAXXXAAXA", "AA"));
		assertEquals(0, DBSString.getStringCount("AAAXAAXXXAAXXXAAXA", "aa"));
		assertEquals(0, DBSString.getStringCount(null, "aa"));
		assertEquals(0, DBSString.getStringCount("AAAAAA", null));
		assertEquals(0, DBSString.getStringCount(null, null));
	}
	
	@Test
	public void test_changeStr() {
		assertEquals("jony  fabio  neto", DBSString.changeStr("jony, fabio, neto",",", " ", false));
		assertEquals("jony fabio neto", DBSString.changeStr("jony,fabio,neto",",", " ", false));
		assertEquals(":abcxyz", DBSString.changeStr(":abc :xyz"," :", ""));
		assertEquals("jony", DBSString.changeStr("FABIO","FABIO", "jony"));
		assertEquals("FABIO", DBSString.changeStr("FABIO","Fabio", "jony", true));
		assertEquals("jony", DBSString.changeStr("FÁBIO","Fábio", "jony", false));
		assertEquals("jony neto", DBSString.changeStr("jony fabio neto","fabio ", "", false));
		assertEquals("jony neto", DBSString.changeStr("jony Fábio neto","fÁbio ", "", false));
		assertEquals("aassfabioaasss", DBSString.changeStr("aassFABIOaasss","FABIO", "fabio"));
		assertEquals("abcdefgh", DBSString.changeStr("abcfgh","abc", "abcde"));
		assertEquals("abcdefgh", DBSString.changeStr("abcfgh","abc", "abcde", false));
		assertEquals("A @@ x Á @@ y A @ z", DBSString.changeStr("A @@ x Á @@ y a @@ z","a @@", "A @", true));
		assertEquals("A @ x A @ y A @ z", DBSString.changeStr("A @ x A @ y A @ z","a @", "A @@", true));
		assertEquals("A @ x Á @@ y A @ z", DBSString.changeStr("A @@ x Á @@ y A @@ z","a @@", "A @", false));
		assertEquals("A @@ x A @@ y A @@ z", DBSString.changeStr("A @ x A @ y A @ z","a @", "A @@", false));
	}
	
	@Test
	public void test_getNotEmpyt() {
		assertEquals("jony", DBSString.getNotEmpty("","jony"));
		assertEquals("", DBSString.getNotEmpty("",""));
		assertEquals("", DBSString.getNotEmpty(null,""));
	}
	
	@Test
	public void test_ToProper() {
		assertEquals("Jony", DBSString.toProper("jony"));
		assertEquals("Jony Fabio", DBSString.toProper("jony fabio"));
		assertEquals(" Jony   Fabio", DBSString.toProper(" jony   fabio"));
		assertEquals("Árvore Ébano", DBSString.toProper("árvore ébano"));
		assertEquals("Árvore,Ébano", DBSString.toProper("árvore,ébano"));
		assertEquals("Árvore,Ébano", DBSString.toProper("árvore,ébano"));
	}

	@Test
	public void test_getNotNull() {
		assertEquals("jony", DBSString.getNotNull("","jony"));
		assertEquals("", DBSString.getNotNull("",""));
		assertEquals("", DBSString.getNotNull(null,""));
		assertEquals("Fabio", DBSString.getNotNull("Fabio","jony"));
	}
	
	@Test
	public void test_joinString() {
		assertEquals("A, a", DBSString.joinString("A", "a"));
		assertEquals("a", DBSString.joinString(null, "a"));
		assertEquals("a, b, c", DBSString.joinString(null, "a","b","c"));
		assertEquals("a, b, c", DBSString.joinString(null, "a","b","c", null));
		assertEquals("a, b", DBSString.joinString(null, "a","b",null, null));
		assertEquals("1, 4", DBSString.joinString("1",null,null,null,"4"));
		assertEquals("1, 2, 3", DBSString.joinString("1","2","3",null,null,null));
		assertEquals("1", DBSString.joinString("1"));
		assertEquals("1, 2", DBSString.joinString("1","2",null));
		assertEquals("1, 2", DBSString.joinString("1",null,"2"));
		assertEquals("1", DBSString.joinString(null,null,"1"));
	}
	
	@Test
	public void test_getNumeroSemPonto() {
	   
		assertEquals("189988", DBSString.getNumeroSemPonto(18.9988233, 5, 4));
		assertEquals("18339988", DBSString.getNumeroSemPonto(1833.9988233, 5, 4));
		assertEquals("1833333998", DBSString.getNumeroSemPonto(1833333.998, 2, 3));
		assertEquals("183333400", DBSString.getNumeroSemPonto(1833333.998, 2, 2));
		
	}

	@Test
	public void test_subString() {
		assertEquals("abc", DBSString.getSubString("abcdefgh", 1, 3));
		assertEquals("bcd", DBSString.getSubString("abcdefgh", 2, 3));
		assertEquals("bcdefgh", DBSString.getSubString("abcdefgh", 2, 100));
	
	}
	
	@Test
	public void test_repeat() {
	   
		assertEquals("aaa", DBSString.repeat("a", 3));
		assertEquals(null, DBSString.repeat(null, 3));
		assertEquals("aBaB", DBSString.repeat("aB", 2));
	}



	@Test
	public void test_getFormattedDate() {
		
		Date xxDate = DBSDate.toDate("12/11/2011 23:59");
		assertEquals("12/11/2011", DBSFormat.getFormattedDate(xxDate));

		xxDate = DBSDate.toDate("22","04","2011");
		assertEquals("22/04/2011", DBSFormat.getFormattedDate(xxDate));
		
	}
	
	@Test
	public void test_getFormattedDateTime() {
		
		Date xxDate = DBSDate.toDateDMYHMS("12/11/2011 22:14:00");
		assertEquals("12/11/2011 22:14:00", DBSFormat.getFormattedDateTimes(xxDate));

		xxDate = DBSDate.toDateDMYHMS("22/04/2011 16:34:12");
		assertEquals("22/04/2011 16:34:12", DBSFormat.getFormattedDateTimes(xxDate));

		xxDate = DBSDate.toDateDMYHMS("22/4/2011 16:34:12");
		assertEquals("22/04/2011 16:34:12", DBSFormat.getFormattedDateTimes(xxDate));

		xxDate = DBSDate.toDateYMDHMS("2011/04/22 16:34:12");
		assertEquals("22/04/2011 16:34:12", DBSFormat.getFormattedDateTimes(xxDate));

		xxDate = DBSDate.toDateDMYHMS("22/04/2011 00:00:00");
		assertEquals("22/04/2011 00:00:00", DBSFormat.getFormattedDateTimes(xxDate));
	}


	
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
		xResult = DBSString.corretorOrtografico(DBSString.toProper("ACRUX SECURITIZADORA SA"));
		assertEquals("Acrux Securitizadora S.A.",xResult);
		xResult = DBSString.corretorOrtografico(DBSString.toProper("BB NOSSA CAIXA REFERENCIADO DI LP 50 FUNDO INVEST COTAS FUNDOS INVEST"));
		assertEquals("BB Nossa Caixa Referenciado DI LP 50 FIC de FI",xResult);
		xResult = DBSString.corretorOrtografico(DBSString.toProper("a ACOES"));
		assertEquals("A Ações",xResult);
		xResult = DBSString.corretorOrtografico(DBSString.toProper("TARPON 1016 FUNDO INVEST COTAS FUNDOS INVEST ACOES"));
		assertEquals("Tarpon 1016 FIC de FIA",xResult);
		xResult = DBSString.corretorOrtografico(DBSString.toProper("BB NOSSA CAIXA REFERENCIADO DI LP 50 FUNDO INVEST COTAS FUNDOS INVEST"));
		assertEquals("BB Nossa Caixa Referenciado DI LP 50 FIC de FI",xResult);

//		System.out.println(DBSString.corretorOrtografico(DBSString.toProper("TARPON 1016 FUNDO INVEST COTAS FUNDOS INVEST ACOES")));
//		assertEquals("Bb Nossa Caixa Referenciado DI LP 50 FIC Fundos Invest",xResult);
	}
	




}
