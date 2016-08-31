/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.services;

/**
 * @author vinjoshi
 *
 */
/**
 * EMTargetMXBeanImpl is the implementation for EMTargetMXBean.
 */
public class EMTargetMXBeanImpl implements EMTargetMXBean
{

	private static final String m_target_type = EMTargetConstants.M_TARGET_TYPE;
	private String m_name = null;

	public EMTargetMXBeanImpl(String name)
	{
		m_name = name;
	}

	@Override
	public String getEMTargetType() throws Exception
	{
		return m_target_type;
	}

	@Override
	public String getName() throws Exception
	{
		return m_name;
	}
}
