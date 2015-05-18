package br.com.dbsoft.util;

public class DBSHtml {
	
//	private static Logger		wLogger = Logger.getLogger(DBSHtml.class);
	

	/**
	 * Retorna html contendo o valor em informado <b>pValue</b> dento do tag informada em <b>pTag</b>.<br/>
	 * O tag <b>não</b> deve vir com os sinais <br/>.
	 * @param pTag Nome do tag
	 * @param pContent Conteúdo dentro do tag
	 * @return
	 */
	public static String encodeTag(String pTag, Object pContent){
		return encodeTag(pTag, pContent, null);
	}

	/**
	 * Retorna html contendo o valor em informado <b>pVapContentlue</b> dento do tag informada em <b>pTag</b>.<br/>
	 * O tag <b>não</b> deve vir com os sinais <br/>.
	 * @param pTag Nome do tag
	 * @param pContent Conteúdo dentro do tag. Se for nulo, faz o encode sem conteúdo.
	 * @param pTagAttributes Atributos do tag
	 * @return
	 */
	public static String encodeTag(String pTag, Object pContent, String pTagAttributes){
		if (pTag == null){return null;}
		return encodeTagBegin(pTag, pTagAttributes) + DBSString.toString(pContent, "") + encodeTagEnd(pTag);
	}

	/**
	 * Retorna html contendo a inicialização do tag informada em <b>pTag</b>.<br/>
	 * O tag <b>não</b> deve vir com os sinais <br/>.
	 * @param pTag
	 * @param pString
	 */
	public static String encodeTagBegin(String pTag){
		return encodeTagBegin(pTag, null);
	}

	/**
	 * Retorna html contendo o valor em informado <b>pValue</b> dento do tag informada em <b>pTag</b>.<br/>
	 * O tag <b>não</b> deve vir com os sinais <br/>.
	 * @param pTag
	 * @param pString
	 */
	public static String encodeTagBegin(String pTag, String pAttributes){
		if (pTag == null){return null;}
		return "<" + pTag + " " + DBSString.getNotNull(pAttributes, "") + ">";
	}

	/**
	 * Retorna html com a filalização do tag informada em <b>pTag</b>.<br/>
	 * O tag <b>não</b> deve vir com os sinais <br/>.
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
