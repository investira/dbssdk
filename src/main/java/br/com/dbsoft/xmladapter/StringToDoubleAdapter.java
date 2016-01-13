/**
 * 
 */
package br.com.dbsoft.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSString;

/**
 * @author jose.avila@dbsoft.com.br
 *
 */
public class StringToDoubleAdapter extends XmlAdapter<String, Double> {

	@Override
	public String marshal(Double pValor) {
		return DBSString.toString(pValor, null);
	}

	@Override
	public Double unmarshal(String pValor) {
		String xValue = pValor;
		if (DBSString.getStringCount(xValue, ",") > 1) {
			xValue = DBSString.changeStr(xValue, ",", "");
		}
		if (xValue.contains(".")) {
			if (DBSString.getStringCount(xValue, ".") == 1) {
				if (DBSString.getStringCount(xValue, ",") == 0) {
					xValue = DBSString.changeStr(xValue, ".", ",");
				} else {
					xValue = DBSString.changeStr(xValue, ".", "");
				}
			} else {
				xValue = DBSString.changeStr(xValue, ".", "");
			}
		}
		return DBSNumber.toDouble(xValue);
	}
}