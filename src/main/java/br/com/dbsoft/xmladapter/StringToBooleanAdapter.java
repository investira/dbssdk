/**
 * 
 */
package br.com.dbsoft.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author jose.avila@dbsoft.com.br
 *
 */
public class StringToBooleanAdapter extends XmlAdapter<String, Boolean> {

	@Override
	public String marshal(Boolean pValue) {
		if (pValue) {
			return "Sim";
		} else {
			return "Não";
		}
	}

	@Override
	public Boolean unmarshal(String pValue) {
		if (pValue.equals("S") || pValue.equals("SIM") || pValue.equals("Sim") || pValue.equals("Aberto")) {
			return true;
		} else {
			return false;
		}
	}

}
