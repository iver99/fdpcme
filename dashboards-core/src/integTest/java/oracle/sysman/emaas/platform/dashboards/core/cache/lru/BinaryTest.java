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

import org.testng.annotations.Test;

/**
 * @author chehao
 *
 */
public class BinaryTest
{
	@Test
	public void testBinary(){
		byte[] b=new byte[15];
		Binary binary=new Binary(b);
		binary.getData();
		binary.setData(b);
	}
}
