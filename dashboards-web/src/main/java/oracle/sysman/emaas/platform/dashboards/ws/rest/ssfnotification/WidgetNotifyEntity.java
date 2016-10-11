/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification;

/**
 * @author guochen
 */
public class WidgetNotifyEntity
{
	private Long uniqueId;
	private String name;
	private Integer affactedObjects;

	public WidgetNotifyEntity()
	{
	}

	/**
	 * @return the affactedObjects
	 */
	public Integer getAffactedObjects()
	{
		return affactedObjects;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the uniqueId
	 */
	public Long getUniqueId()
	{
		return uniqueId;
	}

	/**
	 * @param affactedObjects
	 *            the affactedObjects to set
	 */
	public void setAffactedObjects(Integer affactedObjects)
	{
		this.affactedObjects = affactedObjects;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param uniqueId
	 *            the uniqueId to set
	 */
	public void setUniqueId(Long uniqueId)
	{
		this.uniqueId = uniqueId;
	}
}
