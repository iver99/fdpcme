/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows;

import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigInteger;

/**
 * @author guochen
 */
public class DashboardLastAccessRowEntity implements RowEntity
{
	@JsonProperty("DASHBOARD_ID")
	private BigInteger dashboardId;

	@JsonProperty("ACCESSED_BY")
	private String accessedBy;

	@JsonProperty("ACCESS_DATE")
	private String accessDate;

	@JsonProperty("TENANT_ID")
	private Long tenantId;

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
		DashboardLastAccessRowEntity other = (DashboardLastAccessRowEntity) obj;
		if (creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		}
		else if (!creationDate.equals(other.creationDate)) {
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
		if (accessedBy == null) {
			if (other.accessedBy != null) {
				return false;
			}
		}
		else if (!accessedBy.equals(other.accessedBy)) {
			return false;
		}
		if (accessDate == null) {
			if (other.accessDate != null) {
				return false;
			}
		}
		else if (!accessDate.equals(other.accessDate)) {
			return false;
		}
		if (dashboardId == null) {
			if (other.dashboardId != null) {
				return false;
			}
		}
		else if (!dashboardId.equals(other.dashboardId)) {
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
	 * @return the accessDate
	 */
	public String getAccessDate()
	{
		return accessDate;
	}

	/**
	 * @return the accessedBy
	 */
	public String getAccessedBy()
	{
		return accessedBy;
	}

	/**
	 * @return the dashboardId
	 */
	public BigInteger getDashboardId()
	{
		return dashboardId;
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
		result = prime * result + (accessedBy == null ? 0 : accessedBy.hashCode());
		result = prime * result + (accessDate == null ? 0 : accessDate.hashCode());
		result = prime * result + (dashboardId == null ? 0 : dashboardId.hashCode());
		result = prime * result + (tenantId == null ? 0 : tenantId.hashCode());
		result = prime * result + (lastModificationDate == null ? 0 : lastModificationDate.hashCode());
		return result;
	}

	/**
	 * @param accessDate
	 *            the accessDate to set
	 */
	public void setAccessDate(String accessDate)
	{
		this.accessDate = accessDate;
	}
	/**
	 * @return the creationDate
	 */
	public String getCreationDate()
	{
		return creationDate;
	}
	/**
	 * @return the lastModificationDate
	 */
	public String getLastModificationDate()
	{
		return lastModificationDate;
	}
	/**
	 * @param accessedBy
	 *            the accessedBy to set
	 */
	public void setAccessedBy(String accessedBy)
	{
		this.accessedBy = accessedBy;
	}

	/**
	 * @param dashboardId
	 *            the dashboardId to set
	 */
	public void setDashboardId(BigInteger dashboardId)
	{
		this.dashboardId = dashboardId;
	}

	/**
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(Long tenantId)
	{
		this.tenantId = tenantId;
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
	 * @param lastModificationDate
	 *            the lastModificationDate to set
	 */
	public void setLastModificationDate(String lastModificationDate)
	{
		this.lastModificationDate = lastModificationDate;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "DashboardLastAccessRowEntity [dashboardId=" + dashboardId + ", accessedBy=" + accessedBy + ", accessDate="
				+ accessDate +", creationDate=" + creationDate + ", tenantId=" + tenantId + ", lastModificationDate=" + lastModificationDate+"]";
	}
}
