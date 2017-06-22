/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyReader
{
	private PropertyReader() {
	  }

	private static final Logger LOGGER = LogManager.getLogger(PropertyReader.class);
	
	public static final String DFUI_CONFIG = "dfuiconfig.properties";

	public static final String getInstallDir()
	{
		if (RUNNING_IN_CONTAINER) {
			return System.getProperty("user.dir") + File.separator + ".." + File.separator;
		}
		else {
			return System.getProperty("java.home") + File.separator + ".." + File.separator + "..";
		}
	}

	public static final boolean getRunningInContainer()
	{
		final String JNDI_INITIAL_CONTEXT = System.getProperty("java.naming.factory.initial");
		return JNDI_INITIAL_CONTEXT != null
				&& JNDI_INITIAL_CONTEXT.startsWith("weblogic")
				&& (System.getProperty("weblogic.home") != null || System.getProperty("wls.home") != null || System
						.getProperty("weblogic.management.startmode") != null);
	}

	public static Properties loadProperty(String filename) throws IOException
	{
		Properties prop = new Properties();
		InputStream input = null;
		try {

			if (SERVICE_PROPS.equals(filename)) {
				input = new FileInputStream(CONFIG_DIR + File.separator + filename);
			}
			else {
				input = PropertyReader.class.getResourceAsStream(filename);
			}
			
			prop.load(input);

		}
		catch (IOException ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
					LOGGER.error(e.getLocalizedMessage(), e);
				}
			}
		}

		return prop;

	}

	private static final boolean RUNNING_IN_CONTAINER = PropertyReader.getRunningInContainer();

	public static final String CONFIG_DIR = "/opt/ORCLemaas/Applications/DashboardService-UI/init";//getInstallDir() + "config";

	public static final String SERVICE_PROPS = "servicemanager.properties";
}
