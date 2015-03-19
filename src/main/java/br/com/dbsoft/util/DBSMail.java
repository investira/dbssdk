package br.com.dbsoft.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class DBSMail {
	
	/**
	 * Retorna se é um email válido
	 * @param email
	 * @return
	 */
	public static boolean isValidEmailAddress(String email) {
	   boolean result = true;
	   try {
	      InternetAddress emailAddr = new InternetAddress(email);
	      emailAddr.validate();
	   } catch (AddressException ex) {
	      result = false;
	   }
	   return result;
	}
}
