/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.webutils.json;

import java.util.List;

/**
 * @author guobaochen
 */
public class AppMappingEntity
{
	public static class AppMappingKey
	{
		private String name;
		private String value;

		public String getName()
		{
			return name;
		}

		public String getValue()
		{
			return value;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}

	public static class AppMappingValue
	{
		private String opcTenantId;
		private String applicationNames;

		public String getApplicationNames()
		{
			return applicationNames;
		}

		public String getOpcTenantId()
		{
			return opcTenantId;
		}

		public void setApplicationNames(String applicationNames)
		{
			this.applicationNames = applicationNames;
		}

		public void setOpcTenantId(String opcTenantId)
		{
			this.opcTenantId = opcTenantId;
		}
	}

	private String domainUuid;
	private String domainName;
	private String uuid;
	private String canonicalUrl;
	private List<AppMappingKey> keys;
	private List<AppMappingValue> values;
	private Long hash;

	public String getCanonicalUrl()
	{
		return canonicalUrl;
	}

	public String getDomainName()
	{
		return domainName;
	}

	public String getDomainUuid()
	{
		return domainUuid;
	}

	public Long getHash()
	{
		return hash;
	}

	public List<AppMappingKey> getKeys()
	{
		return keys;
	}

	public String getUuid()
	{
		return uuid;
	}

	public List<AppMappingValue> getValues()
	{
		return values;
	}

	public void setCanonicalUrl(String canonicalUrl)
	{
		this.canonicalUrl = canonicalUrl;
	}

	public void setDomainName(String domainName)
	{
		this.domainName = domainName;
	}

	public void setDomainUuid(String domainUuid)
	{
		this.domainUuid = domainUuid;
	}

	public void setHash(Long hash)
	{
		this.hash = hash;
	}

	public void setKeys(List<AppMappingKey> keys)
	{
		this.keys = keys;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public void setValues(List<AppMappingValue> values)
	{
		this.values = values;
	}

}
