package br.com.dbsoft.json.resolver;

import java.io.IOException;
import java.sql.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import br.com.dbsoft.util.DBSDate;

public class DBSDateWithTimeDeserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = -5787166120363721661L;
	
	private static String FORMAT = "dd/MM/yyyy HH:mm:ss";

	public DBSDateWithTimeDeserializer() {
		this(null);
	}

	public DBSDateWithTimeDeserializer(Class<?> pData) {
		super(pData);
	}

	@Override
	public Date deserialize(JsonParser pJSONParser, DeserializationContext pContext) throws IOException {
		Date xDate = DBSDate.toDateCustom(pJSONParser.getText(), FORMAT);
		return DBSDate.toDate(xDate.getTime());
	}

}
