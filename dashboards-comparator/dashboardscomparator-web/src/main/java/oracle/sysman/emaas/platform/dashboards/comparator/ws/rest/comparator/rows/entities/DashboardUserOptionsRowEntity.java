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
public class DashboardUserOptionsRowEntity
{
	@JsonProperty("USER_NAME")
	private String USER_NAME;

	@JsonProperty("TENANT_ID")
	private Long TENANT_ID;

	@JsonProperty("DASHBOARD_ID")
	private Long DASHBOARD_ID;

	@JsonProperty("AUTO_REFRESH_INTERVAL")
	private Long AUTO_REFRESH_INTERVAL;

	@JsonProperty("ACCESS_DATE")
	private String ACCESS_DATE;

	@JsonProperty("IS_FAVORITE")
	private Integer IS_FAVORITE;

	@JsonProperty("EXTENDED_OPTIONS")
	private String EXTENDED_OPTIONS;

	/**
	 * @return the aCCESS_DATE
	 */
	public String getACCESS_DATE()
	{
		return ACCESS_DATE;
	}

	/**
	 * @return the aUTO_REFRESH_INTERVAL
	 */
	public Long getAUTO_REFRESH_INTERVAL()
	{
		return AUTO_REFRESH_INTERVAL;
	}

	/**
	 * @return the dASHBOARD_ID
	 */
	public Long getDASHBOARD_ID()
	{
		return DASHBOARD_ID;
	}

	/**
	 * @return the eXTENDED_OPTIONS
	 */
	public String getEXTENDED_OPTIONS()
	{
		return EXTENDED_OPTIONS;
	}

	/**
	 * @return the iS_FAVORITE
	 */
	public Integer getIS_FAVORITE()
	{
		return IS_FAVORITE;
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
	 * @param aCCESS_DATE
	 *            the aCCESS_DATE to set
	 */
	public void setACCESS_DATE(String aCCESS_DATE)
	{
		ACCESS_DATE = aCCESS_DATE;
	}

	/**
	 * @param aUTO_REFRESH_INTERVAL
	 *            the aUTO_REFRESH_INTERVAL to set
	 */
	public void setAUTO_REFRESH_INTERVAL(Long aUTO_REFRESH_INTERVAL)
	{
		AUTO_REFRESH_INTERVAL = aUTO_REFRESH_INTERVAL;
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
	 * @param eXTENDED_OPTIONS
	 *            the eXTENDED_OPTIONS to set
	 */
	public void setEXTENDED_OPTIONS(String eXTENDED_OPTIONS)
	{
		EXTENDED_OPTIONS = eXTENDED_OPTIONS;
	}

	/**
	 * @param iS_FAVORITE
	 *            the iS_FAVORITE to set
	 */
	public void setIS_FAVORITE(Integer iS_FAVORITE)
	{
		IS_FAVORITE = iS_FAVORITE;
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
