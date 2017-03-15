/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class PaginatedDashboardsTest
{
	@Test(groups = { "s1" })
	public void testPaginatedDashboards()
	{
		PaginatedDashboards pd = new PaginatedDashboards(0L, 0, 0, 50, new ArrayList<Dashboard>());
		pd.setCount(0);
		pd.setDashboards(new ArrayList<Dashboard>());
		pd.setLimit(0);
		pd.setOffset(0);
		pd.setTotalResults(0);
		Assert.assertNotNull(pd.getCount());
		Assert.assertNotNull(pd.getDashboards());
		Assert.assertNotNull(pd.getLimit());
		Assert.assertNotNull(pd.getOffset());
		Assert.assertNotNull(pd.getTotalResults());
	}
}
