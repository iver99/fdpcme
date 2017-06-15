/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.emcpdf.tenant.model;

import java.util.List;

/**
 * @author guobaochen
 */
public class DomainEntity
{
	public static class DomainKeyEntity
	{
		private String name;

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
	}

	private String uuid;
	private String domainName;
	private String canonicalUrl;
	private List<DomainKeyEntity> keys;

	public String getCanonicalUrl()
	{
		return canonicalUrl;
	}

	public String getDomainName()
	{
		return domainName;
	}

	public List<DomainKeyEntity> getKeys()
	{
		return keys;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setCanonicalUrl(String canonicalUrl)
	{
		this.canonicalUrl = canonicalUrl;
	}

	public void setDomainName(String domainName)
	{
		this.domainName = domainName;
	}

	public void setKeys(List<DomainKeyEntity> keys)
	{
		this.keys = keys;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}
}
