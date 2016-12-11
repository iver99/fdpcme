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

import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author chehao
 *
 */
public class ElementTest
{
	public static Logger logger=Logger.getLogger(CacheUnitTest.class.getName());
	@Test(groups = { "s2" })
	public void testElementEquals(){
		Object key1=new Object();
		Object value1=new Object();
		Element e1=new Element(key1,value1);
		Assert.assertFalse(e1.equals(null));
		
		Object key2=null;
		Object value2=new Object();
		Element e2=new Element(key2,value2);
		Assert.assertFalse(e1.equals(e2));
	}
	
	@Test(groups = { "s2" })
	public void testElementHashCode(){
		Object key1=new Object();
		Object value1=new Object();
		Element e1=new Element(key1,value1);
		Assert.assertNotNull(e1.hashCode());
		
		Object key2=new Object();
		Object value2=new Object();
		Element e2=new Element(key2,value2);
		Assert.assertNotNull(e2.hashCode());
		
		Assert.assertNotEquals(e1.hashCode(), e2.hashCode());
	}
}
