/**
 * 
 */
package br.com.dbsoft.xmladapter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Avila
 *
 */
public class StringtToDateSQLAdapter extends XmlAdapter<String, java.sql.Date> {

	private SimpleDateFormat wFormat = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	public String marshal(Date v) {
		return wFormat.format(v);
	}

	@Override
	public Date unmarshal(String v) {
		try {
			return new Date(wFormat.parse(v).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

}
