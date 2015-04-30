/**
 * 
 */
package br.com.dbsoft.xmladapter;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.dbsoft.util.DBSDate;

/**
 * @author jose.avila@dbsoft.com.br
 *
 */
public class StringToDateSQLAdapter extends XmlAdapter<String, java.sql.Date> {

	private SimpleDateFormat wFormat = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	public String marshal(Date pValue) {
		return wFormat.format(pValue);
	}

	@Override
	public Date unmarshal(String pValue) {
		if (pValue.contains("/") || pValue.contains("-")) {
			return DBSDate.toDate(pValue);
		} else {
			return DBSDate.toDateYYYYMMDD(pValue);
		}
	}

}
