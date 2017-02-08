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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author pingwu
 */
public class SavedSearchSearchRowEntity implements RowEntity
{
	@JsonProperty("SEARCH_ID")
	private BigInteger searchId;

	@JsonProperty("DESCRIPTION")
	private String description;

	@JsonProperty("IS_LOCKED")
	private Integer isLocked;

	@JsonProperty("LAST_MODIFIED_BY")
	private String lastModifiedBy;

	@JsonProperty("METADATA_CLOB")
	private String metadataClob;

	@JsonProperty("NAME")
	private String name;	

	@JsonProperty("OWNER")
	private String owner;

	@JsonProperty("SEARCH_DISPLAY_STR")
	private String searchDisplayStr;
	
	@JsonProperty("SEARCH_GUID")
	private String searchGuid;

	@JsonProperty("SYSTEM_SEARCH")
	private Integer systemSearch;

	@JsonProperty("UI_HIDDEN")
	private Integer uiHidden;

	@JsonProperty("DELETED")
	private BigInteger deleted;

	@JsonProperty("IS_WIDGET")
	private Integer isWidget;

	@JsonProperty("CREATION_DATE")
	private String creationDate;

	@JsonProperty("LAST_MODIFICATION_DATE")
	private String lastModificationDate;

	@JsonProperty("WIDGET_SOURCE")
	private String nameWidgetSource;

	@JsonProperty("WIDGET_GROUP_NAME")
	private String widgetGroupName;

	@JsonProperty("WIDGET_SCREENSHOT_HREF")
	private String widgetScreenshotHref;

	@JsonProperty("WIDGET_ICON")
	private String widgetIcon;

	@JsonProperty("WIDGET_KOC_NAME")
	private String widgetKocName;

	@JsonProperty("WIDGET_VIEWMODEL")
	private String widgetViewModel;

	@JsonProperty("WIDGET_TEMPLATE")
	private String widgetTemplate;

	@JsonProperty("WIDGET_SUPPORT_TIME_CONTROL")
	private String widgetSupportTimeControl;

	@JsonProperty("WIDGET_LINKED_DASHBOARD")
	private Long widgetLinkedDashboard;

	@JsonProperty("WIDGET_DEFAULT_WIDTH")
	private Long widgetDefaulWidth;

	@JsonProperty("WIDGET_DEFAULT_HEIGHT")
	private Long widgetDefaultHeight;

	@JsonProperty("DASHBOARD_INELIGIBLE")
	private String dashboardIneligible;

	@JsonProperty("TENANT_ID")
	private Long tenantId;

	@JsonProperty("FOLDER_ID")
	private BigInteger folderId;

	@JsonProperty("CATEGORY_ID")
	private BigInteger categoryId;

	@JsonProperty("PROVIDER_NAME")
	private String providerName;

	@JsonProperty("PROVIDER_VERSION")
	private String providerVersion;

	@JsonProperty("PROVIDER_ASSET_ROOT")
	private String providerAssetRoot;
	
	

	public SavedSearchSearchRowEntity() {
		super();
	}

	public SavedSearchSearchRowEntity(BigInteger searchId, String description,
			String descriptionNlsid, String descriptionSubsystem,
			String emPluginId, Integer isLocked, String lastModifiedBy,
			String metadataClob, String name, String nameNlsid,
			String nameSubsystem, String owner, String searchDisplayStr,
			String searchGuid, Integer systemSearch, Integer uiHidden,
			BigInteger deleted, Integer isWidget, String creationDate,
			String lastModificationDate, String nameWidgetSource,
			String widgetGroupName, String widgetScreenshotHref,
			String widgetIcon, String widgetKocName, String widgetViewModel,
			String widgetTemplate, String widgetSupportTimeControl,
			Long widgetLinkedDashboard, Long widgetDefaulWidth,
			Long widgetDefaultHeight, String dashboardIneligible,
			Long tenantId, BigInteger folderId, BigInteger categoryId,
			String providerName, String providerVersion,
			String providerAssetRoot) {
		super();
		this.searchId = searchId;
		this.description = description;
		this.isLocked = isLocked;
		this.lastModifiedBy = lastModifiedBy;
		this.metadataClob = metadataClob;
		this.name = name;
		this.owner = owner;
		this.searchDisplayStr = searchDisplayStr;
		this.searchGuid = searchGuid;
		this.systemSearch = systemSearch;
		this.uiHidden = uiHidden;
		this.deleted = deleted;
		this.isWidget = isWidget;
		this.creationDate = creationDate;
		this.lastModificationDate = lastModificationDate;
		this.nameWidgetSource = nameWidgetSource;
		this.widgetGroupName = widgetGroupName;
		this.widgetScreenshotHref = widgetScreenshotHref;
		this.widgetIcon = widgetIcon;
		this.widgetKocName = widgetKocName;
		this.widgetViewModel = widgetViewModel;
		this.widgetTemplate = widgetTemplate;
		this.widgetSupportTimeControl = widgetSupportTimeControl;
		this.widgetLinkedDashboard = widgetLinkedDashboard;
		this.widgetDefaulWidth = widgetDefaulWidth;
		this.widgetDefaultHeight = widgetDefaultHeight;
		this.dashboardIneligible = dashboardIneligible;
		this.tenantId = tenantId;
		this.folderId = folderId;
		this.categoryId = categoryId;
		this.providerName = providerName;
		this.providerVersion = providerVersion;
		this.providerAssetRoot = providerAssetRoot;
	}

	public String getSearchGuid() {
		return searchGuid;
	}

	public void setSearchGuid(String searchGuid) {
		this.searchGuid = searchGuid;
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
		SavedSearchSearchRowEntity other = (SavedSearchSearchRowEntity) obj;
		if (creationDate == null) {
			if (other.creationDate != null) {
				return false;
			}
		}
		else if (!creationDate.equals(other.creationDate)) {
			return false;
		}
		if (dashboardIneligible == null) {
			if (other.dashboardIneligible != null) {
				return false;
			}
		}
		else if (!dashboardIneligible.equals(other.dashboardIneligible)) {
			return false;
		}
		if (deleted == null) {
			if (other.deleted != null) {
				return false;
			}
		}
		else if (!deleted.equals(other.deleted)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		}
		else if (!description.equals(other.description)) {
			return false;
		}
		
		if (isLocked == null) {
			if (other.isLocked != null) {
				return false;
			}
		}
		else if (!isLocked.equals(other.isLocked)) {
			return false;
		}
		if (isWidget == null) {
			if (other.isWidget != null) {
				return false;
			}
		}
		else if (!isWidget.equals(other.isWidget)) {
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
		if (lastModifiedBy == null) {
			if (other.lastModifiedBy != null) {
				return false;
			}
		}
		else if (!lastModifiedBy.equals(other.lastModifiedBy)) {
			return false;
		}
		if (metadataClob == null) {
			if (other.metadataClob != null) {
				return false;
			}
		}
		else if (!metadataClob.equals(other.metadataClob)) {
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
	
		if (nameWidgetSource == null) {
			if (other.nameWidgetSource != null) {
				return false;
			}
		}
		else if (!nameWidgetSource.equals(other.nameWidgetSource)) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		}
		else if (!owner.equals(other.owner)) {
			return false;
		}
		if (searchDisplayStr == null) {
			if (other.searchDisplayStr != null) {
				return false;
			}
		}
		else if (!searchDisplayStr.equals(other.searchDisplayStr)) {
			return false;
		}

		if (searchId == null) {
			if (other.searchId != null) {
				return false;
			}
		}
		else if (!searchId.equals(other.searchId)) {
			return false;
		}
		if (systemSearch == null) {
			if (other.systemSearch != null) {
				return false;
			}
		}
		else if (!systemSearch.equals(other.systemSearch)) {
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
		if (uiHidden == null) {
			if (other.uiHidden != null) {
				return false;
			}
		}
		else if (!uiHidden.equals(other.uiHidden)) {
			return false;
		}
		if (widgetDefaulWidth == null) {
			if (other.widgetDefaulWidth != null) {
				return false;
			}
		}
		else if (!widgetDefaulWidth.equals(other.widgetDefaulWidth)) {
			return false;
		}
		if (widgetDefaultHeight == null) {
			if (other.widgetDefaultHeight != null) {
				return false;
			}
		}
		else if (!widgetDefaultHeight.equals(other.widgetDefaultHeight)) {
			return false;
		}
		if (widgetGroupName == null) {
			if (other.widgetGroupName != null) {
				return false;
			}
		}
		else if (!widgetGroupName.equals(other.widgetGroupName)) {
			return false;
		}
		if (widgetIcon == null) {
			if (other.widgetIcon != null) {
				return false;
			}
		}
		else if (!widgetIcon.equals(other.widgetIcon)) {
			return false;
		}
		if (widgetKocName == null) {
			if (other.widgetKocName != null) {
				return false;
			}
		}
		else if (!widgetKocName.equals(other.widgetKocName)) {
			return false;
		}
		if (widgetLinkedDashboard == null) {
			if (other.widgetLinkedDashboard != null) {
				return false;
			}
		}
		else if (!widgetLinkedDashboard.equals(other.widgetLinkedDashboard)) {
			return false;
		}
		if (widgetScreenshotHref == null) {
			if (other.widgetScreenshotHref != null) {
				return false;
			}
		}
		else if (!widgetScreenshotHref.equals(other.widgetScreenshotHref)) {
			return false;
		}
		if (widgetSupportTimeControl == null) {
			if (other.widgetSupportTimeControl != null) {
				return false;
			}
		}
		else if (!widgetSupportTimeControl.equals(other.widgetSupportTimeControl)) {
			return false;
		}
		if (widgetTemplate == null) {
			if (other.widgetTemplate != null) {
				return false;
			}
		}
		else if (!widgetTemplate.equals(other.widgetTemplate)) {
			return false;
		}
		if (widgetViewModel == null) {
			if (other.widgetViewModel != null) {
				return false;
			}
		}
		else if (!widgetViewModel.equals(other.widgetViewModel)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the categoryId
	 */
	public BigInteger getCategoryId()
	{
		return categoryId;
	}

	/**
	 * @return the creationDate
	 */
	public String getCreationDate()
	{
		return creationDate;
	}

	/**
	 * @return the dashboardIneligible
	 */
	public String getDashboardIneligible()
	{
		return dashboardIneligible;
	}

	/**
	 * @return the deleted
	 */
	public BigInteger getDeleted()
	{
		return deleted;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return the folderId
	 */
	public BigInteger getFolderId()
	{
		return folderId;
	}

	/**
	 * @return the isLocked
	 */
	public Integer getIsLocked()
	{
		return isLocked;
	}

	/**
	 * @return the isWidget
	 */
	public Integer getIsWidget()
	{
		return isWidget;
	}

	/**
	 * @return the lastModificationDate
	 */
	public String getLastModificationDate()
	{
		return lastModificationDate;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	/**
	 * @return the metadataClob
	 */
	public String getMetadataClob()
	{
		return metadataClob;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the nameWidgetSource
	 */
	public String getNameWidgetSource()
	{
		return nameWidgetSource;
	}

	/**
	 * @return the owner
	 */
	public String getOwner()
	{
		return owner;
	}

	/**
	 * @return the providerAssetRoot
	 */
	public String getProviderAssetRoot()
	{
		return providerAssetRoot;
	}

	/**
	 * @return the providerName
	 */
	public String getProviderName()
	{
		return providerName;
	}

	/**
	 * @return the providerVersion
	 */
	public String getProviderVersion()
	{
		return providerVersion;
	}

	/**
	 * @return the searchDisplayStr
	 */
	public String getSearchDisplayStr()
	{
		return searchDisplayStr;
	}

	/**
	 * @return the searchId
	 */
	public BigInteger getSearchId()
	{
		return searchId;
	}

	/**
	 * @return the systemSearch
	 */
	public Integer getSystemSearch()
	{
		return systemSearch;
	}

	/**
	 * @return the tenantId
	 */
	public Long getTenantId()
	{
		return tenantId;
	}

	/**
	 * @return the uiHidden
	 */
	public Integer getUiHidden()
	{
		return uiHidden;
	}

	/**
	 * @return the widgetDefaultHeight
	 */
	public Long getWidgetDefaultHeight()
	{
		return widgetDefaultHeight;
	}

	/**
	 * @return the widgetDefaulWidth
	 */
	public Long getWidgetDefaulWidth()
	{
		return widgetDefaulWidth;
	}

	/**
	 * @return the widgetGroupName
	 */
	public String getWidgetGroupName()
	{
		return widgetGroupName;
	}

	/**
	 * @return the widgetIcon
	 */
	public String getWidgetIcon()
	{
		return widgetIcon;
	}

	/**
	 * @return the widgetKocName
	 */
	public String getWidgetKocName()
	{
		return widgetKocName;
	}

	/**
	 * @return the widgetLinkedDashboard
	 */
	public Long getWidgetLinkedDashboard()
	{
		return widgetLinkedDashboard;
	}

	/**
	 * @return the widgetScreenshotHref
	 */
	public String getWidgetScreenshotHref()
	{
		return widgetScreenshotHref;
	}

	/**
	 * @return the widgetSupportTimeControl
	 */
	public String getWidgetSupportTimeControl()
	{
		return widgetSupportTimeControl;
	}

	/**
	 * @return the widgetTemplate
	 */
	public String getWidgetTemplate()
	{
		return widgetTemplate;
	}

	/**
	 * @return the widgetViewModel
	 */
	public String getWidgetViewModel()
	{
		return widgetViewModel;
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
		result = prime * result + (dashboardIneligible == null ? 0 : dashboardIneligible.hashCode());
		result = prime * result + (deleted == null ? 0 : deleted.hashCode());
		result = prime * result + (description == null ? 0 : description.hashCode());
		result = prime * result + (isLocked == null ? 0 : isLocked.hashCode());
		result = prime * result + (isWidget == null ? 0 : isWidget.hashCode());
		result = prime * result + (lastModificationDate == null ? 0 : lastModificationDate.hashCode());
		result = prime * result + (lastModifiedBy == null ? 0 : lastModifiedBy.hashCode());
		result = prime * result + (metadataClob == null ? 0 : metadataClob.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (nameWidgetSource == null ? 0 : nameWidgetSource.hashCode());
		result = prime * result + (owner == null ? 0 : owner.hashCode());
		result = prime * result + (searchDisplayStr == null ? 0 : searchDisplayStr.hashCode());
		result = prime * result + (searchId == null ? 0 : searchId.hashCode());
		result = prime * result + (systemSearch == null ? 0 : systemSearch.hashCode());
		result = prime * result + (tenantId == null ? 0 : tenantId.hashCode());
		result = prime * result + (uiHidden == null ? 0 : uiHidden.hashCode());
		result = prime * result + (widgetDefaulWidth == null ? 0 : widgetDefaulWidth.hashCode());
		result = prime * result + (widgetDefaultHeight == null ? 0 : widgetDefaultHeight.hashCode());
		result = prime * result + (widgetGroupName == null ? 0 : widgetGroupName.hashCode());
		result = prime * result + (widgetIcon == null ? 0 : widgetIcon.hashCode());
		result = prime * result + (widgetKocName == null ? 0 : widgetKocName.hashCode());
		result = prime * result + (widgetLinkedDashboard == null ? 0 : widgetLinkedDashboard.hashCode());
		result = prime * result + (widgetScreenshotHref == null ? 0 : widgetScreenshotHref.hashCode());
		result = prime * result + (widgetSupportTimeControl == null ? 0 : widgetSupportTimeControl.hashCode());
		result = prime * result + (widgetTemplate == null ? 0 : widgetTemplate.hashCode());
		result = prime * result + (widgetViewModel == null ? 0 : widgetViewModel.hashCode());
		return result;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(BigInteger categoryId)
	{
		this.categoryId = categoryId;
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
	 * @param dashboardIneligible
	 *            the dashboardIneligible to set
	 */
	public void setDashboardIneligible(String dashboardIneligible)
	{
		this.dashboardIneligible = dashboardIneligible;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(BigInteger deleted)
	{
		this.deleted = deleted;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @param folderId
	 *            the folderId to set
	 */
	public void setFolderId(BigInteger folderId)
	{
		this.folderId = folderId;
	}

	/**
	 * @param isLocked
	 *            the isLocked to set
	 */
	public void setIsLocked(Integer isLocked)
	{
		this.isLocked = isLocked;
	}

	/**
	 * @param isWidget
	 *            the isWidget to set
	 */
	public void setIsWidget(Integer isWidget)
	{
		this.isWidget = isWidget;
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
	 * @param lastModifiedBy
	 *            the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @param metadataClob
	 *            the metadataClob to set
	 */
	public void setMetadataClob(String metadataClob)
	{
		this.metadataClob = metadataClob;
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
	 * @param nameWidgetSource
	 *            the nameWidgetSource to set
	 */
	public void setNameWidgetSource(String nameWidgetSource)
	{
		this.nameWidgetSource = nameWidgetSource;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	/**
	 * @param providerAssetRoot
	 *            the providerAssetRoot to set
	 */
	public void setProviderAssetRoot(String providerAssetRoot)
	{
		this.providerAssetRoot = providerAssetRoot;
	}

	/**
	 * @param providerName
	 *            the providerName to set
	 */
	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
	}

	/**
	 * @param providerVersion
	 *            the providerVersion to set
	 */
	public void setProviderVersion(String providerVersion)
	{
		this.providerVersion = providerVersion;
	}

	/**
	 * @param searchDisplayStr
	 *            the searchDisplayStr to set
	 */
	public void setSearchDisplayStr(String searchDisplayStr)
	{
		this.searchDisplayStr = searchDisplayStr;
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
	 * @param systemSearch
	 *            the systemSearch to set
	 */
	public void setSystemSearch(Integer systemSearch)
	{
		this.systemSearch = systemSearch;
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
	 * @param uiHidden
	 *            the uiHidden to set
	 */
	public void setUiHidden(Integer uiHidden)
	{
		this.uiHidden = uiHidden;
	}

	/**
	 * @param widgetDefaultHeight
	 *            the widgetDefaultHeight to set
	 */
	public void setWidgetDefaultHeight(Long widgetDefaultHeight)
	{
		this.widgetDefaultHeight = widgetDefaultHeight;
	}

	/**
	 * @param widgetDefaulWidth
	 *            the widgetDefaulWidth to set
	 */
	public void setWidgetDefaulWidth(Long widgetDefaulWidth)
	{
		this.widgetDefaulWidth = widgetDefaulWidth;
	}

	/**
	 * @param widgetGroupName
	 *            the widgetGroupName to set
	 */
	public void setWidgetGroupName(String widgetGroupName)
	{
		this.widgetGroupName = widgetGroupName;
	}

	/**
	 * @param widgetIcon
	 *            the widgetIcon to set
	 */
	public void setWidgetIcon(String widgetIcon)
	{
		this.widgetIcon = widgetIcon;
	}

	/**
	 * @param widgetKocName
	 *            the widgetKocName to set
	 */
	public void setWidgetKocName(String widgetKocName)
	{
		this.widgetKocName = widgetKocName;
	}

	/**
	 * @param widgetLinkedDashboard
	 *            the widgetLinkedDashboard to set
	 */
	public void setWidgetLinkedDashboard(Long widgetLinkedDashboard)
	{
		this.widgetLinkedDashboard = widgetLinkedDashboard;
	}

	/**
	 * @param widgetScreenshotHref
	 *            the widgetScreenshotHref to set
	 */
	public void setWidgetScreenshotHref(String widgetScreenshotHref)
	{
		this.widgetScreenshotHref = widgetScreenshotHref;
	}

	/**
	 * @param widgetSupportTimeControl
	 *            the widgetSupportTimeControl to set
	 */
	public void setWidgetSupportTimeControl(String widgetSupportTimeControl)
	{
		this.widgetSupportTimeControl = widgetSupportTimeControl;
	}

	/**
	 * @param widgetTemplate
	 *            the widgetTemplate to set
	 */
	public void setWidgetTemplate(String widgetTemplate)
	{
		this.widgetTemplate = widgetTemplate;
	}

	/**
	 * @param widgetViewModel
	 *            the widgetViewModel to set
	 */
	public void setWidgetViewModel(String widgetViewModel)
	{
		this.widgetViewModel = widgetViewModel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "SavedSearchSearchRowEntity [searchId=" + searchId + ", description=" + description
				+ ", isLocked=" + isLocked + ", lastModifiedBy=" + lastModifiedBy + ", metadataClob=" + metadataClob + ", name="
				+ name  + ", owner=" + owner
				+ ", searchDisplayStr=" + searchDisplayStr + ", systemSearch=" + systemSearch + ", uiHidden=" + uiHidden
				+ ", deleted=" + deleted + ", isWidget=" + isWidget + ", creationDate=" + creationDate
				+ ", lastModificationDate=" + lastModificationDate + ", nameWidgetSource=" + nameWidgetSource
				+ ", widgetGroupName=" + widgetGroupName + ", widgetScreenshotHref=" + widgetScreenshotHref + ", widgetIcon="
				+ widgetIcon + ", widgetKocName=" + widgetKocName + ", widgetViewModel=" + widgetViewModel + ", widgetTemplate="
				+ widgetTemplate + ", widgetSupportTimeControl=" + widgetSupportTimeControl + ", widgetLinkedDashboard="
				+ widgetLinkedDashboard + ", widgetDefaulWidth=" + widgetDefaulWidth + ", widgetDefaultHeight="
				+ widgetDefaultHeight + ", dashboardIneligible=" + dashboardIneligible + ", tenantId=" + tenantId + ", folderId="
				+ folderId + ", categoryId=" + categoryId + ", providerName=" + providerName + ", providerVersion="
				+ providerVersion + ", providerAssetRoot=" + providerAssetRoot + "]";
	}

}
