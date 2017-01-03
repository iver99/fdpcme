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

import java.math.BigInteger;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author guochen
 */
public class DashboardSetRowEntity implements RowEntity
{
	@JsonProperty("DASHBOARD_SET_ID")
	private BigInteger dashboardSetId;

	@JsonProperty("TENANT_ID")
	private Long tenantId;

	@JsonProperty("SUB_DASHBOARD_ID")
	private Long subDashboardId;

	@JsonProperty("POSITION")
	private Long position;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("LAST_MODIFICATION_DATE")
	private String lastModificationDate;

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
		DashboardSetRowEntity other = (DashboardSetRowEntity) obj;
		if (creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		}
		else if (!creationDate.equals(other.creationDate)) {
			return false;
		}
		if (dashboardSetId == null) {
			if (other.dashboardSetId != null) {
				return false;
			}
		}
		else if (!dashboardSetId.equals(other.dashboardSetId)) {
			return false;
		}
		if (lastModificationDate == null) {
			if (other.lastModificationDate != null) {
				return false;
			}
		}
		else if (!lastModificationDate.equals(other.lastModificationDate)) {
			return false;
		}
		if (position == null) {
			if (other.position != null) {
				return false;
			}
		}
		else if (!position.equals(other.position)) {
			return false;
		}
		if (subDashboardId == null) {
			if (other.subDashboardId != null) {
				return false;
			}
		}
		else if (!subDashboardId.equals(other.subDashboardId)) {
			return false;
		}
		if (tenantId == null) {
			if (other.tenantId != null) {
				return false;
			}
		}
		else if (!tenantId.equals(other.tenantId)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the creationDate
	 */
	public String getCreationDate()
	{
		return creationDate;
	}

	/**
	 * @return the dashboardSetId
	 */
	public BigInteger getDashboardSetId()
	{
		return dashboardSetId;
	}

	/**
	 * @return the lastModificationDate
	 */
	public String getLastModificationDate()
	{
		return lastModificationDate;
	}

	/**
	 * @return the position
	 */
	public Long getPosition()
	{
		return position;
	}

	/**
	 * @return the subDashboardId
	 */
	public Long getSubDashboardId()
	{
		return subDashboardId;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId()
	{
		return tenantId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (creationDate == null ? 0 : creationDate.hashCode());
		result = prime * result + (dashboardSetId == null ? 0 : dashboardSetId.hashCode());
		result = prime * result + (lastModificationDate == null ? 0 : lastModificationDate.hashCode());
		result = prime * result + (position == null ? 0 : position.hashCode());
		result = prime * result + (subDashboardId == null ? 0 : subDashboardId.hashCode());
		result = prime * result + (tenantId == null ? 0 : tenantId.hashCode());
		return result;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(String creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
	 * @param dashboardSetId
	 *            the dashboardSetId to set
	 */
	public void setDashboardSetId(BigInteger dashboardSetId)
	{
		this.dashboardSetId = dashboardSetId;
	}

	/**
	 * @param lastModificationDate
	 *            the lastModificationDate to set
	 */
	public void setLastModificationDate(String lastModificationDate)
	{
		this.lastModificationDate = lastModificationDate;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Long position)
	{
		this.position = position;
	}

	/**
	 * @param subDashboardId
	 *            the subDashboardId to set
	 */
	public void setSubDashboardId(Long subDashboardId)
	{
		this.subDashboardId = subDashboardId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(Long tenantId)
	{
		this.tenantId = tenantId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "DashboardSetRowEntity [dashboardSetId=" + dashboardSetId + ", tenantId=" + tenantId + ", subDashboardId="
				+ subDashboardId + ", position=" + position + ", creationDate=" + creationDate + ", lastModificationDate="
				+ lastModificationDate + "]";
	}
}
