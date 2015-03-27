package br.com.dbsoft.util;

public class DBSHtml {
	
	/**
	 * Retorna html contendo o valor em informado <b>pValue<b/> dento do tab informada em <b>pTab<b/>.<br/>
	 * O tab <b>não</b> deve vir com os sinais <>.
	 * @param pTag
	 * @param pString
	 */
	public static String encodeTag(String pTag, String pValue){
		if (pTag == null){return null;}
		return encodeTagBegin(pTag) + pValue + encodeTagEnd(pTag);
	}
	/**
	 * Retorna html contendo o valor em informado <b>pValue<b/> dento do tab informada em <b>pTab<b/>.<br/>
	 * O tab <b>não</b> deve vir com os sinais <>.
	 * @param pTag
	 * @param pString
	 */
	public static String encodeTagBegin(String pTag){
		if (pTag == null){return null;}
		return "<" + pTag + ">";
	}
	/**
	 * Retorna html contendo o valor em informado <b>pValue<b/> dento do tab informada em <b>pTab<b/>.<br/>
	 * O tab <b>não</b> deve vir com os sinais <>.
	 * @param pTag
	 * @param pString
	 */
	public static String encodeTagEnd(String pTag){
		if (pTag == null){return null;}
		return "</" + pTag + ">";
	}
}
