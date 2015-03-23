/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Class contains converters for json data deserialization
 *
 * @author guobaochen
 */
public class JsonConverters
{
	/**
	 * Converter to deserialize <code>AppMappingCollection</code>
	 *
	 * @author guobaochen
	 */
	public static class AppMappingCollectionConverter extends AbstractJsonConverter<AppMappingCollection>
	{
	}

	/**
	 * Converter to deserialize <code>DomainsEntity</code>
	 *
	 * @author guobaochen
	 */
	public static class DomainsConverter extends AbstractJsonConverter<DomainsEntity>
	{
	}

	private static abstract class AbstractJsonConverter<T>
	{
		private final ObjectMapper mapper;
		private final Class<T> clazz;

		@SuppressWarnings("unchecked")
		public AbstractJsonConverter()
		{
			mapper = new ObjectMapper();
			Type genType = getClass().getGenericSuperclass();
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			clazz = (Class<T>) params[0];
		}

		/**
		 * Converts json response inside specified json string into specified simple type, and returns the instance
		 *
		 * @param content
		 * @return
		 * @throws IOException
		 * @throws UnsupportedEncodingException
		 * @throws JsonMappingException
		 * @throws JsonParseException
		 */
		public T getRawObject(final String content) throws JsonParseException, JsonMappingException, IOException
		{
			return mapper.readValue(content, clazz);
		}

		/**
		 * Converts json response inside specified json string into specified list, and returns the instance
		 *
		 * @param content
		 * @return
		 * @throws JsonParseException
		 * @throws JsonMappingException
		 * @throws UnsupportedEncodingException
		 * @throws IOException
		 */
		public List<T> getRawObjectList(final String content) throws JsonParseException, JsonMappingException,
				UnsupportedEncodingException, IOException
		{
			TypeFactory tf = mapper.getTypeFactory();
			@SuppressWarnings("rawtypes")
			Class<List> clazzList = List.class;
			JavaType paginatedType = tf.constructParametrizedType(clazzList, clazzList, clazz);
			return mapper.readValue(content, paginatedType);
		}

	}

	public static void main(String args[]) throws JsonParseException, JsonMappingException, IOException
	{
		String test = "{"
				+ "\"total\": 10,"
				+ "\"items\": ["
				+ "{"
				+ "\"uuid\": \"925b20f9-9b8f-43f9-acc4-fed7987680ff\","
				+ "\"domainName\": \"ITADatabaseTenantMapping\","
				+ "\"canonicalUrl\": \"http://sca00bym.us.oracle.com:7001/naming/entitynaming/v1/domains/925b20f9-9b8f-43f9-acc4-fed7987680ff\","
				+ "\"keys\": ["
				+ "{"
				+ "\"name\": \"tenantid\""
				+ "}"
				+ "]"
				+ "},"
				+ "{"
				+ "\"uuid\": \"a249bb70-4a6c-404a-a420-53cd7b299d67\","
				+ "\"domainName\": \"TenantSchemaMapping\","
				+ "\"canonicalUrl\": \"http://sca00bym.us.oracle.com:7001/naming/entitynaming/v1/domains/a249bb70-4a6c-404a-a420-53cd7b299d67\","
				+ "\"keys\": [" + "{" + "\"name\": \"tenantID\"" + "}," + "{" + "\"name\": \"serviceName\"" + "}," + "{"
				+ "\"name\": \"serviceVersion\"" + "}" + "]" + "}" + " ]," + "\"count\": 2" + "}";
		DomainsConverter dc = new DomainsConverter();
		DomainsEntity dms = dc.getRawObject(test);
		System.out.println(dms.getTotal());
	}

}
