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

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author guochen
 */
public class TableRowsEntity
{
	@JsonProperty("EMS_DASHBOARD")
	private List<DashboardRowEntity> EMS_DASHBOARD;

	@JsonProperty("EMS_DASHBOARD_FAVORITE")
	private List<DashboardFavoriteRowEntity> EMS_DASHBOARD_FAVORITE;

	@JsonProperty("EMS_DASHBOARD_LAST_ACCESS")
	private List<DashboardLastAccessRowEntity> EMS_DASHBOARD_LAST_ACCESS;

	@JsonProperty("EMS_DASHBOARD_SET")
	private List<DashboardSetRowEntity> EMS_DASHBOARD_SET;

	@JsonProperty("EMS_DASHBOARD_TILE")
	private List<DashboardTileRowEntity> EMS_DASHBOARD_TILE;

	@JsonProperty("EMS_DASHBOARD_TILE_PARAMS")
	private List<DashboardTileParamsRowEntity> EMS_DASHBOARD_TILE_PARAMS;

	@JsonProperty("EMS_DASHBOARD_USER_OPTIONS")
	private List<DashboardUserOptionsRowEntity> EMS_DASHBOARD_USER_OPTIONS;

	@JsonProperty("EMS_PREFERENCE")
	private List<PreferenceRowEntity> EMS_PREFERENCE;

	public TableRowsEntity()
	{
	}

	/**
	 * @return the eMS_DASHBOARD
	 */
	public List<DashboardRowEntity> getEMS_DASHBOARD()
	{
		return EMS_DASHBOARD;
	}

	/**
	 * @return the eMS_DASHBOARD_FAVORITE
	 */
	public List<DashboardFavoriteRowEntity> getEMS_DASHBOARD_FAVORITE()
	{
		return EMS_DASHBOARD_FAVORITE;
	}

	/**
	 * @return the eMS_DASHBOARD_LAST_ACCESS
	 */
	public List<DashboardLastAccessRowEntity> getEMS_DASHBOARD_LAST_ACCESS()
	{
		return EMS_DASHBOARD_LAST_ACCESS;
	}

	/**
	 * @return the eMS_DASHBOARD_SET
	 */
	public List<DashboardSetRowEntity> getEMS_DASHBOARD_SET()
	{
		return EMS_DASHBOARD_SET;
	}

	/**
	 * @return the eMS_DASHBOARD_TILE
	 */
	public List<DashboardTileRowEntity> getEMS_DASHBOARD_TILE()
	{
		return EMS_DASHBOARD_TILE;
	}

	/**
	 * @return the eMS_DASHBOARD_TILE_PARAMS
	 */
	public List<DashboardTileParamsRowEntity> getEMS_DASHBOARD_TILE_PARAMS()
	{
		return EMS_DASHBOARD_TILE_PARAMS;
	}

	/**
	 * @return the eMS_DASHBOARD_USER_OPTIONS
	 */
	public List<DashboardUserOptionsRowEntity> getEMS_DASHBOARD_USER_OPTIONS()
	{
		return EMS_DASHBOARD_USER_OPTIONS;
	}

	/**
	 * @return the eMS_PREFERENCE
	 */
	public List<PreferenceRowEntity> getEMS_PREFERENCE()
	{
		return EMS_PREFERENCE;
	}

	/**
	 * @param eMS_DASHBOARD
	 *            the eMS_DASHBOARD to set
	 */
	public void setEMS_DASHBOARD(List<DashboardRowEntity> eMS_DASHBOARD)
	{
		EMS_DASHBOARD = eMS_DASHBOARD;
	}

	/**
	 * @param eMS_DASHBOARD_FAVORITE
	 *            the eMS_DASHBOARD_FAVORITE to set
	 */
	public void setEMS_DASHBOARD_FAVORITE(List<DashboardFavoriteRowEntity> eMS_DASHBOARD_FAVORITE)
	{
		EMS_DASHBOARD_FAVORITE = eMS_DASHBOARD_FAVORITE;
	}

	/**
	 * @param eMS_DASHBOARD_LAST_ACCESS
	 *            the eMS_DASHBOARD_LAST_ACCESS to set
	 */
	public void setEMS_DASHBOARD_LAST_ACCESS(List<DashboardLastAccessRowEntity> eMS_DASHBOARD_LAST_ACCESS)
	{
		EMS_DASHBOARD_LAST_ACCESS = eMS_DASHBOARD_LAST_ACCESS;
	}

	/**
	 * @param eMS_DASHBOARD_SET
	 *            the eMS_DASHBOARD_SET to set
	 */
	public void setEMS_DASHBOARD_SET(List<DashboardSetRowEntity> eMS_DASHBOARD_SET)
	{
		EMS_DASHBOARD_SET = eMS_DASHBOARD_SET;
	}

	/**
	 * @param eMS_DASHBOARD_TILE
	 *            the eMS_DASHBOARD_TILE to set
	 */
	public void setEMS_DASHBOARD_TILE(List<DashboardTileRowEntity> eMS_DASHBOARD_TILE)
	{
		EMS_DASHBOARD_TILE = eMS_DASHBOARD_TILE;
	}

	/**
	 * @param eMS_DASHBOARD_TILE_PARAMS
	 *            the eMS_DASHBOARD_TILE_PARAMS to set
	 */
	public void setEMS_DASHBOARD_TILE_PARAMS(List<DashboardTileParamsRowEntity> eMS_DASHBOARD_TILE_PARAMS)
	{
		EMS_DASHBOARD_TILE_PARAMS = eMS_DASHBOARD_TILE_PARAMS;
	}

	/**
	 * @param eMS_DASHBOARD_USER_OPTIONS
	 *            the eMS_DASHBOARD_USER_OPTIONS to set
	 */
	public void setEMS_DASHBOARD_USER_OPTIONS(List<DashboardUserOptionsRowEntity> eMS_DASHBOARD_USER_OPTIONS)
	{
		EMS_DASHBOARD_USER_OPTIONS = eMS_DASHBOARD_USER_OPTIONS;
	}

	/**
	 * @param eMS_PREFERENCE
	 *            the eMS_PREFERENCE to set
	 */
	public void setEMS_PREFERENCE(List<PreferenceRowEntity> eMS_PREFERENCE)
	{
		EMS_PREFERENCE = eMS_PREFERENCE;
	}
}
