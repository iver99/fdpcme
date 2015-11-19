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

/**
 * @author vinjoshi
 */
public class OmcUiStatus implements OmcUiStatusMBean
{

	@Override
	public String getStatus()
	{
		if (!GlobalStatus.isOmcUiUp()) {
			return GlobalStatus.STATUS_DOWN;
		}

		else if (GlobalStatus.isOmcUiUp()) {

			return GlobalStatus.STATUS_UP;
		}
		else {
			return GlobalStatus.STATUS_OUT_OF_SERVICE;
		}

	}

	@Override
	public String getStatusMsg()
	{

		if (!GlobalStatus.isOmcUiUp()) {
			return "OMC-UI is being stopped.";
		}

		else if (GlobalStatus.isOmcUiUp()) {
			return "OMC-UI is up and running.";
		}
		else {
			return "OMC-UI is out of service.";
		}

	}

}
