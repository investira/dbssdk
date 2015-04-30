/**
 * 
 */
package br.com.dbsoft.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSString;

/**
 * @author jose.avila@dbsoft.com.br
 */
public class StringToIntegerAdapter extends XmlAdapter<String, Integer> {

	@Override
	public String marshal(Integer pValor) {
		return DBSString.toString(pValor, null);
	}

	@Override
	public Integer unmarshal(String pValor) {
		return DBSNumber.toInteger(pValor);
	}
}