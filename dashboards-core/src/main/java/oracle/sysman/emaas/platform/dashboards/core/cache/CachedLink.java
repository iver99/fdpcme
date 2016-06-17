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

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;

/**
 * @author guochen
 */
public class CachedLink implements Serializable
{
	private static final long serialVersionUID = 6274467996821367632L;

	private final String href;
	private final String rel;

	public CachedLink(Link link)
	{
		href = link.getHref();
		rel = link.getRel();
	}

	/**
	 * @return the href
	 */
	public String getHref()
	{
		return href;
	}

	public Link getLink()
	{
		Link link = new Link();
		link.withHref(href);
		link.withRel(rel);
		return link;
	}

	/**
	 * @return the rel
	 */
	public String getRel()
	{
		return rel;
	}
}
