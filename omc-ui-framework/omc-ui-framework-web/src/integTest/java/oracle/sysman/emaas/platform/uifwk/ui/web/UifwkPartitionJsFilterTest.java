/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.web;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.util.DataFetcher;

import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class UifwkPartitionJsFilterTest
{
	@Test(groups = { "s2" })
	public void testDoFilter(@Mocked final FilterChain chain, @Mocked final HttpServletRequest httpReq,
			@Mocked final HttpServletResponse response, @Mocked final DataFetcher dataFetcher) throws Exception
	{
		final UifwkPartitionJsFilter filter = new UifwkPartitionJsFilter();
		new Expectations() {
			{
				httpReq.getHeader(anyString);
				result = "tenant.user";
				DataFetcher.getRegistrationData(anyString, anyString, anyString, anyString);
				result = "{registration data}";
			}
		};
		filter.doFilter(httpReq, response, chain);
	}
}
