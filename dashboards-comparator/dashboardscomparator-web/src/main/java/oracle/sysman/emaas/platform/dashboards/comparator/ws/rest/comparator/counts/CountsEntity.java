/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts;

/**
 * @author guochen
 */
public class CountsEntity
{
	private Long countOfDashboards;
	private Long countOfFavorite;
	private Long countOfPreference;

	public CountsEntity()
	{
	}

	public CountsEntity(Long countOfDashboards, Long countOfFavorite, Long countOfPreference)
	{
		this.countOfDashboards = countOfDashboards;
		this.countOfFavorite = countOfFavorite;
		this.countOfPreference = countOfPreference;
	}

	/**
	 * @return the countOfDashboards
	 */
	public Long getCountOfDashboards()
	{
		return countOfDashboards;
	}

	/**
	 * @return the countOfFavorite
	 */
	public Long getCountOfFavorite()
	{
		return countOfFavorite;
	}

	/**
	 * @return the countOfPreference
	 */
	public Long getCountOfPreference()
	{
		return countOfPreference;
	}

	/**
	 * @param countOfDashboards
	 *            the countOfDashboards to set
	 */
	public void setCountOfDashboards(Long countOfDashboards)
	{
		this.countOfDashboards = countOfDashboards;
	}

	/**
	 * @param countOfFavorite
	 *            the countOfFavorite to set
	 */
	public void setCountOfFavorite(Long countOfFavorite)
	{
		this.countOfFavorite = countOfFavorite;
	}

	/**
	 * @param countOfPreference
	 *            the countOfPreference to set
	 */
	public void setCountOfPreference(Long countOfPreference)
	{
		this.countOfPreference = countOfPreference;
	}
}
