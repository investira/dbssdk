package br.com.dbsoft.json.resolver;

import java.io.IOException;
import java.sql.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import br.com.dbsoft.util.DBSDate;

public class DBSDateDeserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = -6969701304763364210L;
	
	private static String FORMAT = "dd/MM/yyyy hh:mm:ss";

	public DBSDateDeserializer() {
		this(null);
	}

	public DBSDateDeserializer(Class<?> pData) {
		super(pData);
	}

	@Override
	public Date deserialize(JsonParser pJSONParser, DeserializationContext pContext) throws IOException {
		return DBSDate.toDateCustom(pJSONParser.getText(), FORMAT);
	}

}
