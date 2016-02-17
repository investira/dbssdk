package br.com.dbsoft.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

public class DBSFormat {

	public static String[] ZERO_DDI_DDDs = new String[]{
		"800",
		"300"
	};
	
	public class MASK{
		public static final String CURRENCY = "###,##0.00";
	}
	
	public final static class REGEX{
		public static final String ZIP = "\\d{5}-\\d{3}";
		public static final String LICENSE_PLATE = "[A-Z]{3}-\\d{4}";
//		public static final String PHONE_NUMBER = "^([(+]{0,1})([0-9]{0,2})([)\\-\\s]{0,1})([(\\-\\s]{0,1})([0-9]{0,3})([)\\-\\s]{0,1})([0-9]{3,4})([-.]{0,1})([0-9]{4})$";
		public static final String PHONE_NUMBER = "^([(+]{0,1})(([0-9]{2,3})|([0-9]{0}))([)\\-\\s]{0,1})([(\\-\\s]{0,1})(([0-9]{2,3})|([0-9]{0}))([)\\-\\s]{0,1})([0-9]{3,4})([-.]{0,1})([0-9]{4})$";
//		public static final String PHONE_NUMBER = "^([(+]{0,1})([0-9]{2,3})([)\\-\\s]{0,1})([(\\-\\s]{0,1})([0-9]{2,3})([)\\-\\s]{0,1})([0-9]{3,4})([-.]{0,1})([0-9]{4})$";
		public static final String ONLY_NUMBERS = "^[0-9]+$";
	}
	
	private static Pattern wPHONE_NUMBER;
	
	
	public static enum NUMBER_SIGN{
		NONE,
		CRDB_PREFIX,
		CRDB_SUFFIX,
		MINUS_SUFFIX,
		MINUS_PREFIX,
		PARENTHESES;
	}
	
	
	DBSFormat(){
		wPHONE_NUMBER = Pattern.compile(REGEX.PHONE_NUMBER);
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
	public static String getFormattedDateTimes(Long pLong){
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
	public static String getFormattedDateTimes(Date pDate){
		SimpleDateFormat xFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return xFormat.format(pDate);
	}

	/**
	 * Retorna string contendo a data formatada contendo data e hora
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDateTimes(Timestamp pDate){
		Date xDate = DBSDate.toDate(pDate);
		return getFormattedDateTimes(xDate);
	}
	/**
	 * Retorna string contendo a data formatada contendo hora, minutos e segundos
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedTimes(Date pDate){
		SimpleDateFormat xFormat = new SimpleDateFormat("HH:mm:ss");
		return xFormat.format(pDate.getTime());
	}
	
	/**
	 * Retorna string contendo a data formatada contendo data, hora e minutos
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDateTime(Long pLong){
		if (pLong == null){
			return "";
		}else{
	
			SimpleDateFormat xFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return xFormat.format(pLong);
		}
	}

	/**
	 * Retorna string contendo a data formatada contendo data, hora e minutos
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDateTime(Date pDate){
		SimpleDateFormat xFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return xFormat.format(pDate);
	}

	/**
	 * Retorna string contendo a data formatada contendo data, hora e minutos
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDateTime(Timestamp pDate){
		Date xDate = DBSDate.toDate(pDate);
		return getFormattedDateTime(xDate);
	}
	/**
	 * Retorna string contendo a data formatada contendo hora e minutos
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedTime(Date pDate){
		SimpleDateFormat xFormat = new SimpleDateFormat("HH:mm");
		return xFormat.format(pDate.getTime());
	}
	
	
	/**
	 * Retorna string contendo a data formatada de acordo com a mascara escolida
	 * @param pData Data a ser formatado
	 * @return String com a data já formatada
	 */
	public static String getFormattedDateCustom(Object pDate, String pMask){
		if (DBSObject.isNull(pDate)) {
			return "";
		}
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
		return getFormattedTimes(xDate);
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
		return getFormattedNumber(pValor, pSign, MASK.CURRENCY);
	}

	/**
	 * Retorna valor formatado conforme a indicação do sinal e máscara.
	 * @param pValor
	 * @param pSign
	 * @param pNumberMask
	 * @return
	 */
	public static String getFormattedNumber(Object pValor, NUMBER_SIGN pSign, String pNumberMask){
		return getFormattedNumber(DBSNumber.toDouble(pValor), pSign, pNumberMask);
	}

	/**
	 * Retorna valor formatado conforme a indicação do sinal e máscara.
	 * @param pValor
	 * @param pSign
	 * @param pNumberMask
	 * @return
	 */
	public static String getFormattedNumber(Double pValor, NUMBER_SIGN pSign, String pNumberMask){
		if (DBSObject.isEmpty(pValor)) {
			return null;
		}
		DecimalFormatSymbols xOtherSymbols = new DecimalFormatSymbols(getLocale());
//		xOtherSymbols.setDecimalSeparator(getDecimalSeparator().charAt(0));
//		xOtherSymbols.setGroupingSeparator(getGroupSeparator().charAt(0));
		DecimalFormat xDF = new DecimalFormat(pNumberMask, xOtherSymbols);
//		DecimalFormat xDF = new DecimalFormat(pNumberMask); xDF.get
		xDF.setRoundingMode(RoundingMode.HALF_UP);
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
	 * Retorna valor formatado no padrão CPF ou CNPJ.<br/>
	 * Será desconsiderado qualquer caracter não númerico informado em <b>pValue<b/>. 
	 * @param pPessoaFisica se é pessoa física.
	 * @param pValue
	 * @return
	 */
	public static String getCPFCNPJ(Object pPessoaFisica, Object pValue){
		String xValue = DBSString.toString(pValue);
		xValue = DBSNumber.getOnlyNumber(xValue);
		if (DBSBoolean.toBoolean(pPessoaFisica)){
			return getCPF(pValue);
		}else{
			return getCNPJ(pValue);
		}
	}

	/**
	 * Retorna o caracter utilizado para separar a casa decimal
	 * @return
	 */
	public static String getDecimalSeparator(){
		DecimalFormat xFormat = (DecimalFormat) DecimalFormat.getInstance(getLocale()); 
		DecimalFormatSymbols xD = xFormat.getDecimalFormatSymbols();
		return Character.toString(xD.getDecimalSeparator());  
	}
	
	public static String getGroupSeparator(){
		DecimalFormat xFormat = (DecimalFormat) DecimalFormat.getInstance(getLocale()); 
		DecimalFormatSymbols xD = xFormat.getDecimalFormatSymbols();
		return Character.toString(xD.getGroupingSeparator()); 
	}

	/**
	 * Retorna uma string com o valor formatado conforme a máscara.<br/>
	 * Serão ignorados os caracteres não numericos ou alfabéticos da valor informado
	 * serão ignorados os caracteres que ultrapassatem o tamanho da máscara  
	 * @param pValue Valor que será utilizado para preencher a máscara
	 * @param pMask Máscara sendo 9=Numeric; a=Alpha; x=AlphaNumeric
	 * @return
	 */
	public static String getFormattedMask(Object pValue, String pMask){
		return getFormattedMask(pValue, pMask, "");
	}
	
	/**
	 * Retorna uma string com o valor formatado conforme a máscara.<br/>
	 * Serão ignorados os caracteres não numericos ou alfabéticos da valor informado
	 * serão ignorados os caracteres que ultrapassatem o tamanho da máscara  
	 * @param pValue Valor que será utilizado para preencher a máscara
	 * @param pMask Máscara sendo 9=Numeric; a=Alpha; x=AlphaNumeric
	 * @param pEmptyChr Caracter que será utilizado quando o tamanho da valor for menor que o tamanho da máscara
	 * @return
	 */
	public static String getFormattedMask(Object pValue, String pMask, String pEmptyChr){
		if (pValue ==null ||
			pEmptyChr == null){
			return "";
		}
		
		if (pMask.equals("")){
			return pValue.toString();
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

	//====================================================
	//Validation
	//====================================================
	
	/**
	 * Retorna se string é um número telefonico.
	 * @param pString
	 * @return
	 */
	public static Matcher isPhone(String pString){
		if (pString == null){
			return null;
		}
		Matcher xP = wPHONE_NUMBER.matcher(pString);
		return xP;
	}

	/**
	 * Retorna número de telefone formatado.
	 * @param pPhoneNumber
	 * @return
	 */
	public static String getPhoneNumber(Object pDDI, Object pDDD, Object pNumber){
		return getPhoneNumber(DBSString.toString(pDDI),DBSString.toString(pDDD),DBSString.toString(pNumber));
	}

	/**
	 * Retorna número de telefone formatado.
	 * @param pPhoneNumber
	 * @return
	 */
	public static String getPhoneNumber(String pDDI, String pDDD, String pNumber){
		StringBuilder xSB = new StringBuilder();
		if (DBSObject.isEmpty(pNumber)){
			return "";
		}
		if (!DBSObject.isEmpty(pDDI)){
			xSB.append("(");
			xSB.append(pDDI);
			xSB.append(")");
		}
		if (!DBSObject.isEmpty(pDDD)){
			xSB.append("(");
			xSB.append(pDDD);
			xSB.append(")");
		}
		xSB.append(pNumber);
		return getPhoneNumber(xSB.toString());
	}
	
	
	/**
	 * Retorna número de telefone formatado.
	 * @param pPhoneNumber
	 * @return
	 */
	public static String getPhoneNumber(String pPhoneNumber){
		StringBuilder xFormattedNumber = new StringBuilder();
		String xChar;
		Boolean xIsNumber = false;
		Boolean xWasNumber = null;
		Integer xGroup = 1;
		StringBuilder xValue = new StringBuilder();
		for (int i=pPhoneNumber.length(); i>0; i--){
			xChar = pPhoneNumber.substring(i-1, i);
			xIsNumber = xChar.matches(REGEX.ONLY_NUMBERS);
			if (xWasNumber == null){
				xWasNumber = xIsNumber;
			}else if (xIsNumber != xWasNumber){
				xGroup = pvPhoneNumber(xFormattedNumber, xGroup, xValue.toString(), xWasNumber);
				if (xGroup == null){
					return null;
				}
				xValue = new StringBuilder();
				xWasNumber = xIsNumber;
				//Substitui qualquer caracter não numérico por '-'
				if (!xIsNumber){
					xChar = "-";
				}
			//Ignora caracter não numérico em caso de repetição	
			}else if (!xIsNumber){
				xChar = null;
			}
			if (xChar != null){
				xValue.insert(0, xChar);
			}
		}
		if (!DBSObject.isEmpty(xValue.toString())){
			xGroup = pvPhoneNumber(xFormattedNumber, xGroup, xValue.toString(), xWasNumber);
		}
		String xFN = xFormattedNumber.toString();
		if (xGroup == null
		 || DBSObject.isEmpty(xFN)){
			return null;
		}else{
			if (xFN.startsWith(")")){
				xFN = xFN.substring(1, xFN.length());
			}
			return xFN;
		}
	}
	
	/**
	 * DDI DDD NUMERO
	 * xxx xxx xxxx-xxxx
	 * @param pFormattedNumber
	 * @param pGroup
	 * @param pValue
	 * @param pIsNumber
	 * @return
	 */
	private static Integer pvPhoneNumber(StringBuilder pFormattedNumber, Integer pGroup, String pValue, Boolean pIsNumber){
		String xValue = null;
		int xB = 0;
		int xE = 0;
		Integer xMin = 0;
		Integer xMax = null;
		if (DBSObject.isEmpty(pValue)){return pGroup;}
		xE = pValue.length();
		//Não pode haver mais de 8 grupos.
		if (pGroup > 8){
			return null;
		}else if (pGroup==2 //Separadores
			   || pGroup==4
			   || pGroup==6
			   || pGroup==8){
			//Força o separador
			if (pGroup==8){
				xValue = "(";
			}else if (pGroup==6){
				xValue = ")(";
			}else if (pGroup==4){
				xValue = ")";
			}else if (pGroup==2){
				xValue = "-";
			}
			//Se for número, não faz nada com o valore recebido, para que seja tratado na chamada recursiva.
			//Se não for número, força resto para 'vázio' para evitar uma nova chamada recursiva.
			if (!pIsNumber){
				pValue = "";
			}
		}else{
			if (!pIsNumber){
				return null;
			}
			//Final do número
			if (pGroup==1){
				xMin = 4;
				xMax = 4;
			//Inicio do número
			}else if (pGroup==3){
				xMin = 3;
				xMax = 5;
			//DDD
			}else if (pGroup==5){
				xMin = 2;
				xMax = 3;
			//DDI
			}else if (pGroup==7){
				xMin = 1;
				xMax = 3;
			}
			xB = xE - xMax +1;
			if (xB<=0){
				xB=1;
			}
			//Valor utilizado
			xValue = DBSString.getSubString(pValue, xB, xE);
			
			//DDD e DDI: Retira os zeros a esquerda
			if (pGroup==5
			 || pGroup==7){
				xValue = getFormattedNumber(xValue, NUMBER_SIGN.NONE, "####");
			}
			
			//Erro se tamanho for menor que o necessário
			if (xValue.equals("0") 
			 || xValue.length() < xMin){
				return null; //Erro
			}

			//Resto
			pValue = DBSString.getSubString(pValue, 1, xB-1);
		}
		pGroup++;
		//Adiciona a string
		pFormattedNumber.insert(0, xValue);
		//Verifica se resto é número
		pIsNumber = pValue.matches(REGEX.ONLY_NUMBERS);
		//Retorna chamando recursivamente para verificar se o resto deve ser tratado também.
		return pvPhoneNumber(pFormattedNumber, pGroup, pValue, pIsNumber);
	}

	/**
	 * Retorna o Locale corrente, dando prioridade ao locale da view e depois do sistema.
	 * @return
	 */
	public static Locale getLocale(){
		Locale xLocale;
		if (FacesContext.getCurrentInstance() != null){
			xLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		}else{
			xLocale = Locale.getDefault();
		}
		return xLocale;
	}

}
