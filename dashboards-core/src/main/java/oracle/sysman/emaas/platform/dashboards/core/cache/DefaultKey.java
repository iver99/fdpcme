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

import java.io.Serializable;
import java.util.Arrays;

import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;

/**
 * @author guochen
 */
@SuppressWarnings("serial")
public class DefaultKey implements Serializable
{
	private final Tenant tenant;
	private final Object[] params;
	private final int hashCode;

	/**
	 * Create a new {@link DefaultKey} instance.
	 *
	 * @param elements
	 *            the elements of the key
	 */
	public DefaultKey(Tenant tenant, Object... keys)
	{
		this.tenant = tenant;
		if (keys == null) {
			throw new IllegalArgumentException("Keys must not be null");
		}
		params = new Object[keys.length];
		System.arraycopy(keys, 0, params, 0, keys.length);
		hashCode = (tenant == null ? 0 : tenant.hashCode() * 31) + Arrays.deepHashCode(params);
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj || obj instanceof DefaultKey && (tenant == null ? ((DefaultKey) obj).tenant == null
				: tenant.equals(((DefaultKey) obj).tenant) && Arrays.deepEquals(params, ((DefaultKey) obj).params));
	}

	@Override
	public final int hashCode()
	{
		return hashCode;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [Tenant - " + tenant + ", Keys - " + StringUtil.arrayToCommaDelimitedString(params)
				+ "]";
	}
}
