/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.services;

/**
 * @author vinjoshi
 */

/**
 * EMTargetMXBeanImpl is the implementation for EMTargetMXBean.
 */
public class EMTargetMXBeanImpl implements EMTargetMXBean
{

	private static final String M_TARGET_TYPE = EMTargetConstants.M_TARGET_TYPE;
	private String m_name = null;

	public EMTargetMXBeanImpl(String name)
	{
		m_name = name;
	}

	@Override
	public String getEMTargetType() 
	{
		return M_TARGET_TYPE;
	}

	@Override
	public String getName() 
	{
		return m_name;
	}
}
