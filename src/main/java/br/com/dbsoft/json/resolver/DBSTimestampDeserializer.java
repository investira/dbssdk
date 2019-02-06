package br.com.dbsoft.json.resolver;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import br.com.dbsoft.util.DBSDate;

public class DBSTimestampDeserializer extends StdDeserializer<Timestamp> {

	private static final long serialVersionUID = -5787166120363721661L;
	
	private static String FORMAT = "dd/MM/yyyy HH:mm:ss";

	public DBSTimestampDeserializer() {
		this(null);
	}

	public DBSTimestampDeserializer(Class<?> pData) {
		super(pData);
	}

	@Override
	public Timestamp deserialize(JsonParser pJSONParser, DeserializationContext pContext) throws IOException {
		Date xDate = DBSDate.toDateCustom(pJSONParser.getText(), FORMAT);
		return DBSDate.toTimestamp(xDate.getTime());
	}

}
