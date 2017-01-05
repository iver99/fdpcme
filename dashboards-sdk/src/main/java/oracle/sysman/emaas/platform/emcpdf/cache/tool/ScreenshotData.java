/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import java.util.Date;

/**
 * @author guochen
 */
public class ScreenshotData
{
	private String screenshot;
	private Date creationDate;
	private Date modificationDate;

	public ScreenshotData(String screenshot, Date creationDate, Date modificationDate)
	{
		this.screenshot = screenshot;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate()
	{
		return creationDate;
	}

	/**
	 * @return the modificationDate
	 */
	public Date getModificationDate()
	{
		return modificationDate;
	}

	/**
	 * @return the screenshot
	 */
	public String getScreenshot()
	{
		return screenshot;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
	 * @param modificationDate
	 *            the modificationDate to set
	 */
	public void setModificationDate(Date modificationDate)
	{
		this.modificationDate = modificationDate;
	}

	/**
	 * @param screenshot
	 *            the screenshot to set
	 */
	public void setScreenshot(String screenshot)
	{
		this.screenshot = screenshot;
	}
}
