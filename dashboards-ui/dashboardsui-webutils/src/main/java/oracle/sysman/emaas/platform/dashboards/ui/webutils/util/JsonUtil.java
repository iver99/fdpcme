/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;

public class JsonUtil
{
	private static final Logger LOGGER = LogManager.getLogger(JsonUtil.class);

	/**
	 * only initial changed value
	 */
	public static JsonUtil buildNonDefaultMapper()
	{
		return new JsonUtil(Inclusion.NON_DEFAULT);
	}

	/**
	 * output not null
	 */
	public static JsonUtil buildNonNullMapper()
	{
		return new JsonUtil(Inclusion.NON_NULL);
	}

	/**
	 * output all
	 */
	public static JsonUtil buildNormalMapper()
	{
		return new JsonUtil(Inclusion.ALWAYS);
	}

	private final ObjectMapper mapper;

	//	private static String dateFormat = "yyyy-MM-dd hh:mm:ss";

	public JsonUtil(Inclusion inclusion)
	{
		mapper = new ObjectMapper();
		//set inclusion attribute
		mapper.setSerializationInclusion(inclusion);
		//ignore those exist in json but not in java
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//forbid the deserialize int for Enum order()
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
	}

	/**
	 * For Generic Type, like List<MyBean>, call constructParametricType(ArrayList.class,MyBean.class) Map<String,MyBean> call
	 * (HashMap.class,String.class, MyBean.class)
	 */
	public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses)
	{
		return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
	}

	@SuppressWarnings("unchecked")
	public <T> T fromJson(JsonNode node, Class<?> parametrized, Class<?>... parameterClasses) throws IOException
	{
		JavaType javaType = constructParametricType(parametrized, parameterClasses);
		return (T) mapper.readValue(node, javaType);
	}

	@SuppressWarnings("unchecked")
	public <T> T fromJson(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) throws IOException
	{

		return (T) this.fromJson(jsonString, constructParametricType(parametrized, parameterClasses));
	}

	/**
	 * if JSON string is Null or "null"String , return Null. if JSON string is "[]", return empty collection. if read List/Map,
	 * use constructParametricTypeto construct type first.
	 *
	 * @see #constructParametricType(Class, Class...)
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz) throws IOException
	{
		if (isEmptyString(jsonString)) {
			return null;
		}

		return mapper.readValue(jsonString, clazz);
	}

	/**
	 * if JSON string is Null or "null"String , return Null. if JSON string is "[]", return empty collection. if read List/Map,
	 * use constructParametricTypeto construct type first.
	 *
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @see #constructParametricType(Class, Class...)
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromJson(String jsonString, JavaType javaType) throws IOException
	{
		if (isEmptyString(jsonString)) {
			return null;
		}
		jsonString = URLDecoder.decode(jsonString, "UTF-8");
		return (T) mapper.readValue(jsonString, javaType);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fromJsonToList(String jsonString, Class<T> classMeta) throws IOException
	{

		return (List<T>) this.fromJson(jsonString, constructParametricType(List.class, classMeta));
	}

	/**
	 * Get Mapper
	 */
	public ObjectMapper getMapper()
	{
		return mapper;
	}

	public boolean isEmptyString(String str)
	{
		return str == null || "".equals(str.toString());
	}

	public JsonNode parseNode(String json) throws IOException
	{
		return mapper.readValue(json, JsonNode.class);
	}

	public void setDateFormat(String dateFormat)
	{
		mapper.setDateFormat(new SimpleDateFormat(dateFormat, Locale.US));
	}

	/**
	 * set if use Enum toString to read Enum, if False then use Enum name() to read Enum, default is False. Call after Mapper been
	 * created.
	 */
	public void setEnumUseToString(boolean value)
	{
		mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, value);
		mapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, value);
	}

	/**
	 * if object is null , return null if object is null collection, return "[]"
	 */
	public String toJson(Object object)
	{
		try {
			return mapper.writeValueAsString(object);
		}
		catch (IOException e) {
			LOGGER.error("an error occureed while getting Json string", e);
			return null;
		}
	}

}