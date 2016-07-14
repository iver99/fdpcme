/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author guochen
 */
public class DashboardFavoriteRowEntity
{
	@JsonProperty("USER_NAME")
	private String USER_NAME;

	@JsonProperty("DASHBOARD_ID")
	private Long DASHBOARD_ID;

	@JsonProperty("CREATION_DATE")
	private String CREATION_DATE;

	@JsonProperty("TENANT_ID")
	private Long TENANT_ID;

	/**
	 * @return the cREATION_DATE
	 */
	public String getCREATION_DATE()
	{
		return CREATION_DATE;
	}

	/**
	 * @return the dASHBOARD_ID
	 */
	public Long getDASHBOARD_ID()
	{
		return DASHBOARD_ID;
	}

	/**
	 * @return the tENANT_ID
	 */
	public Long getTENANT_ID()
	{
		return TENANT_ID;
	}

	/**
	 * @return the uSER_NAME
	 */
	public String getUSER_NAME()
	{
		return USER_NAME;
	}

	/**
	 * @param cREATION_DATE
	 *            the cREATION_DATE to set
	 */
	public void setCREATION_DATE(String cREATION_DATE)
	{
		CREATION_DATE = cREATION_DATE;
	}

	/**
	 * @param dASHBOARD_ID
	 *            the dASHBOARD_ID to set
	 */
	public void setDASHBOARD_ID(Long dASHBOARD_ID)
	{
		DASHBOARD_ID = dASHBOARD_ID;
	}

	/**
	 * @param tENANT_ID
	 *            the tENANT_ID to set
	 */
	public void setTENANT_ID(Long tENANT_ID)
	{
		TENANT_ID = tENANT_ID;
	}

	/**
	 * @param uSER_NAME
	 *            the uSER_NAME to set
	 */
	public void setUSER_NAME(String uSER_NAME)
	{
		USER_NAME = uSER_NAME;
	}
}
