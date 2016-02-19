/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.cache;

/**
 * @author guochen
 */
public interface ICacheFetchFactory
{
	/**
	 * Fetch cache data for the given cache key. Note that this method must be thread safe.
	 *
	 * @param key
	 * @return The entry, or null if it does not exist.
	 * @throws Exception
	 *             On failure creating the object.
	 */
	Object fetchCachable(Object key) throws Exception;
}
