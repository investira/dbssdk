package br.com.dbsoft.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
	 * Retorna o número da posição de uma string, dentro de outra string, considerando a caixa.
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
	 * @param pCaseMatch False=Efetua a pesquisa independente da caixa. 
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
	 * @param pCaseMatch False=Efetua a pesquisa independente da caixa. Default = true;
	 * @return Retorna posição da string ou 0 caso seja não encontrada.
	 */
	public static int getInStr(String pTextoBase, String pTextoPesquisa, boolean pCaseMatch){
		return getInStr(pTextoBase, pTextoPesquisa,0,pCaseMatch);
	}

	
	/**
	 * Retorna uma string a partir de uma string original
	 * Esta função tem o mesmo mecanismo que a função substring em .Net
	 * @param pString String original
	 * @param pInicio Posicao inicial dentro a string, sendo 1 a primeira posição
	 * @param pTamanho Tamanho da string que se deseja recuperar a partir da posição pI. Se o tamanho for maior que o tamanho da string, utilizará o tamanho máximo
	 * @return String Se tamanho informado for maior que o tamanho da String original, retorna string máxima permitida
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
	 * Substitui uma determinada string por outra independente do tamanho de cada uma delas
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
	 * Substitui uma determinada string por outra independente do tamanho de cada uma delas
	 * @param pTextoBase Texto base a ser pesquisado
	 * @param pTextoAntigo Texto antigo a ser substituido
	 * @param pTextoNovo Texto novo a utilizado em substituição ao antigo
	 * @param pCaseMatch False=Efetua a substituição independente da caixa 
	 * @return Texto modificado
	 */
	public static String changeStr(String pTextoBase, String pTextoAntigo, String pTextoNovo, boolean pCaseMatch){
		if (pCaseMatch){
			return changeStr(pTextoBase, pTextoAntigo, pTextoNovo);
		}else{
			if (DBSObject.isEmpty(pTextoBase) || 
				DBSObject.isEmpty(pTextoAntigo)){
				return pTextoBase;
			}
			String xTextoAntigo = pTextoAntigo.toLowerCase(); //utiliza caixa alta para normalizar e evitar que se tenha que testar todas as variácoes de caixa
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
	 * Retorna string concatenando as strings recebidas, separando por vírgula e fazendo trim em cada string.
	 * @param Strings
	 * @return String contatenada
	 */
	public static String joinString(Object... pStrings){
		if (pStrings==null){
			return null;
		}else if(pStrings.length>0){//Se houver mais de uma string
			StringBuilder xStringBuilder = new StringBuilder();
			boolean xTemAnterior = false;
			//Loop entre todas as strings recebidas
			for (int xX=0; xX < pStrings.length; xX++){
				//Se não for a primeira
				if (!DBSObject.isEmpty(pStrings[xX])){
					if (xX>0){
						//Se a anterior foi diferente de null
						if (xTemAnterior){
							//Concatena a string com a vírgula
							xStringBuilder.append(", ");
						}
					}
					xTemAnterior = true;
					//Concatena sstring
					xStringBuilder.append(pStrings[xX].toString().trim());
				}
			}
		    return xStringBuilder.toString();
		}else{
			return null;
		}
	}
	
//	/**
//	 * Retorna string concatenando de duas a cinco string recebidas
//	 * @param pDado1 Primeira String
//	 * @param pDado2 Segunda String
//	 * @return String contatenada
//	 */
//	public static String joinString(String pDado1, String pDado2){
//		if (!DBSObject.isEmpty(pDado1) &&
//			!DBSObject.isEmpty(pDado2)){
//			return new StringBuffer(pDado1.trim()).append(", ").append(pDado2.trim()).toString();
//			//return pDado1.trim() + ", " + pDado2.trim(); StringBuffer é ais rápido que string
//		}
//		else{
//			if(DBSObject.isEmpty(pDado1)){
//				return pDado2;
//			}
//			else{
//				return pDado1;
//			}
//		}
//	}
//	public static String joinString(String pDado1, String pDado2, String pDado3){
//		String xS = joinString(pDado1, pDado2);
//		xS = joinString(xS, pDado3);
//		return xS;
//	}
//	public static String joinString(String pDado1, String pDado2, String pDado3, String pDado4){
//		String xS = joinString(pDado1, pDado2, pDado3);
//		xS = joinString(xS, pDado4);
//		return xS;
//	}
//	public static String joinString(String pDado1, String pDado2, String pDado3, String pDado4, String pDado5){
//		String xS = joinString(pDado1, pDado2, pDado3, pDado4);
//		xS = joinString(xS, pDado5);
//		return xS;
//	}

	/**
	 * Converte um valor numérico para string, excluido a separação decimal e fixando o tamanho das casas decimais
	 * @param pValor Valor numérico a ser convertido;
	 * @param pTamanhoMinimoDaString Tamanho mínimo que ter� a string de retorno;
	 * @param pCadasDecimais Quantidade de casas decimais que se�o consideradas como significativas.
	 * @return String
	 */
	public static String getNumeroSemPonto(Double pValor, int pTamanhoMinimoDaString, int pCasasDecimais){
		if (DBSObject.isEmpty(pValor) || 
				DBSObject.isEmpty(pTamanhoMinimoDaString) ||
				DBSObject.isEmpty(pCasasDecimais)){
			return null;
		}
		Double xN = DBSNumber.exp(10D, new Double(pCasasDecimais));
		xN = DBSNumber.multiply(pValor, xN);
		DecimalFormat xF = new DecimalFormat(repeat("0",pCasasDecimais));
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
	 * Separa um Array a patir de uma String, separado por um delimitador informado
	 * Antigo: BreakStringIntoArray(ByVal pString As String, pArray() As String, ByVal pDelimitador As String)
	 * @param pTextoBase String com o texto que se deseja separar
	 * @param pDelimitador String que será utilizado para separar os campos
	 * @return Array com o conteúdo em cada linha 
	 */
	public static ArrayList<String> toArray(String pTextoBase, String pDelimitador){
		return toArray(pTextoBase, pDelimitador, true, true);
	}
		
	/**
	 * Separa um Array a patir de uma String, separado por um delimitador informado
	 * Antigo: BreakStringIntoArray(ByVal pString As String, pArray() As String, ByVal pDelimitador As String)
	 * @param pTextoBase String com o texto que se deseja separar
	 * @param pDelimitador String que será utilizado para separar os campos
	 * @param pCaseMatch indica se o delimitador considerará o caixa 
	 * @return Array com o conteúdo em cada linha 
	 */
	public static ArrayList<String> toArray(String pTextoBase, String pDelimitador, boolean pCaseMatch){
		return toArray(pTextoBase, pDelimitador, pCaseMatch, true);
	}
	
	/**
	 * Separa um Array a patir de uma String, separado por um delimitador informado
	 * Antigo: BreakStringIntoArray(ByVal pString As String, pArray() As String, ByVal pDelimitador As String)
	 * @param pTextoBase String com o texto que se deseja separar
	 * @param pDelimitador String que será utilizado para separar os campos. Não faz a delimitação dos campos se delimitador for nulo ou vázio
	 * @param pCaseMatch indica se o delimitador considerará o caixa
	 * @param pTrim Indicador se exclui espaços no inicio e fim da string antes de inclui-lá no array
	 * @return Array com o conteúdo em cada linha 
	 */
	public static ArrayList<String> toArray(String pTextoBase, String pDelimitador, boolean pCaseMatch, boolean pTrim){
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
	 * Corrige erros ortográficos a partir do dicionário interno.
	 * Para inclur novas palavras, deve-se editar os arquivs: dicionario_acento e dicionario_palavra.
	 * dicionario_acento: correções de acentuação assumindo que a palavra esta correta
	 * dicionario_palavra: troca uma palavra pela outra, considerando inclusive a caixa da letra
	 * @param pTexto
	 * @return
	 */	
	public static String corretorOrtografico(String pTexto){
		String xTexto =  pTexto;
		if(wDicionarioSilaba.isEmpty()){
			pvDicionarioInit();
		}
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
	 * Retorna se a string é somente ccontinuida de letra alfabética
	 * @param pString
	 * @return
	 */
	public static boolean isAlphabetic(String pString){
		if (pString == null){
			return false;
		}
		for (int xMI =0 ; xMI < pString.length(); xMI++){
			char xC = pString.charAt(xMI);
			if(!Character.isAlphabetic(xC)){
				return false;
			}
		}
		return true;
	}
	
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
	 * Retorna uma String com os itens contidos em list, separados por vírgula
	 * @param pList
	 * @return
	 */
	public static <T> String listToString(List<T> pList){
		if (pList == null ||
			pList.size() == 0){
			return "";
		}
		String xString = "";
		String xTest;
		for (Object xO:pList){
			xTest = DBSString.toString(xO,null);
			if (xTest!=null){
				if (!xString.equals("")){
					xString += ",";
				}
				xString += xTest;
			}
		}
		return xString;
	}
	//*******************************************************************************************************
	// Private
	//*******************************************************************************************************
	
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
	
		xPalavras = DBSString.toArray(pTexto, " ", false);
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
				xTexto = xTexto + " " + xPalavra;
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

	
//	/**
//	 * Corrige erros ortográficos a partir do dicionário interno.
//	 * Para inclur novas palavras, deve-se editar os arquivs: dicionario_acento e dicionario_palavra.
//	 * dicionario_acento: correções de acentuação assumindo que a palavra esta correta
//	 * dicionario_palavra: troca uma palavra pela outra, considerando inclusive a caixa da letra
//	 * @param pTexto
//	 * @return
//	 */
//	public static String CorretorOrtografico2(String pTexto){
//		pTexto = pvCorretorOrtograticoSilaba2(pTexto);
//		pTexto = pvCorretorOrtograficoPalavra2(pTexto);
//		pTexto = pvCorretorOrtograficoFrase2(pTexto);
//		return pTexto;
//	}	
	
//
//	/**
//	 * Troca parte da palavras por outro texto considerando a caixa da letra a partir do dicinário interno
//	 * É respeitado a ordem que a frase está no dicionário 
//	 * @param pTexto
//	 * @return
//	 */
//	private static String pvCorretorOrtograticoSilaba2(String pTexto){
//		String 				xTexto = pTexto;
//		DBSLinkedProperties xProps = new DBSLinkedProperties();
//		try {
//			xProps.load(DBSString.class.getResourceAsStream("/META-INF/dicionario_silaba.properties"));
//			//xProps.load(new FileInputStream("dicionario_acento.properties"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Enumeration<Object> xE = xProps.keys();
//		String xKey = ""; 
//		while (xE.hasMoreElements()){
//			xKey = (String) xE.nextElement();
//			xTexto = DBSString.changeStr(xTexto, xKey, xProps.getProperty(xKey),true);
//		}
//		return xTexto;
//	}	
//	/**
//	 * Troca uma palavra pela outra considerando a caixa da letra a partir do dicinário interno
//	 * É respeitado a ordem que a frase está no dicionário 
//	 * @param pTexto
//	 * @return
//	 */
//	private static String pvCorretorOrtograficoPalavra2(String pTexto){
//		List<String> 		xPalavras = new ArrayList<String>();
//		//String 		 	xPalavraNova = ""; 
//		String 		 		xTexto = "";
//		//char 		 		xChar;
//		DBSLinkedProperties xProps = new DBSLinkedProperties();
//		try {
//			xProps.load(DBSString.class.getResourceAsStream("/META-INF/dicionario_palavra.properties"));
//			//xProps.load(DBSString.class.getResourceAsStream("dicionario_palavra.properties"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		String xPalavraErrada = ""; 
//		xPalavras = DBSString.toArray(pTexto, " ", false);
//		for (String xPalavra: xPalavras){
//			Enumeration<Object> xE = xProps.keys();
//			while (xE.hasMoreElements()){
//				xPalavraErrada = (String) xE.nextElement();
//				if (xPalavraErrada.equals(xPalavra)){
//					xPalavra = DBSString.changeStr(xPalavra, xPalavraErrada, xProps.getProperty(xPalavraErrada), true);
//					break;
//				}
//			}
//			if (xTexto.equals("")){
//				xTexto = xPalavra; 
//			}else{
//				xTexto = xTexto + " " + xPalavra;
//			}
//		}
//		return xTexto;
//	}
//
//	/**
//	 * Troca frase(Mais de uma palavra) por outra(s) considerando a caixa da letra a partir do dicinário interno
//	 * É respeitado a ordem que a frase está no dicionário 
//	 * @param pTexto
//	 * @return
//	 */
//	private static String pvCorretorOrtograficoFrase2(String pTexto){
//		String 				xTexto = pTexto;
//		DBSLinkedProperties xProps = new DBSLinkedProperties();
//		try {
//			xProps.load(DBSString.class.getResourceAsStream("/META-INF/dicionario_frase.properties"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		Enumeration<Object> xE = xProps.keys();
//		String xKey = ""; 
//		while (xE.hasMoreElements()){
//			xKey = (String) xE.nextElement();
//			xTexto = DBSString.changeStr(xTexto, xKey, xProps.getProperty(xKey),true);
//		}
//		return xTexto;
//	}
//	
}
