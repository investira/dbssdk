/**
 * 
 */
package br.com.dbsoft.xmladapter;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Avila
 * 
 */
public class TimestampToDateUtilAdapter extends XmlAdapter<java.util.Date, Timestamp> {
	@Override
	public Date marshal(Timestamp v) {
		return new Date(v.getTime());
	}

	@Override
	public Timestamp unmarshal(java.util.Date v) {
		return new Timestamp(v.getTime());
	}
}
