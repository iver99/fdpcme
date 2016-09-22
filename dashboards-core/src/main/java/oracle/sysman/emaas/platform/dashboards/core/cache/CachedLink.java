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

import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil.VersionedLink;

/**
 * @author guochen
 */
public class CachedLink implements Serializable
{
	private static final long serialVersionUID = 6274467996821367632L;

	private final String href;
	private final String rel;
	private final String version;

	public CachedLink(VersionedLink link)
	{
		href = link.getHref();
		rel = link.getRel();
		version = link.getVersion();
	}

	/**
	 * @return the href
	 */
	public String getHref()
	{
		return href;
	}

	public VersionedLink getLink()
	{
		VersionedLink link = new VersionedLink();
		link.withHref(href);
		link.withRel(rel);
		link.setVersion(version);
		return link;
	}

	/**
	 * @return the rel
	 */
	public String getRel()
	{
		return rel;
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}
}
