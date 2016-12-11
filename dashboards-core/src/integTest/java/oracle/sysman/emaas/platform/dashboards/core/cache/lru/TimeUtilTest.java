/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.core.cache.lru;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author chehao
 *
 */
public class TimeUtilTest
{

	@Test(groups = { "s2" })
	public void testToSec(){
		Assert.assertEquals(TimeUtil.toSecs(1000), 1);
	}
	@Test(groups = { "s2" })
	public void testConvertTimeToInt(){
		Assert.assertEquals(TimeUtil.convertTimeToInt(2147483648L), 2147483647L);
		Assert.assertEquals(TimeUtil.convertTimeToInt(2147483646L), 2147483646);
	}
	@Test(groups = { "s2" })
	public void testToMillis(){
		Assert.assertEquals(TimeUtil.toMillis(1),1000L);
	}
}
