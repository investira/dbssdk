/**
 * 
 */
package br.com.dbsoft.xmladapter;

import java.sql.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Avila
 *
 */
public class DateSQLToDateUtilAdapter extends XmlAdapter<java.util.Date, java.sql.Date> {
	@Override
	public java.util.Date marshal(Date v) {
		return new Date(v.getTime());
	}

	@Override
	public Date unmarshal(java.util.Date v) {
		return new Date(v.getTime());
	}
}
