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
public class DefaultKeyGenerator implements KeyGenerator
{
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.core.cache.KeyGenerator#generate(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Object generate(Tenant tenant, Keys keys)
	{
		return new DefaultKey(tenant, keys == null ? null : keys.getKeys());
	}
}
