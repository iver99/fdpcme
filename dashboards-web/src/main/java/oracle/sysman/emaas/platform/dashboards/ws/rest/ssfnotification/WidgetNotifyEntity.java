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

import java.util.Date;

/**
 * @author guochen
 */
public class WidgetNotifyEntity
{
	private Long uniqueId;
	private String name;
	private WidgetNotificationType type;
	private Date notifyTime;
	/**
	 * Used as a 'returned value' to indicate who many objects are impacted by this widget change notification
	 */
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
	
	public Date getNotifyTime()
	{
		return notifyTime;
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
	
	public void setNotifyTime(Date notifyTime)
	{
		this.notifyTime = notifyTime;
	}

	/**
	 * @param uniqueId
	 *            the uniqueId to set
	 */
	public void setUniqueId(Long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * @return the type
	 */
	public WidgetNotificationType getType()
	{
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(WidgetNotificationType type)
	{
		this.type = type;
	}
}
