/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt;

/**
 * @author guochen
 */
public class ZDTEntity
{
	private long countOfDashboards;
	private long countOfFavorite;
	private long countOfPreference;

	public ZDTEntity()
	{
	}

	public ZDTEntity(long countOfDashboards, long countOfFavorite, long countOfPreference)
	{
		this.countOfDashboards = countOfDashboards;
		this.countOfFavorite = countOfFavorite;
		this.countOfPreference = countOfPreference;
	}

	/**
	 * @return the countOfDashboards
	 */
	public long getCountOfDashboards()
	{
		return countOfDashboards;
	}

	/**
	 * @return the countOfFavorite
	 */
	public long getCountOfFavorite()
	{
		return countOfFavorite;
	}

	/**
	 * @return the countOfPreference
	 */
	public long getCountOfPreference()
	{
		return countOfPreference;
	}

	/**
	 * @param countOfDashboards
	 *            the countOfDashboards to set
	 */
	public void setCountOfDashboards(long countOfDashboards)
	{
		this.countOfDashboards = countOfDashboards;
	}

	/**
	 * @param countOfFavorite
	 *            the countOfFavorite to set
	 */
	public void setCountOfFavorite(long countOfFavorite)
	{
		this.countOfFavorite = countOfFavorite;
	}

	/**
	 * @param countOfPreference
	 *            the countOfPreference to set
	 */
	public void setCountOfPreference(long countOfPreference)
	{
		this.countOfPreference = countOfPreference;
	}
}
