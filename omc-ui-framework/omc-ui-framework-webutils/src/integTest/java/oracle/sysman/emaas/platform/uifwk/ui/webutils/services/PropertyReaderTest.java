/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.services;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class PropertyReaderTest
{

private static final Logger LOGGER = LogManager.getLogger(PropertyReaderTest.class);
	@Test(groups = { "s1" })
	public void testGetInstallDir()
	{
		Assert.assertNotNull(PropertyReader.getInstallDir());
	}

	@Test(groups = { "s1" })
	public void testGetRunningInContainer()
	{
		Assert.assertFalse(PropertyReader.getRunningInContainer());
	}

	@Test(groups = { "s1" })
	public void testLoadProperty()
	{
		Properties prop = null;
		try {
			prop = PropertyReader.loadProperty("");
		}
		catch (IOException e) {
			LOGGER.info("context",e);
			e.printStackTrace();
		}
		Assert.assertNotNull(prop);
	}
}
