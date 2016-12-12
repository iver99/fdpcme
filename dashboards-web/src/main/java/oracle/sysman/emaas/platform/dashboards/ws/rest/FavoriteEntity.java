/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.math.BigInteger;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.util.BigIntegerSerializer;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author wenjzhu
 */
public class FavoriteEntity
{
	@JsonSerialize(using = BigIntegerSerializer.class)
	BigInteger id;
	String name;
	String href;

	public FavoriteEntity()
	{
		super();
	}

	public FavoriteEntity(Dashboard d)
	{
		id = d.getDashboardId();
		name = d.getName();
		href = d.getHref();
	}

	/**
	 * @return the href
	 */
	public String getHref()
	{
		return href;
	}

	/**
	 * @return the id
	 */
	public BigInteger getId()
	{
		return id;
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
	 * @param id
	 *            the id to set
	 */
	public void setId(BigInteger id)
	{
		this.id = id;
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
