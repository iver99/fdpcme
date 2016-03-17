/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import java.util.List;

/**
 * @author aduan
 */
public class RoleNamesEntity
{
	private List<String> roleNames;

	/**
	 * @return the roleNames
	 */
	public List<String> getRoleNames()
	{
		return roleNames;
	}

	/**
	 * @param roleNames
	 *            the roleNames to set
	 */
	public void setRoleNames(List<String> roleNames)
	{
		this.roleNames = roleNames;
	}
}
