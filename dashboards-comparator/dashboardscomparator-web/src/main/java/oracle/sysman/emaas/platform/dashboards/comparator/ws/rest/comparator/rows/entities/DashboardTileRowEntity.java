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
public class DashboardTileRowEntity
{
	@JsonProperty("TILE_ID")
	private Long TILE_ID;

	@JsonProperty("DASHBOARD_ID")
	private Long DASHBOARD_ID;

	@JsonProperty("CREATION_DATE")
	private String CREATION_DATE;

	@JsonProperty("LAST_MODIFICATION_DATE")
	private String LAST_MODIFICATION_DATE;

	@JsonProperty("LAST_MODIFIED_BY")
	private String LAST_MODIFIED_BY;

	@JsonProperty("OWNER")
	private String OWNER;

	@JsonProperty("TITLE")
	private String TITLE;

	@JsonProperty("HEIGHT")
	private Long HEIGHT;

	@JsonProperty("WIDTH")
	private Long WIDTH;

	@JsonProperty("IS_MAXIMIZED")
	private Integer IS_MAXIMIZED;

	@JsonProperty("POSITION")
	private Long POSITION;

	@JsonProperty("TENANT_ID")
	private Long TENANT_ID;

	@JsonProperty("WIDGET_UNIQUE_ID")
	private String WIDGET_UNIQUE_ID;

	@JsonProperty("WIDGET_NAME")
	private String WIDGET_NAME;

	@JsonProperty("WIDGET_DESCRIPTION")
	private String WIDGET_DESCRIPTION;

	@JsonProperty("WIDGET_GROUP_NAME")
	private String WIDGET_GROUP_NAME;

	@JsonProperty("WIDGET_ICON")
	private String WIDGET_ICON;

	@JsonProperty("WIDGET_HISTOGRAM")
	private String WIDGET_HISTOGRAM;

	@JsonProperty("WIDGET_OWNER")
	private String WIDGET_OWNER;

	@JsonProperty("WIDGET_CREATION_TIME")
	private String WIDGET_CREATION_TIME;

	@JsonProperty("WIDGET_SOURCE")
	private Long WIDGET_SOURCE;

	@JsonProperty("WIDGET_KOC_NAME")
	private String WIDGET_KOC_NAME;

	@JsonProperty("WIDGET_VIEWMODE")
	private String WIDGET_VIEWMODE;

	@JsonProperty("WIDGET_TEMPLATE")
	private String WIDGET_TEMPLATE;

	@JsonProperty("PROVIDER_NAME")
	private String PROVIDER_NAME;

	@JsonProperty("PROVIDER_VERSION")
	private String PROVIDER_VERSION;

	@JsonProperty("PROVIDER_ASSET_ROOT")
	private String PROVIDER_ASSET_ROOT;

	@JsonProperty("TILE_ROW")
	private Long TILE_ROW;

	@JsonProperty("TILE_COLUMN")
	private Long TILE_COLUMN;

	@JsonProperty("TYPE")
	private Long TYPE;

	@JsonProperty("WIDGET_SUPPORT_TIME_CONTROL")
	private Integer WIDGET_SUPPORT_TIME_CONTROL;

	/**
	 * @return the cREATION_DATE
	 */
	public String getCREATION_DATE()
	{
		return CREATION_DATE;
	}

	/**
	 * @return the dASHBOARD_ID
	 */
	public Long getDASHBOARD_ID()
	{
		return DASHBOARD_ID;
	}

	/**
	 * @return the hEIGHT
	 */
	public Long getHEIGHT()
	{
		return HEIGHT;
	}

	/**
	 * @return the iS_MAXIMIZED
	 */
	public Integer getIS_MAXIMIZED()
	{
		return IS_MAXIMIZED;
	}

	/**
	 * @return the lAST_MODIFICATION_DATE
	 */
	public String getLAST_MODIFICATION_DATE()
	{
		return LAST_MODIFICATION_DATE;
	}

	/**
	 * @return the lAST_MODIFIED_BY
	 */
	public String getLAST_MODIFIED_BY()
	{
		return LAST_MODIFIED_BY;
	}

	/**
	 * @return the oWNER
	 */
	public String getOWNER()
	{
		return OWNER;
	}

	/**
	 * @return the pOSITION
	 */
	public Long getPOSITION()
	{
		return POSITION;
	}

	/**
	 * @return the pROVIDER_ASSET_ROOT
	 */
	public String getPROVIDER_ASSET_ROOT()
	{
		return PROVIDER_ASSET_ROOT;
	}

	/**
	 * @return the pROVIDER_NAME
	 */
	public String getPROVIDER_NAME()
	{
		return PROVIDER_NAME;
	}

	/**
	 * @return the pROVIDER_VERSION
	 */
	public String getPROVIDER_VERSION()
	{
		return PROVIDER_VERSION;
	}

	/**
	 * @return the tENANT_ID
	 */
	public Long getTENANT_ID()
	{
		return TENANT_ID;
	}

	/**
	 * @return the tILE_COLUMN
	 */
	public Long getTILE_COLUMN()
	{
		return TILE_COLUMN;
	}

	/**
	 * @return the tILE_ID
	 */
	public Long getTILE_ID()
	{
		return TILE_ID;
	}

	/**
	 * @return the tILE_ROW
	 */
	public Long getTILE_ROW()
	{
		return TILE_ROW;
	}

	/**
	 * @return the tITLE
	 */
	public String getTITLE()
	{
		return TITLE;
	}

	/**
	 * @return the tYPE
	 */
	public Long getTYPE()
	{
		return TYPE;
	}

	/**
	 * @return the wIDGET_CREATION_TIME
	 */
	public String getWIDGET_CREATION_TIME()
	{
		return WIDGET_CREATION_TIME;
	}

	/**
	 * @return the wIDGET_DESCRIPTION
	 */
	public String getWIDGET_DESCRIPTION()
	{
		return WIDGET_DESCRIPTION;
	}

	/**
	 * @return the wIDGET_GROUP_NAME
	 */
	public String getWIDGET_GROUP_NAME()
	{
		return WIDGET_GROUP_NAME;
	}

	/**
	 * @return the wIDGET_HISTOGRAM
	 */
	public String getWIDGET_HISTOGRAM()
	{
		return WIDGET_HISTOGRAM;
	}

	/**
	 * @return the wIDGET_ICON
	 */
	public String getWIDGET_ICON()
	{
		return WIDGET_ICON;
	}

	/**
	 * @return the wIDGET_KOC_NAME
	 */
	public String getWIDGET_KOC_NAME()
	{
		return WIDGET_KOC_NAME;
	}

	/**
	 * @return the wIDGET_NAME
	 */
	public String getWIDGET_NAME()
	{
		return WIDGET_NAME;
	}

	/**
	 * @return the wIDGET_OWNER
	 */
	public String getWIDGET_OWNER()
	{
		return WIDGET_OWNER;
	}

	/**
	 * @return the wIDGET_SOURCE
	 */
	public Long getWIDGET_SOURCE()
	{
		return WIDGET_SOURCE;
	}

	/**
	 * @return the wIDGET_SUPPORT_TIME_CONTROL
	 */
	public Integer getWIDGET_SUPPORT_TIME_CONTROL()
	{
		return WIDGET_SUPPORT_TIME_CONTROL;
	}

	/**
	 * @return the wIDGET_TEMPLATE
	 */
	public String getWIDGET_TEMPLATE()
	{
		return WIDGET_TEMPLATE;
	}

	/**
	 * @return the wIDGET_UNIQUE_ID
	 */
	public String getWIDGET_UNIQUE_ID()
	{
		return WIDGET_UNIQUE_ID;
	}

	/**
	 * @return the wIDGET_VIEWMODE
	 */
	public String getWIDGET_VIEWMODE()
	{
		return WIDGET_VIEWMODE;
	}

	/**
	 * @return the wIDTH
	 */
	public Long getWIDTH()
	{
		return WIDTH;
	}

	/**
	 * @param cREATION_DATE
	 *            the cREATION_DATE to set
	 */
	public void setCREATION_DATE(String cREATION_DATE)
	{
		CREATION_DATE = cREATION_DATE;
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
	 * @param hEIGHT
	 *            the hEIGHT to set
	 */
	public void setHEIGHT(Long hEIGHT)
	{
		HEIGHT = hEIGHT;
	}

	/**
	 * @param iS_MAXIMIZED
	 *            the iS_MAXIMIZED to set
	 */
	public void setIS_MAXIMIZED(Integer iS_MAXIMIZED)
	{
		IS_MAXIMIZED = iS_MAXIMIZED;
	}

	/**
	 * @param lAST_MODIFICATION_DATE
	 *            the lAST_MODIFICATION_DATE to set
	 */
	public void setLAST_MODIFICATION_DATE(String lAST_MODIFICATION_DATE)
	{
		LAST_MODIFICATION_DATE = lAST_MODIFICATION_DATE;
	}

	/**
	 * @param lAST_MODIFIED_BY
	 *            the lAST_MODIFIED_BY to set
	 */
	public void setLAST_MODIFIED_BY(String lAST_MODIFIED_BY)
	{
		LAST_MODIFIED_BY = lAST_MODIFIED_BY;
	}

	/**
	 * @param oWNER
	 *            the oWNER to set
	 */
	public void setOWNER(String oWNER)
	{
		OWNER = oWNER;
	}

	/**
	 * @param pOSITION
	 *            the pOSITION to set
	 */
	public void setPOSITION(Long pOSITION)
	{
		POSITION = pOSITION;
	}

	/**
	 * @param pROVIDER_ASSET_ROOT
	 *            the pROVIDER_ASSET_ROOT to set
	 */
	public void setPROVIDER_ASSET_ROOT(String pROVIDER_ASSET_ROOT)
	{
		PROVIDER_ASSET_ROOT = pROVIDER_ASSET_ROOT;
	}

	/**
	 * @param pROVIDER_NAME
	 *            the pROVIDER_NAME to set
	 */
	public void setPROVIDER_NAME(String pROVIDER_NAME)
	{
		PROVIDER_NAME = pROVIDER_NAME;
	}

	/**
	 * @param pROVIDER_VERSION
	 *            the pROVIDER_VERSION to set
	 */
	public void setPROVIDER_VERSION(String pROVIDER_VERSION)
	{
		PROVIDER_VERSION = pROVIDER_VERSION;
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
	 * @param tILE_COLUMN
	 *            the tILE_COLUMN to set
	 */
	public void setTILE_COLUMN(Long tILE_COLUMN)
	{
		TILE_COLUMN = tILE_COLUMN;
	}

	/**
	 * @param tILE_ID
	 *            the tILE_ID to set
	 */
	public void setTILE_ID(Long tILE_ID)
	{
		TILE_ID = tILE_ID;
	}

	/**
	 * @param tILE_ROW
	 *            the tILE_ROW to set
	 */
	public void setTILE_ROW(Long tILE_ROW)
	{
		TILE_ROW = tILE_ROW;
	}

	/**
	 * @param tITLE
	 *            the tITLE to set
	 */
	public void setTITLE(String tITLE)
	{
		TITLE = tITLE;
	}

	/**
	 * @param tYPE
	 *            the tYPE to set
	 */
	public void setTYPE(Long tYPE)
	{
		TYPE = tYPE;
	}

	/**
	 * @param wIDGET_CREATION_TIME
	 *            the wIDGET_CREATION_TIME to set
	 */
	public void setWIDGET_CREATION_TIME(String wIDGET_CREATION_TIME)
	{
		WIDGET_CREATION_TIME = wIDGET_CREATION_TIME;
	}

	/**
	 * @param wIDGET_DESCRIPTION
	 *            the wIDGET_DESCRIPTION to set
	 */
	public void setWIDGET_DESCRIPTION(String wIDGET_DESCRIPTION)
	{
		WIDGET_DESCRIPTION = wIDGET_DESCRIPTION;
	}

	/**
	 * @param wIDGET_GROUP_NAME
	 *            the wIDGET_GROUP_NAME to set
	 */
	public void setWIDGET_GROUP_NAME(String wIDGET_GROUP_NAME)
	{
		WIDGET_GROUP_NAME = wIDGET_GROUP_NAME;
	}

	/**
	 * @param wIDGET_HISTOGRAM
	 *            the wIDGET_HISTOGRAM to set
	 */
	public void setWIDGET_HISTOGRAM(String wIDGET_HISTOGRAM)
	{
		WIDGET_HISTOGRAM = wIDGET_HISTOGRAM;
	}

	/**
	 * @param wIDGET_ICON
	 *            the wIDGET_ICON to set
	 */
	public void setWIDGET_ICON(String wIDGET_ICON)
	{
		WIDGET_ICON = wIDGET_ICON;
	}

	/**
	 * @param wIDGET_KOC_NAME
	 *            the wIDGET_KOC_NAME to set
	 */
	public void setWIDGET_KOC_NAME(String wIDGET_KOC_NAME)
	{
		WIDGET_KOC_NAME = wIDGET_KOC_NAME;
	}

	/**
	 * @param wIDGET_NAME
	 *            the wIDGET_NAME to set
	 */
	public void setWIDGET_NAME(String wIDGET_NAME)
	{
		WIDGET_NAME = wIDGET_NAME;
	}

	/**
	 * @param wIDGET_OWNER
	 *            the wIDGET_OWNER to set
	 */
	public void setWIDGET_OWNER(String wIDGET_OWNER)
	{
		WIDGET_OWNER = wIDGET_OWNER;
	}

	/**
	 * @param wIDGET_SOURCE
	 *            the wIDGET_SOURCE to set
	 */
	public void setWIDGET_SOURCE(Long wIDGET_SOURCE)
	{
		WIDGET_SOURCE = wIDGET_SOURCE;
	}

	/**
	 * @param wIDGET_SUPPORT_TIME_CONTROL
	 *            the wIDGET_SUPPORT_TIME_CONTROL to set
	 */
	public void setWIDGET_SUPPORT_TIME_CONTROL(Integer wIDGET_SUPPORT_TIME_CONTROL)
	{
		WIDGET_SUPPORT_TIME_CONTROL = wIDGET_SUPPORT_TIME_CONTROL;
	}

	/**
	 * @param wIDGET_TEMPLATE
	 *            the wIDGET_TEMPLATE to set
	 */
	public void setWIDGET_TEMPLATE(String wIDGET_TEMPLATE)
	{
		WIDGET_TEMPLATE = wIDGET_TEMPLATE;
	}

	/**
	 * @param wIDGET_UNIQUE_ID
	 *            the wIDGET_UNIQUE_ID to set
	 */
	public void setWIDGET_UNIQUE_ID(String wIDGET_UNIQUE_ID)
	{
		WIDGET_UNIQUE_ID = wIDGET_UNIQUE_ID;
	}

	/**
	 * @param wIDGET_VIEWMODE
	 *            the wIDGET_VIEWMODE to set
	 */
	public void setWIDGET_VIEWMODE(String wIDGET_VIEWMODE)
	{
		WIDGET_VIEWMODE = wIDGET_VIEWMODE;
	}

	/**
	 * @param wIDTH
	 *            the wIDTH to set
	 */
	public void setWIDTH(Long wIDTH)
	{
		WIDTH = wIDTH;
	}
}
