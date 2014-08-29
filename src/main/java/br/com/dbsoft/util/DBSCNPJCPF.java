/**
 * 
 */
package br.com.dbsoft.util;

import java.util.InputMismatchException;

import org.apache.log4j.Logger;

/**
 * @author Avila
 *
 * Classe que verifica se o numero é um CNPJ ou CPF válido.
 */
public class DBSCNPJCPF {

	private static Logger wLogger = Logger.getLogger(DBSCNPJCPF.class);
	
	/**
	 * Retormna se CNPJ ou CPF é válido.<br/>
	 * Identificará se é CPF ou CNPJ pelo quantidade de caracteres enviados.
	 * @param pCPFCNPJ
	 * @return boolean
	 */
	public static boolean validarCPFCNPJ(String pCPFCNPJ) {
		if (DBSObject.isEmpty(pCPFCNPJ)) {
			return false;
		}
		if (pCPFCNPJ.length() > 11) {
			if (isCNPJ(pCPFCNPJ)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (isCPF(pCPFCNPJ)) {
				return true;
			} else {
				return false; 
			}
		}
	}
	
	/**
	 * Retorna se CNPJ é válido.
	 * @param pCNPJ
	 * @return boolean
	 */
	public static boolean isCNPJ(String pCNPJ) {
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (DBSObject.isEmpty(pCNPJ)){
			return false;
		}
		if (pCNPJ.equals("00000000000000") 
		 || pCNPJ.equals("11111111111111")
		 || pCNPJ.equals("22222222222222")
		 || pCNPJ.equals("33333333333333")
		 || pCNPJ.equals("44444444444444")
		 || pCNPJ.equals("55555555555555")
		 || pCNPJ.equals("66666666666666")
		 || pCNPJ.equals("77777777777777")
		 || pCNPJ.equals("88888888888888")
		 || pCNPJ.equals("99999999999999") || (pCNPJ.length() != 14))
			return (false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-ésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = (pCNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (pCNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			// Verifica se os dígitos calculados conferem com os dígitos
			// informados.
			if ((dig13 == pCNPJ.charAt(12)) && (dig14 == pCNPJ.charAt(13)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			wLogger.error(erro);
			return (false);
		}
	}

	/**
	 * Retorna se CPF é válido.
	 * @param pCPF
	 * @return boolean
	 */
	public static boolean isCPF(String pCPF) {
		if (DBSObject.isEmpty(pCPF)){
			return false;
		}
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (pCPF.equals("00000000000") 
		 || pCPF.equals("11111111111")
		 || pCPF.equals("22222222222") 
		 || pCPF.equals("33333333333")
		 || pCPF.equals("44444444444") 
		 || pCPF.equals("55555555555")
		 || pCPF.equals("66666666666") 
		 || pCPF.equals("77777777777")
		 || pCPF.equals("88888888888") 
		 || pCPF.equals("99999999999")
		 || (pCPF.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo
		// (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = (pCPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48); // converte no respectivo caractere
											// numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (pCPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			// Verifica se os digitos calculados conferem com os digitos
			// informados.
			if ((dig10 == pCPF.charAt(9)) && (dig11 == pCPF.charAt(10)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			wLogger.error(erro);
			return (false);
		}
	}
	
	/**
	 * Retira a máscara do CNPJ / CPF retornando a string apenas com números
	 * @param pCNPJ
	 * @return String
	 */
	public static String noMask(String pCNPJ) {
		if (DBSObject.isEmpty(pCNPJ)){
			return "";
		}
		int xInStr = DBSString.getInStr(pCNPJ, ".");
		while (xInStr > 0) {
			pCNPJ = DBSString.getSubString(pCNPJ, 1, xInStr-1) + DBSString.getSubString(pCNPJ, xInStr+1, pCNPJ.length());
			xInStr = DBSString.getInStr(pCNPJ, ".");
		}
		xInStr = DBSString.getInStr(pCNPJ, "/");
		pCNPJ = DBSString.getSubString(pCNPJ, 1, xInStr-1) + DBSString.getSubString(pCNPJ, xInStr+1, pCNPJ.length());
		xInStr = DBSString.getInStr(pCNPJ, "-");
		pCNPJ = DBSString.getSubString(pCNPJ, 1, xInStr-1) + DBSString.getSubString(pCNPJ, xInStr+1, pCNPJ.length());
		return pCNPJ;
	}
	
	/**
	 * Retorna CPF ou CNPJ formatado.<br/>
	 * Identificará se é CPF ou CNPJ pelo quantidade de caracteres enviados.
	 * @param pCPFCNPJ
	 * @return
	 */
	public static String formatCNPJ_CPF(String pCPFCNPJ) {
		if (DBSObject.isEmpty(pCPFCNPJ)){
			return "";
		}

		if (pCPFCNPJ.length() > 11) {
			if (isCNPJ(pCPFCNPJ)) {
				return pvFormatCNPJ(pCPFCNPJ);
			} else {
				wLogger.error("CNPJ "+ pCPFCNPJ +" Inválido!");
				return pCPFCNPJ;
			}
		} else {
			if (isCPF(pCPFCNPJ)) {
				return pvFormatCPF(pCPFCNPJ);
			} else {
				wLogger.error("CPF "+ pCPFCNPJ +" Inválido!");
				return pCPFCNPJ; 
			}
		}
	}
	
	private static String pvFormatCNPJ(String pCNPJ) {
		// máscara do CNPJ: 99.999.999.9999-99
		return (pCNPJ.substring(0, 2) + "." + 
				pCNPJ.substring(2, 5) + "." + 
				pCNPJ.substring(5, 8) + "." + 
				pCNPJ.substring(8, 12) + "-" + 
				pCNPJ.substring(12, 14));
	}
	
	private static String pvFormatCPF(String pCPF) {
		// máscara do CNPJ: 999.999.999-99
		return (pCPF.substring(0, 3) + "." + 
				pCPF.substring(3, 6) + "." + 
				pCPF.substring(6, 9) + "-" + 
				pCPF.substring(9, 11));
	}
}
