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
public class DashboardLastAccessRowEntity
{
	@JsonProperty("DASHBOARD_ID")
	private Long DASHBOARD_ID;

	@JsonProperty("ACCESSED_BY")
	private String ACCESSED_BY;

	@JsonProperty("ACCESS_DATE")
	private String ACCESS_DATE;

	@JsonProperty("TENANT_ID")
	private Long TENANT_ID;

	/**
	 * @return the aCCESS_DATE
	 */
	public String getACCESS_DATE()
	{
		return ACCESS_DATE;
	}

	/**
	 * @return the aCCESSED_BY
	 */
	public String getACCESSED_BY()
	{
		return ACCESSED_BY;
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
	 * @param aCCESS_DATE
	 *            the aCCESS_DATE to set
	 */
	public void setACCESS_DATE(String aCCESS_DATE)
	{
		ACCESS_DATE = aCCESS_DATE;
	}

	/**
	 * @param aCCESSED_BY
	 *            the aCCESSED_BY to set
	 */
	public void setACCESSED_BY(String aCCESSED_BY)
	{
		ACCESSED_BY = aCCESSED_BY;
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
}
