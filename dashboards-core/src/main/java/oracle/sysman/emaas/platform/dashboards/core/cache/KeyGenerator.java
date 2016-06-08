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
public interface KeyGenerator
{
	/**
	 * Generate a key for the given tenant and key list.
	 *
	 * @param tenant
	 *            the tenant
	 * @param params
	 *            the key list
	 * @return a generated key
	 */
	Object generate(Tenant tenant, Keys keys);
}
