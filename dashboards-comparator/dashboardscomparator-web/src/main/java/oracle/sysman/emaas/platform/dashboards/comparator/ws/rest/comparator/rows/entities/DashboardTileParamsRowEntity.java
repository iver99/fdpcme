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
public class DashboardTileParamsRowEntity
{
	@JsonProperty("TILE_ID")
	private Long TILE_ID;

	@JsonProperty("PARAM_NAME")
	private String PARAM_NAME;

	@JsonProperty("TENANT_ID")
	private Long TENANT_ID;

	@JsonProperty("IS_SYSTEM")
	private Integer IS_SYSTEM;

	@JsonProperty("PARAM_TYPE")
	private Long PARAM_TYPE;

	@JsonProperty("PARAM_VALUE_STR")
	private String PARAM_VALUE_STR;

	@JsonProperty("PARAM_VALUE_NUM")
	private Long PARAM_VALUE_NUM;

	@JsonProperty("PARAM_VALUE_TIMESTAMP")
	private String PARAM_VALUE_TIMESTAMP;

	/**
	 * @return the iS_SYSTEM
	 */
	public Integer getIS_SYSTEM()
	{
		return IS_SYSTEM;
	}

	/**
	 * @return the pARAM_NAME
	 */
	public String getPARAM_NAME()
	{
		return PARAM_NAME;
	}

	/**
	 * @return the pARAM_TYPE
	 */
	public Long getPARAM_TYPE()
	{
		return PARAM_TYPE;
	}

	/**
	 * @return the pARAM_VALUE_NUM
	 */
	public Long getPARAM_VALUE_NUM()
	{
		return PARAM_VALUE_NUM;
	}

	/**
	 * @return the pARAM_VALUE_STR
	 */
	public String getPARAM_VALUE_STR()
	{
		return PARAM_VALUE_STR;
	}

	/**
	 * @return the pARAM_VALUE_TIMESTAMP
	 */
	public String getPARAM_VALUE_TIMESTAMP()
	{
		return PARAM_VALUE_TIMESTAMP;
	}

	/**
	 * @return the tENANT_ID
	 */
	public Long getTENANT_ID()
	{
		return TENANT_ID;
	}

	/**
	 * @return the tILE_ID
	 */
	public Long getTILE_ID()
	{
		return TILE_ID;
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
	 * @param pARAM_NAME
	 *            the pARAM_NAME to set
	 */
	public void setPARAM_NAME(String pARAM_NAME)
	{
		PARAM_NAME = pARAM_NAME;
	}

	/**
	 * @param pARAM_TYPE
	 *            the pARAM_TYPE to set
	 */
	public void setPARAM_TYPE(Long pARAM_TYPE)
	{
		PARAM_TYPE = pARAM_TYPE;
	}

	/**
	 * @param pARAM_VALUE_NUM
	 *            the pARAM_VALUE_NUM to set
	 */
	public void setPARAM_VALUE_NUM(Long pARAM_VALUE_NUM)
	{
		PARAM_VALUE_NUM = pARAM_VALUE_NUM;
	}

	/**
	 * @param pARAM_VALUE_STR
	 *            the pARAM_VALUE_STR to set
	 */
	public void setPARAM_VALUE_STR(String pARAM_VALUE_STR)
	{
		PARAM_VALUE_STR = pARAM_VALUE_STR;
	}

	/**
	 * @param pARAM_VALUE_TIMESTAMP
	 *            the pARAM_VALUE_TIMESTAMP to set
	 */
	public void setPARAM_VALUE_TIMESTAMP(String pARAM_VALUE_TIMESTAMP)
	{
		PARAM_VALUE_TIMESTAMP = pARAM_VALUE_TIMESTAMP;
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
	 * @param tILE_ID
	 *            the tILE_ID to set
	 */
	public void setTILE_ID(Long tILE_ID)
	{
		TILE_ID = tILE_ID;
	}
}
