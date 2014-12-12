package br.com.dbsoft.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DBSFormat {
	
	public class MASK{
		public static final String QUANTIDADE_INTEIRA = "###,##0";
		public static final String FINANCEIRO = "###,##0.00";
	}
	
	public static enum NUMBER_SIGN{
		NONE,
		CRDB_PREFIX,
		CRDB_SUFFIX,
		MINUS_SUFFIX,
		MINUS_PREFIX,
		PARENTHESES;
	}
	
	//######################################################################################################### 
	//## public properties                                                                                        #
	//#########################################################################################################

	/**
	 * Retorna string contendo a data reduzida formatada no padrão dd/mm/yyyy
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDate(Long pDate){
		return getFormattedDate(DBSDate.toDate(pDate));
	}
	
	public static String getFormattedDate(Object pDate){
		if (DBSObject.isEmpty(pDate)){
			return "";
		}else{
			SimpleDateFormat xFormat = new SimpleDateFormat("dd/MM/yyyy");
			return xFormat.format(DBSDate.toDate(pDate));
		}
	}
	
	/**
	 * Retorna string contendo a data reduzida formatada no padrão dd/mm/yyyy
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDate(Date pDate){
		if (pDate == null){
			return "";
		}else{
			SimpleDateFormat xFormat = new SimpleDateFormat("dd/MM/yyyy");
			return xFormat.format(pDate);
		}
	}
	/**
	 * Retorna string contendo a data reduzida formatada no padrão dd/mm/yyyy
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDate(Timestamp pDate){
		if (pDate == null){
			return "";
		}else{
			Date xDate = DBSDate.toDate(pDate);
			return getFormattedDate(xDate);
		}
	}
	
	public static String getFormattedAno(Object pDate) {
		if (pDate == null){
			return "";
		}else{
			SimpleDateFormat xFormat = new SimpleDateFormat("yyyy");
			return xFormat.format(DBSDate.toDate(pDate));
		}
	}

	/**
	 * Retorna string contendo a data formatada contendo data e hora
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDateTime(Long pLong){
		if (pLong == null){
			return "";
		}else{
	
			SimpleDateFormat xFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return xFormat.format(pLong);
		}
	}

	/**
	 * Retorna string contendo a data formatada contendo data e hora
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDateTime(Date pDate){
		SimpleDateFormat xFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return xFormat.format(pDate);
	}

	/**
	 * Retorna string contendo a data formatada contendo data e hora
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDateTime(Timestamp pDate){
		Date xDate = DBSDate.toDate(pDate);
		return getFormattedDateTime(xDate);
	}
	/**
	 * Retorna string contendo a data formatada contendo hora, minutos e segundos
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedTime(Date pDate){
		SimpleDateFormat xFormat = new SimpleDateFormat("HH:mm:ss");
		return xFormat.format(pDate.getTime());
	}
	/**
	 * Retorna string contendo a data formatada de acordo com a mascara escolida
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDateCustom(Object pDate, String pMask){
		SimpleDateFormat xFormat = new SimpleDateFormat(pMask);
		return xFormat.format(DBSDate.toDate(pDate));
	}
	/**
	 * Retorna string contendo a data formatada hora, minutos e segundos
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedTime(Timestamp pDate){
		Date xDate = DBSDate.toDate(pDate);
		return getFormattedTime(xDate);
	}

	
	
	public static String getFormattedNumberUnsigned(Double pValue, Object pDecimalPlaces){
		return getFormattedNumber(pValue, NUMBER_SIGN.NONE, getNumberMask(DBSNumber.toInteger(pDecimalPlaces)));
	}
	/**
	 * Formata número sem sinal.
	 * @param pValue
	 * @param pDecimalPlaces
	 * @return
	 */
	public static String getFormattedNumberUnsigned(Object pValue, Object pDecimalPlaces){
		return getFormattedNumber(DBSNumber.toDouble(pValue), NUMBER_SIGN.NONE, getNumberMask(DBSNumber.toInteger(pDecimalPlaces)));
	}

	/**
	 * Formata número com sinal negativo no prefixo(caso seja número negativo).
	 * @param pValue
	 * @param pDecimalPlaces
	 * @return
	 */
	public static String getFormattedNumber(Object pValue, Object pDecimalPlaces){
		return getFormattedNumber(DBSNumber.toDouble(pValue), NUMBER_SIGN.MINUS_PREFIX, getNumberMask(DBSNumber.toInteger(pDecimalPlaces)));
	}

	/**
	 * Formata número com sinal negativo no prefixo(caso seja número negativo).
	 * @param pValue
	 * @param pDecimalPlaces
	 * @return
	 */
	public static String getFormattedNumber(Double pValue, Object pDecimalPlaces){
		return getFormattedNumber(pValue, NUMBER_SIGN.MINUS_PREFIX, getNumberMask(DBSNumber.toInteger(pDecimalPlaces)));
	}

	/**
	 * Formata número com sinal de negativo no prefixo(caso seja número negativo).
	 * @param pValue
	 * @param pDecimalPlaces
	 * @return
	 */
	public static String getFormattedNumber(BigDecimal pValue, Object pDecimalPlaces){
		return getFormattedNumber(DBSNumber.toDouble(pValue), pDecimalPlaces);
	}
	
	/**
	 * Formata número com duas casas decimais e sinal negativo no prefixo(caso seja número negativo).
	 * @param pValor
	 * @return
	 */
	public static String getFormattedCurrency(Double pValor){
		return getFormattedCurrency(pValor, NUMBER_SIGN.MINUS_PREFIX);
	}

	/**
	 * Formata número com duas casas decimais e sinal negativo no prefixo(caso seja número negativo).
	 * @param pValor
	 * @return
	 */
	public static String getFormattedCurrency(Object pValor){
		return getFormattedCurrency(DBSNumber.toDouble(pValor));
	}

	/**
	 * Formata número com duas casas decimais e sinal negativo no prefixo(caso seja número negativo).
	 * @param pValor
	 * @return
	 */
	public static String getFormattedCurrency(Object pValor, NUMBER_SIGN pSign){
		return getFormattedCurrency(DBSNumber.toDouble(pValor), pSign);
	}

	/**
	 * Formata número com duas casas decimais e sinal negativo no prefixo(caso seja número negativo).
	 * @param pValor
	 * @return
	 */
	public static String getFormattedCurrency(Double pValor, NUMBER_SIGN pSign){
		return getFormattedNumber(pValor, pSign, MASK.FINANCEIRO);
	}


	public static String getFormattedNumber(Double pValor, NUMBER_SIGN pSign, String pNumberMask){
		if (DBSObject.isEmpty(pValor)) {
			return null;
		}
		DecimalFormatSymbols xOtherSymbols = new DecimalFormatSymbols();
		xOtherSymbols.setDecimalSeparator(',');
		xOtherSymbols.setGroupingSeparator('.');
		DecimalFormat xDF = new DecimalFormat(pNumberMask, xOtherSymbols);
		xDF.setRoundingMode(RoundingMode.DOWN);
		switch (pSign) {
			case NONE:
				xDF.setNegativePrefix("");
				xDF.setNegativeSuffix("");
				break;
			case CRDB_PREFIX:
				xDF.setPositivePrefix("CR ");
				xDF.setNegativePrefix("DB ");
				xDF.setNegativeSuffix("");
				break;
			case CRDB_SUFFIX:
				xDF.setPositiveSuffix(" CR");
				xDF.setNegativeSuffix(" DB");
				xDF.setNegativePrefix("");
				break;
			case MINUS_PREFIX:
				xDF.setPositivePrefix("");
				xDF.setNegativePrefix("-");
				xDF.setNegativeSuffix("");
				break;
			case MINUS_SUFFIX:
				xDF.setPositivePrefix("");
				xDF.setNegativePrefix("");
				xDF.setNegativeSuffix("-");
				break;
			case PARENTHESES:
				xDF.setPositivePrefix("");
				xDF.setNegativePrefix("(");
				xDF.setNegativeSuffix(")");
				break;
			default:
				xDF.setPositivePrefix("");
				xDF.setNegativePrefix("-");
				xDF.setNegativeSuffix("");
				break;
		}
		return xDF.format(pValor);
	}
	
	public static String getNumberMask(int pDecimalPlaces, boolean pUseSeparator){
		return getNumberMask(pDecimalPlaces, pUseSeparator, 0);
	}
	public static String getNumberMask(int pDecimalPlaces, int pLeadingZeroSize){
		return getNumberMask(pDecimalPlaces, false, pLeadingZeroSize);
	}
	public static String getNumberMask(int pDecimalPlaces){
		return getNumberMask(pDecimalPlaces, true,0 );
	}
	
	/**
	 * Retorna uma máscara para ser utilizada no comando DecimalFormat do JAVA para formatar valores numéricos
	 * @return mascará do formato 
	 */
	public static String getNumberMask(int pDecimalPlaces, boolean pUseSeparator, int pLeadingZeroSize){
		String xF;
		
		if (pUseSeparator){
			xF = "#,##0";
		}else{
			if (pLeadingZeroSize > 0){
				xF = DBSString.repeat("0",pLeadingZeroSize);
			}else{
				xF = "##0";
			}
		}
		if (pDecimalPlaces < 0){
			return null;
		}
		if (pDecimalPlaces > 0){
			xF = xF + "." + DBSString.repeat("0",pDecimalPlaces);
		}
		return xF;
	}
	
	/**
	 * Retorna string contendo valor formatado sem a separação por milhar
	 * @param pValor Valor a ser formatado
	 * @param pCasasDecimais Quantidade de Casas decimais a serem consideradas
	 * @return número(String) formatado
	 */
	public static String getNumeroSemPontoMilhar(Double pValor, int pCasasDecimais){
		if (DBSObject.isEmpty(pValor)){
			return null;
		}
		String xS = getFormattedNumber(pValor, getNumberMask(pCasasDecimais));
		return DBSString.changeStr(xS, ",", "");
	}	

	
	/**
	 * Retorna o CNPJ com sua máscara.
	 * 
	 * @param pCNPJ
	 * @return String
	 */
	public static String getCNPJ(Object pCNPJ) {
		return getFormattedMask(pCNPJ, "99.999.999/9999-99", " ");
	}

	/**
	 * Retorna o CPF com sua máscara
	 * 
	 * @param pCPF
	 * @return String
	 */
	public static String getCPF(Object pCPF) {
		return getFormattedMask(pCPF, "999.999.9999-99", " ");
	}

	/**
	 * Retorna o CEP com sua máscara
	 * 
	 * @param pCEP
	 * @return String
	 */
	public static String getCEP(Object pCEP) {
		return getFormattedMask(pCEP, "99999-999", " ");
	}


	/**
	 * Retorna o caracter utilizado para separar a casa decimal
	 * @return
	 */
	public static String getDecimalSeparator(){
		java.text.DecimalFormatSymbols xD = new DecimalFormatSymbols(new Locale("pt","BR"));
		char xDecimalPoint = xD.getDecimalSeparator();
		return Character.toString(xDecimalPoint); 
	}
	
	public static String getGroupSeparator(){
		java.text.DecimalFormatSymbols xD = new DecimalFormatSymbols(new Locale("pt","BR"));
		char xDecimalPoint = xD.getGroupingSeparator();
		return Character.toString(xDecimalPoint); 
	}


	
	/**
	 * Retorna uma string com o valor formatado conforme a máscara
	 * Serão ignorados os caracteres não numericos ou alfabéticos da valor informado
	 * serão ignorados os caracteres que ultrapassatem o tamanho da máscara  
	 * @param pValue Valor que será utilizado para preencher a máscara
	 * @param pMask Máscara sendo 9=Numeric; a=Alpha; x=AlphaNumeric
	 * @param pEmptyChr Caracter que será utilizado quando o tamanho da valor for menor que o tamanho da máscara
	 * @return
	 */
	public static String getFormattedMask(Object pValue, String pMask, String pEmptyChr){
		if (pValue ==null ||
			pMask == null ||
			pEmptyChr == null){
			return "";
		}
		
		//9=Numeric; a=Alpha; x=AlphaNumeric
		String xFV = "";
		String xValue = pValue.toString();
		int 	xVI = 0;
		boolean xAchou;
		for (int xMI =0 ; xMI < pMask.length(); xMI++){
			String xMC = pMask.substring(xMI, xMI+1).toUpperCase();
			if (xMC.equals("9")||
				xMC.equals("A")){
				//Busca próximo caracter válido dentro do valor informado, para preencher a respectivo campo na máscara
				xAchou = false;
				while (xVI < xValue.length()){
					 char xVC = xValue.charAt(xVI);
					 xVI++;
					 if(Character.isLetterOrDigit(xVC)){
						 xFV = xFV + xVC;
						 xAchou = true;
						 break;
					 }
				}
				//Se não achou um caracter válido, preenche com o caracter vázio
				if (!xAchou){
					xFV = xFV + pEmptyChr;
				}
			}else{
				//Incorpora o caracter da máscara ao valor
				xFV = xFV + xMC;
			}
		}
		return xFV;
	}

}
