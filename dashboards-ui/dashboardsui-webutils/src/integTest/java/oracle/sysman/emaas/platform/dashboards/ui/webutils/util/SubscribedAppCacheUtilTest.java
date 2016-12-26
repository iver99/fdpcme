/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.registration.StringCacheUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author chehao
 *
 */
public class SubscribedAppCacheUtilTest
{
	@Test(groups = { "s2" })
	public void testGet(){
		StringCacheUtil cache=StringCacheUtil.getRegistrationCacheInstance();
		String key="";
		Assert.assertEquals(cache.get(key), null);
		
		String key1="key1";
		Assert.assertEquals(cache.get(key1), null);
		
		String key2="key2";
		String value="value";
		cache.put(key2, value);
		
		Assert.assertEquals(cache.get(key2), value);
	}
	@Test(groups = { "s2" })
	public void testPut(){
		StringCacheUtil cache=StringCacheUtil.getUserInfoCacheInstance();
		String key1="";
		String value1="value1";
		Assert.assertEquals(cache.put(key1, value1), value1);
		
		String key2="key2";
		String value2="";
		Assert.assertEquals(cache.put(key2, value2), value2);
		
		String key3="key3";
		String value3="value3";
		Assert.assertEquals(cache.put(key3, value3), value3);
	}
	
	@Test(groups = { "s2" })
	public void testRemove(){
		StringCacheUtil cache=StringCacheUtil.getUserInfoCacheInstance();
		String key1="";
		Assert.assertEquals(cache.remove(key1), null);
		
		String key2="key2";
		String value2="value2";
		cache.put(key2, value2);
		cache.remove("notExisted");
		cache.remove(key2);
	}

}
