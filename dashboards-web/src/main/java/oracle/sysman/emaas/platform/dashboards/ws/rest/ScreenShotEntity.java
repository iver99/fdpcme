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

/**
 * @author wenjzhu
 */
public class ScreenShotEntity
{
	private String href;
	private String screenShot;

	public ScreenShotEntity()
	{
		super();
	}

	public ScreenShotEntity(String href, String screenShot)
	{
		this.href = href;
		this.screenShot = screenShot;
	}

	/**
	 * @return the href
	 */
	public String getHref()
	{
		return href;
	}

	/**
	 * @return the screenShot
	 */
	public String getScreenShot()
	{
		return screenShot;
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
	 * @param screenShot
	 *            the screenShot to set
	 */
	public void setScreenShot(String screenShot)
	{
		this.screenShot = screenShot;
	}

}
