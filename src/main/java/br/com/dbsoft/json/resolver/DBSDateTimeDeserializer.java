package br.com.dbsoft.json.resolver;

import java.io.IOException;
import java.sql.Date;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import br.com.dbsoft.util.DBSDate;

public class DBSDateTimeDeserializer extends StdDeserializer<DateTime> {

	private static final long serialVersionUID = -5787166120363721661L;
	
	private static String FORMAT = "dd/MM/yyyy HH:mm:ss";

	public DBSDateTimeDeserializer() {
		this(null);
	}

	public DBSDateTimeDeserializer(Class<?> pData) {
		super(pData);
	}

	@Override
	public DateTime deserialize(JsonParser pJSONParser, DeserializationContext pContext) throws IOException {
		Date xDate = DBSDate.toDateCustom(pJSONParser.getText(), FORMAT);
		return DBSDate.toDateTime(xDate.getTime());
	}

}
