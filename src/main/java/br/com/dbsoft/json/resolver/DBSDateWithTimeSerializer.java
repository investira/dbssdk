package br.com.dbsoft.json.resolver;

import java.io.IOException;
import java.sql.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import br.com.dbsoft.util.DBSFormat;

public class DBSDateWithTimeSerializer extends StdSerializer<Date> {

	private static final long serialVersionUID = -6244613685169545926L;

	private static String FORMAT = "dd/MM/yyyy HH:mm:ss";

	public DBSDateWithTimeSerializer() {
		this(null);
	}

	public DBSDateWithTimeSerializer(Class<Date> pDateTime) {
		super(pDateTime);
	}

	@Override
	public void serialize(Date pValue, JsonGenerator pGen, SerializerProvider pArg2) throws IOException, JsonProcessingException {
		pGen.writeString(DBSFormat.getFormattedDateCustom(pValue, FORMAT));
	}

}
