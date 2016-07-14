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
public class PreferenceRowEntity
{
	@JsonProperty("USER_NAME")
	private String USER_NAME;

	@JsonProperty("PREF_KEY")
	private String PREF_KEY;

	@JsonProperty("PREF_VALUE")
	private String PREF_VALUE;

	@JsonProperty("TENANT_ID")
	private Long TENANT_ID;

	/**
	 * @return the pREF_KEY
	 */
	public String getPREF_KEY()
	{
		return PREF_KEY;
	}

	/**
	 * @return the pREF_VALUE
	 */
	public String getPREF_VALUE()
	{
		return PREF_VALUE;
	}

	/**
	 * @return the tENANT_ID
	 */
	public Long getTENANT_ID()
	{
		return TENANT_ID;
	}

	/**
	 * @return the uSER_NAME
	 */
	public String getUSER_NAME()
	{
		return USER_NAME;
	}

	/**
	 * @param pREF_KEY
	 *            the pREF_KEY to set
	 */
	public void setPREF_KEY(String pREF_KEY)
	{
		PREF_KEY = pREF_KEY;
	}

	/**
	 * @param pREF_VALUE
	 *            the pREF_VALUE to set
	 */
	public void setPREF_VALUE(String pREF_VALUE)
	{
		PREF_VALUE = pREF_VALUE;
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
	 * @param uSER_NAME
	 *            the uSER_NAME to set
	 */
	public void setUSER_NAME(String uSER_NAME)
	{
		USER_NAME = uSER_NAME;
	}
}
