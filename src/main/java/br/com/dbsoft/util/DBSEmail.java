package br.com.dbsoft.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import br.com.dbsoft.mail.DBSEmailAddress;

public class DBSEmail {
	
//	private static String emailRegex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
//	private static String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
	private static Pattern emailPattern = Pattern.compile(emailRegex);

	/**
	 * Retorna se é um email válido
	 * @param pEmail
	 * @return
	 */
	public static boolean isValidEmailAddress(String pEmail) {
	   boolean xResult = true;
	   if (pEmail == null){return false;}
	   try {
		  if (emailPattern.matcher(pEmail).matches()){
		      InternetAddress xEmailAddr = new InternetAddress(pEmail);
		      xEmailAddr.validate();
		  }else{
			  xResult = false;
		  }
	   } catch (AddressException ex) {
	      xResult = false;
	   }
	   return xResult;
	}
	
	/**
	 * Retorna lista com os emails já validados
	 * @param pArrayList
	 * @return
	 */
	public static List<DBSEmailAddress> validateEmailAddress(List<DBSEmailAddress> pArrayList) {
		List<DBSEmailAddress> xList = new ArrayList<DBSEmailAddress>();
		for (DBSEmailAddress xDbsEmailAddress : pArrayList) {
			List<DBSEmailAddress> xArrayList = toArrayList(xDbsEmailAddress.getAddress());
			for (DBSEmailAddress xEmail:xArrayList) {
				xEmail.setName(xDbsEmailAddress.getName());
				xList.add(xEmail);
			}
		}
		return xList;
	}
	
	/**
	 * Retorna list com os emails já validados a partir de uma string. Email devem estar separados por '.' ou ';' ou espaço
	 * @param pArrayList
	 * @return
	 */
	public static List<DBSEmailAddress> toArrayList(String pEmails) {
		List<DBSEmailAddress> xList = new ArrayList<DBSEmailAddress>();
		List<String> xArrayList = DBSString.toArrayListRegex(pEmails,"[,;\\s]");
		for (String xEmail:xArrayList) {
			if (isValidEmailAddress(xEmail)) {
				DBSEmailAddress xNew = new DBSEmailAddress();
				xNew.setAddress(xEmail);
				xList.add(xNew);
			}
		}
		return xList;
	}

}
