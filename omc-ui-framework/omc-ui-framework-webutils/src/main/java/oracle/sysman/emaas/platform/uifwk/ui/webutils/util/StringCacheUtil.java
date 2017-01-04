/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author aduan
 */
public class StringCacheUtil
{
	private static class CachedObj
	{
		private final long timeStamp;
		private final String obj;

		public CachedObj(String obj)
		{
			timeStamp = System.currentTimeMillis();
			this.obj = obj;
		}

		public String getObject()
		{
			return obj;
		}

		public long getTimestamp()
		{
			return timeStamp;
		}
	}

	private static Logger LOGGER = LogManager.getLogger(StringCacheUtil.class);

	private static final StringCacheUtil REG_INSTANCE = new StringCacheUtil();

	// each string cache will live till expiry time is reached
	private static final long EXPIRY_TIME = 180000L;

	// let's set the capacity for the cache. Note that an known issue is, expired cached object may be inside cache still
	private static final int CACHE_CAPACITY = 800;

	public static final StringCacheUtil getRegistrationCacheInstance()
	{
		return REG_INSTANCE;
	}

	private final LinkedHashMap<String, CachedObj> container = new LinkedHashMap<String, CachedObj>(CACHE_CAPACITY);

	private StringCacheUtil()
	{

	}

	public String get(String key)
	{
		if (StringUtil.isEmpty(key)) {
			LOGGER.error("Failed to get value for key as it is null or empty: {}", key);
			return null;
		}
		CachedObj co = container.get(key);
		if (co == null) {
			return null;
		}
		if (!isValidValue(co)) {
			container.remove(key);
			return null;
		}
		String value = co.getObject();
		return value;
	}

	public String put(String key, String value)
	{
		if (StringUtil.isEmpty(key)) {
			LOGGER.error("Failed to put value {} into cache, as specified key is empty", value == null ? null : value.toString());
			return value;
		}
		if (value == null || "".equals(value)) {
			LOGGER.warn("Did not put value into cache for key {}, as specified value is empty", key);
			return value;
		}
		CachedObj co = new CachedObj(value);
		container.put(key, co);
		return value;
	}

	public String remove(String key)
	{
		if (StringUtil.isEmpty(key)) {
			LOGGER.error("Failed to remove value for key as key is null or empty: {}", key);
			return null;
		}
		if (container.containsKey(key)) {
			CachedObj co = container.remove(key);
			if (isValidValue(co)) {
				return co.getObject();
			}
			else {
				return null;
			}
		}
		return null;
	}

	private boolean isValidValue(CachedObj co)
	{
		if (co == null) {
			return false;
		}
		long now = System.currentTimeMillis();
		if (co.getTimestamp() > now || System.currentTimeMillis() - co.getTimestamp() > EXPIRY_TIME) {
			LOGGER.debug("Invalid cached object: start time is too late, or it expires for timestamp: {}", co.getTimestamp());
			return false;
		}
		else {
			return true;
		}
	}
}
