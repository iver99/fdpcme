/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class StringCacheUtilTest
{
	@Test
	public void testRegistrationCache()
	{
		String key = "tenant.user";
		StringCacheUtil cacheUtil = StringCacheUtil.getRegistrationCacheInstance();
		Assert.assertNotNull(cacheUtil);
		cacheUtil.put(key, "{registrationData}");
		Assert.assertEquals(cacheUtil.get(key), "{registrationData}");
		cacheUtil.remove(key);
		Assert.assertNull(cacheUtil.get("tenant.user"));
	}
}
