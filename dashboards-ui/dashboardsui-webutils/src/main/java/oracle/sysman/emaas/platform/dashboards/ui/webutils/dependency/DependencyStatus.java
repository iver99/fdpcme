/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ui.webutils.dependency;

public class DependencyStatus
{
	private Boolean entityNamingUp;
	
	private static final DependencyStatus INSTANCE = new DependencyStatus();
	
	private DependencyStatus() {
		entityNamingUp = Boolean.TRUE;
	}

	public static DependencyStatus getInstance() {
		return INSTANCE;
	}

	/**
	 * @return the entityNamingUp
	 */
	public Boolean isEntityNamingUp()
	{
		return entityNamingUp;
	}

	/**
	 * @param entityNamingUp the entityNamingUp to set
	 */
	public void setEntityNamingUp(Boolean entityNamingUp)
	{
		this.entityNamingUp = entityNamingUp;
	}
}
