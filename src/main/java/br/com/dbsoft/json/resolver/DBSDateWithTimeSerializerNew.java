package br.com.dbsoft.json.resolver;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DBSDateWithTimeSerializerNew extends StdSerializer<Date> {

	private static final long serialVersionUID = 574506355705668606L;
	
	SimpleDateFormat wFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public DBSDateWithTimeSerializerNew() {
		this(null);
	}

	public DBSDateWithTimeSerializerNew(Class<Date> pDateTime) {
		super(pDateTime);
	}

	@Override
	public void serialize(Date pValue, JsonGenerator pGen, SerializerProvider pArg2) throws IOException, JsonProcessingException {
		pGen.writeString(wFormat.format(pValue));
	}

}
