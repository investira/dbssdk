/**
 * 
 */
package br.com.dbsoft.xmladapter;

import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSFormat;

/**
 * @author jose.avila@dbsoft.com.br
 *
 */
public class StringToTimestampSQLAdapter extends XmlAdapter<String, Timestamp> {

	@Override
	public String marshal(Timestamp pValue) {
		return DBSFormat.getFormattedDateTime(pValue);
	}

	@Override
	public Timestamp unmarshal(String pValue) {
		return DBSDate.toTimestamp(pValue);
	}

}
