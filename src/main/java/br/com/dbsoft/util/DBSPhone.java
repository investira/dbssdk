package br.com.dbsoft.util;

public class DBSPhone {

	/**
	 * Retorna o DDI a partir de telefone completo (DDI+DDD se houver) já formatado.
	 * @param pPhone
	 * @return
	 */
	public static String getDDI(String pPhone){
		String[] xArray = pvPhoneToArray(pPhone);
		if (xArray.length == 4){
			return xArray[xArray.length - 4];
		}else{
			return "";
		}
	}
	/**
	 * Retorna o DDD a partir de telefone completo(DDI+DDD se houver) já formatado.
	 * @param pPhone
	 * @return
	 */
	public static String getDDD(String pPhone){
		String[] xArray = pvPhoneToArray(pPhone);
		if (xArray.length >= 3){
			return xArray[xArray.length - 3];
		}else{
			return "";
		}
	}
	/**
	 * Retorna o número a partir de telefone completo(DDI+DDD se houver) já formatado.
	 * @param pPhone
	 * @return
	 */
	public static String getNumber(String pPhone){
		String[] xArray = pvPhoneToArray(pPhone);
		if (xArray.length >= 2){
			return xArray[xArray.length - 2] + "-" + xArray[xArray.length - 1];
		}else{
			return "";
		}
	}
	
	private static String[] pvPhoneToArray(String pPhone){
		if (pPhone==null){return null;}
		return pPhone.split("[()-]");
	}
}
