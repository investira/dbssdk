/**
 * 
 */
package br.com.dbsoft.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Avila
 *
 */
public class StringtToDoubleAdapter extends XmlAdapter<String, Double> {

	@Override
	public String marshal(Double pValor) {
		return pValor.toString();
	}

	@Override
	public Double unmarshal(String pValor) {
		pValor = pValor.replace(',', '.');
		return Double.valueOf(pValor); 
	}
}