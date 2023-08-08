package br.com.dbsoft.util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.dbsoft.factory.DBSLinkedProperties;

/**
 * @author ricardo.villar
 *
 */
/**
 * @author ricardo.villar
 *
 */
public class DBSString {

	protected static Logger				wLogger = Logger.getLogger(DBSString.class);
	private static DBSLinkedProperties 	wDicionarioSilaba = new DBSLinkedProperties();
	private static DBSLinkedProperties 	wDicionarioPalavra = new DBSLinkedProperties();
	private static DBSLinkedProperties 	wDicionarioFrase = new DBSLinkedProperties();
	
	/**
	 * Retorna o número da posição de uma string, dentro de outra string,);considerando a caixa.
	 * @param pTextoBase Texto que será utilizado como base da pesquisa 
	 * @param pTextoPesquisa Texto a ser pesquisado
	 * @param pInicialPosition Informa posição incial a partir do qual será inciada a pesquisa dentro da String onde 1 é o primeiro caracter
	 * @return Retorna posição da string ou 0 caso seja não encontrada.
	 */
	public static int getInStr(String pTextoBase, String pTextoPesquisa, int pInicialPosition){
		if (DBSObject.isNull(pTextoBase) 
		 || DBSObject.isNull(pTextoPesquisa)
		 || pTextoBase.equals("")
		 || pTextoPesquisa.equals("")){
			return 0;
		}
		return pTextoBase.indexOf(pTextoPesquisa, pInicialPosition  - 1) + 1;
	}
	/**
	 * Retorna o número da posição de uma string, dentro de outra string
	 * @param pTextoBase Texto que será utilizado como base da pesquisa 
	 * @param pTextoPesquisa Texto a ser pesquisado
	 * @param pInicialPosition Informa posição incial a partir do qual será inciada a pesquisa dentro da String
	 * @param pCaseMatch False=Efetua a pesquisa independentemente da caixa. 
	 * @return Retorna posição da string ou 0 caso seja não encontrada.
	 */
	public static int getInStr(String pTextoBase, String pTextoPesquisa, int pInicialPosition, boolean pCaseMatch){
		if (DBSObject.isNull(pTextoBase)
		 || DBSObject.isNull(pTextoPesquisa)
		 || pTextoBase.equals("")
		 || pTextoPesquisa.equals("")){
			return 0;
		}
		if(pCaseMatch){
			return getInStr(pTextoBase, pTextoPesquisa, pInicialPosition);
		}else{
			return getInStr(pTextoBase.toLowerCase(), pTextoPesquisa.toLowerCase(), pInicialPosition);
		}
	}

	/**
	 * Retorna o número da posição de uma string, dentro de outra string, considerando a caixa.
	 * @param pTextoBase Texto que será utilizado como base da pesquisa 
	 * @param pTextoPesquisa Texto a ser pesquisado
	 * @return Retorna posição da string ou 0 caso seja não encontrada.
	 */
	public static int getInStr(String pTextoBase, String pTextoPesquisa){
		return getInStr(pTextoBase, pTextoPesquisa,0,true);
	}
	
	/**
	 * Retorna o número da posição de uma string, dentro de outra string
	 * @param pTextoBase Texto que será utilizado como base da pesquisa 
	 * @param pTextoPesquisa Texto a ser pesquisado
	 * @param pCaseMatch False=Efetua a pesquisa independentemente da caixa. Default = true;
	 * @return Retorna posição da string ou 0 caso seja não encontrada.
	 */
	public static int getInStr(String pTextoBase, String pTextoPesquisa, boolean pCaseMatch){
		return getInStr(pTextoBase, pTextoPesquisa,0,pCaseMatch);
	}

	
	/**
	 * Retorna uma string a partir de uma string original.<br/>
	 * Esta função tem o mesmo mecanismo que a função substring em .Net
	 * @param pString String original
	 * @param pInicio Posicao inicial dentro a string, sendo 1 a primeira posição.
	 * @param pTamanho Tamanho da string que se deseja recuperar a partir da posição pI. Se o tamanho for maior que o tamanho da string, utilizará o tamanho máximo.
	 * @return String Se tamanho informado for maior que o tamanho da String original, retorna string máxima permitida.
	 */
	public static String getSubString(String pString, int pInicio, int pTamanho){
		if (DBSObject.isEmpty(pString)){
			return null;
		}
		
		if(pInicio<1 || 
		   pTamanho<0){
			return "";
		}
		
		if ((pInicio + pTamanho-1) > pString.length()){ //Se ultrapassar o tamanho 
			pTamanho = pString.length() - pInicio + 1;//Ajusta o tamanho máximo pemitido
		}
		//String xS = pString.substring(pI-1, pI + pL -1);
		return pString.substring(pInicio-1, pInicio + pTamanho-1);
	}
	
	/**
	 * Retorna uma string a partir de uma string original.<br/>
	 * @param pString String original
	 * @param pCaractereInicio Caractere inicial.
	 * @param pCaractereFim Caractere Final.
	 * @return String entre os caracteres informados nos argumentos.
	 */
	public static String getSubString(String pString, String pCaractereInicio, String pCaractereFim) {
		if (DBSObject.isEmpty(pString) || DBSObject.isEmpty(pCaractereInicio) || DBSObject.isEmpty(pCaractereFim)){
			return null;
		}
		
		int xInicio = pString.indexOf(pCaractereInicio);
		int xFim = pString.indexOf(pCaractereFim);
		
		if (xInicio != -1 && xFim != -1 && xFim > xInicio) {
			return pString.substring(xInicio + 1, xFim);
		}
		
		return "";
	}
	
	/**
	 * Retorna a string sem zeros a esquerda caso a mesma seja composta apenas de números.<br/>
	 * Caso contrário retorna a string original sem alterações
	 * @param pString String original
	 * @return String sem zeros a esquerda.
	 */
	public static String removeZerosEsquerda(String pString) {
		if (DBSObject.isEmpty(pString)) {
			return null;
		}
		
		if(pString.matches("[0-9]+")) {
			pString = pString.replaceFirst("^0+", "");
		}
		
		return pString;
	}

	/**
	 * Conta o número de ocorrências de uma determinada string dentro de outra
	 * @param pTextoBase Texto base aonde será pesquisado o texto de pesquisa
	 * @param pTextoPesquisa Texto que será procurado dentro do texto base(pStringBase)
	 * @return Quantidade de ocorrências do texto de pesquisa dentro do texto base
	 */
	public static int getStringCount(String pTextoBase, String pTextoPesquisa){
		return getStringCount(pTextoBase, pTextoPesquisa, true);

	}
	
	public static int getStringCount(String pTextoBase, String pTextoPesquisa, boolean pCaseMatch){
		int xX = 0;
		int xI = 0;
		if (DBSObject.isEmpty(pTextoBase) ||
				DBSObject.isEmpty(pTextoPesquisa)){
			return 0;
		}
		do {
			xI = getInStr(pTextoBase, pTextoPesquisa, xI, pCaseMatch);
			if (xI != 0 ){
				xX++;
				xI = xI + pTextoPesquisa.length();
			}
		} while (xI != 0);
		return xX;

	}
	
	/**
	 * Substitui uma determinada string por outra independentemente do tamanho de cada uma delas
	 * @param pTextoBase Texto base a ser pesquisado
	 * @param pTextoAntigo Texto antigo a ser substituido
	 * @param pTextoNovo Texto novo a utilizado em substituição ao antigo
	 * @return Texto modificado
	 */
	public static String changeStr(String pTextoBase, String pTextoAntigo, String pTextoNovo){
		if (DBSObject.isEmpty(pTextoBase) || 
			DBSObject.isEmpty(pTextoAntigo) ||
			pTextoNovo == null){
			return pTextoBase;
		}
		return pTextoBase.replace(pTextoAntigo, pTextoNovo);
	}
	
	/**
	 * Substitui uma determinada string por outra independentemente do tamanho de cada uma delas
	 * @param pTextoBase Texto base a ser pesquisado
	 * @param pTextoAntigo Texto antigo a ser substituido
	 * @param pTextoNovo Texto novo a utilizado em substituição ao antigo
	 * @param pCaseMatch False=Efetua a substituição independentemente da caixa 
	 * @return Texto modificado
	 */
	public static String changeStr(String pTextoBase, String pTextoAntigo, String pTextoNovo, boolean pCaseMatch){
		if (pCaseMatch){
			return changeStr(pTextoBase, pTextoAntigo, pTextoNovo);
		}else{
			if (DBSObject.isNull(pTextoBase) || 
				DBSObject.isNull(pTextoAntigo)){
				return pTextoBase;
			}
			String xTextoAntigo = pTextoAntigo.toLowerCase(); //utiliza caixa alta para normalizar e evitar que se tenha que testar todas as variações de caixa
			String xTextoBase = pTextoBase.toLowerCase();
			int xP= xTextoBase.indexOf(xTextoAntigo); //Busca posição da primeira ocorrencia da string a ser trocada
			int xA = 0;
			if(xP>-1){ //Se existir...
				String xTexto = "";
				while(xP>-1){
					xTexto = xTexto + pTextoBase.substring(xA, xP) + pTextoNovo; 
					xA = xP + pTextoAntigo.length(); 
					xP= xTextoBase.indexOf(xTextoAntigo, xA);
				}
				xTexto = xTexto + pTextoBase.substring(xA, pTextoBase.length());
				return xTexto;
			}else{
				return pTextoBase;
			}
		}
	}
	

	/**
	 * Retorna o mesmo dado informado quando este não for vazio. 
	 * Se for vazio, retorna o conteúdo default definido pelo usuário ou vazio, quando este for nulo.
	 * @param pDado Dado a ser verificado
	 * @param pDadoDefault Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de nulo
	 */
	public static String getNotEmpty(String pDado, String pDadoDefault){
		if (DBSObject.isEmpty(pDado)){
			if (DBSObject.isEmpty(pDadoDefault)){
				return ""; //Retorna vazio
			} 
			else {
				return pDadoDefault; //Retorna valor default informado
			}
		}
		else{
			return pDado;
		}
	}
	
	/**
	 * Retorna o mesmo dado informado quando este não for vazio. 
	 * Se for vazio, retorna o conteúdo default definido pelo usuário ou vazio, quando este for nulo.
	 * @param pDado Dado a ser verificado
	 * @param pDadoDefault Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de nulo
	 */
	public static String getNotEmpty(Object pDado, Object pDadoDefault){
		return getNotEmpty(DBSString.toString(pDado, null), DBSString.toString(pDadoDefault, null));
	}

	/**
	 * Retorna o mesmo dado informado quando este não for nulo. 
	 * Se for nulo, retorna o conteúdo default definido pelo usuário ou vazio, quando este for nulo.
	 * @param pDado Dado a ser verificado
	 * @param pDadoDefault Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de nulo
	 */
	public static String getNotNull(String pDado, String pDadoDefault){
		if (DBSObject.isEmpty(pDado)){
			if (DBSObject.isEmpty(pDadoDefault)){
				return ""; //Retorna vazio
			} 
			else {
				return pDadoDefault; //Retorna valor default informado
			}
		}
		else{
			return pDado;
		}
	}
	
	/**
	 * Retorna o mesmo dado informado quando este não for nulo. 
	 * Se for nulo, retorna o conteúdo default definido pelo usuário ou vazio, quando este for nulo.
	 * @param pDado Dado a ser verificado
	 * @param pDadoDefault Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de nulo
	 */
	public static String getNotNull(Object pDado, Object pDadoDefault){
		return getNotNull(DBSString.toString(pDado, null), DBSString.toString(pDadoDefault, null));
	}
	
	/**
	 * Retorna string concatenando as strings recebidas, separando por vírgula e espaço e fazendo trim em cada string.<br/>
	 * Valores nulos ou vazios serão despresados.
	 * @param Strings
	 * @return String contatenada
	 */
	@SuppressWarnings("unchecked")
	public static <T> String joinString(T... pStrings){
		return joinStringWithDelimiter(", ", pStrings);
	}

	/**
	 * Retorna string concatenando as strings recebidas, separando <i>enter</I> e fazendo trim em cada string.<br/>
	 * Valores nulos ou vazios serão despresados.
	 * @param Strings
	 * @return String contatenada
	 */
	@SuppressWarnings("unchecked")
	public static <T> String joinStringWithDelimiterNewLine(T... pStrings){
		return joinStringWithDelimiter(Character.toString((char) 13), pStrings);
	}

	/**
	 * Retorna string concatenando as strings recebidas, separando por vírgula e espaço e fazendo trim em cada string.<br/>
	 * Valores nulos ou vazios serão despresados.
	 * @param Strings
	 * @return String contatenada
	 */
	@SuppressWarnings("unchecked")
	public static <T> String joinStringWithDelimiter(String pDelimiter, T... pStrings){
		if (pStrings == null
		 || pDelimiter == null){
			return null;
		}else if(pStrings.length>0){//Se houver mais de uma string
			StringBuilder xStringBuilder = new StringBuilder();
			boolean xTemAnterior = false;
			//Loop entre todas as strings recebidas
			for (int xX=0; xX < pStrings.length; xX++){
				//Se não for a primeira
				String xString = DBSString.toString(pStrings[xX], "").trim();
				if (!DBSObject.isEmpty(xString)){
					if (xX>0){
						//Se a anterior foi diferente de null
						if (xTemAnterior){
							//Concatena a string com a vírgula
							xStringBuilder.append(pDelimiter);
						}
					}
					xTemAnterior = true;
					//Concatena String
					xStringBuilder.append(xString);
				}
			}
		    return xStringBuilder.toString();
		}else{
			return null;
		}
	}
	
	/**
	 * Adiciona um ByteArray a outro e retorna o resultado.<br/>
	 * Utiliza o <b>pByteArrayToAppendLenght</b> para informar a quantidade de 
	 * bytes válidos de <b>pByteArrayToAppend</b> que serão copiados no append.<br/>
	 * @param pByteArrayBase
	 * @param pByteArrayToAppend
	 * @param pByteArrayToAppendLenght 
	 * @return
	 */
	public static byte[] appendByteArrays(byte[] pByteArrayBase, byte[] pByteArrayToAppend, Integer pByteArrayToAppendLenght) {
		int xBaseLength = 0;
		int xAppendLength = 0;
		if (pByteArrayBase != null){
			xBaseLength = pByteArrayBase.length;
		}
		if (pByteArrayToAppend != null){
			if (pByteArrayToAppendLenght == null){
				xAppendLength = pByteArrayToAppend.length;
			}else{
				xAppendLength = pByteArrayToAppendLenght;
			}
		}
	    byte[] xResult = new byte[xBaseLength + xAppendLength]; 
		if (pByteArrayBase != null){
			System.arraycopy(pByteArrayBase, 0, xResult, 0, xBaseLength); 
		}
		if (pByteArrayToAppend != null){
			System.arraycopy(pByteArrayToAppend, 0, xResult, xBaseLength, xAppendLength); 
		}
	    return xResult;
	} 

	/**
	 * Converte um valor numérico para string, excluido a separação decimal e fixando o tamanho das casas decimais
	 * @param pValor Valor numérico a ser convertido;
	 * @param pTamanhoMinimoDaString Tamanho mínimo que terá a string de retorno;
	 * @param pCadasDecimais Quantidade de casas decimais que serão consideradas como significativas.
	 * @return String
	 */
	public static String getNumeroSemPonto(Double pValor, int pTamanhoMinimoDaString, int pCasasDecimais){
		if (DBSObject.isEmpty(pValor) || 
				DBSObject.isEmpty(pTamanhoMinimoDaString) ||
				DBSObject.isEmpty(pCasasDecimais)){
			return null;
		}
		Double xN = DBSNumber.exp(10D, pCasasDecimais).doubleValue();
		xN = DBSNumber.multiply(pValor, xN).doubleValue();
		DecimalFormat xF = new DecimalFormat(repeat("0",pTamanhoMinimoDaString));
		return xF.format(xN);
	}
	
	/**
	 * Retorna String preenchida o caracter informado repetido, dentro do tamanho definido 
	 * @param pString Caracter a ser utilizado para preencher
	 * @param pTimes Tamanho da string de retorno
	 * @return String preenchida
	 */	
	public static String repeat(String pString, int pTimes){
		if (!DBSObject.isEmpty(pString)){
			String xS = "";
			for (int xY = 0; xY<pTimes; xY++){
				xS = xS + pString;
			}	
			return xS;
		}else{
			return pString;
		}
	}
	
	/**
	 * Retorna uma String com os itens contidos em <b>pList</b>, separados por vírgula.
	 * @param pList
	 * @return
	 */
	public static <T> String listToCSV(List<T> pList){
		if (pList == null ||
			pList.size() == 0){
			return "";
		}
		String xString = "";
		String xTest;
		for (Object xO:pList){
			xTest = DBSString.toString(xO,null);
			if (!DBSObject.isEmpty(xTest)){
				if (!xString.equals("")){
					xString += ",";
				}
				xString += xTest;
			}
		}
		return xString;
	}


	/**
	 * Retorna um Array a patir de uma String, separado por um delimitador informado.
	 * Antigo: BreakStringIntoArray(ByVal pString As String, pArray() As String, ByVal pDelimitador As String)<br/>
	 * Maiúscula e minúsculo serão consderados nomes diferentes.
	 * @param pTextoBase String com o texto que se deseja separar
	 * @param pDelimitador String que será utilizado para separar os campos
	 * @return Array com o conteúdo em cada linha 
	 */
	public static ArrayList<String> toArrayList(String pTextoBase, String pDelimitador){
		return toArrayList(pTextoBase, pDelimitador, true, true);
	}
		
	/**
	 * Retorna um Array a patir de uma String, separado por um delimitador informado
	 * Antigo: BreakStringIntoArray(ByVal pString As String, pArray() As String, ByVal pDelimitador As String)
	 * @param pTextoBase String com o texto que se deseja separar
	 * @param pDelimitador String que será utilizado para separar os campos
	 * @param pCaseMatch indica se o delimitador considerará a caixa 
	 * @return Array com o conteúdo em cada linha 
	 */
	public static ArrayList<String> toArrayList(String pTextoBase, String pDelimitador, boolean pCaseMatch){
		return toArrayList(pTextoBase, pDelimitador, pCaseMatch, true);
	}
	
	/**
	 * Retorna um Array a patir de uma String, separado por um delimitador informado
	 * Antigo: BreakStringIntoArray(ByVal pString As String, pArray() As String, ByVal pDelimitador As String)
	 * @param pTextoBase String com o texto que se deseja separar
	 * @param pDelimitador String que será utilizado para separar os campos. Não faz a delimitação dos campos se delimitador for nulo ou vázio
	 * @param pCaseMatch indica se o delimitador considerará a caixa
	 * @param pTrim Indicador se exclui espaços no inicio e fim da string antes de inclui-lá no array
	 * @return Array com o conteúdo em cada linha 
	 */
	public static ArrayList<String> toArrayList(String pTextoBase, String pDelimitador, boolean pCaseMatch, boolean pTrim){
		int xF = 1; //Inicio da string
		int xI = 1; //Fim da String
		String xS;
		ArrayList <String> xA = new ArrayList<String>();
		if (DBSObject.isEmpty(pTextoBase)){
			return xA;
		}else if (DBSObject.isNull(pDelimitador)){
			xA.add(pTextoBase);
		}else if (pDelimitador.equals("")){
			xA.add(pTextoBase);
		}else{
			while (xF !=0 ){
				xF = getInStr(pTextoBase, pDelimitador, xI, pCaseMatch);//Procura fim da string
				if (xF==0){ //Se não achou
					xS = getSubString(pTextoBase,xI, pTextoBase.length() - xI + 1); //Retorna restante da string
					//xS = pTextoBase.substring(xI, pTextoBase.length()).trim(); //Retorna restante da string
				}else{
					xS = getSubString(pTextoBase,xI, xF - xI); 
					//xS = pTextoBase.substring(xI, xF).trim(); //Recupera string dentro do textobase sem o delimitador
					xI = xF + pDelimitador.length(); //Incrementa posição inicial da busca dentro da string
				}
				if (pTrim){
					xA.add(xS.trim());
				}else{
					xA.add(xS);
				}
			};
		}
		return xA; 
	}

	/**
	 * * Retorna um Array a patir de uma String, separado conforme o regex
	 * @param pTextoBase
	 * @param pRegex
	 * @return
	 */
	public static ArrayList<String> toArrayListRegex(String pTextoBase, String pRegex){
		String[] xMatches = pTextoBase.split(pRegex);
		ArrayList <String> xA = new ArrayList<String>();
		for (String xMatch:xMatches){
			if (!DBSObject.isEmpty(xMatch)){
				xA.add(xMatch.trim());
			}
		};
		return xA;
	}
	
	/**
	 * Retorna um array a partir dos valores informados.
	 * @param pValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(T... pValues){
		if (pValues==null){
			return null;
		}
		return pValues;
	}

	/**
	 * Retorna um array a partir dos valores informados, ignorando os valores nulos.
	 * @param pValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArrayNotNull(T... pValues){
		return pvAddToArray(1, null, pValues);
	}

	/**
	 * Retorna um array a partir dos valores informados, ignorando os valores vazios ou nulos.
	 * @param pValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArrayNotEmpty(T... pValues){
		return pvAddToArray(2, null, pValues);
	}
	
	/**
	 * Retorna array incluindo os itens informados desconsiderando os valores vazios e nulos.
	 * @param pArray
	 * @param pValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] addToArrayNotEmpty(T[] pArray, T... pValues){
		return pvAddToArray(2, pArray, pValues);
	}
	
	/**
	 * Retorna array incluindo os itens informados  os valores nulos.
	 * @param pArray
	 * @param pValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] addToArrayNotNull(T[] pArray, T... pValues){
		return pvAddToArray(1, pArray, pValues);
	}
	
	/**
	 * Retorna array incluindo os itens informados.
	 * @param pArray
	 * @param pValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] addToArray(T[] pArray, T... pValues){
		return pvAddToArray(0, pArray, pValues);
	}
	
	/**
	 * Retorn array a partir de uma string CSV(Campos separados po vírgula/Comma separated values)
	 * @param pTextoBase
	 * @return
	 */
	public static String[] CSVtoArray(String pTextoBase){
		String[] xArray;
		pTextoBase = DBSString.changeStr(pTextoBase, ",", " ");
		xArray = pTextoBase.split("\\s+");
//		for (int xI = 0; xI < xArray.length; xI++){
//			xPKs[xI] = xPKs[xI].substring(xN+1).trim();
//		}
		return xArray;
	}
	
	/**
	 * Retorna uma String com os itens contidos em <b>pList</b>, separados por vírgula.
	 * @param pList
	 * @return
	 */
	public static String setToCSV(Set<Object> pList){
		return arrayToCSV(pList.toArray());
	}
	/**
	 * Retorna uma String com os itens contidos em <b>pList</b>, separados por vírgula.
	 * @param pList
	 * @return
	 */
	public static String arrayToCSV(Object[] pList){
		return listToCSV(Arrays.asList(pList));
	
	}
	/**
	 * Retorna a string com formatação de nome próprio. 
	 * @param pString original
	 * @return String formatada
	 */	
	public static String toProper(String pString){
		if (DBSObject.isNull(pString)) {
			return null;
		}
		String xS = pvToProper(pString);
		String xS2 = "";
		//Loop en todas os caracteres da string
		for (int xX = 0; xX<pString.length()-1; xX++){
			//Se caracter não for um caracter alfabético...
			if (!Character.isLetter(pString.charAt(xX))){
				xS2 = DBSString.getSubString(xS, xX + 2, pString.length());
				//Considera parte inicial da string(até o caracter não alfabético) + o restante já com a primeira letra convertida para maiúscula
				xS = DBSString.getSubString(xS, 1, xX + 1) + pvToProper(xS2);
			}
		}
		return xS;
	}

	/**
	 * Converte char para caracter ASCII
	 * @param pString contendo caracteres em código ASCII
	 * @return
	 */
	public static String toASCII(String pString){
		if (!DBSObject.isEmpty(pString)){
			StringBuffer xStr = new StringBuffer();
			//char xTable[][] = new char[2][2];
			char xTable[][] = {
								{(char)128,'Ç'},
								{(char)129,'ü'},
								{(char)130,'é'},
								{(char)131,'â'},
								{(char)132,'ä'},
								{(char)133,'à'},
								{(char)134,'a'},
								{(char)135,'ç'},
								{(char)136,'ê'},
								{(char)137,'ë'},
								{(char)138,'è'},
								{(char)139,'ï'},
								{(char)140,'î'},
								{(char)141,'ì'},
								{(char)142,'Ä'},
								{(char)143,'A'},
								{(char)144,'É'},
								{(char)145,'æ'},
								{(char)146,'Æ'},
								{(char)147,'ô'},
								{(char)148,'ö'},
								{(char)149,'ò'},
								{(char)150,'û'},
								{(char)151,'ù'},
								{(char)152,'ÿ'},
								{(char)153,'Ö'},
								{(char)154,'Ü'},
								{(char)155,'ø'},
								{(char)156,'£'},
								{(char)157,'Ø'},
								{(char)158,'×'},
								{(char)159,'ƒ'},
								{(char)160,'á'},
								{(char)161,'í'},
								{(char)162,'ó'},
								{(char)163,'ú'},
								{(char)164,'ñ'},
								{(char)165,'Ñ'},
								{(char)166,'ª'},
								{(char)167,'º'},
								{(char)168,'¿'},
								{(char)169,'®'},
								{(char)170,'¬'},
								{(char)171,'½'},
								{(char)172,'¼'},
								{(char)173,'¡'},
								{(char)174,'«'},
								{(char)175,'»'},
								{(char)176,'░'},
								{(char)177,'▒'},
								{(char)178,'▓'},
								{(char)179,'│'},
								{(char)180,'┤'},
								{(char)181,'Á'},
								{(char)182,'Â'},
								{(char)183,'À'},
								{(char)184,'©'},
								{(char)185,'╣'},
								{(char)186,'║'},
								{(char)187,'╗'},
								{(char)188,'╝'},
								{(char)189,'¢'},
								{(char)190,'¥'},
								{(char)191,'┐'},
								{(char)192,'└'},
								{(char)193,'┴'},
								{(char)194,'┬'},
								{(char)195,'├'},
								{(char)196,'─'},
								{(char)197,'┼'},
								{(char)198,'ã'},
								{(char)199,'Ã'},
								{(char)200,'╚'},
								{(char)201,'╔'},
								{(char)202,'╩'},
								{(char)203,'╦'},
								{(char)204,'╠'},
								{(char)205,'═'},
								{(char)206,'╬'},
								{(char)207,'¤'},
								{(char)208,'ð'},
								{(char)209,'Ð'},
								{(char)210,'Ê'},
								{(char)211,'Ë'},
								{(char)212,'È'},
								{(char)213,'ı'},
								{(char)214,'Í'},
								{(char)215,'Î'},
								{(char)216,'Ï'},
								{(char)217,'┘'},
								{(char)218,'┌'},
								{(char)219,'█'},
								{(char)220,'▄'},
								{(char)221,'¦'},
								{(char)222,'Ì'},
								{(char)223,'▀'},
								{(char)224,'Ó'},
								{(char)225,'ß'},
								{(char)226,'Ô'},
								{(char)227,'Ò'},
								{(char)228,'õ'},
								{(char)229,'Õ'},
								{(char)230,'µ'},
								{(char)231,'þ'},
								{(char)232,'Þ'},
								{(char)233,'Ú'},
								{(char)234,'Û'},
								{(char)235,'Ù'},
								{(char)236,'ý'},
								{(char)237,'Ý'},
								{(char)238,'¯'},
								{(char)239,'´'},
								{(char)240,'≡'},
								{(char)241,'±'},
								{(char)242,'‗'},
								{(char)243,'¾'},
								{(char)244,'¶'},
								{(char)245,'§'},
								{(char)246,'÷'},
								{(char)247,'¸'},
								{(char)248,'°'},
								{(char)249,'¨'},
								{(char)250,'·'},
								{(char)251,'¹'},
								{(char)252,'³'},
								{(char)253,'²'},
								{(char)254,'■'}
								};

			for (int xX=0; xX<=pString.length()-1; xX++){
				char xC = pString.charAt(xX);
				int xD = xC;
				for (int xY=0; xY<= xTable.length-1;xY++){
					if (xD == xTable[xY][0]){
						xC = xTable[xY][1];
					}
				}
				xStr.append(xC);
			}
			//System.out.println(xStr.toString());
			return xStr.toString();
		}
		return pString;
  	}
	

	/**
	 * Retorna string com valores hexa de cada byte da string original. 
	 * @param pString
	 * @return
	 */
	public static String toHex(byte[] pBytes) {
	    return String.format("%x", new BigInteger(1, pBytes));
	}
	
    
//	public static String UTF8toISO(String pTexto){
//		Charset xUTF8charset = Charset.forName("UTF-8");
//        Charset xISO88591charset = Charset.forName("ISO-8859-1");
//
//        //Buffer
//        ByteBuffer xInputBuffer = ByteBuffer.wrap(pTexto.getBytes());
//
//        // decode UTF-8
//        CharBuffer xData = xUTF8charset.decode(xInputBuffer);
//
//        // encode ISO-8559-1
//        ByteBuffer xOutputBuffer = xISO88591charset.encode(xData);
//        byte[] xOutputData = xOutputBuffer.array();
//
//        return new String(xOutputData);
//    }
//	
//	public static String ISOtoUTF8(String pTexto){
//        Charset xUTF8charset = Charset.forName("UTF-8");
//        Charset xISO88591charset = Charset.forName("ISO-8859-1");
//
//        //Buffer
//        ByteBuffer xInputBuffer = ByteBuffer.wrap(pTexto.getBytes());
//
//        // decode ISO-8559-1
//        CharBuffer xData = xISO88591charset.decode(xInputBuffer);
//
//        // encode UTF-8
//        ByteBuffer xOutputBuffer = xUTF8charset.encode(xData);
//        byte[] xOutputData = xOutputBuffer.array();
//
//        return new String(xOutputData);
//    }
	
	
	//*******************************************************************************************************
	// Private
	//*******************************************************************************************************

	public static String toString(Object pValue) {
		return toString(pValue, null);
	}
	
	public static String toString(Object pValue, String pDefault) {
		if (DBSObject.isEmpty(pValue)) {
			return pDefault;
		}
		return pValue.toString();
	}

	/**
	 * Converte string com valores em hexa para valores decimais 
	 * @param pHex
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] fromHex(String pHex){
	    byte[] xBytes = new byte[pHex.length() / 2];
	    for(int i = 0; i<xBytes.length ;i++)
	    {
	        xBytes[i] = (byte)Integer.parseInt(pHex.substring(2 * i, 2 * i + 2), 16);
	    }
	    return xBytes;
	}
	
	/**
	 * Retorna se a string é somente continuida de letra alfabética considerando 'espaço' também válido.
	 * @param pString
	 * @return
	 */
	public static boolean isAlphabetic(String pString){
		if (pString == null){
			return false;
		}
		return pString.matches("[a-zA-Z\\s]+$");
	}


	/**
	 * Busca dentro do array se existe a string informada e retorna a posição.
	 * Se não eistir, retorna -1; 
	 * @param pArray
	 * @param pString
	 * @return
	 */
	public static int findStringInArray(String[] pArray, String pString){
		for (int xI = 0; xI < pArray.length; xI++){
			if (pArray[xI].equals(pString)){
				return xI;
			}
		}
		return -1;
	}
	
	/**
	 * Corrige erros ortográficos a partir do dicionário interno.<br/>
	 * Além de corrigir, formata coo nome próprio
	 * Para inclur novas palavras, deve-se editar os arquivs: dicionario_acento e dicionario_palavra.
	 * dicionario_acento: correções de acentuação assumindo que a palavra esta correta
	 * dicionario_palavra: troca uma palavra pela outra, considerando inclusive a caixa da letra
	 * @param pTexto
	 * @return
	 */	
	public static String corretorOrtografico(String pTexto){
		if (pTexto==null){
			return null;
		}
		String xTexto = toProper(pTexto);
		if(wDicionarioSilaba.isEmpty()){
			pvDicionarioInit();
		}
		//Retira duplo espaço
		xTexto = DBSString.changeStr(xTexto, "  ", " ", false);
		//System.out.println(pTexto);
		xTexto = pvCorretorOrtograticoSilaba(xTexto);
		//System.out.println(pTexto);
		xTexto = pvCorretorOrtograficoPalavra(xTexto);
		//System.out.println(pTexto);
		xTexto = pvCorretorOrtograficoFrase(xTexto);
		//System.out.println(pTexto);
		return xTexto;
	}

	/**
	 * Retorna array incluindo os itens informados
	 * @param pArray
	 * @param pValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T[] pvAddToArray(int pTipo, T[] pArray, T... pValues){
		if (pValues == null){return null;}
		
		//Armazena lista recebida
		List<T> xList;
		T[] 	xArray = null;

		if (pArray == null){
			xList = new ArrayList<T>();
		}else{
	 		xList = new ArrayList<T>(Arrays.asList(pArray));
		}
		
		//Adiciona os valores recebidos a lista
		for (T xValue:pValues){
			if (pTipo == 2){
				if(!DBSObject.isEmpty(xValue)){
					xList.add(xValue);
				}
			}else if (pTipo == 1){
				if (xValue != null){
					xList.add(xValue);
				}
			}else{
				xList.add(xValue);
			}
		}

		//Também busca por item com valor válido para se identificar a class T
		for (T xValue:xList){
			if (xValue !=null){
				xArray = (T[]) Array.newInstance(xValue.getClass(), xList.size());
				break;
			}
		}
		//Se não consegui cria um novo array por não haver valores válidos, retorna o próprio array enviado.
		if (xArray == null){
			return null;
		}

		//Copia valores da lista para o array
		for (int xI=0; xI < xList.size(); xI++){
			xArray[xI] = xList.get(xI);
		}

		return xArray;
		
//		if (pArray == null){return null;}
//		//Salva lista inicial
//		List<T> xList = new ArrayList<T>(Arrays.asList(pArray));
//		//Adiciona valores enviadoa a lista inicial
//		xList.addAll(Arrays.asList(toArray(pValues)));
//		//Retorna lista como array
//		if (xList.size() > 0){
//			T[] xArray = (T[]) Array.newInstance(xList.get(0).getClass(), xList.size());
//			for (int xI=0; xI <= xList.size()-1; xI++){
//				if (xList.get(xI) != null){
//					xArray[xI] =  xList.get(xI);
//				}
//			}
//			return xArray;
//		}
//		return pArray;
	}
	
	/**
	 * Converte a primeira letra para maiúscula e o restante minúscula
	 * @param pString
	 * @return Para com a primeira letra maiúscula e o restante minúscula
	 */
	private static String pvToProper(String pString){
		if (!DBSObject.isEmpty(pString)){
			String xS = pString.substring(0, 1).toUpperCase();
			if (pString.length()>1){
				xS = xS + pString.substring(1).toLowerCase();
			}
			return xS;
		}else{
			return pString;
		}
	}
	/**
	 * Troca parte da palavras por outro texto considerando a caixa da letra a partir do dicinário interno
	 * É respeitado a ordem que a frase está no dicionário 
	 * @param pTexto
	 * @return
	 */
	private static String pvCorretorOrtograticoSilaba(String pTexto){
		String xTexto = pTexto;
		String xKey = ""; 
		Enumeration<Object> xDicionarioSilabaEnum;
		xDicionarioSilabaEnum = wDicionarioSilaba.keys();
		while (xDicionarioSilabaEnum.hasMoreElements()){
			xKey = (String) xDicionarioSilabaEnum.nextElement();
			xTexto = DBSString.changeStr(xTexto, xKey, wDicionarioSilaba.getProperty(xKey),true);
		}
		xTexto = toProper(xTexto);
		return xTexto;
	}
	/**
	 * Troca uma palavra pela outra considerando a caixa da letra a partir do dicinário interno
	 * É respeitado a ordem que a frase está no dicionário 
	 * @param pTexto
	 * @return
	 */
	private static String pvCorretorOrtograficoPalavra(String pTexto){
		List<String> 	xPalavras = new ArrayList<String>();
		String 		 	xTexto = "";
		String 			xPalavraErrada = ""; 

		xPalavras = DBSString.toArrayList(pTexto, " ", false);
		for (String xPalavra: xPalavras){
			Enumeration<Object> xDicionarioPalavraEnum; 
			xDicionarioPalavraEnum = wDicionarioPalavra.keys();
			while (xDicionarioPalavraEnum.hasMoreElements()){
				xPalavraErrada = (String) xDicionarioPalavraEnum.nextElement();
				if (xPalavraErrada.equals(xPalavra)){
					xPalavra = DBSString.changeStr(xPalavra, xPalavraErrada, wDicionarioPalavra.getProperty(xPalavraErrada), true);
					break;
				}
			}
			if (xTexto.equals("")){
				xTexto = xPalavra; 
			}else{
				xTexto += " " + xPalavra;
			}
		}
		return xTexto;
	}
	/**
	 * Troca frase(Mais de uma palavra) por outra(s) considerando a caixa da letra a partir do dicinário interno
	 * É respeitado a ordem que a frase está no dicionário 
	 * @param pTexto
	 * @return
	 */
	private static String pvCorretorOrtograficoFrase(String pTexto){
		String 	xTexto = pTexto;
		String xKey = ""; 
		
		Enumeration<Object> xDicionarioFraseEnum;
		xDicionarioFraseEnum = wDicionarioFrase.keys();
	
		while (xDicionarioFraseEnum.hasMoreElements()){
			xKey = (String) xDicionarioFraseEnum.nextElement();
			xTexto = DBSString.changeStr(xTexto, xKey, wDicionarioFrase.getProperty(xKey),true);
		}
		return xTexto;
	}
	/**
	 * Carrega os dicionarios uma única vez para melhor a performance
	 */
	private static void pvDicionarioInit(){
		try {
			wDicionarioSilaba.load(DBSString.class.getResourceAsStream("/META-INF/dicionario_silaba.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			wDicionarioPalavra.load(DBSString.class.getResourceAsStream("/META-INF/dicionario_palavra.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			wDicionarioFrase.load(DBSString.class.getResourceAsStream("/META-INF/dicionario_frase.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica se algum dos itens do array está contido na String Base
	 * @param pTextoBase
	 * @param pArrayTextoPesquisa
	 * @return
	 */
	public static boolean contains(String pTextoBase, ArrayList<String> pArrayTextoPesquisa) {
		if (DBSObject.isNull(pTextoBase)) {return false;}
		for (String xTextoPesquisa : pArrayTextoPesquisa) {
			if (pTextoBase.contains(xTextoPesquisa)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Decodifica strings HTML
	 * @param pBaseTexto
	 * @return Texto Decodificado
	 */
	public static String decodeHTML(String pBaseTexto) {
		String xTextoDecodificado = pBaseTexto;
		xTextoDecodificado = changeStr(xTextoDecodificado, "&amp;", "&");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&amp;", "&");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&#038;", "&");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&lt;", "<");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&gt;", ">");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&quot;", "\"");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&#039;", "'");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&#8217;", "’");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&#8216;", "‘");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&#8211;", "–");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&#8212;", "—");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&#8230;", "…");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&#8221;", "\"");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&#8220;", "\"");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&ccedil;", "ç");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&atilde;", "ã");
		xTextoDecodificado = changeStr(xTextoDecodificado, "&otilde;", "õ");
	  return xTextoDecodificado;
	}
}
