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
 * Class that represents the retrieved data from one OMC instance, including the data rows for DF tables
 *
 * @author guochen
 */
public class TableRowsEntity
{
	@JsonProperty("EMS_DASHBOARD")
	private List<DashboardRowEntity> emsDashboard;

	@JsonProperty("EMS_DASHBOARD_FAVORITE")
	private List<DashboardFavoriteRowEntity> emsDashboardFavorite;

	@JsonProperty("EMS_DASHBOARD_LAST_ACCESS")
	private List<DashboardLastAccessRowEntity> emsDashboardLastAccess;

	@JsonProperty("EMS_DASHBOARD_SET")
	private List<DashboardSetRowEntity> emsDashboardSet;

	@JsonProperty("EMS_DASHBOARD_TILE")
	private List<DashboardTileRowEntity> emsDashboardTile;

	@JsonProperty("EMS_DASHBOARD_TILE_PARAMS")
	private List<DashboardTileParamsRowEntity> emsDashboardTileParams;

	@JsonProperty("EMS_DASHBOARD_USER_OPTIONS")
	private List<DashboardUserOptionsRowEntity> emsDashboardUserOptions;

	@JsonProperty("EMS_PREFERENCE")
	private List<PreferenceRowEntity> emsPreference;

	public TableRowsEntity()
	{
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TableRowsEntity other = (TableRowsEntity) obj;
		if (emsDashboard == null) {
			if (other.emsDashboard != null) {
				return false;
			}
		}
		else if (!emsDashboard.equals(other.emsDashboard)) {
			return false;
		}
		if (emsDashboardFavorite == null) {
			if (other.emsDashboardFavorite != null) {
				return false;
			}
		}
		else if (!emsDashboardFavorite.equals(other.emsDashboardFavorite)) {
			return false;
		}
		if (emsDashboardLastAccess == null) {
			if (other.emsDashboardLastAccess != null) {
				return false;
			}
		}
		else if (!emsDashboardLastAccess.equals(other.emsDashboardLastAccess)) {
			return false;
		}
		if (emsDashboardSet == null) {
			if (other.emsDashboardSet != null) {
				return false;
			}
		}
		else if (!emsDashboardSet.equals(other.emsDashboardSet)) {
			return false;
		}
		if (emsDashboardTile == null) {
			if (other.emsDashboardTile != null) {
				return false;
			}
		}
		else if (!emsDashboardTile.equals(other.emsDashboardTile)) {
			return false;
		}
		if (emsDashboardTileParams == null) {
			if (other.emsDashboardTileParams != null) {
				return false;
			}
		}
		else if (!emsDashboardTileParams.equals(other.emsDashboardTileParams)) {
			return false;
		}
		if (emsDashboardUserOptions == null) {
			if (other.emsDashboardUserOptions != null) {
				return false;
			}
		}
		else if (!emsDashboardUserOptions.equals(other.emsDashboardUserOptions)) {
			return false;
		}
		if (emsPreference == null) {
			if (other.emsPreference != null) {
				return false;
			}
		}
		else if (!emsPreference.equals(other.emsPreference)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the emsDashboard
	 */
	public List<DashboardRowEntity> getEmsDashboard()
	{
		return emsDashboard;
	}

	/**
	 * @return the emsDashboardFavorite
	 */
	public List<DashboardFavoriteRowEntity> getEmsDashboardFavorite()
	{
		return emsDashboardFavorite;
	}

	/**
	 * @return the emsDashboardLastAccess
	 */
	public List<DashboardLastAccessRowEntity> getEmsDashboardLastAccess()
	{
		return emsDashboardLastAccess;
	}

	/**
	 * @return the emsDashboardSet
	 */
	public List<DashboardSetRowEntity> getEmsDashboardSet()
	{
		return emsDashboardSet;
	}

	/**
	 * @return the emsDashboardTile
	 */
	public List<DashboardTileRowEntity> getEmsDashboardTile()
	{
		return emsDashboardTile;
	}

	/**
	 * @return the emsDashboardTileParams
	 */
	public List<DashboardTileParamsRowEntity> getEmsDashboardTileParams()
	{
		return emsDashboardTileParams;
	}

	/**
	 * @return the emsDashboardUserOptions
	 */
	public List<DashboardUserOptionsRowEntity> getEmsDashboardUserOptions()
	{
		return emsDashboardUserOptions;
	}

	/**
	 * @return the emsPreference
	 */
	public List<PreferenceRowEntity> getEmsPreference()
	{
		return emsPreference;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (emsDashboard == null ? 0 : emsDashboard.hashCode());
		result = prime * result + (emsDashboardFavorite == null ? 0 : emsDashboardFavorite.hashCode());
		result = prime * result + (emsDashboardLastAccess == null ? 0 : emsDashboardLastAccess.hashCode());
		result = prime * result + (emsDashboardSet == null ? 0 : emsDashboardSet.hashCode());
		result = prime * result + (emsDashboardTile == null ? 0 : emsDashboardTile.hashCode());
		result = prime * result + (emsDashboardTileParams == null ? 0 : emsDashboardTileParams.hashCode());
		result = prime * result + (emsDashboardUserOptions == null ? 0 : emsDashboardUserOptions.hashCode());
		result = prime * result + (emsPreference == null ? 0 : emsPreference.hashCode());
		return result;
	}

	/**
	 * @param emsDashboard
	 *            the emsDashboard to set
	 */
	public void setEmsDashboard(List<DashboardRowEntity> emsDashboard)
	{
		this.emsDashboard = emsDashboard;
	}

	/**
	 * @param emsDashboardFavorite
	 *            the emsDashboardFavorite to set
	 */
	public void setEmsDashboardFavorite(List<DashboardFavoriteRowEntity> emsDashboardFavorite)
	{
		this.emsDashboardFavorite = emsDashboardFavorite;
	}

	/**
	 * @param emsDashboardLastAccess
	 *            the emsDashboardLastAccess to set
	 */
	public void setEmsDashboardLastAccess(List<DashboardLastAccessRowEntity> emsDashboardLastAccess)
	{
		this.emsDashboardLastAccess = emsDashboardLastAccess;
	}

	/**
	 * @param emsDashboardSet
	 *            the emsDashboardSet to set
	 */
	public void setEmsDashboardSet(List<DashboardSetRowEntity> emsDashboardSet)
	{
		this.emsDashboardSet = emsDashboardSet;
	}

	/**
	 * @param emsDashboardTile
	 *            the emsDashboardTile to set
	 */
	public void setEmsDashboardTile(List<DashboardTileRowEntity> emsDashboardTile)
	{
		this.emsDashboardTile = emsDashboardTile;
	}

	/**
	 * @param emsDashboardTileParams
	 *            the emsDashboardTileParams to set
	 */
	public void setEmsDashboardTileParams(List<DashboardTileParamsRowEntity> emsDashboardTileParams)
	{
		this.emsDashboardTileParams = emsDashboardTileParams;
	}

	/**
	 * @param emsDashboardUserOptions
	 *            the emsDashboardUserOptions to set
	 */
	public void setEmsDashboardUserOptions(List<DashboardUserOptionsRowEntity> emsDashboardUserOptions)
	{
		this.emsDashboardUserOptions = emsDashboardUserOptions;
	}

	/**
	 * @param emsPreference
	 *            the emsPreference to set
	 */
	public void setEmsPreference(List<PreferenceRowEntity> emsPreference)
	{
		this.emsPreference = emsPreference;
	}
}
