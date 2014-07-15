package br.com.dbsoft.enums;

public class DBSSDKEnums {

//	public static enum EMPRESTIMO_TIPO{
//		DIARIO			("Diário", 0),		//Diário não está sendo utilizado
//		ANTECIPADO		("Antecipado", 1), 	//O valor do principal é calculado no início com base no pu de fechamento do dia anterior
//		VENCIMENTO		("Vencimento", 2);	//O valor do principal é calculado diáriamente até o final com base no pu de fechamento de D -1
//		
//		private String 	wName;
//		private int 	wCode;
//		
//		private EMPRESTIMO_TIPO(String pName, int pCode) {
//			this.wName = pName;
//			this.wCode = pCode;
//		}
//
//		public String getName() {
//			return wName;
//		}
//
//		public int getCode() {
//			return wCode;
//		}
//		
//		
//		public static EMPRESTIMO_TIPO get(Object pCode){
//			if (pCode instanceof EMPRESTIMO_TIPO) {
//				return (EMPRESTIMO_TIPO) pCode;
//			}
//			return get(DBSNumber.toInteger(pCode, null));
//		}
//		
//		public static EMPRESTIMO_TIPO get(Integer pCode) {
//			if (pCode == null){
//				return null;
//			}			
//			switch (pCode) {
//			case 0:
//				return DIARIO;
//			case 1:
//				return ANTECIPADO;
//			case 2:
//				return VENCIMENTO;
//			default:
//				return DIARIO;
//			}
//		}	
//	}
	

}
