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

/**
 * @author guochen
 */
public class PreferenceRowEntity implements RowEntity
{
	@JsonProperty("USER_NAME")
	private String userName;

	@JsonProperty("PREF_KEY")
	private String prefKey;

	@JsonProperty("PREF_VALUE")
	private String prefValue;

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
		PreferenceRowEntity other = (PreferenceRowEntity) obj;
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
		if (prefKey == null) {
			if (other.prefKey != null) {
				return false;
			}
		}
		else if (!prefKey.equals(other.prefKey)) {
			return false;
		}
		if (prefValue == null) {
			if (other.prefValue != null) {
				return false;
			}
		}
		else if (!prefValue.equals(other.prefValue)) {
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
		if (userName == null) {
			if (other.userName != null) {
				return false;
			}
		}
		else if (!userName.equals(other.userName)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the prefKey
	 */
	public String getPrefKey()
	{
		return prefKey;
	}

	/**
	 * @return the prefValue
	 */
	public String getPrefValue()
	{
		return prefValue;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId()
	{
		return tenantId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (prefKey == null ? 0 : prefKey.hashCode());
		result = prime * result + (prefValue == null ? 0 : prefValue.hashCode());
		result = prime * result + (tenantId == null ? 0 : tenantId.hashCode());
		result = prime * result + (userName == null ? 0 : userName.hashCode());
		result = prime * result + (creationDate == null ? 0 : creationDate.hashCode());
		result = prime * result + (lastModificationDate == null ? 0 : lastModificationDate.hashCode());
		return result;
	}

	/**
	 * @param prefKey
	 *            the prefKey to set
	 */
	public void setPrefKey(String prefKey)
	{
		this.prefKey = prefKey;
	}

	/**
	 * @param prefValue
	 *            the prefValue to set
	 */
	public void setPrefValue(String prefValue)
	{
		this.prefValue = prefValue;
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
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
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
		return "PreferenceRowEntity [userName=" + userName + ", prefKey=" + prefKey + ", prefValue=" + prefValue + ", tenantId="
				+ tenantId + ", creationDate=" + creationDate + ", lastModificationDate=" + lastModificationDate+"]";
	}
}
