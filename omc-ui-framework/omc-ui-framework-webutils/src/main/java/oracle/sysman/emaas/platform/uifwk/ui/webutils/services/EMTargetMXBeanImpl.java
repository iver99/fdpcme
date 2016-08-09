/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.services;

/**
 * @author vinjoshi
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
	public String getEMTargetType() throws Exception
	{
		return M_TARGET_TYPE;
	}

	@Override
	public String getName() throws Exception
	{
		return m_name;
	}

}
