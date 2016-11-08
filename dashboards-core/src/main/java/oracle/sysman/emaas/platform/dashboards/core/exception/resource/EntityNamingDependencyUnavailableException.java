/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.core.exception.resource;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

/**
 * @author guochen
 *
 */
public class EntityNamingDependencyUnavailableException extends DashboardException
{
	private static final String ENTITY_NAMING_DEPENDENCY_UNAVAILABLE = "ENTITY_NAMING_DEPENDENCY_UNAVAILABLE";
	private static final long serialVersionUID = -6307245826904140727L;
	
	/**
	 * @param errorCode
	 * @param message
	 */
	public EntityNamingDependencyUnavailableException()
	{
		super(DashboardErrorConstants.ENTITY_NAMING_DEPENDENCY_UNAVAILABLE_ERROR_CODE, MessageUtils
				.getDefaultBundleString(ENTITY_NAMING_DEPENDENCY_UNAVAILABLE));
	}

}
