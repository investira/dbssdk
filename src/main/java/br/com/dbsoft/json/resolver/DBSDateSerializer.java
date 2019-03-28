package br.com.dbsoft.json.resolver;

import java.io.IOException;
import java.sql.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import br.com.dbsoft.util.DBSFormat;

public class DBSDateSerializer extends StdSerializer<Date> {

	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale="pt-BR", timezone="Brazil/East")
	
	private static final long serialVersionUID = 5223129049367062286L;
	
	private static String FORMAT = "dd/MM/yyyy";

	public DBSDateSerializer() {
		this(null);
	}

	public DBSDateSerializer(Class<Date> pDate) {
		super(pDate);
	}

	@Override
	public void serialize(Date pValue, JsonGenerator pGen, SerializerProvider pArg2) throws IOException, JsonProcessingException {
		pGen.writeString(DBSFormat.getFormattedDateCustom(pValue, FORMAT));
	}

}
