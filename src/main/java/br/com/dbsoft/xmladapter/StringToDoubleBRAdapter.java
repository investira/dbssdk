/**
 * 
 */
package br.com.dbsoft.xmladapter;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSString;

/**
 * @author jose.avila@dbsoft.com.br
 *
 */
public class StringToDoubleBRAdapter extends XmlAdapter<String, Double> {

	@Override
	public String marshal(Double pValor) {
		return DBSString.toString(pValor, null);
	}

	@Override
	public Double unmarshal(String pValor) {
		return DBSNumber.toDouble(pValor, 0D, new Locale("pt","BR"));
	}
}