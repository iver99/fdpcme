/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.target.services;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author vinjoshi
 */
public class GlobalStatus
{
	private GlobalStatus() {
	  }

	public static final String STATUS_DOWN = "DOWN";
	public static final String STATUS_UP = "UP";
	public static final String STATUS_OUT_OF_SERVICE = "OUT_OF_SERVICE";
	private final static boolean OMC_UI_UP = true;
	private final static boolean OMC_UI_DOWN = false;
	private static AtomicBoolean omcStatus = new AtomicBoolean(OMC_UI_UP);

	public static boolean isOmcUiUp()
	{
		return omcStatus.get();
	}

	public static void setOmcUiDownStatus()
	{
		omcStatus.set(OMC_UI_DOWN);
	}

	public static void setOmcUiUpStatus()
	{
		omcStatus.set(OMC_UI_UP);
	}

}
