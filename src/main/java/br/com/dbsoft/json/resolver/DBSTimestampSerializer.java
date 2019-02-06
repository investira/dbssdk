package br.com.dbsoft.json.resolver;

import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import br.com.dbsoft.util.DBSFormat;

public class DBSTimestampSerializer extends StdSerializer<Timestamp> {

	private static final long serialVersionUID = -6244613685169545926L;

	private static String FORMAT = "dd/MM/yyyy HH:mm:ss";

	public DBSTimestampSerializer() {
		this(null);
	}

	public DBSTimestampSerializer(Class<Timestamp> pDateTime) {
		super(pDateTime);
	}

	@Override
	public void serialize(Timestamp pValue, JsonGenerator pGen, SerializerProvider pArg2) throws IOException, JsonProcessingException {
		pGen.writeString(DBSFormat.getFormattedDateCustom(pValue, FORMAT));
	}

}
