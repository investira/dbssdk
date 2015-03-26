package br.com.dbsoft.util;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import br.com.dbsoft.mail.DBSEmailAddress;

public class DBSEmail {
	
	/**
	 * Retorna se é um email válido
	 * @param email
	 * @return
	 */
	public static boolean isValidEmailAddress(String email) {
	   boolean xResult = true;
	   try {
	      InternetAddress xEmailAddr = new InternetAddress(email);
	      xEmailAddr.validate();
	   } catch (AddressException ex) {
	      xResult = false;
	   }
	   return xResult;
	}
	
	public static List<DBSEmailAddress> validateEmailAddress(List<DBSEmailAddress> pArrayList) {
		List<DBSEmailAddress> xList = new ArrayList<DBSEmailAddress>();
		for (DBSEmailAddress xDbsEmailAddress : pArrayList) {
			if (isValidEmailAddress(xDbsEmailAddress.getAddress())) {
				xList.add(xDbsEmailAddress);
			}
		}
		return xList;
	}
}
