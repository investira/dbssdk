package br.com.dbsoft.json.resolver;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import br.com.dbsoft.util.DBSDate;

public class DBSDateWithTimeDeserializerNew extends StdDeserializer<Date> {

	private static final long serialVersionUID = -2302982612223014996L;
	
	SimpleDateFormat wFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public DBSDateWithTimeDeserializerNew() {
		this(null);
	}

	public DBSDateWithTimeDeserializerNew(Class<?> pData) {
		super(pData);
	}

	@Override
	public Date deserialize(JsonParser pJSONParser, DeserializationContext pContext) {
		String xJsonData = "";
		Date xData = null;
		try {
			xJsonData = pJSONParser.getText();
			xData = DBSDate.toDate(wFormat.parse(xJsonData));
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return xData;
	}

}
