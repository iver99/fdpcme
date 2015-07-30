/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition;

/**
 * @author guobaochen
 */
public class ServiceEntity
{
	private String serviceName;
	private String serviceType;
	private String edition;
	private String editionUUID;
	private String status;
	private String serviceId;
	private String canonicalLink;

	public String getCanonicalLink()
	{
		return canonicalLink;
	}

	public String getEdition()
	{
		return edition;
	}

	public String getEditionUUID()
	{
		return editionUUID;
	}

	public String getServiceId()
	{
		return serviceId;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public String getServiceType()
	{
		return serviceType;
	}

	public String getStatus()
	{
		return status;
	}

	public void setCanonicalLink(String canonicalLink)
	{
		this.canonicalLink = canonicalLink;
	}

	public void setEdition(String edition)
	{
		this.edition = edition;
	}

	public void setEditionUUID(String editionUUID)
	{
		this.editionUUID = editionUUID;
	}

	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public void setServiceType(String serviceType)
	{
		this.serviceType = serviceType;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
}
