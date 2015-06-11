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

import java.util.List;

/**
 * @author guobaochen
 */
public class TenantDetailEntity
{
	private String tenantId;
	private String name;
	private String status;
	private List<ServiceEntity> services;

	public String getName()
	{
		return name;
	}

	public List<ServiceEntity> getServices()
	{
		return services;
	}

	public String getStatus()
	{
		return status;
	}

	public String getTenantId()
	{
		return tenantId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setServices(List<ServiceEntity> services)
	{
		this.services = services;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public void setTenantId(String tenantId)
	{
		this.tenantId = tenantId;
	}
}
