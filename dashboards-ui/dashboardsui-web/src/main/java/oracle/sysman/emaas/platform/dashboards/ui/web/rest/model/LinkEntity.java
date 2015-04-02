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
	private String serviceName;
	private String version;

	public LinkEntity(String name, String href, String serviceName, String version)
	{
		this.name = name;
		this.href = href;
		this.serviceName = serviceName;
		this.version = version;
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
	 * @return the serviceName
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * @return the serviceVersion
	 */
	public String getVersion()
	{
		return version;
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

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	/**
	 * @param serviceVersion
	 *            the serviceVersion to set
	 */
	public void setVersion(String serviceVersion)
	{
		version = serviceVersion;
	}
}
