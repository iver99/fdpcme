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
public class TenantEditionEntity
{
	private String application;
	private String edition;

	public TenantEditionEntity()
	{
	}

	public TenantEditionEntity(String application, String edition)
	{
		this.application = application;
		this.edition = edition;
	}

	public String getApplication()
	{
		return application;
	}

	public String getEdition()
	{
		return edition;
	}

	public void setApplication(String application)
	{
		this.application = application;
	}

	public void setEdition(String edition)
	{
		this.edition = edition;
	}
}
