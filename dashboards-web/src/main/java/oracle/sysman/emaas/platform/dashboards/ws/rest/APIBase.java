/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emaas.platform.dashboards.ws.rest.util.JsonUtil;

/**
 * @author wenjzhu
 */
public class APIBase
{
	private final JsonUtil jsonUtil = JsonUtil.buildNonNullMapper();

	public JsonUtil getJsonUtil()
	{
		return jsonUtil;
	}

	public String getTenantId()
	{
		return "test";
	}

}
