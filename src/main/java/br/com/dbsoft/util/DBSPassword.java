package br.com.dbsoft.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.log4j.Logger;


/**
 * @author ricardo.villar
 *
 */
public class DBSPassword {

	protected static Logger	wLogger = Logger.getLogger(DBSPassword.class);

	public static String 	Algorithm = "PBKDF2WithHmacSHA1";
	public static String	Salt = "dbs";
	public static int		SaltLenghtInBytes = 15;
	public static int		PasswordLenght = SaltLenghtInBytes * 2;
	public static int		Iterations = 9000;
	
    /**
     * Retorna string com valores em hexa, gerados randomicamente.
     * O tamanho da string retornada é o dobro do tamanho difido por <b>altLenghtInBytes</b>.
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String createRandomSalt(){
		byte[] xSalt = new byte[SaltLenghtInBytes];
		try {
			SecureRandom.getInstance("SHA1PRNG").nextBytes(xSalt);
		} catch (NoSuchAlgorithmException e) {
			wLogger.error(e);
		}
		return DBSString.toHex(xSalt);
    }

    /**
     * Retorna String com a senha criptografada a partir do texto informado adicionando um hash criado randomicamente(<b>salt</b>) para obrigar que todos os textos sejam sempre diferentes.<br/>
     * Para a validação da senha porteriormente, deve-se utilizar o método<b>validateSaltedPassword</b>.
     * @param pPlainPassword
     * @param pHashLenght
     * @return
     */
    public static String createSaltedPassword(String pPlainPassword) {
		String xSalt = createRandomSalt(); 
    	byte[] xHash = pvGetHash(xSalt, pPlainPassword);
		return DBSString.toHex(xHash) + xSalt;
	}

    /**
     * Retorna String com a senha criptografada a partir do texto informado adicionando utilizando um <b>salt</b>) padrão.<br/>
     * Para a validação da senha porteriormente, deve-se utilizar o método<b>validatePassword</b>.
     * @param pPlainPassword
     * @return
     */
    public static String createPassword(String pPlainPassword) {
    	byte[] xHash = pvGetHash(DBSString.toHex(Salt.getBytes()), pPlainPassword);
		return DBSString.toHex(xHash);
	}

    public static String createPassword(String pSalt, String pPlainPassword, Integer pLength) {
    	byte[] xHash = pvGetHash(DBSString.toHex(pSalt.getBytes()), pPlainPassword, pLength);
		return DBSString.toHex(xHash);
	}
    
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
    
    public static boolean validatePassword(String pPlainPassword, String pStoredPassword){
      	//Converte para decimal
    	byte[] xStoredPassword = DBSString.fromHex(pStoredPassword);
        //Cryptografa senha limpa para comparar com a senha armazenada
    	byte[] xPasswordHash = pvGetHash(DBSString.toHex(Salt.getBytes()), pPlainPassword);
         
        return pvIsEqual(xPasswordHash, xStoredPassword);
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
     * Cria string hash criptografado a partir da string informada.
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


//    private static byte[] pvGetPlainText(String pSalt, String pHash, int pHashLenght) throws NoSuchAlgorithmException, InvalidKeySpecException{
//    	SecretKeyFactory xFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//    	xFactory.
//    }

    
//    /**
//     * Converte array de números decimais para valores em hexa.
//     * @param pArray
//     * @return
//     * @throws NoSuchAlgorithmException
//     */
//    private static String pvToHex(byte[] pArray) throws NoSuchAlgorithmException{
//        BigInteger xBigInteger = new BigInteger(1, pArray);
//        String xHex = xBigInteger.toString(16);
//        int xPaddingLength = (pArray.length * 2) - xHex.length();
//        if(xPaddingLength > 0)
//        {
//            return String.format("%0"  +xPaddingLength + "d", 0) + xHex;
//        }else{
//            return xHex;
//        }
//    }
    
    
    



	
}
