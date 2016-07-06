/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

import java.io.IOException;
import java.math.BigInteger;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * @author wenjzhu
 */
public class BigIntegerSerializer extends JsonSerializer<BigInteger>
{

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(BigInteger value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
	JsonProcessingException
	{
		if (value != null) {
			jgen.writeString(value.toString());//.writeStringField("id", value.toString());
		}
	}

}
