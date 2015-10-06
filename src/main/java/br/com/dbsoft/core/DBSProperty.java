package br.com.dbsoft.core;

import java.util.ResourceBundle;

public class DBSProperty {
	
	/**
	 * Retorna o valor de uma propriedade contida em arquivo properties
	 * @param pBundle
	 * @param pProperty
	 * @return
	 */
	public static String getBundleProperty(String pBundle, String pProperty){
		ResourceBundle xProperties = ResourceBundle.getBundle(pBundle);
		if (xProperties !=null){
			return xProperties.getString(pProperty);
		}else{
			return null;
		}
	}
}
