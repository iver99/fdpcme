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
public interface EMTargetMXBean
{

	/**
	 * Gets the EM target type.
	 * 
	 * @exception Exception
	 *                if the operation fails
	 */
	public String getEMTargetType();

	/**
	 * Gets the EM target name.
	 * 
	 * @exception Exception
	 *                if the operation fails
	 */
	public String getName();

}
