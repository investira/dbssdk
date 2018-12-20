package br.com.dbsoft.json.resolver;

import java.io.IOException;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import br.com.dbsoft.util.DBSFormat;

public class DBSDateTimeSerializer extends StdSerializer<DateTime> {

	private static final long serialVersionUID = -6244613685169545926L;

	private static String FORMAT = "dd/MM/yyyy hh:mm:ss";

	public DBSDateTimeSerializer() {
		this(null);
	}

	public DBSDateTimeSerializer(Class<DateTime> pDateTime) {
		super(pDateTime);
	}

	@Override
	public void serialize(DateTime pValue, JsonGenerator pGen, SerializerProvider pArg2) throws IOException, JsonProcessingException {
		pGen.writeString(DBSFormat.getFormattedDateCustom(pValue, FORMAT));
	}

}
