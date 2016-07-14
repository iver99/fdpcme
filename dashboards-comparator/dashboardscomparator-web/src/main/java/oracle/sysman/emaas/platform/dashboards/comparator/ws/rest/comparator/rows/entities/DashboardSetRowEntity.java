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
public class DashboardSetRowEntity
{
	@JsonProperty("DASHBOARD_SET_ID")
	private Long DASHBOARD_SET_ID;

	@JsonProperty("TENANT_ID")
	private Long TENANT_ID;

	@JsonProperty("SUB_DASHBOARD_ID")
	private Long SUB_DASHBOARD_ID;

	@JsonProperty("POSITION")
	private Long POSITION;

	/**
	 * @return the dASHBOARD_SET_ID
	 */
	public Long getDASHBOARD_SET_ID()
	{
		return DASHBOARD_SET_ID;
	}

	/**
	 * @return the pOSITION
	 */
	public Long getPOSITION()
	{
		return POSITION;
	}

	/**
	 * @return the sUB_DASHBOARD_ID
	 */
	public Long getSUB_DASHBOARD_ID()
	{
		return SUB_DASHBOARD_ID;
	}

	/**
	 * @return the tENANT_ID
	 */
	public Long getTENANT_ID()
	{
		return TENANT_ID;
	}

	/**
	 * @param dASHBOARD_SET_ID
	 *            the dASHBOARD_SET_ID to set
	 */
	public void setDASHBOARD_SET_ID(Long dASHBOARD_SET_ID)
	{
		DASHBOARD_SET_ID = dASHBOARD_SET_ID;
	}

	/**
	 * @param pOSITION
	 *            the pOSITION to set
	 */
	public void setPOSITION(Long pOSITION)
	{
		POSITION = pOSITION;
	}

	/**
	 * @param sUB_DASHBOARD_ID
	 *            the sUB_DASHBOARD_ID to set
	 */
	public void setSUB_DASHBOARD_ID(Long sUB_DASHBOARD_ID)
	{
		SUB_DASHBOARD_ID = sUB_DASHBOARD_ID;
	}

	/**
	 * @param tENANT_ID
	 *            the tENANT_ID to set
	 */
	public void setTENANT_ID(Long tENANT_ID)
	{
		TENANT_ID = tENANT_ID;
	}
}
