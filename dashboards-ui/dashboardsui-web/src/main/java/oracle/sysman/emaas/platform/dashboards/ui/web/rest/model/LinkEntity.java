/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web.rest.model;

/**
 * @author aduan
 */
public class LinkEntity
{
	private String name;
	private String href;

	public LinkEntity(String name, String href)
	{
		this.name = name;
		this.href = href;
	}

	/**
	 * @return the href
	 */
	public String getHref()
	{
		return href;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param href
	 *            the href to set
	 */
	public void setHref(String href)
	{
		this.href = href;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
