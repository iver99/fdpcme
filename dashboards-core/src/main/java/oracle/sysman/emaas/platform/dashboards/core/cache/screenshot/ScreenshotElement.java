/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.cache.screenshot;

import com.oracle.platform.emaas.cache.Binary;

import java.io.Serializable;






/**
 * @author guochen
 */
public class ScreenshotElement implements Serializable
{
	private static final long serialVersionUID = 1989708606319417448L;

	private String fileName;
	private Binary buffer;

	public ScreenshotElement(String fileName, Binary buffer)
	{
		this.fileName = fileName;
		this.buffer = buffer;
	}

	/**
	 * @return the buffer
	 */
	public Binary getBuffer()
	{
		return buffer;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param buffer
	 *            the buffer to set
	 */
	public void setBuffer(Binary buffer)
	{
		this.buffer = buffer;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
}
