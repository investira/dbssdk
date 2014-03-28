package br.com.dbsoft.tmp;

import java.text.NumberFormat;
import java.text.ParseException;

import org.junit.Test;

public class lixo  {

//	public static class ConfirmacaoAgendamento{
//		public static final int OPERACAO_NAO_EFETUADA = 1;   	// Status Inicial, quando da inclusão do agendamento
//		public static final int OPERACAO_EFETUADA = 2;      	// Usuário indica que operação agendada foi efetuada
//		public static final int OPERACAO_PENDENTE = 3;      	// Usuário indica que operação esta pendente
//	}

	public static enum ConfirmacaoAgendamento {
		OPERACAO_NAO_EFETUADA 		("Não Efetuada", 1),
		OPERACAO_EFETUADA 			("Efetuada", 2),
		OPERACAO_PENDENTE			("Pendente", 3);
		
		private String 	wString;
		private Integer wInt;
		
		private ConfirmacaoAgendamento(String pString, int pInt) {
			this.wString = pString;
			this.wInt = pInt;
		}

		@Override
		public String toString() {
			return wString;
		}

		public Integer toInt() {
			return wInt;
		}
		
		public static ConfirmacaoAgendamento get(int pInt) {
			switch (pInt) {
			case 0:
				return ConfirmacaoAgendamento.OPERACAO_NAO_EFETUADA;
			case 1:
				return ConfirmacaoAgendamento.OPERACAO_EFETUADA;
			default:
				return ConfirmacaoAgendamento.OPERACAO_PENDENTE;
			}
		}		
	}
	


	public static enum NULO{
		NAO_EXIBIR,
		NENHUM,
		NENHUMA,
		DEFAULT,
		PADRAO,
		TODOS,
		TODAS,
		BRANCO;
		
		String toString;

		NULO(String toString) {
			this.toString = toString;
		}

		NULO() {}

		@Override
		public String toString() {
		switch (this) {
			case NAO_EXIBIR:
				return "";
			case NENHUM:
				return "Nenhum";
			case NENHUMA:
				return "Nenhuma";
			case DEFAULT:
				return "Default";
			case PADRAO:
				return "Padrão";
			case TODOS:
				return "Todos";
			case TODAS:
				return "Todas";
			case BRANCO:
				return "Branco";
			default:
				return "";
			}
		}	
	}
	
	public static enum DIALOG_ICON
	{
		NENHUM 		("n"),
	    ATENCAO 	("a"),
	    CONFIRMAR	("c"),
	    ERRO 		("e"),
	    IGNORAR 	("g"),
	    INFORMACAO	("i"),
	    PROIBIDO 	("p"),
	    SOBRE 		("b"),
	    SUCESSO		("s");

	    String wString;

	    DIALOG_ICON (String pString){
	    	wString = pString;
	    }
	
	    @Override
		public String toString() {
	    	return wString;
	    }
	}


	public void teste(){
		System.out.println(NULO.PADRAO.toString());
	}	
	
	public void teste2(){
		NumberFormat xNF = NumberFormat.getInstance();
		try {
			Double number = xNF.parse("0,0").doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	 
	public void teste4(){
		System.out.println(ConfirmacaoAgendamento.get(1).toString());
	}
	
//	@Test 
	public void teste5(){
		System.out.println(DIALOG_ICON.ATENCAO);
		System.out.println(DIALOG_ICON.ATENCAO.toString());
		System.out.println(DIALOG_ICON.ATENCAO.name());
		System.out.println(DIALOG_ICON.valueOf("c"));
	}
	
	@Test 
	public void nomedome(){
//		System.out.println(DBSDate.getNomeDoMes(DBSDate.getNowDate()));
//		Calendar cal = Calendar.getInstance();
//	    System.out.println(new SimpleDateFormat("MMMM").format(cal.getTime()));
	}

}
