package br.com.dbsoft.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.log4j.Logger;


/**
 * @author ricardo.villar
 *
 */
/**
 * @author ricardo.villar
 *
 */
public class DBSPassword {

	protected static Logger	wLogger = Logger.getLogger(DBSPassword.class);

	public static String 	Algorithm 		= "PBKDF2WithHmacSHA1";
	public static String	Salt 			= "dbs";
	public static int		SaltLenghtInBytes = 15;
	public static int		PasswordLenght = SaltLenghtInBytes * 2;
	public static int		Iterations = 9000;
	public static String	ValidsCharacters = "\\-!@#$%&_+=;:,.";
	
    /**
     * Retorna string com valores em hexa, gerados randomicamente.
     * O tamanho da string retornada é o dobro do tamanho difido por <b>altLenghtInBytes</b>.
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String createRandomSalt(){
    	return createRandomSalt(SaltLenghtInBytes);
    }
	public static String createRandomSalt(int pTamanho){
		byte[] xSalt = new byte[pTamanho];
		try {
			SecureRandom.getInstance("SHA1PRNG").nextBytes(xSalt);
		} catch (NoSuchAlgorithmException e) {
			wLogger.error(e);
		}
		return DBSString.toHex(xSalt);
    }

    /**
     * Retorna String com a senha (de 30 caracteres) criptografada a partir do texto informado adicionando um hash criado randomicamente(<b>salt</b>) para obrigar que todos os textos sejam sempre diferentes.<br/>
     * Para a validação da senha porteriormente, deve-se utilizar o método<b>validateSaltedPassword</b>.
     * @param pPlainPassword
     * @param pHashLenght
     * @return
     */
    public static String createSaltedPassword(String pPlainPassword) {
		return createSaltedPassword(pPlainPassword, PasswordLenght);
	}

    /**
     * Retorna String com a senha criptografada a partir do texto informado adicionando um hash criado randomicamente(<b>salt</b>) para obrigar que todos os textos sejam sempre diferentes.<br/>
     * Para a validação da senha porteriormente, deve-se utilizar o método<b>validateSaltedPassword</b>.</br>.
     * O tamanho da senha retornarda para ser inferior ao tamanho solicitado.<br/>
     * @param pPlainPassword
     * @param pHashLenght
     * @return
     */
    public static String createSaltedPassword(String pPlainPassword, Integer pLength) {
    	if (pLength < 30){
    		wLogger.error("Tamanho para o resultado criptografado da senha precisa ser superior a 30.");
    		return null;
    	}
		String xSalt = createRandomSalt(); 
    	byte[] xHash = pvGetHash(xSalt, pPlainPassword, pLength - PasswordLenght);
		return DBSString.toHex(xHash) + xSalt;
	}

    /**
     * Retorna String com a senha (de 30 caracteres) criptografada a partir do texto informado adicionando utilizando um <b>salt</b>) padrão.<br/>
     * Para a validação da senha posteriormente, deve-se utilizar o método <b>validatePassword</b>.
     * Algoritmo PBKDF2WithHmacSHA1
     * @param pPlainPassword
     * @return
     */
    public static String createPassword(String pPlainPassword) {
    	if (pPlainPassword==null){return null;}
    	byte[] xHash = pvGetHash(DBSString.toHex(Salt.getBytes()), pPlainPassword);
		return DBSString.toHex(xHash);
	}

    public static String createPassword(String pSalt, String pPlainPassword, Integer pLength) {
    	if (pPlainPassword==null
		 || pSalt==null
		 || pLength == null
		 || pLength == 0){return null;}
    	byte[] xHash = pvGetHash(DBSString.toHex(pSalt.getBytes()), pPlainPassword, pLength);
		return DBSString.toHex(xHash);
	}
    
    /**
     * Cria string hash criptografado de 256 bits a partir da string informada.
     * @param pPlainPassword
     * @return
     */
    public static String createPassword256(String pPlainPassword) {
    	if (pPlainPassword==null){return null;}
    	byte[] xHash = pvGetHash256(pPlainPassword);
        return DBSString.toHex(xHash);
    }
    
    /**
     * Cria string hash criptografado de 512 bits a partir da string informada.
     * @param pPlainPassword
     * @return
     */
    public static String createPassword512(String pPlainPassword) {
    	if (pPlainPassword==null){return null;}
    	byte[] xHash = pvGetHash512(pPlainPassword);
		return DBSString.toHex(xHash);
    }
    
    /**
     * Verifica se a senha (de 30 caractes) informada em <b>pPlainPassword</b> é iqual a senha já criptografada informada em <b>pStoredPassword</b>.
     * @param pPlainPassword
     * @param pStoredPassword
     * @return
     */
    public static boolean validateSaltedPassword(String pPlainPassword, String pStoredPassword){
    	//Recupera parte da senha armazenada que é a efetiva senha do usuário
    	String xPassword = DBSString.getSubString(pStoredPassword, 1, PasswordLenght);
    	//Recupera parte da senha armazenada que  o salt
    	String xSalt = DBSString.getSubString(pStoredPassword, PasswordLenght + 1, pStoredPassword.length());
    	//Converte para decimal
    	byte[] xStoredPassword = DBSString.fromHex(xPassword);
        //Cryptografa senha limpa para comparar com a senha armazenada
    	byte[] xPasswordHash = pvGetHash(xSalt, pPlainPassword);
    
        return pvIsEqual(xPasswordHash, xStoredPassword);
    }
    
    /**
     * Verifica se a senha informada em <b>pPlainPassword</b> é iqual a senha já criptografada informada em <b>pStoredPassword</b>.
     * @param pPlainPassword
     * @param pStoredPassword
     * @return
     */
    public static boolean validatePassword(String pPlainPassword, String pStoredPassword){
      	//Converte para decimal
    	byte[] xStoredPassword = DBSString.fromHex(pStoredPassword);
        //Cryptografa senha limpa para comparar com a senha armazenada
    	byte[] xPasswordHash = pvGetHash(DBSString.toHex(Salt.getBytes()), pPlainPassword);
         
        return pvIsEqual(xPasswordHash, xStoredPassword);
    }
    /**
     * Verifica se a senha informada em <b>pPlainPassword</b> é iqual a senha já criptografada (256bits) informada em <b>pStoredPassword</b>.
     * @param pPlainPassword
     * @param pStoredPassword
     * @return
     */
    public static boolean validatePassword256(String pPlainPassword, String pStoredPassword){
      	//Converte para decimal
    	byte[] xStoredPassword = DBSString.fromHex(pStoredPassword);
        //Cryptografa senha limpa para comparar com a senha armazenada
    	byte[] xPasswordHash = pvGetHash256(pPlainPassword);
         
        return pvIsEqual(xPasswordHash, xStoredPassword);
    }
    /**
     * Verifica se a senha informada em <b>pPlainPassword</b> é iqual a senha já criptografada (512bits) informada em <b>pStoredPassword</b>.
     * @param pPlainPassword
     * @param pStoredPassword
     * @return
     */
    public static boolean validatePassword512(String pPlainPassword, String pStoredPassword){
      	//Converte para decimal
    	byte[] xStoredPassword = DBSString.fromHex(pStoredPassword);
        //Cryptografa senha limpa para comparar com a senha armazenada
    	byte[] xPasswordHash = pvGetHash512(pPlainPassword);
         
        return pvIsEqual(xPasswordHash, xStoredPassword);
    }

	/**
	 * Retorna se senha contém os caracteres obrigatórios e válidos.<br/>
	 * O tamanho mínimo é de 8 caracteres e máximo de 30 caracteres, 
	 * sendo obrigatórios: uma letras maiúscula, uma letra minúscula e um números.<br/>
	 * São permitidos a utilização dos sinais <b>-!@#$%&_+=.;:,<b/>.<br/>
	 * @param pPassword Senha
	 * @return
	 */
	public static Boolean checkPasswordContent(String pPassword){
		return checkPasswordContent(pPassword, 8, 30);
	}

	/**
	 * Retorna se senha contém os caracteres obrigatórios e válidos.<br/>
	 * Caracteres obrigatórios: uma letras maiúscula, uma letra minúscula e um números.<br/>
	 * São permitidos a utilização dos sinais <b>-!@#$%&_+=.;:,<b/>.
	 * @param pPassword Senha
	 * @param pMinSize Tamanho mínimo
	 * @param pMaxSize Tamanho máximo
	 * @return
	 */
	public static Boolean checkPasswordContent(String pPassword, Integer pMinSize, Integer pMaxSize){
		return checkPasswordContent(pPassword, pMinSize, pMaxSize, true, true, true);
	}

	/**
	 * Retorna se senha contém os caracteres obrigatórios e válidos.<br/>
	 * Permitidos: letras, números e os sinais <b>-!@#$%&_+=.;:,<b/>.
	 * @param pPassword Senha
	 * @param pMinSize Tamanho mínimo
	 * @param pMaxSize Tamanho máximo
	 * @param pUpperCase Se deverá conter um caracter maiúsculo
	 * @param pLowerCase Se deverá conter um caracter minúsculo
	 * @param pNumber se deverá conter um número
	 * @param pOthers
	 * @return
	 */
	public static Boolean checkPasswordContent(String pPassword, Integer pMinSize, Integer pMaxSize, boolean pUpperCase, boolean pLowerCase, boolean pNumber){
		return checkPasswordContent(pPassword, pMinSize, pMaxSize, pUpperCase, pLowerCase, pNumber, ValidsCharacters);
	}

	/**
	 * Retorna se senha contém os caracteres obrigatórios e válidos.<br/>
	 * Permitidos: letras, números e os sinais <b>-!@#$%&_+=.;:,<b/>.
	 * @param pPassword Senha
	 * @param pMinSize Tamanho mínimo
	 * @param pMaxSize Tamanho máximo
	 * @param pUpperCase Se deverá conter um caracter maiúsculo
	 * @param pLowerCase Se deverá conter um caracter minúsculo
	 * @param pNumber se deverá conter um número
	 * @param pOthers
	 * @return
	 */
	public static Boolean checkPasswordContent(String pPassword, Integer pMinSize, Integer pMaxSize, boolean pUpperCase, boolean pLowerCase, boolean pNumber, String pOthers){
		if (pPassword == null
		 || pMinSize == null 
		 || pMaxSize == null
		 || pMinSize > pMaxSize){
			return false;
		}
		if (pPassword.length() < pMinSize
		 || pPassword.length() > pMaxSize){
			return false;
		}
		StringBuilder xRegex = new StringBuilder();
		
		/*
		 * ^(
		 * (?=.*\d)
		 * (?=.*[a-z])
		 * (?=.*[A-Z])
		 * (?!.*\s)
		 * .{8,20}
		 * )[a-zA-Z0-9!@#$%~&*_+-=;:,.]$ 
		 */
//		 Tem que ter 1 A a
		 //^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,15}$
		
		//Precisa ter somente estes, senão vira vazio
		//(^[a-zA-Z0-9]+$)
		//(^[a-zA-Z0-9\-!@#$%&_+=;:,.]+$)
		
		//(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s)(^[a-zA-Z0-9\-!@#$%&_+=;:,.]+$)
		
//		  ^((?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).{8,20})[a-zA-Z0-9\-!@#$%&_+=;:,.]$ 
		
		//(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s)(^[a-zA-Z0-9\-!@#$%&_+=;:,.]+$)
		
//		^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s)(^[a-zA-Z0-9\-!@#$%&_+=;:,.]+$)
		
		xRegex.append("^");
		//Sem expaços
		xRegex.append("(?!.*\\s)");
		//Obriga uma letra maiúscula
		if (pUpperCase){
			xRegex.append("(?=.*[A-Z])");
		}
		//Obriga uma letra minúscula
		if (pLowerCase){
			xRegex.append("(?=.*[a-z])");
		}
		//Obriga um número
		if (pNumber){
			xRegex.append("(?=.*\\d)");
		}
		//Caracteres válidos
		xRegex.append("(^[a-zA-Z0-9");
		xRegex.append(DBSObject.getNotNull(pOthers, ""));
		xRegex.append("]+$)");
		
		return pPassword.matches(xRegex.toString());
	}
	

    private static boolean pvIsEqual(byte[] xPasswordHash, byte[] pStoredPassword){
        int xDiff = pStoredPassword.length ^ xPasswordHash.length;
        for(int i = 0; i < pStoredPassword.length && i < xPasswordHash.length; i++)
        {
            xDiff |= pStoredPassword[i] ^ xPasswordHash[i];
        }
        return xDiff == 0;
    }
    

    /**
     * Cria String (de 30 caracteres) hash criptografado a partir da string informada.
     * @param pSalt String adicional que será utilizada para criptografar a senha.
     * @param pPlainPassword Senha não criptografada
     * @return
     */
    private static byte[] pvGetHash(String pSalt, String pPlainPassword){
		return pvGetHash(pSalt, pPlainPassword, PasswordLenght);
 	}

    /**
     * Cria string hash criptografado a partir da string informada.
     * @param pSalt String adicional que será utilizada para criptografar a senha.
     * @param pPlainPassword Senha não criptografada
     * @return
     */
    private static byte[] pvGetHash(String pSalt, String pPlainPassword, Integer pLenght){
		byte[] xSalt = DBSString.fromHex(pSalt);
		KeySpec xSpec = new PBEKeySpec(pPlainPassword.toCharArray(), xSalt, Iterations, pLenght * 4);
		try {
			SecretKeyFactory xFactory = SecretKeyFactory.getInstance(Algorithm);
			
			return xFactory.generateSecret(xSpec).getEncoded();
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			wLogger.error(e);
			return null;
		}
	}
    
    /**
     * Novo Método para Hash 256
     * @param pPlainPassword
     * @return
     */
    private static byte[] pvGetHash256(String pPlainPassword) {
		MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("SHA-256");
			return algorithm.digest(pPlainPassword.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			wLogger.error(e);
			return null;
		}
	}
    
    /**
     * Novo Método para Hash 256
     * @param pPlainPassword
     * @return
     */
    private static byte[] pvGetHash512(String pPlainPassword) {
		MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("SHA-512");
			return algorithm.digest(pPlainPassword.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			wLogger.error(e);
			return null;
		}
	}

    //VALIDACAO DE REGRAS DA SENHA
    /**
	 * (			# Início do grupo
	 * (?=.*\d)			# deve conter ao menos um digito de 0-9
	 * (?=.*[a-z])		# deve conter ao menos uma letra minúscula
	 * (?=.*[A-Z])		# deve conter ao menos uma letra maiúscula
	 * (?=.*[@#$%])		# deve conter ao menos um caracter especial da lista (@ ou # ou $ ou %)
	 * .				# combina qualquer coisa com a verificação anterior
	 * {6,20}			# deve ter tamanho de no mínimo 6 caracteres e no máximo 20
	 * )			# Fim do grupo
	 */
	private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
	
	/**
	 * Valida se a senha cumpre as regras mínimas
	 * A regra padrão é: deve conter ao menos um número, uma letra minúscula, 
	 * uma letra maiúscula e ter tamanho de no mínimo 6 e no máximo 20 caracteres.
	 * 
	 * @param pPassword senha para validação
	 * @return true Senha Válida, false Senha Inválida
	 */
	public static boolean validateRulePassword(String pPassword) {
		return validateRulePassword(pPassword, PASSWORD_PATTERN);
	}
	
	/**
	 * Valida se a senha cumpre as regras mínimas
	 * Recebe um Pattern com as regras que a senha deve segui.
	 * 
	 * @param pPassword senha para validação
	 * @param pPattern Regras em ExpressionLanguage que a Senha deve segui.
	 * @return true Senha Válida, false Senha Inválida
	 */
	public static boolean validateRulePassword(String pPassword, String pPattern) {
		Pattern xPattern = Pattern.compile(pPattern);
		Matcher xMatcher;
		
		xMatcher = xPattern.matcher(pPassword);
		return xMatcher.matches();
	}
}
