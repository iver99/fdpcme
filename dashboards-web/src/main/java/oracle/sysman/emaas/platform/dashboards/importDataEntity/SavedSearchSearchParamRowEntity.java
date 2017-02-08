/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.importDataEntity;

import java.math.BigInteger;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author pingwu
 */
public class SavedSearchSearchParamRowEntity implements RowEntity
{
	@JsonProperty("SEARCH_ID")
	private BigInteger searchId;

	@JsonProperty("NAME")
	private String name;

	@JsonProperty("PARAM_ATTRIBUTES")
	private String paramAttributes;

	@JsonProperty("PARAM_TYPE")
	private Long paramType;

	@JsonProperty("PARAM_VALUE_CLOB")
	private String paramValueClob;

	@JsonProperty("PARAM_VALUE_STR")
	private String paramValueStr;

	@JsonProperty("TENANT_ID")
	private Long tenantId;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("LAST_MODIFICATION_DATE")
	private String lastModificationDate;
	
	@JsonProperty("DELETED")
	private Integer deleted;
	
	

	public SavedSearchSearchParamRowEntity() {
		super();
	}

	public SavedSearchSearchParamRowEntity(BigInteger searchId, String name,
			String paramAttributes, Long paramType, String paramValueClob,
			String paramValueStr, Long tenantId, String creationDate,
			String lastModificationDate, Integer deleted) {
		super();
		this.searchId = searchId;
		this.name = name;
		this.paramAttributes = paramAttributes;
		this.paramType = paramType;
		this.paramValueClob = paramValueClob;
		this.paramValueStr = paramValueStr;
		this.tenantId = tenantId;
		this.creationDate = creationDate;
		this.lastModificationDate = lastModificationDate;
		this.deleted = deleted;
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
		SavedSearchSearchParamRowEntity other = (SavedSearchSearchParamRowEntity) obj;
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
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}
		if (paramAttributes == null) {
			if (other.paramAttributes != null) {
				return false;
			}
		}
		else if (!paramAttributes.equals(other.paramAttributes)) {
			return false;
		}
		if (paramType == null) {
			if (other.paramType != null) {
				return false;
			}
		}
		else if (!paramType.equals(other.paramType)) {
			return false;
		}
		if (paramValueClob == null) {
			if (other.paramValueClob != null) {
				return false;
			}
		}
		else if (!paramValueClob.equals(other.paramValueClob)) {
			return false;
		}
		if (paramValueStr == null) {
			if (other.paramValueStr != null) {
				return false;
			}
		}
		else if (!paramValueStr.equals(other.paramValueStr)) {
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
	 * @return the lastModificationDate
	 */
	public String getLastModificationDate()
	{
		return lastModificationDate;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	
	

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the paramAttributes
	 */
	public String getParamAttributes()
	{
		return paramAttributes;
	}

	/**
	 * @return the paramType
	 */
	public Long getParamType()
	{
		return paramType;
	}

	/**
	 * @return the paramValueClob
	 */
	public String getParamValueClob()
	{
		return paramValueClob;
	}

	/**
	 * @return the paramValueStr
	 */
	public String getParamValueStr()
	{
		return paramValueStr;
	}

	/**
	 * @return the searchId
	 */
	public BigInteger getSearchId()
	{
		return searchId;
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
		result = prime * result + (lastModificationDate == null ? 0 : lastModificationDate.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (paramAttributes == null ? 0 : paramAttributes.hashCode());
		result = prime * result + (paramType == null ? 0 : paramType.hashCode());
		result = prime * result + (paramValueClob == null ? 0 : paramValueClob.hashCode());
		result = prime * result + (paramValueStr == null ? 0 : paramValueStr.hashCode());
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
	 * @param lastModificationDate
	 *            the lastModificationDate to set
	 */
	public void setLastModificationDate(String lastModificationDate)
	{
		this.lastModificationDate = lastModificationDate;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param paramAttributes
	 *            the paramAttributes to set
	 */
	public void setParamAttributes(String paramAttributes)
	{
		this.paramAttributes = paramAttributes;
	}

	/**
	 * @param paramType
	 *            the paramType to set
	 */
	public void setParamType(Long paramType)
	{
		this.paramType = paramType;
	}

	/**
	 * @param paramValueClob
	 *            the paramValueClob to set
	 */
	public void setParamValueClob(String paramValueClob)
	{
		this.paramValueClob = paramValueClob;
	}

	/**
	 * @param paramValueStr
	 *            the paramValueStr to set
	 */
	public void setParamValueStr(String paramValueStr)
	{
		this.paramValueStr = paramValueStr;
	}

	/**
	 * @param searchId
	 *            the searchId to set
	 */
	public void setSearchId(BigInteger searchId)
	{
		this.searchId = searchId;
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
		return "SavedSearchSearchParamRowEntity [name=" + name + ", paramAttributes=" + paramAttributes + ", paramType="
				+ paramType + ", paramValueClob=" + paramValueClob + ", paramValueStr=" + paramValueStr + ", tenantId="
				+ tenantId + ", creationDate=" + creationDate + ", lastModificationDate=" + lastModificationDate + "]";
	}

}
