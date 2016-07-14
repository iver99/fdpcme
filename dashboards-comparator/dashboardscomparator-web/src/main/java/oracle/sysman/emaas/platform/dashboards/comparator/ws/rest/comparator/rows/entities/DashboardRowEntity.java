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
public class DashboardRowEntity
{
	@JsonProperty("DASHBOARD_ID")
	private Long DASHBOARD_ID;

	@JsonProperty("NAME")
	private String NAME;

	@JsonProperty("TYPE")
	private Long TYPE;

	@JsonProperty("DESCRIPTION")
	private String DESCRIPTION;

	@JsonProperty("CREATION_DATE")
	private String CREATION_DATE;

	@JsonProperty("LAST_MODIFICATION_DATE")
	private String LAST_MODIFICATION_DATE;

	@JsonProperty("LAST_MODIFIED_BY")
	private String LAST_MODIFIED_BY;

	@JsonProperty("OWNER")
	private String OWNER;

	@JsonProperty("IS_SYSTEM")
	private Integer IS_SYSTEM;

	@JsonProperty("APPLICATION_TYPE")
	private Integer APPLICATION_TYPE;

	@JsonProperty("ENABLE_TIME_RANGE")
	private Integer ENABLE_TIME_RANGE;

	@JsonProperty("SCREEN_SHOT")
	private String SCREEN_SHOT;

	@JsonProperty("DELETED")
	private Long DELETED;

	@JsonProperty("TENANT_ID")
	private Long TENANT_ID;

	@JsonProperty("ENABLE_REFRESH")
	private Integer ENABLE_REFRESH;

	@JsonProperty("SHARE_PUBLIC")
	private Integer SHARE_PUBLIC;

	@JsonProperty("ENABLE_ENTITY_FILTER")
	private Integer ENABLE_ENTITY_FILTER;

	@JsonProperty("ENABLE_DESCRIPTION")
	private Integer ENABLE_DESCRIPTION;

	@JsonProperty("EXTENDED_OPTIONS")
	private String EXTENDED_OPTIONS;

	public DashboardRowEntity()
	{
	}

	/**
	 * @return the aPPLICATION_TYPE
	 */
	public Integer getAPPLICATION_TYPE()
	{
		return APPLICATION_TYPE;
	}

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
	 * @return the dELETED
	 */
	public Long getDELETED()
	{
		return DELETED;
	}

	/**
	 * @return the dESCRIPTION
	 */
	public String getDESCRIPTION()
	{
		return DESCRIPTION;
	}

	/**
	 * @return the eNABLE_DESCRIPTION
	 */
	public Integer getENABLE_DESCRIPTION()
	{
		return ENABLE_DESCRIPTION;
	}

	/**
	 * @return the eNABLE_ENTITY_FILTER
	 */
	public Integer getENABLE_ENTITY_FILTER()
	{
		return ENABLE_ENTITY_FILTER;
	}

	/**
	 * @return the eNABLE_REFRESH
	 */
	public Integer getENABLE_REFRESH()
	{
		return ENABLE_REFRESH;
	}

	/**
	 * @return the eNABLE_TIME_RANGE
	 */
	public Integer getENABLE_TIME_RANGE()
	{
		return ENABLE_TIME_RANGE;
	}

	/**
	 * @return the eXTENDED_OPTIONS
	 */
	public String getEXTENDED_OPTIONS()
	{
		return EXTENDED_OPTIONS;
	}

	/**
	 * @return the iS_SYSTEM
	 */
	public Integer getIS_SYSTEM()
	{
		return IS_SYSTEM;
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
	 * @return the nAME
	 */
	public String getNAME()
	{
		return NAME;
	}

	/**
	 * @return the oWNER
	 */
	public String getOWNER()
	{
		return OWNER;
	}

	/**
	 * @return the sCREEN_SHOT
	 */
	public String getSCREEN_SHOT()
	{
		return SCREEN_SHOT;
	}

	/**
	 * @return the sHARE_PUBLIC
	 */
	public Integer getSHARE_PUBLIC()
	{
		return SHARE_PUBLIC;
	}

	/**
	 * @return the tENANT_ID
	 */
	public Long getTENANT_ID()
	{
		return TENANT_ID;
	}

	/**
	 * @return the tYPE
	 */
	public Long getTYPE()
	{
		return TYPE;
	}

	/**
	 * @param aPPLICATION_TYPE
	 *            the aPPLICATION_TYPE to set
	 */
	public void setAPPLICATION_TYPE(Integer aPPLICATION_TYPE)
	{
		APPLICATION_TYPE = aPPLICATION_TYPE;
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
	 * @param dELETED
	 *            the dELETED to set
	 */
	public void setDELETED(Long dELETED)
	{
		DELETED = dELETED;
	}

	/**
	 * @param dESCRIPTION
	 *            the dESCRIPTION to set
	 */
	public void setDESCRIPTION(String dESCRIPTION)
	{
		DESCRIPTION = dESCRIPTION;
	}

	/**
	 * @param eNABLE_DESCRIPTION
	 *            the eNABLE_DESCRIPTION to set
	 */
	public void setENABLE_DESCRIPTION(Integer eNABLE_DESCRIPTION)
	{
		ENABLE_DESCRIPTION = eNABLE_DESCRIPTION;
	}

	/**
	 * @param eNABLE_ENTITY_FILTER
	 *            the eNABLE_ENTITY_FILTER to set
	 */
	public void setENABLE_ENTITY_FILTER(Integer eNABLE_ENTITY_FILTER)
	{
		ENABLE_ENTITY_FILTER = eNABLE_ENTITY_FILTER;
	}

	/**
	 * @param eNABLE_REFRESH
	 *            the eNABLE_REFRESH to set
	 */
	public void setENABLE_REFRESH(Integer eNABLE_REFRESH)
	{
		ENABLE_REFRESH = eNABLE_REFRESH;
	}

	/**
	 * @param eNABLE_TIME_RANGE
	 *            the eNABLE_TIME_RANGE to set
	 */
	public void setENABLE_TIME_RANGE(Integer eNABLE_TIME_RANGE)
	{
		ENABLE_TIME_RANGE = eNABLE_TIME_RANGE;
	}

	/**
	 * @param eXTENDED_OPTIONS
	 *            the eXTENDED_OPTIONS to set
	 */
	public void setEXTENDED_OPTIONS(String eXTENDED_OPTIONS)
	{
		EXTENDED_OPTIONS = eXTENDED_OPTIONS;
	}

	/**
	 * @param iS_SYSTEM
	 *            the iS_SYSTEM to set
	 */
	public void setIS_SYSTEM(Integer iS_SYSTEM)
	{
		IS_SYSTEM = iS_SYSTEM;
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
	 * @param nAME
	 *            the nAME to set
	 */
	public void setNAME(String nAME)
	{
		NAME = nAME;
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
	 * @param sCREEN_SHOT
	 *            the sCREEN_SHOT to set
	 */
	public void setSCREEN_SHOT(String sCREEN_SHOT)
	{
		SCREEN_SHOT = sCREEN_SHOT;
	}

	/**
	 * @param sHARE_PUBLIC
	 *            the sHARE_PUBLIC to set
	 */
	public void setSHARE_PUBLIC(Integer sHARE_PUBLIC)
	{
		SHARE_PUBLIC = sHARE_PUBLIC;
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
	 * @param tYPE
	 *            the tYPE to set
	 */
	public void setTYPE(Long tYPE)
	{
		TYPE = tYPE;
	}
}
