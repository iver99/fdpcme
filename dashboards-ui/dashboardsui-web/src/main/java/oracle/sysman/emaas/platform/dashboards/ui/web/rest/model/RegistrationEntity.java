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
 * @author miao
 */
public class RegistrationEntity
{
	public static final String NAME_REGISTRYUTILS = "registryUrls";
	public static final String NAME_SSF_SERVICENAME = "ssfServiceName";
	public static final String NAME_SSF_VERSION = "ssfVersion";
	private String registryUrls;
	private String ssfServiceName;
	private String ssfVersion;

	public RegistrationEntity(String regValue, String ssfServiceName, String ssfVersion)
	{
		setRegistryUrls(regValue);
		setSsfServiceName(ssfServiceName);
		setSsfVersion(ssfVersion);
	}

	/**
	 * @return the registryUrls
	 */
	public String getRegistryUrls()
	{
		return registryUrls;
	}

	/**
	 * @return the ssfServiceName
	 */
	public String getSsfServiceName()
	{
		return ssfServiceName;
	}

	/**
	 * @return the ssfVersion
	 */
	public String getSsfVersion()
	{
		return ssfVersion;
	}

	/**
	 * @param registryUrls
	 *            the registryUrls to set
	 */
	public void setRegistryUrls(String registryUrls)
	{
		this.registryUrls = registryUrls;
	}

	/**
	 * @param ssfServiceName
	 *            the ssfServiceName to set
	 */
	public void setSsfServiceName(String ssfServiceName)
	{
		this.ssfServiceName = ssfServiceName;
	}

	/**
	 * @param ssfVersion
	 *            the ssfVersion to set
	 */
	public void setSsfVersion(String ssfVersion)
	{
		this.ssfVersion = ssfVersion;
	}
}
