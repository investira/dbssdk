package br.com.dbsoft.util;

public class DBSHtml {
	
	/**
	 * Retorna html contendo o valor em informado <b>pValue<b/> dento do tab informada em <b>pTab<b/>.<br/>
	 * O tab <b>não</b> deve vir com os sinais <>.
	 * @param pTag
	 * @param pString
	 */
	public static String encodeTag(String pTag, Object pValue){
		return encodeTag(pTag, pValue, null);
	}

	/**
	 * Retorna html contendo o valor em informado <b>pValue<b/> dento do tab informada em <b>pTab<b/>.<br/>
	 * O tab <b>não</b> deve vir com os sinais <>.
	 * @param pTag
	 * @param pString
	 */
	public static String encodeTag(String pTag, Object pValue, String pAttributes){
		if (pTag == null){return null;}
		return encodeTagBegin(pTag, pAttributes) + DBSString.toString(pValue, "") + encodeTagEnd(pTag);
	}

	/**
	 * Retorna html contendo o valor em informado <b>pValue<b/> dento do tab informada em <b>pTab<b/>.<br/>
	 * O tab <b>não</b> deve vir com os sinais <>.
	 * @param pTag
	 * @param pString
	 */
	public static String encodeTagBegin(String pTag){
		return encodeTagBegin(pTag, null);
	}

	/**
	 * Retorna html contendo o valor em informado <b>pValue<b/> dento do tab informada em <b>pTab<b/>.<br/>
	 * O tab <b>não</b> deve vir com os sinais <>.
	 * @param pTag
	 * @param pString
	 */
	public static String encodeTagBegin(String pTag, String pAttributes){
		if (pTag == null){return null;}
		return "<" + pTag + " " + DBSString.getNotNull(pAttributes, "") + ">";
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

	public static String encodeTagBR(){
		return "<br/>";
	}
	
	
}
